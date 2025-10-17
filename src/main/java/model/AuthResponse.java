package model;

import lombok.Getter;

@Getter
public class AuthResponse {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;
}
