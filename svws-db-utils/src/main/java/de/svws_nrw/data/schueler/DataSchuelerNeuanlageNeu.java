package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.data.schueler.neuanlage.SchuelerNeuanlage;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/** DataManager zum Erstellen eines Schülers. */
public final class DataSchuelerNeuanlageNeu {

	private final DataSchuelerStammdaten dataSchuelerStammdaten;
	private final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;
	private final DataSchuelerSchulbesuchsdaten dataSchuelerSchulbesuchsdaten;
	private final DataSchuelerEinwilligungen dataSchuelerEinwilligungen;
	private final DataSchuelerLernplattformen dataSchuelerLernplattformen;
	private final DataLernplattformen dataLernplattformen;
	private final DataEinwilligungsarten dataEinwilligungsarten;

	/**
	 * Erstellt einen neuen {@link DataSchuelerNeuanlageNeu} für das Core-DTO {@link SchuelerNeuanlage}
	 *
	 * @param dataSchuelerStammdaten			DataSchuelerStammdaten
	 * @param dataSchuelerLernabschnittsdaten	DataSchuelerLernabschnittsdaten
	 * @param dataSchuelerSchulbesuchsdaten		DataSchuelerSchulbesuchsdaten
	 * @param dataSchuelerEinwilligungen		DataSchuelerEinwilligungen
	 * @param dataSchuelerLernplattformen		DataSchuelerLernplattformen
	 * @param dataLernplattformen				DataLernplattformen
	 * @param dataEinwilligungsarten			DataEinwilligungsarten
	 */
	public DataSchuelerNeuanlageNeu(
			final DataSchuelerStammdaten dataSchuelerStammdaten,
			final DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten,
			final DataSchuelerSchulbesuchsdaten dataSchuelerSchulbesuchsdaten,
			final DataSchuelerEinwilligungen dataSchuelerEinwilligungen,
			final DataSchuelerLernplattformen dataSchuelerLernplattformen,
			final DataLernplattformen dataLernplattformen,
			final DataEinwilligungsarten dataEinwilligungsarten) {
		this.dataSchuelerStammdaten = dataSchuelerStammdaten;
		this.dataSchuelerLernabschnittsdaten = dataSchuelerLernabschnittsdaten;
		this.dataSchuelerSchulbesuchsdaten = dataSchuelerSchulbesuchsdaten;
		this.dataSchuelerEinwilligungen = dataSchuelerEinwilligungen;
		this.dataSchuelerLernplattformen = dataSchuelerLernplattformen;
		this.dataLernplattformen = dataLernplattformen;
		this.dataEinwilligungsarten = dataEinwilligungsarten;
	}

	/**
	 *	Erzeugt einen neuen Schüler und den entsprechenden Lernabschnitt, die Einwilligungen und die Lernplattformen für diesen Schüler
	 *
	 *    @param is							Inputstream
	 *    @param idSchuljahresabschnitt		idSchuljahresabschnitt
	 *
	 *    @return							Response mit dem erstellen Schüler
	 */
	public Response add(final InputStream is, final Long idSchuljahresabschnitt) {
		final Map<String, Object> initAttributes = JSONMapper.toMap(is);
		final SchuelerStammdaten createdSchueler = addSchueler(initAttributes, idSchuljahresabschnitt);
		final long idSchueler = createdSchueler.id;
		addLernabschnitt(initAttributes, idSchueler);
		patchSchulbesuch(initAttributes, idSchueler);
		addEinwilligungen(idSchueler);
		addLernplattformen(idSchueler);

		return Response.status(Response.Status.CREATED).entity(createdSchueler).build();
	}

	private SchuelerStammdaten addSchueler(final Map<String, Object> initAttributes, final Long idSchuljahresabschnitt) {
		final Map<String, Object> schuelerAttributes = extractMap(initAttributes, "schuelerStammdaten");
		schuelerAttributes.put("idSchuljahresabschnitt", idSchuljahresabschnitt);
		return this.dataSchuelerStammdaten.add(schuelerAttributes);
	}

	private void addLernabschnitt(final Map<String, Object> initAttributes, final long idSchueler) {
		final Map<String, Object> lernabschnittAttributes = extractMap(initAttributes, "schuelerLernabschnittsdaten");
		lernabschnittAttributes.put("schuelerID", idSchueler);
		this.dataSchuelerLernabschnittsdaten.add(lernabschnittAttributes);
	}

	private void patchSchulbesuch(final Map<String, Object> initAttributes, final long idSchueler) {
		final Map<String, Object> lernabschnittAttributes = extractMap(initAttributes, "schuelerSchulbesuchsdaten");
		this.dataSchuelerSchulbesuchsdaten.patch(idSchueler, lernabschnittAttributes);
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

	private Map<String, Object> extractMap(final Map<String, Object> source, final String key) {
		final Object value = source.get(key);
		if (value == null) {
			return new HashMap<>();
		}
		if (!(value instanceof Map)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Attribut '%s' ist keine Map.".formatted(key));
		}

		return new ObjectMapper().convertValue(value, new TypeReference<>() { });
	}

}
