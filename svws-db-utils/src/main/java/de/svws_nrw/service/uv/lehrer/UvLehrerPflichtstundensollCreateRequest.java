package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "die Informationen zu einem Lehrer innerhalb der Unterrichtsverteilung.")
public class UvLehrerPflichtstundensollCreateRequest {

	/** Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht. */
	@Schema(description = "Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht.")
	@NotNull(message = "Die ID der UvLehrers muss gesetzt werden.")
	public Long idLehrer;

	/** Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat. */
	@Schema(description = "Die Anzahl der Pflichtstunden, die der Lehrer in dem Gültigkeitszeitraum zu leisten hat.")
	@NotNull(message = "Das Pflichtstundensoll muss vorhanden sein.")
	@Min(value = 0, message = "Die Anzahl der Pflichstunden darf nicht negativ sein.")
	public Double pflichtstdSoll;

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Der Gültigkeitsbeginn eines Pflichtstundensolls muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

}
