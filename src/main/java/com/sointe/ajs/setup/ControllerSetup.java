/**
 * 
 */
package com.sointe.ajs.setup;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sointe.ajs.AjsInitializer;
import com.sointe.web.AbstractController;

/**
 * @author tommy
 *
 */
public class ControllerSetup extends AbstractController {

	private static final Logger logger = LogManager.getLogger(ControllerSetup.class);
	
	/**
	 * 
	 */
	public ControllerSetup() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param request
	 * @param response
	 */
	public ControllerSetup(ServletRequest request, ServletResponse response) {
		super(request, response);
	}

	/**
	 * @param request
	 * @param response
	 */
	public ControllerSetup(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	/*
	 * (non-Javadoc)
	 * @see com.sointe.web.AbstractController#execute()
	 */
	@Override
	public boolean execute() {
		validateSessionShiro();
		try {
			getResponse().getWriter().print("Shiro session ID: " + getShiroSession().getId());
		} catch (IOException e) {
			logger.error(e);
		}
		return true;
	}

}