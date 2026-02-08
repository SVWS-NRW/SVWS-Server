package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Eine Klasse für die Gruppierung von ReportVorlageParameter.
 */
@XmlRootElement
@Schema(description = "Eine Klasse für die Metadaten einer UI-Gruppe.")
@TranspilerDTO
public class ReportingVorlageParameterGruppe {

	/** Der Name der Gruppe. */
	@Schema(description = "Der Name der Gruppe.")
	public @NotNull String name = "";

	/** Die Beschreibung der Gruppe. */
	@Schema(description = "Die Beschreibung der Gruppe.")
	public @NotNull String beschreibung = "";

	/** Gibt an, ob die Gruppe in der UI sichtbar sein soll. */
	@Schema(description = "Gibt an, ob die Gruppe in der UI sichtbar sein soll.")
	public @NotNull String istSichtbar = "";

	/** Die Anzahl der Grid-Spalten, die für die Gruppe zur Verfügung stehen. */
	@Schema(description = "Die Anzahl der Grid-Spalten, die für die Gruppe zur Verfügung stehen.")
	public int anzahlSpalten = 1;

	/** Die Liste der ReportingVorlageParameter, die zu dieser Gruppe gehören. */
	@Schema(description = "Die Liste der ReportingVorlageParameter, die zu dieser Gruppe gehören.")
	public @NotNull List<ReportingVorlageParameter> reportingVorlageParameterList = new ArrayList<>();

	/**
	 * Konstruktor für die Klasse.
	 */
	public ReportingVorlageParameterGruppe() {
		super();
	}
}
