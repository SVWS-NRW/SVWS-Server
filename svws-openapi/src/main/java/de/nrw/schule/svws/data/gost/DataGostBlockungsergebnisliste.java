package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.utils.OperationError;

/** Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostBlockungsergebnisListeneintrag}. */
public class DataGostBlockungsergebnisliste extends DataManager<Long> {

	/** Die ID der Blockung für welche die Blockungsergebnisse bestimmt werden */
	private long idBlockung;

	/** Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsergebnisListeneintrag}.
	 * 
	 * @param conn       die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idBlockung die ID der Blockung für welche die Blockungsergebnisse bestimmt werden. */
	public DataGostBlockungsergebnisliste(DBEntityManager conn, long idBlockung) {
		super(conn);
		this.idBlockung = idBlockung;
	}

	private static GostBlockungsergebnisListeneintrag dtoMapperErgebnis(DTOGostBlockungZwischenergebnis ergebnis,
			DTOGostBlockung blockung, List<DTOGostBlockungZwischenergebnisKursSchiene> listKursSchienen,
			List<DTOGostBlockungZwischenergebnisKursSchueler> listKursSchueler) {
		if (blockung == null) // should not happen
			return null;
		if (listKursSchienen == null)
			listKursSchienen = new Vector<>();
		if (listKursSchueler == null)
			listKursSchueler = new Vector<>();
		GostBlockungsergebnisListeneintrag eintrag = new GostBlockungsergebnisListeneintrag();
		eintrag.id = ergebnis.ID;
		eintrag.blockungID = ergebnis.Blockung_ID;
		eintrag.name = blockung.Name;
		eintrag.gostHalbjahr = blockung.Halbjahr.id;
		eintrag.anzahlUmwaehler = ergebnis.AnzahlUmwaehler;
		eintrag.bewertung = ergebnis.Bewertung == null ? -1 : ergebnis.Bewertung;
		eintrag.istMarkiert = ergebnis.IstMarkiert == null ? false : ergebnis.IstMarkiert;
		eintrag.istDuplikat = ergebnis.IstDupliziert == null ? false : ergebnis.IstDupliziert;
		return eintrag;
	}

	private static List<GostBlockungsergebnisListeneintrag> dtoMapperErgebnisse(
			List<DTOGostBlockungZwischenergebnis> ergebnisse, Map<Long, DTOGostBlockung> blockungen,
			Map<Long, List<DTOGostBlockungZwischenergebnisKursSchiene>> mapKursSchienen,
			Map<Long, List<DTOGostBlockungZwischenergebnisKursSchueler>> mapKursSchueler) {
		Vector<GostBlockungsergebnisListeneintrag> list = new Vector<>();
		if (ergebnisse.size() != 0) {
			for (DTOGostBlockungZwischenergebnis ergebnis : ergebnisse) {
				list.add(dtoMapperErgebnis(ergebnis, blockungen.get(ergebnis.Blockung_ID),
				mapKursSchienen.get(ergebnis.ID), mapKursSchueler.get(ergebnis.ID)));
			}
		}
		return list;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			return OperationError.NOT_FOUND.getResponse();
		Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
			return OperationError.NOT_FOUND.getResponse();
		List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryNamed(
				"DTOGostBlockungZwischenergebnis.blockung_id", idBlockung, DTOGostBlockungZwischenergebnis.class);
		if (ergebnisse == null)
			return OperationError.NOT_FOUND.getResponse();
		ergebnisse.removeIf(e -> e.IstDupliziert); // Gib nur Ergebnisse zurück, welche für diese Blockungsdefinition erstellt wurden
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
		if (blockung == null)
			return OperationError.NOT_FOUND.getResponse();
		List<GostBlockungsergebnisListeneintrag> daten = null;
		if (ergebnisse.size() == 0) {
			daten = new Vector<>();
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		}
		Map<Long, DTOGostBlockung> blockungen = new HashMap<>();
		blockungen.put(blockung.ID, blockung);
		List<Long> ergebnisIDs = ergebnisse.stream().map(e -> e.ID).collect(Collectors.toList());
		Map<Long, List<DTOGostBlockungZwischenergebnisKursSchiene>> mapKursSchienen = (ergebnisIDs.size() == 0) 
				? new HashMap<>()
				: conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id.multiple", ergebnisIDs,
						DTOGostBlockungZwischenergebnisKursSchiene.class)
					.stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));
		Map<Long, List<DTOGostBlockungZwischenergebnisKursSchueler>> mapKursSchueler = (ergebnisIDs.size() == 0) 
				? new HashMap<>()
				: conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id.multiple", ergebnisIDs,
						DTOGostBlockungZwischenergebnisKursSchueler.class)
					.stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));
		daten = dtoMapperErgebnisse(ergebnisse, blockungen, mapKursSchienen, mapKursSchueler);
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
