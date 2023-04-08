package de.svws_nrw.core.data.kursblockung;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen
 * zuordnet. */
@XmlRootElement(name = "SchuelerblockungOutput")
@Schema(name = "SchuelerblockungOutput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen zuordnet.")
@TranspilerDTO
public class SchuelerblockungOutput {

	/** Alle Fachwahlen-Zuordnungen. */
	public @NotNull ArrayList<@NotNull SchuelerblockungOutputFachwahlZuKurs> fachwahlenZuKurs = new ArrayList<>();

}
