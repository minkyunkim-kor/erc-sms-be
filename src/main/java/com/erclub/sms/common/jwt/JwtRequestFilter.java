package com.erclub.sms.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtUserDetailsService jwtUserDetailsService;

  private final JwtTokenUtils jwtTokenUtils;

  private static final List<String> DO_NOT_NEED_TO_AUTH_LIST = Collections.unmodifiableList(Arrays.asList("/signin"));

  public JwtRequestFilter(@Lazy JwtUserDetailsService jwtUserDetailsService, JwtTokenUtils jwtTokenUtils) {
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtils = jwtTokenUtils;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain
  ) throws ServletException, IOException {
    final String requestHeaderToken = httpServletRequest.getHeader("Authorization");
    final String requestUserId = httpServletRequest.getHeader("erc-user-id");

    String id = null;
    String token;

    if (StringUtils.isBlank(requestUserId)) {
      httpServletRequest.setAttribute("exception", "missingHeaderUserId");
      log.error("Missing header(erc-user-id)");
    } else {
      if (StringUtils.isNotBlank(requestHeaderToken) && StringUtils.startsWith(requestHeaderToken, "Bearer ")) {
        token = StringUtils.substring(requestHeaderToken, 7);
        try {
          id = jwtTokenUtils.getUsernameFromToken(token);
        } catch (IllegalArgumentException e) {
          httpServletRequest.setAttribute("exception", "notExistToken");
          log.error("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
          httpServletRequest.setAttribute("exception", "expiredToken");
          log.error("Token has expired");
        } catch (MalformedJwtException e) {
          httpServletRequest.setAttribute("exception", "invalidToken");
          log.error("Token is invalid");
        }
        if (!StringUtils.equals(id, requestUserId)) {
          httpServletRequest.setAttribute("exception", "usingOtherUserToken");
          id = "";
          log.error("User ID is different between token and header");
        }

        if (StringUtils.isNotBlank(id) && SecurityContextHolder.getContext().getAuthentication() == null) {
          if (jwtTokenUtils.validateToken(token, requestUserId)) {
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(id);
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          } else {
            httpServletRequest.setAttribute("exception", "invalidToken");
            log.error("Token is invalid");
          }
        }
      } else {
        httpServletRequest.setAttribute("exception", "notExistToken");
        log.warn("Token does not begin with Bearer String");
      }
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return DO_NOT_NEED_TO_AUTH_LIST.stream().anyMatch(url -> StringUtils.equalsIgnoreCase(url, request.getServletPath()));
  }
}
