package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogLehramtAnerkennungEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Lehramtsanerkennung von Lehrkräften 
 * an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerLehramtAnerkennung {

	/** Lehramtsanerkennung 'Zweite Staatsprüfung für ein Lehramt' */
	ST(new LehrerKatalogLehramtAnerkennungEintrag[]{
		new LehrerKatalogLehramtAnerkennungEintrag(1, "ST", "Zweite Staatsprüfung für ein Lehramt", null, null)
	}),

	/** Lehramtsanerkennung 'Anerkennung Lehramt' */
	AL(new LehrerKatalogLehramtAnerkennungEintrag[]{
		new LehrerKatalogLehramtAnerkennungEintrag(2, "AL", "Anerkennung Lehramt", null, null)
	}),

	/** Lehramtsanerkennung 'Anerkennung geeignete Prüfung' */
	AP(new LehrerKatalogLehramtAnerkennungEintrag[]{
		new LehrerKatalogLehramtAnerkennungEintrag(3, "AP", "Anerkennung geeignete Prüfung", null, null)
	}),

	/** Lehramtsanerkennung 'Förderliche Berufstätigkeit' */
	BT(new LehrerKatalogLehramtAnerkennungEintrag[]{
		new LehrerKatalogLehramtAnerkennungEintrag(4, "BT", "Förderliche Berufstätigkeit", null, null)
	}),

	/** Lehramtsanerkennung 'ohne' */
	OH(new LehrerKatalogLehramtAnerkennungEintrag[]{
		new LehrerKatalogLehramtAnerkennungEintrag(5, "OH", "ohne", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Lehramtsanerkennung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogLehramtAnerkennungEintrag daten;
	
	/** Die Historie mit den Einträgen der Lehramtsanerkennung */
	public final @NotNull LehrerKatalogLehramtAnerkennungEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Lehramtsanerkennungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerLehramtAnerkennung> _anerkennungenByID = new HashMap<>();

	/** Eine Hashmap mit allen Lehramtsanerkennungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerLehramtAnerkennung> _anerkennungenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Lehramtsanerkennung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Lehramtsanerkennung, welches ein Array von {@link LehrerKatalogLehramtAnerkennungEintrag} ist  
	 */
	private LehrerLehramtAnerkennung(final @NotNull LehrerKatalogLehramtAnerkennungEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerLehramtAnerkennung> getMapAnerkennungenByID() {
		if (_anerkennungenByID.size() == 0)
			for (final LehrerLehramtAnerkennung l : LehrerLehramtAnerkennung.values())
				_anerkennungenByID.put(l.daten.id, l);				
		return _anerkennungenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Lehramtssanerkennungen auf die zugehörigen Lehramtssanerkennungen
	 */
	private static @NotNull HashMap<@NotNull String, LehrerLehramtAnerkennung> getMapAnerkennungenByKuerzel() {
		if (_anerkennungenByKuerzel.size() == 0)
			for (final LehrerLehramtAnerkennung l : LehrerLehramtAnerkennung.values())
				_anerkennungenByKuerzel.put(l.daten.kuerzel, l);				
		return _anerkennungenByKuerzel;
	}
		
	
	/**
	 * Gibt die Lehramtsanerkennung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Lehramtsanerkennung
	 * 
	 * @return die Lehramtsanerkennung oder null, falls die ID ungültig ist.
	 */
	public static LehrerLehramtAnerkennung getByID(final long id) {
		return getMapAnerkennungenByID().get(id);
	}


	/**
	 * Gibt die Lehramtsanerkennung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Lehramtsanerkennung
	 * 
	 * @return die Lehramtsanerkennung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerLehramtAnerkennung getByKuerzel(final String kuerzel) {
		return getMapAnerkennungenByKuerzel().get(kuerzel);
	}

}
