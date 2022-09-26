package de.nrw.schule.svws.core.data.kursblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für Eingabedaten, die dem Blockungsalgorithmus
 * übergeben werden. */
@XmlRootElement(name = "KursblockungInput")
@Schema(name = "KursblockungInput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Input-Daten des Kursblockungsalgorithmus.")
@TranspilerDTO
public class KursblockungInput {

	/** Der Seed wird verwendet, um beim Kursblockungsalgorithmus den Zufall zu steuern. Berechnungen mit dem selben
	 * Seed führen deterministisch zu identischen Blockungs-Ergebnissen. Ausgenommen ist der Wert 0, dieser führt dazu,
	 * dass der Seed vom Algorithmus zufällig initialisiert wird. */
	@Schema(required = true, description = "TODO", example = "1")
	public long seed = 0;

	/** Die ID dieses Eingabe-Objekts, nötig für die Datenbank. */
	@Schema(required = true, description = "TODO", example = "1")
	public long input;

	/** Die maximale Blockungszeit in Millisekunden. */
	@Schema(required = true, description = "TODO", example = "1")
	public long maxTimeMillis;

	/** Die maximale Anzahl an zur Verfügung stehenden Schienen. */
	@Schema(required = true, description = "TODO", example = "1")
	public int maxSchienen;

	/** Alle Regeln dieser Blockung. Die Regeln werden in ihrer Reihenfolge abgearbeitet, was ggf. eine Rolle spielen
	 * kann. Wenn bestimmte Regeln fehlen, werden Standardwerte angenommen. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputRegel.class))
	public @NotNull Vector<@NotNull KursblockungInputRegel> regeln = new Vector<>();

	/** Alle Fächer. Es reichen die Fächer, die für die Blockung relevant sind. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputFach.class))
	public @NotNull Vector<@NotNull KursblockungInputFach> faecher = new Vector<>();

	/** Alle Kursarten. Es reichen die Kursarten, die für die Blockung relevant sind. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputKursart.class))
	public @NotNull Vector<@NotNull KursblockungInputKursart> kursarten = new Vector<>();

	/** Alle Kurse. Ein Kurs ist genau einer Fachart (= Fach + Kursart) zugeordnet. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputKurs.class))
	public @NotNull Vector<@NotNull KursblockungInputKurs> kurse = new Vector<>();

	/** Alle Schüler. Ein Schüler hat Fachwahlen. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputSchueler.class))
	public @NotNull Vector<@NotNull KursblockungInputSchueler> schueler = new Vector<>();

	/** Die Fachwahlen aller SuS. */
	@ArraySchema(schema = @Schema(implementation = KursblockungInputFachwahl.class))
	public @NotNull Vector<@NotNull KursblockungInputFachwahl> fachwahlen = new Vector<>();

}
