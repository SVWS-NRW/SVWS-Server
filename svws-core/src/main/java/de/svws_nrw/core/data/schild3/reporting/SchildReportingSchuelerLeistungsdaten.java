package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle Schuelerleistungsdaten.
 */
@XmlRootElement
@Schema(description = "Datenquelle Schuelerleistungsdaten.")
@TranspilerDTO
public class SchildReportingSchuelerLeistungsdaten {

	/** Die ID der Leistungsdaten */
	@Schema(description = "die ID der Leistungsdaten", example = "126784")
	public long id = -1;

	/** Die ID des Schüler-Lernabschnitts, zu dem die Leistungsdaten gehören. */
	@Schema(description = "die ID des Schüler-Lernabschnitts, zu dem die Leistungsdaten gehören", example = "4785")
	public long abschnittID = -1;

	/** Die ID des Faches der Leistungsdaten */
	@Schema(description = "Die ID des Faches der Leistungsdaten", example = "16")
	public long fachID = -1;

	/** Das Kürzel des Faches */
	@Schema(description = "das Kürzel des Faches", example = "D")
	public @NotNull String fachKuerzel = "";

	/** Die Bezeichnung des Faches */
	@Schema(description = "die Bezeichnung des Faches", example = "Deutsch")
	public @NotNull String fach = "";

	/** Die ID des Fachlehrers */
	@Schema(description = "Die ID des Fachlehrers, sofern einer zugewiesen ist - ansonsten null", example = "42")
	public Long lehrerID = null;

	/** Das Kürzel des Fachlehrers */
	@Schema(description = "das Kürzel des Fachlehrers, sofern einer zugewiesen ist - ansonsten null", example = "Alt")
	public String lehrerKuerzel = null;

	/** Die ID des Kurses, sofern vorhanden */
	@Schema(description = "Die ID des Kurses, sofern vorhanden", example = "4711")
	public Long kursID = null;

	/** Die Bezeichnung des Kurses, sofern vorhanden, sonst leer */
	@Schema(description = "Die Bezeichnung des Kurses, sofern vorhanden, sonst leer", example = "M-GK1")
	public @NotNull String kurs = "";

	/** Die spezielle Kursart */
	@Schema(description = "Die spezielle Kursart", example = "GKS")
	public @NotNull String kursart = "";

	/** Die allgemeine Kursart */
	@Schema(description = "Die allgemeine Kursart", example = "GK")
	public @NotNull String kursartAllg = "";

	/** Die Bezeichnung der Note */
	@Schema(description = "Die Bezeichnung der Note", example = "ausreichend (plus)")
	public @NotNull String note = "";

	/** Das Notenkürzel */
	@Schema(description = "Das Notenkürzel", example = "4+")
	public @NotNull String noteKuerzel = "";

	/** Die Notenpunkte, sofern eine Note gesetzt ist */
	@Schema(description = "Die Notenpunkte, sofern eine Note gesetzt ist", example = "6")
	public Integer notePunkte = null;

	/** Wert für allgemeine Sortierung der Fächer. Wird mit aufgenommen, um in Reports eine benutzerdefinierte Sortierung zu ermöglichen */
	@Schema(description = "Der Wert für die allgemeine Sortierung der Fächer", example = "50")
	public Integer sortierungAllg = null;

	/** Wert für Sortierung der Fächer in der Sek-II. Wird mit aufgenommen, um in Reports eine benutzerdefinierte Sortierung zu ermöglichen */
	@Schema(description = "Der Wert für die Sortierung für Fächer in der Sek-II", example = "102")
	public Integer sortierungSekII = null;

	// TODO weitere Attribute

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchildReportingSchuelerLeistungsdaten() {
		// leer
	}

}
