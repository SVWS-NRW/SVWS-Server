package de.svws_nrw.service.schueler.lernabschnitt;

import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fachklasse.FachklasseRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schuelerschwerpunkt.SchuelerschwerpunktRepository;

/**
 * Bündelt die Repositories, die der {@link SchuelerLernabschnittService} benötigt.
 *
 * @param repository                     das Repository für die Lernabschnittsdaten
 * @param schuelerRepository             das Repository für die Schüler
 * @param bemerkungRepository            das Repository für die Bemerkungen zu den Lernabschnitten
 * @param schuljahresabschnitteRepo      das Repository für die Schuljahresabschnitte
 * @param leistungsdatenRepository       das Repository für die Leistungsdaten zu den Lernabschnitten
 * @param lehrerRepository               das Repository für die Lehrer (Tutor, Sonderpädagoge)
 * @param klassenRepository              das Repository für die Klassen (Folgeklasse)
 * @param foerderschwerpunktRepository   das Repository für die Förderschwerpunkte
 * @param fachklasseRepository           das Repository für die Fachklassen (schulinterner Katalog, BK)
 * @param jahrgangRepository             das Repository für die Jahrgänge
 * @param schuelerschwerpunktRepository  das Repository für Schülerschwerpunkte
 */
public record SchuelerLernabschnittsdatenRepos(
		SchuelerLernabschnittRepository repository,
		SchuelerRepository schuelerRepository,
		SchuelerLernabschnittBemerkungRepository bemerkungRepository,
		SchuljahresabschnitteRepository schuljahresabschnitteRepo,
		SchuelerLeistungsdatenRepository leistungsdatenRepository,
		LehrerRepository lehrerRepository,
		KlassenRepository klassenRepository,
		FoerderschwerpunktRepository foerderschwerpunktRepository,
		FachklasseRepository fachklasseRepository,
		JahrgangRepository jahrgangRepository,
		SchuelerschwerpunktRepository schuelerschwerpunktRepository
) { }
