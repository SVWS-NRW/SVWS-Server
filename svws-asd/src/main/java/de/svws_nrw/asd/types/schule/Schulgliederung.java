package de.svws_nrw.asd.types.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für den Katalog der Schulformen.
 */
public enum Schulgliederung implements @NotNull CoreType<SchulgliederungKatalogEintrag, Schulgliederung> {

	/**
	 * Schulgliederung DEFAULT:
	 *   Standard für diese Schulform
	 */
	DEFAULT,

	/**
	 * Schulgliederung A01:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 01 (Fachklassen (BS; TZ))
	 */
	A01,

	/**
	 * Schulgliederung A02:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 02 (Fachklassen/Fachhochschulreife (BS/FHR; TZ))
	 */
	A02,

	/**
	 * Schulgliederung A03:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 03 (Fachklassen/erweiterte Zusatzqualifikation (BS/ZQ; TZ))
	 */
	A03,

	/**
	 * Schulgliederung A04:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 04 (Fachklassen mit erweitertem Stützunterricht (BS/Stütz; TZ))
	 */
	A04,

	/**
	 * Schulgliederung A05 (ausgelaufen):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 05 (Berufsorientierungsjahr (BV; VZ))
	 */
	A05,

	/**
	 * Schulgliederung A06 (ausgelaufen):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 06 (Berufsgrundschuljahr (BG; VZ))
	 */
	A06,

	/**
	 * Schulgliederung A07 (ausgelaufen):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 07 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; TZ) bzw. Werkstattjahr (BS 1j;TZ))
	 */
	A07,

	/**
	 * Schulgliederung A08 (ausgelaufen):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 08 (Vorpraktikum (VP))
	 */
	A08,

	/**
	 * Schulgliederung A09 (ausgelaufen):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 09 (Klassen für Schüler/innen ohne Ausbildungsverhältnis (BS 1j; VZ))
	 */
	A09,

	/**
	 * Schulgliederung A10 (auslaufend):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 10 (Berufsabschluss/mittlerer Schulabschluss (BKAZVO) (BAB/FOR; VZ))
	 */
	A10,

	/**
	 * Schulgliederung A11 (auslaufend):
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 11 (Berufsabschluss/Fachhochschulreife (BKAZVO) (BAB/FHR; VZ))
	 */
	A11,

	/**
	 * Schulgliederung A12:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 12 (Ausbildungsvorbereitung (BS 1j; VZ))
	 */
	A12,

	/**
	 * Schulgliederung A13:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 13 (Ausbildungsvorbereitung (BS 1j; TZ))
	 */
	A13,

	/**
	 * Schulgliederung A14:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 14 (Berufsabschluss (nach §50 BBiG/§40 HwO)/mittlerer Schulabschluss (BAB/FOR; VZ))
	 */
	A14,

	/**
	 * Schulgliederung A15:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 15 (Berufsabschluss (nach §50 BBiG/§40 HwO)/Fachhochschulreife BAB/FHR; VZ)
	 */
	A15,

	/**
	 * Schulgliederung A16:
	 *   Anlage A (Fachklassen duales System und Ausbildungsvorbereitung),
	 *   Typ 16 (Fachklassen (nach §2 BKAZVO) BAB; VZ)
	 */
	A16,

	/**
	 * Schulgliederung AB:
	 *   Schule für Kranke: Allgemeinbildend
	 */
	AB,

	/**
	 * Schulgliederung B01 (ausgelaufen):
	 *   Anlage B (Berufsfachschule),
	 *   Typ 01 (Berufsabschluss/Fachoberschulreife (BAB/FOR 2j; VZ))
	 */
	B01,

	/**
	 * Schulgliederung B02 (ausgelaufen):
	 *   Anlage B (Berufsfachschule),
	 *   Typ 02 (Berufsgrundbildung/Fachoberschulreife (BG/FOR 2j; VZ))
	 */
	B02,

	/**
	 * Schulgliederung B03 (ausgelaufen):
	 *   Anlage B (Berufsfachschule),
	 *   Typ 03 (Berufsgrundbildung (für Schüler mit FOR) (BG 1j; VZ))
	 */
	B03,

	/**
	 * Schulgliederung B04 (ausgelaufen):
	 *   Anlage B (Berufsfachschule),
	 *   Typ 04 (Berufsabschluss/Fachoberschulreife (BAB/FOR; VZ))
	 */
	B04,

