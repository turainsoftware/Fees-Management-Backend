package io.app.services.impl;

import io.app.dto.TeacherFeesHistoryDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Teacher;
import io.app.repository.FeesHistoryRepository;
import io.app.repository.TeacherRepository;
import io.app.services.FeesHistoryService;
import io.app.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeesHistoryServiceImpl implements FeesHistoryService {

    private final FeesHistoryRepository repository;
    private final JwtService jwtService;
    private final TeacherRepository teacherRepository;

    @Override
    public List<TeacherFeesHistoryDto> feesByTeacher(String authToken) {
        String phone=getMobileByToken(authToken);
        boolean isTeacherExist=teacherRepository.existsByPhone(phone);
        if (!isTeacherExist){
            throw new ResourceNotFoundException("Teacher is not registered");
        }

        List<TeacherFeesHistoryDto> result=repository.findFeesHistoryByTeacherPhone(phone);

        return result;
    }

    @Override
    public List<TeacherFeesHistoryDto> latestFeesByTeacher(String authToken) {
        String phoneNumber=getMobileByToken(authToken);
        boolean isTeacherExist=teacherRepository.existsByPhone(phoneNumber);
        if (!isTeacherExist){
            throw new ResourceNotFoundException("Invalid Teacher Credentials");
        }


        Pageable pageable= PageRequest.of(  0,10);

        List<TeacherFeesHistoryDto> result=repository.find10FeesHistoryByTeacherPhone(phoneNumber,pageable);

        return result;
    }

    @Override
    public List<TeacherFeesHistoryDto> feesByTeacherInRange(String authToken, int pageNo, int size) {
        String phone=getMobileByToken(authToken);
        boolean isTeacherExist=teacherRepository.existsByPhone(phone);
        if(!isTeacherExist){
            throw new ResourceNotFoundException("Invalid Teacher Identity");
        }
        Pageable pageable=PageRequest.of(pageNo,size);
        List<TeacherFeesHistoryDto> result=repository.find10FeesHistoryByTeacherPhone(phone,pageable);

        return result;
    }


    public String getMobileByToken(String token){
        token=token.substring(7);
        return jwtService.extractUsername(token);
    }

}
