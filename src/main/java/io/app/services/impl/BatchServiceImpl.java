package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Teacher;
import io.app.repository.BatchRepository;
import io.app.repository.TeacherRepository;
import io.app.services.BatchService;
import io.app.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {
    private final BatchRepository repository;
    private final JwtService jwtService;
    private final TeacherRepository teacherRepository;

    @Override
    public ApiResponse createBatch(String authToken, BatchDto batchDto) {
        String userName=extractJwt(authToken);
        Teacher teacher= teacherRepository.findByPhone(userName)
                .orElseThrow(()->new ResourceNotFoundException("Invalid User credentials"));

        return null;
    }

    @Override
    public String extractJwt(String authToken) {
        authToken=authToken.substring(7);
        return jwtService.extractUsername(authToken);
    }
}
