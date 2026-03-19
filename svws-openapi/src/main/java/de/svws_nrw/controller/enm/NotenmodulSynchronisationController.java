package de.svws_nrw.controller.enm;

import java.io.InputStream;

import jakarta.ws.rs.core.Response;

/**
 * Das Interface für die API-Zugriffe für die Synchronisation mit einem externen Notenmodul-Server
 */
public interface NotenmodulSynchronisationController {

	/**
	 * Synchronisiert die Daten des Externen Notenmoduls (ENM) mit dem ENM-Server und lädt
	 * dabei diese als ZIP beim ENM hoch und anschließend wieder von diesem herunter und speichert
	 * diese in der Datenbank.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response synchronize(long idVerbindung);


	/**
	 * Lädt die ENM-Daten aus der Datenbank zu dem ENM-Server hoch.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response upload(long idVerbindung);


	/**
	 * Importiert die ENM-Daten von dem ENM-Server und schreibt diese in die Datenbank.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response download(long idVerbindung);


	/**
	 * Entfernt die ENM-Daten von dem ENM-Server. Dabei werden auch die Benutzerdaten auf dem Server entfernt.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response truncate(long idVerbindung);

	/**
	 * Entfernt die ENM-Daten von dem ENM-Server.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response reset(long idVerbindung);

	/**
	 * Prüft, ob der ENM-Server mit den hinterlegten Verbindungsdaten erreichbar ist.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response check(long idVerbindung);

	/**
	 * Holt die auf dem ENM-Server hintelegten Konfigurationselemente
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response getENMServerConfig(long idVerbindung);


	/**
	 * Schreibt ein Konfigurationselement in die Serverkonfiguration oder in die Globale
	 * Client-Konfiguration des Servers.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 * @param is             der Input-Stream mit den Konfigurationsdaten
	 *
	 * @return die HTTP-Response
	 */
	Response setENMServerConfigElement(long idVerbindung, InputStream is);


	/**
	 * Prüft, ob der ENM-Server bereits initialisiert ist und gleichzeitig, ob das TLS bekannt ist.
	 *
	 * @param idVerbindung   die ID für die Verbindungsinformationen zum externen Notenmodul-Server
	 *
	 * @return die HTTP-Response
	 */
	Response setup(long idVerbindung);

}
