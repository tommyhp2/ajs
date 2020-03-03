/**
 * 
 */
package com.sointe.web;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.sointe.ajs.AjsContext;

/**
 * @author tommy
 * 
 *         Abstract class to initialize to be used by Filters and Controllers.
 *
 */
/**
 * @author tommy
 *
 */
public abstract class AbstractWeb {

	private ServletContext ctx;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession ses;
//    private Cookie[] cookies;
	private int httpStatus;
	private String httpStatusText;
	
	private Subject shiroSubject;
	private Session shiroSession;

	public AbstractWeb() {
	}

	/**
	 * @param javax.servlet.ServletRequest
	 * @param javax.servlet.ServletResponse
	 */
	public AbstractWeb(ServletRequest req, ServletResponse resp) {
		initWeb(req, resp);
	}

	/**
	 * @param javax.servlet.http.HttpServletRequest
	 * @param javax.servlet.http.HttpServletResponse
	 */
	public AbstractWeb(HttpServletRequest req, HttpServletResponse resp) {
		initWeb(req, resp);
	}
	// ------------------------------------------------------------------------
	// init for Web
	// ------------------------------------------------------------------------
	
	/**
	 * Filters must set this first in Filter.doFilter() to use the various wrapper methods for:<br>
	 * <pre>
	 * javax.servlet.http.HttpServletRequest
	 * javax.servlet.http.HttpServletResponse
	 * </pre>
	 * @param javax.servlet.ServletRequest
	 * @param javax.servlet.ServletResponse
	 */
	protected void initWeb(ServletRequest req, ServletResponse resp) {
		this.req = (HttpServletRequest) req;
		this.resp = (HttpServletResponse) resp;
		this.initWeb();
	}

	/**
	 * @param javax.servlet.http.HttpServletRequest
	 * @param javax.servlet.http.HttpServletResponse
	 */
	protected void initWeb(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
		this.initWeb();
	}

	private void initWeb() {
		this.ctx = req.getServletContext();
	}
	// ------------------------------------------------------------------------
	// Servlet Context
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.ServletContext
	 */
	protected ServletContext getServletContext() {
		return ctx;
	}
	
	/**
	 * Filters must set this first in Filter.init() use the various wrapper ServletContext's methods
	 * 
	 * @param javax.servlet.ServletContext
	 */
	protected void setServletContext(ServletContext ctx) {
		this.ctx = ctx;
	} 

	/**
	 * @return javax.servlet.ServletContext.getContextPath()
	 */
	protected String getContextPath() {
		return ctx.getContextPath();
	}

