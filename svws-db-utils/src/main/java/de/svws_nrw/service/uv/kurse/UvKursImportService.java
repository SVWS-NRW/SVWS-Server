package de.svws_nrw.service.uv.kurse;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.core.data.uv.UvKurs;
import de.svws_nrw.core.data.uv.UvKursCreateRequest;
import de.svws_nrw.core.data.uv.UvKursImportDaten;
import de.svws_nrw.core.data.uv.UvLerngruppe;
import de.svws_nrw.core.data.uv.UvLerngruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenLehrerCreateRequest;
import de.svws_nrw.core.data.uv.UvLerngruppenSchieneCreateRequest;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittLehrerCreateRequest;
import de.svws_nrw.core.data.uv.UvPlanungsabschnittsdatenBundle;
import de.svws_nrw.core.data.uv.UvSchiene;
import de.svws_nrw.core.data.uv.UvSchieneCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppe;
import de.svws_nrw.core.data.uv.UvSchuelergruppeCreateRequest;
import de.svws_nrw.core.data.uv.UvSchuelergruppeSchuelerCreateRequest;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppeService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenLehrerService;
import de.svws_nrw.service.uv.lerngruppen.UvLerngruppenSchieneService;
import de.svws_nrw.service.uv.lehrer.UvPlanungsabschnittLehrerService;
import de.svws_nrw.service.uv.planungsabschnitte.UvPlanungsabschnittService;
import de.svws_nrw.service.uv.schienen.UvSchieneService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeSchuelerService;
import de.svws_nrw.service.uv.schueler.UvSchuelergruppeService;
import jakarta.ws.rs.core.Response.Status;

/** Persistiert die vom Client bestätigten Kursimportdaten für einen UV-Planungsabschnitt. */
public final class UvKursImportService {

	private final UvPlanungsabschnittService uvPlanungsabschnittService;
	private final UvPlanungsabschnittLehrerService uvPlanungsabschnittLehrerService;
	private final UvSchieneService uvSchieneService;
	private final UvSchuelergruppeService uvSchuelergruppeService;
	private final UvKursService uvKursService;
	private final UvLerngruppeService uvLerngruppeService;
	private final UvLerngruppenLehrerService uvLerngruppenLehrerService;
	private final UvLerngruppenSchieneService uvLerngruppenSchieneService;
	private final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService;

	/**
	 * Erstellt einen Import-Service.
	 *
	 * @param uvPlanungsabschnittService der Service für Planungsabschnitte
	 * @param uvPlanungsabschnittLehrerService der Service für Planungsabschnitt-Lehrer-Zuordnungen
	 * @param uvSchieneService der Service für Schienen
	 * @param uvSchuelergruppeService der Service für Schülergruppen
	 * @param uvKursService der Service für Kurse
	 * @param uvLerngruppeService der Service für Lerngruppen
	 * @param uvLerngruppenLehrerService der Service für Lehrer-Lerngruppen-Zuordnungen
	 * @param uvLerngruppenSchieneService der Service für Schienen-Lerngruppen-Zuordnungen
	 * @param uvSchuelergruppeSchuelerService der Service für Schüler-Schülergruppen-Zuordnungen
	 */
	public UvKursImportService(final UvPlanungsabschnittService uvPlanungsabschnittService,
			final UvPlanungsabschnittLehrerService uvPlanungsabschnittLehrerService, final UvSchieneService uvSchieneService,
			final UvSchuelergruppeService uvSchuelergruppeService, final UvKursService uvKursService,
			final UvLerngruppeService uvLerngruppeService, final UvLerngruppenLehrerService uvLerngruppenLehrerService,
			final UvLerngruppenSchieneService uvLerngruppenSchieneService,
			final UvSchuelergruppeSchuelerService uvSchuelergruppeSchuelerService) {
		this.uvPlanungsabschnittService = uvPlanungsabschnittService;
		this.uvPlanungsabschnittLehrerService = uvPlanungsabschnittLehrerService;
		this.uvSchieneService = uvSchieneService;
		this.uvSchuelergruppeService = uvSchuelergruppeService;
		this.uvKursService = uvKursService;
		this.uvLerngruppeService = uvLerngruppeService;
		this.uvLerngruppenLehrerService = uvLerngruppenLehrerService;
		this.uvLerngruppenSchieneService = uvLerngruppenSchieneService;
		this.uvSchuelergruppeSchuelerService = uvSchuelergruppeSchuelerService;
	}

	/**
	 * Persistiert die Importdaten atomar und löst ihre negativen temporären IDs auf.
	 *
	 * @param idPlanungsabschnitt die ID des Zielplanungsabschnitts
	 * @param daten die anzulegenden Importdaten
	 * @return die tatsächlich angelegten UV-Daten
	 */
	public UvPlanungsabschnittsdatenBundle importDaten(final long idPlanungsabschnitt, final UvKursImportDaten daten) {
		return transactional(() -> doImportDaten(idPlanungsabschnitt, daten));
	}

	private UvPlanungsabschnittsdatenBundle doImportDaten(final long idPlanungsabschnitt, final UvKursImportDaten daten) {
		uvPlanungsabschnittService.get(idPlanungsabschnitt);
		pruefeZielplanungsabschnitt(idPlanungsabschnitt, daten);
		final UvPlanungsabschnittsdatenBundle result = new UvPlanungsabschnittsdatenBundle();
		final Map<Long, Long> ids = new HashMap<>();
		createPlanungsabschnittLehrer(daten, result);
		createSchienen(daten, result, ids);
		createSchuelergruppen(daten, result, ids);
		createKurse(daten, result, ids);
		createLerngruppen(daten, result, ids);
		createLerngruppenLehrer(daten, result, ids);
		createLerngruppenSchienen(daten, result, ids);
		createSchuelergruppenSchueler(daten, result, ids);
		return result;
	}

