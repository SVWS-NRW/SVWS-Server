package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungSchiene;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungsergebnis}.
 */
public class DataGostBlockungsergebnisse extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsergebnis}.
	 * 
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungsergebnisse(DBEntityManager conn) {
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


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link Schueler}.  
	 */
	private static Comparator<Schueler> schuelerComparator = (a,b) -> {
		Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.nachname == null) && (b.nachname != null))
			return -1;
		else if ((a.nachname != null) && (b.nachname == null))
			return 1;
		else if ((a.nachname == null) && (b.nachname == null))
			return 0;
		int result = collator.compare(a.nachname, b.nachname);
		if (result == 0) {
    		if ((a.vorname == null) && (b.vorname != null))
    			return -1;
    		else if ((a.vorname != null) && (b.vorname == null))
    			return 1;
    		else if ((a.vorname == null) && (b.vorname == null))
    			return 0;
    		result = collator.compare(a.vorname, b.vorname);
		}
		return result;		
	};
	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchueler} in einen Core-DTO {@link Schueler}.  
	 */
	private static Function<DTOSchueler, Schueler> dtoMapperSchueler = (DTOSchueler dto) -> {
		Schueler daten = new Schueler();
		daten.id = dto.ID;
		daten.nachname = dto.Nachname;
		daten.vorname = dto.Vorname;
		daten.geschlecht = dto.Geschlecht.id;
		return daten;
	};
	

	@Override
	public Response get(Long id) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme das Blockungs-Zwischenergebnis
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme die zugehörige Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme die Fächer anhand des Abiturjahrgangs der Blockung
		GostFaecherManager faecher = (new DataGostFaecher(conn, blockung.Abi_Jahrgang)).getListInternal();
		if (faecher == null)
			return OperationError.NOT_FOUND.getResponse();
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.size() == 0))
			return OperationError.NOT_FOUND.getResponse();
		List<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toList());
		List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.size() == 0))
			return OperationError.NOT_FOUND.getResponse();
		List<Schueler> schueler = schuelerListe.stream().map(dtoMapperSchueler).sorted(schuelerComparator).collect(Collectors.toList());
		// Bestimme alle Schienen
		List<DTOGostBlockungSchiene> listDTOSchienen = conn.queryNamed("DTOGostBlockungSchiene.blockung_id", blockung.ID, DTOGostBlockungSchiene.class);
		if (listDTOSchienen == null)
			listDTOSchienen = new Vector<>();
		List<GostBlockungSchiene> listSchienen = listDTOSchienen.stream().map(s -> DataGostBlockungsdaten.dtoMapperSchiene.apply(s)).collect(Collectors.toList());
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		List<DTOGostBlockungKurs> kurse = conn.queryNamed("DTOGostBlockungKurs.blockung_id", blockung.ID, DTOGostBlockungKurs.class);
		if (kurse == null)
			kurse = new Vector<>();
		List<GostBlockungKurs> listKurse = kurse.stream().map(k -> DataGostBlockungsdaten.dtoMapperKurse.apply(k)).collect(Collectors.toList());
		// Bestimme alle Kurs-Schienen-Zuordnungen
		List<DTOGostBlockungZwischenergebnisKursSchiene> listSchienenKurse = conn
				.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", id, DTOGostBlockungZwischenergebnisKursSchiene.class);
		// Bestimme alle Kurs-Schüler-Zuordnungen
		List<DTOGostBlockungZwischenergebnisKursSchueler> listKursSchueler = conn
				.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", id, DTOGostBlockungZwischenergebnisKursSchueler.class);
		GostBlockungsergebnisManager manager = new GostBlockungsergebnisManager(ergebnis.ID, ergebnis.Blockung_ID, blockung.Name, blockung.Halbjahr, schueler, faecher.toVector(), listSchienen, listKurse);
		for (DTOGostBlockungZwischenergebnisKursSchiene ks : listSchienenKurse)
			manager.assignKursSchiene(ks.Blockung_Kurs_ID, ks.Schienen_ID);
		for (DTOGostBlockungZwischenergebnisKursSchueler ks : listKursSchueler)
			manager.assignSchuelerKurs(ks.Schueler_ID, ks.Blockung_Kurs_ID, false);
// TODO
//		daten.anzahlUmwaehler = ergebnis.AnzahlUmwaehler;
//		daten.bewertung = ergebnis.Bewertung == null ? -1 : ergebnis.Bewertung;
//		daten.istMarkiert = ergebnis.IstMarkiert == null ? false : ergebnis.IstMarkiert;
//		daten.anzahlKollisionen = 0;
//		daten.anzahlSchienenMitKollisionen = 0;
		GostBlockungsergebnis daten = manager.getErgebnis();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Entfernt das Zwischenergebnis mit der angegebenen ID aus der Datenbank. 
	 *
	 * @param id   die ID des zu löschenden Blockungsergebnis 
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		// Bestimme das Zwischenergebnis
		DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (erg == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne das Ergebnis
		conn.remove(erg);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}

	
	
	private void _createKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			throw OperationError.NOT_FOUND.exception();
		if (idSchueler == null)
			throw OperationError.CONFLICT.exception();
		// Bestimme das Blockungs-Zwischenergebnis
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
		if (ergebnis == null)
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die zugehörige Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.size() == 0))
			throw OperationError.NOT_FOUND.exception();
		Set<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toSet());
		if (!schuelerIDs.contains(idSchueler))
			throw OperationError.CONFLICT.exception();
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw OperationError.NOT_FOUND.exception();
		if (kurs.Blockung_ID != ergebnis.Blockung_ID)
			throw OperationError.CONFLICT.exception();
		
		// Füge die neue Kurszuordnung hinzu
		conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(idZwischenergebnis, idKurs, idSchueler));
	}

	
	
	private void _deleteKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			throw OperationError.NOT_FOUND.exception();
		if ((idSchueler == null) || (idKurs == null))
			throw OperationError.CONFLICT.exception();
		// Entferne die Zuordnung
		DTOGostBlockungZwischenergebnisKursSchueler dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchueler.class, idZwischenergebnis, idKurs, idSchueler);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		conn.transactionRemove(dto);
	}
	

	/**
	 * Erstellt eine Kurs-Schüler-Zuordnung in der Datenbank.
	 * 
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKurs               die ID des neuen Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 */
	public Response createKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		try {
			conn.transactionBegin();
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;			
		}
	}
	
	
	/**
	 * Aktualisiert eine Kurs-Schüler-Zuordnung in der Datenbank.
	 * 
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKursAlt            die ID des alten Kurses
	 * @param idKursNeu            die ID des neuen Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 */
	public Response updateKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKursAlt, Long idKursNeu) {
		if (idKursNeu == null)
			return deleteKursSchuelerZuordnung(idZwischenergebnis, idKursAlt, idSchueler);
		try {
			conn.transactionBegin();
			// TODO prüfe, ob die Kursarten übereinstimmen...
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursAlt);
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursNeu);			
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;			
		}
	}
	
	
	/**
	 * Entfernt die die Zuordnung des Schüler zu dem Kurs bei einem Zwischenergebnis.
	 *  
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKurs               die ID des Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		try {
			conn.transactionBegin();
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

}
