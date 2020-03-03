/**
 * 
 */
package com.sointe.web;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author tommy
 *
 */
public abstract class AbstractFilter extends AbstractWeb implements Filter {

	/**
	 * 
	 */
	public AbstractFilter() {
		super();
	}

	/**
	 * @param javax.servlet.ServletRequest
	 * @param javax.servlet.ServletResponse
	 */
	public AbstractFilter(ServletRequest request, ServletResponse response) {
		super(request, response);
	}

	/**
	 * @param javax.servlet.http.HttpServletRequest
	 * @param javax.servlet.http.HttpServletResponse
	 */
	public AbstractFilter(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.http.HttpServletRequest.getRemoteAddr()
	 */
	protected String getRemoteAddr() {
		return getRequest().getRemoteAddr();
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getRemoteHost()
	 */
	protected String getRemoteHost() {
		return getRequest().getRemoteHost();
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getRemotePort()
	 */
	protected int getRemotePort() {
		return getRequest().getRemotePort();
	}
}