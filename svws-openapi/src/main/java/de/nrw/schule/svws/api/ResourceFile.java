package de.nrw.schule.svws.api;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import de.nrw.schule.svws.utils.FileUtils;

/**
 * Diese Klasse dient als Cache für Datei-Ressourcen, wie dem SVWS-Client, die über
 * die OpenAPI-Schnisstelle zur Verfügung gestellt werden.
 */
public class ResourceFile {

	/** Eine HashMap mit den {@link ResourceFile}-Objekt zugeordnet zu dem Dateiname, unter dem auf die Ressource zugegriffen wird, */
	private static final HashMap<String, ResourceFile> files = new HashMap<>();

	/** Der Datei-Pfad dieser Ressource */
	private final String path;
	
	/** Das Datei-Objekt für diese Ressource */
	private final File file;
	
	/** Die gecachten Daten dieser Ressource */
	private byte[] cache;	
	
	/** Der Zeitstempel, wann die gecachte Ressource zuletzt verwendet wurde. Dieser dient der Erkennung, ob sich eine Datei verändert hat und deshalb neu geladen werden muss. */
	private long cacheTimestamp;
	
	
	/**
	 * Erstellt eine neue Datei-Ressource.
	 * 
	 * @param prefix   der Präfix beim Datei-Pfad, der nicht in den Pfad dieser Ressource 
	 *                 übernommen werden soll, da der Pfad der Ressource ein relativer Pfad ist.
	 * @param file   das {@link File}-Objekt für den Zugriff auf die Datei-Ressource
	 */
	private ResourceFile(String prefix, File file) {
		this.file = file;
		String p = file.getPath().replace('\\','/');		
		this.path = p.substring(prefix.length(), p.length()).replaceFirst("^/", ""); 
		this.cache = null;
		this.cacheTimestamp = Long.MIN_VALUE;
// TODO log info
System.out.println("Adding File Resource: " + this.path);		
		files.put(this.path, this);
	}


	/**
	 * Gibt die Daten dieser Ressource zurück. Dabei wird geprüft, ob der
	 * Cache noch gültige Daten besitzt. Ist dies der Fall, so wird der Cache-Inhalt
	 * zurückgegeben. Ist dies nicht der Fall, so wird der Cache durch einen Zugriff
	 * auf die Datei-Ressource aktualisiert. 
	 *  
	 * @return die Daten dieser Datei-Ressource
	 */
	public byte[] getData() {
		try {
			if ((cache == null) || (cacheTimestamp != file.lastModified())) {
				this.cache = FileUtils.file2ByteArray(file);
				this.cacheTimestamp = file.lastModified();
			}
		} catch (SecurityException e) {
			// TODO log error
			e.printStackTrace();
			return null;
		}
		return this.cache;
	}
	
	
	/**
	 * Schreibt die Daten der Datei-Ressource direkt in eine {@link HttpServletResponse}.
	 * 
	 * @param response   die HTTP-Response, in welche die Daten der Date-Ressource 
	 *                   geschrieben werden sollen 
	 * 
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei-Ressource oder 
	 *                       beim Schreiben in die {@link HttpServletResponse} ein Fehler auftritt
	 */
	public void write(HttpServletResponse response) throws IOException {
		try (ServletOutputStream ostream = response.getOutputStream()) {
			FileUtils.copy(file, ostream);
			ostream.close();
		}		
	}
	
	
	/**
	 * Gibt den Pfad der Datei-Ressource zurück.
	 * 
	 * @return der Pfad dieser Datei-Ressource
	 */
	public String getPath() {
		return path;
	}


	/**
	 * Gibt den Dateinamen dieser Datei-Ressource zurück. 
	 *   
	 * @return der Dateiname dieser Datei-Ressource
	 */
	public String getFilename() {
		int pos = path.lastIndexOf('/') + 1;
		return (pos == 0) ? path : path.substring(pos);
	}
	

	/**
	 * Fügt alle Ressourcen in dem angegebenen Verzeichnis zu den Ressourcen hinzu. Dabei wird das 
	 * angegbene Präfix bei dem Pfadnamen der Dateien nicht für die Registierung als Resource 
	 * verwendet und aus dem Pfad für die Registrierung entfernt.     
	 * 
	 * @param prefix   das bei der Registrierung zu ignorierende Präfix des Dateipfades
	 * @param dir      das Verzeichnis, in dem die hinzuzufügenden Datei-Ressourcen gesucht werden
	 */
	@SuppressWarnings("unused")
	private static void addDirectory(String prefix, File dir) {
		if (!dir.isDirectory())
			return;
		File[] dir_content = dir.listFiles();
		if (dir_content == null)
			return;
		for (File f : dir_content) {
			if (f.isFile()) {
				new ResourceFile(prefix, f);
			} else if (f.isDirectory()) {
				addDirectory(prefix, f);
			}
		}		
	}


	/**
	 * Registiert alle Datei-Ressourcen angebenen Pfad. Bei der Registierung werden nur die relativen Pfade 
	 * darunter verwendet.
	 * 
	 * @param path   der Pfad, in dem die Datei-Ressourcen hinzuzufügen sind.
	 */
    public static void add(String path) {
    	addDirectory(path, new File(path));
    }
    
    
    /**
     * Entfernt alle registrierten Datei-Ressourcen aus der Registrierung.
     */
	public static void clearAll() {
    	files.clear();  		
	}
	

	/**
	 * Prüft zunächst, ob eine Datei-Ressource unter dem angegebenen Pfad registriert ist. 
	 * Ist dies der Fall, so wird das {@link HttpServletResponse}-Objekt mit den Daten der 
	 * Datei-Ressource beschrieben und es wird true zurückgegeben. Tritt beim ein Fehler 
	 * auf oder ist keine Datei-Ressource unter dem Pfad registriert, so wird fals 
	 * zurückgegeben.
	 * 
	 * @param path       der Pfad der Datei-Ressource
	 * @param response   das {@link HttpServletResponse}-Objekt für die Daten der Ressource
	 * 
	 * @return true, falls die Response gültige Daten beinhaltet, ansonsten false
	 */
    public static boolean handleResponse(String path, HttpServletResponse response) {
    	ResourceFile res = files.get(path);
    	if (res == null)
    		return false;
    	try {
			res.write(response);
		} catch (IOException e) {
			// TODO log error
			e.printStackTrace();
			return false;
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
    public static byte[] getData(String path) {
    	ResourceFile res = files.get(path);
    	if (res == null)
    		return null;
    	return res.getData();
    }

}
