package at.freebim.db.webapp.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * This class will be invoked as filter once for every request. It is used, like
 * the name suggests, to check if a valid jwt-token is send with every request.
 * 
 * @see org.springframework.web.filter.OncePerRequestFilter
 * 
 * @author Patrick.Lanzinger@student.uibk.ac.at
 *
 */
public class JwtTokenFilter extends BasicAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	private JwtTokenProvider jwtTokenProvider;

	/**
	 * Create a new instance.
	 * 
	 * @param jwtTokenProvider the provider for the jwt-token.
	 */
	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, AuthenticationManager authManager) {
		super(authManager);
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {

		if (jwtTokenProvider != null) {
			String token = jwtTokenProvider.resolveToken(httpServletRequest);
			try {
				if (token != null && jwtTokenProvider.validateToken(token)) {
					Authentication auth = jwtTokenProvider.getAuthentication(token);
					SecurityContextHolder.getContext().setAuthentication(auth);
					logger.debug("user authenticated");
				} else {
					SecurityContextHolder.clearContext();
					logger.debug("Jwt validation failed");
				}
			} catch (JwtValidationException ex) {
				// this is very important, since it guarantees the user is not authenticated at
				// all
				SecurityContextHolder.clearContext();
				logger.debug("Jwt validation failed");
			}

			filterChain.doFilter(httpServletRequest, httpServletResponse);
			SecurityContextHolder.clearContext();
		} else {
			logger.debug("Jwt token provider has not been set");
			throw new JwtValidationException("jwt token provider has not been set", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
