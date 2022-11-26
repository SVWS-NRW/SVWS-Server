package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.api.JSONMapper;
import de.nrw.schule.svws.core.data.schueler.SchuelerStammdaten;
import de.nrw.schule.svws.core.types.Geschlecht;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.schule.Nationalitaeten;
import de.nrw.schule.svws.core.types.schule.Verkehrssprache;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFahrschuelerart;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOHaltestellen;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKonfession;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOOrtsteil;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerFoto;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerStammdaten}.
 */
public class DataSchuelerStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerStammdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerStammdaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchueler} in einen Core-DTO {@link SchuelerStammdaten}.  
	 */
	private Function<DTOSchueler, SchuelerStammdaten> dtoMapper = (DTOSchueler schueler) -> {
    	SchuelerStammdaten daten = new SchuelerStammdaten();
    	// Basisdaten
		daten.id = schueler.ID;
		daten.foto = "";
		daten.nachname = schueler.Nachname;
		daten.zusatzNachname = schueler.ZusatzNachname;
		daten.vorname = schueler.Vorname;
		daten.alleVornamen = schueler.AlleVornamen;
		daten.geschlecht = schueler.Geschlecht.id;
		daten.geburtsdatum = schueler.Geburtsdatum;
		daten.geburtsort = schueler.Geburtsort;
		daten.geburtsname = schueler.Geburtsname;
		// Wohnort und Kontaktdaten
		daten.strassenname = schueler.Strassenname;
		daten.hausnummer = schueler.HausNr;
		daten.hausnummerZusatz = schueler.HausNrZusatz;
		daten.wohnortID = schueler.Ort_ID;
		daten.ortsteilID = schueler.Ortsteil_ID;
		daten.telefon = schueler.Telefon;
		daten.telefonMobil = schueler.Fax;
		daten.emailPrivat = schueler.Email;
		daten.emailSchule = schueler.SchulEmail;
		// Daten zur Staatsangehörigkeit und zur Religion
		daten.staatsangehoerigkeitID = schueler.StaatKrz == null ? null : schueler.StaatKrz.daten.iso3;
		daten.staatsangehoerigkeit2ID = schueler.StaatKrz2 == null ? null : schueler.StaatKrz2.daten.iso3;
		daten.religionID = schueler.Religion_ID;
		daten.druckeKonfessionAufZeugnisse = schueler.KonfDruck;
		daten.religionabmeldung = schueler.Religionsabmeldung;
		daten.religionanmeldung = schueler.Religionsanmeldung;
		// Daten zum Migrationshintergrund
		// TODO DB-Converter für boolean statt Boolean beim Migrationshintergrund
		daten.hatMigrationshintergrund = schueler.Migrationshintergrund == null ? false : schueler.Migrationshintergrund;
		daten.zuzugsjahr = schueler.JahrZuzug == null ? null : schueler.JahrZuzug.toString();
		daten.geburtsland = schueler.GeburtslandSchueler == null ? null : schueler.GeburtslandSchueler.daten.iso3;
		daten.verkehrspracheFamilie = schueler.VerkehrsspracheFamilie == null ? null : schueler.VerkehrsspracheFamilie.daten.kuerzel;
		daten.geburtslandVater = schueler.GeburtslandVater == null ? null : schueler.GeburtslandVater.daten.iso3;
		daten.geburtslandMutter = schueler.GeburtslandMutter == null ? null : schueler.GeburtslandMutter.daten.iso3;
		// Daten zur Sonderpädagogischen Förderung
//TODO Entscheidung abwarten, ob dies abschnittsweise gespeichern wird 			    			
//		daten.foerderschwerpunktID = schueler.Foerderschwerpunkt_ID;
//		daten.foerderschwerpunkt2ID = schueler.Foerderschwerpunkt2_ID;
//		daten.istAOSF = schueler.AOSF; // „Ausbildungsordnung Sonderpädagogische Förderung" bzw. „Verordnung über die sonderpädagogische Förderung, den Hausunterricht und die Schule für Kranke“
//		daten.istLernenZieldifferent = schueler.ZieldifferentesLernen;
		// Statusdaten
		daten.status = schueler.Status.bezeichnung;
		daten.istDuplikat = schueler.Duplikat;
		daten.fahrschuelerArtID = schueler.Fahrschueler_ID;
		daten.haltestelleID = schueler.Haltestelle_ID;
		daten.anmeldedatum = schueler.AnmeldeDatum;
		daten.aufnahmedatum = schueler.Aufnahmedatum;
		daten.istVolljaehrig = schueler.Volljaehrig; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.keineAuskunftAnDritte = schueler.KeineAuskunft;
		daten.istSchulpflichtErfuellt = schueler.SchulpflichtErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.istBerufsschulpflichtErfuellt = schueler.BerufsschulpflErf; // TODO ermittle die Information aus den anderen Schülerdaten
		daten.hatMasernimpfnachweis = schueler.MasernImpfnachweis;
		daten.erhaeltSchuelerBAFOEG = schueler.Bafoeg;
		daten.erhaeltMeisterBAFOEG = schueler.MeisterBafoeg;
		// Bemerkungen
		daten.bemerkungen = schueler.Bemerkungen;
		return daten;
	};

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
    	SchuelerStammdaten daten = dtoMapper.apply(schueler);
		DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
		if (schuelerFoto != null)
			daten.foto = schuelerFoto.FotoBase64;
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
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
		    			// Basisdaten
						case "id" -> {
							Long patch_id = JSONMapper.convertToLong(value, true);
							if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
								throw OperationError.BAD_REQUEST.exception();
						}
		    			case "foto" -> {
		    		    	String strData = JSONMapper.convertToString(value, true, true);
    	        			DTOSchuelerFoto schuelerFoto = conn.queryByKey(DTOSchuelerFoto.class, id);
    	        			if (schuelerFoto == null)
	    	        			schuelerFoto = new DTOSchuelerFoto(id);
    	        			String oldFoto = schuelerFoto.FotoBase64;
    	        	    	if (((strData == null) && (oldFoto == null)) || ((strData != null) && (strData.equals(oldFoto))))
    	        	    		return Response.status(Status.OK).build();
    	        	    	schuelerFoto.FotoBase64 = strData;
    	        	    	conn.transactionPersist(schuelerFoto);
		    			}
		    			case "nachname" -> schueler.Nachname = JSONMapper.convertToString(value, false, false);
		    			case "zusatzNachname" -> schueler.ZusatzNachname = JSONMapper.convertToString(value, false, true);
		    			case "vorname" -> schueler.Vorname = JSONMapper.convertToString(value, false, false);
		    			case "alleVornamen" -> schueler.AlleVornamen = JSONMapper.convertToString(value, false, true);
		    			case "geschlecht" -> {
		    				Geschlecht geschlecht = Geschlecht.fromValue(JSONMapper.convertToInteger(value, false));
		    				if (geschlecht == null)
		    					throw OperationError.CONFLICT.exception();
		    				schueler.Geschlecht = geschlecht;
		    			}
		    			case "geburtsdatum" -> schueler.Geburtsdatum = JSONMapper.convertToString(value, false, false);
		    			case "geburtsort" -> schueler.Geburtsort = JSONMapper.convertToString(value, false, true);
		    			case "geburtsname" -> schueler.Geburtsname = JSONMapper.convertToString(value, false, true);
		    			
		    			// Wohnort und Kontaktdaten
		    			case "strassenname" -> schueler.Strassenname = JSONMapper.convertToString(value, true, true);
		    			case "hausnummer" -> schueler.HausNr = JSONMapper.convertToString(value, true, true);
		    			case "hausnummerZusatz" -> schueler.HausNrZusatz = JSONMapper.convertToString(value, true, true);
		    			case "wohnortID" -> {
		    				setWohnort(schueler, JSONMapper.convertToLong(value, true), map.get("ortsteilID") == null ? schueler.Ortsteil_ID : ((Long)map.get("ortsteilID")));
		    			}
		    			case "ortsteilID" -> {
		    				setWohnort(schueler, map.get("wohnortID") == null ? schueler.Ort_ID : ((Long)map.get("wohnortID")), JSONMapper.convertToLong(value, true));
		    			}
		    			case "telefon" -> schueler.Telefon = JSONMapper.convertToString(value, true, true);
		    			case "telefonMobil" -> schueler.Fax = JSONMapper.convertToString(value, true, true);
		    			case "emailPrivat" -> schueler.Email = JSONMapper.convertToString(value, true, true);
		    			case "emailSchule" -> schueler.SchulEmail = JSONMapper.convertToString(value, true, true);
		    			
		    			// Daten zur Staatsangehörigkeit und zur Religion
		    			case "staatsangehoerigkeitID" -> {
		    		    	String staatsangehoerigkeitID = JSONMapper.convertToString(value, true, true);
		    		    	if ((staatsangehoerigkeitID == null) || ("".equals(staatsangehoerigkeitID))) {
	    						schueler.StaatKrz = null;
	    					} else {
	    						Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeitID);
		    			    	if (nat == null)
		    			    		throw OperationError.NOT_FOUND.exception();
		    					schueler.StaatKrz = nat;
	    					}
		    			}
		    			case "staatsangehoerigkeit2ID" -> {
		    		    	String staatsangehoerigkeit2ID = JSONMapper.convertToString(value, true, true);
		    		    	if ((staatsangehoerigkeit2ID == null) || ("".equals(staatsangehoerigkeit2ID))) {
	    						schueler.StaatKrz2 = null;
	    					} else {
	    						Nationalitaeten nat = Nationalitaeten.getByISO3(staatsangehoerigkeit2ID);
		    			    	if (nat == null)
		    			    		throw OperationError.NOT_FOUND.exception();
		    					schueler.StaatKrz2 = nat;
	    					}
		    			}
		    			case "religionID" -> {
		    				Long religionID = JSONMapper.convertToLong(value, false);
		    		    	if ((religionID != null) && (religionID < 0))
		    		    		throw OperationError.CONFLICT.exception();    	
	    			    	DTOKonfession rel = conn.queryByKey(DTOKonfession.class, religionID);
	    			    	if (rel == null)
	    			    		throw OperationError.NOT_FOUND.exception();
	    					schueler.Religion_ID = religionID; 
		    			}
		    			case "druckeKonfessionAufZeugnisse" -> schueler.KonfDruck = JSONMapper.convertToBoolean(value, false);
		    			case "religionabmeldung" -> schueler.Religionsabmeldung = JSONMapper.convertToString(value, true, true);
		    			case "religionanmeldung" -> schueler.Religionsanmeldung = JSONMapper.convertToString(value, true, true);
		    			
		    			// Daten zum Migrationshintergrund
		    			case "hatMigrationshintergrund" -> schueler.Migrationshintergrund = JSONMapper.convertToBoolean(value, false); 
		    			case "zuzugsjahr" -> {
		    		    	String text = JSONMapper.convertToString(value, true, true);
		    		    	Integer jahr = null;
		    		    	if ((text != null) && (!"".equals(text))) {
		    		        	try {
		    		        		jahr = Integer.parseUnsignedInt(text);
		    		        		if ((jahr <= 1900) || (jahr > 3000))   // TODO Bestimme das aktuelle Jahr für die obere Grenze des Bereichs
		    				    		throw OperationError.BAD_REQUEST.exception();
		    		        	} catch (@SuppressWarnings("unused") NumberFormatException e) {
		    			    		throw OperationError.BAD_REQUEST.exception();
		    		        	}    		
		    		    	}
		    				schueler.JahrZuzug = jahr; 
		    			}
		    			case "geburtsland" -> {
		    				String strData = JSONMapper.convertToString(value, true, true);
    						Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
	    			    	if (nat == null)
	    			    		throw OperationError.NOT_FOUND.exception();
		    				schueler.GeburtslandSchueler = nat;
		    			}
		    			case "verkehrspracheFamilie" -> {
		    				String strData = JSONMapper.convertToString(value, true, true);
		    				Verkehrssprache vs = Verkehrssprache.getByKuerzelAuto(strData);
		    				if (vs == null)
	    			    		throw OperationError.NOT_FOUND.exception();
		    				schueler.VerkehrsspracheFamilie = vs;
		    			}
		    			case "geburtslandVater" -> {
		    				String strData = JSONMapper.convertToString(value, true, true);
    						Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
	    			    	if (nat == null)
	    			    		throw OperationError.NOT_FOUND.exception();
		    				schueler.GeburtslandVater = nat;
		    			}
		    			case "geburtslandMutter" -> {
		    				String strData = JSONMapper.convertToString(value, true, true);
    						Nationalitaeten nat = Nationalitaeten.getByISO3(strData);
	    			    	if (nat == null)
	    			    		throw OperationError.NOT_FOUND.exception();
		    				schueler.GeburtslandMutter = nat;
		    			}
		    			
		    			// Daten zur Sonderpädagogischen Förderung
