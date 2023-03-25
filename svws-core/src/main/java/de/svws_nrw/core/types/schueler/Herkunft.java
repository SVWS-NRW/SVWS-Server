package de.svws_nrw.core.types.schueler;

import java.util.HashMap;

import de.svws_nrw.core.data.schule.HerkunftKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik Herkunftsmöglichkeiten von 
 * Schülern in Abhängigkeit von dessen Schulform.
 * Dieser Core-Type dient der Kompatibilität zu den Statkue-Tabellen und der
 * Handhabung in Schild. Für die Verwendung im SVWS-Server und -Client sollten
 * die Core-Types verwendet werden. Siehe auch {@link HerkunftSchulform},
 * {@link HerkunftBildungsgang}, {@link HerkunftBildungsgangsTyp}
 * und {@link HerkunftSonstige}.
 */
public enum Herkunft {
	
	// --- Herkünfte aus Schulen der Schulformen... --- 
	
	/** Schulform: Berufskolleg */
	BK(HerkunftSchulform.BK),
	
	/** Schulform: Freie Waldorfschule */
	FW(HerkunftSchulform.FW),
	
	/** Schulform: Grundschule */
	G(HerkunftSchulform.G),
	
	/** Schulform: Gesamtschule */
	GE(HerkunftSchulform.GE),
	
	/** Schulform: Gemeinschaftschule */
	GM(HerkunftSchulform.GM),
	
	/** Schulform: Gymnasium */
	GY(HerkunftSchulform.GY),
	
	/** Schulform: Hauptschule */
	H(HerkunftSchulform.H),
	
	/** Schulform: Schulversuch PRIMUS */
	PS(HerkunftSchulform.PS),
	
	/** Schulform: Realschule */
	R(HerkunftSchulform.R),
	
	/** Schulform: Förderschule oder Klinikschule */
	S(HerkunftSchulform.S),
	
	/** Schulform: Sekundarschule */
	SE(HerkunftSchulform.SK),
	
	/** Schulform: Weiterbildungskolleg */
	WB(HerkunftSchulform.WB),


	// --- Herkünfte am Berufskolleg aus Bildungsgängen... ---
	
	/** Bildungsgang: A01 */
	A01(HerkunftBildungsgang.A01),
	
	/** Bildungsgang: A02 */
	A02(HerkunftBildungsgang.A02),
	
	/** Bildungsgang: A03 */
	A03(HerkunftBildungsgang.A03),
	
	/** Bildungsgang: A04 */
	A04(HerkunftBildungsgang.A04),
	
	/** Bildungsgang: A10 */
	A10(HerkunftBildungsgang.A10),
	
	/** Bildungsgang: A11 */
	A11(HerkunftBildungsgang.A11),
	
	/** Bildungsgang: A12 */
	A12(HerkunftBildungsgang.A12),
	
	/** Bildungsgang: A13 */
	A13(HerkunftBildungsgang.A13),
	
	/** Bildungsgang: A14 */
	A14(HerkunftBildungsgang.A14),
	
	/** Bildungsgang: A15 */
	A15(HerkunftBildungsgang.A15),
	
	/** Bildungsgang: A16 */
	A16(HerkunftBildungsgang.A16),
	
	/** Bildungsgang: B01 */
	B01(HerkunftBildungsgang.B01),
	
	/** Bildungsgang: B02 */
	B02(HerkunftBildungsgang.B02),
	
	/** Bildungsgang: B04 */
	B04(HerkunftBildungsgang.B04),
	
	/** Bildungsgang: B05 */
	B05(HerkunftBildungsgang.B05),
	
	/** Bildungsgang: B06 */
	B06(HerkunftBildungsgang.B06),
	
	/** Bildungsgang: B07 */
	B07(HerkunftBildungsgang.B07),
	
	/** Bildungsgang: B08 */
	B08(HerkunftBildungsgang.B08),
	
	/** Bildungsgang: B09 */
	B09(HerkunftBildungsgang.B09),
	
