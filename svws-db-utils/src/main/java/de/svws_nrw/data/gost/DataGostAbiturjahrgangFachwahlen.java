package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachwahlManager;
import de.svws_nrw.core.utils.gost.GostStatistikFachwahlManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostStatistikFachwahl}.
 */
public final class DataGostAbiturjahrgangFachwahlen extends DataManager<Long> {

	private final Integer abijahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostStatistikFachwahl}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahr   der Abi-Jahrgang, für welchen die Fachwahlen ermittelt werden sollen
	 */
	public DataGostAbiturjahrgangFachwahlen(final DBEntityManager conn, final Integer abijahr) {
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
		final List<GostStatistikFachwahl> daten = this.getFachwahlen();
		if (daten == null)
			return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final List<GostStatistikFachwahl> alle = this.getFachwahlen();
		if (alle == null)
			return OperationError.NOT_FOUND.getResponse();
		for (final GostStatistikFachwahl wahl : alle) {
			if (wahl.id == id)
				return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(wahl).build();
		}
		return OperationError.NOT_FOUND.getResponse();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
	 *
	 * @return die Statistik zu den Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public List<GostStatistikFachwahl> getFachwahlen() {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> schueler = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schueler == null) || (schueler.isEmpty()))
			return new ArrayList<>();
		final List<Long> schuelerIDs = schueler.stream().map(s -> s.ID).toList();
    	final List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
		if (fachbelegungen == null)
			return new ArrayList<>();
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
			return new ArrayList<>();
    	// Ermittle die Fachwahlmatrix für alle angegebenen Schüler-IDs
		final HashMap<Long, GostStatistikFachwahl> fachwahlen = new HashMap<>();
	    for (final DTOGostSchuelerFachbelegungen fachbelegung: fachbelegungen) {
	        if ((fachbelegung.EF1_Kursart == null) && (fachbelegung.EF2_Kursart == null) && (fachbelegung.Q11_Kursart == null)
	        		&& (fachbelegung.Q12_Kursart == null) && (fachbelegung.Q21_Kursart == null) && (fachbelegung.Q22_Kursart == null))
	            continue;
	        GostStatistikFachwahl fachwahl = fachwahlen.get(fachbelegung.Fach_ID);
	        if (fachwahl == null) {
	            final DTOFach fach = faecher.get(fachbelegung.Fach_ID);
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
	        if (fachbelegung.AbiturFach != null) {
	        	if (fachbelegung.AbiturFach == 3)
	    	        fachwahl.wahlenAB3++;
	        	if (fachbelegung.AbiturFach == 4)
	        		fachwahl.wahlenAB4++;
	        }
	    }
		return fachwahlen.values().stream()
				.sorted((a, b) -> Integer.compare(faecher.get(a.id).SortierungSekII, faecher.get(b.id).SortierungSekII))
				.toList();
	}


    /**
     * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
     *
     * @return eine HTTP-Response, bei einem Erfolg: Die Fachwahlen des Abiturjahrgangs dieses Objektes
     */
	public Response getSchuelerFachwahlenResponse() {
        final GostJahrgangFachwahlen daten = this.getSchuelerFachwahlen();
        boolean noDataExists = daten.abitur.fachwahlen.isEmpty();
        for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
        	if (!daten.halbjahr[halbjahr.id].fachwahlen.isEmpty()) {
        		noDataExists = false;
        		break;
        	}
        }
        if (noDataExists)
            return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


    /**
     * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
     *
     * @param halbjahr_id   die ID des Halbjahres der gymnasialen Oberstufe, für welches die
     *                      Fachwahlen bestimmt werden sollen
     *
     * @return eine HTTP-Response, bei einem Erfolg: Die Fachwahlen des Abiturjahrgangs dieses Objektes
     */
	public Response getSchuelerFachwahlenResponseHalbjahr(final int halbjahr_id) {
        final GostJahrgangFachwahlenHalbjahr daten = this.getSchuelerFachwahlenHalbjahr(GostHalbjahr.fromID(halbjahr_id));
        if (daten.fachwahlen.isEmpty())
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
	public GostFachwahlManager getFachwahlManager(final GostHalbjahr halbjahr) {
		return new GostFachwahlManager(this.getSchuelerFachwahlenHalbjahr(halbjahr));
	}


	private static GostFachwahl getSchuelerFachwahl(final DTOSchueler schueler, final DTOGostSchuelerFachbelegungen fachbelegung, final DTOFach fach, final GostHalbjahr halbjahr) {
		String strKursart = fachbelegung.Q22_Kursart;
		if (halbjahr != null) {
			strKursart = switch (halbjahr.id) {
				case 0 -> fachbelegung.EF1_Kursart;
				case 1 -> fachbelegung.EF2_Kursart;
				case 2 -> fachbelegung.Q11_Kursart;
				case 3 -> fachbelegung.Q12_Kursart;
				case 4 -> fachbelegung.Q21_Kursart;
				case 5 -> fachbelegung.Q22_Kursart;
				default -> null;
			};
		}
		if (strKursart == null)
			return null;
		final boolean istSchriftlich = switch (strKursart) {
			case "S", "LK" -> true;
			default -> false;
		};
		final GostKursart kursart = switch (strKursart) {
		    case "M" -> {
		    	if ("VX".equals(fach.StatistikFach.daten.kuerzelASD))
		    		yield GostKursart.VTF;
		    	if ("PX".equals(fach.StatistikFach.daten.kuerzelASD))
		    		yield GostKursart.PJK;
		    	yield GostKursart.GK;
		    }
		    case "S" -> GostKursart.GK;
		    case "LK" -> GostKursart.LK;
		    case "ZK" -> GostKursart.ZK;
			default -> null;
		};
		if (kursart == null)
			return null;
		final GostFachwahl fw = new GostFachwahl();
		fw.fachID = fachbelegung.Fach_ID;
		fw.schuelerID = schueler.ID;
		fw.kursartID = kursart.id;
		fw.istSchriftlich = istSchriftlich;
		return fw;
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes für das angegebene Halbjahr.
	 *
	 * @param halbjahr   das Halbjahr der gymnasialen Oberstufe, für welches die
	 *                   Fachwahlen bestimmt werden sollen
	 *
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public GostJahrgangFachwahlenHalbjahr getSchuelerFachwahlenHalbjahr(final GostHalbjahr halbjahr) {
	    if (halbjahr == null)
	        return new GostJahrgangFachwahlenHalbjahr();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
	        return new GostJahrgangFachwahlenHalbjahr();
		final List<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).toList();
    	final List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
	        return new GostJahrgangFachwahlenHalbjahr();
		// Bestimme die Schülernamen
		final List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.isEmpty()))
	        return new GostJahrgangFachwahlenHalbjahr();
		final Map<Long, DTOSchueler> schuelerMap = schuelerListe.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
	        return new GostJahrgangFachwahlenHalbjahr();

		// Erstelle die Fachwahl-Objekte
		final GostJahrgangFachwahlenHalbjahr result = new GostJahrgangFachwahlenHalbjahr();
		for (final DTOGostSchuelerFachbelegungen fachbelegung: fachbelegungen) {
			final DTOSchueler schueler = schuelerMap.get(fachbelegung.Schueler_ID);
			if (schueler == null)
				continue;
			final DTOFach fach = faecher.get(fachbelegung.Fach_ID);
			if (fach == null)
				continue;
			final GostFachwahl fw = getSchuelerFachwahl(schueler, fachbelegung, fach, halbjahr);
			if (fw != null)
				result.fachwahlen.add(fw);
		}
		return result;
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
	 *
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public GostJahrgangFachwahlen getSchuelerFachwahlen() {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
	        return new GostJahrgangFachwahlen();
		final List<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).toList();
    	final List<DTOGostSchuelerFachbelegungen> fachbelegungen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
		if ((fachbelegungen == null) || (fachbelegungen.isEmpty()))
	        return new GostJahrgangFachwahlen();
		// Bestimme die Schülernamen
		final List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.isEmpty()))
	        return new GostJahrgangFachwahlen();
		final Map<Long, DTOSchueler> schuelerMap = schuelerListe.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
	        return new GostJahrgangFachwahlen();

		// Erstelle die Fachwahl-Objekte
		final GostJahrgangFachwahlen result = new GostJahrgangFachwahlen();
		for (final DTOGostSchuelerFachbelegungen fachbelegung: fachbelegungen) {
			final DTOSchueler schueler = schuelerMap.get(fachbelegung.Schueler_ID);
			if (schueler == null)
				continue;
			final DTOFach fach = faecher.get(fachbelegung.Fach_ID);
			if (fach == null)
				continue;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				final GostFachwahl fw = getSchuelerFachwahl(schueler, fachbelegung, fach, halbjahr);
				if (fw != null) {
					if (result.halbjahr[halbjahr.id] == null)
						result.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
					result.halbjahr[halbjahr.id].fachwahlen.add(fw);
				}
			}
			final GostFachwahl fwAbi = getSchuelerFachwahl(schueler, fachbelegung, fach, null);
			if (fwAbi != null)
				result.abitur.fachwahlen.add(fwAbi);
		}
		return result;
	}

}
