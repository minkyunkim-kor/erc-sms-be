package com.erclub.sms.common.jwt;


import com.erclub.sms.common.domain.CommonResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e
  ) throws IOException {
    String message = (String) httpServletRequest.getAttribute("exception");
    int status;

    switch (message) {
      case "notExistToken" :
      case "invalidToken" :
      case "usingOtherUserToken" :
        status = HttpStatus.BAD_REQUEST.value();
        break;
      case "expiredToken":
        status = HttpStatus.UNAUTHORIZED.value();
        break;
      default:
        status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    httpServletResponse.setStatus(status);
    httpServletResponse.getWriter().println(new Gson().toJson(CommonResponse.of(message)));
  }
}
