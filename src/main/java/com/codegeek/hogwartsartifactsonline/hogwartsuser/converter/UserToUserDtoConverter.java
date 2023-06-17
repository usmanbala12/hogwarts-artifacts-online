package com.codegeek.hogwartsartifactsonline.hogwartsuser.converter;

import com.codegeek.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    @Override
    public UserDto convert(HogwartsUser source) {
        UserDto userDto = new UserDto(
                source.getId(),
                source.getUsername(), 
                source.isEnabled(),
                source.getRoles()
        );
        
        return userDto;
    }
}
