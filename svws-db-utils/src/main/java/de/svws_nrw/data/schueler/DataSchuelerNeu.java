package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.SchuelerNeu;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchPatchRequest;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
import jakarta.ws.rs.core.Response;
import org.openapitools.jackson.nullable.JsonNullable;

/** DataManager zum Erstellen eines Schülers. */
public final class DataSchuelerNeu {

	private final SchuelerStammdatenService schuelerStammdatenService;
	private final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;
	private final SchuelerSchulbesuchService schuelerSchulbesuchService;
	private final DataSchuelerEinwilligungen dataSchuelerEinwilligungen;
	private final DataSchuelerLernplattformen dataSchuelerLernplattformen;
	private final DataLernplattformen dataLernplattformen;
	private final DataEinwilligungsarten dataEinwilligungsarten;

	private static final String ID_SCHULJAHRESABSCHNITT = "idSchuljahresabschnitt";

	/**
	 * Erstellt einen neuen {@link DataSchuelerNeu} für das Core-DTO {@link SchuelerNeu}
	 *
	 * @param schuelerStammdatenService			DataSchuelerStammdaten
	 * @param dataSchuelerLernabschnittsdaten	DataSchuelerLernabschnittsdaten
	 * @param schuelerSchulbesuchService				SchulbesuchService
	 * @param dataSchuelerEinwilligungen		DataSchuelerEinwilligungen
	 * @param dataSchuelerLernplattformen		DataSchuelerLernplattformen
	 * @param dataLernplattformen				DataLernplattformen
	 * @param dataEinwilligungsarten			DataEinwilligungsarten
	 */
	public DataSchuelerNeu(
			final SchuelerStammdatenService schuelerStammdatenService,
			final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten,
			final SchuelerSchulbesuchService schuelerSchulbesuchService,
			final DataSchuelerEinwilligungen dataSchuelerEinwilligungen,
			final DataSchuelerLernplattformen dataSchuelerLernplattformen,
			final DataLernplattformen dataLernplattformen,
			final DataEinwilligungsarten dataEinwilligungsarten) {
		this.schuelerStammdatenService = schuelerStammdatenService;
		this.dataSchuelerLernabschnittsdaten = dataSchuelerLernabschnittsdaten;
		this.schuelerSchulbesuchService = schuelerSchulbesuchService;
		this.dataSchuelerEinwilligungen = dataSchuelerEinwilligungen;
		this.dataSchuelerLernplattformen = dataSchuelerLernplattformen;
		this.dataLernplattformen = dataLernplattformen;
		this.dataEinwilligungsarten = dataEinwilligungsarten;
	}

	/**
	 *	Erzeugt einen neuen Schüler und den entsprechenden Lernabschnitt, die Einwilligungen und die Lernplattformen für diesen Schüler
	 *
	 *    @param is							Inputstream
	 *
	 *    @return							Response mit dem erstellen Schüler
	 */
	public Response add(final InputStream is) {
		final Map<String, Object> initAttributes = JSONMapper.toMap(is);
		final SchuelerStammdaten createdSchueler = addSchueler(initAttributes);
		if (createdSchueler == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Keine Daten zum Schüler vorhanden.");
		}
		final long idSchueler = createdSchueler.id;
		addLernabschnitt(initAttributes, idSchueler);
		patchSchulbesuch(initAttributes, idSchueler);
		addEinwilligungen(idSchueler);
		addLernplattformen(idSchueler);

		return Response.status(Response.Status.CREATED).entity(createdSchueler).build();
	}

