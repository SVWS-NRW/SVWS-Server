package de.svws_nrw.data.kurse;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursDaten}.
 */
public final class DataKursdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKursdaten(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKurs} in einen Core-DTO {@link KursDaten}.
	 */
	private final Function<DTOKurs, KursDaten> dtoMapper = (final DTOKurs kurs) -> {
		final KursDaten daten = new KursDaten();
		daten.id = kurs.ID;
		daten.idSchuljahresabschnitt = kurs.Schuljahresabschnitts_ID;
		daten.kuerzel = kurs.KurzBez;
		if (kurs.Jahrgang_ID != null)
			daten.idJahrgaenge.add(kurs.Jahrgang_ID);
		if (kurs.Jahrgaenge != null)
			for (final String jahrgang : kurs.Jahrgaenge.split(","))
				if (jahrgang.matches("^\\d+$"))
					daten.idJahrgaenge.add(Long.parseLong(jahrgang));
		daten.idFach = kurs.Fach_ID;
		daten.lehrer = kurs.Lehrer_ID;
		daten.kursartAllg = kurs.KursartAllg == null ? "" : kurs.KursartAllg;
		daten.sortierung = kurs.Sortierung == null ? 32000 : kurs.Sortierung;
		daten.istSichtbar = kurs.Sichtbar;
		if ((kurs.Schienen != null) && (!kurs.Schienen.isBlank())) {
			for (final String strSchiene : kurs.Schienen.split(",")) {
				if ("".equals(strSchiene.trim()))
					continue;
				try {
					daten.schienen.add(Integer.parseInt(strSchiene.trim()));
				} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
					// ignore exception
				}
			}
		}
		return daten;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOKurs kurs = conn.queryByKey(DTOKurs.class, id);
    	if (kurs == null)
    		return OperationError.NOT_FOUND.getResponse();
		final KursDaten daten = dtoMapper.apply(kurs);
		// Bestimme die Schüler des Kurses
		final List<DTOKursSchueler> listKursSchueler = conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID = ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, daten.id);
    	final List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
    	final List<DTOSchueler> listSchueler = ((schuelerIDs == null) || (schuelerIDs.isEmpty())) ? new ArrayList<>()
    			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		for (final DTOSchueler dto : listSchueler)
			daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
