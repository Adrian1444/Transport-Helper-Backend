package org.trhelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.trhelper.domain.authentication.AuthenticationRequest;
import org.trhelper.domain.authentication.AuthenticationResponse;
import org.trhelper.domain.order.dto.RegisterDataDTO;
import org.trhelper.security.JwtUtil;
import org.trhelper.service.authentication.UserLoginDetailsService;
import org.trhelper.service.user.UserCommonService;
import org.trhelper.service.user.implementation.ClientServiceImpl;
import org.trhelper.service.user.implementation.DriverServiceImpl;
import org.trhelper.service.user.implementation.UserCommonServiceImpl;

@CrossOrigin
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserLoginDetailsService userLoginDetailsService;

    @Autowired
    UserCommonServiceImpl userCommonServiceImpl;

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private DriverServiceImpl driverService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @RequestMapping(value="/authenticate",method=RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch(BadCredentialsException e){
            throw new Exception("Incorrect username and password!",e);
        }
        final UserDetails userDetails= userLoginDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt=jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @RequestMapping(value="/user/find/role/{token}",method=RequestMethod.POST)
    public ResponseEntity<?> getUserRoleByToken(@PathVariable String token) throws Exception{
        String username= jwtTokenUtil.extractUsername(token);
        return ResponseEntity.ok(userCommonServiceImpl.findUserRole(username));
    }

    @RequestMapping(value="/user/find/username/{token}",method=RequestMethod.POST)
    public ResponseEntity<?> getUsernameByToken(@PathVariable String token) {
        String username= jwtTokenUtil.extractUsername(token);
        return ResponseEntity.ok(username);
    }

    @RequestMapping(value="/register",method=RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterDataDTO registerDataDTO) throws Exception{
        userCommonServiceImpl.register(registerDataDTO);
        return ResponseEntity.ok("Account created successfully");
    }

    @RequestMapping(value="/find/entity/{username}",method=RequestMethod.GET)
    public ResponseEntity<?> findEntity(@PathVariable String username) {
        String role=userCommonServiceImpl.findUserRole(username);
        if(role.equals("client"))
            return ResponseEntity.ok().body(clientService.findClientDataByUsername(username));
        else if(role.equals("driver"))
            return ResponseEntity.ok().body(driverService.findDriverDataByUsername(username));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid username");

    }
}
