package de.nrw.schule.svws.core.data.kursblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.nrw.schule.svws.core.data.gost.GostFachwahl;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die dem Schüler-Blockungsalgorithmus übergeben
 * werden. Dabei handelt es sich um eine (Neu-)Zuweisung EINES Schülers auf eine existierende Kurslage. */
@XmlRootElement(name = "SchuelerblockungInput")
@Schema(name = "SchuelerblockungInput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die dem Schüler-Blockungsalgorithmus übergeben werden. Dabei handelt es sich um eine (Neu-)Zuweisung EINES Schülers auf eine existierende Kurslage.")
@TranspilerDTO
public class SchuelerblockungInput {

	/** Die Anzahl an vorhandenen Schienen. */
	public int schienen = 0;

	/** Alle Kurse, die zu den Fachwahlen des Schülers passen. */
	@ArraySchema(schema = @Schema(implementation = SchuelerblockungInputKurs.class)) 
	public @NotNull Vector<@NotNull SchuelerblockungInputKurs> kurse = new Vector<>();

	/** Alle Fachwahlen des Schülers. */
	@ArraySchema(schema = @Schema(implementation = SchuelerblockungInputFachwahl.class)) 
	public @NotNull Vector<@NotNull GostFachwahl> fachwahlen = new Vector<>();

}
