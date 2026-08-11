package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.lehrer.personalabschnittsdaten.LehrerPersonalabschnittsdatenRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.schule.SchuleRepository;

public record LehrerPersonalabschnittsdatenRepos(
		LehrerPersonalabschnittsdatenRepository lehrerPersonalabschnittsdatenRepo,
		LehrerRepository lehrerRepo,
		SchuleRepository schulenRepo,
		SchuljahresabschnitteRepository schuljahresabschnitteRepo
) { }
