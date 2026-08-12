package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import de.svws_nrw.service.lehrer.anrechnung.LehrerAnrechnungsstundeService;
import de.svws_nrw.service.lehrer.mehrleistung.LehrerMehrleistungService;
import de.svws_nrw.service.lehrer.minderleistung.LehrerMinderleistungService;
import de.svws_nrw.service.lehrer.funktion.LehrerFunktionService;

public record LehrerPersonalabschnittsdatenSubServices(
		LehrerAnrechnungsstundeService anrechnungsService,
		LehrerMehrleistungService mehrleistungService,
		LehrerMinderleistungService minderleistungService,
		LehrerFunktionService funktionService
) { }