	/**
	 * @return
	 */
//	protected ConfigStatus getConfigStatus() {
//		return AppContext.getConfigStatus(ctx);
//	}
	// ------------------------------------------------------------------------
	// Http Servlet Request
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.http.HttpServletRequest
	 */
	protected HttpServletRequest getRequest() {
		return req;
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getMethod()
	 */
	protected String getMethod() {
		return req.getMethod();
	}

	/**
	 * @param name
	 * @return javax.servlet.http.HttpServletRequest.getParameter()
	 */
	protected String[] getParameter(String name) {
		return getParameterMap().get(name);
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getParameterMap()
	 */
	protected Map<String, String[]> getParameterMap() {
		return req.getParameterMap();
	}
	
	/**
	 * @param name
	 * @return javax.servlet.http.HttpServletRequest.getParameterMap().containsKey(name)
	 */
	protected boolean hasParameter(String name) {
		return req.getParameterMap().containsKey(name);
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getRemoteUser()
	 */
	protected String getRemoteUser() {
		return req.getRemoteUser();
	}

	/**
	 * @return javax.servlet.http.HttpServletRequest.getRemoteUser() == null
	 */
	protected boolean hasRemoteUser() {
		return getRemoteUser() != null;
	}
	
	protected String getRequestedSessionId() {
		return req.getRequestedSessionId(); 
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.isRequestedSessionIdFromCookie()
	 */
	protected boolean isRequestedSessionIdFromCookie() {
		return req.isRequestedSessionIdFromCookie();
	}

	/**
	 * @return javax.servlet.http.HttpServletRequest.isRequestedSessionIdFromURL()
	 */
	protected boolean isRequestedSessionIdFromURL() {
		return req.isRequestedSessionIdFromURL();
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.isRequestedSessionIdValid()
	 */
	protected boolean isRequestedSessionIdValid() {
		return req.isRequestedSessionIdValid();
	}
	
	/**
	 * @return javax.servlet.http.HttpServletRequest.getRequestURI()
	 */
	protected String getRequestURI() {
		return req.getRequestURI();
	}
	// ------------------------------------------------------------------------
	// Http Servlet Response
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.http.HttpServletResponse
	 */
	protected HttpServletResponse getResponse() {
		return resp;
	}
	
	/**
	 * @see javax.servlet.http.HttpServletResponse.sendError()
	 * 
	 * @param sc
	 * @throws IOException
	 */
	protected void sendError(int sc) throws IOException {
		resp.sendError(sc);
	}

	/**
	 * @see javax.servlet.http.HttpServletResponse.sendError()
	 * 
	 * @param sc
	 * @param msg
	 * @throws IOException
	 */
	protected void sendError(int sc, String msg) throws IOException {
		resp.sendError(sc, msg);
	}

	/**
	 * @return javax.servlet.http.HttpServletResponse.httpStatus
	 */
	protected int getHttpStatus() {
		return httpStatus;
	}

	/**
	 * @return text of javax.servlet.http.HttpServletResponse.httpStatus
	 */
	protected String getHttpStatusText() {
		return httpStatusText;
	}

	/**
	 * @param javax.servlet.http.HttpServletResponse.httpStatus
	 */
	protected void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
		this.httpStatusText = AjsContext.getHttpStatus(httpStatus);
	}
	// ------------------------------------------------------------------------
	// Http Session
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.http.HttpSession
	 */
	protected HttpSession getSession() {
		return ses;
	}

	/**
	 * Default of javax.servlet.http.HttpServletRequest.getSession()
	 * 
	 * @return javax.servlet.http.HttpSession
	 */
	protected HttpSession getSessionDefault() {
		ses = req.getSession();
		return ses;
	}
	
	/**
	 * Use this first by passing false to verify session then use getSession() to get actual session for processing.
	 * 
	 * @param create
	 * @return javax.servlet.http.HttpSession
	 */
	protected HttpSession getSession(boolean create) {
		ses = req.getSession(create);
		return ses;
	}
	
	/**
	 * @param name
	 * @return javax.servlet.http.HttpSession.getAttribute()
	 */
	protected Object getSessionAttribute(String name) {
		return ses.getAttribute(name);
	}
	
	protected boolean hasSessionAttribute(String name) {
		return ses.getAttribute(name) != null;
	}
	
	/**
	 * javax.servlet.http.HttpSession.setAttribute()
	 * 
	 * @param name
	 * @param value
	 */
	protected void setSessionAttribute(String name, Object value) {
		ses.setAttribute(name, value);
	}
	// ------------------------------------------------------------------------
	/**
	 * @return javax.servlet.SessionCookieConfig
	 */
	protected SessionCookieConfig getSessionCookieConfig() {
		return ctx.getSessionCookieConfig();
	}

	/**
	 * @return javax.servlet.SessionCookieConfig().getName();
	 */
	protected String getSessionCookieName() {
		return getSessionCookieConfig().getName() == null ? "JSESSIONID" : getSessionCookieConfig().getName();
	}

	/**
	 * @return javax.servlet.http.Cookie
	 */
	protected Cookie getSessionIdCookie() {
		if (getSession() == null) {
			getSessionDefault();
		}
		Cookie c = new Cookie(getSessionCookieName(), getSession().getId());
		c.setHttpOnly(true);
		c.setSecure(true);
		return c;
	}

	protected boolean hasCookie(String name) {
		if (getCookies() != null) {
			for (Cookie c : getCookies()) {
				if (c.getName().contentEquals(name)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @return javax.servlet.http.Cookie[]
	 */
	protected Cookie[] getCookies() {
		return req.getCookies();
	}
	
	/**
	 * javax.servlet.http.HttpServletResponse.addCookie()
	 * 
	 * @param name
	 */
	protected void removeCookie(String name) {
		Cookie c = new Cookie(name, "");
		c.setMaxAge(0);
		resp.addCookie(c);
	}
	// ------------------------------------------------------------------------

	/**
	 * @return org.apache.shiro.subject.Subject
	 */
	protected Subject getShiroSubject() {
		return shiroSubject;
	}
	
	/**
	 * The current user object.
	 * 
	 * @param org.apache.shiro.subject.Subject
	 */
	protected void setShiroSubject(Subject subject) {
		shiroSubject = subject;
	}
	/**
	 * @return org.apache.shiro.session.Session
	 */
	protected Session getShiroSession() {
		return shiroSession;
	}
	
	/**
	 * @param create
	 * @return org.apache.shiro.session.Session
	 */
	protected Session getShiroSession(boolean create) {
		shiroSession = shiroSubject.getSession(create);
		return shiroSession;
	}
	
	/**
	 * @param key
	 * @return org.apache.shiro.session.Session.getAttribute()
	 */
	protected Object getShiroSessionAttribute(Object key) {
		return shiroSession.getAttribute(key);
	}

	/**
	 * org.apache.shiro.session.Session.setAttribute()
	 * 
	 * @param key
	 * @param value
	 */
	protected void setShiroSessionAttribute(Object key, Object value) {
		shiroSession.setAttribute(key, value);
	}
	
	/**
	 * @param key
	 * @return
	 */
	protected boolean hasShiroSessionAttribute(Object key) {
		return shiroSession.getAttribute(key) != null;
	}
	// ------------------------------------------------------------------------

	protected void validateSessionShiro() {
		// Use the shiro.ini file at the root of the classpath
		// (file: and url: prefixes load from files and urls respectively):
//		IniRealm iniRealm = new IniRealm("classpath:shiro.ini");
//		SecurityManager securityManager = new DefaultSecurityManager();

		// for this simple example quickstart, make the SecurityManager
		// accessible as a JVM singleton. Most applications wouldn't do this
		// and instead rely on their container configuration or web.xml for
		// webapps. That is outside the scope of this simple quickstart, so
		// we'll just do the bare minimum so you can continue to get a feel
		// for things.

//		SecurityUtils.setSecurityManager(securityManager);
//		SecurityUtils.setSecurityManager(new DefaultSecurityManager());
//		Factory<SecurityManager> factory = new IniSecurityManagerFactory();
//		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
//		SecurityManager securityManager = factory.getInstance();
//		SecurityUtils.setSecurityManager(securityManager);
//		SecurityUtils.setSecurityManager(ThreadContext.getSecurityManager());

		// Now that a simple Shiro environment is set up, let's see what you can do:
		// get the currently executing user:
//		Subject currentUser = SecurityUtils.getSubject();
		setShiroSubject(SecurityUtils.getSubject());

		// Do some stuff with a Session (no need for a web or EJB container!!!)
//		Session session = currentUser.getSession();
		if (getShiroSession(false) == null) {
			getShiroSession(true);
		}
	}

	protected void validSessionJava() {
		if (getSession(false) == null) {
			getSession(true);
			Cookie c = getSessionIdCookie();
			getResponse().addCookie(c);
		}
	}
	// ------------------------------------------------------------------------
	
	/**
	 * Release all resources by assigning null to local properties of:<br>
	 * <pre>
	 * javax.servlet.ServletContext
	 * javax.servlet.http.HttpServletRequest
	 * javax.servlet.http.HttpServletResponse
	 * javax.servlet.http.HttpSession
	 * </pre>
	 */
	protected void close() {
		ctx = null;
		req = null;
		resp = null;
		ses = null;
	}
}