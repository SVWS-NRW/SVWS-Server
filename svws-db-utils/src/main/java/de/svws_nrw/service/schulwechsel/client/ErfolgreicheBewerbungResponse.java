package de.svws_nrw.service.schulwechsel.client;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Repräsentiert eine erfolgreiche Bewerbung, wie sie vom Schulbewerbungs-Service geliefert wird.
 *
 * @param schulkindId       die eindeutige ID des Schulkinds
 * @param createdAt         der Zeitpunkt der Erstellung
 * @param bewerbungsstatus  der Status der Bewerbung
 */
public record ErfolgreicheBewerbungResponse(
		@JsonProperty("schulkindId") String schulkindId,
		@JsonProperty("createdAt") OffsetDateTime createdAt,
		@JsonProperty("bewerbungsstatus") String bewerbungsstatus
) {

}
