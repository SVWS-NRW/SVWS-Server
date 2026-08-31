package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Ankreuzkompetenz;
import de.svws_nrw.core.data.schule.AnkreuzkompetenzJahrgangszuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schule.katalog.ankreuzkompetenz.AnkreuzkompetenzJahrgangService;
import jakarta.annotation.Nonnull;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Ankreuzkompetenz}.
 */
public final class DataAnkreuzkompetenzen extends DataManagerRevised<Long, DTOAnkreuzfloskeln, Ankreuzkompetenz> {

	private static final String ID_FACH = "idFach";
	private static final String IST_ASV = "istASV";
	private final AnkreuzkompetenzJahrgangService ankreuzkompetenzJahrgangService;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Ankreuzkompetenz}.
	 *
	 * @param conn	 die Datenbankverbindung
	 * @param service   {@link AnkreuzkompetenzJahrgangService}
	 */
	public DataAnkreuzkompetenzen(final DBEntityManager conn, final AnkreuzkompetenzJahrgangService service) {
		super(conn);
		this.ankreuzkompetenzJahrgangService = service;
		setAttributesNotPatchable("id", "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation(IST_ASV, "floskelText");
	}

	@Override
	protected void initDTO(final DTOAnkreuzfloskeln dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.ID = newId;
	}

	@Override
	protected long getLongId(final DTOAnkreuzfloskeln dto) {
		return dto.ID;
	}

	@Override
	public Ankreuzkompetenz getById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Ankreuzkompetenz darf nicht null sein.");
		}

		final DTOAnkreuzfloskeln dto = conn.queryByKey(DTOAnkreuzfloskeln.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Ankreuzkompetenz mit der ID %d wurde nicht gefunden.".formatted(id));
		}

		return this.map(dto);
	}

	@Override
	protected Ankreuzkompetenz map(final DTOAnkreuzfloskeln dto) {
		final Ankreuzkompetenz ankreuzkompetenz = new Ankreuzkompetenz();
		ankreuzkompetenz.id = dto.ID;
		ankreuzkompetenz.istASV = (dto.IstASV == 1);
		ankreuzkompetenz.idFach = ankreuzkompetenz.istASV ? null : dto.Fach_ID;
		ankreuzkompetenz.schulgliederung = dto.Gliederung;
		ankreuzkompetenz.floskelText = Objects.requireNonNullElse(dto.FloskelText, "");
		ankreuzkompetenz.abschnitt = dto.Abschnitt;
		ankreuzkompetenz.istAktiv = Boolean.TRUE.equals(dto.Aktiv);
		ankreuzkompetenz.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		ankreuzkompetenz.fachSortierung = Objects.requireNonNullElse(dto.FachSortierung, 0);
		ankreuzkompetenz.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		return ankreuzkompetenz;
	}

	@Override
	public List<Ankreuzkompetenz> getList() {
		final List<DTOAnkreuzfloskeln> allAnkreuzkompetenzen = this.conn.queryAll(DTOAnkreuzfloskeln.class);

		final Set<Long> idsOfReferencedAnkreuzkompetenzen = getIdsOfReferencedAnkreuzkompetenzen(allAnkreuzkompetenzen
				.stream()
				.map(a -> a.ID)
				.collect(Collectors.toSet()));

		final Map<Long, List<AnkreuzkompetenzJahrgangszuordnung>> zuordnungenById = ankreuzkompetenzJahrgangService.getAllByIdAnkreuzkompetenz();

		return allAnkreuzkompetenzen
				.stream()
				.map(this::map)
				.map(a -> addJahrgangszuordnungen(a, zuordnungenById))
				.map(a -> setReferencedFlag(a, idsOfReferencedAnkreuzkompetenzen))
				.sorted(Comparator.comparing(a -> a.id))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOAnkreuzfloskeln dto, final String name, final Object value, final Map<String, Object> attributes) {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.ID, name, value);
			case ID_FACH -> updateIdFach(dto, name, value, attributes);
			case IST_ASV -> updateASV(dto, name, value, attributes);
			case "schulgliederung" -> updateKuerzelSchulgliederung(dto, name, value);
			case "floskelText" -> updateFloskelText(dto, name, value);
			case "abschnitt" -> updateAbschnitt(dto, name, value);
			case "istAktiv" -> updateAktiv(dto, name, value);
			case "istSichtbar" -> updateSichtbar(dto, name, value);
			case "fachSortierung" -> updateFachSortierung(dto, name, value);
			case "sortierung" -> updateSortierung(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateIdFach(final DTOAnkreuzfloskeln dto, final String name, final Object value, final Map<String, Object> attributes) {
		final Long idFach = JSONMapper.convertToLong(value, true, name);
		final boolean patchContainsIstASV = attributes.containsKey(IST_ASV);

		final boolean istASV = patchContainsIstASV ? JSONMapper.convertToBoolean(attributes.get(IST_ASV), false, IST_ASV) : (dto.IstASV == 1);

		// Wenn ASV aktiv(1) ist, darf kein Fach gesetzt sein
		if ((idFach != null) && istASV) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Einer ASV-Ankreuzkompetenz kann kein Fach zugeordnet werden.");
		}

		// Wenn ASV inaktiv(0), muss ein Fach gesetzt sein
		if ((idFach == null) && !istASV) {
			if (patchContainsIstASV) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Wenn ASV deaktiviert wird, muss ein gültiges Fach ausgewählt werden.");
			}

			throw new ApiOperationException(Status.BAD_REQUEST, "Für diese Ankreuzkompetenz muss ein Fach ausgewählt werden.");
		}

		if (idFach != null) {
			checkFachExists(idFach);
		}

		dto.Fach_ID = idFach;
	}

	private void checkFachExists(final Long idFach) {
		final DTOFach fach = this.conn.queryByKey(DTOFach.class, idFach);
		if (fach == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Kein Fach mit der ID %d gefunden.".formatted(idFach));
		}
	}

	private static void updateASV(final DTOAnkreuzfloskeln dto, final String name, final Object value, final Map<String, Object> attributes) {
		final boolean istASV = JSONMapper.convertToBoolean(value, false, name);
		final boolean patchContainsFach = attributes.containsKey(ID_FACH);

		final Long idFach = patchContainsFach ? JSONMapper.convertToLong(attributes.get(ID_FACH), true, ID_FACH) : dto.Fach_ID;

		if (istASV && (idFach != null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Umstellung auf ASV nicht möglich: Es ist noch ein Fach zugeordnet.");
		}

		if (!istASV && (idFach == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei Deaktivierung von ASV muss gleichzeitig ein Fach im Patch übergeben werden.");
		}

		dto.IstASV = istASV ? 1 : 0;
	}

	private void updateKuerzelSchulgliederung(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		final String kuerzelSchuldgliederung = JSONMapper.convertToString(value, true, false,
				Schema.tab_K_Ankreuzfloskeln.col_Gliederung.datenlaenge(), name);
		if (kuerzelSchuldgliederung == null) {
			dto.Gliederung = null;
			return;
		}

		final Schulgliederung schulgliederung = Schulgliederung.data().getWertBySchluessel(kuerzelSchuldgliederung);
		if (schulgliederung == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schulgliederung mit dem Schlüssel %s gefunden.".formatted(kuerzelSchuldgliederung));
		}

		if (!schulgliederung.hatSchulform(conn.getUser().schuleGetSchuljahr(), conn.getUser().schuleGetSchulform())) {
			throw new ApiOperationException(Status.CONFLICT, "Die Schulgliederung ist für diese Schulform nicht gültig.");
		}

		dto.Gliederung = kuerzelSchuldgliederung;
	}

	private void updateFloskelText(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		final String floskelText = JSONMapper.convertToString(value, false, false, Schema.tab_K_Ankreuzfloskeln.col_FloskelText.datenlaenge(), name);
		if (ValidationUtils.isBlankOrUnchanged(dto.FloskelText, floskelText)) {
			return;
		}

		validateFloskelText(dto.ID, dto.Fach_ID, floskelText);

		dto.FloskelText = floskelText;
	}

	private static void updateAbschnitt(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		dto.Abschnitt = JSONMapper.convertToInteger(value, true, name);
	}

	private static void updateAktiv(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		dto.Aktiv = JSONMapper.convertToBoolean(value, true, name);
	}

	private static void updateSichtbar(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
	}

	private static void updateFachSortierung(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		dto.FachSortierung = JSONMapper.convertToInteger(value, true, name);
	}

	private static void updateSortierung(final DTOAnkreuzfloskeln dto, final String name, final Object value) {
		dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
	}

	private Ankreuzkompetenz addJahrgangszuordnungen(final Ankreuzkompetenz ankreuzkompetenz,
			final Map<Long, List<AnkreuzkompetenzJahrgangszuordnung>> zuordnungenByIdAnkreuzkompetenz) {
		final List<AnkreuzkompetenzJahrgangszuordnung> zuordnungen = zuordnungenByIdAnkreuzkompetenz.get(ankreuzkompetenz.id);
		if (zuordnungen != null) {
			ankreuzkompetenz.jahrgaengezuordnung.addAll(zuordnungen);
		}

		return ankreuzkompetenz;
	}

	private void validateFloskelText(final Long id, final Long idFach, final String floskelText) {
		final boolean isAlreadyUsed = this.conn
				.queryAll(DTOAnkreuzfloskeln.class)
				.stream()
				.anyMatch(sameFloskelTextAndFachID(id, idFach, floskelText));

		if (isAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Floskeltext %s wird für die FachID %d bereits verwendet.".formatted(floskelText, idFach));
		}
	}

	@Nonnull
	private static Predicate<DTOAnkreuzfloskeln> sameFloskelTextAndFachID(final Long id, final Long idFach, final String floskelText) {
		return f -> (f.ID != id) && Objects.equals(f.Fach_ID, idFach) && Strings.CI.equals(floskelText, f.FloskelText);
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOAnkreuzfloskeln> ankreuzkompetenzen,
			final Map<Long, SimpleOperationResponse> mapResponses) {
		final Set<Long> result = getIdsOfReferencedAnkreuzkompetenzen(ankreuzkompetenzen.stream().map(a -> a.ID).collect(Collectors.toSet()));
		ankreuzkompetenzen.stream()
				.filter(a -> result.contains(a.ID))
				.forEach(a -> markResponseAsFailed(mapResponses.get(a.ID), a.FloskelText));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String bezeichnung) {
		response.success = false;
		response.log.add("Die Ankreuzkompetenz mit der Kompetenzbeschreibung %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden"
				.formatted(bezeichnung));
	}

	private Set<Long> getIdsOfReferencedAnkreuzkompetenzen(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT a.Floskel_ID FROM DTOSchuelerAnkreuzfloskeln a WHERE a.Floskel_ID IN :ids";
		final List<Long> results = conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private Ankreuzkompetenz setReferencedFlag(final Ankreuzkompetenz ankreuzkompetenz, final Set<Long> idsOfReferencedAnkreuzkompetenzen) {
		ankreuzkompetenz.referenziertInAnderenTabellen = idsOfReferencedAnkreuzkompetenzen.contains(ankreuzkompetenz.id);
		return ankreuzkompetenz;
	}
}
