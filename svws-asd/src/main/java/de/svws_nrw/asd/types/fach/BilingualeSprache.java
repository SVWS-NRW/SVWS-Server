package de.svws_nrw.asd.types.fach;

import java.util.HashMap;
import java.util.Set;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.fach.BilingualeSpracheKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Fremdsprachen, welche an Schulen bilingual
 * unterrichtet werden können.
 */
public enum BilingualeSprache implements CoreType<BilingualeSpracheKatalogEintrag, BilingualeSprache> {

	/** Bilinguale Sprache Englisch */
	ENGLISCH,

	/** Bilinguale Sprache Französisch */
	FRANZOESISCH,

	/** Bilinguale Sprache Italienisch */
	ITALIENISCH,

	/** Bilinguale Sprache Niederländisch */
	NIEDERLAENDISCH,

	/** Bilinguale Sprache Spanisch */
	SPANISCH,

	/** Bilinguale Sprache Türkisch */
	TUERKISCH,

	/** Bilinguale Sprache Neugriechisch */
	NEUGRIECHIESCH;


	/** Die Menge der Schulformen. Diese ist nach der Initialisierung nicht leer. */
	private static final @NotNull HashMap<Long, Set<Schulform>> _mapSchulformenByID = new HashMap<>();
	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache> manager) {
		CoreTypeDataManager.putManager(BilingualeSprache.class, manager);
		for (final var ct : data().getWerte())
			for (final var e : ct.historie())
				_mapSchulformenByID.put(e.id, Schulform.data().getWerteByBezeichnerAsNonEmptySet(e.schulformen));
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<BilingualeSpracheKatalogEintrag, BilingualeSprache> data() {
		return CoreTypeDataManager.getManager(BilingualeSprache.class);
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
		final BilingualeSpracheKatalogEintrag bske = this.daten(schuljahr);
		if (bske != null) {
			final Set<Schulform> result = _mapSchulformenByID.get(bske.id);
			if (result == null)
				throw new CoreTypeException("Fehler beim prüfen der Schulform. Der Core-Type %s ist nicht korrekt initialisiert.".formatted(this.getClass().getSimpleName()));
			return result.contains(sf);
		}
		return false;
	}

}
