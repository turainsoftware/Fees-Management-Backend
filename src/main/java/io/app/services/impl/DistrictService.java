package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.DistrictDto;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Districts;
import io.app.model.State;
import io.app.repository.DistrictRepository;
import io.app.repository.StateReposiroty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictService {
    @Autowired
    private DistrictRepository repository;

    @Autowired
    private StateReposiroty stateReposiroty;


    public ApiResponse createMultipleDistricts(List<Districts> districts,long stateId){
        State state=stateReposiroty.findById(stateId)
                .orElseThrow(()->new ResourceNotFoundException("Invalid state"));

        List<Districts> results=districts.stream().map((item)->{
            return Districts.builder()
                    .name(item.getName())
                    .state(state)
                    .build();
        })
                .collect(Collectors.toList());

        repository.saveAll(results);

        return ApiResponse.builder()
                .message("Districts added successfully")
                .status(true)
                .build();
    }

    public List<DistrictDto> getAllByStateId(long stateId){
        return repository.findDistrictByStateId(stateId);
    }

}
