package de.svws_nrw.service.statistik;

import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturFachRepository;
import de.svws_nrw.repo.schueler.abitur.SchuelerAbiturRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;

public record SchuelerStatistikRepositories(
		BenutzerAllgemeinRepository benutzerRepository,
		SchuelerRepository schuelerRepository,
		SchuelerLernabschnittRepository schuelerLernabschnittRepository,
		SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
		SchuelerAbiturRepository schuelerAbiturRepository,
		SchuelerAbiturFachRepository schuelerAbiturFachRepository,
		FachRepository fachRepository,
		SchuleRepository schuleRepository
) { }
