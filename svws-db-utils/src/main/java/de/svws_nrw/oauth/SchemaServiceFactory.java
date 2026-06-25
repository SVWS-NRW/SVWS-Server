package de.svws_nrw.oauth;

import de.svws_nrw.repo.DbConnectionProvider;

/**
 * Factory fuer {@link SchemaService}-Instanzen.
 *
 * <p>Kapselt die Beschaffung der aktiven Datenbankverbindung ueber den
 * {@link DbConnectionProvider} und entkoppelt so Aufrufer von der konkreten
 * Verbindungsinfrastruktur.
 */
public final class SchemaServiceFactory {

	private SchemaServiceFactory() {
		// utility class -- nicht instanziierbar
	}

	/**
	 * Erzeugt neue Instanz der SchemaServicerFactory
	 * @return {@link SchemaServiceFactory}
	 */
	public static SchemaServiceFactory getNewInstance() {
		return new SchemaServiceFactory();
	}

	/**
	 * Erzeugt einen {@link SchemaService} mit der aktuell aktiven Datenbankverbindung.
	 *
	 * @return neuer {@link SchemaService} fuer das aktive Schema
	 */
	public SchemaService getService() {
		return new SchemaService(DbConnectionProvider.getConnection());
	}


}
