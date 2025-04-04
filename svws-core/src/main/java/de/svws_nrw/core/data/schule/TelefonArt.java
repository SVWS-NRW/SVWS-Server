package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den schulspezifischen Katalog der Telefonarten.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag in dem Katalog der schulspezifischen Telefonarten.")
@TranspilerDTO
public class TelefonArt {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "Die ID des Katalog-Eintrags", example = "2", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Bezeichnung der Telefonarten. */
	@Schema(description = "Die Bezeichnung der Telefonarten", example = "Festnetznummer")
	public @NotNull String bezeichnung = "";

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	@Schema(description = "Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an", example = "1")
	public int sortierung = 1;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar = true;

	/** Gibt an wie viele Telefonnummern der entsprechenden Telefonart zugeordnet sind. */
	@Schema(description = "Gibt an wie viele Telefonnummern der entsprechenden Telefonart zugeordnet sind", example = "3")
	public int anzahlTelefonnummern;
}