	/**
	 * Schulgliederung B05 (ausgelaufen):
	 *   Anlage B (Berufsfachschule),
	 *   Typ 05 (Berufsabschluss/Fachhochschulreife (BAB/FHR; VZ))
	 */
	B05,

	/**
	 * Schulgliederung B06:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 06 (Berufliche Kenntnisse/Hauptschulabschluss Kl. 10 (BK/HSA10; 1j. VZ))
	 */
	B06,

	/**
	 * Schulgliederung B07:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 07 (Berufliche Kenntnisse/mittlerer Schulabschluss (BK/FOR; 1j. VZ))
	 */
	B07,

	/**
	 * Schulgliederung B08:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 08 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 2j. VZ))
	 */
	B08,

	/**
	 * Schulgliederung B09:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 09 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 3j. TZ))
	 */
	B09,

	/**
	 * Schulgliederung B10:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 10 (Berufsabschl./Hauptschulab.Kl. 10 oder mittl. Schulab. (BAB/HSA10-FOR, 4j. TZ))
	 */
	B10,

	/**
	 * Schulgliederung B11:
	 *   Anlage B (Berufsfachschule),
	 *   Typ 11 (Berufsabschl./HSA Kl. 10 oder mittl. Schulabschluss berufsbegleitend (BAB/HSA10-FOR, 3j. TZ))
	 */
	B11,

	/**
	 * Schulgliederung BT:
	 *   Schule für Kranke: Berufsbildend (Teilzeit)
	 */
	BT,

	/**
	 * Schulgliederung BV:
	 *   Schule für Kranke: Berufsbildend (Vollzeit)
	 */
	BV,

	/**
	 * Schulgliederung C01:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 01 (Berufsabschluss/Fachhochschulreife (ohne Berufspraktikum) BAB/FHR 3j; VZ BFS)
	 */
	C01,

	/**
	 * Schulgliederung C02:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 02 (Berufsabschluss f. Hochschulzugangsberechtigte (BAB 2j; VZ) BFS)
	 */
	C02,

	/**
	 * Schulgliederung C03:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 03 (Berufliche Kenntnisse/FHR (BK/FHR 2j; VZ) HBFS)
	 */
	C03,

	/**
	 * Schulgliederung C04 (ausgelaufen):
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 04 (Berufliche Kenntnisse/Sonderform für Abiturienten (BK 1j; VZ) HBFS)
	 */
	C04,

	/**
	 * Schulgliederung C05:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 05 (Fachoberschule Kl. 11 (BK/FHR 1j; TZ))
	 */
	C05,

	/**
	 * Schulgliederung C06:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 06 (Fachoberschule Kl. 12S (BK/FHR 1j; VZ))
	 */
	C06,

	/**
	 * Schulgliederung C07:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 07 (Fachoberschule Kl. 12B (BK/FHR 2j; TZ))
	 */
	C07,

	/**
	 * Schulgliederung C08:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 08 (Fachoberschule Kl. 12B (BK/FHR 1j; VZ))
	 */
	C08,

	/**
	 * Schulgliederung C09 (ausgelaufen):
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 09 (Berufspraktikum Erzieher/innen (Vollzeit) (BP/Erz 1j; VZ))
	 */
	C09,

	/**
	 * Schulgliederung C10 (ausgelaufen):
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 10 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	C10,

	/**
	 * Schulgliederung C11:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 11 (Fachoberschule Kl. 12B (BK/FHR 3j; TZ))
	 */
	C11,

	/**
	 * Schulgliederung C12:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 12 (Berufsabschluss/Fachhochschulreife (mit  Berufspraktikum) BAB/FHR 3,5j; VZ)
	 */
	C12,

	/**
	 * Schulgliederung C13:
	 *   Anlage C (Berufsfachschule und Fachoberschule),
	 *   Typ 13 (Berufsabschluss/Fachhochschulreife (gestuft) (BAB/FHR 3j; VZ))
	 */
	C13,

	/**
	 * Schulgliederung D01:
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 01 (Berufsabschluss/Allg. Hochschulreife (mit Berufspraktikum) (BAB/AHR 4j; VZ))
	 */
	D01,

	/**
	 * Schulgliederung D02:
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 02 (Berufl. Kenntnisse/Allg. Hochschulreife (BK/AHR 3j; VZ))
	 */
	D02,

	/**
	 * Schulgliederung D03 (ausgelaufen):
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 03 (Berufspraktikum (Vollzeit) (BP 1j; VZ))
	 */
	D03,

