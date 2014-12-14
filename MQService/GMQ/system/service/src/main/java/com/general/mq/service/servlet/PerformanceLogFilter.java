package com.general.mq.service.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.general.mq.common.logger.MQLogger;

/**
 * A simple filter to log the time taken to execute a request. Logging is carried
 * out via log4j to give the flexibility to add other data (such as current time)
 * and format the log as required.
 */

public class PerformanceLogFilter implements Filter {

	/**
	 * An optional regular expression to use as a filter for the servlet patch.
	 * Gives more flexibility than the standard servlet url-filter.
	 * All requests are logged if not specified.
	 * 
	 * */
	private static final String URL_FILTER_PARAM = "url-filter";

	/**
	 * An optional log4j category to use. The fully qualified class name
	 * of the filter will be used if not specified.
	 */
	private static final String LOG_CATEGORY_PARAM = "log-category";

	private Logger perfLogger;
	private String urlFilter;

	public void init(FilterConfig config) throws ServletException {

		String logCategory = config.getInitParameter(LOG_CATEGORY_PARAM);
		if (logCategory == null) {
			this.getClass().getName();
		}
		perfLogger = LoggerFactory.getLogger(logCategory);
		urlFilter = config.getInitParameter(URL_FILTER_PARAM);
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {

		long startTime;
		long endTime;
		String path = ((HttpServletRequest) request).getServletPath();

		if (urlFilter == null || path.matches(urlFilter)) {

			startTime = System.currentTimeMillis();

			chain.doFilter(request, response);
			endTime = System.currentTimeMillis();

			//Log the servlet path and time taken
			perfLogger.info(path + " => " + (endTime - startTime));
		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		MQLogger.l.info("Performance Log Filter Servlet Destroyed");
	}
}
