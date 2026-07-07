package de.svws_nrw.service.lehrer.personalabschnittsdaten;

import org.openapitools.jackson.nullable.JsonNullable;

interface LehrerPersonalabschnittsdatenPatchable {
	JsonNullable<Double> getPflichtstundensoll();
	JsonNullable<Long> getIdRechtsverhaeltnis();
	JsonNullable<Long> getIdBeschaeftigungsart();
	JsonNullable<Long> getIdEinsatzstatus();
	JsonNullable<String> getStammschulnummer();
}
