package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.HerkunftBildungsgangKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Herkünfte bezüglich von Bildungsgängen
 * beim Wechsel von einem Berufskolleg an ein Berufskolleg.
 */
public enum HerkunftBildungsgang implements CoreType<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> {

	/** Berufsschule, Fachklassen (Teilzeit) */
	A01,

	/** Berufsschule, Fachklassen/Fachhochschulreife (Teilzeit) */
	A02,

	/** Berufsschule, Fachklassen/erweiterte Zusatzqualifikation (Teilzeit) */
	A03,

	/** Berufsschule, Fachklassen mit erweitertem Stützunterricht (Teilzeit) */
	A04,

	/** Berufsfachschule, Berufsabschluss/mittlerer Schulabschluss (nach BKAZVO, BBiG/HwO/ in Vollzeit) */
	A10,

	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit) */
	A11,

	/** Berufsschule, Ausbildungsvorbereitung (1-jährig, Vollzeit) */
	A12,

	/** Berufsschule, Ausbildungsvorbereitung (1-jährig, Teilzeit) */
	A13,

	/** Berufsabschluss (nach §50 BBiG/§40 HwO)/Mittlerer Schulabschluss */
	A14,

	/** Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife */
	A15,

	/** Fachklassen (nach §2 BKAZVO) */
	A16,

	/** Internationale Förderklasse */
	A17,

	/** Fit für Mehr */
	A18,

	/** Förderzentrum */
	A19,

	/** Berufsfachschule, Berufsabschluss/Fachoberschulreife (2-jährig, Vollzeit) */
	B01,

	/** Berufsfachschule, Berufsgrundbildung/Fachoberschulreife (2-jährig, Vollzeit) */
	B02,

	/** Berufsfachschule, Berufsabschluss/Fachoberschulreife (nach BKAZVO, BBiG/HwO, in Vollzeit) */
	B04,

	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (nach BKAZVO, BBiG/HwO in Vollzeit) */
	B05,

	/** Berufsfachschule, Berufliche Kenntnisse/Erweiterter Erster Schulabschluss (1-jährig, Vollzeit) */
	B06,

	/** Berufsfachschule, Berufliche Kenntnisse/Mittlerer Schulabschluss (1-jährig, Vollzeit) */
	B07,

	/** Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (2-jährig, Vollzeit) */
	B08,

	/** Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (3-jährig, Teilzeit) */
	B09,

	/** Berufsfachschule, Berufsabschluss/Erweiterter Erster Schulabschluss oder Mittlerer Schulabschluss (4-jährig, Teilzeit) */
	B10,

	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum, 3-jährig, Vollzeit) */
	C01,

	/** Berufsfachschule, Berufsabschluss (2-jährig, Vollzeit) */
	C02,

	/** Berufsfachschule, Berufliche Kenntnisse/FHR (HBFS) (2-jährig, Vollzeit) */
	C03,

	/** Fachoberschule, Fachoberschule Kl. 11 (1-jährig, Teilzeit) */
	C05,

	/** Fachoberschule, Fachoberschule Kl. 12S (1-jährig, Vollzeit) */
	C06,

	/** Fachoberschule, Fachoberschule Kl. 12B (2-jährig, Teilzeit) */
	C07,

	/** Fachoberschule, Fachoberschule Kl. 12B (1-jährig, Vollzeit) */
	C08,

	/** Fachoberschule, Fachoberschule Kl. 12B (3-jährig, Teilzeit) */
	C11,

	/** Berufsfachschule, Berufsabschluss/Fachhochschulreife (mit Berufspraktikum; 3,5 -jährig, Vollzeit) */
	C12,

	/** Berufsabschluss/FHR (gestuft), (3jährig, Vollzeit) */
	C13,

	/** Berufliches Gymnasium, Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum; 4-jährig, Vollzeit) */
	D01,

	/** Berufliches Gymnasium, Berufliche Kenntnisse/Allg. Hochschulreife */
	D02,

	/** Fachoberschule, Fachoberschule Kl. 13 (1-jährig, Vollzeit) */
	D05,

	/** Fachoberschule, Fachoberschule Kl. 13 (2-jährig, Teilzeit) */
	D06,

	/** Fachschule (2-jährig, Vollzeit) */
	E01,

	/** Fachschule (4-jährig, Teilzeit) */
	E02,

	/** Fachschule (verkürzt/1-jährig, Vollzeit/Teilzeit) */
	E03,

	/** Fachschule (verkürzt/2-jährig, Teilzeit) */
	E04,

	/** Fachschule für Sozialwesen (mit Berufspraktikum/3-jährig, Vollzeit) */
	E05,

	/** Fachschule für Sozialwesen (mit Berufspraktikum/6-jährig, Teilzeit) */
	E07,

	/** Fachschule (3-jährig, Teilzeit) */
	E13;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> manager) {
		CoreTypeDataManager.putManager(HerkunftBildungsgang.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<HerkunftBildungsgangKatalogEintrag, HerkunftBildungsgang> data() {
		return CoreTypeDataManager.getManager(HerkunftBildungsgang.class);
	}

}
