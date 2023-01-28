package de.nrw.schule.svws.data.gost.klausurplan;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;
import java.util.function.Function;

import de.nrw.schule.svws.core.data.gost.klausuren.GostKlausurtermin;
import de.nrw.schule.svws.core.data.schule.SchuleStammdaten;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.klausurplanung.DTOGostKlausurenTermine;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link GostKlausurtermin}.
 */
public class DataGostKlausurenTermine extends DataManager<Long> {

	private int _abiturjahr;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link GostKlausurtermin}.
	 * 
	 * @param conn			die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abiturjahr 	das Jahr, in welchem der Jahrgang Abitur machen wird
	 */
	public DataGostKlausurenTermine(DBEntityManager conn, int abiturjahr) {
		super(conn);
		_abiturjahr = abiturjahr;
	}

	@Override
	public Response getAll() {
		return this.getList();
	}
	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOEigeneSchule} in einen Core-DTO {@link SchuleStammdaten}.  
	 */
	private Function<DTOGostKlausurenTermine, GostKlausurtermin> dtoMapper = (DTOGostKlausurenTermine z) -> {
		GostKlausurtermin daten = new GostKlausurtermin();
		daten.id = z.ID;
		daten.abijahr = z.Abi_Jahrgang;
		daten.datum = z.Datum;
		daten.halbjahr = z.Halbjahr.id;
		daten.quartal = z.Quartal;
		daten.startzeit = z.Startzeit;
		daten.bemerkung = z.Bemerkungen;
		return daten;
	};

	/**
	 * Gibt die Liste der Kursklausuren einer Jahrgangsstufe im übergebenen Gost-Halbjahr zurück.
	 * 
	 * @param halbjahr das Gost-Halbjahr
	 * 
	 * @return die Liste der Kursklausuren
	 */
	private List<GostKlausurtermin> getKlausurtermine(int halbjahr) {
		List<DTOGostKlausurenTermine> termine = conn.query(
				"SELECT t FROM DTOGostKlausurenTermine t WHERE t.Abi_Jahrgang = :jgid AND t.Halbjahr = :hj",
				DTOGostKlausurenTermine.class)
				.setParameter("jgid", _abiturjahr)
				.setParameter("hj", GostHalbjahr.fromID(halbjahr))
				.getResultList();
		List<GostKlausurtermin> daten = new Vector<>();
		for (DTOGostKlausurenTermine z : termine)
			daten.add(dtoMapper.apply(z));
		return daten;
	}

	@Override
	public Response get(Long halbjahr) {
		// Kursklausuren für einen Abiturjahrgang in einem Gost-Halbjahr
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(this.getKlausurtermine(halbjahr.intValue()))
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
