package de.svws_nrw.asd.types.kaoa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.jahrgang.JahrgaengeKatalogEintrag;
import de.svws_nrw.asd.data.kaoa.KAOAKategorieKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die KAoA-Kategorien.
 */
public enum KAOAKategorie implements CoreType<KAOAKategorieKatalogEintrag, KAOAKategorie> {

	/** KAoA-Kategorie: Formen der Orientierung und Beratung */
	SBO_2,

	/** KAoA-Kategorie: Strukturen an Schulen */
	SBO_3,

	/** KAoA-Kategorie: Potenziale entdecken und den eigenen Standort bestimmen */
	SBO_4,

	/** KAoA-Kategorie: Berufsfelder erkunden und Informationen sammeln */
	SBO_5,

	/** KAoA-Kategorie: Praxis der Arbeitswelt kennenlernen und erproben */
	SBO_6,

	/** KAoA-Kategorie: Nachholung der Erstberufsorientierung */
	SBO_7,

	/** KAoA-Kategorie: Sekundarstufe II - Individuelle Voraussetzungen für eine Ausbildung oder ein Studium überprüfen */
	SBO_8,

	/** KAoA-Kategorie: Sekundarstufe II - Praxis vertiefen - Ausbildungs- und Studienwahl konkretisieren */
	SBO_9,

	/** KAoA-Kategorie: Gestaltung und Koordination der Übergänge in der Sek. I und Sek. II */
	SBO_10;


	/* ----- Die nachfolgenden Attribute werden nicht initialisiert und werden als Cache verwendet, um z.B. den Schuljahres-bezogenen Zugriff zu cachen ----- */

	private static final @NotNull Map<Integer, Map<KAOAKategorie, List<Jahrgaenge>>> _mapBySchuljahrAndKategorie = new HashMap<>();

	private static final @NotNull Map<Integer, Map<Long, List<KAOAKategorieKatalogEintrag>>> _mapEintraegeBySchuljahrAndJahrgang = new HashMap<>();

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> manager) {
		CoreTypeDataManager.putManager(KAOAKategorie.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<KAOAKategorieKatalogEintrag, KAOAKategorie> data() {
		return CoreTypeDataManager.getManager(KAOAKategorie.class);
	}


	/**
	 * Prüft, ob der angegebene Jahrgang in dem angegebenen Schuljahr für diese Kategorie zulässig ist.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param jahrgang    der Jahrgang
	 *
	 * @return true, falls der Jahrgang in dem Schuljahr zulässig ist - ansonsten false.
	 */
	public boolean hatJahrgang(final int schuljahr, final Jahrgaenge jahrgang) {
		return getListBySchuljahrAndKategorie(schuljahr, this).contains(jahrgang);
	}


	/**
	 * Liefert alle zulässigen Jahrgänge für die angegebene Kategorie in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param kategorie   die Kategorie
	 *
	 * @return die bei der Kategorie in dem angegebenen Schuljahr zulässigen Jahrgänge
	 */
	public static @NotNull List<Jahrgaenge> getListBySchuljahrAndKategorie(final int schuljahr, final @NotNull KAOAKategorie kategorie) {
		final Map<KAOAKategorie, List<Jahrgaenge>> mapByKategorie =
				_mapBySchuljahrAndKategorie.computeIfAbsent(schuljahr, k -> new HashMap<KAOAKategorie, List<Jahrgaenge>>());
		if (mapByKategorie == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<Jahrgaenge> result = mapByKategorie.get(kategorie);
		if (result == null) {
			result = new ArrayList<>();
			final KAOAKategorieKatalogEintrag eintrag = kategorie.daten(schuljahr);
			if (eintrag != null)
				for (final String jgBezeichner : eintrag.jahrgaenge)
					result.add(Jahrgaenge.data().getWertByBezeichner(jgBezeichner));
			mapByKategorie.put(kategorie, result);
		}
		return result;
	}

	/**
	 * Liefert alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr zurück.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param idJahrgang    der Jahrgang
	 *
	 * @return alle zulässigen KAoA-Kategorie-Historien-Einträge für den angegebenen Jahrgang in dem angegebenen Schuljahr.
	 */
	public static @NotNull List<KAOAKategorieKatalogEintrag> getListBySchuljahrAndJahrgang(final int schuljahr, final long idJahrgang) {
		final Map<Long, List<KAOAKategorieKatalogEintrag>> mapEintraegeByJahrgaenge =
				_mapEintraegeBySchuljahrAndJahrgang.computeIfAbsent(schuljahr, j -> new HashMap<Long, List<KAOAKategorieKatalogEintrag>>());
		if (mapEintraegeByJahrgaenge == null)
			throw new NullPointerException("computeIfAbsent darf nicht null liefern");
		List<KAOAKategorieKatalogEintrag> result = mapEintraegeByJahrgaenge.get(idJahrgang);
		if (result == null) {
			result = new ArrayList<>();
			final List<KAOAKategorie> werte = KAOAKategorie.data().getWerte();
			for (final KAOAKategorie kategorie : werte) {
				final KAOAKategorieKatalogEintrag eintrag = kategorie.daten(schuljahr);
				if (eintrag != null) {
					for (final String bezeichner : eintrag.jahrgaenge) {
						final JahrgaengeKatalogEintrag eintragJahrgang = Jahrgaenge.data().getWertByBezeichner(bezeichner).daten(schuljahr);
						if ((eintragJahrgang != null) && (eintragJahrgang.id == idJahrgang)) {
							result.add(eintrag);
							break;
						}
					}
				}
			}
			mapEintraegeByJahrgaenge.put(idJahrgang, result);
		}
		return result;
	}
}
