package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.demo.dto.NotePublishedRequestDTO;
import com.example.demo.dto.AbsenceRecordedRequestDTO;
import com.example.demo.dto.ModuleAssignedRequestDTO;

@FeignClient(name = "A6-NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/notifications/note-published")
    void notifyNotePublished(@RequestBody NotePublishedRequestDTO req);

    @PostMapping("/notifications/absence-recorded")
    void notifyAbsenceRecorded(@RequestBody AbsenceRecordedRequestDTO req);
    
    @PostMapping("/notifications/module-assigned")
    void notifyModuleAssigned(@RequestBody ModuleAssignedRequestDTO req);
}