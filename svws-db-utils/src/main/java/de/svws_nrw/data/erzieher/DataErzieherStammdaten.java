package de.svws_nrw.data.erzieher;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import de.svws_nrw.core.data.erzieher.ErzieherStammdaten;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOErzieherart;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ErzieherStammdaten}.
 */
public final class DataErzieherStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ErzieherStammdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherStammdaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherStammdaten}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherStammdaten> dtoMapperErzieher1 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherStammdaten eintrag = new ErzieherStammdaten();
		eintrag.id = e.ID * 10 + 1;
        eintrag.idSchueler = e.Schueler_ID;
        eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.titel = e.Titel1;
        eintrag.anrede = e.Anrede1;
		eintrag.nachname = e.Name1;
		eintrag.zusatzNachname = e.Erz1ZusatzNachname;
        eintrag.vorname = e.Vorname1;
		eintrag.strassenname = e.ErzStrassenname;
		eintrag.hausnummer = e.ErzHausNr;
		eintrag.hausnummerZusatz = e.ErzHausNrZusatz;
		eintrag.wohnortID = e.ErzOrt_ID;
		eintrag.ortsteilID = e.ErzOrtsteil_ID;
        eintrag.eMail = e.ErzEmail;
		eintrag.staatsangehoerigkeitID = e.Erz1StaatKrz == null ? null : e.Erz1StaatKrz.daten.iso3;
		eintrag.erhaeltAnschreiben = e.ErzAnschreiben;
		eintrag.bemerkungen = e.Bemerkungen;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln des zweiten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherStammdaten}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherStammdaten> dtoMapperErzieher2 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherStammdaten eintrag = new ErzieherStammdaten();
		eintrag.id = e.ID * 10 + 2;
        eintrag.idSchueler = e.Schueler_ID;
        eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.titel = e.Titel2;
        eintrag.anrede = e.Anrede2;
		eintrag.nachname = e.Name2;
		eintrag.zusatzNachname = e.Erz2ZusatzNachname;
        eintrag.vorname = e.Vorname2;
		eintrag.strassenname = e.ErzStrassenname;
		eintrag.hausnummer = e.ErzHausNr;
		eintrag.hausnummerZusatz = e.ErzHausNrZusatz;
		eintrag.wohnortID = e.ErzOrt_ID;
		eintrag.ortsteilID = e.ErzOrtsteil_ID;
        eintrag.eMail = e.ErzEmail2;
		eintrag.staatsangehoerigkeitID = e.Erz2StaatKrz == null ? null : e.Erz2StaatKrz.daten.iso3;
		eintrag.erhaeltAnschreiben = e.ErzAnschreiben;
		eintrag.bemerkungen = e.Bemerkungen;
		return eintrag;
	};

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt eine Liste der {@link ErzieherStammdaten} für den Schüler mit der angegebenen ID.
	 *
	 * @param schuelerID   die ID des Schülers, dessen {@link ErzieherStammdaten} ermittelt werden sollen
	 *
	 * @return eine Liste mit den {@link ErzieherStammdaten} für den Schüler mit der angegebenen ID
	 */
	public Response getListFromSchueler(final long schuelerID) {
		final List<DTOSchuelerErzieherAdresse> erzieher = conn.queryNamed("DTOSchuelerErzieherAdresse.schueler_id", schuelerID, DTOSchuelerErzieherAdresse.class);
		if (erzieher == null)
			return OperationError.NOT_FOUND.getResponse();
		final List<ErzieherStammdaten> daten = new ArrayList<>();
		daten.addAll(erzieher.stream().filter(e -> ((e.Name1 != null) && !"".equals(e.Name1.trim()))).map(dtoMapperErzieher1).toList());
		daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !"".equals(e.Name2.trim()))).map(dtoMapperErzieher2).toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(final Long tmpid) {
		if (tmpid == null)
			return OperationError.NOT_FOUND.getResponse();
        final long id = tmpid / 10;
        final long nr = tmpid % 10;
        if ((nr != 1) && (nr != 2))
        	return OperationError.NOT_FOUND.getResponse();
    	final DTOSchuelerErzieherAdresse erzieher = conn.queryByKey(DTOSchuelerErzieherAdresse.class, id);
    	if (erzieher == null) {
        	return OperationError.NOT_FOUND.getResponse();
    	}
		final ErzieherStammdaten daten = (nr == 1) ? dtoMapperErzieher1.apply(erzieher) : dtoMapperErzieher2.apply(erzieher);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long tmpid, final InputStream is) {
        if (tmpid == null)
            return OperationError.NOT_FOUND.getResponse();
        final long id = tmpid / 10;
        final long nr = tmpid % 10;
        if ((nr != 1) && (nr != 2))
            return OperationError.NOT_FOUND.getResponse();
        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();
                final DTOSchuelerErzieherAdresse erzieher = conn.queryByKey(DTOSchuelerErzieherAdresse.class, id);
                if (erzieher == null)
                    return OperationError.NOT_FOUND.getResponse();
                for (final Entry<String, Object> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    switch (key) {
						case "id" -> {
							final Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != tmpid.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
                        case "idSchueler" -> throw OperationError.BAD_REQUEST.exception();
                        case "idErzieherArt" -> {
		    		    	final Long artID = JSONMapper.convertToLong(value, true);
		    		    	if (artID == null) {
    						    erzieher.ErzieherArt_ID = null;
	    					} else {
                                final DTOErzieherart art = conn.queryByKey(DTOErzieherart.class, artID);
		    			    	if (art == null)
		    			    		throw OperationError.NOT_FOUND.exception();
	    					    erzieher.ErzieherArt_ID = artID;
	    					}
                        }
                        case "titel" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Titel1.datenlaenge());
                            if (nr == 1)
                                erzieher.Titel1 = tmp;
                            else
                                erzieher.Titel2 = tmp;
                        }
                        case "anrede" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Anrede1.datenlaenge());
                            if (nr == 1)
                                erzieher.Anrede1 = tmp;
                            else
                                erzieher.Anrede2 = tmp;
                        }
                        case "nachname" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Name1.datenlaenge());
                            if (nr == 1)
                                erzieher.Name1 = tmp;
                            else
                                erzieher.Name2 = tmp;
                        }
                        case "zusatzNachname" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Erz1ZusatzNachname.datenlaenge());
                            if (nr == 1)
                                erzieher.Erz1ZusatzNachname = tmp;
                            else
                                erzieher.Erz2ZusatzNachname = tmp;
                        }
                        case "vorname" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Vorname1.datenlaenge());
                            if (nr == 1)
                                erzieher.Vorname1 = tmp;
                            else
                                erzieher.Vorname2 = tmp;
                        }
                        case "eMail" -> {
                            final String tmp = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_ErzEmail.datenlaenge());
                            if (nr == 1)
                                erzieher.ErzEmail = tmp;
                            else
                                erzieher.ErzEmail2 = tmp;
                        }
                        case "strassenname" -> erzieher.ErzStrassenname = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_ErzStrassenname.datenlaenge());
		    			case "hausnummer" -> erzieher.ErzHausNr = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_ErzHausNr.datenlaenge());
		    			case "hausnummerZusatz" -> erzieher.ErzHausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_ErzHausNrZusatz.datenlaenge());
		    			case "wohnortID" -> setWohnort(conn, erzieher, JSONMapper.convertToLong(value, true), map.get("ortsteilID") == null ? erzieher.ErzOrtsteil_ID : ((Long) map.get("ortsteilID")));
		    			case "ortsteilID" -> setWohnort(conn, erzieher, map.get("wohnortID") == null ? erzieher.ErzOrt_ID : ((Long) map.get("wohnortID")), JSONMapper.convertToLong(value, true));

		    			case "staatsangehoerigkeitID" -> {
		    		    	final String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true, null);
		    		    	if ((staatsangehoerigkeitID == null) || ("".equals(staatsangehoerigkeitID))) {
                                if (nr == 1)
                                	erzieher.Erz1StaatKrz = null;
                                else
                                    erzieher.Erz2StaatKrz = null;
	    					} else {
	    						final Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
		    			    	if (nat == null)
		    			    		throw OperationError.NOT_FOUND.exception();
                                if (nr == 1)
                                	erzieher.Erz1StaatKrz = nat;
                                else
                                    erzieher.Erz2StaatKrz = nat;
	    					}
		    			}
                        case "erhaeltAnschreiben" -> erzieher.ErzAnschreiben = JSONMapper.convertToBoolean(value, true);
                        case "bemerkungen" -> erzieher.Bemerkungen = JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerErzAdr.col_Bemerkungen.datenlaenge());
                        default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(erzieher);
                conn.transactionCommit();
            } catch (final Exception e) {
    			if (e instanceof final WebApplicationException webAppException)
    				return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		} finally {
    			// Perform a rollback if necessary
    			conn.transactionRollback();
    		}
        }
        return Response.status(Status.OK).build();
	}


    /**
     * Setzt den Wohnort bei den Erzieherdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
     * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
     *
     * @param conn         die aktuelle Datenbankverbindung
     * @param erzieher     das Erzieher-DTO der Datenbank
     * @param wohnortID    die zu setzende Wohnort-ID
     * @param ortsteilID   die zu setzende Ortsteil-ID
     *
     * @throws WebApplicationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
     */
    private static void setWohnort(final DBEntityManager conn, final DTOSchuelerErzieherAdresse erzieher, final Long wohnortID, final Long ortsteilID) throws WebApplicationException {
    	if ((wohnortID != null) && (wohnortID < 0))
    		throw OperationError.CONFLICT.exception();
    	if ((ortsteilID != null) && (ortsteilID < 0))
    		throw OperationError.CONFLICT.exception();
		erzieher.ErzOrt_ID = wohnortID;
    	// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			final DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
	    	if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
	    		ortsteilIDNeu = null;
	    	}
		}
		erzieher.ErzOrt_ID = ortsteilIDNeu;
    }


}
