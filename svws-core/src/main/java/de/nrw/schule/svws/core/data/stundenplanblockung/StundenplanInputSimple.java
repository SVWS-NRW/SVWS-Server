package de.nrw.schule.svws.core.data.stundenplanblockung;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanberechnung.
 * Es ist ein simples Austauschformat bei der viele Informationen nicht vorhanden sind. 
 */
@XmlRootElement(name = "StundenplanInputSimple")
@Schema(name = "StundenplanInputSimple", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanberechnung. Es ist ein simples Austauschformat bei der viele Informationen nicht vorhanden sind.")
@TranspilerDTO
public class StundenplanInputSimple {

	/** Alle Lehrkräfte. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleLehrkraft.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleLehrkraft> lehrkraefte = new Vector<>();

	/** Alle Klassen. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKlasse.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleKlasse> klassen = new Vector<>();

	/** Alle Fächer. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleFach.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleFach> faecher = new Vector<>();

	/** Alle Räume. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleRaum.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleRaum> raeume = new Vector<>();

	/** Alle Kopplungen. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKopplung.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleKopplung> kopplungen = new Vector<>();

	/** Alle Kurse. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKurs.class)) 
	public @NotNull Vector<@NotNull StundenplanInputSimpleKurs> kurse = new Vector<>();

}
