package project.investmentservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.investmentservice.exceptions.AuthException;

import java.lang.reflect.Field;

@Service
public class AuthenticateService {

    public static void LoginCheck(Long userId, String username) {
        if (userId == null || username == null || username.isEmpty()) {
            throw new AuthException("Login Required");
        }
    }
}
