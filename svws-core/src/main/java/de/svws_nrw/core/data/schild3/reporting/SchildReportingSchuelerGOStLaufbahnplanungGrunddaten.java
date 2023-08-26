package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.base.annotations.SchildReportingDate;
import de.svws_nrw.base.annotations.SchildReportingMemo;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle SchuelerGOStLaufbahnplanungGrunddaten.
 */
@XmlRootElement
@Schema(description = "Datenquelle SchuelerGOStLaufbahnplanungGrunddaten")
@TranspilerDTO
public class SchildReportingSchuelerGOStLaufbahnplanungGrunddaten {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example = "4711")
    public long schuelerID;

    /** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
    @Schema(description = "Das Jahr, in welchem die Abiturprüfung stattfindet", example = "2024")
    public int abiturjahr;

	/** Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt */
	@Schema(description = "Das Halbjahr der Oberstufenlaufbahn, in dem sich der Schüler befindet", example = "Q1.1")
	public @NotNull String aktuellesGOSthalbjahr = "";

	/** Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt */
	@Schema(description = "Das Halbjahr der Oberstufenlaufbahn, für das die Beratung erfolgt", example = "Q1.2")
	public @NotNull String beratungsGOSthalbjahr = "";

	/** Der Text der Schule für den Beratungsbogen */
	@SchildReportingMemo
	@Schema(description = "Der Text der Schule für den Beratungsbogen", example = "Mit der Abgabe der folgenden Wahl ...")
	public @NotNull String beratungsbogentext = "";

	/** Der Text der Schule für den E-Mail-Versand */
	@SchildReportingMemo
	@Schema(description = "Der Text der Schule für das versenden der Wahldateien mit einer E-Mail", example = "Mit dieser E-Mail ...")
	public @NotNull String emailtext = "";

	/** Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt */
	@Schema(description = "Die Beratungslehrkräfte des Abiturjahrgangs durch Semikolon getrennt", example = "Klaus Müller; Erwin Meier")
	public @NotNull String beratungslehrkraefte = "";

	/** Die Lehrkraft der letzten Beratung */
	@Schema(description = "Die Beratungslehrkraft der letzten Beratung oder bei dessen Fehlen die des Abiturjahrgangs durch ';' getrennt", example = "Klaus Müller; Erwin Meier")
	public @NotNull String beratungslehrkraft = "";

	/** Das Datum des Rücklaufes der letzten importierten Wahldatei */
	@SchildReportingDate
	@Schema(description = "Das Datum der letzten Änderung", example = "2023-01-14")
	public @NotNull String ruecklaufdatum = "";

	/** Das Datum der letzten Beratung */
	@SchildReportingDate
	@Schema(description = "Das Datum der letzten Beratung", example = "2023-01-15")
	public @NotNull String beratungsdatum = "";

	/** Kommentar der Schule zur Laufbahn */
	@SchildReportingMemo
	@Schema(description = "Kommentar der Schule zur Laufbahnplanung", example = "Wir empfehlen ...")
    public @NotNull String kommentar = "";
}
