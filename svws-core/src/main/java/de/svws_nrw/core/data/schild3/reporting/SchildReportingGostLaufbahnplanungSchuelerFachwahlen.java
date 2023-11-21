package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die GOSt-Laufbahnplanung mit den Schüler-Fachwahlen.
 */
@XmlRootElement
@Schema(description = "Die Daten der Fachwahlen zur GOSt-Laufbahnplanung eines Schülers")
@TranspilerDTO
public class SchildReportingGostLaufbahnplanungSchuelerFachwahlen {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example = "4711")
    public long SchuelerID;

	/** Die Bezeichnung des Faches */
	@Schema(description = "Die Bezeichnung des Faches", example = "Deutsch")
	public @NotNull String Bezeichnung = "";

	/** Das Kürzel des Faches */
	@Schema(description = "Das Kürzel Faches", example = "D")
	public @NotNull String Kuerzel = "";

	/** Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache */
	@Schema(description = "Fach ist eine Fremdsprache in der GOSt fortführbare Fremdsprache", example = "false")
	public Boolean FachIstFortfuehrbareFremdspracheInGOSt = false;

	/** Fach ist eine Fremdsprache: Jahrgangsstufe des Beginns der Sprachbelegung */
	@Schema(description = "Jahrgangsstufe des Fremdsprachenbeginns", example = "7")
	public @NotNull String JahrgangFremdsprachenbeginn = "";

	/** Fach ist eine Fremdsprache: Position in der Fremdsprachenfolge bzw. Prüfungsvermerk */
	@Schema(description = "Position in der Fremdsprachenfolge bzw. Prüfungsvermerk", example = "2")
	public @NotNull String PositionFremdsprachenfolge = "";

	/** Fachbelegung in der EF.1 */
	@Schema(description = "Belegung in der EF.1", example = "S")
	public @NotNull String BelegungEF1 = "";

	/** Fachbelegung in der EF.2 */
	@Schema(description = "Belegung in der EF.2", example = "M")
	public @NotNull String BelegungEF2 = "";

	/** Fachbelegung in der Q1.1 */
	@Schema(description = "Belegung in der Q1.1", example = "LK")
	public @NotNull String BelegungQ11 = "";

	/** Fachbelegung in der Q1.2 */
	@Schema(description = "Belegung in der Q1.2", example = "LK")
	public @NotNull String BelegungQ12 = "";

	/** Fachbelegung in der Q2.1 */
	@Schema(description = "Belegung in der Q2.1", example = "LK")
	public @NotNull String BelegungQ21 = "";

	/** Fachbelegung in der Q2.2 */
	@Schema(description = "Belegung in der Q2.2", example = "LK")
	public @NotNull String BelegungQ22 = "";

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	@Schema(description = "Nummer des Abiturfaches", example = "3")
	public @NotNull String Abiturfach = "";

	/** Fach ist in mindestens einem Halbjahr der GOSt belegt. */
	@Schema(description = "Fach ist in mindestens einem Halbjahr der GOSt belegt.", example = "true")
	public Boolean FachIstBelegtInGOSt = false;

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	@Schema(description = "Aufgabenfeld, der das Fach zugeordnet ist", example = "3")
	public @NotNull int Aufgabenfeld;

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	@Schema(description = "Fachgruppe, der das Fach zugeordnet ist", example = "FG_M")
	public @NotNull String Fachgruppe = "";

	/** Abiturfacheintrag, sofern das belegte Fach als Abiturfach gewählt wurde */
	@Schema(description = "RGB-Farbe des Faches im Oberstufen-Client", example = "????")
	public @NotNull String FarbeClientRGB = "";

}
