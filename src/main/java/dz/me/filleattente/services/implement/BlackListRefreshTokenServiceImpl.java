package dz.me.filleattente.services.implement;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.exceptions.ResourceForbiddenException;
import dz.me.filleattente.filters.JwtUtils;
import dz.me.filleattente.models.Token;
import dz.me.filleattente.repositories.BlackListJwtRepository;
import dz.me.filleattente.services.BlackListRefreshTokenService;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class BlackListRefreshTokenServiceImpl implements BlackListRefreshTokenService {

	@Autowired
	BlackListJwtRepository blackListJwtServiceImpl;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public Optional<BlackListRefreshToken> findById(String token) {
		return blackListJwtServiceImpl.findById(token);
	}

	@Override
	public BlackListRefreshToken save(Token blackListRefreshToken, Token AccesToken, Date generatedOn) {

		if (blackListRefreshToken != null
				&& blackListRefreshToken.getToken().startsWith("Bearer ")) {

			try {
				String jwt = blackListRefreshToken.getToken().substring("Bearer ".length());

				Algorithm algorithm = Algorithm.HMAC256(jwtUtils.getJwtSecret());
				JWTVerifier jwtVerifier = JWT.require(algorithm).build();
				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);

				BlackListRefreshToken blackListJwt = new BlackListRefreshToken();
				blackListJwt.setExpired(decodedJWT.getExpiresAt());
				blackListJwt.setToken(jwt);
				blackListJwt.setUsername(decodedJWT.getSubject());
				blackListJwt.setGeneratedFromIp(decodedJWT.getIssuer());
				blackListJwt.setGeneratedOn(generatedOn);
				blackListJwt.setBlackListedDate(new Date());
				String[] roles = JWT.decode(blackListRefreshToken.getToken().replace("Bearer ", ""))
						.getClaim("roles").asArray(String.class);
				boolean isAccessToken = true;
				try {
					if (!roles.equals(null)) {
						isAccessToken = true;// access token because the token contain roles table
					}

				} catch (Exception e) {
					isAccessToken = false; // a refresh token

				}

				if (isAccessToken == true) {
					throw new ResourceForbiddenException("you can put only the refreshToken on the blacklist ");
				}

				// info access token
				String jwtAccessToken = AccesToken.getToken();
				DecodedJWT decodedJWTAccessToken = jwtVerifier.verify(jwtAccessToken);

				blackListJwt.setBlockedByAccesToken(jwtAccessToken);
				blackListJwt.setBlockedByUsername(decodedJWTAccessToken.getSubject());
				return blackListJwtServiceImpl.save(blackListJwt);

			} catch (Exception e) {
				throw new ResourceForbiddenException(e.getMessage());

			}
		} else {
			throw new ResourceForbiddenException("refresh token required start with Bearer ey...");
		}

	}

}
