package de.svws_nrw.core.types.schule;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.schule.SchulformKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Aufzählung stellt einen Core-Type für die Schulformen zur Verfügung.
 *
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public enum Schulform {

	/** Schulform Berufskolleg */
	BK(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(1000, "BK", "30", "Berufskolleg", false, true, false, false, null, null)
	}),

	/** Schulform Freie Waldorfschule */
	FW(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(2000, "FW", "17", "Freie Waldorfschule", true, true, false, true, null, null)
	}),

	/** Schulform Grundschule */
	G(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(3000, "G", "02", "Grundschule", true, false, false, false, null, null)
	}),

	/** Schulform Gesamtschule */
	GE(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(4000, "GE", "15", "Gesamtschule", true, false, false, true, null, null)
	}),

	/** Schulform Gemeinschaftsschule */
	GM(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(5000, "GM", "16", "Gemeinschaftsschule", true, false, false, false, null, 2022)
	}),

	/** Schulform Gymnasium */
	GY(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(6000, "GY", "20", "Gymnasium", true, false, false, true, null, null)
	}),

	/** Schulform Hauptschule */
	H(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(7000, "H", "04", "Hauptschule", true, false, false, false, null, null)
	}),

	/** Hibernia */
	HI(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(8000, "HI", "18", "Hibernia", true, true, false, false, null, null)
	}),

	/** Schulform Schulversuch PRIMUS */
	PS(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(9000, "PS", "13", "Schulversuch PRIMUS", true, false, false, false, null, null)
	}),

	/** Schulform Realschule */
	R(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(10000, "R", "10", "Realschule", true, false, false, false, null, null)
	}),

	/** Schulform Förderschule im Bereich G/H */
	S(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(11000, "S", "08", "Förderschule im Bereich G/H", true, false, false, false, null, null)
	}),

	/** Schulform Klinikschule */
	KS(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(12000, "KS", "83", "Klinikschule", true, true, false, false, null, null)
	}),

	/** Schulform Förderschule im Bereich Berufskolleg */
	SB(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(13000, "SB", "88", "Förderschule im Bereich Berufskolleg", true, false, false, false, null, null)
	}),

	/** Schulform Förderschule im Bereich Gymnasium */
	SG(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(14000, "SG", "87", "Förderschule im Bereich Gymnasium", true, false, false, true, null, null)
	}),

	/** Schulform Sekundarschule */
	SK(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(15000, "SK", "14", "Sekundarschule", true, false, false, false, null, null)
	}),

	/** Schulform Förderschule im Bereich Realschule */
	SR(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(16000, "SR", "85", "Förderschule im Bereich Realschule", true, false, false, false, null, null)
	}),

	/** Schulform nicht umorganisierte Volksschule */
	V(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(17000, "V", "06", "nicht umorganisierte Volksschule", true, false, false, false, null, null)
	}),

	/** Schulform Weiterbildungskolleg */
	WB(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(18000, "WB", "25", "Weiterbildungskolleg", false, false, true, false, null, null)
	}),

	/** Schulform Freie Waldorfschule (Förderschule) */
	WF(new SchulformKatalogEintrag[]{
		new SchulformKatalogEintrag(19000, "WF", "19", "Freie Waldorfschule (Förderschule)", true, true, false, true, null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Schulform, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull SchulformKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Schulformen */
	public final @NotNull SchulformKatalogEintrag@NotNull[] historie;

	/** Ein ArrayList mit allen definierten Schulformen */
	private static final @NotNull HashMap<@NotNull String, Schulform> _schulformen = new HashMap<>();

	/** Ein ArrayList mit allen definierten Schulformen, die eine Statistiknummer zugewiesen haben. */
	private static final @NotNull HashMap<@NotNull String, Schulform> _schulformenNummer = new HashMap<>();

	/** Eine Map mit allen Historien-Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<@NotNull Long, SchulformKatalogEintrag> _mapEintragById = new HashMap<>();


	/**
	 * Erzeugt eine neue Schulform in der Aufzählung.
	 *
	 * @param historie   die Historie der Schulformen, welches ein Array von {@link SchulformKatalogEintrag} ist
	 */
	Schulform(final @NotNull SchulformKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 */
	private static @NotNull HashMap<@NotNull String, Schulform> getMapSchulformenByKuerzel() {
		if (_schulformen.size() == 0) {
			for (final Schulform s : Schulform.values()) {
				if (s.daten != null)
					_schulformen.put(s.daten.kuerzel, s);
			}
		}
		return _schulformen;
	}


	/**
	 * Gibt eine Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulformen auf die zugehörigen Schulformen
	 */
	private static @NotNull HashMap<@NotNull String, Schulform> getMapSchulformenByNummer() {
		if (_schulformenNummer.size() == 0)
			for (final Schulform s : Schulform.values())
				if ((s.daten != null) && (s.daten.nummer != null))
					_schulformenNummer.put(s.daten.nummer, s);
		return _schulformenNummer;
	}


	/**
	 * Gibt eine Map von den IDs der Schulform-Katalog-Einträge auf die zugehörigen
	 * Schulform-Katalog-Einträge zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Schulform-Katalog-Einträge auf die zugehörigen Schulform-Katalog-Einträge
	 */
	private static @NotNull HashMap<@NotNull Long, SchulformKatalogEintrag> getMapEintragById() {
		if (_mapEintragById.size() == 0) {
			for (final Schulform s : Schulform.values()) {
				for (final SchulformKatalogEintrag e : s.historie)
					_mapEintragById.put(e.id, e);
			}
		}
		return _mapEintragById;
	}


	/**
	 * Gibt die Schulform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Schulform
	 *
	 * @return die Schulform oder null, falls das Kürzel ungültig ist
	 */
	public static Schulform getByKuerzel(final String kuerzel) {
		return getMapSchulformenByKuerzel().get(kuerzel);
	}


	/**
	 * Gibt die Schulform für die angegebene Nummer zurück.
	 *
	 * @param nummer   die Nummer der Schulform
	 *
	 * @return die Schulform oder null, falls keine Schulform mit dieser Nummer vorhanden ist
	 */
	public static Schulform getByNummer(final String nummer) {
		return getMapSchulformenByNummer().get(nummer);
	}


	/**
	 * Gibt den Schulform-Katalog-Eintrag anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Schulform-Katalog-Eintrag oder null, falls kein Eintrag mit dieser ID vorhanden ist
	 */
	public static SchulformKatalogEintrag getEintragByID(final long id) {
		return getMapEintragById().get(id);
	}


	/**
	 * Gibt alle "echten" Schulformen dieser Aufzählung zurück.
	 * Das bedeutet, dass Pseudoschulformen, die in NRW nicht
	 * existieren, nicht zurückgegeben werden.
	 *
	 * @return eine {@link List} mit alle "echten" Schulformen
	 */
	public static @NotNull List<@NotNull Schulform> get() {
		final @NotNull ArrayList<@NotNull Schulform> result = new ArrayList<>();
		for (final @NotNull Schulform sf : Schulform.values())
			if ((sf.daten != null) && (sf.daten.nummer != null))
				result.add(sf);
		return result;
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück.
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static @NotNull List<@NotNull Schulform> getMitGymOb() {
		final @NotNull ArrayList<@NotNull Schulform> result = new ArrayList<>();
		for (final @NotNull Schulform sf : Schulform.values())
			if (sf.daten.hatGymOb)
				result.add(sf);
		return result;
	}

}
