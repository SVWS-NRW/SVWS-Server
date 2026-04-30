package de.svws_nrw.service.wiedervorlage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.svws_nrw.validation.constraints.ValidDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.openapitools.jackson.nullable.JsonNullable;

/**
 * Patch-Request-DTO für einen Wiedervorlage-Eintrag.
 * Alle Felder sind als {@link JsonNullable} deklariert, um zwischen
 * "nicht gesetzt" (absent) und "explizit null" unterscheiden zu können.
 * Nur present-Felder werden auf die Entity angewendet.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public final class WiedervorlagePatchRequest {

	/**
	 * ID der Benutzergruppe, der dieser Eintrag zugeordnet ist.
	 * Wenn gesetzt, muss die Gruppe existieren.
	 * Kann explizit auf {@code null} gesetzt werden, um die Zuordnung zu entfernen.
	 */
	public JsonNullable<@Positive Long> idBenutzergruppe = JsonNullable.undefined();

	/**
	 * Freitext-Bemerkung des Wiedervorlage-Eintrags.
	 * Darf nicht leer sein, wenn present.
	 */
	public JsonNullable<@NotBlank String> bemerkung = JsonNullable.undefined();

	/**
	 * Zeitstempel der Wiedervorlage im Format yyyy-MM-dd HH:mm:ss (z. B. {@code 2026-04-07 08:00:00}).
	 * Gibt an, zu welchem Zeitpunkt der Eintrag erneut vorgelegt werden soll.
	 */
	public JsonNullable<@NotNull @ValidDateTime String> tsWiedervorlage = JsonNullable.undefined();

	/**
	 * Gibt an, ob der Eintrag automatisch als erledigt markiert wurde.
	 * Manuelles Erledigen erfolgt über den dedizierten Endpunkt {@code postErledigt}.
	 */
	public JsonNullable<Boolean> automatischErledigt = JsonNullable.undefined();

}
