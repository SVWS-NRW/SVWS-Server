package de.svws_nrw.service.lehrer.unterrichtsfach;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Unterrichtsfächer von Lehrern.
 */
@Schema(description = "Die Zuordnung eines Unterrichtsfachs zu einer Lehrkraft.")
public class LehrerUnterrichtsfachCreateRequest {

	/** Die ID des Lehrers - muss gesetzt werden. */
	@Schema(description = "Die ID des Lehrers - muss gesetzt werden.", example = "42")
	@NotNull(message = "Die ID des Lehrers muss gesetzt werden.")
	public Long idLehrer;

	/** Die ID des Fachs - muss gesetzt werden. */
	@Schema(description = "Die ID des Fachs - muss gesetzt werden.", example = "13")
	@NotNull(message = "Die ID des Fachs muss gesetzt werden.")
	public Long idFach;

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf.", example = "true")
	@NotNull(message = "Das Feld 'istSek1' darf nicht null sein.")
	public Boolean istSek1;

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf.", example = "false")
	@NotNull(message = "Das Feld 'istSek2' darf nicht null sein.")
	public Boolean istSek2;

	/** Die Bemerkung zum Unterrichtsfach. */
	@Schema(description = "Die Bemerkung zum Unterrichtsfach.", example = "")
	public String bemerkung;

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "Das Datum, ab dem die Lehrkraft das Fach unterrichtet.", example = "2025-08-01")
	public String gueltigVon;

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet. */
	@Schema(description = "Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet.", example = "2026-07-31")
	public String gueltigBis;

}
