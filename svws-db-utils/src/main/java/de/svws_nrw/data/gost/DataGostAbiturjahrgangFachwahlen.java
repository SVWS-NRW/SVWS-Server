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
import de.svws_nrw.core.data.gost.GostStatistikFachwahlHalbjahr;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFachwahlManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
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
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
			return new ArrayList<>();
		// Bestimme die Fachwahlen des Abiturjahrganges
		final GostJahrgangFachwahlen wahlen = this.getSchuelerFachwahlen();
		final HashMap<Long, GostStatistikFachwahl> matrixFachwahlen = new HashMap<>();
		for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
			if (wahlen.halbjahr[halbjahr.id] == null)
				continue;
			for (final GostFachwahl wahl : wahlen.halbjahr[halbjahr.id].fachwahlen) {
				final DTOFach fach = faecher.get(wahl.fachID);
				if (fach == null)
					continue;
				GostStatistikFachwahl statfw = matrixFachwahlen.get(fach.ID);
		        if (statfw == null) {
		        	statfw = new GostStatistikFachwahl();
		        	statfw.abiturjahr = abijahr;
		        	statfw.id = wahl.fachID;
		            statfw.kuerzel = fach.Kuerzel;
		            statfw.bezeichnung = fach.Bezeichnung;
		            statfw.kuerzelStatistik = fach.StatistikFach.daten.kuerzelASD;
		            for (final GostHalbjahr hj : GostHalbjahr.values())
		            	statfw.fachwahlen[hj.id] = new GostStatistikFachwahlHalbjahr();
		            matrixFachwahlen.put(statfw.id, statfw);
		        }
		        final GostKursart kursart = GostKursart.fromIDorNull(wahl.kursartID);
		        if (kursart != null) {
					switch (kursart) {
						case GK -> {
							statfw.fachwahlen[halbjahr.id].wahlenGK++;
							if (wahl.istSchriftlich)
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
		for (final GostFachwahl wahl : wahlen.abitur.fachwahlen) {
			final GostStatistikFachwahl statfw = matrixFachwahlen.get(wahl.fachID);
			if (statfw == null)
				continue;
	        if (wahl.abiturfach != null) {
	        	if (wahl.abiturfach == 3)
	        		statfw.wahlenAB3++;
	        	if (wahl.abiturfach == 4)
	        		statfw.wahlenAB4++;
	        }
		}
		return matrixFachwahlen.values().stream()
				.sorted((a, b) -> {
					final int cmp = GostFachbereich.compareFachByKuerzel(a.kuerzelStatistik, b.kuerzelStatistik);
					if (cmp != 0)
						return cmp;
					return Integer.compare(faecher.get(a.id).SortierungAllg, faecher.get(b.id).SortierungAllg);
				})
				.toList();
	}


    /**
     * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
     *
     * @return eine HTTP-Response, bei einem Erfolg: Die Fachwahlen des Abiturjahrgangs dieses Objektes
     */
	public Response getSchuelerFachwahlenResponse() {
        final GostJahrgangFachwahlen daten = this.getSchuelerFachwahlen();
        for (final GostHalbjahr halbjahr : GostHalbjahr.values())
        	if (daten.halbjahr[halbjahr.id] == null)
        		daten.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
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
	    final GostJahrgangFachwahlenHalbjahr result = this.getSchuelerFachwahlen().halbjahr[halbjahr.id];
	    return result == null ? new GostJahrgangFachwahlenHalbjahr() : result;
	}


	/**
	 * Ermittelt die Fachwahlen zu dem Abiturjahrgang dieses Objektes.
	 *
	 * @return die Fachwahlen des Abiturjahrgangs dieses Objektes
	 */
	public GostJahrgangFachwahlen getSchuelerFachwahlen() {
		final Map<Long, GostJahrgangFachwahlen> mapFachwahlen = DBUtilsGostLaufbahn.getFachwahlenByAbiJahrgang(conn, abijahr);
		final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, new ArrayList<>(mapFachwahlen.keySet())).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		final GostJahrgangFachwahlen result = new GostJahrgangFachwahlen();
		for (final Map.Entry<Long, GostJahrgangFachwahlen> entry : mapFachwahlen.entrySet()) {
			final DTOSchueler schueler = mapSchueler.get(entry.getKey());
			final GostJahrgangFachwahlen fachwahlen = entry.getValue();
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if (DBUtilsGost.pruefeIstAnSchule(schueler, halbjahr, abijahr, mapSchuljahresabschnitte) && (fachwahlen.halbjahr[halbjahr.id] != null)) {
					if (result.halbjahr[halbjahr.id] == null)
						result.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
					result.halbjahr[halbjahr.id].fachwahlen.addAll(fachwahlen.halbjahr[halbjahr.id].fachwahlen);
				}
			}
			if (DBUtilsGost.pruefeIstAnSchule(schueler, GostHalbjahr.Q22, abijahr, mapSchuljahresabschnitte))
				result.abitur.fachwahlen.addAll(fachwahlen.abitur.fachwahlen);
		}
		return result;
	}

}