//TODO Entscheidung abwarten, ob dies abschnittsweise gespeichern wird 			    			
//		    			case "foerderschwerpunktID" -> {
//		    		    	Long fid = JSONMapper.convertToLong(value, true);
//		    		    	if ((fid != null) && (fid < 0))
//		    		    		OperationError.CONFLICT.throwException();    	
//	    					if (fid == null) {
//	    						schueler.Foerderschwerpunkt_ID = null;
//	    					} else {
//		    					DTOFoerderschwerpunkt f = conn.queryByKey(DTOFoerderschwerpunkt.class, fid);
//		    			    	if (f == null)
//		    			    		OperationError.NOT_FOUND.throwException();
//		    			    	schueler.Foerderschwerpunkt_ID = fid;
//	    					}
//		    			}
//		    			case "foerderschwerpunkt2ID" -> {
//		    		    	Long fid = JSONMapper.convertToLong(value, true);
//		    		    	if ((fid != null) && (fid < 0))
//		    		    		OperationError.CONFLICT.throwException();    	
//	    					if (fid == null) {
//	    						schueler.Foerderschwerpunkt2_ID = null;
//	    					} else {
//		    					DTOFoerderschwerpunkt f = conn.queryByKey(DTOFoerderschwerpunkt.class, fid);
//		    			    	if (f == null)
//		    			    		OperationError.NOT_FOUND.throwException();
//		    			    	schueler.Foerderschwerpunkt2_ID = fid;
//	    					}
//		    			}
//		    			case "istAOSF" -> schueler.AOSF = JSONMapper.convertToBoolean(value, false);
//		    			case "istLernenZieldifferent" -> schueler.ZieldifferentesLernen = JSONMapper.convertToBoolean(value, false);
		    			
		    			// Statusdaten
		    			case "status" -> {
		    				SchuelerStatus s = SchuelerStatus.fromBezeichnung(JSONMapper.convertToString(value, false, false));
		    				if (s == null)
		    					throw OperationError.BAD_REQUEST.exception();
		    				schueler.Status = s;
		    			}
		    			case "fahrschuelerArtID" -> {
		    		    	Long fid = JSONMapper.convertToLong(value, true);
		    		    	if ((fid != null) && (fid < 0))
		    		    		throw OperationError.CONFLICT.exception();
	    					if (fid == null) {
	    						schueler.Fahrschueler_ID = null;
	    					} else {
		    					DTOFahrschuelerart f = conn.queryByKey(DTOFahrschuelerart.class, fid);
		    			    	if (f == null)
		    			    		throw OperationError.NOT_FOUND.exception();
		    			    	schueler.Fahrschueler_ID = fid;
	    					}
		    			}
		    			case "haltestelleID" -> {
		    		    	Long hid = JSONMapper.convertToLong(value, true);
		    		    	if ((hid != null) && (hid < 0))
		    		    		throw OperationError.CONFLICT.exception();    	
	    					if (hid == null) {
	    						schueler.Haltestelle_ID = null;
	    					} else {
		    					DTOHaltestellen h = conn.queryByKey(DTOHaltestellen.class, hid);
		    			    	if (h == null)
		    			    		throw OperationError.NOT_FOUND.exception();
		    			    	schueler.Haltestelle_ID = hid;
	    					}
		    			}
		    			case "anmeldedatum" -> {
		    				schueler.AnmeldeDatum = JSONMapper.convertToString(value, false, false);
		    			}
		    			case "aufnahmedatum" -> {
		    				String aufnahmedatum = JSONMapper.convertToString(value, true, true);
		    				schueler.Aufnahmedatum = "".equals(aufnahmedatum) ? null : aufnahmedatum;
		    			}
		    			case "istVolljaehrig" -> schueler.Volljaehrig = JSONMapper.convertToBoolean(value, false);
		    			case "istSchulpflichtErfuellt" -> schueler.SchulpflichtErf = JSONMapper.convertToBoolean(value, false);
		    			case "istBerufsschulpflichtErfuellt" -> schueler.BerufsschulpflErf = JSONMapper.convertToBoolean(value, false);
		    			case "hatMasernimpfnachweis" -> schueler.MasernImpfnachweis = JSONMapper.convertToBoolean(value, false);
		    			case "keineAuskunftAnDritte" -> schueler.KeineAuskunft = JSONMapper.convertToBoolean(value, false);
		    			case "erhaeltSchuelerBAFOEG" -> schueler.Bafoeg = JSONMapper.convertToBoolean(value, false);
		    			case "erhaeltMeisterBAFOEG" -> schueler.MeisterBafoeg = JSONMapper.convertToBoolean(value, false);
		    			case "istDuplikat" -> schueler.Duplikat = JSONMapper.convertToBoolean(value, false);
		    			
		    			// Bemerkungen
		    			case "bemerkungen" -> schueler.Bemerkungen = JSONMapper.convertToString(value, true, true);
		    			
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


    /**
     * Setzt den Wohnort bei den Schülerdaten und prüft dabei die Angabe des Ortsteils auf Korrektheit in Bezug auf die Ortsteile
     * in der Datenbank. Ggf. wird der Ortsteil auf null gesetzt.
     * 
     * @param schueler     das Schüler-DTO der Datenbank 
     * @param wohnortID    die zu setzende Wohnort-ID
     * @param ortsteilID   die zu setzende Ortsteil-ID
     * 
     * @throws WebApplicationException   eine Exception mit dem HTTP-Fehlercode 409, falls die ID negative und damit ungültig ist
     */
    private void setWohnort(DTOSchueler schueler, Long wohnortID, Long ortsteilID) throws WebApplicationException {
    	if ((wohnortID != null) && (wohnortID < 0))
    		throw OperationError.CONFLICT.exception();
    	if ((ortsteilID != null) && (ortsteilID < 0))
    		throw OperationError.CONFLICT.exception();
		schueler.Ort_ID = wohnortID;
    	// Prüfe, ob die Ortsteil ID in Bezug auf die WohnortID gültig ist, wähle hierbei null-Verweise auf die K_Ort-Tabelle als überall gültig
		Long ortsteilIDNeu = ortsteilID;
		if (ortsteilIDNeu != null) {
			DTOOrtsteil ortsteil = conn.queryByKey(DTOOrtsteil.class, ortsteilIDNeu);
	    	if ((ortsteil == null) || ((ortsteil.Ort_ID != null) && (!ortsteil.Ort_ID.equals(wohnortID)))) {
	    		ortsteilIDNeu = null;
	    	}
		}
		schueler.Ortsteil_ID = ortsteilIDNeu;    	
    }

	
}
