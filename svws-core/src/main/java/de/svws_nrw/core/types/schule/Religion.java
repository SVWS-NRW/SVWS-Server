package de.svws_nrw.core.types.schule;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.ReligionKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik erhobenen Religionen.
 */
public enum Religion {

	/** Religion: alevitisch */
	AR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(1000, "AR", "alevitisch", null, null)
	}),

	/** Religion: evangelisch */
	ER(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(2000, "ER", "evangelisch", null, null)
	}),

	/** Religion: jüdisch */
	HR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(3000, "HR", "jüdisch", null, null)
	}),

	/** Religion: islamisch */
	IR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(4000, "IR", "islamisch", null, null)
	}),

	/** Religion: katholisch */
	KR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(5000, "KR", "katholisch", null, null)
	}),

	/** Religion: mennonitische BG NRW */
	ME(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(6000, "ME", "mennonitische BG NRW", null, null)
	}),

	/** Religion: ohne Bekenntnis */
	OH(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(7000, "OH", "ohne Bekenntnis", null, null)
	}),

	/** Religion: griechisch-orthodox */
	OR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(8000, "OR", "griechisch-orthodox", null, null)
	}),

	/** Religion: syrisch-orthodox */
	SO(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(9000, "SO", "syrisch-orthodox", null, null)
	}),

	/** Religion: sonstige orthodoxe */
	XO(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(10000, "XO", "sonstige orthodoxe", null, null)
	}),

	/** Religion: andere Religionen */
	XR(new ReligionKatalogEintrag[] {
			new ReligionKatalogEintrag(11000, "XR", "andere Religionen", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Religionen */
	public final @NotNull ReligionKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Religionen */
	public final @NotNull ReligionKatalogEintrag @NotNull [] historie;

	/** Eine Hashmap mit allen definierten Religionen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<String, Religion> _mapByKuerzel = new HashMap<>();

	/** Eine Map mit allen Historien-Einträgen, welche ihrer ID zugeordnet sind. */
	private static final @NotNull HashMap<Long, ReligionKatalogEintrag> _mapEintragById = new HashMap<>();

	/** Eine Hashmap mit allen definierten Religionen, zugeordnet zu den IDs der Historieneinträgen */
	private static final @NotNull HashMap<Long, Religion> _mapById = new HashMap<>();



	/**
	 * Erzeugt eine neue Religion in der Aufzählung.
	 *
	 * @param historie   die Historie der Religionen, welche ein Array von
	 *                   {@link ReligionKatalogEintrag} ist
	 */
	Religion(final @NotNull ReligionKatalogEintrag @NotNull [] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Religionen auf die
	 * zugehörigen Religionen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Religionen
	 */
	private static @NotNull HashMap<String, Religion> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0) {
			for (final @NotNull Religion s : Religion.values()) {
				if (s.daten != null)
					_mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapByKuerzel;
	}


	/**
	 * Gibt eine Map von den IDs der Historien-Einträge der Religionen auf die
	 * zugehörigen Religionen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs der Religion-Katalog-Einträge auf die zugehörigen Religionen
	 */
	private static @NotNull HashMap<Long, Religion> getMapById() {
		if (_mapById.size() == 0) {
			for (final @NotNull Religion s : Religion.values()) {
				for (final @NotNull ReligionKatalogEintrag e : s.historie)
					_mapById.put(e.id, s);
			}
		}
		return _mapById;
	}


	/**
	 * Gibt eine Map von den IDs der Religion-Katalog-Einträge auf die zugehörigen
	 * Religion-Katalog-Einträge zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzels der Religion-Katalog-Einträge auf die zugehörigen Religion-Katalog-Einträge
	 */
	private static @NotNull HashMap<Long, ReligionKatalogEintrag> getMapEintragById() {
		if (_mapEintragById.size() == 0) {
			for (final Religion s : Religion.values()) {
				for (final @NotNull ReligionKatalogEintrag e : s.historie)
					_mapEintragById.put(e.id, e);
			}
		}
		return _mapEintragById;
	}


	/**
	 * Gibt die Religion für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Religion
	 *
	 * @return die Religion oder null, falls das Kürzel ungültig ist
	 */
	public static Religion getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}


	/**
	 * Gibt die Religion für die angegebene ID zurück.
	 *
	 * @param id   die ID der Religion
	 *
	 * @return die Religion oder null, falls die ID ungültig ist
	 */
	public static Religion getByID(final long id) {
		return getMapById().get(id);
	}


	/**
	 * Gibt den Religion-Katalog-Eintrag anhand der angegebenen ID zurück.
	 *
	 * @param id   die ID
	 *
	 * @return der Religion-Katalog-Eintrag oder null, falls kein Eintrag mit dieser ID vorhanden ist
	 */
	public static ReligionKatalogEintrag getEintragByID(final long id) {
		return getMapEintragById().get(id);
	}


}
