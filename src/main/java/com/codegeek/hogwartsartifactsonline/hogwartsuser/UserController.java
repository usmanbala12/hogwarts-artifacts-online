package com.codegeek.hogwartsartifactsonline.hogwartsuser;

import com.codegeek.hogwartsartifactsonline.hogwartsuser.converter.UserDtoToUserConverter;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.codegeek.hogwartsartifactsonline.system.Result;
import com.codegeek.hogwartsartifactsonline.system.StatusCode;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping
    public Result findAllUsers() {
        List<HogwartsUser> foundHogwartsUsers = userService.findAll();

        List<UserDto> userDtos = foundHogwartsUsers.stream()
                .map(userToUserDtoConverter::convert).toList();

        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId) {
        HogwartsUser hogwartsUser = userService.findById(userId);
        UserDto userDto = userToUserDtoConverter.convert(hogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser hogwartsUser) {
        HogwartsUser savedUser = userService.save(hogwartsUser);
        UserDto userDto = userToUserDtoConverter.convert(savedUser);
        return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UserDto userDto) {
        HogwartsUser update = userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedHogwartsUser = userService.update(userId, update);
        UserDto updatedUserDto = userToUserDtoConverter.convert(updatedHogwartsUser);
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        userService.delete(userId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }
}
