package de.nrw.schule.svws.data.kurse;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import de.nrw.schule.svws.core.data.kurse.KursDaten;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursDaten}.
 */
public class DataKursdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursDaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKursdaten(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKurs} in einen Core-DTO {@link KursDaten}.  
	 */
	private Function<DTOKurs, KursDaten> dtoMapper = (DTOKurs kurs) -> {
		KursDaten daten = new KursDaten();
		daten.id = kurs.ID;
		daten.idSchuljahresabschnitt = kurs.Schuljahresabschnitts_ID;
		daten.kuerzel = kurs.KurzBez;
		if (kurs.Jahrgang_ID != null)
			daten.idJahrgaenge.add(kurs.Jahrgang_ID);
		if (kurs.Jahrgaenge != null)
			for (String jahrgang : kurs.Jahrgaenge.split(",")) 
				if (jahrgang.matches("^[0-9]+$")) 
					daten.idJahrgaenge.add(Long.parseLong(jahrgang));
		daten.idFach = kurs.Fach_ID;
		daten.lehrer = kurs.Lehrer_ID;
		daten.sortierung = kurs.Sortierung;
		daten.istSichtbar = kurs.Sichtbar;
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
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOKurs kurs = conn.queryByKey(DTOKurs.class, id);
    	if (kurs == null)
    		return OperationError.NOT_FOUND.getResponse();
		KursDaten daten = dtoMapper.apply(kurs);
		// Bestimme die Schüler des Kurses
		List<DTOKursSchueler> listKursSchueler = conn.queryNamed("DTOKursSchueler.kurs_id", daten.id, DTOKursSchueler.class);
    	List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
    	List<DTOSchueler> listSchueler = ((schuelerIDs == null) || (schuelerIDs.size() == 0)) ? new Vector<>() : 
			conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		for (DTOSchueler dto : listSchueler) {
			Schueler schueler = new Schueler();
			schueler.id = dto.ID;
			schueler.geschlecht = dto.Geschlecht.id;
			schueler.nachname = dto.Nachname;
			schueler.vorname = dto.Vorname;
			daten.schueler.add(schueler);
		}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
