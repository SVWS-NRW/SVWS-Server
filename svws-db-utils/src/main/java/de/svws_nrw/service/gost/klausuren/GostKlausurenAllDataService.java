package de.svws_nrw.service.gost.klausuren;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenAlleKlausurdaten;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurenHalbjahresdaten;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.gost.klausuren.GostKlausurenAllDataRepository;

/**
 * Service für die vollständigen Klausurplanungsdaten.
 *
 * TODO Dieser Service aggregiert korrekt mehrere Datenbereiche, nutzt für externe Lesedaten aber noch
 * {@link GostKlausurenAllDataRepository}. Diese Repository-Abhängigkeit kann entfernt werden, sobald für Schüler,
 * GOSt-Fächer, Schuljahresabschnitte, Kurse und Lehrer passende Basis-Repositories bzw. DTO-nahe Services existieren
 * und die AllData-Aggregation diese statt der alten Data*-Kapsel verwenden kann.
 */
public final class GostKlausurenAllDataService {

	private final GostKlausurenAllDataRepository repository;
	private final GostKlausurenKlausurdatenService collectionService;
	private final GostKlausurenRaumzuweisungService raumzuweisungService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param repository das Repository für externe Lesedaten
	 * @param collectionService der Service für Klausurdaten
	 * @param raumzuweisungService der Service für Raumzuweisungen
	 */
	public GostKlausurenAllDataService(final GostKlausurenAllDataRepository repository, final GostKlausurenKlausurdatenService collectionService,
			final GostKlausurenRaumzuweisungService raumzuweisungService) {
		this.repository = repository;
		this.collectionService = collectionService;
		this.raumzuweisungService = raumzuweisungService;
	}

	/**
	 * Liefert alle Klausurplanungsdaten.
	 *
	 * @param hjData die Halbjahresdaten
	 *
	 * @return die vollständigen Daten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public GostKlausurenAlleKlausurdaten getAllData(final List<GostKlausurenHalbjahresdaten> hjData) throws ApiOperationException {
		final GostKlausurenAlleKlausurdaten data = new GostKlausurenAlleKlausurdaten();
		final List<SchuelerListeEintrag> schuelerNichtSenden = new ArrayList<>();

		for (final GostKlausurenHalbjahresdaten jg : hjData) {
			jg.klausurdaten = collectionService.getKlausurdaten(jg.abiturjahrgang, jg.gostHalbjahr, false);

			final List<SchuelerListeEintrag> schuelerJahrgang = repository.getSchuelerByAbiturjahr(jg.abiturjahrgang);
			schuelerNichtSenden.addAll(schuelerJahrgang);
			if (jg.schueler != null) {
				jg.schueler = schuelerJahrgang;
			}
			if (jg.faecher != null) {
				jg.faecher = repository.getFaecherByAbiturjahr(jg.abiturjahrgang);
			}

			final GostHalbjahr gj = GostHalbjahr.fromID(jg.gostHalbjahr);
			final Schuljahresabschnitt sja =
					repository.getSchuljahresabschnitt(gj.getSchuljahrFromAbiturjahr(jg.abiturjahrgang), gj.halbjahr);
			if (sja != null) {
				jg.idSchuljahresabschnitt = sja.id;
				jg.kurse.addAll(repository.getKurseBySchuljahresabschnitt(sja.id));
			}
			jg.raumdaten = raumzuweisungService.getRaumDataByTerminIds(jg.klausurdaten.termine.stream().map(t -> t.id).toList());
			data.halbjahresdaten.add(jg);
		}

		final List<Long> missingSchuelerIds = hjData.stream().flatMap(jg -> jg.klausurdaten.schuelerklausuren.stream()).map(sk -> sk.idSchueler)
				.filter(item -> !schuelerNichtSenden.stream().map(s -> s.id).distinct().toList().contains(item)).toList();
		if (!missingSchuelerIds.isEmpty()) {
			hjData.getFirst().schueler = (hjData.getFirst().schueler == null) ? new ArrayList<>() : new ArrayList<>(hjData.getFirst().schueler);
			hjData.getFirst().schueler.addAll(repository.getSchuelerByIds(-1, missingSchuelerIds));
		}
		data.lehrer.addAll(repository.getLehrer());
		return data;
	}

}
