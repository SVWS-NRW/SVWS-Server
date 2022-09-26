package de.nrw.schule.svws.core.data.stundenplanblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Kurs bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleKurs")
@Schema(name = "StundenplanInputSimpleKurs", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Kurs bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleKurs {

	/** Alle Lehrkräfte des Kurses. Die Liste darf 0-* Elemente beinhalten. 
	 *  Alle Lehrkräfte werden dem Kurs zugeordnet.  
	 */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleLehrkraft.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleLehrkraft> lehrkraefte = new Vector<>();

	/** Alle Klassen des Kurses. Die Liste darf 0-* Elemente beinhalten. 
	 *  Alle Klassen werden dem Kurs zugeordnet.
	 */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKlasse.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleKlasse> klassen = new Vector<>();

	/** Das Fach des Kurses. Die Liste darf 0-1 Element beinhalten.
	 *  Genau ein oder kein Fach wird dem Kurs zugeordnet.
	 */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleFach.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleFach> faecher = new Vector<>();

	/** Alle potentiellen Räume des Kurses. Die Liste darf 0-* Elemente beinhalten. 
	 *  Genau ein oder kein Raum der Liste wird dem Kurs zugeordnet. 
	 */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleRaum.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleRaum> raeume = new Vector<>();

	/** Die Kopplung des Kurses. Die Liste darf 0-1 Element beinhalten.
	 *  Genau eine oder keine Kopplung wird dem Kurs zugeordnet.
	 */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKopplung.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleKopplung> kopplungen = new Vector<>();

	/** Die Anzahl der Stunden des Kurses. Wie die Stunden auf die Woche verteilt werden, ist in diesem
	 * Format nicht definiert und wird vom Stundenplanprogramm automatisch entschieden. 
	 */
	public int stunden = -1;

}
