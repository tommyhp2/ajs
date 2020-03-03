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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sointe.web.AbstractFilter;

/**
 * @author tommy
 *
 */
public class FilterStaticFiles extends AbstractFilter {

	private static final Logger logger = LogManager.getLogger(FilterStaticFiles.class);

	public FilterStaticFiles() {
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		logger.debug("");
		setServletContext(config.getServletContext());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
    	logger.debug(((HttpServletRequest)request).getRequestURI());

		chain.doFilter(request, response);

		logger.trace("post chain - response");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		logger.debug("");
		super.close();
	}
}