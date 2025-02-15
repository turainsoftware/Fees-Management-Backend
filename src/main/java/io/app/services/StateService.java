package io.app.services;

import io.app.dto.ApiResponse;
import io.app.dto.StateDto;
import io.app.model.State;

import java.util.List;

public interface StateService {
    public ApiResponse createState(State state);
    public ApiResponse createMultipleState(List<State> states);
    public StateDto getStateById(long stateId);
    public List<StateDto> getAllStates();
}
