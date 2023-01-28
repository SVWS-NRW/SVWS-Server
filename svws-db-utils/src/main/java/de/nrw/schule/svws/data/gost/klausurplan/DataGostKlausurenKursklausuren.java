package de.nrw.schule.svws.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKursklausur;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.klausurplanung.DTOGostKlausurenKursklausuren;
import de.nrw.schule.svws.db.dto.current.gost.klausurplanung.DTOGostKlausurenSchuelerklausuren;
import de.nrw.schule.svws.db.dto.current.gost.klausurplanung.DTOGostKlausurenVorgaben;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKursklausur}.
 */
public class DataGostKlausurenKursklausuren extends DataManager<Long> {

	private int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKursklausur}.
	 * 
	 * @param conn			die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr 	das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausurenKursklausuren(DBEntityManager conn, int abiturjahr) {
		super(conn);
		_abiturjahr = abiturjahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen Gost-Halbjahr zurück.
	 * 
	 * @param halbjahr das Gost-Halbjahr
	 * 
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKursklausur> getKursKlausuren(int halbjahr) {
		List<GostKursklausur> daten = new Vector<>();

		Map<Long, DTOGostKlausurenVorgaben> mapVorgaben = conn.query(
				"SELECT v FROM DTOGostKlausurenVorgaben v WHERE v.Abi_Jahrgang = :jgid AND v.Halbjahr = :hj",
				DTOGostKlausurenVorgaben.class)
				.setParameter("jgid", _abiturjahr)
				.setParameter("hj", GostHalbjahr.fromID(halbjahr))
				.getResultList().stream().collect(Collectors.toMap(v -> v.ID, v -> v));
		
		if (mapVorgaben.isEmpty()) {
			// TODO Errorhandling nötig?
			return daten;
		}
		
		List<DTOGostKlausurenKursklausuren> kursklausuren = conn.queryNamed(
				"DTOGostKlausurenKursklausuren.vorgabe_id.multiple", mapVorgaben.keySet(), DTOGostKlausurenKursklausuren.class);
		
		if (kursklausuren.isEmpty()) {
			// TODO Errorhandling nötig?
			return daten;
		}

		
		Map<Long, List<DTOGostKlausurenSchuelerklausuren>> mapSchuelerklausuren = conn.queryNamed(
				"DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", kursklausuren.stream().map(k -> k.ID).collect(Collectors.toList()), 
				DTOGostKlausurenSchuelerklausuren.class).stream()
				.collect(Collectors.groupingBy(s -> s.Kursklausur_ID));
		
		if (mapSchuelerklausuren.isEmpty()) {
			// TODO Errorhandling nötig?
			return daten;
		}

		kursklausuren.stream().forEach(k -> {
			GostKursklausur kk = new GostKursklausur();
			DTOGostKlausurenVorgaben v = mapVorgaben.get(k.Vorgabe_ID);
			kk.id = k.ID;
			kk.idVorgabe = k.Vorgabe_ID;
			kk.abijahr = v.Abi_Jahrgang;
			kk.auswahlzeit = v.Auswahlzeit;
			kk.bemerkungenVorgabe = v.Bemerkungen;
			kk.dauer = v.Dauer;
			kk.kursartAllg = v.KursartAllg;	
			kk.idFach = v.Fach_ID;
			kk.quartal = v.Quartal;
			kk.halbjahr = v.Halbjahr.id;
			kk.idKurs = k.Kurs_ID;
			kk.idTermin = k.Termin_ID;
			kk.istAudioNotwendig = v.IstAudioNotwendig;
			kk.istMdlPruefung = v.IstMdlPruefung;
			kk.istVideoNotwendig = v.IstVideoNotwendig;
			kk.startzeit = k.Startzeit;
			kk.schuelerIds = mapSchuelerklausuren.get(k.ID).stream().map(s -> s.Schueler_ID).collect(Collectors.toList());
			daten.add(kk);
		});
		return daten;
	}

	@Override
	public Response get(Long halbjahr) {
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKursKlausuren(halbjahr.intValue()))
				.build();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

}
