package de.svws_nrw.core.data.reporting;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse enthält den gespeicherten Wert eines benutzerweiten Report-Parameters. Sie ist Teil des Speicherformats
 * der benutzerweiten Reporting-Einstellungen in der Client-Konfiguration (siehe {@link ReportingEinstellungenBenutzerVorlagen}).
 */
@XmlRootElement
@Schema(description = "Der gespeicherte Wert eines benutzerweiten Report-Parameters.")
@TranspilerDTO
public class ReportingEinstellungenBenutzerVorlagenParameterWert {

	/** Der Name des Parameters gemäß dem Katalog der benutzerweiten Parameter. */
	@Schema(description = "Der Name des Parameters gemäß dem Katalog der benutzerweiten Parameter.", example = "mitExternerSchuleKuerzel")
	public @NotNull String name = "";

	/** Der gespeicherte Wert des Parameters als Zeichenkette. */
	@Schema(description = "Der gespeicherte Wert des Parameters als Zeichenkette.", example = "true")
	public @NotNull String wert = "";


	/**
	 * Diese Klasse enthält den gespeicherten Wert eines benutzerweiten Report-Parameters.
	 */
	public ReportingEinstellungenBenutzerVorlagenParameterWert() {
		super();
	}
}