	/**
	 * Schulgliederung D04 (ausgelaufen):
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 04 (Berufspraktikum Erzieher/innen (Teilzeit) (BP/Erz 2j; TZ))
	 */
	D04,

	/**
	 * Schulgliederung D05:
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 05 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 1j; VZ) FOS13)
	 */
	D05,

	/**
	 * Schulgliederung D06:
	 *   Anlage D (Berufliches Gymnasium und Fachoberschule),
	 *   Typ 06 (AHR (gem. § 2 Abs. 3 Anlage D) (AHR 2j; TZ) FOS13)
	 */
	D06,

	/**
	 * Schulgliederung E01:
	 *   Anlage E (Fachschule),
	 *   Typ 01 (Fachschule Vollzeit (BW 2j; VZ))
	 */
	E01,

	/**
	 * Schulgliederung E02:
	 *   Anlage E (Fachschule),
	 *   Typ 02 (Fachschule Teilzeit (BW 4j; TZ))
	 */
	E02,

	/**
	 * Schulgliederung E03:
	 *   Anlage E (Fachschule),
	 *   Typ 03 (Fachschule (verkürzt) Vollzeit (BW 1j; VZ))
	 */
	E03,

	/**
	 * Schulgliederung E04:
	 *   Anlage E (Fachschule),
	 *   Typ 04 (Fachschule (verkürzt) Teilzeit (BW 2j; TZ))
	 */
	E04,

	/**
	 * Schulgliederung E05:
	 *   Anlage E (Fachschule),
	 *   Typ 05 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 3j; VZ))
	 */
	E05,

	/**
	 * Schulgliederung E06 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 06 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 1j; VZ))
	 */
	E06,

	/**
	 * Schulgliederung E07:
	 *   Anlage E (Fachschule),
	 *   Typ 07 (Fachschule für Sozialwesen (mit Berufspraktikum) (BAB 6j; TZ))
	 */
	E07,

	/**
	 * Schulgliederung E08 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 08 (Fachschule für Sozialpädagogik / Heilerziehungspflege (Praxis) (BAB/FP 2j; TZ))
	 */
	E08,

	/**
	 * Schulgliederung E09 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 09 (Fachschule (Sonderform) Vollzeit (BW 3j; VZ))
	 */
	E09,

	/**
	 * Schulgliederung E10 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 10 (Fachschule (Sonderform) Teilzeit (BW 6j; TZ))
	 */
	E10,

	/**
	 * Schulgliederung E11 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 11 (Berufspraktikum Erzieher/innen (FS/BP/Erz 1j; VZ))
	 */
	E11,

	/**
	 * Schulgliederung E12 (ausgelaufen):
	 *   Anlage E (Fachschule),
	 *   Typ 12 (Berufspraktikum Erzieher/innen (FS/BP/Erz 2j; TZ))
	 */
	E12,

	/**
	 * Schulgliederung E13:
	 *   Anlage E (Fachschule),
	 *   Typ 13 (Fachschule Teilzeit (BW 3j; TZ))
	 */
	E13,

	/**
	 * Schulgliederung ER:
	 *   kooperative Form: Erweiterungsebene
	 */
	ER,

	/**
	 * Schulgliederung EVB:
	 *   Evangelische Bekenntnisschule
	 */
	EVB,

	/**
	 * Schulgliederung G01:
	 *   Aufbaugymnasium
	 */
	G01,

	/**
	 * Schulgliederung G02:
	 *   Bildungsgang Abendgymnasium
	 */
	G02,

	/**
	 * Schulgliederung GGS (auslaufend):
	 *   Gemeinschaftsschule (auslaufend) integrierte Form
	 */
	GGS,

	/**
	 * Schulgliederung GGY (auslaufend):
	 *   Gemeinschaftsschule (auslaufend) Gymnasialbildungsgang
	 */
	GGY,

	/**
	 * Schulgliederung GMS:
	 *   Gemeinschaftsschule
	 */
	GMS,

	/**
	 * Schulgliederung GR:
	 *   kooperative Form: Grundebene
	 */
	GR,

	/**
	 * Schulgliederung GRH (auslaufend):
	 *   Gemeinschaftsschule auslaufend: teilintegrierte Form
	 */
	GRH,

	/**
	 * Schulgliederung GS:
	 *   integrierte Form (Binnendifferenzierung)
	 */
	GS,

	/**
	 * Schulgliederung GY:
	 *   Bildungsgang Gymnasium
	 */
	GY,

	/**
	 * Schulgliederung GY8:
	 *   Bildungsgang G8-Gymnasium
	 */
	GY8,

