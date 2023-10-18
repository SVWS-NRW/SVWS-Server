package de.svws_nrw.core.data.druck;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 *  Core-DTO mit den Daten für Schüler für den Druck von Kursen aus der Gost-Kursplanung mit Schülerliste
 */
@XmlRootElement
@Schema(description = "Die Daten für Schüler für den Druck von Kursen aus der Gost-Kursplanung mit Schülerliste.")
@TranspilerDTO
public class DruckGostKursplanungKursSchueler {

	/** ID des Schülers. */
	@Schema(description = "die ID des Schülers", example = "2345")
	public long Id = -1;

	/** Nachname des Schülers. */
	@Schema(description = "der Nachname des Schülers", example = "Mustermann")
	public @NotNull String Nachname = "";

	/** Vorname des Schülers. */
	@Schema(description = "der Vorname des Schülers", example = "Max")
	public @NotNull String Vorname = "";

	/** Geschlecht des Schülers. */
	@Schema(description = "das Geschlecht des Schülers", example = "m")
	public String Geschlecht = "";

	/** Geburtsdatum des Schülers. */
	@Schema(description = "das Geburtsdatum des Schülers", example = "02.02.2007")
	public String Geburtsdatum = "";

	/** Externe Schulnummer des Schülers, wenn er den Status extern hat. */
	@Schema(description = "die externe Schulnummer des Schülers", example = "123456")
	public String ExterneSchulnummer = "";

	/** Kursbelegung des Schülers, d. h. schriftlich oder mündlich. */
	@Schema(description = "die Belegung des übergeordneten Kurses aus der Kursplanung durch den Schüler", example = "s")
	public String Belegung = "";

	/** Nummer des Abiturfaches, sofern das Fach des Kurses ein Abiturfach des Schülers ist. */
	@Schema(description = "die Abiturfachbelegung des übergeordneten Kurses aus der Kursplanung durch den Schüler", example = "3")
	public String Abiturfach = "";
}
