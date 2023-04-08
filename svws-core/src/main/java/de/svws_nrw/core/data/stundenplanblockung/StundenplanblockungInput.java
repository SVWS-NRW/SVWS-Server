package de.svws_nrw.core.data.stundenplanblockung;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanblockung.
 *
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungInput")
@Schema(name = "StundenplanblockungInput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanblockung.")
@TranspilerDTO
public class StundenplanblockungInput {

	/** Alle Lehrkräfte, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLehrkraft.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungLehrkraft> lehrkraefte = new ArrayList<>();

	/** Alle Klassen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKlasse.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungKlasse> klassen = new ArrayList<>();

	/** Alle Fächer, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungFach.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungFach> faecher = new ArrayList<>();

	/** Alle Räume, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungRaum.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungRaum> raeume = new ArrayList<>();

	/** Alle Kopplungen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKopplung.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungKopplung> kopplungen = new ArrayList<>();

	/** Alle Lerngruppen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLerngruppe.class))
	public @NotNull ArrayList<@NotNull StundenplanblockungLerngruppe> lerngruppen = new ArrayList<>();

}
