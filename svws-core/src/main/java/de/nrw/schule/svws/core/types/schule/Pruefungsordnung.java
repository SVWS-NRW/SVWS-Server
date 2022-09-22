package de.nrw.schule.svws.core.types.schule;

import java.util.HashMap;

import de.nrw.schule.svws.core.data.schule.PruefungsordnungKatalogEintrag;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die Ausbildungs- und/oder Prüfungsordnungen.
 */
public enum Pruefungsordnung {

	/** APO-BK: Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs */
	APO_BK(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(1000, "APO-BK", "APO-BK-99", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 1999, "26", "239-384", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1999&p_nr=26", 1999, 2002),
		new PruefungsordnungKatalogEintrag(1001, "APO-BK", "APO-BK-03", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2003, "32", "357-368", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=10290&ver=8&val=10290&sg=0&menu=0&vd_back=N", 2003, 2010),
		new PruefungsordnungKatalogEintrag(1002, "APO-BK", "APO-BK-11", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2011, "17", "361-376", "https://recht.nrw.de/lmi/owa/br_show_historie?p_id=20405", 2011, 2011),
		new PruefungsordnungKatalogEintrag(1003, "APO-BK", "APO-BK-13", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2012, "23", "421-438", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=13493&ver=8&val=13493&sg=0&menu=0&vd_back=N", 2012, 2014),
		new PruefungsordnungKatalogEintrag(1004, "APO-BK", "APO-BK-15", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Berufskollegs (Ausbildungs- und Prüfungsordnung Berufskolleg - APO-BK)", 2015, "2", "13-38", "https://bass.schul-welt.de/3129.htm", 2015, null)
	}),

	/** APO-GOSt: Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe */
	APO_GOST(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(2000, "APO-GOSt", null, "Verordnung über den Bildungsgang und die Abiturprüfung in der Oberstufe des Gymnasiums (Ausbildungs- und Prüfungsordnung gemäß §26b SchVG – APO – OStG)", 1979, "20", "248-259", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1979&p_nr=20", 1979, 1998),
		new PruefungsordnungKatalogEintrag(2001, "APO-GOSt", "APO-GOSt01", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 1998, "43", "593-608", "https://bass.schul-welt.de/9607.htm", 1999, 2011),
		new PruefungsordnungKatalogEintrag(2002, "APO-GOSt", "APO-GOSt02", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 1998, "43", "593-608", "https://bass.schul-welt.de/9607.htm", 1999, 2011),
		new PruefungsordnungKatalogEintrag(2003, "APO-GOSt", "APO-GOSt(C)10", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 2009, "8", "177-184", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=11319&ver=8&val=11319&sg=0&menu=0&vd_back=N", 2010, 2012),
		new PruefungsordnungKatalogEintrag(2004, "APO-GOSt", "APO-GOSt(B)10", "Verordnung über den Bildungsgang und die Abiturprüfung in der gymnasialen Oberstufe (APO-GOSt)", 2009, "26", "529-538", "https://bass.schul-welt.de/9607.htm", 2010, null)
	}),
	
	/** APO-WbK: Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs */
	APO_WBK(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(3000, "APO-WbK", "APO-WbK-00", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs (Ausbildungs- und Prüfungsordnung Weiterbildungskolleg - APO-WbK)", 2000, "19", "289-308", "https://recht.nrw.de/lmi/owa/br_show_historie?p_id=2661", 2000, 2009),
		new PruefungsordnungKatalogEintrag(3001, "APO-WbK", "APO-WBK10", "Verordnung über die Ausbildung und Prüfung in den Bildungsgängen des Weiterbildungskollegs (Ausbildungs- und Prüfungsordnung Weiterbildungskolleg - APO-WbK)", 2010, "8", "143-154", "https://bass.schul-welt.de/3693.htm", 2010, null)
	}),
		
	/** AO-GS: Verordnung über den Bildungsgang in der Grundschule */
	AO_GS(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(4000, "AO-GS", "AO-GS00", "Verordnung über den Bildungsgang in der Grundschule (Ausbildungsordnung gemäß §26b SchVG - AO-GS)", 1979, "34", "465-467", "https://recht.nrw.de/lmi/owa/br_gv_show_pdf?p_jahr=1979&p_nr=34", 1979, null),
		new PruefungsordnungKatalogEintrag(4001, "AO-GS", "AO-GS05", "Verordnung über den Bildungsgang in der Grundschule (Ausbildungsordnung Grundschule - AO-GS)", 2005, "16", "251-272", "https://bass.schul-welt.de/6181.htm", 2005, null)	
	}),
	
	/** AO-SI: Verordnung über die Ausbildung in der Sekundarstufe I */
	AO_SI(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(5000, "AO-SI", "AO-SI99", "Verordnung über die Ausbildung in der Sekundarstufe I (Ausbildungsordnung Sekundarstufe I - AO-S I)", 1998, "45", "631-648", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=2148&ver=8&val=2148&sg=0&menu=1&vd_back=N", 1999, 2004)
	}),
	
	/** APO-SI: Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I */
	APO_SI(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(6000, "APO-SI", "APO-SI05", "Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I (Ausbildungs- und Prüfungsordnung Sekundarstufe I – APO-S I)", 2005, "24", "535-566", "https://recht.nrw.de/lmi/owa/br_vbl_detail_text?anw_nr=6&vd_id=3779&vd_back=N546&sg=0&menu=1", 2005, 2012),
		new PruefungsordnungKatalogEintrag(6001, "APO-SI", "APO-SI05", "Verordnung über die Ausbildung und die Abschlussprüfungen in der Sekundarstufe I (Ausbildungs- und Prüfungsordnung Sekundarstufe I – APO-S I)", 2012, "27", "487-506", "https://bass.schul-welt.de/12691.htm", 2013, null)
	}),
		
	/** AOSF: Verordnung über die sonderpädagogische Förderung, den Hausunterricht und die Schule für Kranke */
	AOSF(new PruefungsordnungKatalogEintrag[] {
		new PruefungsordnungKatalogEintrag(8000, "AOSF", "AOSF-SI05", "Verordnung über die sonderpädagogische Förderung, den Hausunterricht und die Schule für Kranke (Ausbildungsordnung gemäß § 52 SchulG - AO-SF)", 2005, "24", "535-566", "https://bass.schul-welt.de/6225.htm", 2005, null)
	});


	/** Die Version dieses Core-Types, um beim Datenbank Update-Process die Version des Core-Types feststellen zu können. */
	public static long VERSION = 1;	
	
	/** Der aktuellen Daten der Verordnung, wenn keine Beschränkung der Gültigkeit vorliegen - sonst null */
	public final @NotNull PruefungsordnungKatalogEintrag daten;
	
	/** Die Historie mit den Einträgen der Verordnung */
	public final @NotNull PruefungsordnungKatalogEintrag@NotNull[] historie;	

	/** Eine Hashmap mit allen definierten Verordnungen, zugeordnet zu ihren Kürzeln */ 
	private static final @NotNull HashMap<@NotNull String, @NotNull Pruefungsordnung> _verordnungen = new HashMap<>();
	

	/**
	 * Erzeugt eine neue Verordnung in der Aufzählung.
	 * 
	 * @param historie   die Historie der Verordnung, welches ein Array von {@link PruefungsordnungKatalogEintrag} ist  
	 */
	private Pruefungsordnung(@NotNull PruefungsordnungKatalogEintrag@NotNull[] historie) {
		this.historie = historie;
		// TODO Prüfe korrekte Reihenfolge der Einträge und sortiere so, dass Eintrag 0 im Array der älteste Eintrag ist 
		this.daten = historie[historie.length - 1];
	}
	
	
	/**
	 * Gibt eine Map von den Kürzels der Prüfungsordnungen auf die zugehörigen Prüfungsordnungen
	 * zurück. Sollte diese noch nicht initialisiert sein, so wird sie initialisiert.
	 *    
	 * @return die Map von den Kürzeln der Prüfungsordnungen auf die zugehörigen Prüfungsordnungen
	 */
	private static @NotNull HashMap<@NotNull String, @NotNull Pruefungsordnung> getMapPruefungsordnungByKuerzel() {
		if (_verordnungen.size() == 0) {
			for (Pruefungsordnung s : Pruefungsordnung.values()) {
				if (s.daten != null)
					_verordnungen.put(s.daten.kuerzel, s);
			}
		}
		return _verordnungen;
	}


	/**
	 * Gibt die Prüfungsordnung für das angegebene Kürzel zurück.
	 * 
	 * @param kuerzel   das Kürzel der Prüfungsordnung
	 * 
	 * @return die Prüfungsordnung oder null, falls das Kürzel ungültig ist
	 */
	public static Pruefungsordnung getByKuerzel(String kuerzel) {
		return getMapPruefungsordnungByKuerzel().get(kuerzel);
	}

}