	/**
	 * Schulgliederung GY9:
	 *   Bildungsgang G9-Gymnasium
	 */
	GY9,

	/**
	 * Schulgliederung H:
	 *   Bildungsgang Hauptschule
	 */
	H,

	/**
	 * Schulgliederung H01:
	 *   Berufsgrundbildung (Jahrgang 07 bis 10)
	 */
	H01,

	/**
	 * Schulgliederung H02:
	 *   Berufsausbildung (Jahrgang 11 und 12)
	 */
	H02,

	/**
	 * Schulgliederung K02:
	 *   Bildungsgang Kolleg
	 */
	K02,

	/**
	 * Schulgliederung R:
	 *   Bildungsgang Realschule
	 */
	R,

	/**
	 * Schulgliederung R00:
	 *   Realschule
	 */
	R00,

	/**
	 * Schulgliederung R01:
	 *   Aufbaurealschule
	 */
	R01,

	/**
	 * Schulgliederung R02:
	 *   Bildungsgang Abendrealschule
	 */
	R02,

	/**
	 * Schulgliederung RH:
	 *   teilintegrierte Form
	 */
	RH,

	/**
	 * Schulgliederung RKB:
	 *   Katholische  Bekenntnisschule
	 */
	RKB,

	/**
	 * Schulgliederung SRH (auslaufend):
	 *   Sekundarschule, teilintegrierte Form (auslaufend)
	 */
	SRH,

	/**
	 * Schulgliederung SSI (auslaufend):
	 *   Sekundarschule, integrierte Form (auslaufend)
	 */
	SSI,

	/**
	 * Schulgliederung Y8:
	 *   Lehrplan G8
	 */
	Y8;


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung> manager) {
		CoreTypeDataManager.putManager(Schulgliederung.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SchulgliederungKatalogEintrag, Schulgliederung> data() {
		return CoreTypeDataManager.getManager(Schulgliederung.class);
	}


	/**
	 * Gibt die Standard-Gliederung der angegebenen Schulform zurück.
	 *
	 * @param sf   die Schulform
	 *
	 * @return die Schulgliederung, falls die Schulform gültig ist und ansonsten null
	 */
	public static Schulgliederung getDefault(final Schulform sf) {
		if (sf == null)
			return null;
		if ((sf == Schulform.GY) || (sf == Schulform.SK) || (sf == Schulform.GM) || (sf == Schulform.G)
				|| (sf == Schulform.S) || (sf == Schulform.PS) || (sf == Schulform.V) || (sf == Schulform.FW)
				|| (sf == Schulform.H) || (sf == Schulform.R) || (sf == Schulform.GE) || (sf == Schulform.SR)
				|| (sf == Schulform.SG))
			return Schulgliederung.DEFAULT;
		return null;
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
		return data().hatSchulform(schuljahr, sf, this);
	}


	/**
	 * Gibt zurück, ob es sich um einen 8-jährigen gymnasialen Bildungsgang
	 * handelt oder nicht.
	 *
	 * @return true, falls es sich um einen 8-jährigen gymnasialen Bildungsgang
	 *         handelt, sonst false
	 */
	public boolean istG8() {
		return (this == Schulgliederung.GY8) || (this == Schulgliederung.Y8);
	}


	/**
	 * Liefert alle zulässigen Schulgliederungen für die angegebene Schulform in dem angegebenen Schuljahr.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr zulässigen Schulgliederungen
	 */
	public static @NotNull List<Schulgliederung> getBySchuljahrAndSchulform(final int schuljahr, final @NotNull Schulform schulform) {
		return data().getListBySchuljahrAndSchulform(schuljahr, schulform);
	}


	/**
	 * Liefert die zulässige Schulgliederungen für die angegebene Schulform in dem angegebenen Schuljahr und dem angebenen Schlüssel oder
	 * null falls eine solche nicht existiert.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param schulform   die Schulform
	 * @param schluessel  der Schlüssel für die Schulgliederung
	 *
	 * @return die bei der Schulform in dem angegebenen Schuljahr dem Schlüssel zugehörige Schulgliederung oder null falls eine solche nicht existiert
	 */
	public static Schulgliederung getBySchuljahrAndSchulformAndSchluessel(final int schuljahr, final @NotNull Schulform schulform, final @NotNull String schluessel) {
		return data().getBySchuljahrAndSchulformAndSchluessel(schuljahr, schulform, schluessel);
	}

}
