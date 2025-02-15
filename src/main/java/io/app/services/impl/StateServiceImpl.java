package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.dto.StateDto;
import io.app.model.State;
import io.app.repository.StateReposiroty;
import io.app.services.StateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {
    private final StateReposiroty reposiroty;

    @Override
    public ApiResponse createState(State state) {
        reposiroty.save(state);

        return ApiResponse.builder()
                .status(true)
                .message("State Created Successfully")
                .build();
    }

    @Override
    public ApiResponse createMultipleState(List<State> states) {
        reposiroty.saveAll(states);
        return ApiResponse.builder()
                .message("States Created Successfully")
                .status(true)
                .build();
    }

    @Override
    public StateDto getStateById(long stateId) {
        return null;
    }

    @Override
    public List<StateDto> getAllStates() {
        List<State> states=reposiroty.findAll();
        List<StateDto> result=states.stream().map((state)->{
            return StateDto.builder()
                    .id(state.getId())
                    .name(state.getName())
                    .build();
        }).collect(Collectors.toList());


        return result;
    }
}
