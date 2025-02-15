package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.StateDto;
import io.app.model.State;
import io.app.services.impl.StateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/state")
@RequiredArgsConstructor
public class StateController {
    private final StateServiceImpl service;

    @PostMapping("/create-state")
    public ApiResponse createState(@RequestBody State state){
        return service.createState(state);
    }

    @PostMapping("/create-multiple-state")
    public ApiResponse createMultipleState(@RequestBody List<State> states){
        return service.createMultipleState(states);
    }

    @GetMapping
    public List<StateDto> getAllStates(){
        return service.getAllStates();
    }

}
