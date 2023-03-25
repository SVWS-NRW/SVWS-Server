package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongFunction;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.types.kaoa.KAOAAnschlussoptionen;
import de.svws_nrw.core.types.kaoa.KAOABerufsfeld;
import de.svws_nrw.core.types.kaoa.KAOAEbene4;
import de.svws_nrw.core.types.kaoa.KAOAKategorie;
import de.svws_nrw.core.types.kaoa.KAOAMerkmal;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmal;
import de.svws_nrw.core.types.kaoa.KAOAZusatzmerkmaleOptionsarten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.persistence.Table;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerKAoADaten}.
 */
public class DataSchuelerKAoADaten extends DataManager<Long> {

	/**
	 * statischer Text
	 */
	public static final String NICHT_GEFUNDEN = " nicht gefunden";

	/**
	 * statischer Text
	 */
	public static final String SERVER_ERROR = "Server Error";

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataSchuelerKAoADaten(DBEntityManager conn) {
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
	public Response get(Long aLong) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long aLong, InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt alle SchuelerKAoADaten zum Schüler zurück
	 *
	 * @param aLong SchuelerID
	 *
	 * @return Liste von SchuelerKAoADaten passend zum Schüler
	 */
	public List<SchuelerKAoADaten> getBySchuelerID(Long aLong) {
		List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schueler_id", aLong, DTOSchuelerLernabschnittsdaten.class);
		List<Long> schuelerLernabschnittsdatenIds = schuelerLernabschnittsdaten.stream().map(sla -> sla.ID).toList();
		List<DTOSchuelerKAoADaten> daten = conn.queryNamed("DTOSchuelerKAoADaten.abschnitt_id.multiple", schuelerLernabschnittsdatenIds, DTOSchuelerKAoADaten.class);
		return daten.stream().map(mapSchuelerKAoADaten).toList();
	}

	/**
	 * Gibt getBySchuelerID als Response zurück
	 *
	 * @param aLong SchuelerID
	 *
	 * @return getBySchuelerID als Response
	 */
	public Response getBySchuelerIDAsResponse(Long aLong) {
		try {
			List<SchuelerKAoADaten> daten = this.getBySchuelerID(aLong);
			if (daten == null || daten.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (@SuppressWarnings("unused") Exception e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}


	/**
	 * Mapt DTOSchuelerKAoADaten auf SchuelerKAoADaten
	 */
	public static Function<DTOSchuelerKAoADaten, SchuelerKAoADaten> mapSchuelerKAoADaten = (DTOSchuelerKAoADaten schuelerKAoADaten) -> {
		SchuelerKAoADaten result = new SchuelerKAoADaten();
		result.id = schuelerKAoADaten.ID;
		result.kategorie = schuelerKAoADaten.KategorieID;
		result.merkmal = schuelerKAoADaten.MerkmalID;
		result.zusatzmerkmal = schuelerKAoADaten.ZusatzmerkmalID;
		result.anschlussoption = schuelerKAoADaten.AnschlussoptionID;
		result.ebene4 = schuelerKAoADaten.SBO_Ebene4ID;
		result.berufsfeld = schuelerKAoADaten.BerufsfeldID;
		result.jahrgang = schuelerKAoADaten.Jahrgang;
		result.abschnitt = schuelerKAoADaten.Abschnitt_ID;
		result.bemerkung = schuelerKAoADaten.Bemerkung;
		return result;
	};

	/**
	 * Gibt den Response für deleteBySchuelerIDAsResponse zurück
	 *
	 * @param schuelerKAoAID id des Datensatzes
	 *
	 * @return Response
	 */
	public Response deleteBySchuelerKAoAIDAsResponse(final Long schuelerKAoAID) {
		this.deleteBySchuelerKAoAID(schuelerKAoAID);
		return buildResponse(null, Response.Status.NO_CONTENT);
	}

	/**
	 * Läscht DTOSchuelerKAoADaten für die gegebene id
	 *
	 * @param schuelerKAoAID ID der DTOSchuelerKAoADaten
	 */
	private void deleteBySchuelerKAoAID(Long schuelerKAoAID) {
		DTOSchuelerKAoADaten result = this.getByID(schuelerKAoAID);

		if (result == null) {
			throw OperationError.NOT_FOUND.exception("Datensatz mit der id: " + schuelerKAoAID + NICHT_GEFUNDEN);
		}

		boolean success = this.conn.remove(result);

		if (!success) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(SERVER_ERROR);
		}
	}

	/**
	 * Gibt den Response für createBySchuelerID zurück
	 *
	 * @param schuelerid id des Schuelers
	 * @param daten      die Daten
	 *
	 * @return Response
	 */
	public Response createBySchuelerIDAsResponse(Long schuelerid, final SchuelerKAoADaten daten) {
		SchuelerKAoADaten result = this.createBySchuelerID(schuelerid, daten);
		return buildResponse(result, Response.Status.CREATED);
	}

	/**
	 * Legt SchuelerKAoADaten an
	 *
	 * @param schuelerid id des Schuelers
	 * @param daten      die Daten
	 *
	 * @return SchuelerKAoADaten
	 */
	public SchuelerKAoADaten createBySchuelerID(Long schuelerid, final SchuelerKAoADaten daten) {
		this.validate(schuelerid, daten);
		boolean success = this.persist(DTOSchuelerKAoADaten.class, id -> {
			daten.id = id;
			return getDtoSchuelerKAoADaten(daten);
		});

		if (!success) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(SERVER_ERROR);
		}
		return daten;
	}

	/**
	 * Gibt den Response für patchBySchuelerID zurück
	 *
	 * @param schuelerid     id des Schuelers
	 * @param daten          die Daten
	 * @param schuelerKAoAID id von SchuelerKAoADaten
	 *
	 * @return Response
	 */
	public Response putBySchuelerIDAsResponse(Long schuelerid, SchuelerKAoADaten daten, Long schuelerKAoAID) {
		this.putBySchuelerID(schuelerid, daten, schuelerKAoAID);
		return buildResponse(null, Response.Status.ACCEPTED);
	}

	/**
	 * Erzeugt einen Response
	 *
	 * @param result der Response
	 * @param status der Status code
	 *
	 * @return Response
	 */
	private static Response buildResponse(SchuelerKAoADaten result, Response.Status status) {
		return Response.status(status).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	/**
	 * Ändert die SchuelerKAoADaten
	 *
	 * @param schuelerid     id des Schuelers
	 * @param daten          die Daten
	 * @param schuelerKAoAID id von SchuelerKAoADaten
	 *
	 * @return SchuelerKAoADaten
	 */
	public SchuelerKAoADaten putBySchuelerID(Long schuelerid, SchuelerKAoADaten daten, Long schuelerKAoAID) {
		if (!Objects.equals(schuelerKAoAID, daten.id)) {
			throw OperationError.BAD_REQUEST.exception("Payload id inkorrekt");
		}
		this.validate(schuelerid, daten);

		DTOSchuelerKAoADaten result = this.getByID(schuelerKAoAID);
		if (result == null) {
			throw OperationError.NOT_FOUND.exception("Datensatz mit der id: " + schuelerKAoAID + NICHT_GEFUNDEN);
		}

		getDtoSchuelerKAoADaten(daten, result);
		boolean success = this.conn.persist(result);

		if (!success) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(SERVER_ERROR);
		}
		return daten;
	}

	/**
	 * Holt DTOSchuelerKAoADaten für eine gegebene ID
	 *
	 * @param id die ID
	 *
	 * @return die DTOSchuelerKAoADaten
	 */
	public DTOSchuelerKAoADaten getByID(Long id) {
		return this.conn.queryByKey(DTOSchuelerKAoADaten.class, id);
	}


	/**
	 * Generische Methode zum speichern von Daten
	 *
	 * @param t       Typ der zu speichernden Daten
	 * @param idApply methode die die ID im Objekt übernimmt
	 * @param <T>     erwarteter Rückgabetyp
	 *
	 * @return Rückgabetyp
	 */

	private <T> boolean persist(Class<T> t, LongFunction<T> idApply) {
		String tableName = t.getAnnotation(Table.class).name();
		conn.transactionBegin();
		DTODBAutoInkremente dbRegelID = conn.queryByKey(DTODBAutoInkremente.class, tableName);
		long nextID = dbRegelID == null ? 1 : dbRegelID.MaxID + 1;
		T daten = idApply.apply(nextID);
		this.conn.transactionPersist(daten);

		if (!this.conn.transactionCommit()) {
			this.conn.transactionRollback();
			return false;
		}

		return true;
	}

	/**
	 * Erzeugt DTOSchuelerKAoADaten aus SchuelerKAoADaten
	 *
	 * @param daten die SchuelerKAoADaten
	 *
	 * @return DTOSchuelerKAoADaten
	 */
	private static DTOSchuelerKAoADaten getDtoSchuelerKAoADaten(SchuelerKAoADaten daten) {
		DTOSchuelerKAoADaten result = new DTOSchuelerKAoADaten(daten.id, daten.abschnitt, daten.kategorie);
		return getDtoSchuelerKAoADaten(daten, result);
	}

	/**
	 * Mapt SchuelerKAoADaten auf DTOSchuelerKAoADaten
	 *
	 * @param daten  die SchuelerKAoADaten
	 * @param result die DTOSchuelerKAoADaten
	 *
	 * @return die DTOSchuelerKAoADaten
	 */
	private static DTOSchuelerKAoADaten getDtoSchuelerKAoADaten(SchuelerKAoADaten daten, DTOSchuelerKAoADaten result) {
		result.MerkmalID = daten.merkmal;
		result.ZusatzmerkmalID = daten.zusatzmerkmal;
		result.AnschlussoptionID = daten.anschlussoption;
		result.SBO_Ebene4ID = daten.ebene4;
		result.BerufsfeldID = daten.berufsfeld;
		result.Jahrgang = daten.jahrgang;
		result.Bemerkung = daten.bemerkung;
		result.Abschnitt_ID = daten.abschnitt;
		result.KategorieID = daten.kategorie;
		return result;
	}


	/**
	 * validiert SchuelerKAoADaten und holt lernabschnittsdaten aus der Datenbank
	 *
	 * @param schuelerid die SChueler ID
	 * @param daten      SchuelerKAoADaten
	 *
	 * @return boolean
	 */
	public boolean validate(Long schuelerid, SchuelerKAoADaten daten) {
		DTOSchuelerLernabschnittsdaten lernabschnittsdaten = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, daten.abschnitt);
		validateLernabschnittsdaten(schuelerid, lernabschnittsdaten);
		return validate(daten);
	}

	/**
	 * validiert Lernabschnittsdaten
	 *
	 * @param schuelerid          id des Schuelers
	 * @param lernabschnittsdaten DTOSchuelerLernabschnittsdaten
	 */
	protected static void validateLernabschnittsdaten(Long schuelerid, DTOSchuelerLernabschnittsdaten lernabschnittsdaten) {
		if (lernabschnittsdaten == null) {
			throw OperationError.BAD_REQUEST.exception("lernabschnittsdaten nicht gefunden");
		}
		if (!Objects.equals(schuelerid, lernabschnittsdaten.Schueler_ID)) {
			throw OperationError.BAD_REQUEST.exception("id der lernabschnittsdaten passt nicht zum schueler");
		}
	}

	/**
	 * validiert SchuelerKAoADaten
	 *
	 * @param daten die SchuelerKAoADaten
	 *
	 * @return boolean
	 */
	public static boolean validate(SchuelerKAoADaten daten) {
		KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		validateKategorie(daten, kategorie);

		KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		validateMerkmal(daten, kategorie, merkmal);

		KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		validateZusatzmerkmal(daten, merkmal, zusatzmerkmal);

		KAOAEbene4 ebene4 = KAOAEbene4.getByID(daten.ebene4);
		validateEbene4(daten, zusatzmerkmal, ebene4);

		KAOAAnschlussoptionen anschlussoptionen = KAOAAnschlussoptionen.getByID(daten.anschlussoption);
		validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen);

		KAOABerufsfeld berufsfeld = KAOABerufsfeld.getByID(daten.berufsfeld);
		validateBerufsfeld(daten, zusatzmerkmal, berufsfeld);

		validateJahrgang(daten, kategorie);

		return true;
	}

