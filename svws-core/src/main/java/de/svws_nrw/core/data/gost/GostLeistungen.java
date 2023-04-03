package de.svws_nrw.core.data.gost;

import java.util.Vector;

import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse stellt die Core-Types für Leistungsdaten für die
 * Gymnasiale Oberstufe.
 */
@XmlRootElement
@Schema(description = "Informationen zu den Leistungsdaten von einem Schüler der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLeistungen {

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

	/** Das Thema des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte. */
	public String projektkursThema = null;

	/** Das Kürzel des ersten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte. */
	public String projektkursLeitfach1Kuerzel = null;

	/** Das Kürzel eines zweiten Leitfaches des Projektkurses, sofern der Schüler einen Projektkurs belegt hatte und der Projektkurs ein zweites Leitfach hat. */
	public String projektkursLeitfach2Kuerzel = null;

	/** Gibt für die einzelnen {@link GostHalbjahr}-Werte an, ob gewertete Leistungsdaten vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = Boolean.class, description = "Gibt für die einzelnen Halbjahre der Oberstufe an, ob gewertete Leistungsdaten vorhanden sind."))
	public final @NotNull boolean[] bewertetesHalbjahr = new boolean[6];

	/** Die einzelnen Fachwahlen des Schülers. */
	public final @NotNull Vector<@NotNull GostLeistungenFachwahl> faecher = new Vector<>();

}
