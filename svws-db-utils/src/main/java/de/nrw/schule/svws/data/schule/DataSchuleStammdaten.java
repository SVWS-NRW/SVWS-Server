package de.nrw.schule.svws.data.schule;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.function.Function;

import de.nrw.schule.svws.core.data.schule.SchuleStammdaten;
import de.nrw.schule.svws.core.data.schule.SchulenKatalogEintrag;
import de.nrw.schule.svws.core.types.jahrgang.Jahrgaenge;
import de.nrw.schule.svws.core.types.schule.Religion;
import de.nrw.schule.svws.core.types.schule.Schulform;
import de.nrw.schule.svws.core.types.schule.Schulgliederung;
import de.nrw.schule.svws.core.utils.AdressenUtils;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.JSONMapper;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.nrw.schule.svws.db.dto.current.schild.erzieher.DTOErzieherart;
import de.nrw.schule.svws.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogAdressart;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogDatenschutz;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKonfession;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOSchwerpunkt;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOVermerkArt;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSportbefreiung;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchulformen;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOTeilstandorte;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuleStammdaten}.
 */
public class DataSchuleStammdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuleStammdaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuleStammdaten(DBEntityManager conn) {
		super(conn);
	}

	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOEigeneSchule} in einen Core-DTO {@link SchuleStammdaten}.  
	 */
	private Function<DTOEigeneSchule, SchuleStammdaten> dtoMapper = (DTOEigeneSchule schule) -> {
    	SchuleStammdaten daten = new SchuleStammdaten();
		daten.schulNr = schule.SchulNr;
		daten.schulform = schule.Schulform.daten.kuerzel;
		daten.bezeichnung1 = schule.Bezeichnung1;
		daten.bezeichnung2 = schule.Bezeichnung2;
		daten.bezeichnung3 = schule.Bezeichnung3;
		daten.strassenname = schule.Strassenname;
		daten.hausnummer = schule.HausNr;
		daten.hausnummerZusatz = schule.HausNrZusatz;
		daten.plz = schule.PLZ;
		daten.ort = schule.Ort;
		daten.telefon = schule.Telefon;
		daten.fax = schule.Fax;
		daten.email = schule.Email;
		daten.webAdresse = schule.WebAdresse;
		daten.idSchuljahresabschnitt = schule.Schuljahresabschnitts_ID;
		daten.anzJGS_Jahr = schule.AnzJGS_Jahr == null ? 1 : schule.AnzJGS_Jahr;
		daten.schuleAbschnitte.anzahlAbschnitte = schule.AnzahlAbschnitte;
		daten.schuleAbschnitte.abschnittBez = schule.AbschnittBez;
		daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt1);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 2)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt2);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 3)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt3);
		if (daten.schuleAbschnitte.anzahlAbschnitte >= 4)
			daten.schuleAbschnitte.bezAbschnitte.add(schule.BezAbschnitt4);
		daten.dauerUnterrichtseinheit = schule.DauerUnterrichtseinheit == null ? 45 : schule.DauerUnterrichtseinheit;
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
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	SchuleStammdaten daten = dtoMapper.apply(schule);
    	daten.abschnitte.addAll((new DataSchuljahresabschnitte(conn)).getAbschnitte());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	/**
	 * Bestimmt die Schulnummer der Schule
	 * 
	 * @return die Schulnummer oder null im Fehlerfall
	 */
	public Integer getSchulnummer() {
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		return null;
    	return schule.SchulNr;
	}
	
	
	/**
	 * Bestimmt die Schulnummer der Schule
	 * 
	 * @return Die HTTP-Response (NOT_FOUND im Fehlerfall)
	 */
	public Response getSchulnummerResponse() {
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(schule.SchulNr).build();
	}
	

	/**
	 * Gibt die Anzahl der Abschnitte pro Schuljahr
	 * 
	 * @param conn   die Datenbankverbindung, welche bei der Abfrage genutzt wird 
	 * 
	 * @return die Anzahl der Abschnitte pro Schuljahr
	 */
	public static int getAnzahlAbschnitte(DBEntityManager conn) {
    	DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if ((schule == null) || (schule.AnzahlAbschnitte == null))
    		return 2;   // Default-Wert
    	return schule.AnzahlAbschnitte;
	}
	

	@Override
	public Response patch(Long id, InputStream is) {
    	Map<String, Object> map = JSONMapper.toMap(is);
    	if (map.size() > 0) {
    		try {
    			conn.transactionBegin();
    			DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		    	if (schule == null)
		    		return OperationError.NOT_FOUND.getResponse();
		    	for (Entry<String, Object> entry : map.entrySet()) {
		    		String key = entry.getKey();
		    		Object value = entry.getValue();
		    		switch (key) {
		    			case "schulNr" -> throw OperationError.BAD_REQUEST.exception(); 
		    			case "schulform" -> throw OperationError.BAD_REQUEST.exception();
		    			case "bezeichnung1" -> schule.Bezeichnung1 = JSONMapper.convertToString(value, true, true);
		    			case "bezeichnung2" -> schule.Bezeichnung2 = JSONMapper.convertToString(value, true, true);
		    			case "bezeichnung3" -> schule.Bezeichnung3 = JSONMapper.convertToString(value, true, true);
		    			case "strassenname" -> schule.Strassenname = JSONMapper.convertToString(value, true, true);
		    			case "hausnummer" -> schule.HausNr = JSONMapper.convertToString(value, true, true);
		    			case "hausnummerZusatz" -> schule.HausNrZusatz = JSONMapper.convertToString(value, true, true);

		    			case "plz" -> schule.PLZ = JSONMapper.convertToString(value, true, true); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID
		    			case "ort" -> schule.Ort = JSONMapper.convertToString(value, true, true); // TODO Schema anpassen: Stakue-Ortskatalog nutzen -> Orts-ID

		    			case "telefon" -> schule.Telefon = JSONMapper.convertToString(value, true, true);
		    			case "fax" -> schule.Fax = JSONMapper.convertToString(value, true, true);
		    			case "email" -> schule.Email = JSONMapper.convertToString(value, true, true);
		    			case "webAdresse" -> schule.WebAdresse = JSONMapper.convertToString(value, true, true);
		    			
		    			case "idSchuljahresabschnitt" -> schule.Schuljahresabschnitts_ID = JSONMapper.convertToLong(value, false); // TODO ID des Schuljahresabschnittes überprüfen
		    			case "anzJGS_Jahr" -> schule.AnzJGS_Jahr = JSONMapper.convertToInteger(value, false); // TODO Abschnitt überprüfen

		    			case "schuleAbschnitte" -> {
		    				@SuppressWarnings("unchecked") // TODO check conversion
							Map<String, Object> mapAbschnitte = (Map<String, Object>)value;
		    				if (mapAbschnitte.containsKey("anzahlAbschnitte")) {
		    					Integer anzahlAbschnitte = JSONMapper.convertToInteger(mapAbschnitte.get("anzahlAbschnitte"), false);
		    					if ((anzahlAbschnitte < 1) || (anzahlAbschnitte > 4))
		    						throw OperationError.CONFLICT.exception();
		    					schule.AnzahlAbschnitte = anzahlAbschnitte; 
		    				}
		    				if (mapAbschnitte.containsKey("abschnittBez"))
		    					schule.AbschnittBez = JSONMapper.convertToString(mapAbschnitte.get("abschnittBez"), true, true);
		    				if (mapAbschnitte.containsKey("bezAbschnitte")) {
		    					List<?> bezAbschnitte = (List<?>)mapAbschnitte.get("bezAbschnitte");
		    					if (bezAbschnitte.size() != schule.AnzahlAbschnitte)
		    						throw OperationError.CONFLICT.exception();
		    					for (int i = 0; i < bezAbschnitte.size(); i++) {
		    						Object objBezeichnung = bezAbschnitte.get(i);
		    						if (!(objBezeichnung instanceof String))
		    							throw OperationError.BAD_REQUEST.exception();
		    						switch (i) {
		    							case 0 -> schule.BezAbschnitt1 = (String)objBezeichnung;
		    							case 1 -> schule.BezAbschnitt2 = (String)objBezeichnung;
		    							case 2 -> schule.BezAbschnitt3 = (String)objBezeichnung;
		    							case 3 -> schule.BezAbschnitt4 = (String)objBezeichnung;
		    						}
		    					}
		    				}
		    			}

		    			case "dauerUnterrichtseinheit" -> schule.DauerUnterrichtseinheit = JSONMapper.convertToInteger(value, false); // TODO Dauer in Minuten prüfen, evtl. einschränken
		    			case "abschnitte" -> throw OperationError.BAD_REQUEST.exception();
		    			
		    			default -> throw OperationError.BAD_REQUEST.exception();
		    		}
		    	}
		    	conn.transactionPersist(schule);
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
	 * Ermittelt das Schullogo.
	 * 
	 * @return Die HTTP-Response der Get-Operation
	 */
	public Response getSchullogo() {
    	Schule schule = Schule.query(conn);
    	if ((schule == null) || (schule.dto == null))
    		return OperationError.NOT_FOUND.getResponse();
    	String daten = "\"" + schule.dto.SchulLogoBase64 + "\"";
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	
	/**
	 * Ersetzt das Schullogo. 
	 * 
	 * @param is            der {@link InputStream} mit dem JSON-Patch für das Logo
	 * 
	 * @return Die HTTP-Response der Patch-Operation
	 */
	public Response putSchullogo(InputStream is) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
    	if (schule == null)
    		throw OperationError.NOT_FOUND.exception();
    	schule.SchulLogoBase64 = JSONMapper.toString(is);
    	conn.persist(schule);
		return Response.ok().build();
	}
	
	
	/**
	 * Initialisiert das Schema mit der angebenen Schulnummer
	 * 
	 * @param schulnummer   die Schulnummer der anzulegenden Schule
	 * 
	 * @return die HTTP-Response mit den Stammdaten der Schule
	 */
	public Response init(int schulnummer) {
		// Prüfe, ob bereits ein Eintrag in der Tabelle EigeneSchule vorliegt...
		DTOEigeneSchule eigeneSchule = conn.querySingle(DTOEigeneSchule.class);
		if (eigeneSchule != null)
			return OperationError.CONFLICT.getResponse("Das Datenbank-Schema kann nicht mit einer Schule initialisiert werden, da es bereits einen Schuleintrag enthält.");
		// Prüfe, ob die Schulnummer im Katalog der Schulen vorkommt.
		List<SchulenKatalogEintrag> katalogSchulen = DataKatalogSchulen.getKatalog();
		SchulenKatalogEintrag schulEintrag = null;
		for (SchulenKatalogEintrag eintrag : katalogSchulen) {
			if (eintrag.SchulNr.equals("" + schulnummer)) {
				schulEintrag = eintrag;
				break;
			}
		}
		if (schulEintrag == null)
			return OperationError.NOT_FOUND.getResponse("Keine Schule mit der Schulnummer " + schulnummer + " im Katalog der Schulen gefunden.");
		// Bestimme das aktuelle Datum
		LocalDate date = LocalDate.now();
		int month = date.getMonthValue();
		int year = date.getYear();
		// Lege den ersten Schuljahresabschnitt an
		int schuljahr = month > 7 ? year : year - 1;
		int abschnitt = month > 2 && month < 8 ? 2 : 1;
		DTOSchuljahresabschnitte schuljahresabschnitt = new DTOSchuljahresabschnitte(1L, schuljahr, abschnitt);
		conn.persist(schuljahresabschnitt);
		// Initialisiere die Daten in der Tabelle EigeneSchule
		eigeneSchule = new DTOEigeneSchule(1L);
		eigeneSchule.Schulform = Schulform.getByNummer(schulEintrag.SF);
		eigeneSchule.SchulformNr = eigeneSchule.Schulform.daten.nummer;
		eigeneSchule.SchulformBez = eigeneSchule.Schulform.daten.bezeichnung;
		eigeneSchule.SchultraegerArt = schulEintrag.ArtDerTraegerschaft;
		eigeneSchule.SchultraegerNr = schulEintrag.SchultraegerNr;
		eigeneSchule.SchulNr = Integer.parseInt(schulEintrag.SchulNr);
		eigeneSchule.Bezeichnung1 = schulEintrag.ABez1;
		eigeneSchule.Bezeichnung2 = schulEintrag.ABez2;
		eigeneSchule.Bezeichnung3 = schulEintrag.ABez3;
		String[] strasse = AdressenUtils.splitStrasse(schulEintrag.Strasse);
		eigeneSchule.Strassenname = strasse[0];
		eigeneSchule.HausNr = strasse[1];
		eigeneSchule.HausNrZusatz = strasse[2];
		eigeneSchule.PLZ = schulEintrag.PLZ;
		eigeneSchule.Ort = schulEintrag.Ort;
		eigeneSchule.Telefon = schulEintrag.TelVorw + " " + schulEintrag.Telefon;
		eigeneSchule.Fax = schulEintrag.FaxVorw + " " + schulEintrag.Fax;
		eigeneSchule.Email = schulEintrag.Email;
		eigeneSchule.Ganztags = Integer.parseInt(schulEintrag.Ganztagsbetrieb) > 0;
		eigeneSchule.Schuljahresabschnitts_ID = schuljahresabschnitt.ID;
		eigeneSchule.AnzahlAbschnitte = 2;
		eigeneSchule.Fremdsprachen = null;
		eigeneSchule.JVAZeigen = null;
		eigeneSchule.RefPaedagogikZeigen = null;
		eigeneSchule.AnzJGS_Jahr = null;
		eigeneSchule.AbschnittBez = "Halbjahr";
		eigeneSchule.BezAbschnitt1 = "1. Halbjahr";
		eigeneSchule.BezAbschnitt2 = "2. Halbjahr";
		eigeneSchule.BezAbschnitt3 = null;
		eigeneSchule.BezAbschnitt4 = null;
		eigeneSchule.IstHauptsitz = true;
		conn.persist(eigeneSchule);
		
		// Der Hauptstandort einrichten
		DTOTeilstandorte teilstandort = new DTOTeilstandorte("A");
		teilstandort.PLZ = schulEintrag.PLZ;
        teilstandort.Ort = schulEintrag.Ort;
        teilstandort.Strassenname = strasse[0];
        teilstandort.HausNr = strasse[1];
        teilstandort.HausNrZusatz = strasse[2];
        teilstandort.Bemerkung = "Hauptstandort";
        teilstandort.Kuerzel = "";
        conn.persist(teilstandort);
		
        // Einrichten der Schulgliederung in EigeneSchule_Schulformen - je nach Schulform
        DTOSchulformen schulgliederung = new DTOSchulformen(1L);
        Schulgliederung sgl = switch (eigeneSchule.Schulform) {
            case BK, SB -> Schulgliederung.A01;
            case SG, SR, V, PS, S, KS, SK, R, H, GE, G, FW, HI, WF -> Schulgliederung.DEFAULT;
            case GM -> Schulgliederung.GRH;
            case GY -> Schulgliederung.GY9;
            case WB -> Schulgliederung.G02;
        };
        schulgliederung.SGL = sgl.daten.kuerzel;
        schulgliederung.SF_SGL = eigeneSchule.Schulform.daten.kuerzel + "" + sgl.daten.kuerzel;
        schulgliederung.Schulform = sgl.daten.beschreibung;
        schulgliederung.Sortierung = 1;
        schulgliederung.BKIndex = sgl.daten.bkIndex;
        schulgliederung.Schulform2 = sgl.daten.kuerzel + ": " + sgl.daten.beschreibung;
        conn.persist(schulgliederung);
        
		// TODO Grundlegende Fächer - je nach Schulform - einrichten
        
		// TODO Kursarten - je nach Schulform - einrichten
        
		// Einrichten der Jahrgänge - je nach Schulform
        Vector<DTOJahrgang> dtoJahrgaenge = new Vector<>();
        List<Jahrgaenge> jahrgaenge = Jahrgaenge.get(eigeneSchule.Schulform);
        for (int i = 0; i < jahrgaenge.size(); i++) {
        	Jahrgaenge jg = jahrgaenge.get(i);
        	DTOJahrgang dto = new DTOJahrgang((long)i + 1);
        	dto.InternKrz = jg.daten.kuerzel;
        	dto.GueltigVon = null;
        	dto.GueltigBis = null;
        	dto.ASDJahrgang = jg.daten.kuerzel;
        	dto.ASDBezeichnung = jg.getBezeichnung(eigeneSchule.Schulform);
        	dto.Sichtbar = true;
        	dto.Sortierung = i + 1;
        	dto.IstChronologisch = true;
        	dto.Kurzbezeichnung = jg.daten.kuerzel;
        	dto.Sekundarstufe = null;        // TODO Core-Type Jahrgaenge erweitern?
        	dto.Gliederung = null;           // TODO   
        	dto.AnzahlRestabschnitte = null; // TODO Core-Type Jahrgaenge erweitern?
        	dto.Folgejahrgang_ID = null;     // TODO Core-Type Jahrgaenge erweitern?
        	dtoJahrgaenge.add(dto);
        }
        conn.persistRange(dtoJahrgaenge, 0, dtoJahrgaenge.size() - 1);
		
		// TODO K_Adressart mit Betrieb füllen
        DTOKatalogAdressart addressart = new DTOKatalogAdressart(1L, "Betrieb");
        addressart.Sortierung = 1;
        conn.persist(addressart);
        
		// TODO K_Beschaeftigungsart mit Ausbildung und Praktikum füllen
        Vector<DTOBeschaeftigungsart> beschaeftigungsart = new Vector<>();
        beschaeftigungsart.add(new DTOBeschaeftigungsart(1L, "Ausbildung"));
        beschaeftigungsart.add(new DTOBeschaeftigungsart(2L, "Praktikum"));
        for (int i = 0; i < beschaeftigungsart.size(); i++)
          	beschaeftigungsart.get(i).Sortierung = i+1;
        conn.persistRange(beschaeftigungsart, 0, 1);
        
		// TODO K_Datenschutz mit Verwendung Foto
        DTOKatalogDatenschutz foto = new DTOKatalogDatenschutz(1L, "Verwendung Foto", true, 32000);
        foto.Schluessel = "FOTO";
        foto.PersonArt = "S";
        conn.persist(foto);
        
        
		// TODO K_EinschulgungsArt normal, vorzeitig und zurückgestellt
        
		// TODO K_Entlassgrund mit "Schulpflicht endet", "Normales Abschluss", "Ohne Angabe" und "Wechsel zu anderer Schule"
		
        // K_Erzieherart mit den Vorgaben von Schild-NRW befüllen
		Vector<DTOErzieherart> erzieherarten = new Vector<>(); 
		erzieherarten.add(new DTOErzieherart(1L, "Vater"));
        erzieherarten.add(new DTOErzieherart(2L, "Mutter"));
        erzieherarten.add(new DTOErzieherart(3L, "Schüler ist volljährig"));
        erzieherarten.add(new DTOErzieherart(4L, "Schülerin ist volljährig"));
        erzieherarten.add(new DTOErzieherart(5L, "Eltern"));
        erzieherarten.add(new DTOErzieherart(6L, "Sonstige"));
        for (int i = 0; i < erzieherarten.size(); i++)
            erzieherarten.get(i).Sortierung = i+1;
        conn.persistRange(erzieherarten, 0, 5);
        
        // TODO K-Ort aus der Default-Daten-Tabelle befüllen
        
        // TODO K_Religion aus dem Core-Type befüllen
        Vector<DTOKonfession> dtoKonfession = new Vector<>();
        Religion[] konfession = Religion.values();
        for (int i = 0; i < konfession.length; i++) {
            Religion kon = konfession[i];
            DTOKonfession dto = new DTOKonfession((long)i + 1, kon.daten.bezeichnung);
            dto.StatistikKrz = kon.daten.kuerzel;
            dto.Sortierung = i + 1;
            dtoKonfession.add(dto);
        }
        conn.persistRange(dtoKonfession, 0, dtoKonfession.size() - 1);
        
        // TODO K_Schule mit Schulen aus dem sonstigen Ausland, den Bundesländern und Nachbarländern, Keine Schul und der eigenen Schule befüllen (Core-Type)
        
        // K_Schwerpunkte befüllen
        Vector<DTOSchwerpunkt> schwerpunkte = new Vector<>();
        schwerpunkte.add(new DTOSchwerpunkt(1L, "naturwissenschaftlich-technisch"));
        schwerpunkte.add(new DTOSchwerpunkt(2L, "sozialwissenschaftlich"));
        schwerpunkte.add(new DTOSchwerpunkt(3L, "musisch-künstlerisch"));
        schwerpunkte.add(new DTOSchwerpunkt(4L, "fremdsprachlich"));
        for (int i = 0; i < schwerpunkte.size(); i++)
            schwerpunkte.get(i).Sortierung = i+1;
        conn.persistRange(schwerpunkte, 0, 3);
		
        //K_Sportbefreiung mit einem Beispiel befüllen
        DTOSportbefreiung sportbefreiung = new DTOSportbefreiung(1L, "temporär - Schwimmen");
        sportbefreiung.Sortierung = 1;
        conn.persist(sportbefreiung);
        
        // K_Telefonart mit den Schild-NRW-Vorgaben befüllen
        Vector<DTOTelefonArt> telefonArten = new Vector<>();
        telefonArten.add(new DTOTelefonArt(1L, "Eltern"));
        telefonArten.add(new DTOTelefonArt(2L, "Mutter"));
        telefonArten.add(new DTOTelefonArt(3L, "Vater"));
        telefonArten.add(new DTOTelefonArt(4L, "Schüler/-in"));
        telefonArten.add(new DTOTelefonArt(5L, "(sonst.) gesetzl. Vertreter"));
        telefonArten.add(new DTOTelefonArt(6L, "Notfallnummer"));
        telefonArten.add(new DTOTelefonArt(7L, "Festnetznummer"));
        telefonArten.add(new DTOTelefonArt(8L, "Mobilnummer"));
        telefonArten.add(new DTOTelefonArt(9L, "Fax-Nummer"));     
        for (int i = 0; i < telefonArten.size(); i++)
            telefonArten.get(i).Sortierung = i+1;
        conn.persistRange(telefonArten, 0, 8);
        
        // K_Vermerkart mit einem Beispiel befüllen
        DTOVermerkArt vermerkArt = new DTOVermerkArt(1L, "allgemeine Bemerkung");
        vermerkArt.Sortierung = 1;
        conn.persist(vermerkArt);
        
		// Liefere die Schul-Stammdaten der neu angelegten Schule zurück.
		return this.get(null);
	}

}
