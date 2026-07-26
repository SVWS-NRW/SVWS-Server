package de.svws_nrw.controller.gost.klausuren;

import java.util.List;

import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.Responses;
import de.svws_nrw.service.gost.klausuren.GostKlausurenAllDataService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenKlausurdatenIssuesService;
import de.svws_nrw.service.gost.klausuren.GostKlausurenSchuelerKlausurdatenService;
import jakarta.ws.rs.core.Response;

/**
 * In dieser Klasse werden API-Zugriffe für aggregierte GOSt-Klausurdaten gebündelt.
 */
public final class GostKlausurenKlausurdatenControllerImpl implements GostKlausurenKlausurdatenController {

	private final GostKlausurenAllDataService allDataService;
	private final GostKlausurenKlausurdatenIssuesService klausurdatenIssuesService;
	private final GostKlausurenSchuelerKlausurdatenService schuelerKlausurdatenService;

	/**
	 * Erstellt eine neue Controller-Instanz.
	 *
	 * @param allDataService der Service für vollständige Daten
	 * @param klausurdatenIssuesService der Service für Klausurdaten-Issues
	 * @param schuelerKlausurdatenService der Service für Schüler-Klausurdaten
	 */
	public GostKlausurenKlausurdatenControllerImpl(final GostKlausurenAllDataService allDataService,
			final GostKlausurenKlausurdatenIssuesService klausurdatenIssuesService,
			final GostKlausurenSchuelerKlausurdatenService schuelerKlausurdatenService) {
		this.allDataService = allDataService;
		this.klausurdatenIssuesService = klausurdatenIssuesService;
		this.schuelerKlausurdatenService = schuelerKlausurdatenService;
	}

	@Override
	public Response getAllData(final List<GostKlausurenHalbjahresdaten> hjData) {
		return Responses.ok(allDataService.getAllData(hjData));
	}

	@Override
	public Response getAllDataGZip(final List<GostKlausurenHalbjahresdaten> hjData) {
		return JSONMapper.gzipFileResponseFromObject(allDataService.getAllData(hjData), "klausurdaten.json.gz");
	}

	@Override
	public Response getKlausurdatenIssues(final int abiturjahr, final GostHalbjahr halbjahr) {
		return Responses.ok(klausurdatenIssuesService.getKlausurdatenIssues(abiturjahr, halbjahr));
	}

	@Override
	public Response getKlausurdatenIssuesGZip(final int abiturjahr, final GostHalbjahr halbjahr) {
		return JSONMapper.gzipFileResponseFromObject(klausurdatenIssuesService.getKlausurdatenIssues(abiturjahr, halbjahr),
				"klausurdaten_issues_%d-%d.json.gz".formatted(abiturjahr, halbjahr.id));
	}

	@Override
	public Response getKlausurdatenBySchuelerId(final long idSchueler, final int abiturjahr, final int halbjahr) {
		return Responses.ok(schuelerKlausurdatenService.getKlausurdatenBySchuelerId(idSchueler, abiturjahr, halbjahr));
	}

}
