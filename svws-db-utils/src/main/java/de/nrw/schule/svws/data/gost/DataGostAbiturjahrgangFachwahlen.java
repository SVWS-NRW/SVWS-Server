package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.data.gost.GostStatistikFachwahl;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.utils.gost.GostFachwahlManager;
import de.nrw.schule.svws.core.utils.gost.GostStatistikFachwahlManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostStatistikFachwahl}.
 */
public class DataGostAbiturjahrgangFachwahlen extends DataManager<Long> {

	private Integer abijahr;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostStatistikFachwahl}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr   der Abi-Jahrgang, für welchen die Fachwahlen ermittelt werden sollen
	 */
	public DataGostAbiturjahrgangFachwahlen(DBEntityManager conn, Integer abijahr) {
		super(conn);
		if (abijahr == null)
			throw new NullPointerException();
		this.abijahr = abijahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<GostStatistikFachwahl> daten = this.getFachwahlen();
		if (daten == null)
			return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		List<GostStatistikFachwahl> alle = this.getFachwahlen();
		if (alle == null)
			return OperationError.NOT_FOUND.getResponse();
		for (GostStatistikFachwahl wahl : alle) {
			if (wahl.id == id)
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(wahl).build();
		}
		return OperationError.NOT_FOUND.getResponse();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
	 * 
	 * @return die Statistik zu den Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public List<GostStatistikFachwahl> getFachwahlen() {
		GostUtils.pruefeSchuleMitGOSt(conn);
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		List<DTOViewGostSchuelerAbiturjahrgang> schueler = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if (schueler == null)
			return null;
		if (schueler.size() == 0)
			return new Vector<>();
		List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).collect(Collectors.toList());
    	List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
		if (fachbelegungen == null)
			return null;
		// Lese die Fachliste aus der DB
		Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
			return null;
    	// Ermittle die Fachwahlmatrix für alle angegebenen Schüler-IDs
		HashMap<Long, GostStatistikFachwahl> fachwahlen = new HashMap<>();
	    for (DTOGostSchuelerFachbelegungen fachbelegung: fachbelegungen) {
	        if ((fachbelegung.EF1_Kursart == null) && (fachbelegung.EF2_Kursart == null) && (fachbelegung.Q11_Kursart == null) &&
	            (fachbelegung.Q12_Kursart == null) && (fachbelegung.Q21_Kursart == null) && (fachbelegung.Q22_Kursart == null))
	            continue;
	        GostStatistikFachwahl fachwahl = fachwahlen.get(fachbelegung.Fach_ID);
	        if (fachwahl == null) {
	            DTOFach fach = faecher.get(fachbelegung.Fach_ID);
	            fachwahl = new GostStatistikFachwahl();
	            fachwahl.abiturjahr = abijahr;
	            fachwahl.id = fach.ID;
	            fachwahl.kuerzel = fach.Kuerzel;
	            fachwahl.bezeichnung = fach.Bezeichnung;
	            fachwahl.kuerzelStatistik = fach.StatistikFach.daten.kuerzelASD;
	            fachwahlen.put(fachwahl.id, fachwahl);
	        }
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.EF1, fachbelegung.EF1_Kursart);
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.EF2, fachbelegung.EF2_Kursart);
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.Q11, fachbelegung.Q11_Kursart);
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.Q12, fachbelegung.Q12_Kursart);
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.Q21, fachbelegung.Q21_Kursart);
	        GostStatistikFachwahlManager.setFachwahlHalbjahr(fachwahl, GostHalbjahr.Q22, fachbelegung.Q22_Kursart);
	    }
		return fachwahlen.values().stream().sorted((a,b) -> { 
    		return Integer.compare(faecher.get(a.id).SortierungSekII, faecher.get(b.id).SortierungSekII); 
    	}).collect(Collectors.toList());
	}


    /**
     * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
     * 
     * @param halbjahr_id   die ID des Halbjahres der gymnasialen Oberstufe, für welches die
     *                      Fachwahlen bestimmt werden sollen 
     * 
     * @return eine HTTP-Response, bei einem Erfolg: Die Fachwahlen des Abiturjahrgangs dieses Objektes
     */
	public Response getSchuelerFachwahlenResponse(int halbjahr_id) {
        List<GostFachwahl> daten = this.getSchuelerFachwahlen(GostHalbjahr.fromID(halbjahr_id));
        if (daten.size() == 0)
            return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes und gibt einen
	 * Fachwahl-Manager dafür zurück
	 * 
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe, für welches die
	 *                   Fachwahlen bestimmt werden sollen 
	 * 
	 * @return der Fachwahl-Manager für die Fachwahlen dieses Abiturjahrgangs 
	 */
	public GostFachwahlManager getFachwahlManager(GostHalbjahr halbjahr) {
		return new GostFachwahlManager(this.getSchuelerFachwahlen(halbjahr));
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
	 * 
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe, für welches die
	 *                   Fachwahlen bestimmt werden sollen 
	 * 
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public List<GostFachwahl> getSchuelerFachwahlen(GostHalbjahr halbjahr) {
	    if (halbjahr == null)
	        return Collections.emptyList();
		GostUtils.pruefeSchuleMitGOSt(conn);
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.size() == 0))
			return Collections.emptyList();
		List<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toList());
    	List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
		if ((fachbelegungen == null) || (fachbelegungen.size() == 0))
			return Collections.emptyList();
		// Bestimme die Schülernamen
		List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.size() == 0))
			return Collections.emptyList();
		Map<Long, DTOSchueler> schuelerMap = schuelerListe.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		// Lese die Fachliste aus der DB
		Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
			return Collections.emptyList();
		
		// Erstelle die Fachwahl-Objekte
		Vector<GostFachwahl> fachwahlen = new Vector<>();
		for (DTOGostSchuelerFachbelegungen fachbelegung: fachbelegungen) {
			DTOFach fach = faecher.get(fachbelegung.Fach_ID);
			if (fach == null)
				continue;
			String strKursart = switch(halbjahr.id) {
				case 0 -> fachbelegung.EF1_Kursart;
				case 1 -> fachbelegung.EF2_Kursart;
				case 2 -> fachbelegung.Q11_Kursart;
				case 3 -> fachbelegung.Q12_Kursart;
				case 4 -> fachbelegung.Q21_Kursart;
				case 5 -> fachbelegung.Q22_Kursart;
				default -> null;
			};
			if (strKursart == null)
				continue;
			boolean istSchriftlich = switch (strKursart) {
				case "S", "LK" -> true;
				default -> false;
			};
			GostKursart kursart = switch (strKursart) {
			    case "M" -> "VX".equals(fach.StatistikFach.daten.kuerzelASD) ? GostKursart.VTF : 
			    	        "PX".equals(fach.StatistikFach.daten.kuerzelASD) ? GostKursart.PJK : GostKursart.GK;
			    case "S" -> GostKursart.GK;
			    case "LK" -> GostKursart.LK;
			    case "ZK" -> GostKursart.ZK;
				default -> null;
			};
			if (kursart == null)
				continue;
			DTOSchueler schueler = schuelerMap.get(fachbelegung.Schueler_ID);
			if (schueler == null)
				continue;
			GostFachwahl fw = new GostFachwahl();
			fw.fachID = fachbelegung.Fach_ID;
			fw.schuelerID = schueler.ID;
			fw.kursartID = kursart.id;
			fw.istSchriftlich = istSchriftlich;
			fachwahlen.add(fw);
		}
		return fachwahlen;
	}
	
}
