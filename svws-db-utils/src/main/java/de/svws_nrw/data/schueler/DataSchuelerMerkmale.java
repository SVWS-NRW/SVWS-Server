package de.svws_nrw.data.schueler;

import java.util.Collection;
import java.util.List;

import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerMerkmale;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für das
 * Core-DTO {@link SchuelerSchulbesuchMerkmal}.
 */
public final class DataSchuelerMerkmale extends DataManagerRevised<Long, DTOSchuelerMerkmale, SchuelerSchulbesuchMerkmal> {

	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link SchuelerSchulbesuchMerkmal}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerMerkmale(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	protected long getLongId(final DTOSchuelerMerkmale dto) {
		return dto.ID;
	}


	@Override
	public SchuelerSchulbesuchMerkmal getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das Merkmal des Schülers darf nicht null sein.");
		final DTOSchuelerMerkmale dto = conn.queryByKey(DTOSchuelerMerkmale.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Merkmal eines Schülers mit der Id %d gefunden".formatted(id));
		return map(dto);
	}

	/**
	 * Mapped das übergebene Datenbank-DTO auf das zugehörige Core-DTO
	 *
	 * @param dto   das Datenbank-DTO
	 *
	 * @return das Core-DTO
	 */
	public static SchuelerSchulbesuchMerkmal mapDto(final DTOSchuelerMerkmale dto) {
		final SchuelerSchulbesuchMerkmal merkmal = new SchuelerSchulbesuchMerkmal();
		merkmal.id = dto.ID;
		merkmal.datumVon = dto.DatumVon;
		merkmal.datumBis = dto.DatumBis;
		return merkmal;
	}

	/**
	 * Mapped die übergebenen Datenbank-DTOs auf die zugehörigen Core-DTOs
	 *
	 * @param dtos   die Datenbank-DTOs
	 *
	 * @return die Core-DTOs
	 */
	public static List<SchuelerSchulbesuchMerkmal> mapMultiple(final Collection<DTOSchuelerMerkmale> dtos) {
		return dtos.stream().map(dto -> mapDto(dto)).toList();
	}

	@Override
	protected SchuelerSchulbesuchMerkmal map(final DTOSchuelerMerkmale dto) throws ApiOperationException {
		return mapDto(dto);
	}

}
