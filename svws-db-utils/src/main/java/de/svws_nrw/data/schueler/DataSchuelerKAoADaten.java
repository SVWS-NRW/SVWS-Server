package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.core.types.kaoa.KAOAAnschlussoption;
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
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerKAoADaten}.
 */
public final class DataSchuelerKAoADaten extends DataManager<Long> {

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
	public DataSchuelerKAoADaten(final DBEntityManager conn) {
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
	public Response get(final Long aLong) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long aLong, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt alle SchuelerKAoADaten zum Schüler zurück
	 *
	 * @param id   SchuelerID
	 *
	 * @return Liste von SchuelerKAoADaten passend zum Schüler
	 */
	public List<SchuelerKAoADaten> getBySchuelerID(final Long id) {
		final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID,
				DTOSchuelerLernabschnittsdaten.class, id);
		final List<Long> schuelerLernabschnittsdatenIds = schuelerLernabschnittsdaten.stream().map(sla -> sla.ID).toList();
		final List<DTOSchuelerKAoADaten> daten = conn.queryList(DTOSchuelerKAoADaten.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOSchuelerKAoADaten.class, schuelerLernabschnittsdatenIds);
		return daten.stream().map(mapSchuelerKAoADaten).toList();
	}

	/**
	 * Gibt getBySchuelerID als Response zurück
	 *
	 * @param id   SchuelerID
	 *
	 * @return getBySchuelerID als Response
	 */
	public Response getBySchuelerIDAsResponse(final Long id) {
		try {
			List<SchuelerKAoADaten> daten = this.getBySchuelerID(id);
			if ((daten == null) || daten.isEmpty())
				daten = new ArrayList<>();
			return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
		} catch (@SuppressWarnings("unused") final Exception e) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}


