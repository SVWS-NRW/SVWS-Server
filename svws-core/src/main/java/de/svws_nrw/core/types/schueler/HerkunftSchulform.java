package de.svws_nrw.core.types.schueler;

import java.util.Arrays;
import java.util.HashMap;

import de.svws_nrw.core.data.schule.HerkunftSchulformKatalogEintrag;
import de.svws_nrw.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen
 * Herkunftsschulformen von Schülern in Abhängigkeit von dessen
 * aktueller Schulform.
 */
public enum HerkunftSchulform {

	/** Berufskolleg */
	BK(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(1000, "BK", "BK", Arrays.asList(
					Schulform.FW, Schulform.HI, Schulform.WF, Schulform.GE, Schulform.GY, Schulform.SG
			), "Berufskolleg", null, null)
	}),


	/** Freie Waldorfschule */
	FW(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(2000, "FW", "FW", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Freie Waldorfschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(2001, "FW", "FW", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Freie Waldorfschule", 2023, null)
	}),


	/** Grundschule (auch Primarstufe der Volksschule) */
	G(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(3000, "G", "G", Arrays.asList(
					Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R,
					Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V
			), "Grundschule (auch Primarstufe der Volksschule)", null, 2022),
			new HerkunftSchulformKatalogEintrag(3001, "G", "G", Arrays.asList(
					Schulform.FW, Schulform.HI, Schulform.WF, Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R,
					Schulform.KS, Schulform.S, Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V
			), "Grundschule (auch Primarstufe der Volksschule)", 2023, null)
	}),


	/** Gesamtschule */
	GE(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(4000, "GE", "GE", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gesamtschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(4001, "GE", "GE", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gesamtschule", 2023, null)
	}),


	/** Gemeinschaftsschule */
	GM(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(5000, "GM", "GM", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gemeinschaftsschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(5001, "GM", "GM", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gemeinschaftsschule", 2023, null)
	}),


	/** Gymnasium (auch Aufbaugymnasium) */
	GY(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(6000, "GY", "GY", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gymnasium (auch Aufbaugymnasium)", null, 2022),
			new HerkunftSchulformKatalogEintrag(6001, "GY", "GY", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Gymnasium (auch Aufbaugymnasium)", 2023, null)
	}),


	/** Hauptschule (auch Sekundarstufe I der Volksschule) */
	H(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(7000, "H", "H", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Hauptschule (auch Sekundarstufe I der Volksschule)", null, 2022),
			new HerkunftSchulformKatalogEintrag(7001, "H", "H", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Hauptschule (auch Sekundarstufe I der Volksschule)", 2023, null)
	}),


	/** Hiberniaschule */
	HI(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(8000, "HI", "FW", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Hiberniaschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(8001, "HI", "FW", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Hiberniaschule", 2023, null)
	}),


	/** Schulversuch PRIMUS */
	PS(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(9000, "PS", "PS", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Schulversuch PRIMUS", null, 2022),
			new HerkunftSchulformKatalogEintrag(9001, "PS", "PS", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Schulversuch PRIMUS", 2023, null)
	}),


	/** Realschule (auch Aufbaurealschule) */
	R(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(10000, "R", "R", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Realschule (auch Aufbaurealschule)", null, 2022),
			new HerkunftSchulformKatalogEintrag(10001, "R", "R", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Realschule (auch Aufbaurealschule)", 2023, null)
	}),


	/** Förderschule oder Klinikschule */
	S(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(11000, "S", "S", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Förderschule oder Klinikschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(11001, "S", "S", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.G, Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Förderschule oder Klinikschule", 2023, null)
	}),


	/** Sekundarschule */
	SK(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(15000, "SK", "SE", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GM, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Sekundarschule", null, 2022),
			new HerkunftSchulformKatalogEintrag(15001, "SK", "SE", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.H, Schulform.PS, Schulform.R, Schulform.KS, Schulform.S,
					Schulform.SG, Schulform.SK, Schulform.SR, Schulform.V, Schulform.WB
			), "Sekundarschule", 2023, null)
	}),


	/** Weiterbildungskolleg */
	WB(new HerkunftSchulformKatalogEintrag[] {
			new HerkunftSchulformKatalogEintrag(18000, "WB", "WB", Arrays.asList(
					Schulform.BK, Schulform.SB, Schulform.FW, Schulform.HI, Schulform.WF,
					Schulform.GE, Schulform.GY, Schulform.PS, Schulform.SG
			), "Weiterbildungskolleg", null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static final long VERSION = 2;

	/** Der aktuellen Daten der Herkunftsschulform, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftSchulformKatalogEintrag daten;

	/** Die Historie mit den Einträgen der Herkunftsschulform */
	public final @NotNull HerkunftSchulformKatalogEintrag @NotNull [] historie;

	/** Eine Hashmap mit allen definierten Herkunftsschulformen, zugeordnet zu ihren Kürzeln */
	private static final @NotNull HashMap<@NotNull String, HerkunftSchulform> _ebenen = new HashMap<>();


	/**
	 * Erzeugt eine neue Herkunftsschulform in der Aufzählung.
	 *
	 * @param historie   die Historie der Herkunftsschulform, welche ein Array von
	 *                   {@link HerkunftSchulformKatalogEintrag} ist
	 */
	HerkunftSchulform(final @NotNull HerkunftSchulformKatalogEintrag @NotNull [] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist
		this.daten = historie[historie.length - 1];
	}


	/**
	 * Gibt eine Map von den Kürzeln der Herkunftsschulformen auf die
	 * zugehörigen Herkunftsschulformen zurück.
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *
	 * @return die Map von den Kürzeln auf die zugehörigen Herkunftsschulformen
	 */
	private static @NotNull HashMap<@NotNull String, HerkunftSchulform> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final HerkunftSchulform s : HerkunftSchulform.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt die Herkunftsschulform für das angegebene Kürzel zurück.
	 *
	 * @param kuerzel   das Kürzel der Herkunftsschulform
	 *
	 * @return die Herkunftsschulform oder null, falls das Kürzel ungültig ist
	 */
	public static HerkunftSchulform getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}

}