	private SchuelerStammdaten addSchueler(final Map<String, Object> initAttributes) {
		final var neuerSchueler = new SchuelerImportData(
				JSONMapper.convertToString(initAttributes.get("nachname"), false, false, Schema.tab_Schueler.col_Name.datenlaenge(), "nachname"),
				JSONMapper.convertToString(initAttributes.get("vorname"), false, false, Schema.tab_Schueler.col_Vorname.datenlaenge(), "vorname"),
				JSONMapper.convertToString(initAttributes.get("alleVornamen"), true, true, Schema.tab_Schueler.col_Zusatz.datenlaenge(), "alleVornamen"),
				JSONMapper.convertToInteger(initAttributes.get("geschlecht"), false, "geschlecht"),
				JSONMapper.convertToString(initAttributes.get("geburtsdatum"), false, false, null, "geburtsdatum"),
				JSONMapper.convertToInteger(initAttributes.get("status"), false, "status"),
				JSONMapper.convertToString(initAttributes.get("anmeldedatum"), true, false, null, "anmeldedatum"),
				JSONMapper.convertToString(initAttributes.get("aufnahmedatum"), true, false, null, "aufnahmedatum"),
				JSONMapper.convertToString(initAttributes.get("beginnBildungsgang"), true, false, Schema.tab_Schueler.col_BeginnBildungsgang.datenlaenge(), "beginnBildungsgang"),
				JSONMapper.convertToInteger(initAttributes.get("dauerBildungsgang"), true, "dauerBildungsgang"),
				JSONMapper.convertToLongInRange(initAttributes.get("idReligion"), true, 0L, null, "idReligion"),
				JSONMapper.convertToLong(initAttributes.get(ID_SCHULJAHRESABSCHNITT), false, ID_SCHULJAHRESABSCHNITT)
		);
		return schuelerStammdatenService.create(neuerSchueler);
	}

	private void addLernabschnitt(final Map<String, Object> initAttributes, final long idSchueler) {
		final Map<String, Object> lernabschnittAttributes = new HashMap<>();
		putIfPresent(lernabschnittAttributes, "schuljahresabschnitt", initAttributes.get(ID_SCHULJAHRESABSCHNITT));
		putIfPresent(lernabschnittAttributes, "jahrgangID", initAttributes.get("idJahrgang"));
		putIfPresent(lernabschnittAttributes, "klassenID", initAttributes.get("idKlasse"));
		lernabschnittAttributes.put("schuelerID", idSchueler);
		this.dataSchuelerLernabschnittsdaten.add(lernabschnittAttributes);
	}

	private void patchSchulbesuch(final Map<String, Object> initAttributes, final long idSchueler) {
		final var idGrundschuleEinschulungsart = JSONMapper.convertToLong(initAttributes.get("idGrundschuleEinschulungsart"), true);
		if (idGrundschuleEinschulungsart == null) {
			return;
		}
		final var dto = new SchuelerSchulbesuchPatchRequest();
		dto.idEinschulungsartGrundschule = JsonNullable.of(idGrundschuleEinschulungsart);
		this.schuelerSchulbesuchService.patch(idSchueler, dto);
	}

	private void addLernplattformen(final long idSchueler) {
		this.dataLernplattformen
				.getAllIds()
				.forEach(id -> {
					final Map<String, Object> lernplattformAttributes = Map.of("idSchueler", idSchueler, "idLernplattform", id);
					this.dataSchuelerLernplattformen.add(lernplattformAttributes);
				});
	}

	private void addEinwilligungen(final long idSchueler) {
		this.dataEinwilligungsarten
				.getAllIdsByPersonTyp(PersonTyp.SCHUELER)
				.forEach(id -> {
					final Map<String, Object> einwilligungAttributes = Map.of("idSchueler", idSchueler, "idEinwilligungsart", id);
					this.dataSchuelerEinwilligungen.add(einwilligungAttributes);
				});
	}

	/**
	 * Adds the key-value pair to the target map if the value is not {@code null}.
	 *
	 * @param target the target map
	 * @param key the key to insert
	 * @param value the value to insert; ignored if {@code null}
	 */
	private void putIfPresent(final Map<String, Object> target, final String key, final Object value) {
		if (value != null) {
			target.put(key, value);
		}
	}

}
