package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.NoteDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NoteService {
    public ApiResponse createNotes(String title, String description, long batchId, long subjectId, MultipartFile notePdf) throws IOException;
    public List<NoteDto> notesByBatchId(long batchId);
    public ApiResponse deleteNoteById(long id);
}
