package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachwahlManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
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
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
			return new ArrayList<>();
    	// Bestimme alle Schüler-IDs des angegebenen Abiturjahrgangs
		final List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahr, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
			return new ArrayList<>();
		final HashMap<Long, GostStatistikFachwahl> fachwahlen = new HashMap<>();
		for (final DTOViewGostSchuelerAbiturjahrgang schueler : schuelerAbijahrgang) {
			final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, schueler.ID);
			for (final AbiturFachbelegung belegung : abidaten.fachbelegungen) {
				final DTOFach fach = faecher.get(belegung.fachID);
				if (fach == null)
					continue;
		        if ((belegung.belegungen[GostHalbjahr.EF1.id] == null) && (belegung.belegungen[GostHalbjahr.EF2.id] == null) && (belegung.belegungen[GostHalbjahr.Q11.id] == null)
		        		&& (belegung.belegungen[GostHalbjahr.Q12.id] == null) && (belegung.belegungen[GostHalbjahr.Q21.id] == null) && (belegung.belegungen[GostHalbjahr.Q22.id] == null))
		            continue;
		        GostStatistikFachwahl statfw = fachwahlen.get(belegung.fachID);
		        if (statfw == null) {
		        	statfw = new GostStatistikFachwahl();
		        	statfw.abiturjahr = abijahr;
		        	statfw.id = fach.ID;
		            statfw.kuerzel = fach.Kuerzel;
		            statfw.bezeichnung = fach.Bezeichnung;
		            statfw.kuerzelStatistik = fach.StatistikFach.daten.kuerzelASD;
		            for (final GostHalbjahr halbjahr : GostHalbjahr.values())
		            	statfw.fachwahlen[halbjahr.id] = new GostStatistikFachwahlHalbjahr();
		            fachwahlen.put(statfw.id, statfw);
		        }
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					if (belegung.belegungen[halbjahr.id] != null) {
						final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[halbjahr.id];
				        final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
				        if (kursart != null) {
							switch (kursart) {
								case GK -> {
									statfw.fachwahlen[halbjahr.id].wahlenGK++;
									if (belegungHj.schriftlich)
										statfw.fachwahlen[halbjahr.id].wahlenGKSchriftlich++;
									else
										statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich++;
								}
								case LK -> statfw.fachwahlen[halbjahr.id].wahlenLK++;
								case PJK, VTF -> {
									statfw.fachwahlen[halbjahr.id].wahlenGK++;
									statfw.fachwahlen[halbjahr.id].wahlenGKMuendlich++;
								}
								case ZK -> statfw.fachwahlen[halbjahr.id].wahlenZK++;
							}
						}
					}
				}
		        if (belegung.abiturFach != null) {
		        	if (belegung.abiturFach == 3)
		        		statfw.wahlenAB3++;
		        	if (belegung.abiturFach == 4)
		        		statfw.wahlenAB4++;
		        }
			}
		}
		return fachwahlen.values().stream()
				.sorted((a, b) -> Integer.compare(faecher.get(a.id).SortierungAllg, faecher.get(b.id).SortierungAllg))
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
        	if ((daten.halbjahr[halbjahr.id] != null) && (!daten.halbjahr[halbjahr.id].fachwahlen.isEmpty())) {
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
		// Bestimme die Schüler und prüfe im Falle von Abgängern, ob diese in dem Halbjahr an der Schule waren
		final List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.isEmpty()))
	        return new GostJahrgangFachwahlenHalbjahr();
		for (int i = schuelerListe.size() - 1; i >= 0; i--) {
			final DTOSchueler schueler = schuelerListe.get(i);
			if (!DataGostBlockungsdaten.checkIstAnSchule(conn, schueler, halbjahr, abijahr))
				schuelerListe.remove(i);
		}
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
	        return new GostJahrgangFachwahlenHalbjahr();

		// Erstelle die Fachwahl-Objekte
		final GostJahrgangFachwahlenHalbjahr result = new GostJahrgangFachwahlenHalbjahr();
		for (final DTOSchueler schueler : schuelerListe) {
			final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, schueler.ID);
			for (final AbiturFachbelegung belegung : abidaten.fachbelegungen) {
				final DTOFach fach = faecher.get(belegung.fachID);
				if (fach == null)
					continue;
				if (belegung.belegungen[halbjahr.id] != null) {
					final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[halbjahr.id];
					final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
					if (kursart == null)
						continue;
					final GostFachwahl fw = new GostFachwahl();
					fw.fachID = belegung.fachID;
					fw.schuelerID = schueler.ID;
					fw.kursartID = kursart.id;
					fw.istSchriftlich = belegungHj.schriftlich;
					fw.abiturfach = belegung.abiturFach;
					result.fachwahlen.add(fw);
				}
			}
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
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
	        return new GostJahrgangFachwahlen();
		// Erstelle die Fachwahl-Objekte
		final GostJahrgangFachwahlen result = new GostJahrgangFachwahlen();
		for (final DTOViewGostSchuelerAbiturjahrgang schueler : schuelerAbijahrgang) {
			final Abiturdaten abidaten = DBUtilsGostLaufbahn.get(conn, schueler.ID);
			for (final AbiturFachbelegung belegung : abidaten.fachbelegungen) {
				final DTOFach fach = faecher.get(belegung.fachID);
				if (fach == null)
					continue;
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					if (belegung.belegungen[halbjahr.id] != null) {
						final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[halbjahr.id];
						final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
						if (kursart == null)
							continue;
						final GostFachwahl fw = new GostFachwahl();
						fw.fachID = belegung.fachID;
						fw.schuelerID = schueler.ID;
						fw.kursartID = kursart.id;
						fw.istSchriftlich = belegungHj.schriftlich;
						fw.abiturfach = belegung.abiturFach;
						if (result.halbjahr[halbjahr.id] == null)
							result.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
						result.halbjahr[halbjahr.id].fachwahlen.add(fw);
					}
				}
				if ((belegung.abiturFach != null) && (belegung.belegungen[GostHalbjahr.Q22.id] != null)) {
					final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[GostHalbjahr.Q22.id];
					final GostFachwahl fwAbi = new GostFachwahl();
					fwAbi.fachID = belegung.fachID;
					fwAbi.schuelerID = schueler.ID;
					fwAbi.kursartID = GostKursart.fromKuerzel(belegungHj.kursartKuerzel).id;
					fwAbi.istSchriftlich = belegungHj.schriftlich;
					fwAbi.abiturfach = belegung.abiturFach;
					result.abitur.fachwahlen.add(fwAbi);
				}
			}
		}
		return result;
	}

}
