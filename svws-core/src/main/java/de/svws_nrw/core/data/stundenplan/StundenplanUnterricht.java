package de.svws_nrw.core.data.stundenplan;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Unterricht im Stundenplan.
 */
@XmlRootElement
@Schema(description = "der Unterricht im Stundenplan.")
@TranspilerDTO
public class StundenplanUnterricht {

	/** Die ID der Unterrichtseinheit */
	@Schema(description = "die ID der Unterrichtseinheit", example = "815")
	public long id = -1;

	/** Die ID im Zeitraster des Stundenplans */
	@Schema(description = "die ID im Zeitraster des Stundenplans", example = "1")
	public long idZeitraster = -1;

	/** Der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 */
	@Schema(description = "der Wochen-Typ bei der Unterscheidung von (A,B,... -Wochen -> 1, 2, ...) oder 0 ", example = "0")
	public int wochentyp = -1;

	/** Die ID des Kurses, sofern es sich um {@link StundenplanKurs} handelt, andernfalls NULL.*/
	@Schema(description = "die ID des Kurses, sofern es sich um Kursunterricht handelt, andernfalls NULL.", example = "89")
	public Long idKurs = null;

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "89")
	public long idFach = -1;

	/** Die IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind. */
	@ArraySchema(
			schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Lehrer, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<Long> lehrer = new ArrayList<>();

	/** Die IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind. Diese Liste ist leer, falls idKurs definiert ist.
	 *  Dann müssen die Klassen über die Schüler des Kurses aggregiert werden!*/
	@ArraySchema(
			schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Klassen, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<Long> klassen = new ArrayList<>();

	/** Die IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den IDs der Räume, die dieser Unterrichtseinheit zugeordnet sind."))
	public @NotNull List<Long> raeume = new ArrayList<>();

	/** Die IDs der Schienen, die dieser Unterrichtseinheit zugeordnet sind (im Normalfall eine, bei Kursen mit Schülern aus mehreren Jahrgangsstufen ggf. mehrere). */
	@ArraySchema(schema = @Schema(implementation = Long.class,
			description = "Ein Array mit den IDs der Schienen, die dieser Unterrichtseinheit zugeordnet sind (im Normalfall eine, bei Kursen mit Schülern aus mehreren Jahrgangsstufen ggf. mehrere)."))
	public @NotNull List<Long> schienen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public StundenplanUnterricht() {
		// leer
	}

}
