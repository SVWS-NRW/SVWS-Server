package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import de.svws_nrw.service.lehrer.LehrerAnrechnungsstundenService;
import de.svws_nrw.service.lehrer.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;

public record LehrerPersonalabschnittsdatenSubServices(
		LehrerAnrechnungsstundenService anrechnungsService,
		LehrerMehrleistungService mehrleistungService,
		LehrerMinderleistungService minderleistungService,
		LehrerFunktionService funktionService
) { }
