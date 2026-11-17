package de.svws_nrw.controller.statistik;

import de.svws_nrw.asd.data.statistik.StatistikGesamt;
import de.svws_nrw.asd.data.statistik.ZusatzerhebungenStatistikGesamt;
import de.svws_nrw.asd.export.data.StatistikExport;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.statistik.StatistikService;
import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikPatchRequest;
import de.svws_nrw.service.statistik.ZusatzerhebungenStatistikService;
import de.svws_nrw.validation.BeanValidator;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden die Methoden zur Behandlung der API-Zugriffe im Bereich der amtlichen Schulstatistik gebündelt
 */
public final class StatistikControllerImpl implements StatistikController {

	/** Der zugehörige Service */
	private final StatistikService service;

	/** Der Service für die Daten der Zusatzerhebungen */
	private final ZusatzerhebungenStatistikService serviceZusatzerhebungen;

	/**
	 * Erstellt für  die Datenbank-Verbindung eine neue Controller-Instanz.
	 *
	 * @param service                   der zugehörige Service
	 * @param serviceZusatzerhebungen   der Service für die Daten der Zusatzerhebungen
	 */
	public StatistikControllerImpl(final StatistikService service, final ZusatzerhebungenStatistikService serviceZusatzerhebungen) {
		this.service = service;
		this.serviceZusatzerhebungen = serviceZusatzerhebungen;
	}

	@Override
	public Response getStatistikGesamt() {
		final StatistikGesamt daten = service.get();
		return Responses.ok(daten);
	}

	@Override
	public Response getStatistikExport() {
		final StatistikExport daten = service.getExport();
		return Responses.ok(daten);
	}

	@Override
	public Response patchZusatzerhebungen(final long idSchuljahresabschnitt, final ZusatzerhebungenStatistikPatchRequest patch) {
		BeanValidator.validate(patch);
		final ZusatzerhebungenStatistikGesamt daten = serviceZusatzerhebungen.patch(idSchuljahresabschnitt, patch);
		return Responses.ok(daten);
	}

}
