package de.nrw.schule.svws.data.jahrgaenge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.jahrgang.JahrgangsListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsListeEintrag}.
 */
public class DataJahrgangsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsListeEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsliste(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOJahrgang} in einen Core-DTO {@link JahrgangsListeEintrag}.  
	 */
	private Function<DTOJahrgang, JahrgangsListeEintrag> dtoMapperJahrgang = (DTOJahrgang j) -> {
		JahrgangsListeEintrag eintrag = new JahrgangsListeEintrag();
		eintrag.id = j.ID;
		eintrag.kuerzel = j.InternKrz;
		eintrag.kuerzelStatistik = j.ASDJahrgang; // TODO Anpassung beim DTO, so dass ein Konverter zu dem Statistik-Jahrgangs-Objekt genutzt wird - hierbei auch die Bezeichnung miteinbeziehen
		eintrag.bezeichnung = j.ASDBezeichnung;
		eintrag.idSchulgliederung = j.Gliederung.daten.kuerzel;
		eintrag.idFolgejahrgang = j.Folgejahrgang_ID;
		eintrag.sortierung = j.Sortierung;
		eintrag.istSichtbar = j.Sichtbar;
		return eintrag;
	};

	@Override
	public Response getAll() {
    	List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
    	if (jahrgaenge == null)
    		return OperationError.NOT_FOUND.getResponse();
    	List<JahrgangsListeEintrag> daten = jahrgaenge.stream().map(dtoMapperJahrgang).sorted((a,b) -> Long.compare(a.sortierung, b.sortierung)).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
