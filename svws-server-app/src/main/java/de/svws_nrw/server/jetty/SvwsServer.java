package de.svws_nrw.server.jetty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.ServletMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.security.Constraint;
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
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ScheduledExecutorScheduler;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import de.svws_nrw.api.RestAppAdminClient;
import de.svws_nrw.api.RestAppClient;
import de.svws_nrw.api.RestAppDav;
import de.svws_nrw.api.RestAppDebug;
import de.svws_nrw.api.RestAppSchemaRoot;
import de.svws_nrw.api.RestAppServer;
import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.core.logger.Logger;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.Application;


/**
 * Diese Klasse repräsentiert den Http-Server des SVWS-Server-Projektes,
 * welcher auf einer Embedded-Version des Jetty-Servers basiert.
 */
public final class SvwsServer {

	/** Die Instanz dieses SVWS-Servers */
	private static SvwsServer _instance = null;

	/** Die Instanz des Jetty-Servers (siehe {@link Server}) */
	private final Server server;

	/** Der im Server verwendete {@link ServletContextHandler} */
	private final ServletContextHandler context_handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

	/** Die Menge der Handler, welche dem Security Context - Handler des Jetty-Server zugeordnet sind */
	private final Handler.Sequence handlerCollection = new Handler.Sequence();

	/** Die Menge der Context-Handler, welche zu den Handlern gehören */
	private final ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();

	/** Der Login-Service, welcher die Zugriffsberechtigungen auf die API prüft. */
	private final LoginService loginService = new SVWSLoginService("Authentifizierung bei dem SVWS-Server");


	/**
	 * Initialisiert den Jetty-Http-Server und fügt den Login-Service {@link SVWSLoginService}
	 * und die Http-Konfiguration basieren auf der {@link SVWSKonfiguration} hinzu.
	 */
	private SvwsServer() {
		// Create a server with a threadpool of max. 500 threads
		final QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMaxThreads(500);
		server = new Server(threadPool);
		server.addBean(new ScheduledExecutorScheduler());

		// Server extra options
		server.setDumpAfterStart(false);
		server.setDumpBeforeStop(false);
		server.setStopAtShutdown(true);

		// Registriere den Login Service beim Jetty-Server
		server.addBean(loginService);

		// Initialisieren den ServletContextHandler für die Unterstützung mehrerer Servlets
		context_handler.setContextPath("/");
		contextHandlerCollection.addHandler(context_handler);

		// Ordne die Context-Handler den allgemeinen Handlern zu
		handlerCollection.setHandlers(contextHandlerCollection, new DefaultHandler());

		// Erstelle einen ConstraintSecurityHandler mit dem Login-Service, etc.
		server.setHandler(createConstraintSecurityHandler(server, loginService, handlerCollection));

		// Konfiguriere das Logging für API-Zugriffe
		addLoggingHandler();

		// Konfiguriere und ergänze die HTTP-Verbindungen, auf welchen der Server lauscht
		addHTTPServerConnections();

		// Füge die einzelnen Servlets mit den verschiedenen APIs zum SVWS-Server hinzu.
		addAPIApplications();
	}


	/**
	 * Gibt die Instanz des SVWS-Servers zurück.
	 *
	 * @return die SVWS-Server-Instanz
	 */
	public static SvwsServer instance() {
		if (_instance == null)
			_instance = new SvwsServer();
		return _instance;
	}


	/**
	 * Started den Server und blockiert den aufrufende Thread bis
	 * der zum Jetty-Server gehörende {@link ThreadPool} gestoppt wurde.
	 *
	 * @throws Exception   eine Exception beim Starten des Servers wird zurückgemeldet
	 */
	public void start() throws Exception {
		// Start the server
		server.start();
		server.join();
	}


	private static ConstraintMapping getSecurityConstraintMapping() {
		final Constraint constraint = Constraint.from("auth", Constraint.Authorization.SPECIFIC_ROLE, "user", "admin");
		final ConstraintMapping mapping = new ConstraintMapping();
		mapping.setPathSpec("/*");
		mapping.setMethodOmissions(new String[] { HttpMethod.OPTIONS });
		mapping.setConstraint(constraint);
		return mapping;
	}


