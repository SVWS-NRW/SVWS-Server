package de.svws_nrw.core.data.gost.laufbahnplanung.v2;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Datenaustauschformat für die Laufbahnplanung der gymnasialen Oberstufe:
 *
 * - Die Informationen zu einem Schüler und seinen Laufbahnplanungs-Daten
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zu einem Schüler bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungExportV2Schueler {

	/** Die eindeutige ID des Schülers */
	@Schema(description = "Die eindeutige ID des Schülers.", example = "4711")
	public long id;

	/** Die ID des Schüler verschlüsselt */
	@Schema(description = "Die ID des Schüler verschlüsselt.", example = "4711")
	public @NotNull String idEnc = "";

	/** Der Vorname des Schülers */
	@Schema(description = "Der Vorname des Schülers.", example = "Max")
	public @NotNull String vorname = "";

	/** Der Nachname des Schülers */
	@Schema(description = "Der Nachname des Schülers.", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Das Geschlecht des Schülers */
	@Schema(description = "Das Geschlecht des Schülers.", example = "w")
	public @NotNull String geschlecht = "";

	/** Gibt an, ob es sich um einen Export für einen Schüler in einem G8-Jahrgang handelt oder nicht. */
	@Schema(description = "gibt an, ob es sich um einen Export für einen Schüler in einem G8-Jahrgang handelt oder nicht", example = "false")
	public boolean istG8 = false;

	/** Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt. */
	@Schema(description = "Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt, ansonsten null.",
			example = "E")
	public String bilingualeSprache = null;

	/** Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "Gibt für die einzelnen Halbjahre der Oberstufe an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];

	/** Die Informationen zu den Wahlen der Gleichwertigen komplexen Lernleistungen (ID oder null), jeweils für die Aufgabenfelder 1-3 in der EF (Index 0-2) und der Q-Phase (Index 3-5). */
	@ArraySchema(schema = @Schema(implementation = Long.class,
			description = "Die Informationen zu den Wahlen der Gleichwertigen komplexen Lernleistungen (ID oder null), jeweils für die Aufgabenfelder 1-3 in der EF (Index 0-2) und der Q-Phase (Index 3-5)."))
	public final @NotNull Long[] gkl = new Long[6];

	/** Ein Array mit den Fachbelegungen in der Oberstufe. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2SchuelerFachbelegung.class,
			description = "Ein Array mit den Fachbelegungen in der Oberstufe."))
	public final @NotNull List<GostLaufbahnplanungExportV2SchuelerFachbelegung> fachbelegungen = new ArrayList<>();

	/** Die Liste der Sprachbelegungen. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2SchuelerSprachbelegung.class,
			description = "Ein Array mit den Sprachbelegungen des Schülers."))
	public @NotNull List<GostLaufbahnplanungExportV2SchuelerSprachbelegung> sprachbelegungen = new ArrayList<>();

	/** Die Liste der Sprachprüfungen. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV2SchuelerSprachpruefung.class,
			description = "Ein Array mit den Sprachprüfungen des Schülers."))
	public @NotNull List<GostLaufbahnplanungExportV2SchuelerSprachpruefung> sprachpruefungen = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV2Schueler() {
		// leer
	}

}
