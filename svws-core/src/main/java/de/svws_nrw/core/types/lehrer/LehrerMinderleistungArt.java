package de.svws_nrw.core.types.lehrer;

import java.util.HashMap;

import de.svws_nrw.core.data.lehrer.LehrerKatalogMinderleistungsartEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Arten von Minderleistungen durch Lehrkräfte 
 * an der Schule zur Verfügung.
 *  
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum LehrerMinderleistungArt {

	/** Minderleistungsart 'Pflichtstundenermäßigung aus Altersgründen' */
	ID_200(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(37, "200", "Pflichtstundenermäßigung aus Altersgründen", null, null)
	}),

	/** Minderleistungsart 'Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)' */
	ID_210(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(38, "210", "Pflichtstundenermäßigung wegen Schwerbehinderung (Regelanrechnung)", null, null)
	}),

	/** Minderleistungsart 'Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)' */
	ID_220(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(39, "220", "Pflichtstundenermäßigung wegen Schwerbehinderung (Erhöhung auf Antrag)", null, null)
	}),

	/** Minderleistungsart 'Beurlaubung, Rückkehr im Laufe des Schuljahres' */
	ID_230(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(40, "230", "Beurlaubung, Rückkehr im Laufe des Schuljahres", null, null)
	}),

	/** Minderleistungsart 'Langfristige Erkrankung' */
	ID_240(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(41, "240", "Langfristige Erkrankung", null, null)
	}),

	/** Minderleistungsart 'Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG' */
	ID_250(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(42, "250", "Abwesend wegen Beschäftigungsverbot gem. § 3 MuSchG", null, null)
	}),

	/** Minderleistungsart 'Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG' */
	ID_255(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(53, "255", "Abwesend wegen Teilbeschäftigungsverbot gem. § 3 MuSchG", null, null)
	}),

	/** Minderleistungsart 'Wiedereingliederungsmaßnahme' */
	ID_260(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(43, "260", "Wiedereingliederungsmaßnahme", null, null)
	}),

	/** Minderleistungsart 'Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit' */
	ID_270(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(44, "270", "Rückgabe vorgeleisteter Stunden wegen Nichtinanspruchnahme von Altersteilzeit", null, null)
	}),

	/** Minderleistungsart 'Rückgabe der Vorgriffsstunden' */
	ID_275(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(51, "275", "Rückgabe der Vorgriffsstunden", null, null)
	}),

	/** Minderleistungsart 'Seiteneinsteigerentlastung' */
	ID_280(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(45, "280", "Seiteneinsteigerentlastung", null, null)
	}),

	/** Minderleistungsart 'Ermäßigungs-/Freistellungsphase "Teilzeitbeschäftigung im Blockmodell" (§ 65 LBG) (vormals Sabbatjahr)' */
	ID_290(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(46, "290", "Ermäßigungs-/Freistellungsphase \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr)", null, null)
	}),

	/** Minderleistungsart 'Sonstige Ermäßigungen aus besonderen persönlichen Gründen' */
	ID_300(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(47, "300", "Sonstige Ermäßigungen aus besonderen persönlichen Gründen", null, null)
	}),

	/** Minderleistungsart 'Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr ' */
	ID_350(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(48, "350", "Abrundung der Pflichtstundenzahl wegen Aufrundung im vorhergehenden Schuljahr", null, null)
	}),

	/** Minderleistungsart 'Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)' */
	ID_360(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(49, "360", "Unterschreitung der Pflichtstundenzahl aus organisatorischen Gründen (z. B. Epochenunterricht)", null, null)
	}),

	/** Minderleistungsart 'Unterschreitung der Pflichtstundenzahl wegen COVID-19' */
	ID_365(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(54, "365", "Unterschreitung der Pflichtstundenzahl wegen COVID-19", null, null)
	}),

	/** Minderleistungsart 'Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite' */
	ID_370(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(50, "370", "Unterschreitung der Pflichtstundenzahl wegen Pflichstunden-Bandbreite", null, null)
	}),

	/** Minderleistungsart 'Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes' */
	ID_380(new LehrerKatalogMinderleistungsartEintrag[]{
		new LehrerKatalogMinderleistungsartEintrag(52, "380", "Fortbildung: Nachträglicher Erwerb des sonderpädagogischen Lehramtes", null, null)
	});



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Art von Minderleistung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull LehrerKatalogMinderleistungsartEintrag daten;
	
	/** Die Historie mit den Einträgen der Art von Minderleistung */
	public final @NotNull LehrerKatalogMinderleistungsartEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen Arten von Minderleistungen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, LehrerMinderleistungArt> _artenByID = new HashMap<>();

	/** Eine Hashmap mit allen Arten von Minderleistungen, welche dem Kürzel bzw. ASD-Schlüssel zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull String, LehrerMinderleistungArt> _artenByKuerzel = new HashMap<>();


	/**
	 * Erzeugt eine neue Art von Minderleistung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Art von Minderleistung, welches ein Array von {@link LehrerKatalogMinderleistungsartEintrag} ist  
	 */
	private LehrerMinderleistungArt(final @NotNull LehrerKatalogMinderleistungsartEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den IDs der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den IDs der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 */
	private static @NotNull HashMap<@NotNull Long, LehrerMinderleistungArt> getMapArtenByID() {
		if (_artenByID.size() == 0)
			for (final LehrerMinderleistungArt g : LehrerMinderleistungArt.values())
				_artenByID.put(g.daten.id, g);				
		return _artenByID;
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initielisiert.
	 *    
	 * @return die Map von den Kürzeln der Minderleistungsarten auf die zugehörigen Minderleistungsarten
	 */
	private static @NotNull HashMap<@NotNull String, LehrerMinderleistungArt> getMapArtenByKuerzel() {
		if (_artenByKuerzel.size() == 0)
			for (final LehrerMinderleistungArt g : LehrerMinderleistungArt.values())
				_artenByKuerzel.put(g.daten.kuerzel, g);				
		return _artenByKuerzel;
	}
	

	/**
	 * Gibt die Art der Minderleistung anhand der angegebenen ID zurück.
	 * 
	 * @param id   die ID der Art der Minderleistung
	 * 
	 * @return die Art der Minderleistung oder null, falls die ID ungültig ist
	 */
	public static LehrerMinderleistungArt getByID(final long id) {
		return getMapArtenByID().get(id);
	}


	/**
	 * Gibt die Art der Minderleistung anhand des angegebenen Kürzels zurück.
	 * 
	 * @param kuerzel   das Kürzel der Art der Minderleistung
	 * 
	 * @return die Art der Minderleistung oder null, falls das Kürzel ungültig ist
	 */
	public static LehrerMinderleistungArt getByKuerzel(final String kuerzel) {
		return getMapArtenByKuerzel().get(kuerzel);
	}

}
