package de.nrw.schule.svws.server.jetty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Application;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.server.AbstractConnectionFactory;
import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.RequestLogWriter;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import de.nrw.schule.svws.config.SVWSKonfiguration;


/**
 * Diese Klasse repräsentiert den Http-Server des SVWS-Server-Projektes,
 * welcher auf einer Embedded-Version des Jetty-Servers basiert.
 */
public class HttpServer {
	
	/** Die Instanz des Jetty-Servers (siehe {@link Server}) */
	private static Server server;
	
	/** Die Sammlung der im Server verwendeten Context-Handler (siehe {@link ContextHandlerCollection}) */
	private static ContextHandlerCollection contexts;
	
	/** Der im Server verwendete {@link ServletContextHandler} */
	private static ServletContextHandler context_handler;
	

	
	/**
	 * Fügt einen neuen {@link SVWSLoginService} zum Server hinzu und konfiguriert diesen basierend
	 * auf der {@link SVWSKonfiguration}.
	 */
	private static void addLoginService() {
		// Login Service
		LoginService loginService = new SVWSLoginService("Authentifizierung bei dem SVWS-Server");
		server.addBean(loginService);
		ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        server.setHandler(security);
        
		// openapi.json darf durchgelassen werden. Wird zum Erstellen des Client benötigt
		Constraint oa_constraint = new Constraint();
		oa_constraint.setName("pass_openapi.json");
		oa_constraint.setAuthenticate(false);
		ConstraintMapping oa_mapping = new ConstraintMapping();
		oa_mapping.setPathSpec("/openapi.json");
		oa_mapping.setConstraint(oa_constraint);
		security.setConstraintMappings(Collections.singletonList(oa_mapping));
        
        Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[] { "user", "admin" });
        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);
        mapping.setMethodOmissions(new String[] { HttpMethod.OPTIONS });
        mapping.setConstraint(constraint);
        security.addConstraintMapping(mapping);
        
        security.setAuthenticator(new SVWSAuthenticator());
        security.setLoginService(loginService);
        
		// Handler Structure
		HandlerCollection handlers = new HandlerCollection();
		contexts = new ContextHandlerCollection();
		handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
		security.setHandler(handlers);

		// logging
		if (SVWSKonfiguration.get().isLoggingEnabled()) {
			String logPath = SVWSKonfiguration.get().getLoggingPath();
			try {
				Files.createDirectories(Paths.get(logPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			RequestLogWriter logWriter = new RequestLogWriter(logPath + "/yyyy_mm_dd.request.log");
			logWriter.setFilenameDateFormat("yyyy_MM_dd");
			logWriter.setRetainDays(90);
			logWriter.setAppend(true);
			logWriter.setTimeZone("GMT");
			CustomRequestLog requestLog = new CustomRequestLog(logWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT);
			RequestLogHandler requestLogHandler = new RequestLogHandler();
			requestLogHandler.setRequestLog(requestLog);
			handlers.addHandler(requestLogHandler);
		}

		// create a ServletContextHandle to support multiple servlets
		context_handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context_handler.setContextPath("/");
		context_handler.setResourceBase(System.getProperty("java.io.tmpdir"));
		contexts.addHandler(context_handler);
	}


	/**
	 * Erzeugt eine neue HTTP- bzw. HTTPS-Konfiguration basierend auf der {@link SVWSKonfiguration} 
	 * und fügt diese zum Jetty-Server hinzu.
	 */
	private static void addHTTPConfiguration() {
		// HTTP Configuration
		HttpConfiguration http_config = new HttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(SVWSKonfiguration.get().getPortHTTPS());
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);

		// SSL Context Factory
		SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStorePath(SVWSKonfiguration.get().getTLSKeystorePath() + "/keystore");
		sslContextFactory.setKeyStorePassword(SVWSKonfiguration.get().getTLSKeystorePassword());
		sslContextFactory.setKeyManagerPassword(SVWSKonfiguration.get().getTLSKeystorePassword());
		sslContextFactory.setTrustStorePath(SVWSKonfiguration.get().getTLSKeystorePath() + "/keystore");
		sslContextFactory.setTrustStorePassword(SVWSKonfiguration.get().getTLSKeystorePassword());
		sslContextFactory.setIncludeProtocols("TLSv1.3", "TLSv1.2");
		sslContextFactory.setIncludeCipherSuites("TLS_AES_256_GCM_SHA384", "TLS_CHACHA20_POLY1305_SHA256", "TLS_AES_128_GCM_SHA256", 
				"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256");
		sslContextFactory.setSniRequired(false);
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

		// SSL HTTP Configuration
		HttpConfiguration https_config = new HttpConfiguration(http_config);
		SecureRequestCustomizer secureRequestCustomizer = new SecureRequestCustomizer();
		secureRequestCustomizer.setSniHostCheck(false);
		https_config.addCustomizer(secureRequestCustomizer);

		// HTTP Connection Factory (v1.1 oder v2)		
		AbstractConnectionFactory connFactoryHTTPv1_1 = new HttpConnectionFactory(https_config);
		AbstractConnectionFactory connFactoryHTTPv2 = new HTTP2ServerConnectionFactory(https_config);
		ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
		@SuppressWarnings("resource")
		ServerConnector connector = new ServerConnector(server, sslContextFactory, alpn, connFactoryHTTPv2, connFactoryHTTPv1_1);
		connector.setPort(SVWSKonfiguration.get().getPortHTTPS());
		server.addConnector(connector);				
	}
	
	
	/**
	 * Initialisiert den Jetty-Http-Server und fügt den Login-Service {@link SVWSLoginService}
	 * und die Http-Konfiguration basieren auf der {@link SVWSKonfiguration} hinzu. 
	 */
	public static void init() {
		// Create a server with a threadpool of max. 500 threads
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);
		server = new Server(threadPool);
		server.addBean(new ScheduledExecutorScheduler());

		// Server extra options
		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);

		// Add several standard configurations for this application
		addLoginService();
		addHTTPConfiguration();
		
	}
	
	
	/**
	 * Started den Server und blockiert den aufrufende Thread bis
	 * der zum Jetty-Server gehörende {@link ThreadPool} gestoppt wurde.
	 * 
	 * @throws Exception   eine Exception beim Starten des Servers wird zurückgemeldet
	 */
	public static void start() throws Exception {
		// Start the server
		server.start();
		server.join();
	}
	

	/**
	 * Fügt eine OpenAPI-Applikation zum Server hinzu.
	 * 
	 * @param c  die hinzuzufügende OpenAPI-REST-{@link Application}
	 */
	public static void addOpenAPIApplication(Class<? extends Application> c) {
		ServletHolder servlet = context_handler.addServlet(HttpServletDispatcher.class, "/*");
		servlet.setInitParameter("jakarta.ws.rs.Application", c.getCanonicalName());		
	}
	
}
