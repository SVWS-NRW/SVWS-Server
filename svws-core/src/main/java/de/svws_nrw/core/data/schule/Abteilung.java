package de.svws_nrw.core.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zu einer Abteilung,
 * welche von einem Lehrer geleitet wird.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Abteilung, welche von einem Lehrer geleitet wird.")
@TranspilerDTO
public class Abteilung {
	/** Die ID des Eintrags für die Abteilung */
	@Schema(description = "die ID des Eintrags für die Abteilung", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung der Abteilung (max. 50 Zeichen) */
	@Schema(description = "die Bezeichnung der Abteilung (max. 50 Zeichen)", example = "4712")
	public @NotNull String bezeichnung = "";

	/** Die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat. */
	@Schema(description = "die Lehrer-ID des Abteilungsleiters, sofern die Abteilung einen zugewiesen hat", example = "null")
	public Long idAbteilungsleiter;

	/** Die Bezeichnung des Raums des Abteilungsleiters (z.B. für Briefköpfe, max. 20 Zeichen) */
	@Schema(description = "die Bezeichnung des Raums des Abteilungsleiters (z.B. für Briefköpfe, max. 20 Zeichen)", example = "R0815")
	public String raum;

	/** Die eMail-Adresse des Abteilungsleiters (max. 100 Zeichen) */
	@Schema(description = "die eMail-Adresse des Abteilungsleiters (max. 100 Zeichen)", example = "max.mustermann@home")
	public String email;

	/** Die interne telefonische Durchwahl des Abteilungsleiters */
	@Schema(description = "die interne telefonische Durchwahl des Abteilungsleiters", example = "007")
	public String durchwahl;

	/** Gibt an, ob die Abteilung in der Anwendung sichtbar sein soll oder nicht */
	@Schema(description = "gibt an, ob die Abteilung in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Gibt einen Wert für die Sortierung der Abteilungen an. */
	@Schema(description = "gibt einen Wert für die Sortierung der Abteilungen an", example = "32000")
	public int sortierung;

	/** Die Zuordnung der Klassen zu der Abteilung. */
	@ArraySchema(
			schema = @Schema(implementation = AbteilungKlassenzuordnung.class,
			description = "die Zuordnung der Klassen zu der Abteilung.",
			accessMode = Schema.AccessMode.READ_ONLY)
	)
	public final @NotNull List<AbteilungKlassenzuordnung> klassenzuordnungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public Abteilung() {
		// leer
	}

}
