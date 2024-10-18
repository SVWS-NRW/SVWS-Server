package de.svws_nrw.asd.types.schule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Schulformen.
 */
public enum Schulform implements @NotNull CoreType<SchulformKatalogEintrag, Schulform> {

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

	private static final @NotNull Map<Integer, List<Schulform>> _mapSchuljahrToSchulformenMitGymOb = new HashMap<>();

	private static List<Schulform> _listSchulformenMitGymOb = null;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SchulformKatalogEintrag, Schulform> manager) {
		CoreTypeDataManager.putManager(Schulform.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SchulformKatalogEintrag, Schulform> data() {
		return CoreTypeDataManager.getManager(Schulform.class);
	}


	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück, welche
	 * in dem angebenen Schuljahr gültig sind.
	 *
	 * @param schuljahr   das Schuljahr
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static @NotNull List<Schulform> getListMitGymOb(final int schuljahr) {
		List<Schulform> result = _mapSchuljahrToSchulformenMitGymOb.get(schuljahr);
		if (result == null) {
			result = new ArrayList<>(data().getWerteBySchuljahr(schuljahr));
			for (int i = result.size() - 1; i >= 0; i--) {
				final @NotNull Schulform sf = result.get(i);
				final SchulformKatalogEintrag eintrag = data().getEintragBySchuljahrUndWert(schuljahr, sf);
				if ((eintrag == null) || (!eintrag.hatGymOb))
					result.remove(i);
			}
			_mapSchuljahrToSchulformenMitGymOb.put(schuljahr, result);
		}
		return result;
	}

	/**
	 * Gibt alle Schulformen dieser Aufzählung mit gymnasialer Oberstufe zurück, welche
	 * in irgendeinem Schuljahr gültig waren.
	 *
	 * @return eine {@link List} mit allen Schulformen, welche eine gymnasiale Oberstufe haben.
	 */
	public static @NotNull List<Schulform> getListAllMitGymOb() {
		List<Schulform> result = _listSchulformenMitGymOb;
		if (result == null) {
			result = new ArrayList<>(data().getWerte());
			for (int i = result.size() - 1; i >= 0; i--) {
				final @NotNull Schulform sf = result.get(i);
				boolean hatGymOb = false;
				for (final SchulformKatalogEintrag sfke : sf.historie())
					if (sfke.hatGymOb)
						hatGymOb = true;
				if (!hatGymOb)
					result.remove(i);
			}
			_listSchulformenMitGymOb = result;
		}
		return result;
	}


	/**
	 * Gibt zurück, ob es sich um eine allgemeinbildende Schulform handelt.
	 *
	 * @return true/false
	 */
	public boolean istAllgemeinbildend() {
		return switch (this) {
			case FW, G, GE, GM, GY, H, HI, PS, R, S, KS, SG, SK, SR, V, WF -> true;
			default -> false;
		};
	}

	/**
	 * Gibt zurück, ob es sich um eine berufsbildende Schulform handelt.
	 *
	 * @return true/false
	 */
	public boolean istBerufsbildend() {
		return switch (this) {
			case BK, FW, HI, KS, SB, WF -> true;
			default -> false;
		};
	}

	/**
	 * Gibt zurück, ob es sich um eine Schulform für Weiterbildung handelt.
	 *
	 * @return true/false
	 */
	public boolean istWeiterbildung() {
		return switch (this) {
			case WB -> true;
			default -> false;
		};
	}

}
