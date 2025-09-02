package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.SchulabschlussAllgemeinbildendKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der allgemeinbildenden Schulabschlüsse.
 */
public enum SchulabschlussAllgemeinbildend implements CoreType<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> {

	/** Es liegt kein Abschluss vor */
	OA,

	/** Hauptschulabschluss nach Klasse 9 (ohne Berechtigung zum Besuch der Klasse 10 Typ B) */
	HA9A,

	/** Hauptschulabschluss nach Klasse 9 (mit Berechtigung zum Besuch der Klasse 10 Typ B) */
	HA9,

	/** Hauptschulabschluss nach Klasse 9 (ggf. mit Berechtigung zum Besuch eines weiterführenden Bildungsgangs am Berufskolleg bei internationalen Förderklassen) - siehe BK-Bildungsgang A12 */
	HA9_FOE,

	/** Hauptschulabschluss nach Klasse 9 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe */
	HA9_Q,

	/** Hauptschulabschluss nach Klasse 10 */
	HA10,

	/** Hauptschulabschluss nach Klasse 10 mit der Berechtigung zum Besuch der Gymnasialen Oberstufe */
	HA10_Q,

	/** Der Mittlere Schulabschluss bzw. Fachoberschulreife */
	MSA,

	/** Der Mittlere Schulabschluss mit der Berechtigung zum Besuch Gymnasialen Oberstufe */
	MSA_Q,

	/** Der Mittlere Schulabschluss mit der Berechtigung zum Besuch der Qualifikationsphase der Gymnasialen Oberstufe */
	MSA_Q1,

	/** Versetzung in die Klasse 11 der Fachoberschule (BK) */
	VS_11,

	/** Fachhochschulreife (nur schulischer Teil) */
	FHR_S,

	/** Fachhochschulreife */
	FHR,

	/** fachgebundene Hochschulreife (BK) */
	FGHR,

	/** Abitur / Allgemeine Hochschulreife */
	ABITUR,

	/** Förderschule (Förderschwerpunkt geistige Entwicklung) */
	FOEG,

	/** Förderschule (Förderschwerpunkt Lernen) */
	FOEL,

	/** Waldorfschule */
	WALD,

	/** Ohne Abschluss, kommt aus der Deutschförderung */
	DFR;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> manager) {
		CoreTypeDataManager.putManager(SchulabschlussAllgemeinbildend.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SchulabschlussAllgemeinbildendKatalogEintrag, SchulabschlussAllgemeinbildend> data() {
		return CoreTypeDataManager.getManager(SchulabschlussAllgemeinbildend.class);
	}


	/**
	 * Prüft, ob dieser Abschluss dem Abschluss mit dem übergebene Kürzel entspricht.
	 *
	 * @param kuerzel   das Kürzel des anderen Abschlusses
	 *
	 * @return true, falls die Abschlüsse übereinstimmen und ansonsten false
	 */
	public boolean is(final String kuerzel) {
		if (kuerzel == null)
			return false;
		final SchulabschlussAllgemeinbildend other = SchulabschlussAllgemeinbildend.data().getWertByKuerzel(kuerzel);
		if (other == null)
			return false;
		return (other == this);
	}

}
