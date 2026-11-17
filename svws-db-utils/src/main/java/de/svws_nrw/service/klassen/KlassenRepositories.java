package de.svws_nrw.service.klassen;

import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.teilstandort.TeilstandortRepository;

/**
 * Bündelt die Repositories, die der {@link KlassenService} benötigt.
 *
 * @param klassenRepository        das Repository für den Zugriff auf die Klassen
 * @param jahrgangRepository       das Repository für den Zugriff auf die Jahrgänge
 * @param teilstandortRepository   das Repository für den Zugriff auf die Teilstandorte
 * @param lernabschnittRepository  das Repository für den Zugriff auf die Lernabschnitte der Schüler, wird beim Löschen für die Anzahl der Schüler benötigt
 */
public record KlassenRepositories(
		KlassenRepository klassenRepository,
		JahrgangRepository jahrgangRepository,
		TeilstandortRepository teilstandortRepository,
		SchuelerLernabschnittRepository lernabschnittRepository
) { }
