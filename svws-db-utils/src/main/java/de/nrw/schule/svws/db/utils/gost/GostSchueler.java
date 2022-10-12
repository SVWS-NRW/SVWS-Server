package de.nrw.schule.svws.db.utils.gost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.SprachendatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostFachManager;
import de.nrw.schule.svws.core.data.Sprachendaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostLeistungen;
import de.nrw.schule.svws.core.data.gost.GostLeistungenFachbelegung;
import de.nrw.schule.svws.core.data.gost.GostLeistungenFachwahl;
import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.utils.gost.GostAbiturjahrUtils;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.Schueler;
import de.nrw.schule.svws.db.utils.data.Schule;



/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen 
 * zu Schülern der gymnasialen Oberstufe zur Verfügung.
 */
public class GostSchueler {

	/**
	 * Bestimmt für den übergegebenen Schüler das zugehörige Abiturjahr, sofern sich der Schüler 
	 * bereits in der gymnasialen Oberstufe befindet.
	 *  
	 * @param schule          die Schule des Schülers
	 * @param lernabschnitt   der aktuelle Lernabschnitt des Schülers
	 * @param schuljahr       das aktuelle Schuljahr, in welchem sich der Schüler befindet
	 * 
	 * @return das voraussichtliche Jahr des Abiturs 
	 */
	public static Integer getAbiturjahr(Schule schule, DTOSchuelerLernabschnittsdaten lernabschnitt, int schuljahr) {
		if ((lernabschnitt == null) || (lernabschnitt.Schuljahresabschnitts_ID == null))
			return null;
		return GostAbiturjahrUtils.getGostAbiturjahr(schule.getSchulform(), lernabschnitt.Schulgliederung, schuljahr, lernabschnitt.ASDJahrgang);
	}
	
	
	/**
	 * Ermittelt die Leistungsdaten der gymnasialen Oberstufe für den Schüler mit der
	 * angegebenen ID aus der Datenbank.
	 * 
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 * 
	 * @return die Leistungsdaten der gymnasialen Oberstufe für den Schüler mit der
	 *         angegebenen ID
	 */
	public static GostLeistungen getLeistungsdaten(DBEntityManager conn, long id) {
		Schule schule = Schule.queryCached(conn);
    	
		DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw OperationError.NOT_FOUND.exception();
		
		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
		if (abschnittSchueler == null)
			throw OperationError.NOT_FOUND.exception();

		Sprachendaten sprachendaten = Schueler.getSchuelerSprachendaten(conn, id);

		// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
		List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class)
				.stream()
				.sorted((l1, l2) -> {
					DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
					DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
					return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
				})
				.collect(Collectors.toList());

		// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
		DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
		    	
