package de.svws_nrw.core.data.stundenplanblockung;

import java.util.ArrayList;
import java.util.List;

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
	public @NotNull List<@NotNull StundenplanblockungLehrkraft> lehrkraefte = new ArrayList<>();

	/** Alle Klassen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKlasse.class))
	public @NotNull List<@NotNull StundenplanblockungKlasse> klassen = new ArrayList<>();

	/** Alle Fächer, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungFach.class))
	public @NotNull List<@NotNull StundenplanblockungFach> faecher = new ArrayList<>();

	/** Alle Räume, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungRaum.class))
	public @NotNull List<@NotNull StundenplanblockungRaum> raeume = new ArrayList<>();

	/** Alle Kopplungen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKopplung.class))
	public @NotNull List<@NotNull StundenplanblockungKopplung> kopplungen = new ArrayList<>();

	/** Alle Lerngruppen, die an der Stundenplanberechnung beteiligt sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLerngruppe.class))
	public @NotNull List<@NotNull StundenplanblockungLerngruppe> lerngruppen = new ArrayList<>();

}
