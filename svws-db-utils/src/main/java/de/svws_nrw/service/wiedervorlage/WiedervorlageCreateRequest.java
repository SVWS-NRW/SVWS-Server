package de.svws_nrw.service.wiedervorlage;

import de.svws_nrw.validation.constraints.ValidDateTime;
import de.svws_nrw.service.wiedervorlage.validation.ValidPersonTypAndId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request-DTO für das Erstellen eines neuen Wiedervorlage-Eintrags.
 * Optionale Felder (idBenutzergruppe, typPerson, idPerson, tsWiedervorlage)
 * dürfen {@code null} sein.
 *
 * <p>Die Felder {@code typPerson} und {@code idPerson} sind voneinander abhängig:
 * Wird {@code typPerson} gesetzt, muss auch {@code idPerson} gesetzt sein und umgekehrt.
 * Diese Cross-Field-Validierung erfolgt über {@link ValidPersonTypAndId}.</p>
 */
@ValidPersonTypAndId
public final class WiedervorlageCreateRequest {

	/**
	 * Die Bemerkung / der Betreff der Wiedervorlage. Pflichtfeld.
	 */
	@NotBlank
	public String bemerkung;

	/**
	 * Optionale ID der Benutzergruppe, für die diese Wiedervorlage sichtbar sein soll.
	 * Wenn gesetzt, muss die Gruppe in der Datenbank existieren (Prüfung im Service).
	 */
	@Positive
	public Long idBenutzergruppe;

	/**
	 * Optionaler Personentyp. Gültige Werte:
	 * <ul>
	 *   <li>null - Allgemein</li>
	 *   <li>1 – Lehrer</li>
	 *   <li>2 – Schüler</li>
	 *   <li>3 – Erzieher</li>
	 * </ul>
	 * Muss zusammen mit {@link #idPerson} angegeben werden.
	 */
	@Min(value = 1)
	@Max(value = 4)
	public Integer typPerson;

	/**
	 * Optionale ID der verknüpften Person (Lehrer, Schüler oder Erzieher).
	 * Muss zusammen mit {@link #typPerson} angegeben werden.
	 */
	@Positive
	public Long idPerson;

	/**
	 * Optionaler Wiedervorlagezeitpunkt im Format yyyy-MM-dd HH:mm:ss.
	 * Beispiel: {@code "2026-04-07 08:00:00"}
	 * Wird nicht gesetzt, bleibt das Feld in der Entität {@code null}.
	 */
	@NotNull
	@ValidDateTime
	public String tsWiedervorlage;

	/**
	 * Gibt an, ob der Eintrag automatisch als erledigt markiert werden soll,
	 * wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde.
	 */
	@NotNull
	public Boolean automatischErledigt;

}
