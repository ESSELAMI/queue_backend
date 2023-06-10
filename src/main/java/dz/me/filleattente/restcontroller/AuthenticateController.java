package dz.me.filleattente.restcontroller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.entities.RefreshToken;
import dz.me.filleattente.entities.TentativeAcces;
import dz.me.filleattente.filters.JwtUtils;
import dz.me.filleattente.models.LoginRequest;
import dz.me.filleattente.repositories.RefreshTokenRepository;
import dz.me.filleattente.repositories.RoleRepository;
import dz.me.filleattente.repositories.UserRepository;
import dz.me.filleattente.services.BlackListRefreshTokenService;
import dz.me.filleattente.services.TentativeAccesService;
import dz.me.filleattente.services.UserService;
import dz.me.filleattente.utils.ResponseEntityUtils;
import dz.me.filleattente.utils.UtilsIP;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth/login")
public class AuthenticateController {
	// Injections
	private AuthenticationManager authenticationManager;
	private JwtUtils jwtDetailsService;
	private RefreshTokenRepository refreshTokenRepository;
	private BlackListRefreshTokenService blackListRefreshTokenService;
	private TentativeAccesService tentativeAccesService;
	private UserService userService;

	// variables
	private String jwtRefreshToken;
	private String jwtAccessToken;

	/*
	 * @Autowired
	 * private ServiceRepository serviceRepository;
	 * 
	 * @Autowired
	 * private StructureRepository structureRepository;
	 * 
	 * @Autowired
	 * private WilayaRepository wilayaRepository;
	 */

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	public AuthenticateController(TentativeAccesService tentativeAccesService,
			AuthenticationManager authenticationManager, JwtUtils jwtDetailsService,
			RefreshTokenRepository refreshTokenRepository, BlackListRefreshTokenService blackListRefreshTokenService,
			UserService userService) {
		super();
		this.tentativeAccesService = tentativeAccesService;
		this.authenticationManager = authenticationManager;
		this.jwtDetailsService = jwtDetailsService;
		this.refreshTokenRepository = refreshTokenRepository;
		this.blackListRefreshTokenService = blackListRefreshTokenService;
		this.userService = userService;

	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest,
			HttpServletRequest request) {

		Authentication authResult = null;

		// recuperer les informations general : ip,tentative login,...

		String ip = UtilsIP.getClientIpAddr(request);

		// verifier l'acces avec l'@ip simultané

		try {
			tentativeAccesService.validerAcces(request);
		} catch (Exception e) {
			return ResponseEntityUtils.ExceptionResponseEntity(e.getMessage(),
					HttpStatus.FORBIDDEN.value());
		}

		try

		{

			authenticationRequest.setPassword(authenticationRequest.getPassword());

			authResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));

		} catch (Exception e) {

			// augmenter le nombres de tentative
			Optional<dz.me.filleattente.entities.User> user1 = userRepository
					.findByUsername(authenticationRequest.getUsername());
			if (user1.isPresent()) {
				int tentative = tentativeAccesService.getNombreTentative(ip);
				tentative++;
				TentativeAcces tentativeAcces = new TentativeAcces(tentative, tentativeAccesService.sysdate(), ip);
				tentativeAccesService.addTentative(tentativeAcces);
				return ResponseEntityUtils.ExceptionResponseEntity("Utilisateur et mot de passe sont incorrects",
						HttpStatus.FORBIDDEN.value());
				// a supprimer plus tard else
			} else {

				return ResponseEntityUtils.ExceptionResponseEntity(
						"Utilisateur et mot de passe sont incorrects",
						HttpStatus.FORBIDDEN.value());

			}
		}

		User user = (User) authResult.getPrincipal();

		Algorithm algorithmA = Algorithm.HMAC256(jwtDetailsService.getJwtSecret());
		Algorithm algorithmR = Algorithm.HMAC256(jwtDetailsService.getJwtRefreshSecret());
		dz.me.filleattente.entities.User userRepo = userService.findByUsername(user.getUsername()).get();

		// chercher si le guichet existe reelement dans le service;
		String gichetUser = "0";

		// genere le access token

		jwtAccessToken = JWT.create().withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtDetailsService.getJwtExpirationMs()))
				.withIssuer(UtilsIP.getClientIpAddr(request))
				.withClaim("roles",
						user.getAuthorities().stream().map(ga -> ga.getAuthority()).collect(Collectors.toList()))
				.withClaim("user-id", userRepo.getId().toString())
				.withClaim("name", userRepo.getLastname() + " " + userRepo.getFirstname())

				.sign(algorithmA);

		/*
		 * Traitement refresh token s il n y a pas de refresh token generer un nouveau
		 * sinon si le refresh token est expiré ou dans le black liste generener un
		 * nouveau sinon recupere l'ancien refresh token
		 **/
		Optional<RefreshToken> RefreshToken = refreshTokenRepository
				.findById(user.getUsername());
		if (RefreshToken.isPresent()) {

			// genere le refresh token
			Optional<BlackListRefreshToken> blackListRefreshToken = blackListRefreshTokenService
					.findById(RefreshToken.get().getToken());

			if (RefreshToken.get().getExpired().before(new Date()) || blackListRefreshToken.isPresent()) {

				jwtRefreshToken = JWT.create().withSubject(user.getUsername())
						.withExpiresAt(
								new Date(
										System.currentTimeMillis() + jwtDetailsService.getJwtRefreshExpirationMs()))
						.withIssuer(UtilsIP.getClientIpAddr(request))
						.sign(algorithmR);
				refreshTokenRepository.save(new RefreshToken(user.getUsername(), jwtRefreshToken,
						new Date(System.currentTimeMillis() + jwtDetailsService.getJwtRefreshExpirationMs()),
						new Date(System.currentTimeMillis()), UtilsIP.getClientIpAddr(request)));

			} else {

				jwtRefreshToken = RefreshToken.get().getToken();
			}

		} else {

			jwtRefreshToken = JWT.create().withSubject(user.getUsername())
					.withExpiresAt(
							new Date(System.currentTimeMillis() + jwtDetailsService.getJwtRefreshExpirationMs()))
					.withIssuer(UtilsIP.getClientIpAddr(request))
					.sign(algorithmR);

			refreshTokenRepository.save(new RefreshToken(user.getUsername(), jwtRefreshToken,
					new Date(System.currentTimeMillis() + jwtDetailsService.getJwtRefreshExpirationMs()),
					new Date(System.currentTimeMillis()), UtilsIP.getClientIpAddr(request)));
		}

		Map<String, String> idToken = new HashMap<>();
		idToken.put("access_token", jwtAccessToken);
		idToken.put("refresh_token", jwtRefreshToken);

		/* reinitialiser le nombre de tentative */

		TentativeAcces tentativeAcces = new TentativeAcces(0, tentativeAccesService.sysdate(),
				ip);
		tentativeAccesService.addTentative(tentativeAcces);
		return ResponseEntity.ok(idToken);
	}

}
