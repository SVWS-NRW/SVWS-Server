package de.svws_nrw.transpiler.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Schulformen.
 */
public enum Schulform implements @NotNull CoreType<@NotNull SchulformKatalogEintrag, @NotNull Schulform> {

	/** Berufskolleg */
	BK,

	/** Freie Waldorfschule */
	FW,

	/** Grundschule */
	G,

	/** Gesamtschule */
	GE,

	/** Gemeinschaftsschule */
	GM,

	/** Gymnasium */
	GY,

	/** Hauptschule */
	H,

	/** Hibernia */
	HI,

	/** Schulversuch PRIMUS */
	PS,

	/** Realschule */
	R,

	/** Förderschule im Bereich G/H */
	S,

	/** Klinikschule */
	KS,

	/** Förderschule im Bereich Berufskolleg */
	SB,

	/** Förderschule im Bereich Gymnasium */
	SG,

	/** Sekundarschule */
	SK,

	/** Förderschule im Bereich Realschule */
	SR,

	/** nicht umorganisierte Volksschule */
	V,

	/** Weiterbildungskolleg */
	WB,

	/** Freie Waldorfschule (Förderschule) */
	WF;


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<@NotNull Integer, @NotNull List<@NotNull Schulform>> _mapSchuljahrToSchulformenMitGymOb = new HashMap<>();



	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeEnumDataManager<@NotNull SchulformKatalogEintrag, @NotNull Schulform> manager) {
		CoreTypeEnumDataManager.putManager(Schulform.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeEnumDataManager<@NotNull SchulformKatalogEintrag, @NotNull Schulform> data() {
		return CoreTypeEnumDataManager.getManager(Schulform.class);
	}


	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück, welche
	 * in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static @NotNull List<@NotNull Schulform> getListMitGymOb(final int schuljahr) {
		List<@NotNull Schulform> result = _mapSchuljahrToSchulformenMitGymOb.get(schuljahr);
		if (result == null) {
			result = new ArrayList<>(data().getWerteBySchuljahr(schuljahr));
			for (int i = result.size() - 1; i >= 0; i--) {
				final @NotNull Schulform sf = result.get(i);
				final SchulformKatalogEintrag eintrag = data().getEintragBySchuljahrUndWert(schuljahr, sf);
				if ((eintrag == null) || (!eintrag.hatGymOb))
					result.remove(i);
				_mapSchuljahrToSchulformenMitGymOb.put(schuljahr, result);
			}
		}
		return result;
	}

}
