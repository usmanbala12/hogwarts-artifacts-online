package com.codegeek.hogwartsartifactsonline.security;

import com.codegeek.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.MyUserPrincipal;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.converter.UserToUserDtoConverter;
import com.codegeek.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();
        UserDto convert = userToUserDtoConverter.convert(hogwartsUser);
        // create jwt
        String token = jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();
        loginResultMap.put("userInfo", convert);
        loginResultMap.put("token", token);

        return loginResultMap;
    }
}