	/** Bildungsgang: B10 */
	B10(HerkunftBildungsgang.B10),
	
	/** Bildungsgang: C01 */
	C01(HerkunftBildungsgang.C01),
	
	/** Bildungsgang: C02 */
	C02(HerkunftBildungsgang.C02),
	
	/** Bildungsgang: C03 */
	C03(HerkunftBildungsgang.C03),
	
	/** Bildungsgang: C05 */
	C05(HerkunftBildungsgang.C05),
	
	/** Bildungsgang: C06 */
	C06(HerkunftBildungsgang.C06),
	
	/** Bildungsgang: C07 */
	C07(HerkunftBildungsgang.C07),
	
	/** Bildungsgang: C08 */
	C08(HerkunftBildungsgang.C08),
	
	/** Bildungsgang: C11 */
	C11(HerkunftBildungsgang.C11),
	
	/** Bildungsgang: C12 */
	C12(HerkunftBildungsgang.C12),
	
	/** Bildungsgang: C13 */
	C13(HerkunftBildungsgang.C13),
	
	/** Bildungsgang: D01 */
	D01(HerkunftBildungsgang.D01),
	
	/** Bildungsgang: D02 */
	D02(HerkunftBildungsgang.D02),
	
	/** Bildungsgang: D05 */
	D05(HerkunftBildungsgang.D05),
	
	/** Bildungsgang: D06 */
	D06(HerkunftBildungsgang.D06),

	/** Bildungsgang: E01 */
	E01(HerkunftBildungsgang.E01),

	/** Bildungsgang: E02 */
	E02(HerkunftBildungsgang.E02),

	/** Bildungsgang: E03 */
	E03(HerkunftBildungsgang.E03),

	/** Bildungsgang: E04 */
	E04(HerkunftBildungsgang.E04),

	/** Bildungsgang: E05 */
	E05(HerkunftBildungsgang.E05),

	/** Bildungsgang: E07 */
	E07(HerkunftBildungsgang.E07),

	/** Bildungsgang: E13 */
	E13(HerkunftBildungsgang.E13),


	// --- Herkünfte am Weiterbildungskolleg aus Typen von Bildungsgängen des Berufskolleg oder Weiterbildungskolleg... ---

	/** Weiterbildungskolleg: Abendgymnasium */
	AG(HerkunftBildungsgangsTyp.AG),

	/** Weiterbildungskolleg: Abendrealschule */
	AR(HerkunftBildungsgangsTyp.AR),

	/** Weiterbildungskolleg: Kolleg */
	KL(HerkunftBildungsgangsTyp.KL),

	/** Berufskolleg: Berufsfachschule */
	BF(HerkunftBildungsgangsTyp.BF),

	/** Berufskolleg: Berufschule */
	BS(HerkunftBildungsgangsTyp.BS),

	/** Berufskolleg: Berufliches Gymnasium  */
	BY(HerkunftBildungsgangsTyp.BY),

	/** Berufskolleg: Fachoberschule  */
	FO(HerkunftBildungsgangsTyp.FO),

	/** Berufskolleg: Fachschule */
	FS(HerkunftBildungsgangsTyp.FS),


	// --- Sonstige Herkünfte ---
	
	/** Sonstige Herkunft: Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind */
	AS(HerkunftSonstige.AS),

	/** Sonstige Herkunft: Keine Schule bzw. kein Förderschulkindergarten (Einschulung) */
	ES(HerkunftSonstige.ES),

	/** Sonstige Herkunft: Hausfrüherziehung für Hör- bzw. Sehgeschädigte */
	FE(HerkunftSonstige.FE),

	/** Sonstige Herkunft: Hochschule, Universität */
	HU(HerkunftSonstige.HU),

	/** Sonstige Herkunft: Förderschulkindergarten (einschließlich frühkindliche Förderung) */
	SK(HerkunftSonstige.SK),

	/** Sonstige Herkunft: Herkunft noch unbekannt (nur Gliederung A12, A13) */
	UN(HerkunftSonstige.UN),

