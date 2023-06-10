package dz.me.filleattente.services;

import java.util.Date;
import java.util.Optional;

import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.models.Token;

public interface BlackListRefreshTokenService {

	public Optional<BlackListRefreshToken> findById(String token);

	public BlackListRefreshToken save(Token blackListRefreshToken, Token AccesToken, Date generatedOn);

}
