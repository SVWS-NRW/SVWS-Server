package de.svws_nrw.core.data.gost;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Die Klasse enthält die Informationen zu einem Schüler bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zu einem Schüler bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungDatenSchueler {

	/** Die eindeutige ID des Schülers */
	@Schema(description = "Die eindeutige ID des Schülers.", example = "4711")
	public long id;

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
	@Schema(description = "Das einstellige Kürzel der Sprache des bilingualen Bildungsganges, falls der Schüler an einem solchen teilnimmt, ansonsten null.", example = "E")
	public String bilingualeSprache = null;

	/** Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt. */
	@ArraySchema(schema = @Schema(implementation = Boolean.class, description = "Gibt für die einzelnen Halbjahre der Oberstufe an, ob gewertete Leistungsdaten vorhanden sind oder es sich um Werte der Laufbahnplanung handelt."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];

	/** Ein Array mit den Fachbelegungen in der Oberstufe. */
	@ArraySchema(schema = @Schema(implementation = GostLaufbahnplanungDatenFachbelegung.class, description = "Ein Array mit den Fachbelegungen in der Oberstufe."))
	public final @NotNull List<@NotNull GostLaufbahnplanungDatenFachbelegung> fachbelegungen = new ArrayList<>();

	/** Die Sprachendaten des Schülers mit Informationen zu Sprachbelegungen (Sprachenfolge) und zu Sprachprüfungen. */
	@Schema(implementation = Sprachendaten.class, description = "Die Sprachenfolge und die Sprachprüfungen des Schülers unter Einbeziehung der Daten aus der Sekundarstufe I.")
	public @NotNull Sprachendaten sprachendaten = new Sprachendaten();

}
