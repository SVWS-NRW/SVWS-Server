package de.svws_nrw.data.schueler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchSchule;
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
	private final Map<String, DTOEntlassarten> mapEntlassarten;

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
		setAttributesRequiredOnCreation("schulnummer");
		setAttributesNotPatchable("id");
		mapEntlassarten = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
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
		return map(dto, this.mapEntlassarten);
	}

	private static SchuelerSchulbesuchSchule map(final DTOSchuelerAbgaenge dto, final Map<String, DTOEntlassarten> mapEntlassarten) {
		final SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
		bisherigeSchule.id = dto.ID;
		bisherigeSchule.schulnummer = dto.AbgangsSchulNr;
		bisherigeSchule.schulgliederung = dto.LSSGL;
		bisherigeSchule.entlassgrundID = Optional.ofNullable(dto.BemerkungIntern).map(mapEntlassarten::get).map(e -> e.ID).orElse(null);
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
	 * @param mapEntlassarten   eine Map mit den Entlassarten für das Mapping
	 *
	 * @return die Core-DTOs
	 */
	public static List<SchuelerSchulbesuchSchule> mapMultiple(final Collection<DTOSchuelerAbgaenge> dtos, final Map<String, DTOEntlassarten> mapEntlassarten) {
		return dtos.stream().map(dto -> map(dto, mapEntlassarten)).toList();
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
			case "schulnummer" -> dto.AbgangsSchulNr = JSONMapper.convertToString(value, false, false, 6, "schulnummer");
			case "schulgliederung" -> dto.LSSGL = JSONMapper.convertToString(value, true, true, 5, "schulgliederung");
			case "entlassgrundID" ->  {
				final Long id = JSONMapper.convertToLong(value, true, "entlassgrundID");
				if (id == null) {
					dto.BemerkungIntern = null;
					return;
				}
				dto.BemerkungIntern = mapEntlassarten.values().stream().filter(e -> e.ID == id).findFirst().map(e -> e.Bezeichnung)
						.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Zur id %d existiert keine Entlassart".formatted(id)));
			}
			case "abschlussartID" -> dto.LSEntlassArt = JSONMapper.convertToString(value, true, true, 2, "abschlussartID");
			case "organisationsFormID" -> dto.OrganisationsformKrz = JSONMapper.convertToString(value, true, true, 1, "organisationsformKrz");
			case "datumVon" -> dto.LSBeginnDatum = JSONMapper.convertToString(value, true, true, null, "datumVon");
			case "datumBis" -> dto.LSSchulEntlassDatum = JSONMapper.convertToString(value, true, true, null, "datumBis");
			case "jahrgangVon" -> dto.LSBeginnJahrgang = JSONMapper.convertToString(value, true, true, 2, "jahrgangVon");
			case "jahrgangBis" -> dto.LSJahrgang = JSONMapper.convertToString(value, true, true, 2, "jahrgangBis");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}
}