	/**
	 * validiert Jahrgang
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 */
	protected static void validateJahrgang(SchuelerKAoADaten daten, KAOAKategorie kategorie) {
		if (!kategorie.daten.jahrgaenge.contains(daten.jahrgang)) {
			throw OperationError.BAD_REQUEST.exception("jahrgaenge ungültig");
		}
	}

	/**
	 * validiert Berufsfeld
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 * @param berufsfeld    KAOABerufsfeld
	 */
	protected static void validateBerufsfeld(SchuelerKAoADaten daten, KAOAZusatzmerkmal zusatzmerkmal, KAOABerufsfeld berufsfeld) {
		if (KAOAZusatzmerkmaleOptionsarten.BERUFSFELD.equals(KAOAZusatzmerkmaleOptionsarten.getByKuerzel(zusatzmerkmal.daten.optionsart))) {
			validateValueFound(daten.berufsfeld, berufsfeld, "berufsfeld");
		}
	}

	/**
	 * validiert Anschlussoption
	 *
	 * @param daten             SchuelerKAoADaten
	 * @param zusatzmerkmal     KAOAZusatzmerkmal
	 * @param anschlussoptionen KAOAAnschlussoptionen
	 */
	protected static void validateAnschlussoption(SchuelerKAoADaten daten, KAOAZusatzmerkmal zusatzmerkmal, KAOAAnschlussoptionen anschlussoptionen) {
		if (KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION.equals(KAOAZusatzmerkmaleOptionsarten.getByKuerzel(zusatzmerkmal.daten.optionsart))) {
			validateValueFound(daten.anschlussoption, anschlussoptionen, "anschlussoption");
			if (anschlussoptionen != null && !anschlussoptionen.daten.anzeigeZusatzmerkmal.contains(zusatzmerkmal.daten.kuerzel)) {
				throw OperationError.BAD_REQUEST.exception("anschlussoptionen passt nicht zum zusatzmerkmal");
			}
		}
	}

