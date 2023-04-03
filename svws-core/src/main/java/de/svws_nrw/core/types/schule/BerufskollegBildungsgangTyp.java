package de.svws_nrw.core.types.schule;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.BildungsgangTypKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den (Schul-)Typ des Bildungsgangs am Berufskolleg.
 */
public enum BerufskollegBildungsgangTyp {

	/** Berufsfachschulen */
	BERUFSFACHSCHULE(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(1000, "BF", "Berufsfachschulen", null, null)
	}),

	/** Berufsfachschulen */
	BERUFSSCHULE(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(2000, "BS", "Berufsschule", null, null)
	}),

	/** Berufliches Gymnasium */
	BERUFLICHES_GYMNASIUM(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(3000, "BY", "Berufliches Gymnasium", null, null)
	}),

	/** Fachoberschule */
	FACHOBERSCHULE(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(4000, "FO", "Fachoberschule", null, null)
	}),

	/** Fachschule */
	FACHSCHULE(new BildungsgangTypKatalogEintrag[] {
		new BildungsgangTypKatalogEintrag(5000, "FS", "Fachschule", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten der Berufsschultypen von Bildungsgängen, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull BildungsgangTypKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Berufsschultypen von Bildungsgängen */
	public final @NotNull BildungsgangTypKatalogEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen definierten Berufsschultypen von Bildungsgängen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, @NotNull BerufskollegBildungsgangTyp> _ebenen = new HashMap<>();


	/**
	 * Erzeugt einen neuen Berufsschultyp von Bildungsgängen in der Aufzählung.
	 *
	 * @param historie   die Historie der Berufsschultypen von Bildungsgängen, welches ein Array von
	 *                   {@link BildungsgangTypKatalogEintrag} ist
	 */
	BerufskollegBildungsgangTyp(final @NotNull BildungsgangTypKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Berufsschultypen von Bildungsgängen auf die
	 * zugehörigen Berufsschultypen von Bildungsgängen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln der Typen auf die zugehörigen Berufsschultypen von Bildungsgängen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull BerufskollegBildungsgangTyp> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final BerufskollegBildungsgangTyp s : BerufskollegBildungsgangTyp.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt den Berufsschultyp von Bildungsgängen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Berufsschultyps von Bildungsgängen
	 *
	 * @return der Berufsschultyp von Bildungsgängen oder null, falls das Kürzel ungültig ist
	 */
	public static BerufskollegBildungsgangTyp getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
