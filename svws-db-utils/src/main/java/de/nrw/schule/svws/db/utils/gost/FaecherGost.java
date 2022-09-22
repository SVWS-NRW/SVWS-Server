package de.nrw.schule.svws.db.utils.gost;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.types.statkue.Fachgruppe;
import de.nrw.schule.svws.core.types.statkue.Jahrgaenge;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFaecher;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen 
 * zu Fächern der gymnasialen Oberstufe zur Verfügung.
 */
public class FaecherGost {

	
	/**
	 * Diese Methode erstellt ein {@link GostFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOFach}. Dabei werden Informationen aus der Liste der Fächer verwendet.
	 * 
	 * @param fach       das Datenbank-Objekt 
	 * @param faecher    eine Map mit Fach-Informationen 
	 * 
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOFach(DTOFach fach, Map<Long, DTOFach> faecher) {
		GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikFach.daten.kuerzelASD;
		eintrag.kuerzelAnzeige= fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungSekII;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterichtssprache != null) && (!"".equals(fach.Unterichtssprache)) && (!"D".equals(fach.Unterichtssprache))) ? fach.Unterichtssprache.substring(0, 1) : null;
		eintrag.istMoeglichAbiLK = fach.IstMoeglichAbiLK;
		eintrag.istMoeglichAbiGK = fach.IstMoeglichAbiGK;
		eintrag.istMoeglichEF1 = fach.IstMoeglichEF1;
		eintrag.istMoeglichEF2 = fach.IstMoeglichEF2;
		eintrag.istMoeglichQ11 = fach.IstMoeglichQ11;
		eintrag.istMoeglichQ12 = fach.IstMoeglichQ12;
		eintrag.istMoeglichQ21 = fach.IstMoeglichQ21;
		eintrag.istMoeglichQ22 = fach.IstMoeglichQ22;
		int defaultWochenstundenEF = (fach.StatistikFach.getFachgruppe() == Fachgruppe.FG13_VX ? 2 : 3);
		int defaultWochenstundenQ = (Jahrgaenge.JG_EF == fach.StatistikFach.getJahrgangAb() ? 4 : defaultWochenstundenEF);
		eintrag.wochenstundenEF1 = fach.WochenstundenEF1 == null ? defaultWochenstundenEF : fach.WochenstundenEF1;
		eintrag.wochenstundenEF2 = fach.WochenstundenEF2 == null ? defaultWochenstundenEF : fach.WochenstundenEF2;
		eintrag.wochenstundenQualifikationsphase = fach.WochenstundenQualifikationsphase == null ? defaultWochenstundenQ : fach.WochenstundenQualifikationsphase;
		eintrag.mussSchriftlichEF1 = fach.MussSchriftlichEF1;
		eintrag.mussSchriftlichEF2 = fach.MussSchriftlichEF2;			
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		if (fach.ProjektKursLeitfach1_ID == null) {
			eintrag.projektKursLeitfach1Kuerzel = null;
		} else {
			DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach1_ID);
			eintrag.projektKursLeitfach1Kuerzel = ((dtoFach == null) || (dtoFach.StatistikFach == null)) ? null : dtoFach.StatistikFach.daten.kuerzelASD;
		}
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		if (fach.ProjektKursLeitfach2_ID == null) {
			eintrag.projektKursLeitfach2Kuerzel = null;
		} else {
			DTOFach dtoFach = faecher.get(fach.ProjektKursLeitfach2_ID);
			eintrag.projektKursLeitfach2Kuerzel = ((dtoFach == null) || (dtoFach.StatistikFach == null)) ? null : dtoFach.StatistikFach.daten.kuerzelASD;
		}
		return eintrag;
	}
	

	/**
	 * Diese Methode erstellt ein {@link GostFach}-Objekt mit den Daten aus dem Datenbank-Objekt
	 * von Typ {@link DTOGostJahrgangFaecher}. Dabei werden Informationen aus der übergebenen Liste 
	 * der Fächer verwendet.
	 * 
	 * @param jf         das Datenbank-Objekt 
	 * @param faecher    eine Map mit Fach-Informationen 
	 * 
	 * @return das {@link GostFach}-Objekt
	 */
	public static GostFach mapFromDTOGostJahrgangFaecher(DTOGostJahrgangFaecher jf, Map<Long, DTOFach> faecher) {
		DTOFach fach = faecher.get(jf.Fach_ID);
		if (fach == null)
			return null;
		GostFach eintrag = new GostFach();
		eintrag.id = fach.ID;
		eintrag.kuerzel = fach.StatistikFach.daten.kuerzelASD;
		eintrag.kuerzelAnzeige = fach.Kuerzel;
		eintrag.bezeichnung = fach.Bezeichnung;
		eintrag.sortierung = fach.SortierungSekII;
		eintrag.istFremdsprache = fach.IstFremdsprache;
		eintrag.istFremdSpracheNeuEinsetzend = fach.IstMoeglichAlsNeueFremdspracheInSekII;
		eintrag.biliSprache = ((fach.Unterichtssprache != null) && (!"".equals(fach.Unterichtssprache)) && (!"D".equals(fach.Unterichtssprache))) ? fach.Unterichtssprache.substring(0, 1) : null;
		eintrag.istMoeglichAbiLK = jf.WaehlbarAbiLK;
		eintrag.istMoeglichAbiGK = jf.WaehlbarAbiGK;
		eintrag.istMoeglichEF1 = jf.WaehlbarEF1;
		eintrag.istMoeglichEF2 = jf.WaehlbarEF2;
		eintrag.istMoeglichQ11 = jf.WaehlbarQ11;
		eintrag.istMoeglichQ12 = jf.WaehlbarQ12;
		eintrag.istMoeglichQ21 = jf.WaehlbarQ21;
		eintrag.istMoeglichQ22 = jf.WaehlbarQ22;
		int defaultWochenstundenEF = (fach.StatistikFach.getFachgruppe() == Fachgruppe.FG13_VX ? 2 : 3);
		int defaultWochenstundenQ = (Jahrgaenge.JG_EF == fach.StatistikFach.getJahrgangAb() ? 4 : defaultWochenstundenEF);
		eintrag.wochenstundenEF1 = jf.WochenstundenEF1 == null ? defaultWochenstundenEF : jf.WochenstundenEF1;
		eintrag.wochenstundenEF2 = jf.WochenstundenEF2 == null ? defaultWochenstundenEF : jf.WochenstundenEF2;
		eintrag.wochenstundenQualifikationsphase = jf.WochenstundenQPhase == null ? defaultWochenstundenQ : jf.WochenstundenQPhase;
		eintrag.mussSchriftlichEF1 = "J".equals(jf.SchiftlichkeitEF1);
		eintrag.mussSchriftlichEF2 = "J".equals(jf.SchiftlichkeitEF2);			
		eintrag.projektKursLeitfach1ID = fach.ProjektKursLeitfach1_ID;
		eintrag.projektKursLeitfach1Kuerzel = fach.ProjektKursLeitfach1_ID == null ? null : faecher.get(fach.ProjektKursLeitfach1_ID).StatistikFach.daten.kuerzelASD;
		eintrag.projektKursLeitfach2ID = fach.ProjektKursLeitfach2_ID;
		eintrag.projektKursLeitfach2Kuerzel = fach.ProjektKursLeitfach2_ID == null ? null : faecher.get(fach.ProjektKursLeitfach2_ID).StatistikFach.daten.kuerzelASD;
		return eintrag;
	}
	
	
	/**
	 * Ermittelt die Liste aller Fächer der gymnasialen Oberstufe
	 * 
	 * @param conn          die Datenbank-Verbindung
	 * @param abiJahrgang   der Abiturjahrgang, für den die Liste erstellt werden soll
	 *  
	 * @return       die Liste aller Fächer der gymnasialen Oberstufe
	 */
	public static GostFaecherManager getFaecherListeGost(DBEntityManager conn, Integer abiJahrgang) {
		Schulform schulform = Schule.query(conn).getSchulform();
		if ((schulform.daten == null) || (!schulform.daten.hatGymOb))
			return new GostFaecherManager();
    	Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
    	if (faecher == null)
    		throw OperationError.NOT_FOUND.exception();
		if (abiJahrgang == null) {
	    	GostFaecherManager manager = new GostFaecherManager(faecher.values().stream()
	    		.filter(fach -> fach.IstOberstufenFach)
	    		.map(fach -> mapFromDTOFach(fach, faecher))
	    		.filter(fach -> (fach != null))
	    		.collect(Collectors.toList())
	    	);
	    	return manager;
		}
				
		List<DTOGostJahrgangFaecher> jahrgangfaecher = conn.queryNamed("DTOGostJahrgangFaecher.abi_jahrgang", abiJahrgang, DTOGostJahrgangFaecher.class);
    	if (jahrgangfaecher == null)
			return new GostFaecherManager();
    	GostFaecherManager manager = new GostFaecherManager(jahrgangfaecher.stream()
    			.map(jf -> mapFromDTOGostJahrgangFaecher(jf, faecher))
    			.filter(fach -> (fach != null)).collect(Collectors.toList())
    	);
    	return manager;
    }
	
}
