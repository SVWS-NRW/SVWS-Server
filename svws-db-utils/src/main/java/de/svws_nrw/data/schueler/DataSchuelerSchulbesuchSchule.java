package de.svws_nrw.data.schueler;

import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link SchuelerSchulbesuchSchule}.
 */
public final class DataSchuelerSchulbesuchSchule extends DataManagerRevised<Long, DTOSchuelerAbgaenge, SchuelerSchulbesuchSchule> {

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Entlassarten. */
	private final Map<String, DTOEntlassarten> entlassartenByBezeichnung;

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Schulen */
	private final Map<String, DTOSchuleNRW> schulenBySchulnummer;

	/** die Id des Schülers - benötigt zum Persistieren neuer DTOSchuelerAbgaenge */
	private final Long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchuelerSchulbesuchSchule}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchSchule(final DBEntityManager conn) {
		this(conn, null);
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchuelerSchulbesuchSchule}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülers - benötigt zum Persistieren neuer DTOSchuelerAbgaenge
	 */
	public DataSchuelerSchulbesuchSchule(final DBEntityManager conn, final Long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
		setAttributesRequiredOnCreation("idSchule");
		setAttributesNotPatchable("id");
		this.entlassartenByBezeichnung = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
		this.schulenBySchulnummer = conn.queryAll(DTOSchuleNRW.class).stream().collect(Collectors.toMap(s -> s.SchulNr, s -> s));
	}

	@Override
	protected void initDTO(final DTOSchuelerAbgaenge dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
		if (this.idSchueler == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID des Schuelers darf nicht null sein.");
		dto.Schueler_ID = this.idSchueler;
	}

	@Override
	protected long getLongId(final DTOSchuelerAbgaenge dto) {
		return dto.ID;
	}

	@Override
	public SchuelerSchulbesuchSchule getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Schulbesuch eines Schülers darf nicht null sein.");

		final DTOSchuelerAbgaenge dto = conn.queryByKey(DTOSchuelerAbgaenge.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Schulbesuch eines Schülers mit der Id %d gefunden".formatted(id));

		return map(dto);
	}

	@Override
	protected SchuelerSchulbesuchSchule map(final DTOSchuelerAbgaenge dto) throws ApiOperationException {
		return map(dto, this.entlassartenByBezeichnung, this.schulenBySchulnummer);
	}

	private static SchuelerSchulbesuchSchule map(final DTOSchuelerAbgaenge dto, final Map<String, DTOEntlassarten> entlassartenByBezeichnung,
			final Map<String, DTOSchuleNRW> schulenBySchulnummer) {
		final SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
		bisherigeSchule.id = dto.ID;
		final DTOSchuleNRW schule = schulenBySchulnummer.get(dto.AbgangsSchulNr);
		bisherigeSchule.idSchule = (schule != null) ? schule.ID : null;
		bisherigeSchule.schulgliederung = dto.LSSGL;
		bisherigeSchule.entlassgrundID = Optional.ofNullable(dto.BemerkungIntern).map(entlassartenByBezeichnung::get).map(e -> e.ID).orElse(null);
		bisherigeSchule.abschlussartID = dto.LSEntlassArt;
		bisherigeSchule.organisationsFormID = dto.OrganisationsformKrz;
		bisherigeSchule.datumVon = dto.LSBeginnDatum;
		bisherigeSchule.datumBis = dto.LSSchulEntlassDatum;
		bisherigeSchule.jahrgangVon = dto.LSBeginnJahrgang;
		bisherigeSchule.jahrgangBis = dto.LSJahrgang;
		return bisherigeSchule;
	}

	/**
	 * Mapped die übergebenen Datenbank-DTOs auf die zugehörigen Core-DTOs
	 *
	 * @param dtos              die Datenbank-DTOs
	 * @param entlassartenByBezeichnung   eine Map mit den Entlassarten für das Mapping
	 * @param schulenBySchulnummer		  eine Map mit den Schulen für das Mapping
	 *
	 * @return die Core-DTOs
	 */
	public static List<SchuelerSchulbesuchSchule> mapMultiple(final Collection<DTOSchuelerAbgaenge> dtos,
			final Map<String, DTOEntlassarten> entlassartenByBezeichnung, final Map<String, DTOSchuleNRW> schulenBySchulnummer) {
		return dtos.stream().map(dto -> map(dto, entlassartenByBezeichnung, schulenBySchulnummer)).toList();
	}

	@Override
	protected void mapAttribute(final DTOSchuelerAbgaenge dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST, "PatchId %d ist ungleich dtoID %d.".formatted(id, dto.ID));
			}
			case "idSchule" -> mapSchulnummer(dto, value);
			case "schulgliederung" -> dto.LSSGL = JSONMapper.convertToString(value, true, true, 5, "schulgliederung");
			case "entlassgrundID" -> mapEntlassgrund(dto, value);
			case "abschlussartID" -> dto.LSEntlassArt = JSONMapper.convertToString(value, true, true, 2, "abschlussartID");
			case "organisationsFormID" -> dto.OrganisationsformKrz = JSONMapper.convertToString(value, true, true, 1, "organisationsformKrz");
			case "datumVon" -> dto.LSBeginnDatum = JSONMapper.convertToString(value, true, true, null, "datumVon");
			case "datumBis" -> mapDatumBis(dto, value);
			case "jahrgangVon" -> dto.LSBeginnJahrgang = JSONMapper.convertToString(value, true, true, 2, "jahrgangVon");
			case "jahrgangBis" -> dto.LSJahrgang = JSONMapper.convertToString(value, true, true, 2, "jahrgangBis");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void mapDatumBis(final DTOSchuelerAbgaenge dto, final Object value) throws ApiOperationException {
		final String datum = JSONMapper.convertToString(value, true, true, null, "DatumBis");
		if ((dto.LSBeginnDatum != null) && (datum != null)) {
			// Prüfung, ob das Enddatum nach dem Startdatum liegt
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			final LocalDate datumVon = LocalDate.parse(dto.LSBeginnDatum, formatter);
			final LocalDate datumBis = LocalDate.parse(datum, formatter);
			if (datumBis.isBefore(datumVon))
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Enddatum %s darf nicht vor dem Startdatum %s liegen".formatted(datumBis, datumVon));
		}
		dto.LSSchulEntlassDatum = datum;
	}

	private void mapSchulnummer(final DTOSchuelerAbgaenge dto, final Object value) throws ApiOperationException {
		final Long idSchule = JSONMapper.convertToLong(value, false, "idSchule");

		dto.AbgangsSchulNr = this.schulenBySchulnummer.values().stream().filter(s -> Objects.equals(s.ID, idSchule)).findFirst()
				.map(s -> s.SchulNr)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine Schule mit der ID %d gefunden.".formatted(idSchule)));
	}

	private void mapEntlassgrund(final DTOSchuelerAbgaenge dto, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, true, "entlassgrundID");
		if (id == null) {
			dto.BemerkungIntern = null;
			return;
		}
		dto.BemerkungIntern = entlassartenByBezeichnung.values().stream().filter(e -> e.ID == id).findFirst().map(e -> e.Bezeichnung)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Zur id %d existiert keine Entlassart".formatted(id)));
	}
}
