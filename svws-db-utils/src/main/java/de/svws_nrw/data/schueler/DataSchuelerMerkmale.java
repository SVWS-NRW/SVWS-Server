package de.svws_nrw.data.schueler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link SchuelerSchulbesuchMerkmal}.
 */
public final class DataSchuelerMerkmale extends DataManagerRevised<Long, DTOSchuelerMerkmale, SchuelerSchulbesuchMerkmal> {

	private static final String ID_SCHUELER = "idSchueler";
	private static final String ID_MERKMAL = "idMerkmal";
	/** Ein Cache für den schnellen Zugriff auf den Katalog der Merkmale */
	private final Map<String, DTOMerkmale> merkmale;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link SchuelerSchulbesuchMerkmal}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerMerkmale(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", ID_SCHUELER);
		setAttributesRequiredOnCreation(ID_MERKMAL, ID_SCHUELER);
		merkmale = conn.queryAll(DTOMerkmale.class).stream().collect(Collectors.toMap(m -> m.kuerzel, m -> m));
	}

	@Override
	protected void initDTO(final DTOSchuelerMerkmale dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOSchuelerMerkmale dto) {
		return dto.ID;
	}

	@Override
	public SchuelerSchulbesuchMerkmal getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das Merkmal des Schülers darf nicht null sein.");
		}

		final DTOSchuelerMerkmale dto = conn.queryByKey(DTOSchuelerMerkmale.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Merkmal eines Schülers mit der Id %d gefunden".formatted(id));
		}

		return map(dto);
	}

	@Override
	protected SchuelerSchulbesuchMerkmal map(final DTOSchuelerMerkmale dto) throws ApiOperationException {
		return map(dto, this.merkmale);
	}

	private static SchuelerSchulbesuchMerkmal map(final DTOSchuelerMerkmale dto, final Map<String, DTOMerkmale> merkmale) {
		final SchuelerSchulbesuchMerkmal merkmal = new SchuelerSchulbesuchMerkmal();
		merkmal.id = dto.ID;
		merkmal.idSchueler = dto.Schueler_ID;
		merkmal.idMerkmal = Optional.ofNullable(dto.Kurztext).map(merkmale::get).map(m -> m.id).orElse(null);
		merkmal.datumVon = dto.DatumVon;
		merkmal.datumBis = dto.DatumBis;
		return merkmal;
	}

	/**
	 * Mapped die übergebenen Datenbank-DTOs auf die zugehörigen Core-DTOs
	 *
	 * @param dtos        die Datenbank-DTOs
	 * @param merkmale    eine Map der Merkmale
	 *
	 * @return die Core-DTOs
	 */
	public static List<SchuelerSchulbesuchMerkmal> mapMultiple(final Collection<DTOSchuelerMerkmale> dtos, final Map<String, DTOMerkmale> merkmale) {
		return dtos.stream()
				.map(dto -> map(dto, merkmale))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOSchuelerMerkmale dto, final String name, final Object value, final Map<String, Object> map)
		throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "ID");
				if (!Objects.equals(dto.ID, id)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
				}
			}
			case ID_SCHUELER -> mapIdSchueler(dto, value, name);
			case ID_MERKMAL -> {
				final Long id = JSONMapper.convertToLong(value, false, ID_MERKMAL);
				dto.Kurztext = merkmale.values().stream().filter(m -> m.id == id).findFirst().map(m -> m.kuerzel)
						.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Zur id %d existiert kein Merkmal.".formatted(id)));
			}
			case "datumVon" -> dto.DatumVon = JSONMapper.convertToString(value, true, true, null, "DatumVon");
			case "datumBis" -> mapDatumBis(dto, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void mapIdSchueler(final DTOSchuelerMerkmale dto, final Object value, final String name) {
		final Long idSchueler = JSONMapper.convertToLong(value, false, name);
		final DTOSchueler dtoSchueler = this.conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Schüler zur id %d gefunden".formatted(idSchueler));
		}
		dto.Schueler_ID = idSchueler;
	}

	private static void mapDatumBis(final DTOSchuelerMerkmale dto, final Object value) throws ApiOperationException {
		final String datum = JSONMapper.convertToString(value, true, true, null, "DatumBis");
		if ((dto.DatumVon != null) && (datum != null)) {
			// Prüfung, ob das Enddatum nach dem Startdatum liegt
			final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			final LocalDate datumVon = LocalDate.parse(dto.DatumVon, formatter);
			final LocalDate datumBis = LocalDate.parse(datum, formatter);
			if (datumBis.isBefore(datumVon)) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Das Enddatum %s darf nicht vor dem Startdatum %s liegen".formatted(datumBis, datumVon));
			}
		}
		dto.DatumBis = datum;
	}
}
