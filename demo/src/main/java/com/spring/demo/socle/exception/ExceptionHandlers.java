package com.spring.demo.socle.exception;

import ci.monvisa.backend.socle.validation.FieldErreur;
import ci.monvisa.backend.socle.validation.ValidationErreur;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.QueryTimeoutException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ci.monvisa.backend.socle.exception.CodeErreurTechnique.*;
import static ci.monvisa.backend.socle.utils.ArrayUtils.isNullOrEmpty;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

/**
 * Gestion des exceptions avec spring MVC.
 */

/**
 * Gestion des exceptions avec spring MVC.
 */
@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

	public static final int CODE_HTTP_OPTIMISTICLOCK = 550;
	/**
	 * Encodage par défaut des messages json
	 */
	public static final Charset ENCODAGE_MESSAGE = StandardCharsets.UTF_8;
	public static final Logger log = LoggerFactory.getLogger(ExceptionHandlers.class);
	private static final String FORBIDDEN_ERREUR = "Droits insuffisants pour accéder à la ressource";
	private final String UNAUTHORIZED_ERREUR = "Authentification requise pour accéder à la ressource";
	private final String RUNTIME_ERREUR = "Une erreur inattendue est survenue. Contacter la hotline";
	private final String OPTIMISTIC_LOCK_ERROR = "L'opération n'a pas pu aboutir. Merci de réessayer";
	private final long profondeurStacktrace = 10;

	/**
	 * Permet de spécifier que la réponse http renvoie du JSON.
	 *
	 * @param erreur   l'erreur à renvoyer.
	 * @param response la réponse HTTP.
	 */
	public static void setReponseJson(Object erreur, HttpServletResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(erreur);

			response.setContentType("application/json");
			response.setCharacterEncoding(ENCODAGE_MESSAGE.name());
			OutputStream os = response.getOutputStream();
			os.write(json.getBytes(ENCODAGE_MESSAGE));
		} catch (IOException ex) {
// Ne devrait pas arriver mais...
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Permet de gérer les exceptions lancées lorsqu'on n'a aucun résultat pour une requête en base.
	 *
	 * @param ex       l'exception lorsqu'on n'a aucun résultat pour une requête en base.
	 * @param response la réponse HTTP.
	 */
	@ExceptionHandler(NoResultException.class)
	public void handleNoResultException(NoResultException ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_FOUND.value());

		ApplicationErreur msg = new ApplicationErreur(
				CodeErreurTechnique.NO_RESULT_ERROR.name(),
				RUNTIME_ERREUR,
				CodeErreurTechnique.NO_RESULT_ERROR.getCode()
		);

		log.warn(msg.getMessageAvecCode());
		log.warn(ex.getMessage(), ex);
		setReponseJson(msg, response);
	}

	/**
	 * Handler dédié aux OptimisticLockException
	 *
	 * @param ex       l'exception lorsqu'on n'a aucun résultat pour une requête en base.
	 * @param response la réponse HTTP.
	 */
	@ExceptionHandler(OptimisticLockException.class)
	public void handleOptimisticLockException(OptimisticLockException ex, HttpServletResponse response) {
		response.setStatus(CODE_HTTP_OPTIMISTICLOCK);
		ApplicationErreur msg = new ApplicationErreur(
				NEED_REPLAY_EXCEPTION.name(),
				OPTIMISTIC_LOCK_ERROR,
				NEED_REPLAY_EXCEPTION.getCode()
		);
		log.warn(msg.getMessageAvecCode());
		log.info(ex.getMessage(), ex);
		setReponseJson(msg, response);
	}

	/**
	 * Handler dédié aux OptimisticLockException
	 *
	 * @param ex       l'exception lorsqu'on n'a aucun résultat pour une requête en base.
	 * @param response la réponse HTTP.
	 */
	@ExceptionHandler(OptimisticLockingFailureException.class)
	public void handleOptimisticLockingFailureException(OptimisticLockingFailureException ex, HttpServletResponse response) {
		response.setStatus(CODE_HTTP_OPTIMISTICLOCK);
		ApplicationErreur msg = new ApplicationErreur(
				NEED_REPLAY_EXCEPTION.name(),
				OPTIMISTIC_LOCK_ERROR,
				NEED_REPLAY_EXCEPTION.getCode()
		);
		log.warn(msg.getMessageAvecCode());
		log.info(ex.getMessage(), ex);
		setReponseJson(msg, response);
	}

	/**
	 * Permet de gérer les exceptions liés à un échec d'authentification avec Spring Security.
	 * C'est l'exception renvoyée lorsqu'une erreur est renvoyée liée à
	 * l'annotation @Secured.
	 *
	 * @param ex       L'exception.
	 * @param response La réponse HTTP.
	 */
	@ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
	public void handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		CodeErreurTechnique code = CodeErreurTechnique.UNAUTHORIZED_ERREUR;
		ApplicationErreur erreur = new ApplicationErreur(
				code.name(),
				UNAUTHORIZED_ERREUR,
				code.getCode(),
				ExceptionUserDisplay.ERROR
		);
		handleUnauthorizedError(ex, erreur, response);
	}

	/**
	 * Permet de gérer les exceptions liées à un échec d'authentification car le(s)
	 * rôle(s) requis pour accéder à la ressource sont absents.
	 * <p>
	 * La différence avec une erreur de type {@link CodeErreurTechnique#UNAUTHORIZED_ERREUR} est
	 * que l'utilisateur est bien authentifié mais ne dispose pas des droits suffisants pour
	 * accéder à la ressource.
	 *
	 * @param ex       L'exception.
	 * @param response La réponse http.
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public void handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.FORBIDDEN.value());

		CodeErreurTechnique code = CodeErreurTechnique.FORBIDDEN_ERREUR;
		ApplicationErreur erreur = new ApplicationErreur(
				code.name(),
				FORBIDDEN_ERREUR,
				code.getCode(),
				ExceptionUserDisplay.ERROR
		);

		log.warn(erreur.getMessageAvecCode());
		log.debug(ex.getMessage(), ex);
		setReponseJson(erreur, response);
	}

	private void handleUnauthorizedError(Exception ex, ApplicationErreur erreur, HttpServletResponse response) {
		log.warn(erreur.getMessageAvecCode());
		log.debug(ex.getMessage(), ex);
		setReponseJson(erreur, response);
	}

	/**
	 * Permet de gérer les exceptions lancées lorsqu'une entité cherchée n'existe pas en base.
	 *
	 * @param ex       l'exception pour l'entité qui n'existe pas en base.
	 * @param response la réponse HTTP.
	 */
	@ExceptionHandler(EntiteNonConnueException.class)
	public void handleEntiteNonConnueException(EntiteNonConnueException ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.NOT_FOUND.value());

		log.warn(ex.getMessageAvecCode());
		log.warn(ex.getMessage(), ex);
		setReponseJson(ex.getApplicationErreur(), response);
	}

	/**
	 * Permet de gérer les exceptions runtime.
	 *
	 * @param ex       l'exception runtime.
	 * @param response la réponse HTTP.
	 */
	@ExceptionHandler(RuntimeException.class)
	public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
		response.setStatus(INTERNAL_SERVER_ERROR.value());

		Throwable rootCause = ex;
		boolean afficherRootCause = false;

