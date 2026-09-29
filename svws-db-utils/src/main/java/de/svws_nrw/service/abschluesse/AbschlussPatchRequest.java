package de.svws_nrw.service.abschluesse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.openapitools.jackson.nullable.JsonNullable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Klasse beschreibt das Patch-DTO für die Unterrichtsfächer von Lehrern.
 */
@Schema(description = "Die Zuordnung eines Unterrichtsfachs zu einer Lehrkraft.")
public class AbschlussPatchRequest {

	/** Die Prüfungsordnung, welche der Abschlussberechnung zugrunde liegt. */
	@Schema(description = "die Prüfungsordnung", example = "APO-SI20")
	@NotNull(message = "Das Feld 'pruefungsordnung' darf nicht null sein.")
	public JsonNullable<String> pruefungsordnung = JsonNullable.undefined();

	/** Die ID des erreichten allgemeinbildende Abschlusses */
	@Schema(description = "die ID des erreichten allgemeinbildende Abschlusses", example = "null")
	public JsonNullable<Long> idAbschluss = JsonNullable.undefined();

	/** Die ID des erreichten berufsbezogenen Abschlusses am Berufskolleg */
	@Schema(description = "die ID des erreichten berufsbezogenen Abschlusses am Berufskolleg", example = "null")
	public JsonNullable<Long> idAbschlussBerufsbildend = JsonNullable.undefined();

	/** Gibt an, ob es sich bei dem Abschluss um eine Prognose handelt oder nicht */
	@Schema(description = "gibt an, ob es sich bei dem Abschluss um eine Prognose handelt oder nicht", example = "false")
	public JsonNullable<Boolean> istAbschlussPrognose = JsonNullable.undefined();

	/** Die Art des Abschlusses für den Lernabschnitt (0 = Jahrgang ohne Abschluss, 1 = Abschluss erreicht, 2 = ohne Abschluss, 3 = ohne Abschluss mit Nachprüfung) */
	@Schema(description = "die Art des Abschlusses für den Lernabschnitt (0 = Jahrgang ohne Abschluss, 1 = Abschluss erreicht, 2 = ohne Abschluss, 3 = ohne Abschluss mit Nachprüfung)", example = "null")
	@Min(value = 0, message = "Die Abschlussart muss größer oder gleich 0 sein.")
	@Max(value = 3, message = "Die Abschlussart darf maximal 3 sein.")
	public JsonNullable<Long> idAbschlussart = JsonNullable.undefined();

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung */
	@Schema(description = "die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung", example = "null")
	public JsonNullable<String> textErgebnisPruefungsalgorithmus = JsonNullable.undefined();

	/** Die ID des bei der auf Quartalsnoten basierenden Prognose erreichten allgemeinbildende Abschlusses */
	@Schema(description = "die ID des bei der auf Quartalsnoten basierenden Prognose erreichten allgemeinbildende Abschlusses", example = "null")
	public JsonNullable<Long> idAbschlussQuartalsprognose = JsonNullable.undefined();

	/** Die textuelle Ausgabe des Prüfungsalgorithmus bei der auf Quartalsnoten basierenden Prognose für die Abschlussberechnung */
	@Schema(description = "die textuelle Ausgabe des Prüfungsalgorithmus bei der auf Quartalsnoten basierenden Prognose für die Abschlussberechnung", example = "null")
	public JsonNullable<String> textErgebniseQuartalsprognose = JsonNullable.undefined();

}
