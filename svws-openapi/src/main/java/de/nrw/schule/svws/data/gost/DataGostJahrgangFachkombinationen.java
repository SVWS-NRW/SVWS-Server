package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.data.gost.GostJahrgangFachkombinationen;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangFachkombinationen}.
 */
public class DataGostJahrgangFachkombinationen extends DataManager<Long> {

	/** der Abiturjahrgang */
	protected Integer abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangFachkombinationen}.
	 * 
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang 
	 */
	public DataGostJahrgangFachkombinationen(DBEntityManager conn, Integer abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}
	
	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Lese die Fächerkombinationen für den Abiturjahrgang ein
		List<DTOGostJahrgangFachkombinationen> kombis = (abijahrgang == null)
				? conn.queryList("SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang IS NULL", DTOGostJahrgangFachkombinationen.class)
				: conn.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", abijahrgang, DTOGostJahrgangFachkombinationen.class);
		if (kombis == null)
			return OperationError.NOT_FOUND.getResponse();
		Vector<GostJahrgangFachkombinationen> daten = new Vector<>();
		for (DTOGostJahrgangFachkombinationen kombi : kombis) {
			GostJahrgangFachkombinationen result = new GostJahrgangFachkombinationen();
			result.id = kombi.ID;
			result.abiturjahr = kombi.Abi_Jahrgang;
			result.fachID1 = kombi.Fach1_ID;
			result.kursart1 = kombi.Kursart1;
			result.fachID2 = kombi.Fach2_ID;
			result.kursart2 = kombi.Kursart2;
			result.gueltigInHalbjahr[0] = kombi.EF1;
			result.gueltigInHalbjahr[1] = kombi.EF2;
			result.gueltigInHalbjahr[2] = kombi.Q11;
			result.gueltigInHalbjahr[3] = kombi.Q12;
			result.gueltigInHalbjahr[4] = kombi.Q21;
			result.gueltigInHalbjahr[5] = kombi.Q22;
			result.typ = kombi.Typ.getValue();
			result.hinweistext = kombi.Hinweistext;
			daten.add(result);
		}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
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
