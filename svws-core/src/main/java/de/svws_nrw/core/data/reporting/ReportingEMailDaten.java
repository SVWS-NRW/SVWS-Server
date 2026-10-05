package de.svws_nrw.core.data.reporting;

import de.svws_nrw.core.types.reporting.ReportingEMailEmpfaengerTyp;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält Daten für den Versand von E-Mails im Reporting.
 * Sie wird für die Kommunikation über die Open-API-Schnittstelle verwendet und enthält die notwendigen
 * Parameter wie Betreff und Text der E-Mail.
 */
@XmlRootElement
@Schema(description = "Daten für den Versand von E-Mails über die Reporting.")
@TranspilerDTO
public class ReportingEMailDaten {

	/** Steuert, an wen versendet wird. Die Werte sind gemäß {@link ReportingEMailEmpfaengerTyp} zu wählen. */
	@Schema(description = "Empfänger-Typ für den E-Mail-Versand.", example = "1")
	public int empfaengerTyp = ReportingEMailEmpfaengerTyp.UNDEFINED.getId();

	/** Gibt an, ob bei fehlender oder fehlerhafter schulischer E-Mail-Adresse die private E-Mail-Adresse genutzt werden soll. */
	@Schema(description = "Gibt an, ob bei fehlender oder fehlerhafter schulischer E-Mail-Adresse die private E-Mail-Adresse genutzt werden soll.",
			example = "false")
	public boolean istPrivateEmailAlternative = false;

	/** Gibt an, ob die Reportvorlage den Versand an die private E-Mail-Adresse zulässt. Der Server liest den Wert aus der Vorlage, nicht aus dem Request. */
	@Schema(description = "Gibt an, ob die Reportvorlage den Versand an die private E-Mail-Adresse zulässt.", example = "true")
	public boolean istPrivateEmailAlternativeZulaessig = true;

	/** Der Betreff der E-Mail. */
	@Schema(description = "Der Betreff der E-Mail.", example = "Persönlicher Stundenplan")
	public @NotNull String betreff = "";

	/** Der Text der E-Mail. */
	@Schema(description = "Der Text der E-Mail.", example = "Sehr geehrte Damen und Herren, ...")
	public @NotNull String text = "";

	/**
	 * Der Konstruktor der Klasse ReportingEMailDaten.
	 * Erzeugt eine Instanz der Klasse und initialisiert sie mit Standardwerten.
	 * Diese Klasse enthält Daten für den Versand von E-Mails im Reporting.
	 */
	public ReportingEMailDaten() {
		super();
	}
}
