package de.svws_nrw.data.schule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

import static de.svws_nrw.db.schema.Schema.tab_Logo;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Logo}.
 */
public final class DataLogoverwaltung extends DataManagerRevised<Long, DTOLogo, Logo> {

	private static final String ID = "id";
	private static final String KENNUNG = "kennung";
	private static final String MIME_TYPE = "mimeType";
	private static final String LOGO_BASE64 = "logoBase64";
	private static final String HINZUGEFUEGT_AM = "hinzugefuegtAm";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Logo}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataLogoverwaltung(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation(MIME_TYPE, LOGO_BASE64, KENNUNG, HINZUGEFUEGT_AM);
		setAttributesNotPatchable(ID, KENNUNG);
	}

	@Override
	protected void initDTO(final DTOLogo dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.id = newID;
	}

	@Override
	protected long getLongId(final DTOLogo dto) {
		return dto.id;
	}

	@Override
	public Logo getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Logos darf nicht null sein.");
		}
		final DTOLogo dto = this.conn.queryByKey(DTOLogo.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Logo mit der ID %d gefunden.".formatted(id));
		}
		return map(dto);
	}

	@Override
	public List<Logo> getAll() {
		final DTOEigeneSchule eigeneSchuleSchulform = conn.querySingle(DTOEigeneSchule.class);
		final Schulform schulform = (eigeneSchuleSchulform != null)
				? Schulform.data().getWertByKuerzel(eigeneSchuleSchulform.SchulformKuerzel) : null;

		final List<ReportingBildDefinition> validDefinitionen = ReportingBildDefinition.getBySchulform(schulform);

		final Set<String> validKennungen = validDefinitionen.stream()
				.map(ReportingBildDefinition::getKennung)
				.collect(Collectors.toSet());

		return this.conn.queryAll(DTOLogo.class).stream()
				.filter(dto -> validKennungen.contains(dto.kennung.getKennung()))
				.map(this::map)
				.toList();
	}

	@Override
	public Logo map(final DTOLogo dto) {
		final Logo logo = new Logo();
		final String kennung = dto.kennung.getKennung();
		final ReportingBildDefinition definition = ReportingBildDefinition.getByKennung(kennung);

		logo.id = dto.id;
		logo.kennung = Objects.requireNonNullElse(kennung, "");
		logo.bezeichnung = definition.getBezeichnung();
		logo.beschreibung = definition.getBeschreibung();
		logo.logoBase64 = Objects.requireNonNullElse(dto.logoBase64, "");
		logo.mimeType = Objects.requireNonNullElse(dto.mimeType, "");
		logo.breitePX = dto.breitePX;
		logo.hoehePX = dto.hoehePX;
		logo.breiteMM = dto.breiteMM;
		logo.hoeheMM = dto.hoeheMM;
		logo.hinzugefuegtAm = Objects.requireNonNullElse(dto.hinzugefuegtAm, "");

		return logo;
	}

	@Override
	protected void mapAttribute(final DTOLogo dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case ID -> ValidationUtils.validateId(dto.id, name, value);
			case KENNUNG -> dto.kennung = getKennung(value);
			case LOGO_BASE64 -> dto.logoBase64 = JSONMapper.convertToString(value, false, false, tab_Logo.col_Logo_Base64.datenlaenge(), name);
			case MIME_TYPE -> dto.mimeType = JSONMapper.convertToString(value, false, false, tab_Logo.col_MimeType.datenlaenge(), name);
			case "breitePX" -> dto.breitePX = JSONMapper.convertToInteger(value, true, name);
			case "hoehePX" -> dto.hoehePX = JSONMapper.convertToInteger(value, true, name);
			case "breiteMM" -> dto.breiteMM = JSONMapper.convertToInteger(value, true, name);
			case "hoeheMM" -> dto.hoeheMM = JSONMapper.convertToInteger(value, true, name);
			case HINZUGEFUEGT_AM -> updateCreationDate(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private ReportingBildDefinition getKennung(final Object value) {
		return ReportingBildDefinition.getByKennung(JSONMapper.convertToString(value, false, false, tab_Logo.col_Kennung.datenlaenge()));
	}

	private void updateCreationDate(final DTOLogo dto, final String name, final Object value) {
		final String dateNew = JSONMapper.convertToString(value, false, false, tab_Logo.col_Hinzugefuegt_Am.datenlaenge(), name);
		final String dateToday = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		if (dateNew.equals((dateToday))) {
			dto.hinzugefuegtAm = dateNew;
		} else {
			throw new ApiOperationException(Status.BAD_REQUEST, "Neues Datum %s entspricht nicht dem heutigen Datum %s.".formatted(dateNew, dateToday));
		}
	}
}
