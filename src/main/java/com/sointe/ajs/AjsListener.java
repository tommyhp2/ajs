/**
 * 
 */
package com.sointe.ajs;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author tommy
 *
 */
public class AjsListener implements ServletContextListener {

	private static final Logger logger = LogManager.getLogger(AjsListener.class);
	
	private ServletContext sc;

	/**
	 * 
	 */
	public AjsListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sc = sce.getServletContext();
		logger.trace("{} context initialized.", sc.getContextPath());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sc = sce.getServletContext();
		logger.trace("{} context destroy.", sc.getContextPath());
	}
}