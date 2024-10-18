package de.svws_nrw.asd.types.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.FoerderschwerpunktKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für den Katalog der möglichen Förderschwerpunkte
 */
public enum Foerderschwerpunkt implements @NotNull CoreType<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> {

	/** Förderschwerpunkt - kein Förderschwerpunkt */
	KEINER,

	/** Förderschwerpunkt - Sehen (Blinde) */
	BL,

	/** Förderschwerpunkt - Emotionale und soziale Entwicklung */
	EZ,

	/** Förderschwerpunkt - Geistige Entwicklung */
	GB,

	/** Förderschwerpunkt - Hören und Kommunikation (Gehörlose) */
	GH,

	/** Förderschwerpunkt - Körperliche und motorische Entwicklung */
	KB,

	/** Förderschwerpunkt - Schule für Kranke */
	KR,

	/** Förderschwerpunkt - Lernen */
	LB,

	/** Förderschwerpunkt - Präventive Förderung im Bereich Emotionale und soziale Entwicklung */
	PE,

	/** Förderschwerpunkt - Präventive Förderung */
	PF,

	/** Förderschwerpunkt - Präventive Förderung im Bereich Lernen */
	PL,

	/** Förderschwerpunkt - Präventive Förderung im Bereich Sprache */
	PS,

	/** Förderschwerpunkt - Sprache */
	SB,

	/** Förderschwerpunkt - Hören und Kommunikation (Schwerhörige) */
	SG,

	/** Förderschwerpunkt - Sehen (Sehbehinderte) */
	SH,

	/** Förderschwerpunkt - Kein Förderschwerpunkt */
	XX;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> manager) {
		CoreTypeDataManager.putManager(Foerderschwerpunkt.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> data() {
		return CoreTypeDataManager.getManager(Foerderschwerpunkt.class);
	}


	/**
	 * Prüft, ob der Förderschwerpunkt in dem angebenen Schuljahr bei der angegeben Schulform zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return true, wenn er zulässig ist uns ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		return data().hatSchulform(schuljahr, schulform, this);
	}


	/**
	 * Liefert alle zulässigen Förderschwerpunkte für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Förderschwerpunkte
	 */
	public static @NotNull List<Foerderschwerpunkt> getBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		return data().getListBySchuljahrAndSchulform(schuljahr, schulform);
	}

}
