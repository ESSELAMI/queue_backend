package dz.me.filleattente.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import com.google.gson.Gson;
import dz.me.filleattente.models.ErrorDetails;

/**
 *
 * @author Tarek Mekriche
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ResourceForbiddenWithRequestException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private HttpServletResponse response;
	private String message;

	public ResourceForbiddenWithRequestException(String message, HttpServletResponse response) {

		super(message);
		this.response = response;
		this.message = message;
	}

	public HttpServletResponse getReponse() {

		// déclencher l'exception avec le message
		ErrorDetails erreur = new ErrorDetails();
		erreur.setTimestamp(new Date());
		erreur.setStatus(403);
		erreur.setMessage(message);
		erreur.setError("Forbidden");
		String tokenJsonString = new Gson().toJson(erreur);
		PrintWriter out = null;
		try {

			out = response.getWriter();

		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(tokenJsonString);
		response.setStatus(403);
		out.flush();

		return response;

	}

}