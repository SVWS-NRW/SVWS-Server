package de.nrw.schule.svws.core.types.lehrer;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.lehrer.LehrerKatalogMehrleistungsartEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Arten von Mehrleistungen durch Lehrkräfte 
 * an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerMehrleistungArt {

	/** Mehrleistungsart 'Beschäftigungsphase Sabbatjahr' */
	ID_100(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(1, "100", "Beschäftigungsphase Sabbatjahr", null, null)
	}),

	/** Mehrleistungsart 'Mehrarbeit (angeordnet und regelmäßig)' */
	ID_110(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(2, "110", "Mehrarbeit (angeordnet und regelmäßig)", null, null)
	}),

	/** Mehrleistungsart 'Aufrundung der Pflichtstundenzahl wegen Abrundung im folgenden Schuljahr ' */
	ID_150(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(3, "150", "Aufrundung der Pflichtstundenzahl wegen Abrundung im folgenden Schuljahr ", null, null)
	}),

	/** Mehrleistungsart 'Überschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)' */
	ID_160(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(4, "160", "Überschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)", null, null)
	}),

	/** Mehrleistungsart 'Überschreitung der Pflichtstundenzahl wegen COVID-19' */
	ID_165(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(6, "165", "Überschreitung der Pflichtstundenzahl wegen COVID-19", null, null)
	}),

	/** Mehrleistungsart 'Überschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite' */
	ID_170(new LehrerKatalogMehrleistungsartEintrag[]{
		new LehrerKatalogMehrleistungsartEintrag(5, "170", "Überschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Art von Mehrleistung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogMehrleistungsartEintrag daten;
	
	/** Die Historie mit den Einträgen der Art von Mehrleistung */
	public final @NotNull LehrerKatalogMehrleistungsartEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Arten von Mehrleistungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerMehrleistungArt> _artenByID = new HashMap<>();

	/** Eine Hashmap mit allen Arten von Mehrleistungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerMehrleistungArt> _artenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Art von Mehrleistung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Art von Mehrleistung, welches ein Array von {@link LehrerKatalogMehrleistungsartEintrag} ist  
	 */
	private LehrerMehrleistungArt(final @NotNull LehrerKatalogMehrleistungsartEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Mehrleistungsarten auf die zugehörigen Mehrleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Mehrleistungsarten auf die zugehörigen Mehrleistungsarten
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerMehrleistungArt> getMapArtenByID() {
		if (_artenByID.size() == 0)
			for (final LehrerMehrleistungArt g : LehrerMehrleistungArt.values())
				_artenByID.put(g.daten.id, g);				
		return _artenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Mehrleistungsarten auf die zugehörigen Mehrleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Mehrleistungsarten auf die zugehörigen Mehrleistungsarten
	 */
	private static @NotNull HashMap<@NotNull String, LehrerMehrleistungArt> getMapArtenByKuerzel() {
		if (_artenByKuerzel.size() == 0)
			for (final LehrerMehrleistungArt g : LehrerMehrleistungArt.values())
				_artenByKuerzel.put(g.daten.kuerzel, g);				
		return _artenByKuerzel;
	}
	

	/**
	 * Gibt die Art der Mehrleistung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Art der Mehrleistung
	 * 
	 * @return die Art der Mehrleistung oder null, falls die ID ungültig ist
	 */
	public static LehrerMehrleistungArt getByID(final long id) {
		return getMapArtenByID().get(id);
	}


	/**
	 * Gibt die Art der Mehrleistung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Art der Mehrleistung
	 * 
	 * @return die Art der Mehrleistung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerMehrleistungArt getByKuerzel(final String kuerzel) {
		return getMapArtenByKuerzel().get(kuerzel);
	}

}
