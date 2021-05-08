package com.ksteindl.logolo.web;

import com.ksteindl.logolo.domain.User;
import com.ksteindl.logolo.domain.UserInput;
import com.ksteindl.logolo.security.JwtAuthenticationFilter;
import com.ksteindl.logolo.security.JwtProvider;
import com.ksteindl.logolo.security.payload.JwtLoginResponse;
import com.ksteindl.logolo.security.payload.LoginRequest;
import com.ksteindl.logolo.services.MapValidationErrorService;
import com.ksteindl.logolo.services.UserService;
import com.ksteindl.logolo.validator.UserInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInputValidator userInputValidator;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserInput userInput, BindingResult result) {
        userInputValidator.validate(userInput, result);
        mapValidationErrorService.throwExceptionIfNotValid(result);
        User newUser = userService.saveUser(userInput);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtLoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        mapValidationErrorService.throwExceptionIfNotValid(result);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtAuthenticationFilter.TOKEN_PREFIX + jwtProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtLoginResponse(true, jwt));

    }

}


