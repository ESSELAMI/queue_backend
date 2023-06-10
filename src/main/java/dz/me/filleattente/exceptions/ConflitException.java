package dz.me.filleattente.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Tarek Mekriche
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflitException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConflitException(String message) {
		super(message);
	}
}
