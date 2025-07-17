package de.svws_nrw.data.erzieher;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.erzieher.ErzieherListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.data.DataManagerRevised;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link ErzieherListeEintrag}.
 */
public final class DataErzieherliste extends DataManagerRevised<Long, DTOSchuelerErzieherAdresse, ErzieherListeEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ErzieherListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataErzieherliste(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	protected ErzieherListeEintrag map(final DTOSchuelerErzieherAdresse dto) throws ApiOperationException {
		if ((dto.Name1 != null) && !dto.Name1.isBlank())
			return dtoMapperErzieher1.apply(dto);
		if ((dto.Name2 != null) && !dto.Name2.isBlank())
			return dtoMapperErzieher2.apply(dto);
		throw new ApiOperationException(Status.NOT_FOUND);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln des ersten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherListeEintrag}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher1 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
		eintrag.id = (e.ID * 10) + 1;
		eintrag.idSchueler = e.Schueler_ID;
		eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.anrede = e.Anrede1;
		eintrag.name = e.Name1;
		eintrag.vorname = e.Vorname1;
		eintrag.email = e.ErzEmail;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Umwandeln des zweiten Erziehers eines Datenbank-DTOs {@link DTOSchuelerErzieherAdresse} in einen Core-DTO {@link ErzieherListeEintrag}.
	 */
	private final Function<DTOSchuelerErzieherAdresse, ErzieherListeEintrag> dtoMapperErzieher2 = (final DTOSchuelerErzieherAdresse e) -> {
		final ErzieherListeEintrag eintrag = new ErzieherListeEintrag();
		eintrag.id = (e.ID * 10) + 2;
		eintrag.idSchueler = e.Schueler_ID;
		eintrag.idErzieherArt = e.ErzieherArt_ID;
		eintrag.anrede = e.Anrede2;
		eintrag.name = e.Name2;
		eintrag.vorname = e.Vorname2;
		eintrag.email = e.ErzEmail2;
		return eintrag;
	};

	@Override
	public List<ErzieherListeEintrag> getAll() throws ApiOperationException {
		final List<DTOSchuelerErzieherAdresse> erzieher = conn.queryAll(DTOSchuelerErzieherAdresse.class);
		if (erzieher == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final List<ErzieherListeEintrag> daten = erzieher.stream().filter(e -> ((e.Name1 != null) && !e.Name1.trim().isEmpty())).map(dtoMapperErzieher1)
				.collect(Collectors.toList());
		daten.addAll(erzieher.stream().filter(e -> ((e.Name2 != null) && !e.Name2.trim().isEmpty())).map(dtoMapperErzieher2).toList());

		return daten;
	}

}
