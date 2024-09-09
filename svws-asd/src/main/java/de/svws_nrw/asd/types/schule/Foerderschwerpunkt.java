package de.svws_nrw.asd.types.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


	/** Eine Map, welche die Menge der erlaubten Schulformen den IDs der Förderschwerpunkt-Einträge zuordnet. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<Integer, Map<Schulform, List<Foerderschwerpunkt>>> _mapFoerderschwerpunkteBySchuljahrAndSchulform = new HashMap<>();



	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FoerderschwerpunktKatalogEintrag, Foerderschwerpunkt> manager) {
		CoreTypeDataManager.putManager(Foerderschwerpunkt.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
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
	public boolean hatSchulform(final int schuljahr, final Schulform schulform) {
		final FoerderschwerpunktKatalogEintrag fske = this.daten(schuljahr);
		if (fske == null)
			return false;
		final Set<Schulform> schulformen = _mapSchulformenByID.get(fske.id);
		return (schulformen != null) && (schulformen.contains(schulform));
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
		final Map<Schulform, List<Foerderschwerpunkt>> mapFoerderschwerpunkteBySchulformOfSchuljahr =
				_mapFoerderschwerpunkteBySchuljahrAndSchulform.computeIfAbsent(schuljahr, k -> new HashMap<Schulform, List<Foerderschwerpunkt>>());
		if (mapFoerderschwerpunkteBySchulformOfSchuljahr == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<Foerderschwerpunkt> result = mapFoerderschwerpunkteBySchulformOfSchuljahr.get(schulform);
		if (result == null) {
			result = new ArrayList<>();
			final List<Foerderschwerpunkt> schwerpunkte = Foerderschwerpunkt.data().getWerteBySchuljahr(schuljahr);
			for (final @NotNull Foerderschwerpunkt schwerpunkt : schwerpunkte)
				if (schwerpunkt.hatSchulform(schuljahr, schulform))
					result.add(schwerpunkt);
			mapFoerderschwerpunkteBySchulformOfSchuljahr.put(schulform, result);
		}
		return result;
	}


}
