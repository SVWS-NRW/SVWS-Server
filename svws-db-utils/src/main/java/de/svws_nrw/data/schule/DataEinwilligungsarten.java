package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schule.Einwilligungsschluessel;
import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Einwilligungsart}.
 */
public final class DataEinwilligungsarten extends DataManagerRevised<Long, DTOKatalogEinwilligungsart, Einwilligungsart> {

	private static final String BEZEICHNUNG = "bezeichnung";
	private static final String ID_PERSON_TYP = "idPersonTyp";
	private static final String SCHLUESSEL = "schluessel";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Einwilligungsart}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataEinwilligungsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation(BEZEICHNUNG, ID_PERSON_TYP);
		setAttributesNotPatchable("id");
	}

	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		validateBezeichnungisUniqueForThisPersonTypOnCreation(newID, initAttributes);
	}

	@Override
	protected long getLongId(final DTOKatalogEinwilligungsart dto) {
		return dto.ID;
	}

	@Override
	public Einwilligungsart map(final DTOKatalogEinwilligungsart dto) {
		final Einwilligungsart daten = new Einwilligungsart();
		daten.id = dto.ID;
		daten.bezeichnung = Objects.requireNonNullElse(dto.Bezeichnung, "");
		daten.schluessel = Objects.requireNonNullElse(dto.Schluessel, "");
		daten.beschreibung = Objects.requireNonNullElse(dto.Beschreibung, "");
		daten.idPersonTyp = (dto.personTyp == null) ? -1 : dto.personTyp.id;
		daten.sortierung = dto.Sortierung;
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return daten;
	}

	@Override
	public List<Einwilligungsart> getAll() {
		final List<DTOKatalogEinwilligungsart> einwilligungsarten = this.conn.queryAll(DTOKatalogEinwilligungsart.class);
		final Set<Long> idsEinwilligungsarten = this.mapToIds(einwilligungsarten);
		final Set<Long> idsOfReferencedEinwilligungsarten = this.getIdsOfReferencedEinwilligungsarten(idsEinwilligungsarten);

		return einwilligungsarten
				.stream()
				.map(this::map)
				.map(e -> setReferencedFlag(e, idsOfReferencedEinwilligungsarten))
				.sorted(Comparator.comparing(e -> e.id))
				.toList();
	}

	@Override
	public Einwilligungsart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Einwilligungsart darf nicht null sein.");

		final DTOKatalogEinwilligungsart einwilligungsart = conn.queryByKey(DTOKatalogEinwilligungsart.class, id);
		if (einwilligungsart == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Einwilligungsart mit der ID %d wurde nicht gefunden.".formatted(id));

		return map(einwilligungsart);
	}

	@Override
	protected void initDTO(final DTOKatalogEinwilligungsart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Sortierung = 32000;
	}

	@Override
	protected Einwilligungsart addBasic(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Einwilligungsart ea = super.addBasic(newID, initAttributes);

		if (ea.idPersonTyp == PersonTyp.LEHRER.id)
			persistEinwilligungen("SELECT e.ID FROM DTOLehrer e", id -> new DTOLehrerDatenschutz(id, ea.id, false, false));
		else if (ea.idPersonTyp == PersonTyp.SCHUELER.id)
			persistEinwilligungen("SELECT e.ID FROM DTOSchueler e", id -> new DTOSchuelerDatenschutz(id, ea.id, false, false));

		return ea;
	}

	private <T> void persistEinwilligungen(final String query, final Function<Long, T> dtoMapper) {
		final List<Long> ids = conn.queryList(query, Long.class);
		final List<T> einwilligungen = ids.stream().map(dtoMapper).toList();
		conn.transactionPersistAll(einwilligungen);
		conn.transactionFlush();
	}

	@Override
	protected void mapAttribute(final DTOKatalogEinwilligungsart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case BEZEICHNUNG -> updateBezeichnung(dto, value, name);
			case SCHLUESSEL -> updateSchluessel(dto, value, name, map.get(ID_PERSON_TYP));
			case ID_PERSON_TYP -> updatePersonTyp(dto, value, name);
			case "beschreibung" -> updateBeschreibung(dto, value, name);
			case "istSichtbar" -> updateSichtbar(dto, value, name);
			case "sortierung" -> updateSortierung(dto, value, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void validateBezeichnungisUniqueForThisPersonTypOnCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Integer idPersonTyp = JSONMapper.convertToInteger(initAttributes.get(ID_PERSON_TYP), false, ID_PERSON_TYP);
		final PersonTyp personTyp = PersonTyp.getByID(idPersonTyp);
		if (personTyp == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für die idPersonTyp %d existiert kein PersonTyp".formatted(idPersonTyp));

		final String bezeichnung = JSONMapper.convertToString(initAttributes.get(BEZEICHNUNG), false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge(), BEZEICHNUNG);
		validateBezeichnung(newID, personTyp, bezeichnung);
	}

	private static void updateBeschreibung(final DTOKatalogEinwilligungsart dto, final Object value, final String name) throws ApiOperationException {
		dto.Beschreibung = JSONMapper.convertToString(value, true, true, null, name);
	}

	private static void validateId(final DTOKatalogEinwilligungsart dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private void updateBezeichnung(final DTOKatalogEinwilligungsart dto, final Object value, final String name)
			throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung))
			return;

		validateBezeichnung(dto.ID, dto.personTyp, bezeichnung);

		dto.Bezeichnung = bezeichnung;
	}

	private void updateSchluessel(final DTOKatalogEinwilligungsart dto, final Object value, final String name, final Object idPersonTyp) throws ApiOperationException {
		final String schluessel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Schluessel.datenlaenge(), name);
		if (schluessel == null) {
			dto.Schluessel = null;
			return;
		}

		if (valueIsBlankOrHasNotChanged(dto.Schluessel, schluessel))
			return;

		if (schluesselIsAlreadyUsed(dto.ID, idPersonTyp, schluessel))
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Schlüssel %s wird bereits verwendet.".formatted(schluessel));

		if (noMatchingCoreTypeFound(schluessel))
			throw new ApiOperationException(Status.NOT_FOUND,
					"Zum angegebenen Schlüssel %s wurde keine passende Einwilligungsart gefunden.".formatted(schluessel));

		dto.Schluessel = schluessel;
	}

	private void updatePersonTyp(final DTOKatalogEinwilligungsart dto, final Object value, final String name)
			throws ApiOperationException {
		final int id = JSONMapper.convertToInteger(value, false, name);
		final PersonTyp personTyp = PersonTyp.getByID(id);
		if (personTyp == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Kein PersonTyp zur ID %d gefunden.".formatted(id));
		if (personTyp == PersonTyp.ERZIEHER)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der PersonTyp Erzieher wird derzeit nicht unterstützt.");

		dto.personTyp = personTyp;
	}

	private void validateBezeichnung(final Long id, final PersonTyp personTyp, final String bezeichnung) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn.queryAll(DTOKatalogEinwilligungsart.class).stream()
				.anyMatch(e -> (e.ID != id) && (e.personTyp == personTyp) && bezeichnung.equalsIgnoreCase(e.Bezeichnung));
		if (isAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
	}

	private static boolean noMatchingCoreTypeFound(final String schluessel) {
		return (Einwilligungsschluessel.data().getWertBySchluessel(schluessel) == null);
	}

	private static void updateSichtbar(final DTOKatalogEinwilligungsart dto, final Object value, final String name) throws ApiOperationException {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}

	private static void updateSortierung(final DTOKatalogEinwilligungsart dto, final Object value, final String name) throws ApiOperationException {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private Set<Long> mapToIds(final List<DTOKatalogEinwilligungsart> einwilligungsarten) {
		return einwilligungsarten.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Einwilligungsart setReferencedFlag(final Einwilligungsart einwilligungsart, final Set<Long> idsOfReferencedEinwilligungsarten) {
		einwilligungsart.referenziertInAnderenTabellen = idsOfReferencedEinwilligungsarten.contains(einwilligungsart.id);
		return einwilligungsart;
	}

	private Set<Long> getIdsOfReferencedEinwilligungsarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

		final String query1 = "SELECT DISTINCT a.Datenschutz_ID FROM DTOSchuelerDatenschutz a WHERE a.Datenschutz_ID IN :ids";
		final String query2 = "SELECT DISTINCT b.DatenschutzID FROM DTOLehrerDatenschutz b WHERE b.DatenschutzID IN :ids";
		final String query3 = "SELECT DISTINCT c.DatenschutzID FROM DTOErzieherDatenschutz c WHERE c.DatenschutzID IN :ids";
		final String query = String.join("\nUNION\n", query1, query2, query3);
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private boolean schluesselIsAlreadyUsed(final Long id, final Object idPersonTyp, final String schluessel) throws ApiOperationException {
		if (idPersonTyp == null)
			return false;

		final int personTyp = JSONMapper.convertToInteger(idPersonTyp, false);
		return this.conn.queryAll(DTOKatalogEinwilligungsart.class).stream()
				.anyMatch(e -> (e.ID != id) && (e.personTyp.id == personTyp) && schluessel.equalsIgnoreCase(e.Schluessel));
	}

}
