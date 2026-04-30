package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.User;
import com.oa.security.JwtTokenUtil;
import com.oa.security.LoginUser;
import com.oa.service.MenuService;
import com.oa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User loginUser) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword())
        );
        
        LoginUser loginUserDetail = (LoginUser) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(loginUserDetail);
        
        User user = loginUserDetail.getUser();
        user.setPassword(null);
        
        List<String> roles = userService.getRoleCodes(user.getId());
        List<String> permissions = userService.getPermissions(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        result.put("roles", roles);
        result.put("permissions", permissions);
        
        return Result.success(result);
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> info(Authentication authentication) {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        user.setPassword(null);
        
        List<String> roles = userService.getRoleCodes(user.getId());
        List<String> permissions = userService.getPermissions(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", roles);
        result.put("permissions", permissions);
        
        return Result.success(result);
    }

    @PostMapping("/logout")
    public Result<Boolean> logout() {
        return Result.success(true);
    }
}
