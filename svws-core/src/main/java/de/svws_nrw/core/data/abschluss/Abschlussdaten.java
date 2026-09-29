package de.svws_nrw.core.data.abschluss;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse liefert die allgemeinen Angaben zu dem Lernabschnitt eines Schülers zurück.
 */
@XmlRootElement
@Schema(description = "Die Informationen zu dem Abschluss in einem Lernabschnitt eines Schülers.")
@TranspilerDTO
public class Abschlussdaten {

	/** Die ID des Lernabschnitts in der Datenbank. */
	@Schema(description = "die ID des Lernabschnitts in der Datenbank", example = "126784")
	public long idLernabschnitt;

	/** Die Prüfungsordnung, welche der Abschlussberechnung zugrunde liegt. */
	@Schema(description = "die Prüfungsordnung", example = "APO-SI20")
	public String pruefungsordnung = null;

	/** Die ID des erreichten allgemeinbildende Abschlusses */
	@Schema(description = "die ID des erreichten allgemeinbildende Abschlusses", example = "null")
	public Long idAbschluss = null;

	/** Die ID des erreichten berufsbezogenen Abschlusses am Berufskolleg */
	@Schema(description = "die ID des erreichten berufsbezogenen Abschlusses am Berufskolleg", example = "null")
	public Long idAbschlussBerufsbildend = null;

	/** Gibt an, ob es sich bei dem Abschluss um eine Prognose handelt oder nicht */
	@Schema(description = "gibt an, ob es sich bei dem Abschluss um eine Prognose handelt oder nicht", example = "false")
	public boolean istAbschlussPrognose = false;

	/** Die Art des Abschlusses für den Lernabschnitt (0 = Jahrgang ohne Abschluss, 1 = Abschluss erreicht, 2 = ohne Abschluss, 3 = ohne Abschluss mit Nachprüfung) */
	@Schema(description = "die Art des Abschlusses für den Lernabschnitt (0 = Jahrgang ohne Abschluss, 1 = Abschluss erreicht, 2 = ohne Abschluss, 3 = ohne Abschluss mit Nachprüfung)", example = "null")
	public Long idAbschlussart = null;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung */
	@Schema(description = "die textuelle Ausgabe des Prüfungsalgorithmus für die Versetzungs-/Abschlussberechnung", example = "null")
	public String textErgebnisPruefungsalgorithmus = null;

	/** Die ID des bei der auf Quartalsnoten basierenden Prognose erreichten allgemeinbildende Abschlusses */
	@Schema(description = "die ID des bei der auf Quartalsnoten basierenden Prognose erreichten allgemeinbildende Abschlusses", example = "null")
	public Long idAbschlussQuartalsprognose = null;

	/** Die textuelle Ausgabe des Prüfungsalgorithmus bei der auf Quartalsnoten basierenden Prognose für die Abschlussberechnung */
	@Schema(description = "die textuelle Ausgabe des Prüfungsalgorithmus bei der auf Quartalsnoten basierenden Prognose für die Abschlussberechnung", example = "null")
	public String textErgebniseQuartalsprognose = null;


	/**
	 * Leerer Standardkonstruktor.
	 */
	public Abschlussdaten() {
		// leer
	}

}
