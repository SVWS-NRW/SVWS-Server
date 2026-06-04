package de.svws_nrw.core.data.gost.laufbahnplanung.v1;

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
 * - DTO für einen Schüler und seinen Laufbahnplanungs-Daten
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zu einem Schüler bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungExportV1Schueler {

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

	/** Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt. */
	@Schema(description = "Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt, ansonsten null.",
			example = "E")
	public String bilingualeSprache = null;

	/** Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "Gibt für die einzelnen Halbjahre der Oberstufe an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];

	/** Ein Array mit den Fachbelegungen in der Oberstufe. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungExportV1Fachbelegung.class,
			description = "Ein Array mit den Fachbelegungen in der Oberstufe."))
	public final @NotNull List<GostLaufbahnplanungExportV1Fachbelegung> fachbelegungen = new ArrayList<>();

	/** Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen. */
	@Schema(implementation = GostLaufbahnplanungExportV1Sprachen.class,
			description = "Die Sprachenfolge und die Sprachprüfungen des Schülers unter Einbeziehung der Daten aus der Sekundarstufe I.")
	public @NotNull GostLaufbahnplanungExportV1Sprachen sprachendaten = new GostLaufbahnplanungExportV1Sprachen();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public GostLaufbahnplanungExportV1Schueler() {
		// leer
	}

}
