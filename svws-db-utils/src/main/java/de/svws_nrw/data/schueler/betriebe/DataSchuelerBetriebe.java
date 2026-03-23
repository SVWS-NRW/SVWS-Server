package de.svws_nrw.data.schueler.betriebe;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;

import de.svws_nrw.asd.data.schueler.SchuelerBetrieb;
import de.svws_nrw.core.data.schule.Betrieb;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.util.ValidationUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebeAnsprechpartner;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerBetrieb;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static de.svws_nrw.db.schema.Schema.tab_Schueler_AllgAdr;
import static jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Betrieb}.
 */
public final class DataSchuelerBetriebe extends DataManagerRevised<Long, DTOSchuelerBetrieb, SchuelerBetrieb> {


	/**
	 * Erstellt einen neuen DataSchuelerBetriebe mit der angegeben Verbindung.
	 *
	 * @param conn        DBEntityManager
	 */
	public DataSchuelerBetriebe(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("idSchueler", "idBetrieb");
	}

	@Override
	protected void initDTO(final DTOSchuelerBetrieb dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.id = newId;
	}

	@Override
	protected long getLongId(final DTOSchuelerBetrieb dto) {
		return dto.id;
	}

	@Override
	public SchuelerBetrieb getById(final Long id) {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Id darf nicht null sein.");
		}
		return Optional.ofNullable(this.conn.queryByKey(DTOSchuelerBetrieb.class, id))
				.map(this::map)
				.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Keine SchuelerBetriebsdaten mit der ID %d gefunden".formatted(id)));
	}

	@Override
	protected SchuelerBetrieb map(final DTOSchuelerBetrieb dto) throws ApiOperationException {
		final SchuelerBetrieb result = new SchuelerBetrieb();
		result.id = dto.id;
		result.idSchueler = dto.idSchueler;
		result.idBetrieb = dto.idBetrieb;
		result.idAnsprechpartner = dto.idAnsprechpartner;
		result.idBetreuungslehrer = dto.idBetreuungslehrer;
		result.idBeschaeftigungsart = dto.idBeschaeftigungsart;
		result.nameAusbilder = dto.nameAusbilder;
		result.vertragsbeginn = dto.vertragsbeginn;
		result.vertragsende = dto.vertragsende;
		result.erhaeltAnschreiben = Boolean.TRUE.equals(dto.erhaeltAnschreiben);
		result.istPraktikum = Boolean.TRUE.equals(dto.istPraktikum);
		result.sortierung = Objects.requireNonNullElse(dto.sortierung, 32000);
		return result;
	}

	/**
	 * Gibt eine Liste der Betriebe zurück, die dem Schüler der angegeben ID zugeordnet sind.
	 *
	 * @param idSchueler        idSchueler
	 *
	 * @return                  die Liste der Betriebe, die dem Schüler der angegeben ID zugeordnet sind
	 *
	 */
	public Response getAllAsResponseByIdSchueler(final Long idSchueler) {
		final List<SchuelerBetrieb> result = this.conn.queryList(DTOSchuelerBetrieb.QUERY_BY_IDSCHUELER, DTOSchuelerBetrieb.class, idSchueler).stream()
				.map(this::map)
				.sorted(Comparator.comparing(b -> b.id))
				.toList();

		return Response
				.status(Status.OK)
				.type(MediaType.APPLICATION_JSON)
				.entity(result)
				.build();
	}

	@Override
	protected void mapAttribute(final DTOSchuelerBetrieb dto, final String name, final Object value, final Map<String, Object> map) {
		switch (name) {
			case "id" -> ValidationUtils.validateId(dto.id, name, value);
			case "idSchueler" -> this.updateRequiredForeignKey(name, value, DTOSchueler.class, "Kein Schüler mit der id %d gefunden.", id -> dto.idSchueler = id);
			case "idBetrieb" -> this.updateRequiredForeignKey(name, value, DTOBetrieb.class, "Kein Betrieb mit der id %d gefunden.", id -> dto.idBetrieb = id);
			case "idAnsprechpartner" -> this.updateNonRequiredForeignKey(name, value, DTOBetriebeAnsprechpartner.class, "Kein Ansprechpartner mit der id %d gefunden.", id -> dto.idAnsprechpartner = id);
			case "idBetreuungslehrer" -> this.updateNonRequiredForeignKey(name, value, DTOLehrer.class, "Kein Lehrer mit der id %d gefunden.", id -> dto.idBetreuungslehrer = id);
			case "idBeschaeftigungsart" -> this.updateNonRequiredForeignKey(name, value, DTOBeschaeftigungsart.class, "Keine Beschäftigungsart mit der id %d gefunden.", id -> dto.idBeschaeftigungsart = id);
			case "vertragsbeginn" -> updateDatum(dto, name, value,  d -> d.vertragsbeginn, (d, v) -> d.vertragsbeginn = v);
			case "vertragsende" -> updateDatum(dto, name, value, d -> d.vertragsende, (d, v) -> d.vertragsende = v);
			case "nameAusbilder" -> dto.nameAusbilder = JSONMapper.convertToString(value, true, true, tab_Schueler_AllgAdr.col_Ausbilder.datenlaenge(), name);
			case "erhaeltAnschreiben" -> dto.erhaeltAnschreiben = JSONMapper.convertToBoolean(value, true, name);
			case "istPraktikum" -> dto.istPraktikum = JSONMapper.convertToBoolean(value, true, name);
			case "sortierung" -> dto.sortierung = JSONMapper.convertToInteger(value, true, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateRequiredForeignKey(final String name, final Object value, final Class<?> dtoClass, final String errorMessage,
			final LongConsumer dtoSetter) {
		final long id = JSONMapper.convertToLong(value, false, name);
		if (this.conn.queryByKey(dtoClass, id) == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, errorMessage.formatted(id));
		}
		dtoSetter.accept(id);
	}

	@SuppressWarnings("java:S4276") // LongConsumer cannot handle null values, which are valid here
	private void updateNonRequiredForeignKey(final String name, final Object value, final Class<?> dtoClass, final String errorMessage,
			final Consumer<Long> dtoSetter) {
		final Long id = JSONMapper.convertToLong(value, true, name);
		if ((id != null) && (this.conn.queryByKey(dtoClass, id) == null)) {
			throw new ApiOperationException(Status.BAD_REQUEST, errorMessage.formatted(id));
		}
		dtoSetter.accept(id);
	}

	private void updateDatum(final DTOSchuelerBetrieb dto, final String name, final Object value,
			final Function<DTOSchuelerBetrieb, String> getter, final BiConsumer<DTOSchuelerBetrieb, String> setter) {
		final String datum = JSONMapper.convertToString(value, true, true, null, name);
		this.valiDate(getter.apply(dto), datum, name);
		setter.accept(dto, datum);
	}

	private void valiDate(final String oldDate, final String newDate, final String name) throws ApiOperationException {
		if (ValidationUtils.isBlankOrUnchanged(oldDate, newDate)) {
			return;
		}
		try {
			LocalDate.parse(newDate);
		} catch (final Exception ignored) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Feld: %s: Das Datumsformat für %s ist ungültig".formatted(name, newDate));
		}
	}
}
