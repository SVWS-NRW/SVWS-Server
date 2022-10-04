package de.nrw.schule.svws.core.data.kursblockung;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen
 * zuordnet. */
@XmlRootElement(name = "SchuelerblockungOutput")
@Schema(name = "SchuelerblockungOutput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen zuordnet.")
@TranspilerDTO
public class SchuelerblockungOutput {

	/** Alle Fachwahlen-Zuordnungen. */
	public @NotNull Vector<@NotNull SchuelerblockungOutputFachwahlZuKurs> fachwahlenZuKurs = new Vector<>();

}
