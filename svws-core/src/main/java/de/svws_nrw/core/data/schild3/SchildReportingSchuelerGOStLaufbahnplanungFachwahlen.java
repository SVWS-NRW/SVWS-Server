package de.svws_nrw.core.data.schild3;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle SchuelerGOStLaufbahnplanungFachwahlen.
 */
@XmlRootElement
@Schema(description="Datenquelle SchuelerGOStLaufbahnplanungFachwahlen")
@TranspilerDTO
public class SchildReportingSchuelerGOStLaufbahnplanungFachwahlen {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example="4711")
    public long schuelerID;

	/** Die Bezeichnung des Faches */
	@Schema(description = "Die Bezeichnung des Faches", example="Deutsch")
	public @NotNull String bezeichnung = "";

	/** Das Kürzel des Faches */
	@Schema(description = "Das Kürzel Faches", example="D")
	public @NotNull String kuerzel = "";

	/** Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache */
	@Schema(description = "Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache", example="false")
	public Boolean fachIstFortfuehrbareFremdspracheInGOSt = false;

	/** Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung */
	@Schema(description = "Jahrgangsstufe des Fremdsprachenbeginns", example="7")
	public @NotNull String jahrgangFremdsprachenbeginn = "";

	/** Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk */
	@Schema(description = "Position in der Fremdsprachenfolge bzw. Prüfungsvermerk", example="2")
	public @NotNull String positionFremdsprachenfolge = "";

	/** Fachbelegung in der EF.1 */
	@Schema(description = "Belegung in der EF.1", example="S")
	public @NotNull String belegungEF1 = "";

	/** Fachbelegung in der EF.2 */
	@Schema(description = "Belegung in der EF.2", example="M")
	public @NotNull String belegungEF2 = "";

	/** Fachbelegung in der Q1.1 */
	@Schema(description = "Belegung in der Q1.1", example="LK")
	public @NotNull String belegungQ11 = "";

	/** Fachbelegung in der Q1.2 */
	@Schema(description = "Belegung in der Q1.2", example="LK")
	public @NotNull String belegungQ12 = "";

	/** Fachbelegung in der Q2.1 */
	@Schema(description = "Belegung in der Q2.1", example="LK")
	public @NotNull String belegungQ21 = "";

	/** Fachbelegung in der Q2.2 */
	@Schema(description = "Belegung in der Q2.2", example="LK")
	public @NotNull String belegungQ22 = "";

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	@Schema(description = "Nummer des Abiturfaches", example="3")
	public @NotNull String abiturfach = "";

}
