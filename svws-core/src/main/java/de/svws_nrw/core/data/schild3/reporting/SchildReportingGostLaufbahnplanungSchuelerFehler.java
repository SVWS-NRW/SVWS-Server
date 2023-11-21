package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.base.annotations.SchildReportingMemo;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die GOSt-Laufbahnplanung mit den Schüler-Fehlern.
 */
@XmlRootElement
@Schema(description = "Die Daten der Fehler zur GOSt-Laufbahnplanung eines Schülers")
@TranspilerDTO
public class SchildReportingGostLaufbahnplanungSchuelerFehler {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example = "4711")
    public long schuelerID;

	/** Fehler aus der Belegprüfung */
	@SchildReportingMemo
	@Schema(description = "Fehler aus der Belegprüfung", example = "Eine Naturwissenschaft muss durchgängig belegt werden.")
	public @NotNull String belegungsfehler = "";
}
