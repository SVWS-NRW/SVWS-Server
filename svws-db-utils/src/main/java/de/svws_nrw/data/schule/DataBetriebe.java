package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Betrieb;
import de.svws_nrw.core.data.schule.BetriebeAnsprechpartner;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.db.schema.Schema.tab_K_AllgAdresse;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das CoreDTO {@link Betrieb} */
public final class DataBetriebe extends DataManagerRevised<Long, DTOBetrieb, Betrieb> {

	private final DataBetriebeAnsprechpartner dataBetriebeAnsprechpartner;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Betrieb}.
	 *
	 * @param conn   						die Datenbank-Verbindung für den Datenbankzugriff
	 * @param dataBetriebeAnsprechpartner   DataBetriebeAnsprechpartner
	 */
	public DataBetriebe(final DBEntityManager conn, final DataBetriebeAnsprechpartner dataBetriebeAnsprechpartner) {
		super(conn);
		this.dataBetriebeAnsprechpartner = dataBetriebeAnsprechpartner;
		setAttributesRequiredOnCreation("name");
		setAttributesNotPatchable("id");
	}

	@Override
	protected void initDTO(final DTOBetrieb dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOBetrieb dto) {
		return dto.ID;
	}

	@Override
	public Betrieb getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Betriebs darf nicht null sein.");

		final DTOBetrieb dto = this.conn.queryByKey(DTOBetrieb.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Betrieb mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Betrieb> getAll() {
		final List<DTOBetrieb> betriebe = this.conn.queryAll(DTOBetrieb.class);
		final Set<Long> idsOfReferencedBetriebe = this.getIdsOfReferencedBetriebe(mapToIds(betriebe));
		final Map<Long, List<BetriebeAnsprechpartner>> ansprechpartnerByIdBetrieb = this.dataBetriebeAnsprechpartner
				.getAll()
				.stream()
				.collect(Collectors.groupingBy(a -> a.idBetrieb));

		return betriebe
				.stream()
				.map(this::map)
				.map(b -> setReferenceFlag(b, idsOfReferencedBetriebe))
				.map(b -> addAnsprechpartner(b, ansprechpartnerByIdBetrieb))
				.sorted(Comparator.comparing(b -> b.id))
				.toList();
	}

	@Override
	protected Betrieb map(final DTOBetrieb dto) {
		final Betrieb betrieb = new Betrieb();
		betrieb.id = dto.ID;
		betrieb.name = dto.name1;
		betrieb.nameZusatz = dto.name2;
		betrieb.bemerkungen = dto.bemerkungen;
		betrieb.branche = dto.branche;
		betrieb.idBetriebsart = dto.adressArt;
		betrieb.istAusbildungsbetrieb = Boolean.TRUE.equals(dto.ausbildungsbetrieb);
		betrieb.istMassnahmentraeger = Boolean.TRUE.equals(dto.Massnahmentraeger);
		betrieb.belehrungNachISGErforderlich = Boolean.TRUE.equals(dto.BelehrungISG);
		betrieb.erweitertesFuehrungszeugnisErforderlich = Boolean.TRUE.equals(dto.ErwFuehrungszeugnis);
		betrieb.bietetPraktikumsplaetzeAn = Boolean.TRUE.equals(dto.bietetPraktika);
		betrieb.strasse = dto.strassenname;
		betrieb.hausnummer = dto.hausnr;
		betrieb.hausnummerZusatz = dto.hausnrzusatz;
		betrieb.idOrt = dto.ort_id;
		betrieb.telefon1 = dto.telefon1;
		betrieb.telefon2 = dto.telefon2;
		betrieb.fax = dto.fax;
		betrieb.eMail = dto.email;
		betrieb.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		betrieb.sortierung = Objects.requireNonNullElse(dto.sortierung, 32000);
		return betrieb;
	}

	@Override
	protected void mapAttribute(final DTOBetrieb dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "name" -> updateName(dto, name, value);
			case "idBetriebsart" -> updateIdBetriebsart(dto, name, value);
			case "idOrt" -> updateIdOrt(dto, name, value);
			case "nameZusatz" -> dto.name2 = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrName2.datenlaenge(), name);
			case "bemerkungen" -> dto.bemerkungen = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrBemerkungen.datenlaenge(), name);
			case "branche" -> dto.branche = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrBranche.datenlaenge(), name);
			case "strasse" -> dto.strassenname = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrStrassenname.datenlaenge(), name);
			case "hausnummerZusatz" -> dto.hausnrzusatz = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrHausNrZusatz.datenlaenge(), name);
			case "hausnummer" -> dto.hausnr = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrHausNr.datenlaenge(), name);
			case "telefon1" -> dto.telefon1 = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrTelefon1.datenlaenge(), name);
			case "telefon2" -> dto.telefon2 = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrTelefon2.datenlaenge(), name);
			case "fax" -> dto.fax = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrFax.datenlaenge(), name);
			case "eMail" -> dto.email = JSONMapper.convertToString(value, true, true, tab_K_AllgAdresse.col_AllgAdrEmail.datenlaenge(), name);
			case "istAusbildungsbetrieb" -> dto.ausbildungsbetrieb = JSONMapper.convertToBoolean(value, true, name);
			case "istMassnahmentraeger" -> dto.Massnahmentraeger = JSONMapper.convertToBoolean(value, true, name);
			case "belehrungNachISGErforderlich" -> dto.BelehrungISG = JSONMapper.convertToBoolean(value, true, name);
			case "erweitertesFuehrungszeugnisErforderlich" -> dto.ErwFuehrungszeugnis = JSONMapper.convertToBoolean(value, true, name);
			case "bietetPraktikumsplaetzeAn" -> dto.bietetPraktika = JSONMapper.convertToBoolean(value, true, name);
			case "sortierung" -> dto.sortierung = JSONMapper.convertToInteger(value, true, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOBetrieb> betriebe,
			final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedBetriebe = getIdsOfReferencedBetriebe(mapToIds(betriebe));
		betriebe.stream()
				.filter(f -> idsOfReferencedBetriebe.contains(f.ID))
				.forEach(f -> markResponseAsFailed(responses.get(f.ID), f.name1));
	}

	private void updateIdOrt(final DTOBetrieb dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (id == null) {
			dto.ort_id = null;
			return;
		}

		if (id.equals(dto.ort_id))
			return;

		final DTOOrt ort = this.conn.queryByKey(DTOOrt.class, id);
		if (ort == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Ort zur id %d gefunden.".formatted(id));

		dto.ort_id = id;
	}

	private void updateIdBetriebsart(final DTOBetrieb dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if (id == null) {
			dto.adressArt = null;
			return;
		}

		if (id.equals(dto.adressArt))
			return;

		final DTOBetriebsart betriebsart = this.conn.queryByKey(DTOBetriebsart.class, id);
		if (betriebsart == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Betriebsart zur id %d gefunden.".formatted(id));

		dto.adressArt = id;
	}

	private void updateName(final DTOBetrieb dto, final String name, final Object value) throws ApiOperationException {
		final String newName = JSONMapper.convertToString(value, false, false, tab_K_AllgAdresse.col_AllgAdrName1.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.name1, newName))
			return;

		validateNameIsUnique(dto.ID, newName);

		dto.name1 = newName;
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private void validateNameIsUnique(final Long id, final String name) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn.queryAll(DTOBetrieb.class).stream()
				.anyMatch(b -> (b.ID != id) && name.equalsIgnoreCase(b.name1));
		if (isAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Name %s ist bereits vorhanden.".formatted(name));
	}

	private static Set<Long> mapToIds(final List<DTOBetrieb> betriebe) {
		return betriebe.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedBetriebe(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

		final String query = "SELECT DISTINCT a.Adresse_ID FROM DTOSchuelerAllgemeineAdresse a WHERE a.Adresse_ID IN :ids";
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private Betrieb setReferenceFlag(final Betrieb betrieb, final Set<Long> idsOfReferencedBetriebe) {
		betrieb.referenziertInAnderenTabellen = idsOfReferencedBetriebe.contains(betrieb.id);
		return betrieb;
	}

	private Betrieb addAnsprechpartner(final Betrieb betrieb, final Map<Long, List<BetriebeAnsprechpartner>> ansprechpartnerByIdBetrieb) {
		final List<BetriebeAnsprechpartner> result = ansprechpartnerByIdBetrieb.get(betrieb.id);
		if (result != null)
			betrieb.ansprechpartner = result;
		return betrieb;
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(
				("Der Betrieb mit dem Name %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

	private static void validateId(final DTOBetrieb dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

}
