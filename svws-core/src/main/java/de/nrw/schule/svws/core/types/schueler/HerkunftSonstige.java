package de.nrw.schule.svws.core.types.schueler;

import java.util.Arrays;
import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.HerkunftSonstigeKatalogEintrag;
import de.nrw.schule.svws.core.types.schule.Schulform;
import jakarta.validation.constraints.NotNull;


/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen sonstigen 
 * Herkunftsmöglichkeiten von Schülern in Abhängigkeit von dessen Schulform.
 */
public enum HerkunftSonstige {

	/** Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind */
	AS(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(1000, "AS", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.WB, 
			Schulform.FW, Schulform.HI, Schulform.WF, 
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V
		), "Ausländische Schüler, die seit den letzten amtlichen Schuldaten aus dem Ausland zugezogen sind", null, null)
	}),


	/** Keine Schule bzw. kein Förderschulkindergarten (Einschulung) */
	ES(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(2000, "ES", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF, 
			Schulform.G, 
			Schulform.PS, 
			Schulform.KS, Schulform.S, 
			Schulform.V
		), "Keine Schule bzw. kein Förderschulkindergarten (Einschulung)", null, null)
	}),


	/** Hausfrüherziehung für Hör- bzw. Sehgeschädigte */
	FE(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(3000, "FE", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF,
			Schulform.KS, Schulform.S 
		), "Hausfrüherziehung für Hör- bzw. Sehgeschädigte", null, null)
	}),


	/** Hochschule, Universität */
	HU(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(4000, "HU", Arrays.asList(
			Schulform.BK, Schulform.SB 
		), "Hochschule, Universität", null, null)
	}),


	/** Förderschulkindergarten (einschließlich frühkindliche Förderung) */
	SK(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(5000, "SK", Arrays.asList(
			Schulform.FW, Schulform.HI, Schulform.WF, 
			Schulform.G, 
			Schulform.PS, 
			Schulform.KS, Schulform.S, 
			Schulform.V
		), "Förderschulkindergarten (einschließlich frühkindliche Förderung)", null, null)
	}),


	/** Herkunft noch unbekannt (nur Gliederung A12, A13) */
	UN(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(6000, "UN", Arrays.asList(
			Schulform.BK, Schulform.SB 
		), "Herkunft noch unbekannt (nur Gliederung A12, A13)", null, null)
	}),


	/** Wehr-, Zivil- oder Bundesfreiwilligendienst */
	WZ(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(8000, "WZ", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.WB 
		), "Wehr-, Zivil- oder Bundesfreiwilligendienst", null, null)
	}),


	/** Berufstätigkeit (z. B. vor Besuch einer Fachschule) */
	XB(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(9000, "XB", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.WB 
		), "Berufstätigkeit (z. B. vor Besuch einer Fachschule)", null, null)
	}),


	/** Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler */
	XS(new HerkunftSonstigeKatalogEintrag[]{
		new HerkunftSonstigeKatalogEintrag(10000, "XS", Arrays.asList(
			Schulform.BK, Schulform.SB, 
			Schulform.WB, 
			Schulform.FW, Schulform.HI, Schulform.WF, 
			Schulform.G, 
			Schulform.GE, 
			Schulform.GM, 
			Schulform.GY, 
			Schulform.H, 
			Schulform.PS, 
			Schulform.R, 
			Schulform.KS, Schulform.S, 
			Schulform.SG, 
			Schulform.SK, 
			Schulform.SR, 
			Schulform.V
		), "Sonstige Schule bzw. keine Schule, auch seit den letzten amtlichen Schuldaten aus dem Ausland zugezogene deutsche Schüler", null, null)
	});

	
	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der sonstigen Herkunft, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull HerkunftSonstigeKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der sonstigen Herkunft */
	public final @NotNull HerkunftSonstigeKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten sonstigen Herkünfte, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, HerkunftSonstige> _ebenen = new HashMap<>();
	
	
	/**
	 * Erzeugt eine neue sonstige Herkunft in der Aufzählung.
	 * 
	 * @param historie   die Historie der sonstigen Herkunft, welches ein Array von 
	 *                   {@link HerkunftSonstigeKatalogEintrag} ist  
	 */
	private HerkunftSonstige(final @NotNull HerkunftSonstigeKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}

	
	/**
	 * Gibt eine Map von den Kürzeln der sonstigen Herkünfte auf die 
	 * zugehörigen sonstigen Herkünfte zurück. 
	 * Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln auf die zugehörigen sonstigen Herkünfte
	 */
	private static @NotNull HashMap<@NotNull String, HerkunftSonstige> getMapByKuerzel() {
		if (_ebenen.size() == 0) {
			for (final HerkunftSonstige s : HerkunftSonstige.values()) {
				if (s.daten != null)
					_ebenen.put(s.daten.kuerzel, s);
			}
		}
		return _ebenen;
	}


	/**
	 * Gibt die sonstige Herkunft für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der sonstigen Herkunft
	 * 
	 * @return die sonstige Herkunft oder null, falls das Kürzel ungültig ist
	 */
	public static HerkunftSonstige getByKuerzel(final String kuerzel) {
		return getMapByKuerzel().get(kuerzel);
	}
	
}
