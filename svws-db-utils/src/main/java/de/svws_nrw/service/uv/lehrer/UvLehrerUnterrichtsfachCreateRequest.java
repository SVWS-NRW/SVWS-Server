package de.svws_nrw.service.uv.lehrer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen von Einträgen für die Unterrichtsfächer
 * eines UV-Lehrers.
 */
@Schema(description = "die Informationen zu einem Unterrichtsfach eines UV-Lehrers innerhalb der Unterrichtsverteilung.")
public class UvLehrerUnterrichtsfachCreateRequest {

	/** Die ID des UV-Lehrers, auf den sich das Unterrichtsfach bezieht. */
	@Schema(description = "Die ID des UV-Lehrers, auf den sich das Unterrichtsfach bezieht.")
	@NotNull(message = "Die ID des UvLehrers muss gesetzt werden.")
	public Long idLehrer;

	/** Die ID des Fachs, auf das sich das Unterrichtsfach bezieht (Fremdschlüssel auf EigeneSchule_Faecher). */
	@Schema(description = "Die ID des Fachs, auf das sich das Unterrichtsfach bezieht.")
	@NotNull(message = "Die ID des Fachs muss gesetzt werden.")
	public Long idFach;

	/** Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe I unterrichtet werden darf.")
	@NotNull(message = "Das Kennzeichen IstSek1 muss gesetzt werden.")
	public Boolean istSek1;

	/** Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf. */
	@Schema(description = "Gibt an, ob das Fach in der Sekundarstufe II unterrichtet werden darf.")
	@NotNull(message = "Das Kennzeichen IstSek2 muss gesetzt werden.")
	public Boolean istSek2;

	/** Eine Bemerkung zu dem Unterrichtsfach. */
	@Schema(description = "Eine Bemerkung zu dem Unterrichtsfach.")
	public String bemerkung;

	/** Das Datum, ab dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, ab dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd).")
	public String gueltigVon;

	/** Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd). */
	@Schema(description = "Das Datum, bis zu dem die Lehrkraft das Fach unterrichtet (ISO-Format yyyy-MM-dd).")
	public String gueltigBis;

}