	/**
	 * validiert Ebene4
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 * @param ebene4        KAOAEbene4
	 */
	protected static void validateEbene4(SchuelerKAoADaten daten, KAOAZusatzmerkmal zusatzmerkmal, KAOAEbene4 ebene4) {
		if (zusatzmerkmal == null) {
			throw OperationError.BAD_REQUEST.exception("zusatzmerkmal nicht angegeben");
		}
		if (KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4.equals(KAOAZusatzmerkmaleOptionsarten.getByKuerzel(zusatzmerkmal.daten.optionsart))) {
			validateValueFound(daten.ebene4, ebene4, "ebene4");
			if (ebene4 != null && !ebene4.daten.zusatzmerkmal.equals(zusatzmerkmal.daten.kuerzel)) {
				throw OperationError.BAD_REQUEST.exception("ebene4 passt nicht zum zusatzmerkmal");
			}
		}
	}

	/**
	 * validiert Zusatzmerkmal
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param merkmal       KAOAMerkmal
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 */
	protected static void validateZusatzmerkmal(SchuelerKAoADaten daten, KAOAMerkmal merkmal, KAOAZusatzmerkmal zusatzmerkmal) {
		validateValueFound(daten.zusatzmerkmal, zusatzmerkmal, "zusatzmerkmal");
		if (zusatzmerkmal != null && !zusatzmerkmal.daten.merkmal.equals(merkmal.daten.kuerzel)) {
			throw OperationError.BAD_REQUEST.exception("zusatzmerkmal passt nicht zur merkmal");
		}
	}