	private void createPlanungsabschnittLehrer(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result) {
		for (final UvPlanungsabschnittLehrerCreateRequest request : daten.planungsabschnittlehrer) {
			result.planungsabschnittlehrer.add(uvPlanungsabschnittLehrerService.create(request));
		}
	}

	private void createSchienen(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result, final Map<Long, Long> ids) {
		for (final UvSchieneCreateRequest request : daten.schienen) {
			final UvSchiene erstellt = uvSchieneService.create(request);
			ids.put(request.id, erstellt.id);
			result.schienen.add(erstellt);
		}
	}

	private void createSchuelergruppen(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result,
			final Map<Long, Long> ids) {
		for (final UvSchuelergruppeCreateRequest request : daten.schuelergruppen) {
			final UvSchuelergruppe erstellt = uvSchuelergruppeService.create(request);
			ids.put(request.id, erstellt.id);
			result.schuelergruppen.add(erstellt);
		}
	}

	private void createKurse(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result, final Map<Long, Long> ids) {
		for (final UvKursCreateRequest request : daten.kurse) {
			request.idSchuelergruppe = resolveId(ids, request.idSchuelergruppe);
			final UvKurs erstellt = uvKursService.create(request);
			ids.put(request.id, erstellt.id);
			result.kurse.add(erstellt);
		}
	}

	private void createLerngruppen(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result,
			final Map<Long, Long> ids) {
		for (final UvLerngruppeCreateRequest request : daten.lerngruppen) {
			request.idKurs = resolveId(ids, request.idKurs);
			final UvLerngruppe erstellt = uvLerngruppeService.create(request);
			ids.put(request.id, erstellt.id);
			result.lerngruppen.add(erstellt);
		}
	}

	private void createLerngruppenLehrer(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result,
			final Map<Long, Long> ids) {
		for (final UvLerngruppenLehrerCreateRequest request : daten.lerngruppenlehrer) {
			request.idLerngruppe = resolveId(ids, request.idLerngruppe);
			result.lerngruppenlehrer.add(uvLerngruppenLehrerService.create(request));
		}
	}

	private void createLerngruppenSchienen(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result,
			final Map<Long, Long> ids) {
		for (final UvLerngruppenSchieneCreateRequest request : daten.lerngruppenschienen) {
			request.idLerngruppe = resolveId(ids, request.idLerngruppe);
			request.idSchiene = resolveId(ids, request.idSchiene);
			result.lerngruppenschienen.add(uvLerngruppenSchieneService.create(request));
		}
	}

	private void createSchuelergruppenSchueler(final UvKursImportDaten daten, final UvPlanungsabschnittsdatenBundle result,
			final Map<Long, Long> ids) {
		for (final UvSchuelergruppeSchuelerCreateRequest request : daten.schuelergruppenschueler) {
			request.idSchuelergruppe = resolveId(ids, request.idSchuelergruppe);
			result.schuelergruppenschueler.add(uvSchuelergruppeSchuelerService.create(request));
		}
	}

	private void pruefeZielplanungsabschnitt(final long idPlanungsabschnitt, final UvKursImportDaten daten) {
		for (final UvKursCreateRequest kurs : daten.kurse) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, kurs.idPlanungsabschnitt);
		}
		for (final UvSchuelergruppeCreateRequest gruppe : daten.schuelergruppen) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, gruppe.idPlanungsabschnitt);
		}
		for (final UvSchieneCreateRequest schiene : daten.schienen) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, schiene.idPlanungsabschnitt);
		}
		for (final UvLerngruppeCreateRequest lerngruppe : daten.lerngruppen) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, lerngruppe.idPlanungsabschnitt);
		}
		for (final UvPlanungsabschnittLehrerCreateRequest lehrer : daten.planungsabschnittlehrer) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, lehrer.idPlanungsabschnitt);
		}
		for (final UvLerngruppenLehrerCreateRequest lehrer : daten.lerngruppenlehrer) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, lehrer.idPlanungsabschnitt);
		}
		for (final UvLerngruppenSchieneCreateRequest schiene : daten.lerngruppenschienen) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, schiene.idPlanungsabschnitt);
		}
		for (final UvSchuelergruppeSchuelerCreateRequest schueler : daten.schuelergruppenschueler) {
			pruefePlanungsabschnitt(idPlanungsabschnitt, schueler.idPlanungsabschnitt);
		}
	}

	private static void pruefePlanungsabschnitt(final long idPlanungsabschnitt, final Long idRequestPlanungsabschnitt) {
		if ((idRequestPlanungsabschnitt == null) || (idRequestPlanungsabschnitt != idPlanungsabschnitt)) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Importdaten gehören nicht zum Zielplanungsabschnitt.");
		}
	}

	private long resolveId(final Map<Long, Long> ids, final Long id) {
		if (id == null) {
			return -1;
		}
		if (id >= 0) {
			return id;
		}
		final Long resolved = ids.get(id);
		if (resolved == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die temporäre Import-ID %d kann nicht aufgelöst werden.".formatted(id));
		}
		return resolved;
	}
}
