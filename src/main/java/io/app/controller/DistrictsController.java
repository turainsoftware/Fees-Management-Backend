package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.dto.DistrictDto;
import io.app.model.Districts;
import io.app.services.impl.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/districts")
public class DistrictsController {
    private final DistrictService service;

    public DistrictsController(DistrictService service){
        this.service=service;
    }

    @PostMapping("create-multiple")
    public ApiResponse addMultipleDistricts(
            @RequestBody List<Districts> districts, @RequestParam("stateId") long stateId){
        return service.createMultipleDistricts(districts,stateId);
    }

    @GetMapping("state/{stateId}")
    public List<DistrictDto> allDistrictByState(@PathVariable("stateId") long stateId){
        return service.getAllByStateId(stateId);
    }


}
