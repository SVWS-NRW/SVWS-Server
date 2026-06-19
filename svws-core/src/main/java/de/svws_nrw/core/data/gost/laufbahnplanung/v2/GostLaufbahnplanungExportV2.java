package de.svws_nrw.core.data.gost.laufbahnplanung.v2;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Daten zu Schülern eines Abiturjahrgang der gymnasialen Oberstufe
 * - Dies sind i.A. nicht mehrere Schüler des Abiturjahrgangs, meist wird eine Export-Datei mit einem Schüler erstellt
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zum Export von Laufbahnplanungs-Daten zu einem Abiturjahrgang der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungExportV2 {

	/** Die Revision des LP-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1) */
	@Schema(description = "Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1).",
			example = "2")
	public int lpRevision = 2;

	/** Die Schulnummer der Schule, welcher die Laufbahndaten zugeordnet sind. */
	@Schema(description = "die Schulnummer der Schule, welcher die Laufbahndaten zugeordnet sind", example = "123456")
	public long schulNr;

	/** Der erste Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 1 der Schule", example = "Städt. Gymnasium")
	public @NotNull String schulBezeichnung1 = "";

	/** Der zweite Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 2 der Schule", example = "der Stadt Wuppertal")
	public @NotNull String schulBezeichnung2 = "";

	/** Der dritte Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 3 der Schule", example = "")
	public @NotNull String schulBezeichnung3 = "";

	/** Anmerkungen zu diesen Daten */
	@Schema(description = "anmerkungen zu diesen Daten", example = "Exportiert am 30.2.2023")
	public @NotNull String anmerkungen = "";

	/** Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird. */
	@Schema(description = "Das Kalenderjahr, in dem der Schüler sein Abitur ablegt bzw. ablegen wird.", example = "2025")
	public int abiturjahr = -1;

	/** Die aktuelle Jahrgangsstufe, welche dem Abiturjahrgang zugeordnet ist. */
	@Schema(description = "die aktuelle Jahrgangstufe, welche dem Abiturjahrgang zugeordnet ist", example = "Q1")
	public String jahrgang;

	/** Der derzeitige Beratungstext, welcher auf einem Ausdruck eines Schülerlaufbahnbogens für die
	 * gymnasiale Oberstufe gedruckt wird. */
	@Schema(description = "Der derzeitige Beratungstext, welcher auf einem Ausdruck eines "
			+ "Schülerlaufbahnbogens für die gymnasiale Oberstufe gedruckt wird.", example = "Wahlen zum Beginn der Q1.1")
	public String textBeratungsbogen;

	/** Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird. */
	@Schema(description = "Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird.", example = "true")
	public boolean hatZusatzkursGE = true;

	/** Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Geschichte beginnt. */
	@Schema(description = "Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Geschichte beginnt.", example = "Q2.1")
	public String beginnZusatzkursGE;

	/** Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird. */
	@Schema(description = "Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird.", example = "true")
	public boolean hatZusatzkursSW = true;

	/** Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Sozialwissenschaften beginnt. */
	@Schema(description = "Das eindeutige Kürzel des Halbjahrs, zu dem ein Zusatzkurs in Sozialwissenschaften beginnt.", example = "Q2.1")
	public String beginnZusatzkursSW;

	/** Die Liste der Beratungslehrer für diesen Jahrgang */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2Beratungslehrer.class))
	public final @NotNull List<GostLaufbahnplanungExportV2Beratungslehrer> beratungslehrer = new ArrayList<>();

	/** Die Liste der Fächer der gymnasialen Oberstufe für diesen Jahrgang */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2Fach.class))
	public final @NotNull List<GostLaufbahnplanungExportV2Fach> faecher = new ArrayList<>();

	/** Die Liste der Definition der Gleichwertigen Komplexen Lernleistungen für diesen Jahrgang */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2GKL.class))
	public final @NotNull List<GostLaufbahnplanungExportV2GKL> gkl = new ArrayList<>();

	/** Die Liste der notwendigen und der unzulässigen Kursart-spezifischen Fach-Kombinationen für diesen Jahrgang */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2Fachkombination.class))
	public final @NotNull List<GostLaufbahnplanungExportV2Fachkombination> fachkombinationen = new ArrayList<>();

	/** Die Liste der Schüler mit ihren Laufbahnplanungsdaten. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2Schueler.class))
	public final @NotNull List<GostLaufbahnplanungExportV2Schueler> schueler = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV2() {
		// leer
	}

}
