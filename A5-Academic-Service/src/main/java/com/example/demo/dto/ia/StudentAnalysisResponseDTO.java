package com.example.demo.dto.ia;

import java.util.List;

public class StudentAnalysisResponseDTO {
    private Long studentId;
    private String riskLevel;
    private Double riskScore;
    private Double moyenneGenerale;
    private Integer totalAbsences;
    private Integer absencesNonJustifiees;
    private String performanceTrend;
    private List<IndicatorDTO> indicators;
    private List<RecommendationDTO> recommendations;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long v) { studentId = v; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String v) { riskLevel = v; }
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double v) { riskScore = v; }
    public Double getMoyenneGenerale() { return moyenneGenerale; }
    public void setMoyenneGenerale(Double v) { moyenneGenerale = v; }
    public Integer getTotalAbsences() { return totalAbsences; }
    public void setTotalAbsences(Integer v) { totalAbsences = v; }
    public Integer getAbsencesNonJustifiees() { return absencesNonJustifiees; }
    public void setAbsencesNonJustifiees(Integer v) { absencesNonJustifiees = v; }
    public String getPerformanceTrend() { return performanceTrend; }
    public void setPerformanceTrend(String v) { performanceTrend = v; }
    public List<IndicatorDTO> getIndicators() { return indicators; }
    public void setIndicators(List<IndicatorDTO> v) { indicators = v; }
    public List<RecommendationDTO> getRecommendations() { return recommendations; }
    public void setRecommendations(List<RecommendationDTO> v) { recommendations = v; }

    public static class IndicatorDTO {
        private String key, label, severity;
        private Double value;
        public String getKey() { return key; } public void setKey(String v) { key = v; }
        public String getLabel() { return label; } public void setLabel(String v) { label = v; }
        public Double getValue() { return value; } public void setValue(Double v) { value = v; }
        public String getSeverity() { return severity; } public void setSeverity(String v) { severity = v; }
    }

    public static class RecommendationDTO {
        private String type, message, priority;
        public String getType() { return type; } public void setType(String v) { type = v; }
        public String getMessage() { return message; } public void setMessage(String v) { message = v; }
        public String getPriority() { return priority; } public void setPriority(String v) { priority = v; }
    }
}