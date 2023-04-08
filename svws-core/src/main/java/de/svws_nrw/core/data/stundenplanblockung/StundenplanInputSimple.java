package de.svws_nrw.core.data.stundenplanblockung;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer
 * Stundenplanberechnung. Es ist ein simples Austauschformat bei der viele Informationen nicht vorhanden
 * sind. */
@XmlRootElement(name = "StundenplanInputSimple")
@Schema(name = "StundenplanInputSimple", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für die Eingabedaten einer Stundenplanberechnung. Es ist ein simples Austauschformat bei der viele Informationen nicht vorhanden sind.")
@TranspilerDTO
public class StundenplanInputSimple {

	/** Alle Lehrkräfte. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleLehrkraft.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleLehrkraft> lehrkraefte = new ArrayList<>();

	/** Alle Klassen. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKlasse.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleKlasse> klassen = new ArrayList<>();

	/** Alle Fächer. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleFach.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleFach> faecher = new ArrayList<>();

	/** Alle Räume. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleRaum.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleRaum> raeume = new ArrayList<>();

	/** Alle Kopplungen. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKopplung.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleKopplung> kopplungen = new ArrayList<>();

	/** Alle Kurse. */
	@ArraySchema(schema = @Schema(implementation = StundenplanInputSimpleKurs.class))
	public @NotNull ArrayList<@NotNull StundenplanInputSimpleKurs> kurse = new ArrayList<>();

}