	private static ConstraintSecurityHandler createConstraintSecurityHandler(final Server server, final LoginService loginService,
			final Handler.Sequence handlerCollection) {
		final ConstraintSecurityHandler security = new ConstraintSecurityHandler();
		security.addConstraintMapping(getSecurityConstraintMapping());
		security.setAuthenticator(new SVWSAuthenticator());
		security.setLoginService(loginService);
		security.setHandler(handlerCollection);
		server.setHandler(security);
		return security;
	}


	/**
	 * Fügt einen Logging-Handler für die API-Anfragen hinzu
	 */
	private void addLoggingHandler() {
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
			server.setRequestLog(requestLog);
			server.setTempDirectory(System.getProperty("java.io.tmpdir"));
		}
	}


	/**
	 * Gibt die grundlegende HTTP-Konfiguration des Servers zurück.
	 *
	 * @return die HTTP-Konfiguration mit einigen Grundeinstellungen für den Server.
	 */
	private static HttpConfiguration getNewHttpConfiguration() {
		final HttpConfiguration http_config = new HttpConfiguration();
		http_config.setOutputBufferSize(32768);
		http_config.setRequestHeaderSize(8192);
		http_config.setResponseHeaderSize(8192);
		http_config.setSendServerVersion(true);
		http_config.setSendDateHeader(false);
		return http_config;
	}


	/**
	 * Gibt einen ServerConnector für das Lauschen auf HTTP-Verbindungen über HTTPv1 auf dem übergebenen Port zurück.
	 *
	 * @param port               der Port, auf dem gelauscht werden soll
	 * @param name               der eindeutige Name der dem Connector zugeordnet wird.
	 *
	 * @return der konfigurierte ServerConnector
	 */
	private ServerConnector getHttpServerConnector(final int port, final String name) {
		// HTTP Configuration
		final HttpConfiguration http_config = getNewHttpConfiguration();

		// HTTP Connection Factory (v1.1 bei HTTP)
		final AbstractConnectionFactory connFactoryHTTPv1_1 = new HttpConnectionFactory(http_config);
		final ServerConnector connector = new ServerConnector(server, connFactoryHTTPv1_1);
		connector.setName(name);
		connector.setPort(port);
		return connector;
	}


	/**
	 * Gibt einen ServerConnector für das Lauschen auf HTTP-Verbindungen auf dem übergebenen Port zurück.
	 *
	 * @param port               der Port, auf dem gelauscht werden soll
	 * @param name               der eindeutige Name der dem Connector zugeordnet wird.
	 * @param preferHTTPv1_1     gibt an, ob HTTP-Verbindungen mit v1.1 gegenüber v2 bevorzugt werden sollen
	 * @param keyStorePath       der Pfad zum Java-Key-Store für TLS-Verbindungen
	 * @param keyStorePassword   das Kennwort für den Java-Key-Store
	 * @param keyAlias           der Alias für das zu verwendende Zertifikat
	 *
	 * @return der konfigurierte ServerConnector
	 */
	private ServerConnector getHttpsServerConnector(final int port, final String name,
			final boolean preferHTTPv1_1, final String keyStorePath, final String keyStorePassword, final String keyAlias) {
		// HTTP Configuration
		final HttpConfiguration http_config = getNewHttpConfiguration();
		http_config.setSecureScheme("https");
		http_config.setSecurePort(port);

		// SSL Context Factory
		final SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
		sslContextFactory.setKeyStorePath(keyStorePath + "/keystore");
		sslContextFactory.setKeyStorePassword(keyStorePassword);
		sslContextFactory.setKeyManagerPassword(keyStorePassword);
		sslContextFactory.setTrustStorePath(keyStorePath + "/keystore");
		sslContextFactory.setTrustStorePassword(keyStorePassword);
		if ((keyAlias != null) && (!keyAlias.isBlank()))
			sslContextFactory.setCertAlias(keyAlias);
		sslContextFactory.setIncludeProtocols("TLSv1.3", "TLSv1.2");
		sslContextFactory.setIncludeCipherSuites("TLS_AES_256_GCM_SHA384", "TLS_CHACHA20_POLY1305_SHA256", "TLS_AES_128_GCM_SHA256",
				"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
				"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256");
		sslContextFactory.setSniRequired(false);
		sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);

		// SSL HTTP Configuration
		final HttpConfiguration https_config = new HttpConfiguration(http_config);
		final SecureRequestCustomizer secureRequestCustomizer = new SecureRequestCustomizer();
		secureRequestCustomizer.setSniHostCheck(false);
		https_config.addCustomizer(secureRequestCustomizer);

		// HTTP Connection Factory (v1.1 oder v2)
		final AbstractConnectionFactory connFactoryHTTPv1_1 = new HttpConnectionFactory(https_config);
		final AbstractConnectionFactory connFactoryHTTPv2 = new HTTP2ServerConnectionFactory(https_config);
		final ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
		final ServerConnector connector = preferHTTPv1_1
				? new ServerConnector(server, sslContextFactory, alpn, connFactoryHTTPv1_1, connFactoryHTTPv2)
				: new ServerConnector(server, sslContextFactory, alpn, connFactoryHTTPv2, connFactoryHTTPv1_1);
		connector.setName(name);
		connector.setPort(port);
		return connector;
	}


	/**
	 * Erzeugt eine neue HTTP- bzw. HTTPS-Konfiguration basierend auf der {@link SVWSKonfiguration}
	 * und fügt diese zum Jetty-Server hinzu.
	 */
	private void addHTTPServerConnections() {
		final SVWSKonfiguration config = SVWSKonfiguration.get();
		// Erstelle eine Verbindung für den Server
		final ServerConnector connector = config.isTLSDisabled()
				? getHttpServerConnector(config.getPortHTTP(), "Server")
				: getHttpsServerConnector(config.getPortHTTPS(), "Server",
						config.useHTTPDefaultv11(), config.getTLSKeystorePath(), config.getTLSKeystorePassword(), config.getTLSKeyAlias());
		server.addConnector(connector);
		// Ergänze ggf. eine weitere Verbindung für den Zugriff über einen priviligierten Port
		if (!config.isDBRootAccessDisabled() && config.hatPortHTTPPrivilegedAccess()) {
			final ServerConnector connectorPrivileged = config.isTLSDisabled()
					? getHttpServerConnector(config.getPortHTTPPrivilegedAccess(), "Privileged")
					: getHttpsServerConnector(config.getPortHTTPPrivilegedAccess(), "Privileged",
							config.useHTTPDefaultv11(), config.getTLSKeystorePath(), config.getTLSKeystorePassword(), config.getTLSKeyAlias());
			server.addConnector(connectorPrivileged);
		}
	}


	/**
	 * Fügt die Rest-Applikationen zum Server hinzu.
	 */
	private void addAPIApplications() {
		final SVWSKonfiguration config = SVWSKonfiguration.get();
		addApplication(RestAppServer.class, RestAppServer.getPathSpecification());
		addApplication(RestAppClient.class, "/*");
		addApplication(RestAppDav.class, "/dav/*");
		addApplication(RestAppDebug.class, RestAppDebug.getPathSpecification());
		if (!config.isDBRootAccessDisabled()) {
			addApplication(RestAppSchemaRoot.class, RestAppSchemaRoot.getPathSpecification());
			if ((config.getAdminClientPath() != null) && (!config.getAdminClientPath().isBlank()))
				addApplication(RestAppAdminClient.class, RestAppAdminClient.getPathSpecification());
		}
	}


	/**
	 * Fügt die angegebene API-Applikation zum Server hinzu.
	 *
	 * @param c           die Applikation
	 * @param pathSpecs   die Pfad-Spezifikationen
	 */
	private void addApplication(final Class<? extends Application> c, final String... pathSpecs) {
		final ServletHandler servletHandler = context_handler.getServletHandler();
		final ServletHolder servlet = servletHandler.addServletWithMapping(HttpServletDispatcher.class, pathSpecs[0]);
		final ServletMapping mapping = servletHandler.getServletMapping(pathSpecs[0]);
		mapping.setPathSpecs(pathSpecs);
		Logger.global().logLn("Registriere API-Applikation " + c.getSimpleName() + ": " + Arrays.toString(mapping.getPathSpecs()));
		servlet.setInitParameter("jakarta.ws.rs.Application", c.getCanonicalName());
	}


}

