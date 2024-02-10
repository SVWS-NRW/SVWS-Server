package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.function.Function;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostJahrgangFachkombination;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.gost.GostLaufbahnplanungFachkombinationTyp;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachkombinationen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostJahrgangFachkombination}.
 */
public final class DataGostJahrgangFachkombinationen extends DataManager<Long> {

	/** der Abiturjahrgang */
	protected int abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostJahrgangFachkombination}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang
	 */
	public DataGostJahrgangFachkombinationen(final DBEntityManager conn, final int abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostJahrgangFachkombinationen} in einen Core-DTO {@link GostJahrgangFachkombination}.
	 */
	public static final Function<DTOGostJahrgangFachkombinationen, GostJahrgangFachkombination> dtoMapper = (final DTOGostJahrgangFachkombinationen kombi) -> {
		final GostJahrgangFachkombination daten = new GostJahrgangFachkombination();
		daten.id = kombi.ID;
		daten.abiturjahr = kombi.Abi_Jahrgang;
		daten.fachID1 = kombi.Fach1_ID;
		daten.kursart1 = kombi.Kursart1;
		daten.fachID2 = kombi.Fach2_ID;
		daten.kursart2 = kombi.Kursart2;
		daten.gueltigInHalbjahr[0] = kombi.EF1;
		daten.gueltigInHalbjahr[1] = kombi.EF2;
		daten.gueltigInHalbjahr[2] = kombi.Q11;
		daten.gueltigInHalbjahr[3] = kombi.Q12;
		daten.gueltigInHalbjahr[4] = kombi.Q21;
		daten.gueltigInHalbjahr[5] = kombi.Q22;
		daten.typ = kombi.Typ.getValue();
		daten.hinweistext = kombi.Hinweistext;
		return daten;
	};


	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt die nicht erlaubten und die geforderten Fächerkombinationen für den entsprechenden Abiturjahrgang zurück.
	 *
	 * @param conn          die Datenbankverbindung für die Abfrage
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return eine Liste mit den Fächerkombinationen
	 */
	public static @NotNull List<@NotNull GostJahrgangFachkombination> getFachkombinationen(final DBEntityManager conn, final int abijahrgang) {
		final List<DTOGostJahrgangFachkombinationen> kombis = conn
				.queryNamed("DTOGostJahrgangFachkombinationen.abi_jahrgang", abijahrgang, DTOGostJahrgangFachkombinationen.class);
		if (kombis == null)
			return new ArrayList<>();
		final List<GostJahrgangFachkombination> daten = new ArrayList<>();
		for (final DTOGostJahrgangFachkombinationen kombi : kombis)
			daten.add(dtoMapper.apply(kombi));
        return daten;
	}

	@Override
	public Response getList() {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final @NotNull List<@NotNull GostJahrgangFachkombination> daten = getFachkombinationen(conn, abijahrgang);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			final DTOGostJahrgangFachkombinationen kombi = conn.queryByKey(DTOGostJahrgangFachkombinationen.class, id);
			if (kombi == null)
				throw OperationError.NOT_FOUND.exception();
	    	for (final Entry<String, Object> entry : map.entrySet()) {
	    		final String key = entry.getKey();
	    		final Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
	    			case "abiturjahr" -> throw OperationError.BAD_REQUEST.exception();
	    			case "fachID1" -> {
	    				kombi.Fach1_ID = JSONMapper.convertToLong(value, false);
	    				final DTOFach fach = conn.queryByKey(DTOFach.class, kombi.Fach1_ID);
	    				if (fach == null)
	    					throw OperationError.NOT_FOUND.exception();
	    				if (Boolean.FALSE.equals(fach.IstOberstufenFach))
	    		    		throw OperationError.CONFLICT.exception();
	    			}
	    			case "fachID2" -> {
	    				kombi.Fach2_ID = JSONMapper.convertToLong(value, false);
	    				final DTOFach fach = conn.queryByKey(DTOFach.class, kombi.Fach2_ID);
	    				if (fach == null)
	    					throw OperationError.NOT_FOUND.exception();
	    				if (Boolean.FALSE.equals(fach.IstOberstufenFach))
	    		    		throw OperationError.CONFLICT.exception();
	    			}
	    			case "kursart1" -> {
	    				kombi.Kursart1 = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Jahrgang_Fachkombinationen.col_Kursart1.datenlaenge());
	    				if (kombi.Kursart1 == null) {
	    					final GostKursart kursart = GostKursart.fromKuerzel(kombi.Kursart1);
	    					if (kursart == null)
		    					throw OperationError.NOT_FOUND.exception();
	    				}
	    			}
	    			case "kursart2" -> {
	    				kombi.Kursart2 = JSONMapper.convertToString(value, true, false, Schema.tab_Gost_Jahrgang_Fachkombinationen.col_Kursart2.datenlaenge());
	    				if (kombi.Kursart2 == null) {
	    					final GostKursart kursart = GostKursart.fromKuerzel(kombi.Kursart2);
	    					if (kursart == null)
		    					throw OperationError.NOT_FOUND.exception();
	    				}
	    			}
	    			case "gueltigInHalbjahr" -> {
	    				final Boolean[] data = JSONMapper.convertToBooleanArray(value, false, false, 6);
	    				kombi.EF1 = data[0];
	    				kombi.EF2 = data[1];
	    				kombi.Q11 = data[2];
	    				kombi.Q12 = data[3];
	    				kombi.Q21 = data[4];
	    				kombi.Q22 = data[5];
	    			}
	    			case "typ" -> throw OperationError.BAD_REQUEST.exception();
	    			case "hinweistext" -> kombi.Hinweistext = JSONMapper.convertToString(value, false, true, Schema.tab_Gost_Jahrgang_Fachkombinationen.col_Hinweistext.datenlaenge());
	    			default -> throw OperationError.BAD_REQUEST.exception();
	    		}
	    	}
	    	conn.transactionPersist(kombi);
		}
        return Response.status(Status.OK).build();
	}


	/**
	 * Löscht eine Regel zu einer Fachkombination der Gymnasialen Oberstufe
	 *
	 * @param id   die ID der Regel
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Fachkombination
		final DTOGostJahrgangFachkombinationen kombi = conn.queryByKey(DTOGostJahrgangFachkombinationen.class, id);
		if (kombi == null)
    		throw OperationError.NOT_FOUND.exception();
		// Erzeuge den Core-DTO, der zurückgegeben wird
		final GostJahrgangFachkombination daten = dtoMapper.apply(kombi);
		// Entferne die Fachkombination
		conn.transactionRemove(kombi);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Fügt eine neue Regel zu einer Fachkombination vom angegebenen Typ hinzu
	 *
	 * @param typ   der typ der Regel
	 *
	 * @return die neu erstellte Regel
	 */
	public Response add(final int typ) {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Prüfe ob der Typ der Regel korrekt ist
		final GostLaufbahnplanungFachkombinationTyp kombityp = GostLaufbahnplanungFachkombinationTyp.fromValue(typ);
		// Bestimme die ID für die neue Regel
		final DTOSchemaAutoInkremente dbID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_Gost_Jahrgang_Fachkombinationen.name());
		final long id = dbID == null ? 1 : dbID.MaxID + 1;
		// Bestimme die Fächer der gymnasialen Oberstufe, um zwei Default-Fächer zu bestimmen
		final GostFaecherManager fachmanager = DBUtilsFaecherGost.getFaecherManager(conn, abijahrgang);
		final List<GostFach> faecher = fachmanager.faecher();
		if (faecher.size() < 2)
			throw OperationError.NOT_FOUND.exception("Nicht genügend Fächer für den Abiturjahrgang definiert.");
		final DTOGostJahrgangFachkombinationen kombi = new DTOGostJahrgangFachkombinationen(id, abijahrgang, faecher.get(0).id, faecher.get(1).id, true, true, true, true, true, true, kombityp, "");
		conn.transactionPersist(kombi);
		final GostJahrgangFachkombination daten = dtoMapper.apply(kombi);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
