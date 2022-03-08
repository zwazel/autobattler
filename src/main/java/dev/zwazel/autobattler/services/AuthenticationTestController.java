package dev.zwazel.autobattler.services;

import dev.zwazel.autobattler.security.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthenticationTestController {
  @GetMapping("/all")
  public ResponseEntity<MessageResponse> allAccess() {
    return ResponseEntity.ok(new MessageResponse("Content Available to All"));
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
  public ResponseEntity<MessageResponse> userAccess() {
    return ResponseEntity.ok(new MessageResponse("Content Available to User"));
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<MessageResponse> adminAccess() {
    return ResponseEntity.ok(new MessageResponse("Content Available to Admin"));
  }
}
