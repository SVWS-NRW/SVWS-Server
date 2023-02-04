package de.nrw.schule.svws.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurtermin;
import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurvorgabe;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurvorgabe}.
 */
public class DataGostKlausurenVorgabe extends DataManager<Long> {

	private int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 * 
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausurenVorgabe(DBEntityManager conn, int abiturjahr) {
		super(conn);
		_abiturjahr = abiturjahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOGostKlausurenVorgaben} in
	 * einen Core-DTO {@link GostKlausurvorgabe}.
	 */
	private Function<DTOGostKlausurenVorgaben, GostKlausurvorgabe> dtoMapper = (DTOGostKlausurenVorgaben z) -> {
		GostKlausurvorgabe daten = new GostKlausurvorgabe();
		daten.idVorgabe = z.ID;
		daten.abiJahrgang = z.Abi_Jahrgang;
		daten.idFach = z.Fach_ID;
		daten.kursartAllg = z.KursartAllg;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.bemerkungVorgabe = z.Bemerkungen;
		daten.auswahlzeit = z.Auswahlzeit;
		daten.dauer = z.Dauer;
		daten.istAudioNotwendig = z.IstAudioNotwendig;
		daten.istVideoNotwendig = z.IstVideoNotwendig;
		daten.istMdlPruefung = z.IstMdlPruefung;
		return daten;
	};

	/**
	 * Gibt die Liste der Klausurvorgaben einer Jahrgangsstufe im übergebenen
	 * Gost-Halbjahr zurück.
	 * 
	 * @param halbjahr das Gost-Halbjahr
	 * 
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurvorgabe> getKlausurvorgaben(int halbjahr) {
		List<DTOGostKlausurenVorgaben> vorgaben = conn.query("SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr = :hj", DTOGostKlausurenVorgaben.class)
				.setParameter("jgid", _abiturjahr).setParameter("hj", GostHalbjahr.fromID(halbjahr)).getResultList();
		List<GostKlausurvorgabe> daten = new Vector<>();
		for (DTOGostKlausurenVorgaben v : vorgaben)
			daten.add(dtoMapper.apply(v));
		return daten;
	}

	@Override
	public Response get(Long halbjahr) {
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurvorgaben(halbjahr.intValue())).build();
	}

//	@Override
//	public Response patch(Long id, InputStream is) {
//    	Map<String, Object> map = JSONMapper.toMap(is);
//    	if (map.size() > 0) {
//    		try {
//    			conn.transactionBegin();
//    			DTOGostKlausurenTermine termin = conn.queryByKey(DTOGostKlausurenTermine.class, id);
//		    	if (termin == null)
//		    		throw OperationError.NOT_FOUND.exception();
//		    	for (Entry<String, Object> entry : map.entrySet()) {
//		    		String key = entry.getKey();
//		    		Object value = entry.getValue();
//		    		switch (key) {
//						case "id" -> {
//							Long patch_id = JSONMapper.convertToLong(value, true);
//							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
//								throw OperationError.BAD_REQUEST.exception();
//						}
//						case "abijahr" -> {
//							int patch_abijahr = JSONMapper.convertToInteger(value, false);
//							if ((patch_abijahr != termin.Abi_Jahrgang))
//								throw OperationError.BAD_REQUEST.exception();
//						}
//						case "halbjahr" -> {
//							int patch_halbjahr = JSONMapper.convertToInteger(value, false);
//							if ((patch_halbjahr != termin.Halbjahr.id))
//								throw OperationError.BAD_REQUEST.exception();
//						}
//						case "quartal" -> {
//							int patch_quartal = JSONMapper.convertToInteger(value, false);
//							if ((patch_quartal != termin.Quartal))
//								throw OperationError.BAD_REQUEST.exception();
//						}
//						case "bemerkung" -> termin.Bemerkungen = JSONMapper.convertToString(value, true, false);
//						case "datum" -> termin.Datum = JSONMapper.convertToString(value, true, false);
//						case "startzeit" -> termin.Startzeit = JSONMapper.convertToString(value, true, false);
//		    			
//		    			default -> throw OperationError.BAD_REQUEST.exception();
//		    		}
//		    	}
//		    	conn.transactionPersist(termin);
//		    	conn.transactionCommit();
//    		} catch (Exception e) {
//    			if (e instanceof WebApplicationException webAppException)
//    				return webAppException.getResponse();
//				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
//    		} finally {
//    			// Perform a rollback if necessary
//    			conn.transactionRollback();
//    		}
//    	}
//    	return Response.status(Status.OK).build();
//	}
	
	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		// TODO Auto-generated method stub
		return null;
	}

//	/**
//	 * Erstellt einen neuen Gost-Klausurtermin *
//	 * 
//	 * @param halbjahr das Gost-Halbjahr, in dem der Klausurtermin liegen soll
//	 * @param quartal  das Quartal, in dem der Klausurtermin liegen soll.
//	 * @param is       Das JSON-Objekt mit den Daten
//	 * 
//	 * @return Eine Response mit dem neuen Gost-Klausurtermin
//	 */
//	public Response create(int halbjahr, int quartal, InputStream is) {
//		DTOGostKlausurenTermine termin = null;
//		try {
//			conn.transactionBegin();
//			// Bestimme die ID des neuen Klausurtermins
//			DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Gost_Klausuren_Termine");
//			Long ID = lastID == null ? 1 : lastID.MaxID + 1;
//			termin = new DTOGostKlausurenTermine(ID, _abiturjahr, GostHalbjahr.fromID(halbjahr), quartal);
//			Map<String, Object> map = JSONMapper.toMap(is);
//			if (map.size() > 0) {
//				for (Entry<String, Object> entry : map.entrySet()) {
//					String key = entry.getKey();
//					Object value = entry.getValue();
//					switch (key) {
//					case "id" -> {
//						Long patch_id = JSONMapper.convertToLong(value, true);
//						if ((patch_id == null) || (patch_id.longValue() != ID.longValue()))
//							throw OperationError.BAD_REQUEST.exception();
//					}
//					case "abijahr" -> {
//						int patch_abijahr = JSONMapper.convertToInteger(value, false);
//						if ((patch_abijahr != termin.Abi_Jahrgang))
//							throw OperationError.BAD_REQUEST.exception();
//					}
//					case "halbjahr" -> {
//						int patch_halbjahr = JSONMapper.convertToInteger(value, false);
//						if ((patch_halbjahr != termin.Halbjahr.id))
//							throw OperationError.BAD_REQUEST.exception();
//					}
//					case "quartal" -> {
//						int patch_quartal = JSONMapper.convertToInteger(value, false);
//						if ((patch_quartal != termin.Quartal))
//							throw OperationError.BAD_REQUEST.exception();
//					}
//					case "bemerkung" -> termin.Bemerkungen = JSONMapper.convertToString(value, true, false);
//					case "datum" -> termin.Datum = JSONMapper.convertToString(value, true, false);
//					case "startzeit" -> termin.Startzeit = JSONMapper.convertToString(value, true, false);
//					default -> throw OperationError.BAD_REQUEST.exception();
//					}
//				}
//			}
//			conn.transactionPersist(termin);
//			if (!conn.transactionCommit())
//				return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Gost-Klausurtermins");
//		} catch (Exception e) {
//			if (e instanceof WebApplicationException webApplicationException)
//				return webApplicationException.getResponse();
//			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
//		} finally {
//			conn.transactionRollback();
//		}
//
//		GostKlausurtermin daten = dtoMapper.apply(termin);
//		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
//
//	}

}
