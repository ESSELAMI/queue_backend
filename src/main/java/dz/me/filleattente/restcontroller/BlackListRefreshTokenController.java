package dz.me.filleattente.restcontroller;

import java.util.Date;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dz.me.filleattente.entities.BlackListRefreshToken;
import dz.me.filleattente.exceptions.ResourceNotFoundException;
import dz.me.filleattente.models.Token;
import dz.me.filleattente.services.BlackListRefreshTokenService;
import dz.me.filleattente.utils.ResponseEntityUtils;

/**
 *
 * @author ABDELLATIF ESSELAMI
 */
@RestController
@RequestMapping("api/v1/blackListRefreshTokens")
public class BlackListRefreshTokenController {

	@Autowired
	private BlackListRefreshTokenService blackListRefreshTokenService;

	@Autowired
	private HttpServletRequest request;

	public ResponseEntity<?> save(Token blackListRefreshToken, Date generatedOn) {
		try {

			Optional<BlackListRefreshToken> optionalBlackListRefreshToken = blackListRefreshTokenService
					.findById(blackListRefreshToken.getToken().substring("Bearer ".length()));
			if (optionalBlackListRefreshToken.isPresent()) {
				return ResponseEntityUtils.ExceptionResponseEntity("RefreshToken already exists on the black list",
						HttpStatus.CONFLICT.value());
			} else {

				String token = request.getHeader("Authorization").replace("Bearer ", "");
				Token tokenAccess = new Token(token);
				return ResponseEntity
						.ok(blackListRefreshTokenService.save(blackListRefreshToken, tokenAccess, generatedOn));
			}
		} catch (Exception exp) {
			return ResponseEntityUtils.ExceptionResponseEntity(exp.getMessage(), HttpStatus.BAD_REQUEST.value());
		}
	}

	public ResponseEntity<?> byId(@PathVariable(value = "id") String id) {
		try {

			Optional<BlackListRefreshToken> opt = blackListRefreshTokenService.findById(id);
			if (opt.isPresent()) {
				return ResponseEntity.ok(opt.get());
			} else {
				throw new ResourceNotFoundException("token not found");
			}
		} catch (Exception exp) {
			return ResponseEntityUtils.ExceptionResponseEntity(exp.getMessage(), HttpStatus.NOT_FOUND.value());
		}
	}

}
