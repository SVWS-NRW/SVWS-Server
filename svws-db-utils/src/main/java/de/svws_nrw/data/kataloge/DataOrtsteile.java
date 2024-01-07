package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.List;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link OrtsteilKatalogEintrag}.
 */
public final class DataOrtsteile extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link OrtsteilKatalogEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataOrtsteile(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Liest die Ortsteile aus der Datenbank aus und gibt sie als Katalog zurück
	 * @return Liste der Ortsteil als Katalog.
	 */
	public List<OrtsteilKatalogEintrag> getOrtsteile() {
		final var katalog = conn.queryAll(DTOOrtsteil.class);
		if (katalog == null)
			throw OperationError.NOT_FOUND.exception("Keine Ortsteile gefunden.");
		return katalog.stream().map(k -> {
			final var eintrag = new OrtsteilKatalogEintrag();
			eintrag.id = k.ID;
			eintrag.ort_id = k.Ort_ID;
			eintrag.ortsteil = k.Bezeichnung;
			eintrag.sortierung = k.Sortierung;
			eintrag.istSichtbar = k.Sichtbar;
			eintrag.istAenderbar = k.Aenderbar;
			return eintrag;
		}).toList();
	}

	@Override
	public Response getAll() {
		final List<OrtsteilKatalogEintrag> daten = getOrtsteile();
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
