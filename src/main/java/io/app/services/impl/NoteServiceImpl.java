package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.NoteDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Batch;
import io.app.model.Note;
import io.app.model.Subject;
import io.app.repository.BatchRepository;
import io.app.repository.NoteRepository;
import io.app.repository.SubjectRepository;
import io.app.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {
    private final NoteRepository repository;
    private final BatchRepository batchRepository;
    private final SubjectRepository subjectRepository;
    private final FileServiceImpl fileService;



    @Override
    public ApiResponse createNotes(String title, String description,
                                   long batchId, long subjectId,
                                   MultipartFile notePdf) throws IOException {
        if(title.isEmpty()){
            throw new ResourceNotFoundException("Please Provide an title");
        }
        if (!batchRepository.existsById(batchId)){
            throw new ResourceNotFoundException("Invalid Batch Details");
        }
        if (!subjectRepository.existsById(subjectId)){
            throw new ResourceNotFoundException("Invalid Subject Details");
        }

        String noteFileName=fileService.uploadNote(notePdf);
        Batch batch=Batch.builder()
                .id(batchId)
                .build();
        Subject subject= Subject.builder()
                .id(subjectId)
                .build();

        Note note=Note.builder()
                .title(title)
                .description(description)
                .batch(batch)
                .subject(subject)
                .fileName(noteFileName)
                .build();

        repository.save(note);

        return ApiResponse.builder()
                .status(true)
                .message("Note added successfully")
                .build();
    }

    @Override
    public List<NoteDto> notesByBatchId(long batchId) {
        if(!batchRepository.existsById(batchId)){
            throw new ResourceNotFoundException("Invalid Batch Details");
        }
        Batch batch=Batch.builder()
                .id(batchId)
                .build();
        List<Note> notes=repository.findByBatch(batch);
        System.out.println(notes.size());
        List<NoteDto> results=notes.stream().map(note->noteToNoteDto(note))
                .collect(Collectors.toList());
        return results;
    }

    @Override
    public ApiResponse deleteNoteById(long id) {
        boolean isNoteExist=repository.existsById(id);
        if(!isNoteExist){
            throw new ResourceNotFoundException("Invalid Note Details");
        }

        return null;
    }


    private NoteDto noteToNoteDto(Note note){
        NoteDto noteDto=NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .description(note.getDescription())
                .subject(note.getSubject())
                .fileName(note.getFileName())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
        return noteDto;
    }




}