	/**
	 * validiert Merkmal
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 * @param merkmal   KAOAMerkmal
	 */
	protected static void validateMerkmal(SchuelerKAoADaten daten, KAOAKategorie kategorie, KAOAMerkmal merkmal) {
		validateValueFound(daten.merkmal, merkmal, "merkmal");
		if (merkmal != null && !merkmal.daten.kategorie.equals(kategorie.daten.kuerzel)) {
			throw OperationError.BAD_REQUEST.exception("merkmal passt nicht zur kategorie");
		}
	}

	/**
	 * validiert Kategorie
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 */
	protected static void validateKategorie(SchuelerKAoADaten daten, KAOAKategorie kategorie) {
		if (daten.kategorie == null) {
			throw OperationError.BAD_REQUEST.exception("kategorie darf nicht leer sein");
		}
		if (kategorie == null) {
			throw OperationError.BAD_REQUEST.exception("kategorie nicht gefunden");
		}
	}

	/**
	 * validiert übergebenen Wert
	 *
	 * @param idFromData id aus daten
	 * @param obj        Objekt aus dem enum
	 * @param fieldName  names des Feldes
	 */
	protected static void validateValueFound(Long idFromData, Object obj, String fieldName) {
		if (idFromData != null && obj == null) {
			throw OperationError.BAD_REQUEST.exception(fieldName + NICHT_GEFUNDEN);
		}
	}
}
