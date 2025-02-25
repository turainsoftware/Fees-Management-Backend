package io.app.services.impl;

import io.app.dto.AnalysisResponse;
import io.app.dto.ApiResponse;
import io.app.dto.BatchDto;
import io.app.dto.Projections.BatchProjection;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Batch;
import io.app.model.Days;
import io.app.model.Subject;
import io.app.model.Teacher;
import io.app.repository.*;
import io.app.services.BatchService;
import io.app.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ResourceClosedException;
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

        System.out.println(batchDto.getStartYear()+" "+batchDto.getStartMonth());
//        if (!teacher.getBoards().stream().anyMatch((board)->board.getId().equals(batchDto.getBoard().getId()))){
//            throw new ResourceNotFoundException("Board is not valid");
//        }
//        if(!teacher.getLanguages().stream().anyMatch((data)->data.getId().equals(batchDto.getLanguage().getId()))){
//            throw new ResourceNotFoundException("Invalid Language");
//        }

//        if (batchDto.getSubjects()!=null && !teacher.getSubjects().containsAll(batchDto.getSubjects())){
//            throw new ResourceNotFoundException("Invalid Subjects");
//        }

//        if (batchDto.getClasses()!=null && !teacher.getClasses().containsAll(batchDto.getClasses())){
//            throw new ResourceNotFoundException("Invalid Classes");
//        }

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

    @Override
    public AnalysisResponse batchAnalysis(String authToken) {
        String mobileNumber=extractJwt(authToken);
        long teacherId=teacherRepository.findIdByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Teacher"));

        long currentMonthCount=repository.countBatchesByTeacherInCurrentMonth(teacherId);
        long previousMonthCount=repository.countBatchesByTeacherInPreviousMonth(teacherId);
        long totalBatches=repository.countBatchesByTeacherId(teacherId);

        double percentage=0;
        if (previousMonthCount==0){
            percentage=100;
        }else{
            percentage=((currentMonthCount-previousMonthCount)/previousMonthCount)*100;
        }

        return AnalysisResponse.builder()
                .current(totalBatches)
                .percentage(percentage)
                .trend(percentage>=0?"Increased":"Decreased")
                .build();
    }

    @Override
    public List<BatchProjection> getAllBatchDetailsWithSpecificDetails(String authToken) {
        String mobileNumber=extractJwt(authToken);
        long teacherId=teacherRepository.findIdByPhone(mobileNumber)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Teachers Credentials"));

        List< BatchProjection> result=repository.findByTeacherWithSpecificDetails(teacherId);
        return result;
    }

    @Override
    public BatchDto getBatchById(long id) {
        Batch batch=repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Batch Id"));
        BatchDto batchDto=modelMapper.map(batch,BatchDto.class);
        return batchDto;
    }

    @Override
    public ApiResponse updateBatchFees(String authToken, long batchId,
                                       double monthlyFees,double monthlyExamFees) {
        String mobile=extractJwt(authToken);
        long teacherId=teacherRepository.findIdByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Token"));
        Teacher teacher=Teacher.builder()
                .id(teacherId)
                .build();
        Batch batch=repository.findByIdAndTeacher(batchId,teacher)
                .orElseThrow(()->new ResourceClosedException("Teacher do not have such batch!"));
        batch.setMonthlyFees(monthlyFees);
        batch.setMonthlyExamFees(monthlyExamFees);
        repository.save(batch);
        return ApiResponse.builder()
                .message("Fees Details Updated Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse updateBatchSubjects(long batchId, Set<Long> subjectsId) {
        Batch batch=repository.findById(batchId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Batch Details"));
        Set<Subject> subjects=subjectsId.stream().map((item)->{
            return Subject.builder()
                    .id(item)
                    .build();
        }).collect(Collectors.toSet());
        batch.setSubjects(subjects);
        repository.save(batch);
        return ApiResponse.builder()
                .status(true)
                .message("Subjects Updated Successfully")
                .build();
    }

    @Override
    public ApiResponse updateBatchSchedule(String authToken, long batchId,
                                           int startYear, int startMonth,
                                           int endYear, int endMonth,
                                           Set<Days> days, LocalTime startTime,
                                           LocalTime endTime) {
        String mobile=extractJwt(authToken);
        long teacherId=teacherRepository.findIdByPhone(mobile)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Credentials"));
        Teacher teacher=Teacher.builder()
                .id(teacherId)
                .build();

        Batch batch=repository.findById(batchId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid Batch"));

        // Checking the overlap with days
        if(hasTimeConflict(teacher,startTime,endTime,days,batchId)){
            throw new ResourceNotFoundException("Teacher has Schedule batch! check free days");
        }

        batch.setDays(days);
        batch.setStartTime(startTime);
        batch.setEndTime(endTime);
        batch.setStartYear(startYear);
        batch.setEndYear(endYear);
        batch.setEndMonth(endMonth);
        batch.setStartMonth(startMonth);

        repository.save(batch);

        return ApiResponse.builder()
                .status(true)
                .message("Updated Successfully")
                .build();
    }

    public boolean hasTimeConflict(Teacher teacher,
                                   LocalTime newStartTime,
                                   LocalTime newEndTime,
                                   Set<Days> days,
                                   long batchId) {
        List<Batch> batches = repository.findByTeacher(teacher);
        log.info("{}", teacher);

        for (Batch existingBatch : batches) {
            if(existingBatch.getId()==batchId){
                System.out.println("yes");
                continue;
            }
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

}
