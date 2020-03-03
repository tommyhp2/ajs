/**
 * 
 */
package com.sointe.ajs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author tommy
 *
 */
public class AjsFilterChain implements FilterChain {

	private static final Logger logger = LogManager.getLogger(AjsFilterChain.class);

	private FilterChain chain;
	private List<Filter> filters = new ArrayList<>();
	private Iterator<Filter> iterator;

	/**
	 * 
	 */
	public AjsFilterChain(FilterChain chain) {
		this.chain = chain;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
		if (iterator == null) {
			iterator = filters.iterator();
		}
		if (iterator.hasNext()) {
			logger.trace("");
			iterator.next().doFilter(request, response, this);
		} else {
			chain.doFilter(request, response);
			logger.trace("post chain - reverting back to standard chain");
		}
	}

	public void addFilter(Filter filter) {
		if (iterator != null) {
			throw new IllegalStateException();
		}
		filters.add(filter);
	}
}