// Lorsqu'un rollback est détecté, on cherche la cause racine
		if (ex instanceof UnexpectedRollbackException ||
				ex instanceof StaleObjectStateException) {
			long profondeurInitiale = this.profondeurStacktrace;
			long profondeurCourante = profondeurInitiale;
// Si la profondeur initiale est de 0, alors on a une profondeur infinie
// sinon on a une profondeur fixe.
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause &&
					(profondeurCourante > 0 || profondeurInitiale == 0)) {
// On remplace la cause par la cause parente, cela permet de remonter à l'exception de base.
				rootCause = rootCause.getCause();
// Si l'une des causes est une exception que l'on recherche,
// alors on choisit d'afficher la cause principale de l'exception.
// Dans les autres cas, on ne change pas l'affichage de l'erreur.
				if (exceptionNeedReplay(rootCause)) {
					afficherRootCause = true;
				}
				profondeurCourante--;
			}
		}

		ApplicationErreur msg = new ApplicationErreur(
				RUNTIME_ERROR.name(),
				RUNTIME_ERREUR,
				afficherRootCause ? NEED_REPLAY_EXCEPTION.getCode() : RUNTIME_ERROR.getCode(),
				ExceptionUserDisplay.ERROR
		);
		log.error(msg.getMessageAvecCode(), afficherRootCause ? rootCause : ex);
		setReponseJson(msg, response);
	}

	/**
	 * Indique les erreurs qui vont indiquer que l'on va devoir rejouer le flux.
	 * Ce sont les erreurs qui sont levées lors d'un rollback et que l'on pourra rejouer
	 * sans problème via Tibco.
	 *
	 * @param ex l'erreur a tester.
	 * @return true si on peut rejouer, false sinon.
	 */
	private boolean exceptionNeedReplay(Throwable ex) {
		return ex instanceof OptimisticLockException ||
				ex instanceof OptimisticLockingFailureException ||
				ex instanceof LockAcquisitionException ||
				ex instanceof LockTimeoutException ||
				ex instanceof QueryTimeoutException ||
				ex instanceof StaleStateException;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ObjectError> rawErrors = ex.getBindingResult().getAllErrors();
		List<FieldErreur> erreurs = rawErrors.stream()
				.filter(objectError -> (objectError instanceof FieldError))
				.map(objectError -> {
					FieldError fieldError = (FieldError) objectError;
					String message = fieldError.getDefaultMessage();
					String[] codes = fieldError.getCodes();
					String typeErreur = !isNullOrEmpty(codes) ? codes[codes.length - 1] : "";
					return new FieldErreur(fieldError.getField(), typeErreur, message, fieldError.getRejectedValue());
				})
				.collect(toList());

		log.debug(ex.getMessage(), ex);
		return new ResponseEntity<>(new ValidationErreur(erreurs, ERREUR_VALIDATION), headers, status);
	}

	@ExceptionHandler(AbstractApplicationException.class)
	public void handleAbstractApplicationException(AbstractApplicationException ex, HttpServletResponse response) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

		log.error(ex.getMessageAvecCode());
		log.debug(ex.getMessage(), ex);
		setReponseJson(ex.getApplicationErreur(), response);
	}
}