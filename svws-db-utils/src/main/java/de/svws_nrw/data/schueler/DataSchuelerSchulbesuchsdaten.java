package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerSchulbesuchsdaten}.
 */
public final class DataSchuelerSchulbesuchsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerSchulbesuchsdaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchsdaten(final DBEntityManager conn) {
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
	public Response get(final Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
    	if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();
    	final Map<String, DTOEntlassarten> entlassgruende = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
    	final SchuelerSchulbesuchsdaten daten = new SchuelerSchulbesuchsdaten();
    	// Basisdaten
		daten.id = schueler.ID;
		// Informationen zu der Schule, die vor der Aufnahme besucht wurde
		daten.vorigeSchulnummer = schueler.LSSchulNr;
		daten.vorigeAllgHerkunft = schueler.LSSchulform;
		daten.vorigeEntlassdatum = schueler.LSSchulEntlassDatum;
		daten.vorigeEntlassjahrgang = schueler.LSJahrgang;
		daten.vorigeArtLetzteVersetzung = schueler.LSVersetzung;
		daten.vorigeBemerkung = schueler.LSBemerkung;
		final DTOEntlassarten tmpVorigeEntlassgrund = entlassgruende.get(schueler.LSEntlassgrund);
		daten.vorigeEntlassgrundID = tmpVorigeEntlassgrund == null ? null : tmpVorigeEntlassgrund.ID;
		daten.vorigeAbschlussartID = schueler.LSEntlassArt;
		// Informationen zu der Entlassung von der eigenen Schule
		daten.entlassungDatum = schueler.Entlassdatum;
		daten.entlassungJahrgang = schueler.Entlassjahrgang;
		final DTOEntlassarten tmpEntlassungGrund = entlassgruende.get(schueler.Entlassgrund);
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
		final List<DTOSchuelerMerkmale> dtoMerkmale = conn.queryNamed("DTOSchuelerMerkmale.schueler_id", id, DTOSchuelerMerkmale.class);
		for (final DTOSchuelerMerkmale dtoMerkmal : dtoMerkmale) {
			final SchuelerSchulbesuchMerkmal merkmal = new SchuelerSchulbesuchMerkmal();
			merkmal.id = dtoMerkmal.ID;
			merkmal.datumVon = dtoMerkmal.DatumVon;
			merkmal.datumBis = dtoMerkmal.DatumBis;
			daten.merkmale.add(merkmal);
		}
		// Informationen zu allen bisher besuchten Schulen
		final List<DTOSchuelerAbgaenge> dtoBisherigeSchulen = conn.queryNamed("DTOSchuelerAbgaenge.schueler_id", id, DTOSchuelerAbgaenge.class);
		for (final DTOSchuelerAbgaenge dtoBisherigeSchule : dtoBisherigeSchulen) {
			final SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
			bisherigeSchule.schulnummer = dtoBisherigeSchule.AbgangsSchulNr;
			bisherigeSchule.schulgliederung = dtoBisherigeSchule.LSSGL;
			final DTOEntlassarten tmpBisherigeEntlassungGrund = entlassgruende.get(dtoBisherigeSchule.BemerkungIntern);
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
	public Response patch(final Long id, final InputStream is) {
    	final Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
	    	if (schueler == null)
	    		throw OperationError.NOT_FOUND.exception();
	    	final Map<Long, DTOEntlassarten> entlassgruende = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	    	for (final Entry<String, Object> entry : map.entrySet()) {
	    		final String key = entry.getKey();
	    		final Object value = entry.getValue();
	    		switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}

	    			// Informationen zu der Schule, die vor der Aufnahme besucht wurde
	    			case "vorigeSchulnummer" -> schueler.LSSchulNr = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_LSSchulNr.datenlaenge());
	    			case "vorigeAllgHerkunft" -> { /* TODO zur Zeit noch nicht implementiert */ }
	    			case "vorigeEntlassdatum" -> schueler.LSSchulEntlassDatum = JSONMapper.convertToString(value, true, true, null);
	    			case "vorigeEntlassjahrgang" -> schueler.LSJahrgang = JSONMapper.convertToString(value, true, true, null);    // TODO Katalog ...
	    			case "vorigeArtLetzteVersetzung" -> schueler.LSVersetzung = JSONMapper.convertToString(value, true, true, null); // TODO Katalog
	    			case "vorigeBemerkung" -> schueler.LSBemerkung = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_LSBemerkung.datenlaenge());
	    			case "vorigeEntlassgrundID" -> {
	    				final Long vorigeEntlassgrundID = JSONMapper.convertToLong(value, true);
	    				if (vorigeEntlassgrundID == null) {
	    					schueler.LSEntlassgrund = null;
	    				} else {
	    					final DTOEntlassarten tmpVorigeEntlassgrund = entlassgruende.get(vorigeEntlassgrundID);
	    					if (tmpVorigeEntlassgrund == null)
	    						throw OperationError.CONFLICT.exception();
	    					schueler.LSEntlassgrund = tmpVorigeEntlassgrund.Bezeichnung;
	    				}
	    			}
	    			case "vorigeAbschlussartID" -> schueler.LSEntlassArt = JSONMapper.convertToString(value, true, true, null);   // TODO Katalog ...

	    			// Informationen zu der Entlassung von der eigenen Schule
	    			case "entlassungDatum" -> schueler.Entlassdatum = JSONMapper.convertToString(value, true, true, null);
	    			case "entlassungJahrgang" -> schueler.Entlassjahrgang = JSONMapper.convertToString(value, true, true, null);    // TODO Katalog ...
	    			case "entlassungGrundID" -> {
	    				final Long entlassungGrundID = JSONMapper.convertToLong(value, true);
	    				if (entlassungGrundID == null) {
	    					schueler.Entlassgrund = null;
	    				} else {
	    					final DTOEntlassarten tmpEntlassungGrund = entlassgruende.get(entlassungGrundID);
	    					if (tmpEntlassungGrund == null)
	    						throw OperationError.CONFLICT.exception();
	    					schueler.Entlassgrund = tmpEntlassungGrund.Bezeichnung;
	    				}
	    			}
	    			case "entlassungAbschlussartID" -> schueler.Entlassart = JSONMapper.convertToString(value, true, true, null);   // TODO Katalog ...

	    			// Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule
	    			case "aufnehmdendSchulnummer" -> schueler.SchulwechselNr = JSONMapper.convertToString(value, true, true, Schema.tab_Schueler.col_SchulwechselNr.datenlaenge());
	    			case "aufnehmdendWechseldatum" -> schueler.Schulwechseldatum = JSONMapper.convertToString(value, true, true, null);
	    			case "aufnehmdendBestaetigt" -> schueler.WechselBestaetigt = JSONMapper.convertToBoolean(value, true);

	    			// Informationen zu der besuchten Grundschule
	    			case "grundschuleEinschulungsjahr" -> schueler.Einschulungsjahr = JSONMapper.convertToInteger(value, true); // TODO Überprüfung des Jahres
	    			case "grundschuleEinschulungsartID" -> schueler.Einschulungsart_ID = JSONMapper.convertToLong(value, true);   // TODO Katalog ...
	    			case "grundschuleJahreEingangsphase" -> schueler.EPJahre = JSONMapper.convertToInteger(value, true);   // TODO Auswahl auf 2 und 3 beschränken?
	    			case "grundschuleUebergangsempfehlungID" -> schueler.Uebergangsempfehlung_JG5 = JSONMapper.convertToString(value, true, false, null);   // TODO Katalog statkue_schueleruebergangsempfehlung5jg

	    			// Informationen zu dem Besuch der Sekundarstufe I
	    			case "sekIWechsel" -> schueler.JahrWechsel_SI = JSONMapper.convertToInteger(value, true);  // TODO Überprüfung des Jahres
	    			case "sekIErsteSchulform" -> schueler.ErsteSchulform_SI = JSONMapper.convertToString(value, true, false, null);   // TODO Katalog ...
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
    	}
    	return Response.status(Status.OK).build();
	}

}
