package at.freebim.db.webapp.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import at.freebim.db.domain.FreebimUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * The provider aka. creator of the jwt-refresh-token.
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
@Component
public class RefreshTokenProvider {

	@Value("${security.refresh.secret}")
	private String secretKey = "";

	@Value("${security.refresh.validity}")
	private long validityInMilliseconds = 0;

	/**
	 * The logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RefreshTokenProvider.class);

	/**
	 * Creates a new token that will be added to the jwt-refresh-token claims to
	 * validate a user.
	 * 
	 * @param user the user for which the token will be created.
	 * @return the token.
	 * @throws NoSuchAlgorithmException
	 */
	public String createToken(FreebimUser user) throws NoSuchAlgorithmException {
		byte[] username = hash(Optional.ofNullable(user.getUsername()).orElse(""));
		byte[] validFrom = hash(Optional.ofNullable(user.getValidFrom()).orElse(0L).toString());
		byte[] validTo = hash(Optional.ofNullable(user.getValidTo()).orElse(0L).toString());

		return bytesToHex(xor(xor(username, validFrom), validTo));
	}

	/**
	 * Create a new jwt-refresh-token.
	 * 
	 * @param user the user for which the refresh-token will be created.
	 * @return the jwt-refresh-token.
	 * @throws NoSuchAlgorithmException
	 */
	public String createRefreshToken(FreebimUser user) throws NoSuchAlgorithmException {
		logger.debug("Create refresh token");

		Claims claims = Jwts.claims().setSubject(user.getUsername());
		claims.put("token", createToken(user));

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
	 * Get the username fromt the jwt-refresh-token.
	 * 
	 * @param token
	 * @return the username.
	 */
	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	private String getToken(String token) {
		return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("token");
	}

	/**
	 * Validate the jwt-refresh-token.
	 * 
	 * @param token the jwt-refresh-token.
	 * @param user  the user.
	 * @return <code>true</code> when the jwt-refresh-token is valid and
	 *         <code>false</code> otherwise.
	 * @throws NoSuchAlgorithmException
	 */
	public boolean validateToken(String token, FreebimUser user) throws NoSuchAlgorithmException {
		logger.debug("Validate refresh token");
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

			String userToken = createToken(user);
			String recievedToken = getToken(token);

			if (!userToken.equals(recievedToken)) {
				return false;
			}

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			throw new JwtValidationException("Expired or invalid refresh token", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private byte[] hash(String value) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(value.getBytes(StandardCharsets.UTF_8));

		return encodedhash;
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	private byte[] xor(byte[] a, byte[] key) {
		byte[] out = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			out[i] = (byte) (a[i] ^ key[i % key.length]);
		}
		return out;
	}

}