	/**
	 * Mapt DTOSchuelerKAoADaten auf SchuelerKAoADaten
	 */
	public static final Function<DTOSchuelerKAoADaten, SchuelerKAoADaten> mapSchuelerKAoADaten = (final DTOSchuelerKAoADaten schuelerKAoADaten) -> {
		final SchuelerKAoADaten result = new SchuelerKAoADaten();
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
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response deleteBySchuelerKAoAIDAsResponse(final Long schuelerKAoAID) throws ApiOperationException {
		this.deleteBySchuelerKAoAID(schuelerKAoAID);
		return buildResponse(null, Response.Status.NO_CONTENT);
	}

	/**
	 * Läscht DTOSchuelerKAoADaten für die gegebene id
	 *
	 * @param schuelerKAoAID ID der DTOSchuelerKAoADaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	private void deleteBySchuelerKAoAID(final Long schuelerKAoAID) throws ApiOperationException {
		final DTOSchuelerKAoADaten result = this.conn.queryByKey(DTOSchuelerKAoADaten.class, schuelerKAoAID);
		if (result == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Datensatz mit der id: " + schuelerKAoAID + NICHT_GEFUNDEN);
		final boolean success = this.conn.transactionRemove(result);
		if (!success)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, SERVER_ERROR);
	}

	/**
	 * Gibt den Response für createBySchuelerID zurück
	 *
	 * @param schuelerid id des Schuelers
	 * @param daten      die Daten
	 *
	 * @return Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response createBySchuelerIDAsResponse(final Long schuelerid, final SchuelerKAoADaten daten) throws ApiOperationException {
		final SchuelerKAoADaten result = this.createBySchuelerID(schuelerid, daten);
		return buildResponse(result, Response.Status.CREATED);
	}

	/**
	 * Legt SchuelerKAoADaten an
	 *
	 * @param schuelerid id des Schuelers
	 * @param daten      die Daten
	 *
	 * @return SchuelerKAoADaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerKAoADaten createBySchuelerID(final Long schuelerid, final SchuelerKAoADaten daten) throws ApiOperationException {
		this.validate(schuelerid, daten);
		daten.id = conn.transactionGetNextID(DTOSchuelerKAoADaten.class);
		final boolean success = conn.transactionPersist(getDtoSchuelerKAoADaten(daten));
		if (!success)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, SERVER_ERROR);
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
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response putBySchuelerIDAsResponse(final Long schuelerid, final SchuelerKAoADaten daten, final Long schuelerKAoAID) throws ApiOperationException {
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
	private static Response buildResponse(final SchuelerKAoADaten result, final Response.Status status) {
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
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerKAoADaten putBySchuelerID(final Long schuelerid, final SchuelerKAoADaten daten, final Long schuelerKAoAID) throws ApiOperationException {
		if (!Objects.equals(schuelerKAoAID, daten.id))
			throw new ApiOperationException(Status.BAD_REQUEST, "Payload id inkorrekt");
		this.validate(schuelerid, daten);

		final DTOSchuelerKAoADaten result = this.conn.queryByKey(DTOSchuelerKAoADaten.class, schuelerKAoAID);
		if (result == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Datensatz mit der id: " + schuelerKAoAID + NICHT_GEFUNDEN);

		getDtoSchuelerKAoADaten(daten, result);
		final boolean success = this.conn.transactionPersist(result);
		if (!success)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, SERVER_ERROR);
		return daten;
	}

	/**
	 * Erzeugt DTOSchuelerKAoADaten aus SchuelerKAoADaten
	 *
	 * @param daten die SchuelerKAoADaten
	 *
	 * @return DTOSchuelerKAoADaten
	 */
	private static DTOSchuelerKAoADaten getDtoSchuelerKAoADaten(final SchuelerKAoADaten daten) {
		final DTOSchuelerKAoADaten result = new DTOSchuelerKAoADaten(daten.id, daten.abschnitt, daten.kategorie);
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
	private static DTOSchuelerKAoADaten getDtoSchuelerKAoADaten(final SchuelerKAoADaten daten, final DTOSchuelerKAoADaten result) {
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
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public boolean validate(final Long schuelerid, final SchuelerKAoADaten daten) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten lernabschnittsdaten = this.conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, daten.abschnitt);
		validateLernabschnittsdaten(schuelerid, lernabschnittsdaten);
		return validate(daten);
	}

	/**
	 * validiert Lernabschnittsdaten
	 *
	 * @param schuelerid          id des Schuelers
	 * @param lernabschnittsdaten DTOSchuelerLernabschnittsdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateLernabschnittsdaten(final Long schuelerid, final DTOSchuelerLernabschnittsdaten lernabschnittsdaten)
			throws ApiOperationException {
		if (lernabschnittsdaten == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "lernabschnittsdaten nicht gefunden");
		if (!Objects.equals(schuelerid, lernabschnittsdaten.Schueler_ID))
			throw new ApiOperationException(Status.BAD_REQUEST, "id der lernabschnittsdaten passt nicht zum schueler");
	}

	/**
	 * validiert SchuelerKAoADaten
	 *
	 * @param daten die SchuelerKAoADaten
	 *
	 * @return boolean
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static boolean validate(final SchuelerKAoADaten daten) throws ApiOperationException {
		final KAOAKategorie kategorie = KAOAKategorie.getByID(daten.kategorie);
		validateKategorie(daten, kategorie);

		final KAOAMerkmal merkmal = KAOAMerkmal.getByID(daten.merkmal);
		validateMerkmal(daten, kategorie, merkmal);

		final KAOAZusatzmerkmal zusatzmerkmal = KAOAZusatzmerkmal.getByID(daten.zusatzmerkmal);
		validateZusatzmerkmal(daten, merkmal, zusatzmerkmal);

		final KAOAEbene4 ebene4 = KAOAEbene4.getByID(daten.ebene4);
		validateEbene4(daten, zusatzmerkmal, ebene4);

		final KAOAAnschlussoption anschlussoptionen = KAOAAnschlussoption.getByID(daten.anschlussoption);
		validateAnschlussoption(daten, zusatzmerkmal, anschlussoptionen);

		final KAOABerufsfeld berufsfeld = KAOABerufsfeld.getByID(daten.berufsfeld);
		validateBerufsfeld(daten, zusatzmerkmal, berufsfeld);

		validateJahrgang(daten, kategorie);

		return true;
	}

	/**
	 * validiert Jahrgang
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateJahrgang(final SchuelerKAoADaten daten, final KAOAKategorie kategorie) throws ApiOperationException {
		if (!kategorie.daten.jahrgaenge.contains(daten.jahrgang)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "jahrgaenge ungültig");
		}
	}

	/**
	 * validiert Berufsfeld
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 * @param berufsfeld    KAOABerufsfeld
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateBerufsfeld(final SchuelerKAoADaten daten, final KAOAZusatzmerkmal zusatzmerkmal, final KAOABerufsfeld berufsfeld)
			throws ApiOperationException {
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
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateAnschlussoption(final SchuelerKAoADaten daten, final KAOAZusatzmerkmal zusatzmerkmal,
			final KAOAAnschlussoption anschlussoptionen) throws ApiOperationException {
		if (KAOAZusatzmerkmaleOptionsarten.ANSCHLUSSOPTION.equals(KAOAZusatzmerkmaleOptionsarten.getByKuerzel(zusatzmerkmal.daten.optionsart))) {
			validateValueFound(daten.anschlussoption, anschlussoptionen, "anschlussoption");
			if ((anschlussoptionen != null) && !anschlussoptionen.daten.anzeigeZusatzmerkmal.contains(zusatzmerkmal.daten.kuerzel)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "anschlussoptionen passt nicht zum zusatzmerkmal");
			}
		}
	}

	/**
	 * validiert Ebene4
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 * @param ebene4        KAOAEbene4
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateEbene4(final SchuelerKAoADaten daten, final KAOAZusatzmerkmal zusatzmerkmal, final KAOAEbene4 ebene4)
			throws ApiOperationException {
		if (zusatzmerkmal == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "zusatzmerkmal nicht angegeben");
		if (KAOAZusatzmerkmaleOptionsarten.SBO_EBENE_4.equals(KAOAZusatzmerkmaleOptionsarten.getByKuerzel(zusatzmerkmal.daten.optionsart))) {
			validateValueFound(daten.ebene4, ebene4, "ebene4");
			if ((ebene4 != null) && !ebene4.daten.zusatzmerkmal.equals(zusatzmerkmal.daten.kuerzel)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "ebene4 passt nicht zum zusatzmerkmal");
			}
		}
	}

	/**
	 * validiert Zusatzmerkmal
	 *
	 * @param daten         SchuelerKAoADaten
	 * @param merkmal       KAOAMerkmal
	 * @param zusatzmerkmal KAOAZusatzmerkmal
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateZusatzmerkmal(final SchuelerKAoADaten daten, final KAOAMerkmal merkmal, final KAOAZusatzmerkmal zusatzmerkmal)
			throws ApiOperationException {
		validateValueFound(daten.zusatzmerkmal, zusatzmerkmal, "zusatzmerkmal");
		if ((zusatzmerkmal != null) && !zusatzmerkmal.daten.merkmal.equals(merkmal.daten.kuerzel))
			throw new ApiOperationException(Status.BAD_REQUEST, "zusatzmerkmal passt nicht zur merkmal");
	}

	/**
	 * validiert Merkmal
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 * @param merkmal   KAOAMerkmal
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateMerkmal(final SchuelerKAoADaten daten, final KAOAKategorie kategorie, final KAOAMerkmal merkmal)
			throws ApiOperationException {
		validateValueFound(daten.merkmal, merkmal, "merkmal");
		if ((merkmal != null) && !merkmal.daten.kategorie.equals(kategorie.daten.kuerzel))
			throw new ApiOperationException(Status.BAD_REQUEST, "merkmal passt nicht zur kategorie");
	}

	/**
	 * validiert Kategorie
	 *
	 * @param daten     SchuelerKAoADaten
	 * @param kategorie KAOAKategorie
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateKategorie(final SchuelerKAoADaten daten, final KAOAKategorie kategorie) throws ApiOperationException {
		if (kategorie == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "kategorie nicht gefunden");
	}

	/**
	 * validiert übergebenen Wert
	 *
	 * @param idFromData id aus daten
	 * @param obj        Objekt aus dem enum
	 * @param fieldName  names des Feldes
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	protected static void validateValueFound(final Long idFromData, final Object obj, final String fieldName) throws ApiOperationException {
		if ((idFromData != null) && (obj == null))
			throw new ApiOperationException(Status.BAD_REQUEST, fieldName + NICHT_GEFUNDEN);
	}

}
