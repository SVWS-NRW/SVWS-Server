package de.svws_nrw.service.kurse;

import de.svws_nrw.repo.kurse.KursSchuelerRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.kurse.KurslehrerRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;

/**
 * Bündelt die Repositories, die der {@link KursService} benötigt.
 *
 * @param kurseRepository                    das Repository für den Zugriff auf die Kurse
 * @param kursSchuelerRepository             das Repository für den Zugriff auf die Kurs-Schüler-Zuordnung
 * @param kurslehrerRepository               das Repository für den Zugriff auf die Zusatzkräfte der Kurse
 * @param schuelerRepository                 das Repository für den Zugriff auf die Schüler
 * @param jahrgangRepository                 das Repository für den Zugriff auf die Jahrgänge
 * @param fachRepository                     das Repository für den Zugriff auf die Fächer
 * @param lehrerRepository                   das Repository für den Zugriff auf die Lehrer
 * @param schuleRepository                   das Repository für den Zugriff auf den Schulkatalog
 * @param schuljahresabschnitteRepository    das Repository für den Zugriff auf die Schuljahresabschnitte
 * @param schuelerLeistungsdatenRepository   das Repository für den Zugriff auf die Leistungsdaten der Schüler
 */
public record KursRepositories(
		KurseRepository kurseRepository,
		KursSchuelerRepository kursSchuelerRepository,
		KurslehrerRepository kurslehrerRepository,
		SchuelerRepository schuelerRepository,
		JahrgangRepository jahrgangRepository,
		FachRepository fachRepository,
		LehrerRepository lehrerRepository,
		SchuleRepository schuleRepository,
		SchuljahresabschnitteRepository schuljahresabschnitteRepository,
		SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository
) { }
