package com.erclub.sms.services;

import com.erclub.sms.api.request.SaveUserRequest;
import com.erclub.sms.common.exception.CommonException;
import com.erclub.sms.common.jwt.JwtTokenUtils;
import com.erclub.sms.common.jwt.JwtUserDetailsService;
import com.erclub.sms.models.ErcUser;
import com.erclub.sms.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class UserService {

  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenUtils jwtTokenUtils;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtils jwtTokenUtils, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtils = jwtTokenUtils;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public void saveUser(String userId, SaveUserRequest request) {
    checkAdmin(userId);
    if (request.getIsNew() && userRepository.findByLoginId(request.getId()).isPresent()) {
      throw new CommonException(BAD_REQUEST.value(), "duplicate id");
    }
    request.setPw(passwordEncoder.encode(request.getPw()));
    ErcUser ercUser = ErcUser.from(request);
    userRepository.save(ercUser);
  }

  public ErcUser signIn(String id, String pwd) {
    final ErcUser user = jwtUserDetailsService.authenticateByIdAndPassword(id, pwd);
    user.setToken(jwtTokenUtils.generateToken(user.getUserId()));
    return user;
  }

  public List<ErcUser> getUsers(String userId) {
    checkAdmin(userId);
    return userRepository.findAll().stream().filter(ercUser -> !ercUser.getUserId().equals(userId)).collect(Collectors.toList());
  }

  public ErcUser getUser(String userId, String targetId) {
    checkAdmin(userId);
    return userRepository.findById(targetId).orElseThrow(() -> new CommonException(BAD_REQUEST.value(), "Not Exist User"));
  }

  public void deleteUsers(String userId, String targetId) {
    checkAdmin(userId);
    userRepository.deleteById(userId);
  }

  private void checkAdmin(String userId) {
    ErcUser admin = userRepository.findById(userId).orElseThrow(() -> new CommonException(BAD_REQUEST.value(), "Not Admin User"));
    if (!admin.getRole().equals("ADMIN")) throw new CommonException(BAD_REQUEST.value(), "Not Admin User");
  }
}
