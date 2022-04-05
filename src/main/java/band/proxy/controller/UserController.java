package band.proxy.controller;

import band.proxy.dto.UserRequestDto;
import band.proxy.dto.UserResponseDto;
import band.proxy.entity.UserEntity;
import band.proxy.exception.UserNotFoundException;
import band.proxy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "User Management")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    ModelMapper mapper;

    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        List<UserResponseDto> response = userService.getAll().stream()
                .map(entity -> mapper.map(entity, UserResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create user", description = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@RequestBody UserRequestDto dto) {
        UserEntity entity = userService.save(mapper.map(dto, UserEntity.class));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(entity, UserResponseDto.class));
    }

    @Operation(summary = "Update user", description = "Update exists user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "UserNotFound exception",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))})
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> update(@PathVariable String userId, @RequestBody UserRequestDto dto) {
        UserEntity entityFromDto = mapper.map(dto, UserEntity.class);
        entityFromDto.setId(userId);
        UserEntity entityFromDb = userService.update(entityFromDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(entityFromDb, UserResponseDto.class));
    }

    @Operation(summary = "Delete user", description = "Delete exists user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "UserNotFound exception",
                    content = @Content(schema = @Schema(implementation = UserNotFoundException.class)))})
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable String userId) {
        userService.deleteBy(userId);
        return ResponseEntity.ok().build();
    }

}

