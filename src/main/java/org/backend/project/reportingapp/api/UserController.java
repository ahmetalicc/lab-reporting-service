package org.backend.project.reportingapp.api;

import lombok.RequiredArgsConstructor;
import org.backend.project.reportingapp.core.responses.*;
import org.backend.project.reportingapp.dto.request.UserRegisterRequest;
import org.backend.project.reportingapp.dto.request.UserUpdateRequest;
import org.backend.project.reportingapp.dto.response.UserViewResponse;
import org.backend.project.reportingapp.service.Abstract.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<SuccessDataResponse<List<UserViewResponse>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                                   @RequestParam(defaultValue = "3") int size,
                                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                                   @RequestParam(defaultValue = "asc") String sortOrder,
                                                                                   @RequestParam(required = false) String filter){
        return ResponseEntity.ok(new SuccessDataResponse<>(userService.getAllUsers(page, size, sortBy, sortOrder, filter), "Data has been listed successfully."));
    }

    @PostMapping("/save")
    public ResponseEntity<DataResponse<UserViewResponse>> saveUser(@RequestBody UserRegisterRequest userRegisterRequest){
        try {
            return ResponseEntity.ok(new SuccessDataResponse<>(userService.saveUser(userRegisterRequest), "User is saved to the database successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<DataResponse<UserViewResponse>> updateUser(@PathVariable("id") Integer id, @RequestBody UserUpdateRequest userUpdateRequest){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessDataResponse<>(userService.updateUser(id, userUpdateRequest), "User updated successfully."));
        }
        catch (NullPointerException | IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDataResponse<>(e.getMessage()));
        }
    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable("id") Integer id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("User is deleted successfully."));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }
}
