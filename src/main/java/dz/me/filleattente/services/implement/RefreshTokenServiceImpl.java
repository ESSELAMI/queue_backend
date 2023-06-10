package dz.me.filleattente.services.implement;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.entities.RefreshToken;
import dz.me.filleattente.entities.TentativeAcces;
import dz.me.filleattente.entities.User;
import dz.me.filleattente.exceptions.ResourceForbiddenException;
import dz.me.filleattente.exceptions.ResourceNotFoundException;
import dz.me.filleattente.filters.JwtUtils;
import dz.me.filleattente.models.AccessTokenModel;
import dz.me.filleattente.models.RefreshTokenRequest;
import dz.me.filleattente.repositories.RefreshTokenRepository;
import dz.me.filleattente.services.BlackListRefreshTokenService;
import dz.me.filleattente.services.RefreshTokenService;
import dz.me.filleattente.services.TentativeAccesService;
import dz.me.filleattente.services.UserService;
import dz.me.filleattente.utils.UtilsIP;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired
	private BlackListRefreshTokenService blackListRefreshTokenService;
	@Autowired
	private HttpServletRequest httpServletRequest;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private TentativeAccesService tentativeAccesService;
	@Autowired
	private UserService userService;

	@Override
	public Optional<RefreshToken> findById(String token) {
		return refreshTokenRepository.findById(token);
	}

	@Override
	public AccessTokenModel createAccessToken(RefreshTokenRequest refreshTokenRequest) {
		Boolean existe = null;
		String ip = UtilsIP.getClientIpAddr(httpServletRequest);

		try {
			tentativeAccesService.validerAcces(httpServletRequest);
		} catch (Exception e) {
			throw new ResourceForbiddenException(e.getMessage());
		}
		try {
			existe = doFilterBlackList(refreshTokenRequest.getToken().substring("Bearer ".length()));
		} catch (Exception e) {
			int tentative = tentativeAccesService.getNombreTentative(ip);
			tentative++;
			TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
			tentativeAccesService.addTentative(tentativeAcces);
			throw new ResourceNotFoundException("token Invalide : " + refreshTokenRequest.getToken());

		}
		if (existe) {

			int tentative = tentativeAccesService.getNombreTentative(ip);
			tentative++;
			TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
			tentativeAccesService.addTentative(tentativeAcces);
			throw new ResourceForbiddenException(
					"Token on the black list :" + refreshTokenRequest.getToken().substring(0, 20) + "...");
		}

		try {
			if (!refreshTokenRequest.getToken().substring(0, "Bearer ".length())
					.equals("Bearer ")) {
				throw new ResourceForbiddenException("");
			}

			refreshTokenRequest.getToken().replace("Bearer ", "");

		} catch (Exception e) {
			int tentative = tentativeAccesService.getNombreTentative(ip);
			tentative++;
			TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
			tentativeAccesService.addTentative(tentativeAcces);
			throw new ResourceForbiddenException("Error Body request : Bearer token invalide");
		}

		String[] roles = JWT.decode(refreshTokenRequest.getToken().replace("Bearer ", ""))
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
			int tentative = tentativeAccesService.getNombreTentative(ip);
			tentative++;
			TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
			tentativeAccesService.addTentative(tentativeAcces);
			throw new ResourceForbiddenException(
					"you need a refresh token to have a new access token (invalide refresh token ) ");
		} else {

			if (refreshTokenRequest.getToken() != null
					&& refreshTokenRequest.getToken().startsWith("Bearer ")) {
				try {
					String jwt = refreshTokenRequest.getToken().substring("Bearer ".length());
					Algorithm algorithm = Algorithm.HMAC256(jwtUtils.getJwtRefreshSecret());
					JWTVerifier jwtVerifier = JWT.require(algorithm).build();
					DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
					Algorithm algorithmAcces = Algorithm.HMAC256(jwtUtils.getJwtSecret());
					User userRepo = userService.findByUsername(decodedJWT.getSubject()).get();

					List<String> role = userRepo.getRoles().stream()
							.map(ga -> ga.getName()).collect(Collectors.toList());
					String username = decodedJWT.getSubject();
					String jwtAccessToken = JWT.create().withSubject(username)
							.withExpiresAt(
									new Date(System.currentTimeMillis() + jwtUtils.getJwtExpirationMs()))
							.withIssuer(UtilsIP.getClientIpAddr(httpServletRequest))
							.withClaim("roles", role)
							.withClaim("user-id", userRepo.getId().toString())
							.withClaim("name", userRepo.getLastname() + " " + userRepo.getFirstname())

							.sign(algorithmAcces);

					return new AccessTokenModel(jwtAccessToken);
				} catch (Exception e) {
					int tentative = tentativeAccesService.getNombreTentative(ip);
					tentative++;
					TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
					tentativeAccesService.addTentative(tentativeAcces);
					throw new ResourceForbiddenException(e.getMessage());

				}
			} else {
				int tentative = tentativeAccesService.getNombreTentative(ip);
				tentative++;
				TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
				tentativeAccesService.addTentative(tentativeAcces);
				throw new ResourceForbiddenException("refresh token required");
			}

		}

	}

	BlackListRefreshToken blackListJwt = null;

	private Boolean doFilterBlackList(String token) {

		try {
			blackListJwt = blackListRefreshTokenService.findById(token).get();

			throw new ResourceForbiddenException("");
		} catch (Exception e) {
			try {

				if (!blackListJwt.equals(null)) {

					return true;

				}

				return false;
			} catch (Exception e1) {

				return false;
			}
		}

	}

	@Override
	public Optional<RefreshToken> findByUsername(String username) {
		return refreshTokenRepository.findByUsername(username);
	}

}
