package de.nrw.schule.svws.core.types.schueler;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.HerkunftBildungsgangKatalogEintrag;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen 
 * Herkünfte bezüglich von Bildungsgängen beim Wechsel
 * von einem Berufskolleg an ein Berufskolleg.
 */
public enum HerkunftBildungsgang {

	/** Berufsschule, Fachklassen (Teilzeit) */
	A01(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(1000, Schulgliederung.A01, null, null)
	}),


	/** Berufsschule, Fachklassen/Fachhochschulreife (Teilzeit) */
	A02(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(2000, Schulgliederung.A02, null, null)
	}),


	/** Berufsschule, Fachklassen/erweiterte Zusatzqualifikation (Teilzeit) */
	A03(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(3000, Schulgliederung.A03, null, null)
	}),


	/** Berufsschule, Fachklassen mit erweitertem Stützunterricht (Teilzeit) */
	A04(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(4000, Schulgliederung.A04, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/mittlerer Schulabschluss (nach BKAZVO, BBiG/HwO/ in Vollzeit) */
	A10(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(5000, Schulgliederung.A10, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit) */
	A11(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(6000, Schulgliederung.A11, null, null)
	}),


	/** Berufsschule, Ausbildungsvorbereitung (1-jährig, Vollzeit) */
	A12(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(7000, Schulgliederung.A12, null, null)
	}),


	/** Berufsschule, Ausbildungsvorbereitung (1-jährig, Teilzeit) */
	A13(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(8000, Schulgliederung.A13, null, null)
	}),


	/** Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss */
	A14(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(9000, Schulgliederung.A14, null, null)
	}),


	/** Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife */
	A15(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(10000, Schulgliederung.A15, null, null)
	}),


	/** Fachklassen (nach §2 BKAZVO) */
	A16(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(11000, Schulgliederung.A16, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Fachoberschulreife (2-jährig, Vollzeit) */
	B01(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(20000, Schulgliederung.B01, null, null)
	}),


	/** Berufsfachschule, Berufsgrundbildung/Fachoberschulreife (2-jährig, Vollzeit) */
	B02(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(21000, Schulgliederung.B02, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Fachoberschulreife (nach BKAZVO, BBiG/HwO, in Vollzeit) */
	B04(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(22000, Schulgliederung.B04, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit) */
	B05(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(23000, Schulgliederung.B05, null, null)
	}),


	/** Berufsfachschule, Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (1-jährig, Vollzeit) */
	B06(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(24000, Schulgliederung.B06, null, null)
	}),


	/** Berufsfachschule, Berufliche Kenntnisse/mittlerer Schulabschluss (1-jährig, Vollzeit) */
	B07(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(25000, Schulgliederung.B07, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Hauptschulabschluss Kl. 10 oder mittlerer Schulabschluss (2-jährig, Vollzeit) */
	B08(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(26000, Schulgliederung.B08, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Hauptschulabschluss Kl. 10 oder mittlerer Schulabschluss (3-jährig, Teilzeit) */
	B09(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(27000, Schulgliederung.B09, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Hauptschulabschluss Kl. 10 oder mittlerer Schulabschluss (4-jährig, Teilzeit) */
	B10(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(28000, Schulgliederung.B10, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum, 3-jährig, Vollzeit) */
	C01(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(40000, Schulgliederung.C01, null, null)
	}),


	/** Berufsfachschule, Berufsabschluss (2-jährig, Vollzeit) */
	C02(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(41000, Schulgliederung.C02, null, null)
	}),


	/** Berufsfachschule, Berufliche Kenntnisse/FHR (HBFS) (2-jährig, Vollzeit) */
	C03(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(42000, Schulgliederung.C03, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 11 (1-jährig, Teilzeit) */
	C05(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(43000, Schulgliederung.C05, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 12S (1-jährig, Vollzeit) */
	C06(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(44000, Schulgliederung.C06, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 12B (2-jährig, Teilzeit) */
	C07(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(45000, Schulgliederung.C07, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 12B (1-jährig, Vollzeit) */
	C08(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(46000, Schulgliederung.C08, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 12B (3-jährig, Teilzeit) */
	C11(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(47000, Schulgliederung.C11, null, null)
	}),


	/**  3,5 -jährig, Vollzeit)" */
	C12(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(48000, Schulgliederung.C12, null, null)
	}),


	/** Berufsabschluss/FHR (gestuft), (3jährig, Vollzeit) */
	C13(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(49000, Schulgliederung.C13, null, null)
	}),


	/**  4-jährig, Vollzeit)" */
	D01(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(60000, Schulgliederung.D01, null, null)
	}),


	/** Berufliches Gymnasium, Berufliche Kenntnisse/Allg. Hochschulreife */
	D02(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(61000, Schulgliederung.D02, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 13 (1-jährig, Vollzeit) */
	D05(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(62000, Schulgliederung.D05, null, null)
	}),


	/** Fachoberschule, Fachoberschule Kl. 13 (2-jährig, Teilzeit) */
	D06(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(63000, Schulgliederung.D06, null, null)
	}),


	/** Fachschule (2-jährig, Vollzeit) */
	E01(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(80000, Schulgliederung.E01, null, null)
	}),


	/** Fachschule (4-jährig, Teilzeit) */
	E02(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(81000, Schulgliederung.E02, null, null)
	}),


	/** Fachschule (verkürzt/1-jährig, Vollzeit/Teilzeit) */
	E03(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(82000, Schulgliederung.E03, null, null)
	}),


	/** Fachschule (verkürzt/2-jährig, Teilzeit) */
	E04(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(83000, Schulgliederung.E04, null, null)
	}),


	/** Fachschule für Sozialwesen (mit Berufspraktikum/3-jährig, Vollzeit) */
	E05(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(84000, Schulgliederung.E05, null, null)
	}),


	/** Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit) */
	E07(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(85000, Schulgliederung.E07, null, null)
	}),


	/** Fachschule (3-jährig, Teilzeit) */
	E13(new HerkunftBildungsgangKatalogEintrag[] {
		new HerkunftBildungsgangKatalogEintrag(86000, Schulgliederung.E13, null, null)
	});
	

	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten des Herkunftsbildungsganges, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftBildungsgangKatalogEintrag daten;

	/** Die Historie mit den Einträgen des Herkunftsbildungsganges */
	public final @NotNull HerkunftBildungsgangKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Herkunftsbildungsgängen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, HerkunftBildungsgang> _ebenen = new HashMap<>();


	/**
	 * Erzeugt einen neuen Herkunftsbildungsgang in der Aufzählung.
	 * 
	 * @param historie   die Historie des Herkunftsbildungsganges, welche ein Array von 
	 *                   {@link HerkunftBildungsgangKatalogEintrag} ist  
	 */
	private HerkunftBildungsgang(@NotNull HerkunftBildungsgangKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsbildungsgänge auf die 
	 * zugehörigen Herkunftsbildungsgänge zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Herkunftsbildungsgänge
	 */
	private static @NotNull HashMap<@NotNull String, HerkunftBildungsgang> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (HerkunftBildungsgang s : HerkunftBildungsgang.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt den Herkunftsbildungsgang für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des Herkunftsbildungsganges
	 * 
	 * @return der Herkunftsbildungsgang oder null, falls das Kürzel ungültig ist
	 */
	public static HerkunftBildungsgang getByKuerzel(String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
