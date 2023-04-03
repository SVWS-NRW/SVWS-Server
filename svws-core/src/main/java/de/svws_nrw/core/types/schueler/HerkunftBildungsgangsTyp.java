package de.svws_nrw.core.types.schueler;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.HerkunftBildungsgangTypKatalogEintrag;
import de.svws_nrw.core.types.schule.BerufskollegBildungsgangTyp;
import de.svws_nrw.core.types.schule.WeiterbildungskollegBildungsgangTyp;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen
 * Bildungsgangs-(schul-)typen bei einem Berufskolleg oder Weiterbildungskolleg
 * als Herkunft von Schülern eines Weiterbildungskollegs.
 */
public enum HerkunftBildungsgangsTyp {

	/** Weiterbildungskolleg: Abendgymnasium */
	AG(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(1000, WeiterbildungskollegBildungsgangTyp.ABENDGYMNASIUM, null, null, null)
	}),

	/** Weiterbildungskolleg: Abendrealschule */
	AR(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(2000, WeiterbildungskollegBildungsgangTyp.ABENDREALSCHULE, null, null, null)
	}),

	/** Weiterbildungskolleg: Abendrealschule */
	KL(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(3000, WeiterbildungskollegBildungsgangTyp.KOLLEG, null, null, null)
	}),

	/** Berufskolleg: Berufsfachschule */
	BF(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(10000, null, BerufskollegBildungsgangTyp.BERUFSFACHSCHULE, null, null)
	}),

	/** Berufskolleg: Berufschule */
	BS(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(11000, null, BerufskollegBildungsgangTyp.BERUFSSCHULE, null, null)
	}),

	/** Berufskolleg: Berufliches Gymnasium */
	BY(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(12000, null, BerufskollegBildungsgangTyp.BERUFLICHES_GYMNASIUM, null, null)
	}),

	/** Berufskolleg: Fachoberschule */
	FO(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(13000, null, BerufskollegBildungsgangTyp.FACHOBERSCHULE, null, null)
	}),

	/** Berufskolleg: Fachschule */
	FS(new HerkunftBildungsgangTypKatalogEintrag[] {
		new HerkunftBildungsgangTypKatalogEintrag(14000, null, BerufskollegBildungsgangTyp.FACHSCHULE, null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 1;

	/** Der aktuellen Daten des Bildungsgangtyps, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftBildungsgangTypKatalogEintrag daten;

	/** Die Historie mit den Einträgen des Bildungsgangtyps */
	public final @NotNull HerkunftBildungsgangTypKatalogEintrag@NotNull[] historie;

	/** Eine Hashmap mit allen definierten Bildungsgangtypen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, HerkunftBildungsgangsTyp> _ebenen = new HashMap<>();


	/**
	 * Erzeugt eine neuen Herkunfts-Bildungsgangtyp in der Aufzählung.
	 *
	 * @param historie   die Historie des Herkunfts-Bildungsgangtyps, welche ein Array von
	 *                   {@link HerkunftBildungsgangTypKatalogEintrag} ist
	 */
	HerkunftBildungsgangsTyp(final @NotNull HerkunftBildungsgangTypKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Bildungsgangtypen auf die
	 * zugehörigen Bildungsgangtypen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Bildungsgangtypen
	 */
	private static @NotNull HashMap<@NotNull String, HerkunftBildungsgangsTyp> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final HerkunftBildungsgangsTyp s : HerkunftBildungsgangsTyp.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt den Herkunfts-Bildungsgangtypen für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel des Herkunfts-Bildungsgangtyps
	 *
	 * @return der Herkunfts-Bildungsgangtyp oder null, falls das Kürzel ungültig ist
	 */
	public static HerkunftBildungsgangsTyp getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
