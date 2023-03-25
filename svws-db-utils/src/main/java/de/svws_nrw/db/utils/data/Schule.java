package de.svws_nrw.db.utils.data;

import java.util.HashMap;

import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;


/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen 
 * zur Schule unter Verwendung der entsprechenden SVWS-DB-DTOs zur Verfügung.
 */
public class Schule {
	
	/** Diese HashMap dient als Cache für den Zugriff auf das als Schlüssel verwendete Datenbank-Schema */
	private static HashMap<String, Schule> cache = new HashMap<>();
	

	/**
	 * Die Methode liefert die Schulinformationen aus der SVWS-Datenbank.
	 * Dabei wird ggf. auf einen Cache zurückgegriffen, um die Anzahl der Datenbank-
	 * Zugriffe zu reduzieren.
	 * 
	 * @param conn   die Datenbankverbindung
	 * 
	 * @return eine Instanz dieser Klasse mit den Schulinformationen oder null, 
	 *         falls diese nicht aus der DB gelesen werden konnte
	 */
	public synchronized static Schule queryCached(DBEntityManager conn) {
		Schule schule = cache.get(conn.getDBSchema());
		return (schule == null) ? query(conn) : schule;
	}
	
	
	/**
	 * Die Methode liefert die Schulinformationen aus der SVWS-Datenbank.
	 * 
	 * @param conn   die Datenbankverbindung
	 * 
	 * @return eine Instanz dieser Klasse mit den Schulinformationen oder null, 
	 *         falls diese nicht aus der DB gelesen werden konnte
	 */
	public synchronized static Schule query(DBEntityManager conn) {
		DTOEigeneSchule dto = conn.querySingle(DTOEigeneSchule.class);
		if (dto == null)
			return null;
		DTOSchuljahresabschnitte dtoAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dto.Schuljahresabschnitts_ID);
		if (dtoAbschnitt == null)
			return null;		
		Schule schule = new Schule(dto, dtoAbschnitt);
		cache.put(conn.getDBSchema(), schule);
		return schule;
	}
	
	
	/** Das DTO-Objekt für den Datenbank-Zugriff auf die Schule */
	public final DTOEigeneSchule dto;
	
	/** Das DTO-Objekt für den Datenbank-Zugriff auf den aktuellen Schuljahresabschnitt der Schule */
	public final DTOSchuljahresabschnitte dtoAbschnitt;
	
	/**
	 * Erzeugt ein neues Schule-Hilfsobjekt unter Verwendung des angegebenen DTO-Objektes. 
	 * 
	 * @param dto           das DTO-Objekt für die EigeneSchule aus der Datenbank
	 * @param dtoAbschnitt  das DTO-Objekt für den Datenbank-Zugriff auf den aktuellen Schuljahresabschnitt der Schule
	 */
	public Schule(DTOEigeneSchule dto, DTOSchuljahresabschnitte dtoAbschnitt) {
		this.dto = dto;
		this.dtoAbschnitt = dtoAbschnitt;
	}
	

	/**
	 * Diese Methode prüft, ob die Schule im Quartalsmodus betrieben wird oder nicht
	 * 
	 * @return true, falls die Schule im Quartalsmodus betrieben wird und false sonst
	 */
	public boolean istImQuartalsmodus() {
		return (dto.AnzahlAbschnitte == 4);
	}


	/**
	 * Diese Methode gibt das aktuelle Halbjahr der Schule zurück.
	 * 
	 * @return das Halbjahr, in dem sich die Schule aktuell befindet
	 */
	public int getHalbjahr() {
		return (dto.AnzahlAbschnitte == 4) ? dtoAbschnitt.Abschnitt / 2 : dtoAbschnitt.Abschnitt;
	}
	
	
	/**
	 * Diese Methode gibt die Schulform der Schule zurück.
	 * 
	 * @return die Schulform dieser Schule
	 */
	public Schulform getSchulform() {
		return dto.Schulform;
	}
	
	
}
