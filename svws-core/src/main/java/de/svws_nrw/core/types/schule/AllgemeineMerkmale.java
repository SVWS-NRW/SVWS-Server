package de.svws_nrw.core.types.schule;

import java.util.Arrays;
import java.util.HashMap;

import de.svws_nrw.core.data.schule.AllgemeineMerkmaleKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die allgemeinen Merkmale bei Schulen bzw. Schülern.
 */
public enum AllgemeineMerkmale {

	/** Merkmal: Ganztagschule */
	GANZTAG(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(100000L, "GANZTAG", "Ganztagsschule", true, true, null, Arrays.asList(
			Schulform.H, 
			Schulform.V, 
			Schulform.R, 
			Schulform.GY, 
			Schulform.GE, 
			Schulform.KS, Schulform.S,
			Schulform.FW, Schulform.HI, Schulform.WF, 
			Schulform.G
		), null, null)
	}),


	/** Merkmal: Offene Ganztagschule */
	GANZTAG_OFFEN(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(200000L, "OFFGANZ", "Offene Ganztagsschule", true, true, null, Arrays.asList(
			Schulform.KS, Schulform.S, 
			Schulform.G, 
			Schulform.V, 
			Schulform.FW, Schulform.HI, Schulform.WF 
		), null, null)
	}),


	/** Merkmal: Übermittagsbetreuung */
	UEBERMITTAGSBETREUUNG(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(250000L, "ÜMI", "Übermittagsbetreuung", true, true, null, Arrays.asList(
			Schulform.G, 
			Schulform.KS, Schulform.S
		), 2012, 2017),
		new AllgemeineMerkmaleKatalogEintrag(250001L, "ÜMI", "Übermittagsbetreuung", true, true, null, Arrays.asList(
			Schulform.G, 
			Schulform.KS, Schulform.S, 
			Schulform.H, 
			Schulform.R
		), 2018, null)
	}),


	/** Merkmal: Selbständige Schule */
	SELBSTAENDIGE_SCHULE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(300000L, "SELBST", "Selbstständige Schule", true, false, null, Arrays.asList(
			Schulform.H, 
			Schulform.R, 
			Schulform.KS, Schulform.S,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.WB, 
			Schulform.BK, Schulform.SB, 
			Schulform.G, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Verbundschule */
	VERBUNDSCHULE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(350000L, "VERBUND", "Verbundschule", true, false, null, Arrays.asList(
			Schulform.GY, 
			Schulform.GE, 
			Schulform.R, 
			Schulform.H, 
			Schulform.G
		), null, null)
	}),


	/** Merkmal: BUS-Projekt */
	BUS(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(400000L, "BUS", "BUS-Projekt", true, true, null, Arrays.asList(
			Schulform.GE, 
			Schulform.KS, Schulform.S, 
			Schulform.H, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Schule von 8 bis 1 */
	VON_8_BIS_1(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(500000L, "8BIS1", "Schule von 8 bis 1", true, true, null, Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G
		), null, null)
	}),


	/** Merkmal: Schule von 8 bis 1 an anderer Schule */
	VON_8_BIS_1_ANDERE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(550000L, "8BIS1AS", "Schule von 8 bis 1 an anderer Schule", true, true, null, Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: 13+ */
	DREIZEHN_PLUS(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(600000L, "13+", "13+", true, true, null, Arrays.asList(
			Schulform.H, 
			Schulform.KS, Schulform.S,
			Schulform.GY, 
			Schulform.R, 
			Schulform.G, 
			Schulform.GE, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: 13+ an anderer Schule */
	DREIZEHN_PLUS_ANDERE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(650000L, "13+AS", "13+ an anderer Schule", true, true, null, Arrays.asList(
			Schulform.H, 
			Schulform.KS, Schulform.S,
			Schulform.GY, 
			Schulform.R, 
			Schulform.G, 
			Schulform.GE, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: JVA-Schüler */
	JVA(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(700000L, "JVA", "JVA-Schüler", true, true, null, Arrays.asList(
			Schulform.BK
		), null, null)
	}),


	/** Merkmal: Reformpädagogik */
	REFORMPAEDAGOGIK(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(800000L, "RefPäd", "Reformpädagogik", true, false, null, Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.H,
			Schulform.R,
			Schulform.GE, 
			Schulform.GY, 
			Schulform.G, 
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Begegnung mit Sprachen -Französisch- */
	BEGEGNUNG_MIT_SPRACHEN_F(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1410000L, "BegSprF", "Begegnung mit Sprachen -Französisch-", true, true, "F", Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G
		), null, null)
	}),


	/** Merkmal: Begegnung mit Sprachen -Italienisch- */
	BEGEGNUNG_MIT_SPRACHEN_I(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1420000L, "BegSprI", "Begegnung mit Sprachen -Italienisch-", true, true, "I", Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G,
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Begegnung mit Sprachen -Türkisch- */
	BEGEGNUNG_MIT_SPRACHEN_T(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1430000L, "BegSprT", "Begegnung mit Sprachen -Türkisch-", true, true, "T", Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G,
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Begegnung mit Sprachen -Niederländisch- */
	BEGEGNUNG_MIT_SPRACHEN_N(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1440000L, "BegSprN", "Begegnung mit Sprachen -Niederländisch-", true, true, "N", Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G,
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Begegnung mit Sprachen -Sonstige Fremdsprache- */
	BEGEGNUNG_MIT_SPRACHEN_SONSTIGE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1450000L, "BegSprSon", "Begegnung mit Sprachen -Sonstige Fremdsprache-", true, true, "X", Arrays.asList(
			Schulform.KS, Schulform.S,
			Schulform.G
		), null, null)
	}),


	/** Merkmal: Arbeitssprache in der gym. Oberstufe -Englisch- */
	ARBEITSSPRACHE_GOST_E(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1700000L, "ArbSprE", "Arbeitssprache in der gym. Oberstufe -Englisch-", true, true, null, Arrays.asList(
			Schulform.GE, 
			Schulform.GY
		), null, null)
	}),


	/** Merkmal: Arbeitssprache in der gym. Oberstufe -Französisch- */
	ARBEITSSPRACHE_GOST_F(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1710000L, "ArbSprF", "Arbeitssprache in der gym. Oberstufe -Französisch-", true, true, null, Arrays.asList(
			Schulform.GE, 
			Schulform.GY
		), null, null)
	}),


	/** Merkmal: Arbeitssprache in der gym. Oberstufe -Sonstige Sprache- */
	ARBEITSSPRACHE_GOST_SONSTIGE(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(1720000L, "ArbSprSon", "Arbeitssprache in der gym. Oberstufe -Sonstige Sprache-", true, true, null, Arrays.asList(
			Schulform.GE, 
			Schulform.GY,
			Schulform.V
		), null, null)
	}),


	/** Merkmal: Internat */
	INTERNAT(new AllgemeineMerkmaleKatalogEintrag[] {
		new AllgemeineMerkmaleKatalogEintrag(2000000L, "INTERNAT", "Internat", true, true, null, Arrays.asList(
			Schulform.H, 
			Schulform.V, 
			Schulform.R, 
			Schulform.GY, 
			Schulform.GE, 
			Schulform.KS, Schulform.S,
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.G,
			Schulform.BK, Schulform.SB, 
			Schulform.SR, 
			Schulform.SG
		), null, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	

	/** Der aktuellen Daten des allgemeinen Merkmals, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull AllgemeineMerkmaleKatalogEintrag daten;

	/** Die Historie mit den Einträgen des allgemeinen Merkmals */
	public final @NotNull AllgemeineMerkmaleKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten allgemeinen Merkmalen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, AllgemeineMerkmale> _mapByKuerzel = new HashMap<>();


	/**
	 * Erzeugt ein neues allgemeines Merkmal in der Aufzählung.
	 * 
	 * @param historie   die Historie der allgemeinen Merkmale, welche ein Array von 
	 *                   {@link AllgemeineMerkmaleKatalogEintrag} ist  
	 */
	private AllgemeineMerkmale(final @NotNull AllgemeineMerkmaleKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der allgemeinen Merkmale auf die 
	 * zugehörigen allgemeinen Merkmale zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen allgemeinen Merkmale
	 */
	private static @NotNull HashMap<@NotNull String, AllgemeineMerkmale> getMapByKuerzel() {
		if (_mapByKuerzel.size() == 0) {
			for (final AllgemeineMerkmale s : AllgemeineMerkmale.values()) {
				if (s.daten != null)
					_mapByKuerzel.put(s.daten.kuerzel, s);
			}
		}
		return _mapByKuerzel;
	}


	/**
	 * Gibt das allgemeine Merkmal für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel des allgemeinen Merkmals
	 * 
	 * @return das allgemeine Merkmal oder null, falls das Kürzel ungültig ist
	 */
	public static AllgemeineMerkmale getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
}
