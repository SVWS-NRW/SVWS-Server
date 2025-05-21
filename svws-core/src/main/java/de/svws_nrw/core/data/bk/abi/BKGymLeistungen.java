package de.svws_nrw.core.data.bk.abi;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schueler.Sprachendaten;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse stellt die Core-Types für Leistungsdaten für das
 * Berufliche Gymnasium.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Leistungsdaten von einem Schüler des Beruflichen Gymnasiums.")
@TranspilerDTO
public class BKGymLeistungen {

	/** Die ID des Schülers, dessen Leistungen in diesem Objekt gespeichert sind. */
	public long id;

	/** Das aktuelle Schuljahr, welches dem Schüler zugeordnet ist. */
	public Integer aktuellesSchuljahr = null;

	/** Der Jahrgang, in dem sich der Schüler in dem aktuellen Schuljahr befindet. */
	public String aktuellerJahrgang = null;

	/** Die Sprachbelegungen (Sprachenfolge) und die Sprachprüfungen des Schülers */
	public Sprachendaten sprachendaten = null;

	/** Das einstellige Kürzel der bilingualen Sprache, sofern der Schüler einem bilingualen Bildungsgang angehört. */
	public String bilingualeSprache = null;

	/** Gibt für die einzelnen Halbjahre an, ob gewertete Leistungsdaten vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = Boolean.class,
			description = "Gibt für die einzelnen Halbjahre an, ob gewertete Leistungsdaten vorhanden sind."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];

	/** Die einzelnen Fächer des Schülers mit Leistungen. */
	public final @NotNull List<BKGymLeistungenFach> faecher = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BKGymLeistungen() {
		// leer
	}

}
