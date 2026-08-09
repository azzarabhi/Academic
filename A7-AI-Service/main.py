from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional

app = FastAPI(title="AcadémIA - AI Service", version="1.0.0")

# Permet au frontend/backend d'appeler ce service sans erreur CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# ================= SCHÉMAS (structure des données échangées) =================

class NoteIn(BaseModel):
    moduleId: int
    partie: Optional[int] = 1
    noteCC: Optional[float] = 0
    noteTP: Optional[float] = 0
    noteExam: Optional[float] = 0
    moyenne: Optional[float] = 0
    coefficient: Optional[float] = 1

class AbsenceIn(BaseModel):
    moduleId: int
    date: Optional[str] = None
    justifiee: bool = False

class StudentAnalysisRequest(BaseModel):
    studentId: int
    studentName: Optional[str] = ""
    notes: List[NoteIn] = []
    absences: List[AbsenceIn] = []

class Indicator(BaseModel):
    key: str
    label: str
    value: float
    severity: str  # "ok" | "warning" | "danger"

class Recommendation(BaseModel):
    type: str
    message: str
    priority: str  # "low" | "medium" | "high"

class StudentAnalysisResponse(BaseModel):
    studentId: int
    riskLevel: str            # "faible" | "moyen" | "eleve"
    riskScore: float          # 0-100
    moyenneGenerale: float
    totalAbsences: int
    absencesNonJustifiees: int
    performanceTrend: str     # "hausse" | "stable" | "baisse"
    indicators: List[Indicator]
    recommendations: List[Recommendation]


# ================= LOGIQUE D'ANALYSE (les "règles" de l'IA) =================

def calculer_moyenne_generale(notes: List[NoteIn]) -> float:
    """Moyenne pondérée par coefficient de chaque module."""
    if not notes:
        return 0.0
    total_pond = sum((n.moyenne or 0) * (n.coefficient or 1) for n in notes)
    total_coef = sum((n.coefficient or 1) for n in notes)
    if total_coef == 0:
        return 0.0
    return round(total_pond / total_coef, 2)


def calculer_tendance(notes: List[NoteIn]) -> str:
    """Compare la Partielle 1 et la Partielle 2 pour voir si le niveau baisse."""
    p1 = [n.moyenne or 0 for n in notes if (n.partie or 1) == 1]
    p2 = [n.moyenne or 0 for n in notes if (n.partie or 1) == 2]
    if not p1 or not p2:
        return "stable"
    moy1 = sum(p1) / len(p1)
    moy2 = sum(p2) / len(p2)
    diff = moy2 - moy1
    if diff <= -2:
        return "baisse"
    if diff >= 2:
        return "hausse"
    return "stable"


def analyser_etudiant(req: StudentAnalysisRequest) -> StudentAnalysisResponse:
    notes = req.notes
    absences = req.absences

    moyenne_generale = calculer_moyenne_generale(notes)
    total_absences = len(absences)
    absences_non_just = sum(1 for a in absences if not a.justifiee)
    tendance = calculer_tendance(notes)

    indicators: List[Indicator] = []
    recommendations: List[Recommendation] = []
    risk_score = 0.0

    # ---- Règle 1 : Moyenne générale ----
    if moyenne_generale < 8:
        sev = "danger"; risk_score += 40
    elif moyenne_generale < 10:
        sev = "warning"; risk_score += 20
    else:
        sev = "ok"
    indicators.append(Indicator(
        key="moyenne", label="Moyenne générale", value=moyenne_generale, severity=sev
    ))

    # ---- Règle 2 : Absences excessives ----
    if total_absences >= 8:
        sev = "danger"; risk_score += 30
        recommendations.append(Recommendation(
            type="absence",
            message="Nombre d'absences très élevé — convoquer l'étudiant et informer les parents.",
            priority="high"
        ))
    elif total_absences >= 4:
        sev = "warning"; risk_score += 15
        recommendations.append(Recommendation(
            type="absence",
            message="Absences en augmentation — surveiller l'assiduité de près.",
            priority="medium"
        ))
    else:
        sev = "ok"
    indicators.append(Indicator(
        key="absences", label="Absences totales", value=float(total_absences), severity=sev
    ))

    # ---- Règle 3 : Absences non justifiées ----
    if absences_non_just >= 3:
        sev = "danger"; risk_score += 15
        recommendations.append(Recommendation(
            type="absence_non_justifiee",
            message=f"{absences_non_just} absence(s) non justifiée(s) — demander une régularisation.",
            priority="high"
        ))
    elif absences_non_just >= 1:
        sev = "warning"; risk_score += 5
    else:
        sev = "ok"
    indicators.append(Indicator(
        key="absences_non_justifiees", label="Absences non justifiées",
        value=float(absences_non_just), severity=sev
    ))

    # ---- Règle 4 : Tendance de performance ----
    if tendance == "baisse":
        sev = "danger"; risk_score += 15
        recommendations.append(Recommendation(
            type="performance",
            message="Baisse de performance entre Partielle 1 et Partielle 2 — proposer un soutien pédagogique.",
            priority="high"
        ))
    elif tendance == "hausse":
        sev = "ok"
        recommendations.append(Recommendation(
            type="performance",
            message="Progression positive détectée — encourager l'étudiant à maintenir ce rythme.",
            priority="low"
        ))
    else:
        sev = "ok"
    indicators.append(Indicator(key="tendance", label="Tendance de performance", value=0, severity=sev))

    # ---- Niveau de risque global (somme des règles) ----
    risk_score = min(risk_score, 100)
    if risk_score >= 50:
        risk_level = "eleve"
        if not any(r.priority == "high" for r in recommendations):
            recommendations.append(Recommendation(
                type="global",
                message="Étudiant à risque élevé — un suivi individualisé est recommandé.",
                priority="high"
            ))
    elif risk_score >= 25:
        risk_level = "moyen"
    else:
        risk_level = "faible"

    if not recommendations:
        recommendations.append(Recommendation(
            type="global",
            message="Aucun signal préoccupant détecté — continuer le suivi habituel.",
            priority="low"
        ))

    return StudentAnalysisResponse(
        studentId=req.studentId,
        riskLevel=risk_level,
        riskScore=round(risk_score, 1),
        moyenneGenerale=moyenne_generale,
        totalAbsences=total_absences,
        absencesNonJustifiees=absences_non_just,
        performanceTrend=tendance,
        indicators=indicators,
        recommendations=recommendations
    )


# ================= ENDPOINTS (les URLs que les autres services appellent) =================

@app.get("/ai/health")
def health():
    return {"status": "UP", "service": "AI Service"}


@app.post("/ai/analyze/student", response_model=StudentAnalysisResponse)
def analyze_student(req: StudentAnalysisRequest):
    return analyser_etudiant(req)