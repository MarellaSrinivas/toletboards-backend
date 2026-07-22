package com.toletboards.service;

import com.toletboards.model.RefreshToken;
import com.toletboards.model.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);

}