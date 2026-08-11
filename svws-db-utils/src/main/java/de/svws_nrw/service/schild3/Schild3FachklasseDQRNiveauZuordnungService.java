package de.svws_nrw.service.schild3;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schule.FachklasseKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.schule.Fachklasse;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.core.data.schild3.Schild3FachklasseDQRNiveauZuordnung;
import de.svws_nrw.mapper.Schild3FachklasseDQRNiveauZuordnungMapper;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.service.schule.SchuljahresabschnittService;

public final class Schild3FachklasseDQRNiveauZuordnungService {

	private final Schild3FachklasseDQRNiveauZuordnungMapper mapper;

	private final EigeneSchuleRepository eigeneSchuleRepository;

	private final SchuljahresabschnittService schuljahresabschnittService;

	/**
	 * Initialisierung eines neuen Services
	 *
	 * @param mapper {@link Schild3FachklasseDQRNiveauZuordnungMapper}
	 * @param eigeneSchuleRepository {@link EigeneSchuleRepository}
	 * @param schuljahresabschnittService {@link SchuljahresabschnittService}
	 */
	public Schild3FachklasseDQRNiveauZuordnungService(final Schild3FachklasseDQRNiveauZuordnungMapper mapper, final EigeneSchuleRepository eigeneSchuleRepository,
			final SchuljahresabschnittService schuljahresabschnittService) {
		this.mapper = mapper;
		this.eigeneSchuleRepository = eigeneSchuleRepository;
		this.schuljahresabschnittService = schuljahresabschnittService;
	}

	/**
	 * Liefert Liste mit allen {@link Schild3FachklasseDQRNiveauZuordnung} Objekten
	 *
	 * @return Liste von DTO's
	 */
	public List<Schild3FachklasseDQRNiveauZuordnung> getAll() {
		final var aktuellerSchuljahresabschnitt = schuljahresabschnittService.getById(eigeneSchuleRepository.getIdSchuljahresabschnitt());
		final var fachklassenWerte = Fachklasse.data().getWerte();
		return fachklassenWerte.stream()
				.map(e -> e.daten(aktuellerSchuljahresabschnitt.schuljahr))
				.filter(Objects::nonNull)
				.flatMap(e -> mapFachklasseToSchild3DQRNiveauZuordnungenAsStream(e, aktuellerSchuljahresabschnitt))
				.filter(e -> Objects.nonNull(e.Gliederung))
				.toList();
	}

	/**
	 * Wenn ein BKIndex auf mehr als eine Schulgliederung verweist, wird zu jeder Schulgliederung eine Zuordnung von Fachklasse zu DQRNiveau erzeugt.
	 *
	 * @param fachklasse Fachklasse
	 * @param aktuellerSchuljahresabschnitt aktueller Schuljahresabschnitt
	 * @return Liste von {@link Schild3FachklasseDQRNiveauZuordnung}
	 */
	private Stream<Schild3FachklasseDQRNiveauZuordnung> mapFachklasseToSchild3DQRNiveauZuordnungenAsStream(final FachklasseKatalogEintrag fachklasse,
			final Schuljahresabschnitt aktuellerSchuljahresabschnitt) {
		final var schulgliederungenByBkIndex = Schulgliederung.getBySchuljahrAndBKIndex(aktuellerSchuljahresabschnitt.schuljahr, fachklasse.bkIndex);
		return schulgliederungenByBkIndex.stream().map(sg -> mapper.toApi(fachklasse, sg.kuerzel));
	}

}
