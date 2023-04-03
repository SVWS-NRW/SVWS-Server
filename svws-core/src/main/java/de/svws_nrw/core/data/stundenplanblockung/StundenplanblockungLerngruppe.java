package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lerngruppe bei {@link StundenplanblockungInput}. <br>
 * Eine Lerngruppe hat 0-* zugeordnete Lehrkräfte. <br>
 * Eine Lerngruppe hat 0-* zugeordnete hospitierende Lehrkräfte. Bei Krankheit nicht relevant für die Lerngruppe.<br>
 * Eine Lerngruppe hat 0-* zugeordnete Klassen. <br>
 * Eine Lerngruppe hat 0-* zugeordnete Fächer (meistens genau ein Fach). <br>
 * Eine Lerngruppe hat 0-* zugeordnete Primärräume, die für die Lerngruppe in Frage kommen. <br>
 * Eine Lerngruppe hat 0-* zugeordnete Sekundärräume, die für die Lerngruppe alternativ in Frage kommen. <br>
 * Eine Lerngruppe hat 0-* zugeordnete Kopplungen. <br>
 *
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungLerngruppe")
@Schema(name = "StundenplanblockungLerngruppe", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Lerngruppe bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungLerngruppe {

	/** Die Datenbank-ID der Lerngruppe. */
	public long id;

	/** Alle Lehrkräfte, die dieser Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLehrkraft.class))
	public @NotNull Vector<@NotNull StundenplanblockungLehrkraft> lehrkraefte1 = new Vector<>();

	/** Alle Lehrkräfte, die dieser Lerngruppe hospitierend zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungLehrkraft.class))
	public @NotNull Vector<@NotNull StundenplanblockungLehrkraft> lehrkraefte2 = new Vector<>();

	/** Alle Klassen, die dieser Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKlasse.class))
	public @NotNull Vector<@NotNull StundenplanblockungKlasse> klassen = new Vector<>();

	/** Alle Fächer, die dieser Lerngruppe zugeordnet sind. In der Regel genau ein Fach.*/
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungFach.class))
	public @NotNull Vector<@NotNull StundenplanblockungFach> faecher = new Vector<>();

	/** Alle Räume, die für diese Lerngruppe primär in Frage kommen.*/
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungRaum.class))
	public @NotNull Vector<@NotNull StundenplanblockungRaum> raeume1 = new Vector<>();

	/** Alle Räume, die für diese Lerngruppe sekundär (alternativ) in Frage kommen.*/
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungRaum.class))
	public @NotNull Vector<@NotNull StundenplanblockungRaum> raeume2 = new Vector<>();

	/** Alle Kopplungen, die dieser Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungKopplung.class))
	public @NotNull Vector<@NotNull StundenplanblockungKopplung> kopplungen = new Vector<>();

	/** Alle Stundenelemente, die dieser Lerngruppe zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = StundenplanblockungStundenelement.class))
	public @NotNull Vector<@NotNull StundenplanblockungStundenelement> stundenelemente = new Vector<>();

}
