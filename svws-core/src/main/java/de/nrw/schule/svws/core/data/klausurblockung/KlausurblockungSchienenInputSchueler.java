package de.nrw.schule.svws.core.data.klausurblockung;

import java.util.Vector;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Spezifiziert die grundlegende Struktur von JSON-Daten für einen Schüler, der Klausuren schreibt. */
@XmlRootElement(name = "KlausurblockungSchienenInputSchueler")
@Schema(name = "KlausurblockungSchienenInputSchueler", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für einen Schüler, der Klausuren schreibt.")
@TranspilerDTO
public class KlausurblockungSchienenInputSchueler {

	/** Die Datenbank-ID des Schülers. Sie muss positiv sein, sonst wird ein Fehler erzeugt. */
	@Schema(required = true, description = "Die Datenbank-ID des Schülers. Sie muss positiv sein, sonst wird ein Fehler erzeugt.", example = "42")
	public long id = -1;

	/** Eine Sammlung der Klausuren von dieses Schülers. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull Vector<@NotNull Long> klausuren = new Vector<>();

}
