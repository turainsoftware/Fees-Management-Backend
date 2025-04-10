package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.NoteDto;
import io.app.services.impl.NoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/note")
@RequiredArgsConstructor
public class NoteController {
    private final NoteServiceImpl service;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createNote(@RequestParam("title") String title,
                                  @RequestParam("desc") String description,
                                  @RequestParam("batchId") long batchId,
                                  @RequestParam("subjectId") long subjectId,
                                  @RequestParam("note") MultipartFile note) throws IOException {
        return service.createNotes(title,description,batchId,subjectId,note);
    }


    @GetMapping("/batch/{id}")
    public ResponseEntity<List<NoteDto>> notesByBatchId(@PathVariable("id") long batchId){
        return ResponseEntity.ok(service.notesByBatchId(batchId));
    }

}
