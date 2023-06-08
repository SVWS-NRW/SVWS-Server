package de.svws_nrw.core.data.kursblockung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die dem Schüler-Blockungsalgorithmus übergeben werden.
 * Dabei handelt es sich um eine (Neu-)Zuweisung EINES Schülers auf eine existierende Kurslage.
 */
@XmlRootElement(name = "SchuelerblockungInput")
@Schema(name = "SchuelerblockungInput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die dem Schüler-Blockungsalgorithmus übergeben werden. Dabei handelt es sich um eine (Neu-)Zuweisung EINES Schülers auf eine existierende Kurslage.")
@TranspilerDTO
public class SchuelerblockungInput {

	/** Die Anzahl an vorhandenen Schienen. */
	public int schienen = 0;

	/** Alle Kurse, die zu den Fachwahlen des Schülers passen. */
	@ArraySchema(schema = @Schema(implementation = SchuelerblockungInputKurs.class))
	public @NotNull List<@NotNull SchuelerblockungInputKurs> kurse = new ArrayList<>();

	/** Alle Fachwahlen des Schülers. */
	@ArraySchema(schema = @Schema(implementation = SchuelerblockungInputFachwahl.class))
	public @NotNull List<@NotNull GostFachwahl> fachwahlen = new ArrayList<>();

	/** Zu jeder Fachwahl eine textuelle Darstellung. */
	@ArraySchema(schema = @Schema(implementation = String.class))
	public @NotNull List<@NotNull String> fachwahlenText = new ArrayList<>();

}
