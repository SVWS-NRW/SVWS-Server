package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.schule.SchuleStammdaten;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public final class DataStundenplanZeitraster extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataStundenplanZeitraster(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOEigeneSchule} in einen Core-DTO {@link SchuleStammdaten}.
	 */
	private final Function<DTOStundenplanZeitraster, StundenplanZeitraster> dtoMapper = (final DTOStundenplanZeitraster z) -> {
		final StundenplanZeitraster daten = new StundenplanZeitraster();
		daten.id = z.ID;
		daten.wochentag = z.Tag;
		daten.unterrichtstunde = z.Stunde;
		daten.stundenbeginn = z.Beginn;
		daten.stundenende = z.Ende;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt das Zeitraster des Stundenplans zurück.
	 *
	 * @return das Zeitraster
	 */
	public List<StundenplanZeitraster> getZeitraster() {
		final List<DTOStundenplanZeitraster> zeitraster = conn.queryNamed("DTOStundenplanZeitraster.stundenplan_id", this.stundenplanID, DTOStundenplanZeitraster.class);
		final ArrayList<StundenplanZeitraster> daten = new ArrayList<>();
		for (final DTOStundenplanZeitraster z : zeitraster)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getZeitraster()).build();
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
