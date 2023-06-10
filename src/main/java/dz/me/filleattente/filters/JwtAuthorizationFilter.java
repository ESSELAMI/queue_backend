package dz.me.filleattente.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.exceptions.ResourceForbiddenException;
import dz.me.filleattente.exceptions.ResourceForbiddenWithRequestException;
import dz.me.filleattente.services.BlackListRefreshTokenService;

/**
 *
 * @author Tarek Mekriche
 */
@Service
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private BlackListRefreshTokenService blackListJwtService;
	private JwtUtils jwtUtils;

	public JwtAuthorizationFilter(BlackListRefreshTokenService blackListJwtService, JwtUtils jwtUtils) {
		super();
		this.blackListJwtService = blackListJwtService;
		this.jwtUtils = jwtUtils;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorizationToken = request.getHeader("Authorization");

		if (authorizationToken != null && authorizationToken.startsWith("Bearer ")) {

			try {

				String jwt = authorizationToken.substring("Bearer ".length());

				Algorithm algorithm = Algorithm.HMAC256(jwtUtils.getJwtSecret());

				JWTVerifier jwtVerifier = JWT.require(algorithm).build();

				DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
				String username = decodedJWT.getSubject();

				String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
				Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				for (String role : roles) {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
				}

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						username, null, authorities);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				if ("/api/v1/auth/refresh".equals(request.getRequestURI())) {

					Boolean existe = doFilterBlackList(authorizationToken.substring("Bearer ".length()));
					if (existe) {
						throw new Exception("Token on the black list :" + authorizationToken.substring(0, 20) + "...");
					}
				}
				filterChain.doFilter(request, response);
			} catch (Exception e) {

				response.setHeader("error-message", e.getMessage());
				response = new ResourceForbiddenWithRequestException(e.getMessage(), response).getReponse();

			}
		} else {

			filterChain.doFilter(request, response);

		}

	}

	BlackListRefreshToken blackListJwt = null;

	private Boolean doFilterBlackList(String token) {

		try {
			blackListJwt = blackListJwtService.findById(token).get();

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

}
