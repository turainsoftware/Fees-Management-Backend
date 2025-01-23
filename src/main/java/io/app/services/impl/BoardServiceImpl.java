package io.app.services.impl;

import io.app.dto.ApiResponse;
import io.app.excetptions.DuplicateFoundException;
import io.app.excetptions.ResourceNotFoundException;
import io.app.model.Board;
import io.app.repository.BoardRepository;
import io.app.services.BoardServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardServices {
    private final BoardRepository repository;

    @Override
    public ApiResponse createBoard(String board) {
        boolean isDuplicate=repository.existsByName(board);
        if(isDuplicate==true){
            throw new DuplicateFoundException("Board Already Exists");
        }
        Board board1= Board.builder()
                .name(board)
                .build();
        repository.save(board1);
        return ApiResponse.builder()
                .status(true)
                .message("Board Added Successfully")
                .build();
    }

    @Override
    public ApiResponse createMultiple(Set<Board> boards) {
        repository.saveAll(boards);
        return ApiResponse.builder()
                .message("All boards has been added")
                .status(true)
                .build();
    }


    @Override
    public List<Board> getAllBoard() {
        List<Board> boards=repository.findAll();
        return boards;
    }

    @Override
    public ApiResponse deleteBoardById(Long id) {
        repository.deleteById(id);
        return ApiResponse.builder()
                .message("Board Deleted Successfully")
                .status(true)
                .build();
    }

    @Override
    public ApiResponse deleteBoardByName(String name) {
        Board board=repository.findByName(name)
                .orElseThrow(()->new ResourceNotFoundException("Board Not Found!"));
        return null;
    }

    @Override
    public ApiResponse deleteAll() {
        repository.deleteAll();
        return ApiResponse.builder()
                .status(true)
                .message("All boards Deleted Successfully")
                .build();
    }

    @Override
    public List<Board> autoSuggest(String name) {
        List<Board> results=repository.findByNameContaining(name);
        return results;
    }
}
