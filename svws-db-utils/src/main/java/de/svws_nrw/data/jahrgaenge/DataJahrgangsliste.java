package de.svws_nrw.data.jahrgaenge;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import de.svws_nrw.core.data.jahrgang.JahrgangsListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link JahrgangsListeEintrag}.
 */
public final class DataJahrgangsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link JahrgangsListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataJahrgangsliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOJahrgang} in einen Core-DTO {@link JahrgangsListeEintrag}.
	 */
	private final Function<DTOJahrgang, JahrgangsListeEintrag> dtoMapperJahrgang = (final DTOJahrgang j) -> {
		final JahrgangsListeEintrag eintrag = new JahrgangsListeEintrag();
		eintrag.id = j.ID;
		eintrag.kuerzel = j.InternKrz;
		eintrag.kuerzelStatistik = j.ASDJahrgang; // TODO Anpassung beim DTO, so dass ein Konverter zu dem Statistik-Jahrgangs-Objekt genutzt wird - hierbei auch die Bezeichnung miteinbeziehen
		eintrag.bezeichnung = j.ASDBezeichnung;
		eintrag.kuerzelSchulgliederung = j.Gliederung.daten.kuerzel;
		eintrag.idFolgejahrgang = j.Folgejahrgang_ID;
		eintrag.sortierung = j.Sortierung;
		eintrag.istSichtbar = j.Sichtbar;
		eintrag.gueltigVon = j.GueltigVon;
		eintrag.gueltigBis = j.GueltigBis;
		return eintrag;
	};

	@Override
	public Response getAll() {
    	final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
    	if (jahrgaenge == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final List<JahrgangsListeEintrag> daten = jahrgaenge.stream().map(dtoMapperJahrgang).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
