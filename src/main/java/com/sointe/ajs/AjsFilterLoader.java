/**
 * 
 */
package com.sointe.ajs;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sointe.security.FilterAccessLog;
import com.sointe.security.FilterSecurity;
import com.sointe.security.FilterStaticFiles;
import com.sointe.web.AbstractFilter;

/**
 * @author tommy
 *
 *         https://stackoverflow.com/questions/7192834/how-to-add-filters-to-servlet-without-modifying-web-xml
 */
public class AjsFilterLoader extends AbstractFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(AjsFilterLoader.class);

	private Map<Pattern, Filter> filters = new LinkedHashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		setServletContext(filterConfig.getServletContext());

		FilterAccessLog fal = new FilterAccessLog();
		fal.init(filterConfig);
		filters.put(new Pattern("/*"), fal);

		FilterSecurity fs = new FilterSecurity();
		fs.init(filterConfig);
		filters.put(new Pattern("/*"), fs);

		FilterStaticFiles fsf = new FilterStaticFiles();
		fsf.init(filterConfig);
		filters.put(new Pattern("/*"), fsf);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		initWeb(request, response);
		String path = getRequestURI().substring(getContextPath().length());
		AjsFilterChain afc = new AjsFilterChain(chain);

		for (Entry<Pattern, Filter> e : filters.entrySet()) {
			if (e.getKey().matches(path)) {
				afc.addFilter(e.getValue());
			}
		}
		afc.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		for (Filter f : filters.values()) {
			f.destroy();
		}
	}

	private static class Pattern {

		private int position;
		private String url;

		public Pattern(String url) {
			this.position = url.startsWith("*") ? 1 : url.endsWith("*") ? -1 : 0;
			this.url = url.replaceAll("/?\\*", "");
		}

		public boolean matches(String path) {
			return (position == -1) ? path.startsWith(url) : (position == 1) ? path.endsWith(url) : path.equals(url);
		}
	}
}