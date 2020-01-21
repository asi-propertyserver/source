package at.freebim.db.webapp.security.jwt;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import at.freebim.db.domain.FreebimUser;
import at.freebim.db.domain.base.Role;
import at.freebim.db.repository.impl.FreebimUserDetails;
import at.freebim.db.service.FreebimUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The provider aka. creator of the jwt-token.
 * 
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
@Component
public class JwtTokenProvider {

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${security.secret}")
	private String secretKey = "";

	@Value("${security.validity}")
	private long validityInMilliseconds = 0;

	@Autowired
	private FreebimUserService freebimUserService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	/**
	 * Creates a new jwt-token. With the claims username and his roles.
	 * 
	 * @param username the username.
	 * @param roles    the roles of the user.
	 * @return the jwt-token.
	 */
	public String createToken(String username, List<Role> roles) {

		logger.debug("Create jwt token");
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
				.filter(Objects::nonNull).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();
	}

	/**
	 * Providing the jwt-token this function can return the token of the
	 * {@link Authentication} class.
	 * 
	 * @param token the jwt-token.
	 * @return the {@link Authentication} class.
	 * @throws JwtValidationException
	 */
	public Authentication getAuthentication(String token) throws JwtValidationException {
		logger.debug("Authenticate user with token");
		FreebimUser user = freebimUserService.get(getUsername(token));

		Calendar userValidTo = Calendar.getInstance();

		if (user.getValidTo() != null) {
			userValidTo.setTimeInMillis(user.getValidTo());
		}

		if (user.getValidTo() != null && userValidTo.before(Calendar.getInstance())) {
			throw new JwtValidationException("User is not valid anymore", HttpStatus.FORBIDDEN);
		}

		FreebimUserDetails details = new FreebimUserDetails(user);
		return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
	}

	/**
	 * Parse the username out of the jwt-token.
	 * 
	 * @param token the jwt-token.
	 * @return the username.
	 */
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Parse the jwt-token out of the http-request.
	 * 
	 * @param req the http-request.
	 * @return the jwt-token.
	 */
	public String resolveToken(HttpServletRequest req) {
		logger.debug("resolve token");
		String bearerToken = req.getHeader("Authorization");

		// check in cookies
		if (req.getCookies() != null) {
			for (Cookie c : req.getCookies()) {
				if (c.getName().equals("token") && c.getValue() != "") {
					return c.getValue();
				}
			}
		}

		// check in header
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	/**
	 * Validate the validity of the jwt-token.
	 * 
	 * @param token the jwt-token.
	 * @return returns <code>true</code> when valid and <code>false</code>
	 *         otherwise.
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtValidationException("Expired or invalid JWT token", HttpStatus.UNAUTHORIZED);
		}
	}

}
