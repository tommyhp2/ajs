/**
 * 
 */
package com.sointe.ajs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.thymeleaf.TemplateEngine;

/**
 * @author tommy
 *
 */
public class AjsContext {

	public static enum ConfigStatus {
		// NOT_CONFIGURED, // new setup
		SETUP_1, // h2 config DB
		SETUP_2, // DB for storing application configuration
		SETUP_3, // Datasources configuration
		CONFIGURED, //
		MAINTENANCE, //
		ERROR
	}

	public static final String APP_CONTEXT_ATTR_TEMPLATE_ENGINE = "__tlTemplateEngine__";

	// ------------------------------------------------------------------------
	/**
	 * Return the real path to the context root.
	 * ie: /usr/local/apache-tomcat/webapps/myApp/
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getAppRealPath(ServletContext ctx) {
		return ctx.getRealPath("/").replace('\\', '/');
	}

	public static String getAppRealPath(ServletContext ctx, String path) {
		return ctx.getRealPath(path).replace('\\', '/');
	}

	/**
	 * JDBC URL for H2's encrypted local 'config' database.
	 * 
	 * @param ctx
	 * @return String JDBC URL for H2 database
	 */
	public static String getConfigDbURL(ServletContext ctx) {
		return "jdbc:h2:".concat(getAppRealPath(ctx)).concat("WEB-INF/config;CIPHER=AES");
	}

	// ------------------------------------------------------------------------
	// Config Properties in context attributes
	// ------------------------------------------------------------------------
	/**
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static String getConfigProperty(ServletContext ctx, String key) {
		return ((Properties) ctx.getAttribute("__configProperties__")).getProperty(key);
	}

	/**
	 * @param ctx
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigProperty(ServletContext ctx, String key, String defaultValue) {
		return ((Properties) ctx.getAttribute("__configProperties__")).getProperty(key, defaultValue);
	}

	/**
	 * @param ctx
	 * @return
	 */
	public static boolean hasConfigProperties(ServletContext ctx) {
		return ctx.getAttribute("__configProperties__") != null //
				&& !((Properties) ctx.getAttribute("__configProperties__")).isEmpty();
	}

	/**
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static boolean hasConfigProperty(ServletContext ctx, String key) {
		return ctx.getAttribute("__configProperties__") != null //
				&& ((Properties) ctx.getAttribute("__configProperties__")).getProperty(key) != null;
	}

	/**
	 * @param ctx
	 * @param key
	 * @return
	 */
	public static Object removeConfigProperty(ServletContext ctx, Object key) {
		return ((Properties) ctx.getAttribute("__configProperties__")).remove(key);
	}

	/**
	 * @param ctx
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean removeConfigProperty(ServletContext ctx, Object key, Object value) {
		return ((Properties) ctx.getAttribute("__configProperties__")).remove(key, value);
	}

	/**
	 * @param ctx
	 * @param props
	 */
	public static void setConfigProperties(ServletContext ctx, Properties props) {
		if (ctx.getAttribute("__configProperties__") == null)
			ctx.setAttribute("__configProperties__", props);
	}
	// ------------------------------------------------------------------------
	// Thymeleaf
	// ------------------------------------------------------------------------
	/**
	 * @param ctx
	 * @return
	 */
	public static TemplateEngine getThymeleafTemplateEngine(ServletContext ctx) {
		return (TemplateEngine) ctx.getAttribute(APP_CONTEXT_ATTR_TEMPLATE_ENGINE);
	}
	// ------------------------------------------------------------------------
	// Config status
	// ------------------------------------------------------------------------
	/**
	 * @param ctx
	 * @return
	 */
	public synchronized static ConfigStatus getConfigStatus(ServletContext ctx) {
		return (ConfigStatus) ctx.getAttribute("__configStatus__");
	}

	/**
	 * @param ctx
	 * @param status
	 */
	public synchronized static void setConfigStatus(ServletContext ctx, ConfigStatus status) {
		ctx.setAttribute("__configStatus__", status);
	}
	// ------------------------------------------------------------------------
	// DataSource
	// ------------------------------------------------------------------------
	/**
	 * @param ctx
	 * @return
	 */
	public static DataSource getDataSource(ServletContext ctx) {
		return (DataSource) ctx.getAttribute("__dataSource__");
	}

	/**
	 * @param ctx
	 * @return
	 */
	public static boolean hasDataSource(ServletContext ctx) {
		return ctx.getAttribute("__dataSource__") instanceof DataSource;
	}
	
