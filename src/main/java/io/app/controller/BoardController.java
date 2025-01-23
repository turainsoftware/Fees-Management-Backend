package io.app.controller;

import io.app.dto.ApiResponse;
import io.app.model.Board;
import io.app.services.impl.BoardServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardServiceImpl service;

    @PostMapping
    public ResponseEntity<ApiResponse> createBoard(@RequestParam(name = "boardName") String boardName){
        return new ResponseEntity<>(service.createBoard(boardName), HttpStatus.CREATED);
    }

    @PostMapping("/create-multiple")
    public ResponseEntity<ApiResponse> createBoard(@RequestBody Set<Board> boards){
        return new ResponseEntity<>(service.createMultiple(boards),HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Board>> allBoard(){
        return ResponseEntity.ok(service.getAllBoard());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable("id") Long id){
        return ResponseEntity.ok(service.deleteBoardById(id));
    }

    @DeleteMapping("/name")
    public ResponseEntity<ApiResponse> deleteByName(@RequestParam("name") String name){
        return ResponseEntity.ok(service.deleteBoardByName(name));
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse deleteAllBoards(){
        return service.deleteAll();
    }

    @GetMapping("/autoSuggest")
    public List<Board> autoSuggest(@RequestParam("query") String name){
        return service.autoSuggest(name);
    }

}
