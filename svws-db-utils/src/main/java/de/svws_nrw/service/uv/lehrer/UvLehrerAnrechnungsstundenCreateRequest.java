package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Anrechnungsstunden von Lehrern.
 */
@Schema(description = "die Informationen zu einem Lehrer innerhalb der Unterrichtsverteilung.")
public class UvLehrerAnrechnungsstundenCreateRequest {

	/** Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht. */
	@Schema(description = "Die ID des Lehrers, auf den sich das Pflichtstundensoll bezieht.")
	@NotNull(message = "Die ID der UvLehrers muss gesetzt werden.")
	public Long idLehrer;

	/** Das Kürzel des Anrechnungsgrundes (z. B. 'AG' für Arbeitsgemeinschaft). */
	@Schema(description = "das Kürzel des Anrechnungsgrundes", example = "AG")
	@NotNull(message = "Das Kürzel des Anrechnungsgrunds muss gesetzt werden.")
	public String anrechnungsgrundKrz;

	/** Die Anzahl der angerechneten Stunden. */
	@Schema(description = "die Anzahl der angerechneten Stunden", example = "2.5")
	@NotNull(message = "Die Anzahl der Anrechnungsstunden muss vorhanden sein.")
	@Min(value = 0, message = "Die Anzahl der Stunden darf nicht negativ sein.")
	public Double anzahlStunden;

	/** Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	@NotNull(message = "Der Gültigkeitsbeginn eines Pflichtstundensolls muss gesetzt werden.")
	public String gueltigVon;

	/** Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem dieses Pflichtstundensoll gültig ist (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

}
