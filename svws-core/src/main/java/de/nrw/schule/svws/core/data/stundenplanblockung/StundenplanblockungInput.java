package de.nrw.schule.svws.core.data.stundenplanblockung;

import java.util.Vector;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import de.nrw.schule.svws.core.utils.stundenplanblockung.StundenplanblockungManager;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanblockung. <br>
 * Zur Manipulation dieses Objektes dient der {@link StundenplanblockungManager}. 
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungInput")
@Schema(name = "StundenplanblockungInput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanblockung.")
@TranspilerDTO
public class StundenplanblockungInput {

	/** Alle Lehrkräfte. Der Manager sortiert diese Liste stets nach Kürzel der Lehrkräfte. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLehrkraft.class))
	public @NotNull Vector<@NotNull StundenplanblockungLehrkraft> lehrkraefte = new Vector<>();

	/** Alle Klassen. Der Manager sortiert diese Liste stets nach Kürzel der Klassen. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKlasse.class))
	public @NotNull Vector<@NotNull StundenplanblockungKlasse> klassen = new Vector<>();

	/** Alle Fächer. Der Manager sortiert diese Liste stets nach der Sortierungs-Nummer der Fächer. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungFach.class))
	public @NotNull Vector<@NotNull StundenplanblockungFach> faecher = new Vector<>();
	
	/** Alle Räume. Der Manager sortiert diese Liste stets nach dem Kürzel des Raumes. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungRaum.class))
	public @NotNull Vector<@NotNull StundenplanblockungRaum> raeume = new Vector<>();
	
	/** Alle Kopplungen. Der Manager sortiert diese Liste stets nach dem Kürzel der Kopplung. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKopplung.class))
	public @NotNull Vector<@NotNull StundenplanblockungKopplung> kopplungen = new Vector<>();
	
}
