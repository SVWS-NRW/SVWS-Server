package de.svws_nrw.core.data.stundenplanblockung;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Kurs bei
 * {@link StundenplanInputSimple}. */
@XmlRootElement(name = "StundenplanInputSimpleKurs")
@Schema(name = "StundenplanInputSimpleKurs", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für einen Kurs bei {@link StundenplanInputSimple}.")
@TranspilerDTO
public class StundenplanInputSimpleKurs {

	/** 0 bis n Lehrkräfte werden dem Kurs zugeordnet. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleLehrkraft.class))
	public @NotNull Vector<@NotNull StundenplanInputSimpleLehrkraft> lehrkraefte = new Vector<>();

	/** 0 bis n Klassen werden dem Kurs zugeordnet. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKlasse.class))
	public @NotNull Vector<@NotNull StundenplanInputSimpleKlasse> klassen = new Vector<>();

	/** 0 oder 1 Fach wird dem Kurs zugeordnet. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleFach.class))
	public @NotNull Vector<@NotNull StundenplanInputSimpleFach> faecher = new Vector<>();

	/** 0 bis n potentielle Räume, von denen 0 oder 1 Raum dem Kurs zugeordnet wird. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleRaum.class))
	public @NotNull Vector<@NotNull StundenplanInputSimpleRaum> raeume = new Vector<>();

	/** 0 oder 1 Kopplung wird dem Kurs zugeordnet. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKopplung.class))
	public @NotNull Vector<@NotNull StundenplanInputSimpleKopplung> kopplungen = new Vector<>();

	/** Die Wochenstunden des Kurses. Das Stundenplanprogramm bestimmt, wie diese verteilt werden. */
	public int wochenstunden = -1;

}
