package de.svws_nrw.data;

import java.io.InputStream;

import de.svws_nrw.db.DBEntityManager;
import jakarta.ws.rs.core.Response;

/**
 * Diese abstrakte Klasse ist die Grundlage für das einheitliche Aggregieren von 
 * Informationen für die OpenAPI und das einheitliche Bereitstellen von Funktionen, 
 * welche Daten für GET oder PATCH-Zugriff zur Verfügung stellen.   
 *
 * @param <ID> die Typ, welcher als ID für die Informationen verwendet wird.  
 */
public abstract class DataManager<ID> {
	
	/** Die Datenbank-Verbindung zum Aggregieren der Informationen aus der DB und zum Schreiben der Informationen bzw. Teilinformationen */
	protected final DBEntityManager conn;
	
	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 * 
	 * @param conn   die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll 
	 */
	protected DataManager(DBEntityManager conn) {
		this.conn = conn;
	}
	
	
	/**
	 * Ermittelt eine Liste mit allen Informationen in der DB. Wird üblicherweise 
	 * durch GET-Methoden für Listen verwendet. Meist ist die Methode getList zu bevorzugen.
	 * 
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getAll();

	
	
	/**
	 * Ermittelt eine Liste mit Informationen. Wird üblicherweise durch GET-Methoden 
	 * für Listen verwendet. Bei dieser Liste werden ggf. Filter verwendet (z.B. nur als sichtbar 
	 * markierte Einträge)
	 * 
	 * @return eine Liste mit den Informationen
	 */
	public abstract Response getList();
	

	/**
	 * Ermittelt die Informationen anhand der angegebenen ID. Wird
	 * üblicherweise durch GET-Methoden verwendet.
	 * 
	 * @param id   die ID der gesuchten Informationen
	 * 
	 * @return die Information mit der angebenen ID
	 */
	public abstract Response get(ID id);
	
	/**
	 * Ermittelt die Informationen ohne eine gültige ID (null). Wird
	 * üblicherweise durch GET-Methoden verwendet.
	 * 
	 * @return die Information mit der angebenen ID
	 */
	public Response get() {
		return this.get(null);
	}
	
	/**
	 * Passt die Informationen mithilfe des JSON-Patches aus dem übergebenen
	 * {@link InputStream} an. 
	 * 
	 * @param id   die ID der anzupassenden Informationen 
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 * 
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public abstract Response patch(ID id, InputStream is); 

	/**
	 * Passt die Informationen mithilfe des JSON-Patches aus dem übergebenen
	 * {@link InputStream} an. Eine ID wird in diesem Fall nicht verwendet und
	 * als null angenommen. 
	 * 
	 * @param is   der {@link InputStream} mit dem JSON-Patch
	 * 
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response patch(InputStream is) {
		return this.patch(null, is);
	}

}
