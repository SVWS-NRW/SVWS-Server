package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.gost.GostBlockungListeneintrag;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungListeneintrag}.
 */
public class DataGostBlockungsliste extends DataManager<Integer> {

  private final int abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungListeneintrag}.
	 * 
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang
	 */
	public DataGostBlockungsliste(DBEntityManager conn, Integer abijahrgang) {
		super(conn);
		if (abijahrgang == null)
			throw new NullPointerException();
		this.abijahrgang = abijahrgang;
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostBlockung} in einen Core-DTO {@link GostBlockungListeneintrag}.  
	 */
	private Function<DTOGostBlockung, GostBlockungListeneintrag> dtoMapper = (DTOGostBlockung blockung) -> {
		GostBlockungListeneintrag daten = new GostBlockungListeneintrag();
		daten.id = blockung.ID;
		daten.name = blockung.Name;
		daten.gostHalbjahr = blockung.Halbjahr.id;		
		return daten;
	};
	
	
	@Override
	public Response getAll() {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1", DTOGostBlockung.class, abijahrgang);
		if (blockungen == null)
			return OperationError.NOT_FOUND.getResponse();
    	var daten = blockungen.stream().map(dtoMapper).collect(Collectors.toList());			
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Integer id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		GostHalbjahr halbjahr = GostHalbjahr.fromID(id);
		if (halbjahr == null)
			return OperationError.NOT_FOUND.getResponse();
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		List<DTOGostBlockung> blockungen = conn.queryList("SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = ?1 and e.Halbjahr = ?2", DTOGostBlockung.class, abijahrgang, halbjahr);
		if (blockungen == null)
			return OperationError.NOT_FOUND.getResponse();
    	var daten = blockungen.stream().map(dtoMapper).collect(Collectors.toList());			
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Integer id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
