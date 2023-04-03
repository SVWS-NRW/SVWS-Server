package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogFachrichtungAnerkennungEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Anerkennung der Fachrichtung eines Lehrers dar.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerFachrichtungAnerkennung {

	/** Fachrichtungsanerkennung 'erworben durch LABG/OVP bzw. Laufbahnverordnung'  */
	ID4(new LehrerKatalogFachrichtungAnerkennungEintrag[]{
		new LehrerKatalogFachrichtungAnerkennungEintrag(4, "1", "erworben durch LABG/OVP bzw. Laufbahnverordnung", null, null)
	}),

	/** Fachrichtungsanerkennung 'Unterrichtserlaubnis (z. B. Zertifikatskurs)'  */
	ID5(new LehrerKatalogFachrichtungAnerkennungEintrag[]{
		new LehrerKatalogFachrichtungAnerkennungEintrag(5, "2", "Unterrichtserlaubnis (z. B. Zertifikatskurs)", null, null)
	}),

	/** Fachrichtungsanerkennung 'mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis'  */
	ID6(new LehrerKatalogFachrichtungAnerkennungEintrag[]{
		new LehrerKatalogFachrichtungAnerkennungEintrag(6, "3", "mehrjähriger Unterricht ohne Lehramtsprüfung oder Unterrichtserlaubnis", null, null)
	}),

	/** Fachrichtungsanerkennung 'sonstige'  */
	ID7(new LehrerKatalogFachrichtungAnerkennungEintrag[]{
		new LehrerKatalogFachrichtungAnerkennungEintrag(7, "9", "sonstige", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten die Anerkennung der Fachrichtung , wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogFachrichtungAnerkennungEintrag daten;

	/** Die Historie mit den Einträgen für die Anerkennung der Fachrichtung  */
	public final @NotNull LehrerKatalogFachrichtungAnerkennungEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen Fachrichtungsanerkennungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerFachrichtungAnerkennung> _anerkennungenByID = new HashMap<>();

	/** Eine Hashmap mit allen Fachrichtungsanerkennungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerFachrichtungAnerkennung> _anerkennungenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt neue Anerkennung für Fachrichtungen in der Aufzählung.
	 *
	 * @param historie   die Historie der Anerkennung für Fachrichtungen, welches ein Array von {@link LehrerKatalogFachrichtungAnerkennungEintrag} ist
	 */
	LehrerFachrichtungAnerkennung(final @NotNull LehrerKatalogFachrichtungAnerkennungEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den IDs der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerFachrichtungAnerkennung> getMapAnerkennungenByID() {
		if (_anerkennungenByID.size() == 0)
			for (final LehrerFachrichtungAnerkennung l : LehrerFachrichtungAnerkennung.values())
				_anerkennungenByID.put(l.daten.id, l);
		return _anerkennungenByID;
	}


	/**
	 * Gibt eine Map von den Kürzeln der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *
	 * @return die Map von den Kürzeln der Fachrichtungsanerkennungen auf die zugehörigen Fachrichtungsanerkennungen
	 */
	private static @NotNull HashMap<@NotNull String, LehrerFachrichtungAnerkennung> getMapAnerkennungenByKuerzel() {
		if (_anerkennungenByKuerzel.size() == 0)
			for (final LehrerFachrichtungAnerkennung l : LehrerFachrichtungAnerkennung.values())
				_anerkennungenByKuerzel.put(l.daten.kuerzel, l);
		return _anerkennungenByKuerzel;
	}



	/**
	 * Gibt die Fachrichtungsanerkennung anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID der Fachrichtungsanerkennung
	 *
	 * @return die Fachrichtungsanerkennung oder null, falls die ID ungültig ist
	 */
	public static LehrerFachrichtungAnerkennung getByID(final long id) {
		return getMapAnerkennungenByID().get(id);
	}


	/**
	 * Gibt die Fachrichtungsanerkennung anhand des angegebenen Kürzels zurück.
	 *
	 * @param kuerzel   das Kürzel der Fachrichtungsanerkennung
	 *
	 * @return die Fachrichtungsanerkennung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerFachrichtungAnerkennung getByKuerzel(final String kuerzel) {
		return getMapAnerkennungenByKuerzel().get(kuerzel);
	}

}
