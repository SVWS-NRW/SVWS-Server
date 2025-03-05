package de.svws_nrw.asd.types.klassen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.klassen.KlassenartKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Klassenarten.
 */
public enum Klassenart implements CoreType<KlassenartKatalogEintrag, Klassenart> {

	/** Klassenart: Kein Eintrag */
	UNDEFINIERT,

	/** Klassenart: Hauptschulklasse 1A */
	HA_1A,

	/** Klassenart: Hauptschulklasse 1B */
	HA_1B,

	/** Klassenart: Hauptschuleklasse ohne Differenzierung nach A und B */
	HA_AB,

	/** Klassenart: Frühförderung: SKG (Ambulante Maßnahmen für blinde, gehörlose, sehbeh. und schwerh. Kinder) */
	AM,

	/** Klassenart: Deutschförderung (ohne Bildungsgangzuordnung, BASS 13-63 Nr. 3) */
	DF,

	/** Klassenart: Frühförderung: SKG (Präsenzgruppe) */
	PG,

	/** Klassenart: Profilklasse (gemäß § 21 Abs. 3 APO-S I) */
	PK,

	/** Klassenart: Regelklasse */
	RK,

	/** Klassenart: Deutschförderklasse (gemäß BASS 13-63 Nr. 3, Nummer 3.5.1) */
	SG;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();

	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<Integer, Map<Schulform, List<Klassenart>>> _mapBySchuljahrAndSchulform = new HashMap<>();


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart> manager) {
		CoreTypeDataManager.putManager(Klassenart.class, manager);
		_mapSchulformenByID.clear();
		_mapBySchuljahrAndSchulform.clear();
		for (final var ct : data().getWerte())
			for (final var e : ct.historie()) {
				final Set<Schulform> tmpSet = new HashSet<>();
				for (final var s : e.zulaessig)
					tmpSet.add(Schulform.data().getWertByBezeichner(s.schulform));
				_mapSchulformenByID.put(e.id, tmpSet);
			}
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KlassenartKatalogEintrag, Klassenart> data() {
		return CoreTypeDataManager.getManager(Klassenart.class);
	}


	/**
	 * Prüft, ob die Schulform bei diesem Core-Type-Wert in dem angegeben Schuljahr zulässig ist oder nicht.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 * @param sf          die Schulform, auf die geprüft wird
	 *
	 * @return true, falls die Schulform zulässig ist, und ansonsten false
	 */
	public boolean hatSchulform(final int schuljahr, final @NotNull Schulform sf) {
		final KlassenartKatalogEintrag ke = this.daten(schuljahr);
		if (ke != null) {
			final Set<Schulform> result = _mapSchulformenByID.get(ke.id);
			if (result == null)
				throw new CoreTypeException(
						"Fehler beim Prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}


	/**
	 * Liefert alle zulässigen Klassenarten für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Klassenarten
	 */
	public static @NotNull List<Klassenart> getBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		final Map<Schulform, List<Klassenart>> mapBySchulform =
				_mapBySchuljahrAndSchulform.computeIfAbsent(schuljahr, k -> new HashMap<Schulform, List<Klassenart>>());
		if (mapBySchulform == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<Klassenart> result = mapBySchulform.get(schulform);
		if (result == null) {
			result = new ArrayList<>();
			final List<Klassenart> klassenarten = Klassenart.data().getWerteBySchuljahr(schuljahr);
			for (final @NotNull Klassenart klassenart : klassenarten)
				if (klassenart.hatSchulform(schuljahr, schulform))
					result.add(klassenart);
			mapBySchulform.put(schulform, result);
		}
		return result;
	}


	/**
	 * Gibt eine Klassenart zurück, welche für die übergebene Schulform als Voreinstellung
	 * für neue Klassen genutzt wird,
	 *
	 * @param schulform   die Schulform
	 *
	 * @return die Default-Klassenart
	 */
	public static @NotNull Klassenart getDefault(final @NotNull Schulform schulform) {
		return switch (schulform) {
			case FW, HI, WF, G, GE, GM, GY, H, PS, R, S, KS, SG, SK, SR, V -> Klassenart.RK;
			case WB, BK, SB -> Klassenart.UNDEFINIERT;
			default -> Klassenart.RK;
		};
	}

}