		Integer abiturjahr = GostSchueler.getAbiturjahr(schule, aktLernabschnitt, abschnittSchueler.Jahr);
    	GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, abiturjahr);
		
		// Ermittle nun die Leistungsdaten aus den Lernabschnitten
		GostLeistungen daten = new GostLeistungen();
		daten.id = schueler.ID;
		daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
		daten.aktuellerJahrgang = aktLernabschnitt.ASDJahrgang;
		daten.sprachendaten = sprachendaten;
		String biliZweig = aktLernabschnitt.BilingualerZweig;
		if ((biliZweig != null) && (!"".equals(biliZweig)))
			daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
		// eine HashMap zur temporären Speicherung der Fächer -> muss später noch sortiert werden
		HashMap<String, GostLeistungenFachwahl> faecher = new HashMap<>(); 
		for (DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
			DTOSchuljahresabschnitte abschnittLeistungsdaten = schuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
			if (abschnittLeistungsdaten == null)
				continue;
			
			GostHalbjahr halbjahr = schule.istImQuartalsmodus()  
					? GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, (abschnittLeistungsdaten.Abschnitt + 1) / 2)
					: GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnittLeistungsdaten.Abschnitt);
			if ((halbjahr != null) && (lernabschnitt.SemesterWertung))
				daten.bewertetesHalbjahr[halbjahr.id] = true;
			
			List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
			for (DTOSchuelerLeistungsdaten leistung : leistungen) {				
				// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
				GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg); 
				if (kursart == null)
					continue;
				// Prüfe, ob das Fach ein Fach der Oberstufe ist
				GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
				if (gostFach == null)
					continue;
				// Füge die Fächer aus den Leistungsdaten zunächst in die HashMap ein...
				GostLeistungenFachwahl fach = faecher.get(gostFach.kuerzelAnzeige);
				if (fach == null) {
					fach = new GostLeistungenFachwahl();
					fach.fach = gostFach;
					faecher.put(gostFach.kuerzelAnzeige, fach);
				}
				// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
				String fremdsprache = GostFachManager.getFremdsprache(gostFach); 
				if (fremdsprache != null)
					fach.istFSNeu = (!SprachendatenManager.istFortfuehrbareSpracheInGOSt(sprachendaten, fremdsprache));
				
				GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
				fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;
				
				// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
				GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
				belegung.id = leistung.ID;
				belegung.schuljahr = abschnittLeistungsdaten.Jahr;
				belegung.halbjahrKuerzel = (halbjahr == null) ? null : halbjahr.kuerzel;
				belegung.abschnitt = abschnittLeistungsdaten.Abschnitt;
				belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
				belegung.jahrgang = lernabschnitt.ASDJahrgang;
				belegung.lehrer = leistung.Fachlehrer_ID;
				belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
				belegung.kursartKuerzel = kursart.kuerzel;
				belegung.istSchriftlich = (kursart == GostKursart.LK) 
						|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart)) 
								|| ("AB3".equals(leistung.Kursart))
								|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
				belegung.bilingualeSprache = gostFach.biliSprache;
				belegung.wochenstunden = leistung.Wochenstunden;
				belegung.fehlstundenGesamt = leistung.FehlStd == null ? 0 : leistung.FehlStd;
				belegung.fehlstundenUnentschuldigt = leistung.uFehlStd == null ? 0 : leistung.uFehlStd;
				fach.belegungen.add(belegung);
				
				// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer 
				if (kursart == GostKursart.PJK) {
					daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
					daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
					if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
						daten.projektkursThema = leistung.Lernentw;
				}
			}
		}
		// Sortiere Fächer anhand der SII-Sortierung der Fächer
		faecher.values().stream()
			.sorted((a,b) -> {return Integer.compare(a.fach.sortierung, b.fach.sortierung);})
			.forEach(f -> daten.faecher.add(f));
		return daten;
	}

	

	/**
	 * Ermittelt die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 * angegebenen IDs aus der Datenbank.
	 * 
	 * @param conn   die Datenbank-Verbindung
	 * @param ids    die IDs der Schüler
	 * 
	 * @return die Leistungsdaten der gymnasialen Oberstufe für die Schüler mit den
	 *         angegebenen IDs
	 */
	public static Map<Long, GostLeistungen> getLeistungsdaten(DBEntityManager conn, List<Long> ids) {
		Schule schule = Schule.queryCached(conn);
		// TODO Ermittle die Abi-Jahrgangsspezifische Fächerliste ! 
    	GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, null);

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		
    	// TODO optimize DB-Access by using db queries with IN (...)
    	HashMap<Long, GostLeistungen> result = new HashMap<>();
    	for (Long id : ids) {
    		if (id == null)
    			throw OperationError.BAD_REQUEST.exception();
    		
			DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
			if (schueler == null)
				throw OperationError.NOT_FOUND.exception();

			DTOSchuljahresabschnitte abschnittSchueler = schuljahresabschnitte.get(schueler.Schuljahresabschnitts_ID);
			if (abschnittSchueler == null)
				throw OperationError.NOT_FOUND.exception();
			
			Sprachendaten sprachendaten = Schueler.getSchuelerSprachendaten(conn, id);

			// Bestimme alle Lernabschnitte der Oberstufe des Schülers, sortiert nach dem Schuljahr und dem Abschnitt
			List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", id, DTOSchuelerLernabschnittsdaten.class)
					.stream()
					.sorted((l1, l2) -> {
						DTOSchuljahresabschnitte a1 = schuljahresabschnitte.get(l1.Schuljahresabschnitts_ID);
						DTOSchuljahresabschnitte a2 = schuljahresabschnitte.get(l2.Schuljahresabschnitts_ID);
						return (a1.Jahr != a2.Jahr) ? Integer.compare(a1.Jahr, a2.Jahr) : Integer.compare(a1.Abschnitt, a2.Abschnitt);
					})
					.collect(Collectors.toList());
	    	
			// Bestimme den neuesten Lernabschnitt des Schülers. Aus diesem kann das voraussichliche Abiturjahr ermittelt werden.
			DTOSchuelerLernabschnittsdaten aktLernabschnitt = lernabschnitte.get(lernabschnitte.size() - 1);
			
			// Ermittle nun die Leistungsdaten aus den Lernabschnitten
			GostLeistungen daten = new GostLeistungen();
			daten.id = schueler.ID;
			daten.aktuellesSchuljahr = abschnittSchueler.Jahr;
			daten.aktuellerJahrgang = aktLernabschnitt.ASDJahrgang;
			daten.sprachendaten = sprachendaten;
			String biliZweig = aktLernabschnitt.BilingualerZweig;
			if ((biliZweig != null) && (!"".equals(biliZweig)))
				daten.bilingualeSprache = biliZweig.toUpperCase().substring(0, 1);
			// eine HashMap zur temporären Speicherung der Fächer -> muss später noch sortiert werden
			HashMap<String, GostLeistungenFachwahl> faecher = new HashMap<>(); 
			for (DTOSchuelerLernabschnittsdaten lernabschnitt : lernabschnitte) {
				DTOSchuljahresabschnitte abschnittLeistungsdaten = schuljahresabschnitte.get(lernabschnitt.Schuljahresabschnitts_ID);
				if (abschnittLeistungsdaten == null)
					continue;
				if (!("EF".equals(lernabschnitt.ASDJahrgang) || "Q1".equals(lernabschnitt.ASDJahrgang) || "Q2".equals(lernabschnitt.ASDJahrgang)))
					continue;
				GostHalbjahr halbjahr = schule.istImQuartalsmodus()  
						? GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, (abschnittLeistungsdaten.Abschnitt + 1) / 2)
						: GostHalbjahr.fromJahrgangUndHalbjahr(lernabschnitt.ASDJahrgang, abschnittLeistungsdaten.Abschnitt);
				if (lernabschnitt.SemesterWertung)
					daten.bewertetesHalbjahr[halbjahr.id] = true;  
				
				List<DTOSchuelerLeistungsdaten> leistungen = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
				for (DTOSchuelerLeistungsdaten leistung : leistungen) {				
					// Prüfe, ob die Kursart eine Kursart der Oberstufe ist.
					GostKursart kursart = GostKursart.fromKuerzel(leistung.KursartAllg); 
					if (kursart == null)
						continue; 
					GostFach gostFach = gostFaecher.get(leistung.Fach_ID);
					// Füge die Fächer aus den Leistungsdaten zunächst in die HashMap ein...
					GostLeistungenFachwahl fach = faecher.get(gostFach.kuerzelAnzeige);
					if (fach == null) {
						fach = new GostLeistungenFachwahl();
						fach.fach = gostFach;
						faecher.put(gostFach.kuerzelAnzeige, fach);
					}
					// Prüfe ggf., ob eine Sprache fortgeführt wurde oder nicht
					String fremdsprache = GostFachManager.getFremdsprache(gostFach); 
					if (fremdsprache != null)
						fach.istFSNeu = (!SprachendatenManager.istFortfuehrbareSpracheInGOSt(sprachendaten, fremdsprache));
					
					GostAbiturFach tmpAbiFach = GostAbiturFach.fromIDString(leistung.AbiFach);
					fach.abiturfach = (tmpAbiFach == null) ? null : tmpAbiFach.id;
					
					// Füge eine Belegung der Kurse für die einzelnen Fächer in dem Halbjahr ein
					GostLeistungenFachbelegung belegung = new GostLeistungenFachbelegung();
					belegung.id = leistung.ID;
					belegung.schuljahr = abschnittLeistungsdaten.Jahr;
					belegung.halbjahrKuerzel = (halbjahr == null) ? null : halbjahr.kuerzel;
					belegung.abschnitt = abschnittLeistungsdaten.Abschnitt;
					belegung.abschnittGewertet = lernabschnitt.SemesterWertung;
					belegung.jahrgang = lernabschnitt.ASDJahrgang;
					belegung.lehrer = leistung.Fachlehrer_ID;
					belegung.notenKuerzel = leistung.NotenKrz.kuerzel;
					belegung.kursartKuerzel = kursart.kuerzel;
					belegung.istSchriftlich = (kursart == GostKursart.LK) 
							|| ((kursart == GostKursart.GK) && (("GKS".equals(leistung.Kursart)) 
									|| ("AB3".equals(leistung.Kursart))
									|| ("AB4".equals(leistung.Kursart) && (halbjahr != GostHalbjahr.Q22))));
					belegung.bilingualeSprache = gostFach.biliSprache;
					belegung.wochenstunden = leistung.Wochenstunden;
					belegung.fehlstundenGesamt = leistung.FehlStd;
					belegung.fehlstundenUnentschuldigt = leistung.uFehlStd;
					fach.belegungen.add(belegung);
					
					// Ermittle ggf. das Projektkursthema und die zughörigen Leitfächer 
					if (kursart == GostKursart.PJK) {
						daten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
						daten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
						if ((leistung.Lernentw != null) && (!"".equals(leistung.Lernentw)))
							daten.projektkursThema = leistung.Lernentw;
					}
				}
			}
			// Sortiere Fächer anhand der SII-Sortierung der Fächer
			faecher.values().stream()
				.sorted((a,b) -> {return Integer.compare(a.fach.sortierung, b.fach.sortierung);})
				.forEach(f -> daten.faecher.add(f));
			result.put(id, daten);
    	}
    	return result;
	}
	

}
