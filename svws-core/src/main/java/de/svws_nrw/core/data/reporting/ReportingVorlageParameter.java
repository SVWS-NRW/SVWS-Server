package de.svws_nrw.core.data.reporting;

import de.svws_nrw.core.types.reporting.ReportingVorlageParameterTyp;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Klasse, deren Objekte an ein HTML-Template übergeben werden, damit diese das Report-Parameter-Objekt mit dem angegebenen Namen und dem
 * zugehörigen Wert erhalten.
 * Anmerkung: Die Angabe des Typs und die Verwendung des Typs String für den Wert ist darauf zurückzuführen, dass weder die Verwendung von
 * Vererbung (Basis-Klasse und abgeleitet typisierte Klassen) noch die Verwendung einer generischen Klasse eine zuverlässige Serialisierung/Deserialisierung
 * ermöglichten. (Ergebnis einer Review mit Projektleitung).
 */
@XmlRootElement
@Schema(description = "Eine Klasse, deren Objekte an ein HTML-Template übergeben werden, damit diese das Vorlage-Parameter-Objekt mit dem angegebenen Namen und dem zugehörigen Wert erhalten.")
@TranspilerDTO
public class ReportingVorlageParameter {

	/** Der Name des Vorlage-Parameters, wie er später im HTML-Template verwendet wird. */
	@Schema(description = "Der Name des Vorlage-Parameters, wie er später im HTML-Template verwendet wird.")
	public @NotNull String name = "";

	/** Die Bezeichnung des Vorlage-Parameters, wie er später in der Anzeige der GUI oder an ähnlichen Stellen dargestellt werden soll. */
	@Schema(description = "Die Bezeichnung des Vorlage-Parameters, wie er später in der Anzeige der GUI oder an ähnlichen Stellen dargestellt werden soll.")
	public @NotNull String bezeichnung = "";

	/** Der Typ des Wertes des Vorlage-Parameters. */
	@Schema(description = "Der Wert des Vorlage-Parameters.")
	public int typ = ReportingVorlageParameterTyp.UNDEFINED.getId();

	/** Der Wert des Vorlage-Parameters. */
	@Schema(description = "Der Wert des Vorlage-Parameters.")
	public @NotNull String wert = "";


	/**
	 * Konstruktor für die Klasse.
	 */
	public ReportingVorlageParameter() {
		super();
	}

}
