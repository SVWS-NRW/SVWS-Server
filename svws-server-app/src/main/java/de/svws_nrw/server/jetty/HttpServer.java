package de.svws_nrw.server.jetty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

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
import org.eclipse.jetty.servlet.ServletMapping;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import de.svws_nrw.api.RestAppClient;
import de.svws_nrw.api.RestAppDebug;
import de.svws_nrw.api.RestAppSchemaRoot;
import de.svws_nrw.api.RestAppServer;
import de.svws_nrw.config.SVWSKonfiguration;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Application;


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
		final LoginService loginService = new SVWSLoginService("Authentifizierung bei dem SVWS-Server");
		server.addBean(loginService);
		final ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        server.setHandler(security);

		// openapi.json darf durchgelassen werden. Wird zum Erstellen des Client benötigt
		final Constraint oa_constraint = new Constraint();
		oa_constraint.setName("pass_openapi.json");
		oa_constraint.setAuthenticate(false);
		final ConstraintMapping oa_mapping = new ConstraintMapping();
		oa_mapping.setPathSpec("/openapi.json");
		oa_mapping.setConstraint(oa_constraint);
		final ConstraintMapping oa_mapping2 = new ConstraintMapping();
		oa_mapping2.setPathSpec("/api/schema/root/openapi.json");
		oa_mapping2.setConstraint(oa_constraint);
		security.setConstraintMappings(List.of(oa_mapping, oa_mapping2));

        final Constraint constraint = new Constraint();
        constraint.setName("auth");
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[] { "user", "admin" });
        final ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);
        mapping.setMethodOmissions(new String[] { HttpMethod.OPTIONS });
        mapping.setConstraint(constraint);
        security.addConstraintMapping(mapping);

        security.setAuthenticator(new SVWSAuthenticator());
        security.setLoginService(loginService);

		// Handler Structure
		final HandlerCollection handlers = new HandlerCollection();
		contexts = new ContextHandlerCollection();
		handlers.setHandlers(new Handler[] { contexts, new DefaultHandler() });
		security.setHandler(handlers);

		// logging
		if (SVWSKonfiguration.get().isLoggingEnabled()) {
			final String logPath = SVWSKonfiguration.get().getLoggingPath();
			try {
				Files.createDirectories(Paths.get(logPath));
			} catch (final IOException e) {
				e.printStackTrace();
			}
			final RequestLogWriter logWriter = new RequestLogWriter(logPath + "/yyyy_mm_dd.request.log");
			logWriter.setFilenameDateFormat("yyyy_MM_dd");
			logWriter.setRetainDays(90);
			logWriter.setAppend(true);
			logWriter.setTimeZone("GMT");
			final CustomRequestLog requestLog = new CustomRequestLog(logWriter, CustomRequestLog.EXTENDED_NCSA_FORMAT);
			final RequestLogHandler requestLogHandler = new RequestLogHandler();
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
		final boolean disableTLS = SVWSKonfiguration.get().isTLSDisabled();

		// HTTP Configuration
		final HttpConfiguration http_config = new HttpConfiguration();
		if (!disableTLS) {
			http_config.setSecureScheme("https");
			http_config.setSecurePort(SVWSKonfiguration.get().getPortHTTPS());
		}
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);

		// SSL Context Factory
		final SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
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
		final HttpConfiguration https_config = new HttpConfiguration(http_config);
		final SecureRequestCustomizer secureRequestCustomizer = new SecureRequestCustomizer();
		if (!disableTLS) {
			secureRequestCustomizer.setSniHostCheck(false);
		}
		https_config.addCustomizer(secureRequestCustomizer);

		// HTTP Connection Factory (v1.1 oder v2)
		final AbstractConnectionFactory connFactoryHTTPv1_1 = new HttpConnectionFactory(https_config);
		final AbstractConnectionFactory connFactoryHTTPv2 = new HTTP2ServerConnectionFactory(https_config);
		final ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
		if (disableTLS) {
			@SuppressWarnings("resource")
			final
			ServerConnector connector = SVWSKonfiguration.get().useHTTPDefaultv11()
				? new ServerConnector(server, connFactoryHTTPv1_1, connFactoryHTTPv2)
				: new ServerConnector(server, connFactoryHTTPv2, connFactoryHTTPv1_1);
			connector.setPort(SVWSKonfiguration.get().getPortHTTP());
			server.addConnector(connector);
		} else {
			@SuppressWarnings("resource")
			final
			ServerConnector connector = SVWSKonfiguration.get().useHTTPDefaultv11()
				? new ServerConnector(server, sslContextFactory, alpn, connFactoryHTTPv1_1, connFactoryHTTPv2)
				: new ServerConnector(server, sslContextFactory, alpn, connFactoryHTTPv2, connFactoryHTTPv1_1);
			connector.setPort(SVWSKonfiguration.get().getPortHTTPS());
			server.addConnector(connector);
		}
	}


	/**
	 * Initialisiert den Jetty-Http-Server und fügt den Login-Service {@link SVWSLoginService}
	 * und die Http-Konfiguration basieren auf der {@link SVWSKonfiguration} hinzu.
	 */
	public static void init() {
		// Create a server with a threadpool of max. 500 threads
		final QueuedThreadPool threadPool = new QueuedThreadPool();
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
		addAPIApplications();
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
	 * Fügt die angegebene API-Applikation zum Server hinzu.
	 *
	 * @param c           die Applikation
	 * @param pathSpecs   die Pfad-Spezifikationen
	 */
	private static void addApplication(final Class<? extends Application> c, final String... pathSpecs) {
		final ServletHolder servlet = context_handler.addServlet(HttpServletDispatcher.class, pathSpecs[0]);
		final ServletMapping mapping = servlet.getServletHandler().getServletMapping(pathSpecs[0]);
		mapping.setPathSpecs(pathSpecs);
		// TODO user Logger instead of System.out
		System.out.println("Registriere API-Applikation " + c.getSimpleName() + ": " + Arrays.toString(mapping.getPathSpecs()));
		servlet.setInitParameter("jakarta.ws.rs.Application", c.getCanonicalName());
	}

	/**
	 * Fügt die Rest-Applikationen zum Server hinzu.
	 */
	private static void addAPIApplications() {
		addApplication(RestAppServer.class, "/db/*", "/config/*", "/status/*", "/api/*", "/openapi/server.json", "/openapi/server.yaml");
		addApplication(RestAppClient.class, "/*");
		addApplication(RestAppDebug.class, "/debug/*");
		if (!SVWSKonfiguration.get().isDBRootAccessDisabled())
			addApplication(RestAppSchemaRoot.class, "/api/schema/root/*", "/openapi/schemaroot.json", "/openapi/schemaroot.yaml");
	}

}
