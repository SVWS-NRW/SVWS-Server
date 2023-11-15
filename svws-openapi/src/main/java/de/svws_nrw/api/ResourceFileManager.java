package de.svws_nrw.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.svws_nrw.config.SVWSKonfiguration;
import de.svws_nrw.db.utils.OperationError;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Diese Klasse dient als Cache für Datei-Ressourcen, wie dem SVWS-Client, die über
 * die OpenAPI-Schnisstelle zur Verfügung gestellt werden.
 */
public final class ResourceFileManager {

	/** Eine HashMap mit den {@link ResourceFile}-Objekten zugeordnet zu den Dateinamen,
	 * unter dem auf die Ressource zugegriffen wird. */
	private final HashMap<String, ResourceFile> files = new HashMap<>();


	/** Verwaltet die Ressourcen des Web-Clients */
	private static ResourceFileManager _client = null;

	/** Verwaltet die Ressourcen des Admin-Web-Clients */
	private static ResourceFileManager _admin = null;



	/**
	 * Erstellt einen neuen Manager für alle Datei-Ressourcen in dem angebenen Pfad.
	 * Bei der Registierung werden nur die relativen Pfade darunter verwendet.
	 *
	 * @param path   der Pfad, aus welchem die Datei-Ressourcen hinzuzufügen sind.
	 */
	private ResourceFileManager(final String path) {
    	addDirectory(path, new File(path));
	}

	/**
	 * Gibt die Ressourcen des Web-Clients zurück.
	 *
	 * @return die Ressourcen des Web-Clients
	 */
	public static ResourceFileManager client() {
		if (_client == null) {
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			final String path = config.getClientPath();
			_client = new ResourceFileManager(path);
		}
		return _client;
	}


	/**
	 * Gibt die Ressourcen des Admin-Web-Clients zurück.
	 *
	 * @return die Ressourcen des Admin-Web-Clients
	 */
	public static ResourceFileManager admin() {
		if (_admin == null) {
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			final String path = config.getAdminClientPath();
			if ((path == null) || (path.isBlank()))
				return null;
			_admin = new ResourceFileManager(path);
		}
		return _admin;
	}


	/**
	 * Fügt alle Ressourcen in dem angegebenen Verzeichnis zu den Ressourcen hinzu. Dabei wird das
	 * angegbene Präfix bei dem Pfadnamen der Dateien nicht für die Registierung als Resource
	 * verwendet und aus dem Pfad für die Registrierung entfernt.
	 *
	 * @param prefix   das bei der Registrierung zu ignorierende Präfix des Dateipfades
	 * @param dir      das Verzeichnis, in dem die hinzuzufügenden Datei-Ressourcen gesucht werden
	 */
	private void addDirectory(final String prefix, final File dir) {
		if (!dir.isDirectory())
			return;
		final File[] dir_content = dir.listFiles();
		if (dir_content == null)
			return;
		for (final File f : dir_content) {
			if (f.isFile()) {
				final ResourceFile file = new ResourceFile(prefix, f);
				// TODO log info
				System.out.println("Adding File Resource: " + file.getPath());
				files.put(file.getPath(), file);
			} else if (f.isDirectory()) {
				addDirectory(prefix, f);
			}
		}
	}


    /**
     * Entfernt alle registrierten Datei-Ressourcen.
     */
	public void clearAll() {
    	files.clear();
	}


	/**
	 * Prüft zunächst, ob eine Datei-Ressource unter dem angegebenen Pfad registriert ist.
	 * Ist dies der Fall, so wird das {@link HttpServletResponse}-Objekt mit den Daten der
	 * Datei-Ressource beschrieben und es wird true zurückgegeben. Tritt beim ein Fehler
	 * auf oder ist keine Datei-Ressource unter dem Pfad registriert, so wird false
	 * zurückgegeben.
	 *
	 * @param path       der Pfad der Datei-Ressource
	 * @param response   das {@link HttpServletResponse}-Objekt für die Daten der Ressource
	 *
	 * @return true, falls die Response gültige Daten beinhaltet, ansonsten false
	 */
    public boolean handleResponse(final String path, final HttpServletResponse response) {
    	final ResourceFile res = files.get(path);
    	if (res == null)
    		return false;
    	try {
			res.write(response);
		} catch (final IOException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
		}
    	return true;
    }


    /**
     * Prüft zunächst, ob eine Datei-Ressource unter dem angegebenen Pfad registriert ist.
     * Ist die nicht der Fall, so wird null zurückgegeben. Ansonsten werden
     * die Daten der registrierten Datei-Ressource zurückgeben (siehe auch
     * {@link ResourceFile#getData()}).
     *
     * @param path   der Pfad der Datei-Ressource
     *
     * @return die Daten der Datei-Ressource oder null im Fehlerfall
     */
    public byte[] getData(final String path) {
    	final ResourceFile res = files.get(path);
    	if (res == null)
    		return new byte[0];
    	return res.getData();
    }

}
