package de.svws_nrw.core.types.schueler;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.EinschulungsartKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Einschulungsart.
 */
public enum Einschulungsart {

	/** Einschulungsart: Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben */
	E51(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(1000, "51", "älter als 6. Jahre",
					"Kinder, die bis zum gültigen Einschulungsstichtag das 6. Lebensjahr vollendet haben", null, null)
	}),

	/** Einschulungsart: Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden */
	E52(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(2000, "52", "jünger als 6 Jahre",
					"Kinder, die nach dem gültigen Einschulungsstichtag das 6. Lebensjahr vollenden", null, null)
	}),

	/** Einschulungsart: Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen */
	E53(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(3000, "53", "zurückgestellt (§35 Abs. 3 SchulG)",
					"Kinder, die in diesem Schuljahr erstmals gemäß §35 Abs. 3 SchulG eine Schule besuchen", null, null)
	}),

	/** Einschulungsart: Kinder, die erstmals eine Früherziehung besuchen */
	E54(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(4000, "54", "Früherziehung", "Kinder, die erstmals eine Früherziehung besuchen", null, null)
	}),

	/** Einschulungsart: Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung */
	E18(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(7000, "18", "vorher: Früherziehung", "Im abgelaufenen Schuljahr: Teilnahme an einer Früherziehung", null, null)
	}),

	/** Einschulungsart: Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten */
	E19(new EinschulungsartKatalogEintrag[] {
			new EinschulungsartKatalogEintrag(8000, "19", "vorher: Förderschulkindergarten",
					"Im abgelaufenen Schuljahr: Besuch eines Förderschul-(nicht Sonder)kindergarten", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Einschulungsart, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull EinschulungsartKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Einschulungsart */
	public final @NotNull EinschulungsartKatalogEintrag @NotNull [] historie;

	/** Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<String, Einschulungsart> _mapBySchluessel = new HashMap<>();

	/** Eine Hashmap mit allen definierten Einschulungsarten, zugeordnet zu ihren IDs */
	private static final @NotNull HashMap<Long, Einschulungsart> _mapByID = new HashMap<>();

	/**
	 * Erzeugt eine neuen Einschulungsart in der Aufzählung.
	 *
	 * @param historie   die Historie der Einschulungsart, welche ein Array von
	 *                   {@link EinschulungsartKatalogEintrag} ist
	 */
	Einschulungsart(final @NotNull EinschulungsartKatalogEintrag @NotNull [] historie) {
		this.historie = historie;
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Einschulungsarten auf die
	 * zugehörigen Einschulungsarten zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Einschulungsarten
	 */
	private static @NotNull HashMap<String, Einschulungsart> getMapBySchluessel() {
		if (_mapBySchluessel.size() == 0) {
			for (final Einschulungsart s : Einschulungsart.values()) {
				if (s.daten != null)
					_mapBySchluessel.put(s.daten.kuerzel, s);
			}
		}
		return _mapBySchluessel;
	}


	/**
	 * Gibt die Einschulungsart für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Einschulungsart
	 *
	 * @return die Einschulungsart oder null, falls das Kürzel ungültig ist
	 */
	public static Einschulungsart getBySchluessel(final String kuerzel) {
		return getMapBySchluessel().get(kuerzel);
	}


	/**
	 * Gibt eine Map von den IDs der Einschulungsarten auf die
	 * zugehörigen Einschulungsarten zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den IDs auf die zugehörigen Einschulungsarten
	 */
	private static @NotNull HashMap<Long, Einschulungsart> getMapByID() {
		if (_mapByID.size() == 0) {
			for (final Einschulungsart s : Einschulungsart.values()) {
				if (s.daten != null)
					_mapByID.put(s.daten.id, s);
			}
		}
		return _mapByID;
	}


	/**
	 * Gibt die Einschulungsart für die angegebene ID zurück.
	 *
	 * @param id   die ID der Einschulungsart
	 *
	 * @return die Einschulungsart oder null, falls die ID ungültig ist
	 */
	public static Einschulungsart getByID(final Long id) {
		return getMapByID().get(id);
	}

}
