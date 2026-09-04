package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt das Core-DTO für die Logoverwaltung.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in der Logoverwaltung.")
@TranspilerDTO
public class Logo {

	/** Die ID des Eintrags. */
	@Schema(description = "die ID des Eintrags", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die Kennung des Logos. */
	@Schema(description = "die Kennung des Logos", example = "DIN5008_BRIEFKOPF")
	public @NotNull String kennung = "";

	/** Die Bezeichnung des Logos. */
	@Schema(description = "die Bezeichnung des Logos", example = "DIN5008-Briefkopf", deprecated = true)
	public @NotNull String bezeichnung = "";

	/** Die Beschreibung des Logos. */
	@Schema(description = "die Beschreibung des Logos", example = "Vollständiger Briefkopf für Anschreiben nach DIN5008", deprecated = true)
	public @NotNull String beschreibung = "";

	/** Das Logo als Bild im Base64-Format. */
	@Schema(description = "das Logo als Bild im Base64-Format")
	public @NotNull String logoBase64 = "";

	/** Datum, wann das Logo hinzugefügt wurde. */
	@Schema(description = "Datum, wann das Logo hinzugefügt wurde", example = "2026-04-10")
	public @NotNull String hinzugefuegtAm = "";

}
