package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchMerkmal;
import de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchSchule;
import de.nrw.schule.svws.core.data.schueler.SchuelerSchulbesuchsdaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOEntlassarten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerSchulbesuchsdaten}.
 */
public class DataSchuelerSchulbesuchsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerSchulbesuchsdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchsdaten(DBEntityManager conn) {
		super(conn);
	}	

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
    	if (schueler == null)
			return OperationError.NOT_FOUND.getResponse();
    	Map<String, DTOEntlassarten> entlassgruende = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e)); 
    	SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
    	// Basisdaten
		daten.id = schueler.ID;
		// Informationen zu der Schule, die vor der Aufnahme besucht wurde
		daten.vorigeSchulnummer = schueler.LSSchulNr;
		// TODO daten.vorigeAllgHerkunft = schueler.;
		daten.vorigeEntlassdatum = schueler.LSSchulEntlassDatum;
		daten.vorigeEntlassjahrgang = schueler.LSJahrgang;
		daten.vorigeArtLetzteVersetzung = schueler.LSVersetzung;
		daten.vorigeBemerkung = schueler.LSBemerkung;
		DTOEntlassarten tmpVorigeEntlassgrund = entlassgruende.get(schueler.LSEntlassgrund);
		daten.vorigeEntlassgrundID = tmpVorigeEntlassgrund == null ? null : tmpVorigeEntlassgrund.ID;
		daten.vorigeAbschlussartID = schueler.LSEntlassArt;
		// Informationen zu der Entlassung von der eigenen Schule
		daten.entlassungDatum = schueler.Entlassdatum;
		daten.entlassungJahrgang = schueler.Entlassjahrgang;
		DTOEntlassarten tmpEntlassungGrund = entlassgruende.get(schueler.Entlassgrund);
		daten.entlassungGrundID = tmpEntlassungGrund == null ? null : tmpEntlassungGrund.ID;
		daten.entlassungAbschlussartID = schueler.Entlassart;
		// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule	
		daten.aufnehmdendSchulnummer = schueler.SchulwechselNr;
		daten.aufnehmdendWechseldatum = schueler.Schulwechseldatum;
		daten.aufnehmdendBestaetigt = schueler.WechselBestaetigt;
		// Informationen zu der besuchten Grundschule
		daten.grundschuleEinschulungsjahr = schueler.Einschulungsjahr;
		daten.grundschuleEinschulungsartID = schueler.Einschulungsart_ID;
		daten.grundschuleJahreEingangsphase = schueler.EPJahre;
		// TODO statkue_schueleruebergangsempfehlung5jg -> daten.grundschuleUebergangsempfehlungID = schueler.Uebergangsempfehlung_JG5;
		// Informationen zu dem Besuch der Sekundarstufe I
		daten.sekIWechsel = schueler.JahrWechsel_SI;
		daten.sekIErsteSchulform = schueler.ErsteSchulform_SI;
		daten.sekIIWechsel = schueler.JahrWechsel_SII;
		// Informationen zu besonderen Merkmalen für die Statistik
		List<DTOSchuelerMerkmale> dtoMerkmale = conn.queryNamed("DTOSchuelerMerkmale.schueler_id", id, DTOSchuelerMerkmale.class);
		for (DTOSchuelerMerkmale dtoMerkmal : dtoMerkmale) {
			SchuelerSchulbesuchMerkmal merkmal = new SchuelerSchulbesuchMerkmal();
			merkmal.id = dtoMerkmal.ID;
			merkmal.datumVon = dtoMerkmal.DatumVon;
			merkmal.datumBis = dtoMerkmal.DatumBis;
			daten.merkmale.add(merkmal);
		}
		// Informationen zu allen bisher besuchten Schulen
		List<DTOSchuelerAbgaenge> dtoBisherigeSchulen = conn.queryNamed("DTOSchuelerAbgaenge.schueler_id", id, DTOSchuelerAbgaenge.class);
		for (DTOSchuelerAbgaenge dtoBisherigeSchule : dtoBisherigeSchulen) {
			SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
			bisherigeSchule.schulnummer = dtoBisherigeSchule.AbgangsSchulNr;
			bisherigeSchule.schulgliederung = dtoBisherigeSchule.LSSGL;
			DTOEntlassarten tmpBisherigeEntlassungGrund = entlassgruende.get(dtoBisherigeSchule.BemerkungIntern);
			bisherigeSchule.entlassgrundID = tmpBisherigeEntlassungGrund == null ? null : tmpBisherigeEntlassungGrund.ID;
			bisherigeSchule.abschlussartID = dtoBisherigeSchule.LSEntlassArt;
			bisherigeSchule.organisationsFormID = dtoBisherigeSchule.OrganisationsformKrz;
			bisherigeSchule.datumVon = dtoBisherigeSchule.LSBeginnDatum;
			bisherigeSchule.datumBis = dtoBisherigeSchule.LSSchulEntlassDatum;
			bisherigeSchule.jahrgangVon = dtoBisherigeSchule.LSBeginnJahrgang;
			bisherigeSchule.jahrgangBis = dtoBisherigeSchule.LSJahrgang;
			daten.alleSchulen.add(bisherigeSchule);
		}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
	    		DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		    	if (schueler == null)
		    		throw OperationError.NOT_FOUND.exception();
		    	Map<Long, DTOEntlassarten> entlassgruende = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.ID, e -> e));
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}

		    			// Informationen zu der Schule, die vor der Aufnahme besucht wurde
		    			case "vorigeSchulnummer" -> schueler.LSSchulNr = JSONMapper.convertToString(value, true, true);
		    			case "vorigeAllgHerkunft" -> { }   // TODO zur Zeit noch nicht implementiert
		    			case "vorigeEntlassdatum" -> schueler.LSSchulEntlassDatum = JSONMapper.convertToString(value, true, true);
		    			case "vorigeEntlassjahrgang" -> schueler.LSJahrgang = JSONMapper.convertToString(value, true, true);    // TODO Katalog ...
		    			case "vorigeArtLetzteVersetzung" -> schueler.LSVersetzung = JSONMapper.convertToString(value, true, true);
		    			case "vorigeBemerkung" -> schueler.LSBemerkung = JSONMapper.convertToString(value, true, true);
		    			case "vorigeEntlassgrundID" -> {
		    				Long vorigeEntlassgrundID = JSONMapper.convertToLong(value, true);
		    				if (vorigeEntlassgrundID == null) {
		    					schueler.LSEntlassgrund = null;
		    				} else {
		    					DTOEntlassarten tmpVorigeEntlassgrund = entlassgruende.get(vorigeEntlassgrundID);
		    					if (tmpVorigeEntlassgrund == null)
		    						throw OperationError.CONFLICT.exception();
		    					schueler.LSEntlassgrund = tmpVorigeEntlassgrund.Bezeichnung;
		    				}
		    			}
		    			case "vorigeAbschlussartID" -> schueler.LSEntlassArt = JSONMapper.convertToString(value, true, true);   // TODO Katalog ...
		    			
		    			// Informationen zu der Entlassung von der eigenen Schule
		    			case "entlassungDatum" -> schueler.Entlassdatum = JSONMapper.convertToString(value, true, true);
		    			case "entlassungJahrgang" -> schueler.Entlassjahrgang = JSONMapper.convertToString(value, true, true);    // TODO Katalog ...
		    			case "entlassungGrundID" -> {
		    				Long entlassungGrundID = JSONMapper.convertToLong(value, true);
		    				if (entlassungGrundID == null) {
		    					schueler.Entlassgrund = null;
		    				} else {
		    					DTOEntlassarten tmpEntlassungGrund = entlassgruende.get(entlassungGrundID);
		    					if (tmpEntlassungGrund == null)
		    						throw OperationError.CONFLICT.exception();
		    					schueler.Entlassgrund = tmpEntlassungGrund.Bezeichnung;
		    				}
		    			}
		    			case "entlassungAbschlussartID" -> schueler.Entlassart = JSONMapper.convertToString(value, true, true);   // TODO Katalog ...
		    			
		    			// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule	
		    			case "aufnehmdendSchulnummer" -> schueler.SchulwechselNr = JSONMapper.convertToString(value, true, true);
		    			case "aufnehmdendWechseldatum" -> schueler.Schulwechseldatum = JSONMapper.convertToString(value, true, true);
		    			case "aufnehmdendBestaetigt" -> schueler.WechselBestaetigt = JSONMapper.convertToBoolean(value, true);
		    			
		    			// Informationen zu der besuchten Grundschule
		    			case "grundschuleEinschulungsjahr" -> schueler.Einschulungsjahr = JSONMapper.convertToInteger(value, true); // TODO Überprüfung des Jahres
		    			case "grundschuleEinschulungsartID" -> schueler.Einschulungsart_ID = JSONMapper.convertToLong(value, true);   // TODO Katalog ...
		    			case "grundschuleJahreEingangsphase" -> schueler.EPJahre = JSONMapper.convertToInteger(value, true);   // TODO Auswahl auf 2 und 3 beschränken?
		    			case "grundschuleUebergangsempfehlungID" -> schueler.Uebergangsempfehlung_JG5 = JSONMapper.convertToString(value, true, false);   // TODO Katalog statkue_schueleruebergangsempfehlung5jg
		    			
		    			// Informationen zu dem Besuch der Sekundarstufe I
		    			case "sekIWechsel" -> schueler.JahrWechsel_SI = JSONMapper.convertToInteger(value, true);  // TODO Überprüfung des Jahres
		    			case "sekIErsteSchulform" -> schueler.ErsteSchulform_SI = JSONMapper.convertToString(value, true, false);   // TODO Katalog ...
		    			case "sekIIWechsel" -> schueler.JahrWechsel_SII = JSONMapper.convertToInteger(value, true); // TODO Überprüfung des Jahres
		    			
		    			// Informationen zu besonderen Merkmalen für die Statistik
		    			case "merkmale" -> {
			    			// TODO Handhabung, der Patches für die Merkmale des Schülers - Getter und Patch über zusätzlichen API-Endpunkt oder über diesen?
		    				// TODO DTOSchuelerMerkmale ...
		    				// SchuelerSchulbesuchMerkmal merkmal = new SchuelerSchulbesuchMerkmal();
		    				// case "id"       -> dtoMerkmal.ID = (...);
		    				// case "datumVon" -> dtoMerkmal.DatumVon = (...);
		    				// case "datumBis" -> dtoMerkmal.DatumBis = (...);
		    			}
		    			
		    			// Informationen zu allen bisher besuchten Schulen
		    			case "alleSchulen" -> {
			    			// TODO Handhabung, der Patches für bisher besuchte Schulen - Getter und Patch über zusätzlichen API-Endpunkt oder über diesen?
		    				// TODO DTOSchuelerAbgaenge
	    					// SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
		    				// case "schulnummer"         -> dtoBisherigeSchule.AbgangsSchulNr = (...);
		    				// case "schulgliederung"     -> dtoBisherigeSchule.LSSGL = (...);
		    				// case "entlassgrundID"      -> dtoBisherigeSchule.BemerkungIntern = (...)
		    				// case "abschlussartID"      -> dtoBisherigeSchule.LSEntlassArt = (...);
		    				// case "organisationsFormID" -> dtoBisherigeSchule.OrganisationsformKrz = (...);
		    				// case "datumVon"            -> dtoBisherigeSchule.LSBeginnDatum = (...);
		    				// case "datumBis"            -> dtoBisherigeSchule.LSSchulEntlassDatum = (...);
		    				// case "jahrgangVon"         -> dtoBisherigeSchule.LSBeginnJahrgang = (...);
		    				// case "jahrgangBis"         -> dtoBisherigeSchule.LSJahrgang = (...);
		    			}
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(schueler);
		    	conn.transactionCommit();
    		} catch (Exception e) {
    			if (e instanceof WebApplicationException webAppException)
    				return webAppException.getResponse();
				return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    		} finally {
    			// Perform a rollback if necessary
    			conn.transactionRollback();
    		}
    	}
    	return Response.status(Status.OK).build();
	}

}