	/** Sonstige Herkunft: Wehr-, Zivil- oder Bundesfreiwilligendienst */
	WZ(HerkunftSonstige.WZ),

	/** Sonstige Herkunft: Berufstätigkeit (z. B. vor Besuch einer Fachschule) */
	XB(HerkunftSonstige.XB),

	/** Sonstige Herkunft: Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler */
	XS(HerkunftSonstige.XS);



	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Herkunft, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Herkunft */
	public final @NotNull HerkunftKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Herkünften, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, Herkunft> _kuerzel = new HashMap<>();
	
	
	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer sonstigen Herkunft.
	 * 
	 * @param h   die sonstige Herkunft
	 */
	private Herkunft(final @NotNull HerkunftSonstige h) {
		this.historie = new HerkunftKatalogEintrag[h.historie.length];
		for (int i = 0; i < h.historie.length; i++) {
			this.historie[i] = new HerkunftKatalogEintrag(
				h.historie[i].id + 1000000000L,
				h.historie[i].kuerzel,
				h.historie[i].schulformen,
				h.historie[i].beschreibung,
				h.historie[i].gueltigVon,
				h.historie[i].gueltigBis
			);
		}
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einem Bildungsgang eines Berufskollegs.
	 * 
	 * @param h   die Herkunft aus einem Bildungsgang
	 */
	private Herkunft(final @NotNull HerkunftBildungsgang h) {
		this.historie = new HerkunftKatalogEintrag[h.historie.length];
		for (int i = 0; i < h.historie.length; i++) {
			this.historie[i] = new HerkunftKatalogEintrag(
				h.historie[i].id + 2000000000L,
				h.historie[i].kuerzel,
				h.historie[i].schulformen,
				h.historie[i].beschreibung,
				h.historie[i].gueltigVon,
				h.historie[i].gueltigBis
			);
		}
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einem Typ eines Bildungsgangs eines Berufskollegs oder eines
	 * Weiterbildungskollegs.
	 * 
	 * @param h   die Herkunft aus einem Bildungsgangtyp
	 */
	private Herkunft(final @NotNull HerkunftBildungsgangsTyp h) {
		this.historie = new HerkunftKatalogEintrag[h.historie.length];
		for (int i = 0; i < h.historie.length; i++) {
			this.historie[i] = new HerkunftKatalogEintrag(
				h.historie[i].id + 3000000000L,
				h.historie[i].kuerzel,
				h.historie[i].schulformen,
				h.historie[i].beschreibung,
				h.historie[i].gueltigVon,
				h.historie[i].gueltigBis
			);
		}
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Erzeugt eine neue Herkunft in der Aufzählung anhand einer Herkunft
	 * aus einer Schulform.
	 * 
	 * @param h   die Herkunft aus einer Schulform
	 */
	private Herkunft(final @NotNull HerkunftSchulform h) {
		this.historie = new HerkunftKatalogEintrag[h.historie.length];
		for (int i = 0; i < h.historie.length; i++) {
			this.historie[i] = new HerkunftKatalogEintrag(
				h.historie[i].id + 4000000000L,
				h.historie[i].kuerzelStatistik,
				h.historie[i].schulformen,
				h.historie[i].beschreibung,
				h.historie[i].gueltigVon,
				h.historie[i].gueltigBis
			);
		}
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der Herkünfte auf die zugehörigen Herkünfte 
	 * zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen Herkünfte
	 */
	private static @NotNull HashMap<@NotNull String, Herkunft> getMapByKuerzel() {
		if (_kuerzel.size() == 0) {
			for (final Herkunft h : Herkunft.values()) {
				if (h.daten != null)
					_kuerzel.put(h.daten.kuerzel, h);
			}
		}
		return _kuerzel;
	}


	/**
	 * Gibt die Herkunft für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Herkunft
	 * 
	 * @return die Herkunft oder null, falls das Kürzel ungültig ist
	 */
	public static Herkunft getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
