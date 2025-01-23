package io.app.services;

import io.app.dto.ApiResponse;
import io.app.model.Board;
import org.apache.catalina.LifecycleState;

import java.util.List;
import java.util.Set;

public interface BoardServices {
    public ApiResponse createBoard(String board);
    public ApiResponse createMultiple(Set<Board> boards);
    public List<Board> getAllBoard();
    public ApiResponse deleteBoardById(Long id);
    public ApiResponse deleteBoardByName(String name);
    public ApiResponse deleteAll();
    public List<Board> autoSuggest(String name);
}
