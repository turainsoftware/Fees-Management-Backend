package io.app.dto;

import io.app.model.Batch;
import io.app.model.Subject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NoteDto {
    private long id;
    private String title;
    private String description;
    private Subject subject;
    private String fileName;
    private Date createdAt;
    private Date updatedAt;
}
