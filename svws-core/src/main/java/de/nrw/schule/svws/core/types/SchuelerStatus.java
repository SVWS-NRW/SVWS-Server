package de.nrw.schule.svws.core.types;

import java.util.HashMap;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types als Enumeration für 
 * die Statuswerte bei Schülern zur Verfügung.
 */
public enum SchuelerStatus {
	

	/** Status Neuaufnahme mit dem Wert 0 */
	NEUAUFNAHME(0, "Neuaufnahme", null, null),
	
	/** Status Warteliste mit dem Wert 1 */
	WARTELISTE(1, "Warteliste", null, null),
	
	/** Status Aktic mit dem Wert 2 */
	AKTIV(2, "Aktiv", null, null),
	
	/** Status Beurlaubt mit dem Wert 3 */
	BEURLAUBT(3, "Beurlaubt", null, null),
	
	/** Status Extern mit dem Wert 6 */
	EXTERN(6, "Extern", null, null),
	
	/** Status Abschluss mit dem Wert 8 */
	ABSCHLUSS(8, "Abschluss", null, null),
	
	/** Status Abgänger mit dem Wert 9 */
	ABGANG(9, "Abgang", null, null),

	/** Status Abgänger mit dem Wert 10 */
	EHEMALIGE(10, "Ehemalige", null, null);

	
	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	


	/** Die Zuordnung des Schüler-Status zu der ID */
	private static final @NotNull HashMap<@NotNull Integer, @NotNull SchuelerStatus> _mapID = new HashMap<>();

	/** Die Zuordnung des Schüler-Status zu der ID */	
	private static final @NotNull HashMap<@NotNull String, @NotNull SchuelerStatus> _mapBezeichnungen = new HashMap<>();


	/** Die ID des Schüler Status, welche auch in der SVWS-Datenbank genutzt wird. */
	public final int id;
	
	/** Die textuelle Bezeichnung des Schülerstatus. */
	public final @NotNull String bezeichnung;
	
	/** Gibt an, in welchem Schuljahr der Schülerstatus einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr der Schülerstatus gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	public final Integer gueltigBis;
	
	
	/**
	 * Erzeugt einen neuen Schüler-Status in der Aufzählung.
	 * 
	 * @param id            die ID des Schüler Status, welche auch in der SVWS-Datenbank genutzt wird
	 * @param bezeichnung   die textuelle Bezeichnung des Schülerstatus
	 * @param gueltigVon    gibt an, in welchem Schuljahr der Schülerstatus einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 * @param gueltigBis    gibt an, bis zu welchem Schuljahr der Schülerstatus gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt.
	 */
	private SchuelerStatus(final int id, final @NotNull String bezeichnung, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.bezeichnung = bezeichnung;
        this.gueltigVon = gueltigVon;
        this.gueltigBis = gueltigBis;
	}
	
	
	
	/**
	 * Gibt eine Map von den IDs der Schüler-Status auf die zugehörigen Schüler-Status
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Schüler-Status auf die zugehörigen Schüler-Status
	 */
	private static @NotNull HashMap<@NotNull Integer, @NotNull SchuelerStatus> getMapID() {
		if (_mapID.size() == 0)
			for (final SchuelerStatus p : SchuelerStatus.values())
				_mapID.put(p.id, p);				
		return _mapID;
	}

	
	/**
	 * Gibt eine Map von den Bezeichnungen der Schüler-Status auf die zugehörigen Schüler-Status
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Bezeichnungen der Schüler-Status auf die zugehörigen Schüler-Status
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull SchuelerStatus> getMapBezeichnungen() {
		if (_mapBezeichnungen.size() == 0)
			for (final SchuelerStatus p : SchuelerStatus.values())
				_mapBezeichnungen.put(p.bezeichnung.toUpperCase(), p);				
		return _mapBezeichnungen;
	}
	    

	/** 
	 * Gibt den Schülerstatus anhand der ID zurück.
	 * 
	 * @param status	die id des Schülerstatus
	 * 
	 * @return der Schülerstatus oder null, falls die ID ungültig ist
	 * */
	public static SchuelerStatus fromID(final int status) {
		return getMapID().get(status);
	}

	
	/** 
	 * Ermittelt den Schülerstatus anhand der Bezeichnung.
	 * 
	 * @param value	  die Bezeichnung des Schülerstatus
	 * 
	 * @return der Schülerstatus oder null, falls die Bezeichnung ungültig ist
	 * */
    public static SchuelerStatus fromBezeichnung(final String value) {
    	if (value == null)
    		return null;
    	return getMapBezeichnungen().get(value.toUpperCase());
    }


	/**
	 * Prüft, ob die übergebene ID für einen gültigen Schülerstatus
	 * steht oder nicht
	 * 
	 * @param id   die zu prüfende ID
	 * 
	 * @return true, falls die ID gültig ist.
	 */
	public static boolean isValidID(final Integer id) {
		if (id == null)
			return false;
		for (final SchuelerStatus status : SchuelerStatus.values())
			if (status.id == id)
				return true;
		return false;
	}

}
