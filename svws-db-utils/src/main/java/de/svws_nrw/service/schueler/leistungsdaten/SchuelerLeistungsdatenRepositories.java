package de.svws_nrw.service.schueler.leistungsdaten;

import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;

/**
 * Bündelt die Repositories, die der {@link SchuelerLeistungsdatenService} benötigt.
 *
 * @param repository                  das Repository für die Leistungsdaten
 * @param lernabschnittRepository     das Repository für die Lernabschnitte
 * @param schuljahresabschnitteRepository   das Repository für die Schuljahresabschnitte
 * @param fachRepository              das Repository für die Fächer
 * @param kurseRepository             das Repository für die Kurse
 * @param lehrerRepository            das Repository für die Lehrer (Fachlehrer, Zusatzkraft)
 */
public record SchuelerLeistungsdatenRepositories(
		SchuelerLeistungsdatenRepository repository,
		SchuelerLernabschnittRepository lernabschnittRepository,
		SchuljahresabschnitteRepository schuljahresabschnitteRepository,
		FachRepository fachRepository,
		KurseRepository kurseRepository,
		LehrerRepository lehrerRepository
) { }
