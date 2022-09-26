package de.nrw.schule.svws.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schule.SchuleStammdaten;
import de.nrw.schule.svws.core.data.stundenplan.StundenplanZeitraster;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public class DataStundenplanZeitraster extends DataManager<Long> {

	private final Long stundenplanID;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 * 
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataStundenplanZeitraster(DBEntityManager conn, Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}
	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOEigeneSchule} in einen Core-DTO {@link SchuleStammdaten}.  
	 */
	private Function<DTOStundenplanZeitraster, StundenplanZeitraster> dtoMapper = (DTOStundenplanZeitraster z) -> {
		StundenplanZeitraster daten = new StundenplanZeitraster();
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
	public Vector<StundenplanZeitraster> getZeitraster() {
		List<DTOStundenplanZeitraster> zeitraster = conn.queryNamed("DTOStundenplanZeitraster.stundenplan_id", this.stundenplanID, DTOStundenplanZeitraster.class); 
		Vector<StundenplanZeitraster> daten = new Vector<>();
		for (DTOStundenplanZeitraster z : zeitraster)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response getList() {
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getZeitraster()).build();
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
