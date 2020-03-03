/**
 * 
 */
package com.sointe.web;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.cache.ICacheManager;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.sointe.ajs.AjsContext;

/**
 * @author tommy
 *
 */
public abstract class AbstractController extends AbstractWeb {

//    private static final Logger logger = LogManager.getLogger(AppController.class);

	private TemplateEngine templateEngine;
	private ICacheManager cacheManager;
	private Set<IDialect> dialects;
	private Set<IMessageResolver> messageResolvers;
	private Set<ITemplateResolver> templateResolvers;

	/**
	 */
	public AbstractController() {
		super();
	}

	/**
	 * @param javax.servlet.ServletRequest
	 * @param javax.servlet.ServletResponse
	 */
	public AbstractController(ServletRequest request, ServletResponse response) {
		super(request, response);
		initController();
	}

	/**
	 * @param javax.servlet.http.HttpServletRequest
	 * @param javax.servlet.http.HttpServletResponse
	 */
	public AbstractController(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
		initController();
	}

	// ------------------------------------------------------------------------
	protected void initController() {
		templateEngine = AjsContext.getThymeleafTemplateEngine(getServletContext());
		cacheManager = templateEngine.getCacheManager();
		dialects = templateEngine.getDialects();
		messageResolvers = templateEngine.getMessageResolvers();
		templateResolvers = templateEngine.getTemplateResolvers();
	}

	// ------------------------------------------------------------------------
	/**
	 * @return
	 */
	protected TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	/**
	 * @return
	 */
	protected ICacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @return
	 */
	protected Set<IDialect> getDialects() {
		return dialects;
	}

	/**
	 * @return
	 */
	protected IDialect getDialect() {
		Iterator<IDialect> iter = dialects.iterator();
		return dialects.size() == 1 && iter.hasNext() ? iter.next() : null;
	}

	/**
	 * @param name
	 * @return
	 */
	protected IDialect getDialect(String name) {
		Iterator<IDialect> iter = dialects.iterator();
		while (iter.hasNext()) {
			IDialect d = iter.next();
			if (d.getName().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	protected Set<IMessageResolver> getMessageResolvers() {
		return messageResolvers;
	}

	/**
	 * @return
	 */
	protected StandardMessageResolver getMessageResolver() {
		Iterator<IMessageResolver> iter = messageResolvers.iterator();
		return messageResolvers.size() == 1 && iter.hasNext() ? (StandardMessageResolver) iter.next() : null;
	}

	/**
	 * @param name
	 * @return
	 */
	protected IMessageResolver getMessageResolver(String name) {
		Iterator<IMessageResolver> iter = messageResolvers.iterator();
		while (iter.hasNext()) {
			IMessageResolver m = iter.next();
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	protected Set<ITemplateResolver> getTemplateResolvers() {
		return templateResolvers;
	}

	/**
	 * @return
	 */
	protected StringTemplateResolver getTemplateResolver() {
		Iterator<ITemplateResolver> iter = templateResolvers.iterator();
		return templateResolvers.size() == 1 && iter.hasNext() ? (StringTemplateResolver) iter.next() : null;
	}

	/**
	 * @param name
	 * @return
	 */
	protected ITemplateResolver getTemplateResolver(String name) {
		Iterator<ITemplateResolver> iter = templateResolvers.iterator();
		while (iter.hasNext()) {
			ITemplateResolver t = iter.next();
			if (t.getName().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	abstract public boolean execute();
	// ------------------------------------------------------------------------

}