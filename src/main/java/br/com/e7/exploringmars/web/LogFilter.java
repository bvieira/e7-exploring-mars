package br.com.e7.exploringmars.web;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFilter implements Filter{
	private final Logger servletLog;
	
	public LogFilter() {
		servletLog = LoggerFactory.getLogger("servlet");
	}
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//noop
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		final Instant start = Instant.now();
		try {
			chain.doFilter(req, resp);
		} catch(RuntimeException e) {
			servletLog.error("error on filter chain", e);
			throw e;
		} finally {
			final HttpServletRequest request = (HttpServletRequest) req;
	        final HttpServletResponse response = (HttpServletResponse) resp;
	        servletLog.info("source:[{}] method:[{}] uri:[{}] code:[{}] content-type:[{}] on {}", request.getRemoteHost(), request.getMethod(), request.getRequestURI(), response.getStatus(), response.getContentType(), Duration.between(start, Instant.now()).toMillis());
		}
	}

	@Override
	public void destroy() {
		//noop
	}

}