	/**
	 * @param ctx
	 * @return
	 * @throws SQLException 
	 */
	public static Connection getConnection(ServletContext ctx) throws SQLException {
		return getDataSource(ctx).getConnection();
	}
	// ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	/**
	 * String text for the give status code:
	 * 
	 * 200 = SC_OK
	 * 404 = SC_NOT_FOUND
	 * 
	 * @param javax.servlet.http.HttpServletResponse.statusCode
	 * @return
	 */
	public static String getHttpStatus(int status) {
		switch (status) {
		case HttpServletResponse.SC_ACCEPTED: // 202
			return "SC_ACCEPTED";
		case HttpServletResponse.SC_BAD_GATEWAY: // 502
			return "SC_BAD_GATEWAY";
		case HttpServletResponse.SC_BAD_REQUEST: // 400
			return "SC_BAD_REQUEST";
		case HttpServletResponse.SC_CONFLICT: // 409
			return "SC_CONFLICT";
		case HttpServletResponse.SC_CONTINUE: // 100
			return "SC_CONTINUE";
		case HttpServletResponse.SC_CREATED: // 201
			return "SC_CREATED";
		case HttpServletResponse.SC_EXPECTATION_FAILED: // 417
			return "SC_EXPECTATION_FAILED";
		case HttpServletResponse.SC_FORBIDDEN: // 403
			return "SC_FORBIDDEN";
		case HttpServletResponse.SC_GATEWAY_TIMEOUT: // 504
			return "SC_GATEWAY_TIMEOUT";
		case HttpServletResponse.SC_GONE: // 410
			return "SC_GONE";
		case HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED: // 505
			return "SC_HTTP_VERSION_NOT_SUPPORTED";
		case HttpServletResponse.SC_INTERNAL_SERVER_ERROR: // 500
			return "SC_INTERNAL_SERVER_ERROR";
		case HttpServletResponse.SC_LENGTH_REQUIRED: // 411
			return "SC_LENGTH_REQUIRED";
		case HttpServletResponse.SC_METHOD_NOT_ALLOWED: // 405
			return "SC_METHOD_NOT_ALLOWED";
		case HttpServletResponse.SC_MOVED_PERMANENTLY: // 301
			return "SC_MOVED_PERMANENTLY";
//		case HttpServletResponse.SC_FOUND: // 302 
		case HttpServletResponse.SC_MOVED_TEMPORARILY: // 302
			return "SC_MOVED_TEMPORARILY";
//			return "SC_MOVED_TEMPORARILY / SC_FOUND";
		case HttpServletResponse.SC_MULTIPLE_CHOICES: // 300
			return "SC_MULTIPLE_CHOICES";
		case HttpServletResponse.SC_NO_CONTENT: // 204
			return "SC_NO_CONTENT";
		case HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION: // 203
			return "SC_NON_AUTHORITATIVE_INFORMATION";
		case HttpServletResponse.SC_NOT_ACCEPTABLE: // 406
			return "SC_NOT_ACCEPTABLE";
		case HttpServletResponse.SC_NOT_FOUND: // 404
			return "SC_NOT_FOUND";
		case HttpServletResponse.SC_NOT_IMPLEMENTED: // 501
			return "SC_NOT_IMPLEMENTED";
		case HttpServletResponse.SC_NOT_MODIFIED: // 304
			return "SC_NOT_MODIFIED";
		case HttpServletResponse.SC_OK: // 200
			return "SC_OK";
		case HttpServletResponse.SC_PARTIAL_CONTENT: // 206
			return "SC_PARTIAL_CONTENT";
		case HttpServletResponse.SC_PAYMENT_REQUIRED: // 402
			return "SC_PAYMENT_REQUIRED";
		case HttpServletResponse.SC_PRECONDITION_FAILED: // 412
			return "SC_PRECONDITION_FAILED";
		case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED: // 407
			return "SC_PROXY_AUTHENTICATION_REQUIRED";
		case HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE: // 413
			return "SC_REQUEST_ENTITY_TOO_LARGE";
		case HttpServletResponse.SC_REQUEST_TIMEOUT: // 408
			return "SC_REQUEST_TIMEOUT";
		case HttpServletResponse.SC_REQUEST_URI_TOO_LONG: // 414
			return "SC_REQUEST_URI_TOO_LONG";
		case HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE: // 416
			return "SC_REQUESTED_RANGE_NOT_SATISFIABLE";
		case HttpServletResponse.SC_RESET_CONTENT: // 205
			return "SC_RESET_CONTENT";
		case HttpServletResponse.SC_SEE_OTHER: // 303
			return "SC_SEE_OTHER";
		case HttpServletResponse.SC_SERVICE_UNAVAILABLE: // 503
			return "SC_SERVICE_UNAVAILABLE";
		case HttpServletResponse.SC_SWITCHING_PROTOCOLS: // 101
			return "SC_SWITCHING_PROTOCOLS";
		case HttpServletResponse.SC_TEMPORARY_REDIRECT: // 307
			return "SC_TEMPORARY_REDIRECT";
		case HttpServletResponse.SC_UNAUTHORIZED: // 401
			return "SC_UNAUTHORIZED";
		case HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE: // 415
			return "SC_UNSUPPORTED_MEDIA_TYPE";
		case HttpServletResponse.SC_USE_PROXY: // 305
			return "SC_USE_PROXY";
//		case HttpServletResponse.: // 
//			return "";
//			break;
		default:
			return "UNKNOWN: " + status;
		}
	}
}