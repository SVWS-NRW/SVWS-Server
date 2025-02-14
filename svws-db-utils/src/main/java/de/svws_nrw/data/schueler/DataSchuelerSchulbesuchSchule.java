package de.svws_nrw.data.schueler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerAbgaenge;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für das
 * Core-DTO {@link SchuelerSchulbesuchSchule}.
 */
public final class DataSchuelerSchulbesuchSchule extends DataManagerRevised<Long, DTOSchuelerAbgaenge, SchuelerSchulbesuchSchule> {

	/** Ein Cache für den schnellen Zugriff auf den Katalog der Entlassarten. */
	private final Map<String, DTOEntlassarten> mapEntlassarten;


	/**
	 * Erstellt einen neuen {@link DataManager} für das Core-DTO {@link SchuelerSchulbesuchSchule}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerSchulbesuchSchule(final DBEntityManager conn) {
		super(conn);
		mapEntlassarten = conn.queryAll(DTOEntlassarten.class).stream().collect(Collectors.toMap(e -> e.Bezeichnung, e -> e));
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

	/**
	 * Mapped das übergebene Datenbank-DTO auf das zugehörige Core-DTO
	 *
	 * @param dto               das Datenbank-DTO
	 * @param mapEntlassarten   eine Map mit den Entlassarten für das Mapping
	 *
	 * @return das Core-DTO
	 */
	public static SchuelerSchulbesuchSchule mapDto(final DTOSchuelerAbgaenge dto, final Map<String, DTOEntlassarten> mapEntlassarten) {
		final SchuelerSchulbesuchSchule bisherigeSchule = new SchuelerSchulbesuchSchule();
		bisherigeSchule.id = dto.ID;
		bisherigeSchule.schulnummer = dto.AbgangsSchulNr;
		bisherigeSchule.schulgliederung = dto.LSSGL;
		final DTOEntlassarten tmpEntlassgrund = (dto.BemerkungIntern == null) ? null : mapEntlassarten.get(dto.BemerkungIntern);
		bisherigeSchule.entlassgrundID = (tmpEntlassgrund == null) ? null : tmpEntlassgrund.ID;
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
		return dtos.stream().map(dto -> mapDto(dto, mapEntlassarten)).toList();
	}


	@Override
	protected SchuelerSchulbesuchSchule map(final DTOSchuelerAbgaenge dto) throws ApiOperationException {
		return mapDto(dto, this.mapEntlassarten);
	}

}
