/**
 * 
 */
package com.sointe.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sointe.web.AbstractFilter;

/**
 * @author tommy
 *
 */
public class FilterSecurity extends AbstractFilter {

	private static final Logger logger = LogManager.getLogger(FilterSecurity.class);
	
	private boolean shiroSession = true;

	public FilterSecurity() {
	}

	/**
	 * @param request
	 * @param response
	 */
	public FilterSecurity(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/**
	 * @param request
	 * @param resposne
	 */
	public FilterSecurity(ServletRequest request, ServletResponse resposne) {
		super(request, resposne);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		logger.debug("");
		setServletContext(filterConfig.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		initWeb(request, response);
		logger.debug(getRequestURI());
		if (!allowMethod()) {
			setHttpStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			sendError(getHttpStatus());
			logger.debug("{}: {} {}", getMethod(), getHttpStatus(), getHttpStatusText());
		}
		if (!allowRemote()) {
			setHttpStatus(HttpServletResponse.SC_FORBIDDEN);
			sendError(getHttpStatus());
			logger.debug("{}: {}", getHttpStatusText(), getHttpStatus());
		}

//		logger.info(">> ThreadContext.getResources(): {} {}", ThreadContext.getResources() != null,
//				ThreadContext.getResources().size());
		validateSession();
//		logger.debug(AppDebug.debugRequest(getRequest(), true, false));
		if (shiroSession) {
			logger.debug(">> Session ID: {}", getShiroSession().getId());
		} else {

		}
		if (allowURI()) {

			// TODO
			chain.doFilter(request, response);
			logger.trace("post chain: {}", getRequestURI());

		} else {
			// TODO
			logger.trace("Skipped remainder of chain: {}", getRequestURI());
		}
	}

	@Override
	public void destroy() {
		logger.debug("");
		super.close();
	}

	protected boolean allowMethod() {
		boolean allow = true;
		return allow;
	}

	protected boolean allowRemote() {
		boolean allow = true;
		// TODO
//		if (!remoteAllow.get("addresses").contains(getRemoteAddr()) //
//				&& !remoteAllow.get("addresses").contains(getRemoteHost())) {
//			allow = false;
//		}
//		if (remoteAllow.get)
		return allow;
	}

	protected boolean allowURI() {
		boolean allow = true;
		// TODO
		return allow;
	}

	protected void validateSession() {
		if (shiroSession) {
			validateSessionShiro();
		} else {
			validSessionJava();
		}
	}
}