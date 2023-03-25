package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogLehrbefaehigungAnerkennungEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Anerkennung der Lehrbefähigungen eines Lehrers dar.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerLehrbefaehigungAnerkennung {

	/** Anerkennung der Lehrbefähigung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'  */
	ID_1(new LehrerKatalogLehrbefaehigungAnerkennungEintrag[]{
		new LehrerKatalogLehrbefaehigungAnerkennungEintrag(1, "1", "erworben durch LABG/OVP bzw. Laufbahnverordnung", null, null)
	}),

	/** Anerkennung der Lehrbefähigung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'  */
	ID_2(new LehrerKatalogLehrbefaehigungAnerkennungEintrag[]{
		new LehrerKatalogLehrbefaehigungAnerkennungEintrag(2, "2", "Unterrichtserlaubnis (z. B. Zertifikatskurs)", null, null)
	}),

	/** Anerkennung der Lehrbefähigung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'  */
	ID_3(new LehrerKatalogLehrbefaehigungAnerkennungEintrag[]{
		new LehrerKatalogLehrbefaehigungAnerkennungEintrag(3, "3", "mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis", null, null)
	}),

	/** Anerkennung der Lehrbefähigung 'sonstige'  */
	ID_9(new LehrerKatalogLehrbefaehigungAnerkennungEintrag[]{
		new LehrerKatalogLehrbefaehigungAnerkennungEintrag(4, "9", "sonstige", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Lehrbefähigung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogLehrbefaehigungAnerkennungEintrag daten;
	
	/** Die Historie mit den Einträgen der Lehrbefähigung */
	public final @NotNull LehrerKatalogLehrbefaehigungAnerkennungEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Anerkennungsgründen für Lehrbefähigungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerLehrbefaehigungAnerkennung> _anerkennungenByID = new HashMap<>();

	/** Eine Hashmap mit allen Anerkennungsgründen für Lehrbefähigungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerLehrbefaehigungAnerkennung> _anerkennungenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt einen neuen Anerkennungsgrund für Lehrbefähigungen in der Aufzählung.
	 * 
	 * @param historie   die Historie der Lehrbefähigung, welches ein Array von {@link LehrerKatalogLehrbefaehigungAnerkennungEintrag} ist  
	 */
	private LehrerLehrbefaehigungAnerkennung(final @NotNull LehrerKatalogLehrbefaehigungAnerkennungEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerLehrbefaehigungAnerkennung> getMapAnerkennungenByID() {
		if (_anerkennungenByID.size() == 0)
			for (final LehrerLehrbefaehigungAnerkennung l : LehrerLehrbefaehigungAnerkennung.values())
				_anerkennungenByID.put(l.daten.id, l);				
		return _anerkennungenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Lehrbefaehigungssanerkennungen auf die zugehörigen Lehrbefaehigungssanerkennungen
	 */
	private static @NotNull HashMap<@NotNull String, LehrerLehrbefaehigungAnerkennung> getMapAnerkennungenByKuerzel() {
		if (_anerkennungenByKuerzel.size() == 0)
			for (final LehrerLehrbefaehigungAnerkennung l : LehrerLehrbefaehigungAnerkennung.values())
				_anerkennungenByKuerzel.put(l.daten.kuerzel, l);				
		return _anerkennungenByKuerzel;
	}
		
	
	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID des Anerkennungsgrunded für Lehrbefähigungen
	 * 
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls die ID ungültig ist
	 */
	public static LehrerLehrbefaehigungAnerkennung getByID(final long id) {
		return getMapAnerkennungenByID().get(id);
	}


	/**
	 * Gibt den Anerkennungsgrund für Lehrbefähigungen anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel des Anerkennungsgrunded für Lehrbefähigungen
	 * 
	 * @return der Anerkennungsgrund für Lehrbefähigungen oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerLehrbefaehigungAnerkennung getByKuerzel(final String kuerzel) {
		return getMapAnerkennungenByKuerzel().get(kuerzel);
	}

}
