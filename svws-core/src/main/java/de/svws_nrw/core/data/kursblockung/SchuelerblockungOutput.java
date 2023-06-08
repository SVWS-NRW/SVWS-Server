package de.svws_nrw.core.data.kursblockung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen
 * zuordnet.
 */
@XmlRootElement(name = "SchuelerblockungOutput")
@Schema(name = "SchuelerblockungOutput", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten, die Fachwahlen EINES Schülers zu Kursen zuordnet.")
@TranspilerDTO
public class SchuelerblockungOutput {

	/** Alle Fachwahlen-Zuordnungen. */
	public @NotNull List<@NotNull SchuelerblockungOutputFachwahlZuKurs> fachwahlenZuKurs = new ArrayList<>();

}
