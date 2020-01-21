package at.freebim.db.webapp.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;
import at.freebim.db.service.FreebimUserService;
import at.freebim.db.webapp.models.request.LoginRequestModel;
import at.freebim.db.webapp.models.request.RefreshTokenModel;
import at.freebim.db.webapp.models.response.LoginResponseModel;
import at.freebim.db.webapp.models.response.RefreshTokenResponseModel;
import at.freebim.db.webapp.security.jwt.JwtTokenProvider;
import at.freebim.db.webapp.security.jwt.RefreshTokenProvider;

/**
 * This controller contains all REST-endpoints that handle authorization.
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
@RestController
@RequestMapping("/")
public class AuthorizationController {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	@Value("${security.validity}")
	private long validityInMilliseconds = 0;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private RefreshTokenProvider refreshTokenProvider;

	@Autowired
	private FreebimUserService service;

	@Autowired
	private Session session;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Rehash the password to old md5 password.
	 * 
	 * @param user  the username and password
	 * @param fUser the user that corresponds to the username
	 * @return <code>true</code> when the password was rehashed and
	 *         <code>false</code> otherwise.
	 * @throws NoSuchAlgorithmException
	 */
	private Boolean rehashPassword(LoginRequestModel user, FreebimUser fUser) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(user.getPassword().getBytes());

		byte[] digest = md.digest();

		String hash = DatatypeConverter.printHexBinary(digest);

		if (fUser.getPassword().toUpperCase().equals(hash)) {

			String newPassword = this.passwordEncoder.encode(user.getPassword());

			fUser.setPassword(newPassword);

			this.session.save(fUser);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * This REST-endpoint can be called to get a token/refresh-token.
	 * 
	 * @param user The user containing the username and password. Should be passed
	 *             through the request body.
	 * @return the response containing the token and refresh-token
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping(value = "login/rest")
	@Transactional
	public ResponseEntity<LoginResponseModel> login(@Valid @RequestBody LoginRequestModel user)
			throws NoSuchAlgorithmException {
		FreebimUser fUser = service.get(user.getUsername());

		if (fUser == null) {
			return new ResponseEntity<LoginResponseModel>(new LoginResponseModel("Username or password is wrong"),
					HttpStatus.BAD_REQUEST);
		}

		// rehash password if the old hash is detected
		if (fUser.getPassword().length() == 32) {
			try {
				if (!rehashPassword(user, fUser)) {
					return new ResponseEntity<LoginResponseModel>(
							new LoginResponseModel("Username or password is wrong"), HttpStatus.BAD_REQUEST);
				}
			} catch (NoSuchAlgorithmException ex) {
				logger.debug("Error ocurred while trying to md5 hash");
				return new ResponseEntity<LoginResponseModel>(new LoginResponseModel("Something went wrong"),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		// ---------------------------

		if (fUser == null || !passwordEncoder.matches(user.getPassword(), fUser.getPassword())) {
			return new ResponseEntity<LoginResponseModel>(new LoginResponseModel("Username or password is wrong"),
					HttpStatus.BAD_REQUEST);
		}
		String token = jwtTokenProvider.createToken(user.getUsername(), new ArrayList<Role>(fUser.getRoles()));
		String refreshToken = refreshTokenProvider.createRefreshToken(fUser);

		Authentication auth = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(auth);

		return new ResponseEntity<LoginResponseModel>(new LoginResponseModel(token, refreshToken), HttpStatus.OK);
	}

	/**
	 * This REST-endpoint can be called to get a new jwt-token when provided the
	 * refresh-token.
	 * 
	 * @param json the refresh-token
	 * @return The new jwt-token
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping("/login/refresh/token")
	@Transactional
	public ResponseEntity<RefreshTokenResponseModel> refreshToken(@Valid @RequestBody RefreshTokenModel json)
			throws NoSuchAlgorithmException {

		FreebimUser user = service.get(refreshTokenProvider.getUsername(json.getRefreshToken()));

		if (user == null) {
			return new ResponseEntity<RefreshTokenResponseModel>(new RefreshTokenResponseModel("", ""),
					HttpStatus.UNAUTHORIZED);
		}

		if (!refreshTokenProvider.validateToken(json.getRefreshToken(), user)) {
			return new ResponseEntity<RefreshTokenResponseModel>(new RefreshTokenResponseModel("", ""),
					HttpStatus.UNAUTHORIZED);
		}

		String token = jwtTokenProvider.createToken(user.getUsername(), new ArrayList<Role>(user.getRoles()));
		Authentication auth = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(auth);

		return new ResponseEntity<RefreshTokenResponseModel>(new RefreshTokenResponseModel(token, ""), HttpStatus.OK);
	}

}
