package dz.me.filleattente.services;

import java.util.Optional;

import dz.me.filleattente.entities.RefreshToken;
import dz.me.filleattente.models.AccessTokenModel;
import dz.me.filleattente.models.RefreshTokenRequest;

public interface RefreshTokenService {

	public Optional<RefreshToken> findById(String token);

	public Optional<RefreshToken> findByUsername(String username);

	public AccessTokenModel createAccessToken(RefreshTokenRequest refreshToken);

}
