package de.nrw.schule.svws.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schueler.SchuelerLeistungsdaten;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.utils.OperationError;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLeistungsdaten}.
 */
public class DataSchuelerLeistungsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLeistungsdaten}.
	 * 
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLeistungsdaten(DBEntityManager conn) {
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOSchuelerLeistungsdaten} in einen Core-DTO {@link SchuelerLeistungsdaten}.  
	 */
	private Function<DTOSchuelerLeistungsdaten, SchuelerLeistungsdaten> dtoMapper = (DTOSchuelerLeistungsdaten dto) -> {
		SchuelerLeistungsdaten daten = new SchuelerLeistungsdaten();
		daten.id = dto.ID;
		daten.lernabschnittID = dto.Abschnitt_ID;
		daten.fachID = dto.Fach_ID;
		daten.kursID = dto.Kurs_ID;
		daten.kursart = dto.Kursart;
		daten.abifach = dto.AbiFach;
		daten.istZP10oderZK10 = dto.Prf10Fach == null ? false : dto.Prf10Fach;
		daten.koopSchule = dto.SchulNr;
		daten.lehrerID = dto.Fachlehrer_ID;
		daten.wochenstunden = dto.Wochenstunden == null ? 0 : dto.Wochenstunden;
		daten.zusatzkraftID = dto.Zusatzkraft_ID;
		daten.zusatzkraftWochenstunden = dto.WochenstdZusatzkraft == null ? 0 : dto.WochenstdZusatzkraft;
		daten.aufZeugnis = dto.AufZeugnis == null ? true : dto.AufZeugnis;
		daten.note = dto.NotenKrz.kuerzel;
		daten.istGemahnt = dto.Warnung == null ? false : dto.Warnung; // TODO bestimme ggf. aus Halbjahr zuvor
		daten.Mahndatum = dto.Warndatum;
		daten.istEpochal = dto.VorherAbgeschl == null ? false : dto.VorherAbgeschl; 
		daten.geholtJahrgangAbgeschlossen = dto.AbschlussJahrgang;
		daten.gewichtungAllgemeinbildend = dto.Gewichtung == null ? 1 : dto.Gewichtung;
		daten.noteBerufsabschluss = dto.NoteAbschlussBA; 
		daten.textFachbezogeneLernentwicklung = dto.Lernentw;
		daten.umfangLernstandsbericht = dto.Umfang;
		daten.fehlstundenGesamt = dto.FehlStd;
		daten.fehlstundenUnentschuldigt = dto.uFehlStd;
		return daten;
	};
	
	
	/**
	 * Ermittelt die Leistungsdaten für den angegebenen Lernabschnitt und fügt diese in die übergebene Liste ein. 
	 * 
	 * @param abschnittID   die ID des Lernabschnitts
	 * @param list          die Liste, in die eingefügt wird
	 * 
	 * @return eine Liste mit der Leistungsdaten des Lernabschnitts
	 */
	public boolean getByLernabschnitt(Long abschnittID, List<SchuelerLeistungsdaten> list) {
    	// Bestimme die Leistungsdaten des Lernabschnitts
    	List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", abschnittID, DTOSchuelerLeistungsdaten.class); 
    	if (leistungsdaten == null)
			return false;
    	// Konvertiere sie und füge sie zur Liste hinzu
    	for (DTOSchuelerLeistungsdaten l : leistungsdaten)
    		list.add(dtoMapper.apply(l));
    	return true;
	}
	
	
	@Override
	public Response get(Long id) {
		// Prüfe, ob die Leistungsdaten mit der ID existieren
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		DTOSchuelerLeistungsdaten dto = conn.queryByKey(DTOSchuelerLeistungsdaten.class, id);
    	if (dto == null)
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		SchuelerLeistungsdaten daten = dtoMapper.apply(dto);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(Long id, InputStream is) {
		// TODO
		throw new UnsupportedOperationException();
	}

}
