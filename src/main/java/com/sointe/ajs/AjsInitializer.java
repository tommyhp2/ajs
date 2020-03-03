/**
 * 
 */
package com.sointe.ajs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.web.env.EnvironmentLoaderListener;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * @author tommy
 *
 */
public class AjsInitializer implements ServletContainerInitializer {

	private static final Logger logger = LogManager.getLogger(AjsInitializer.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContainerInitializer#onStartup(java.util.Set,
	 * javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

		logger.info("*******************************************************************************");
		logger.info("Initializing application for context: " + ctx.getContextPath());
		if (c != null && c.size() > 0) {
			logger.trace("Classes:");
			c.forEach(clazz -> {
				logger.trace(clazz.getName());
			});
		}
		logger.debug("Servlet version: {}.{}", ctx.getMajorVersion(), ctx.getMinorVersion());
		logger.trace("ServletContext class: {}", ctx.getClass().getName());
		// Properties =========================================================
		Properties cfgProps = new Properties();
		try {
			// application.properties
			InputStream is = new FileInputStream(
					ctx.getResource("/WEB-INF/properties/").getPath().concat("application.properties"));
			cfgProps.load(is);
			is.close();
			is = null;
		} catch (IOException | IllegalStateException | NullPointerException e) {
			logger.warn("Error loading properties.  Application may fail to " //
					.concat("initialize, setup, function, and/or enter/exit maintenance mode properly!"), e);
		}
		// application.properties -> log4j2.isLog4jAutoInitializationDisabled
		String log4j = cfgProps.getProperty("log4j2.isLog4jAutoInitializationDisabled", "true"); //
		ctx.setInitParameter("isLog4jAutoInitializationDisabled", log4j);
		logger.debug("Setting context-init parameeters...");
		cfgProps.forEach((k, v) -> {
			String sk = String.valueOf(k);
			if (sk.startsWith("context.init.")) {
				ctx.setInitParameter(sk.replace("context.init.", ""), String.valueOf(v));
			}
		});
		String settings;
		// Session Cookie Config ==============================================
		settings = cfgProps.getProperty("config.security.session.timeout");
		if (settings != null && settings.length() > 1) {
			ctx.setSessionTimeout(Integer.parseInt(settings.trim()));
		}
		SessionCookieConfig scc = ctx.getSessionCookieConfig();
		settings = cfgProps.getProperty("config.security.session.cookie.comment");
		if (settings != null && settings.trim().length() > 1) {
			scc.setComment(settings.trim());
		}
		settings = cfgProps.getProperty("config.security.session.cookie.domain");
		if (settings != null && settings.trim().length() > 1) {
			scc.setDomain(settings.trim());
		}
		settings = cfgProps.getProperty("config.security.session.cookie.maxAge");
		if (settings != null && settings.trim().length() > 1) {
			scc.setMaxAge(Integer.parseInt(settings.trim()));
		}
		settings = cfgProps.getProperty("config.security.session.cookie.name");
		if (settings != null && settings.trim().length() > 1) {
			scc.setName(settings.trim());
		}
		settings = cfgProps.getProperty("config.security.session.cookie.path");
		if (settings != null && settings.trim().length() > 1) {
			scc.setPath(settings.trim());
		}
		settings = cfgProps.getProperty("config.security.session.cookie.httpOnly");
		if (settings != null && settings.trim().length() > 1) {
			scc.setHttpOnly(Boolean.parseBoolean(settings.trim()));
		}
		settings = cfgProps.getProperty("config.security.session.cookie.secure");
		if (settings != null && settings.trim().length() > 1) {
			scc.setSecure(Boolean.parseBoolean(settings.trim()));
		}
		scc = null;
		logger.debug("Session Cookie config:");
		logger.debug("\tgetComment: {}", ctx.getSessionCookieConfig().getComment());
		logger.debug("\tgetDomain: {}", ctx.getSessionCookieConfig().getDomain());
		logger.debug("\tgetMaxAge: {}", ctx.getSessionCookieConfig().getMaxAge());
		logger.debug("\tgetName: {}", ctx.getSessionCookieConfig().getName());
		logger.debug("\tgetPath: {}", ctx.getSessionCookieConfig().getPath());
		logger.debug("\tisHttpOnly: {}", ctx.getSessionCookieConfig().isHttpOnly());
		logger.debug("\tisSecure: {}", ctx.getSessionCookieConfig().isSecure());
		// Listeners ==========================================================
		ctx.addListener(EnvironmentLoaderListener.class);
		ctx.addListener(AjsListener.class);

		// default servlet mapping of static resources ========================
		ServletRegistration srDefault = ctx.getServletRegistration("default");
		if (srDefault != null) {

			logger.trace("Default servlet: " + srDefault.getClassName());
			srDefault.addMapping(new String[] { "/css/*", "/js/*", "/img/*" });

		} else {

			logger.warn("Unable to get 'default' servlet or static resources " //
					.concat("do not exist.  Static resources not mapped to default servlet."));
		}
		srDefault = null;

		// Startup order for: tomcat
		// Startup 1 - default
		// Startup 3 - jsp

		// Servlets ===========================================================
		ServletRegistration.Dynamic srd = ctx.addServlet(AjsServlet.class.getSimpleName(), AjsServlet.class);
		srd.setLoadOnStartup(2);
		srd.addMapping("/");
		srd = null;

		// Filters ============================================================
//		org.apache.shiro.web.servlet.ShiroFilter:name(ShiroFilter):mappingURL(/*):dispatcherType(REQUEST,FORWARD,INCLUDE,ERROR);\
		FilterRegistration fr = ctx.addFilter(ShiroFilter.class.getSimpleName(), ShiroFilter.class);
		fr.addMappingForUrlPatterns(EnumSet.of( //
				DispatcherType.ERROR, //
				DispatcherType.FORWARD, //
				DispatcherType.INCLUDE, //
				DispatcherType.REQUEST), false, "/*");
		fr = ctx.addFilter(AjsFilterLoader.class.getSimpleName(), AjsFilterLoader.class);
		fr.addMappingForUrlPatterns(EnumSet.of( //
				DispatcherType.ASYNC, //
				DispatcherType.ERROR, //
				DispatcherType.FORWARD, //
				DispatcherType.INCLUDE, //
				DispatcherType.REQUEST), false, "/*");
		fr = null;

		// Thymeleaf ==========================================================
		ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(ctx);
		// Cache is set to true by default. Set to false if you want templates to
		// be automatically updated when modified.
		resolver.setCacheable(false);
//		resolver.setCacheablePatterns();
		// Template cache TTL=1h. If not set, entries would be cached until expelled
		resolver.setCacheTTLMs(3600000L);
		resolver.setCharacterEncoding("UTF-8");
//		resolver.setCheckExistence();
//		resolver.setName();
		resolver.setOrder(1);
		resolver.setPrefix("/WEB-INF/templates");
		resolver.setSuffix(".html");
//		resolver.setResolvablePatterns();
		// HTML is the default mode,
		// but we set it anyway for better understanding of code
		resolver.setTemplateMode("HTML");
//		resolver.setUseDecoupledLogic();
		logger.debug("Thymeleaf ServletContext Template Resolver configured.");

		TemplateEngine engine = new TemplateEngine();
		engine.addTemplateResolver(resolver);
		ctx.setAttribute(AjsContext.APP_CONTEXT_ATTR_TEMPLATE_ENGINE, engine);
		engine = null;
		resolver = null;

		if (logger.isTraceEnabled()) {

			logger.trace("-------- Loaded properties ---------------------------------");
			cfgProps.forEach((k, v) -> {
				if (!String.valueOf(k).startsWith("key."))
					logger.trace("\t{}: {}", k, v);
			});
			logger.trace("-------- Servlet Registrations -----------------------------");
			ctx.getServletRegistrations() //
					.forEach((name, reg) -> {

						logger.trace("Servlet name: " + name);
						logger.trace("\tRegistered class: " + reg.getClassName());
						if (reg.getMappings().size() > 0) {

							logger.trace("\tMapping(s): ");
							reg.getMappings().forEach((m) -> logger.trace("\t\t{}", m));
						}

						if (reg.getInitParameters().size() > 0) {

							logger.trace("\tInit parameters:");
							reg.getInitParameters().forEach((k, v) -> logger.trace("\t\t{}: {}", k, v));
						}
					});
			logger.trace("-------- Filter Registrations ------------------------------");
			ctx.getFilterRegistrations() //
					.forEach((name, reg) -> {

						logger.trace("Filter name: " + name);
						logger.trace("\tRegistered class: " + reg.getClassName());
						if (reg.getServletNameMappings().size() > 0) {

							logger.trace("\tServlet mapping(s):");
							reg.getServletNameMappings().forEach((m) -> logger.trace("\t\t" + m));
						}
						if (reg.getUrlPatternMappings().size() > 0) {

							logger.trace("\tURL pattern mapping(s):");
							reg.getUrlPatternMappings().forEach((m) -> logger.trace("\t\t" + m));
						}
						if (reg.getInitParameters().size() > 0) {

							logger.trace("\tInit parameters:");
							reg.getInitParameters().forEach((k, v) -> logger.trace("\t\t{}: {}", k, v));
						}
					});
			logger.trace("------------------------------------------------------------");
			logger.info("Application {} initialized.", ctx.getContextPath());
		}
	}
}