package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Batch;
import io.app.model.Days;
import io.app.model.Teacher;
import io.app.repository.*;
import io.app.services.BatchService;
import io.app.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
    private final BatchRepository repository;
    private final JwtService jwtService;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse createBatch(String authToken, BatchDto batchDto) {
        String userName=extractJwt(authToken);
        Teacher teacher= teacherRepository.findByPhone(userName)
                .orElseThrow(()->new ResourceNotFoundException("Invalid User credentials"));
        if (repository.existsByNameAndTeacher(batchDto.getName(),teacher)){
            throw new DuplicateFoundException("The batch is already exists");
        }
        if (hasTimeConflict(teacher,batchDto.getStartTime(),batchDto.getEndTime(),batchDto.getDays())){
            throw new DuplicateFoundException("Teacher already has a batch scheduled between this time.");
        }


        if (!teacher.getBoards().stream().anyMatch((board)->board.getId().equals(batchDto.getBoard().getId()))){
            throw new ResourceNotFoundException("Board is not valid");
        }
        if(!teacher.getLanguages().stream().anyMatch((data)->data.getId().equals(batchDto.getLanguage().getId()))){
            throw new ResourceNotFoundException("Invalid Language");
        }

        if (batchDto.getSubjects()!=null && !teacher.getSubjects().containsAll(batchDto.getSubjects())){
            throw new ResourceNotFoundException("Invalid Subjects");
        }

        if (batchDto.getClasses()!=null && !teacher.getClasses().containsAll(batchDto.getClasses())){
            throw new ResourceNotFoundException("Invalid Classes");
        }

        Batch batch=modelMapper.map(batchDto,Batch.class);
        batch.setTeacher(teacher);

        repository.save(batch);

        return ApiResponse.builder()
                .status(true)
                .message("Batch Created Successfully")
                .build();
    }

    @Override
    public List<BatchDto> getAllBatch(String authToken) {
        String mobileNumber=extractJwt(authToken);
        Teacher teacher=teacherRepository.findByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        List<Batch> batches=repository.findByTeacher(teacher);
        List<BatchDto> result=batches.stream()
                .map((data)->modelMapper.map(data,BatchDto.class))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean hasTimeConflict(Teacher teacher,
                                   LocalTime newStartTime,
                                   LocalTime newEndTime,
                                   Set<Days> days) {
        List<Batch> batches = repository.findByTeacher(teacher);
        log.info("{}", teacher);

        for (Batch existingBatch : batches) {
            if (existingBatch.getDays().stream().anyMatch(days::contains)) {
                LocalTime existingStartTime = existingBatch.getStartTime();
                LocalTime existingEndTime = existingBatch.getEndTime();

                // Check for overlap
                if (newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public String extractJwt(String authToken) {
        authToken=authToken.substring(7);
        return jwtService.extractUsername(authToken);
    }
}
