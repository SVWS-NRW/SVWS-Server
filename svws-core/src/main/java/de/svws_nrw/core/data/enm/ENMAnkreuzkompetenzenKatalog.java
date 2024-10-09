package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten zu
 * dem Katalog der Ankreuzkompetenzen für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die grundlegende Struktur von JSON-Daten zu dem Katalog der Ankreuzkompetenzen für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMAnkreuzkompetenzenKatalog {

	/** Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an (hier mit einer Verschiebung von 1 zum Array-Index). */
	@ArraySchema(schema = @Schema(implementation = String.class,
			description = "Gibt für die Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an."))
	public @NotNull String[] textStufen = new String[5];

	/** Der für die frei definierbare Zeugnisrubrik "Sonstiges" zu verwendenden Text. */
	@Schema(description = "Der für die frei definierbare Zeugnisrubrik \"Sonstiges\" zu verwendenden Text.", example = "100815")
	public String textSonstiges = null;

	/** Die Katalog-Einträge für die Ankreuzkompetenzen, die in der Notendatei enthalten sind.  */
	@ArraySchema(schema = @Schema(implementation = ENMAnkreuzkompetenz.class,
			description = "Ein Array mit den Katalog-Einträge für die Ankreuzkompetenzen, die in der Notendatei enthalten sind."))
	public @NotNull List<ENMAnkreuzkompetenz> kompetenzen = new ArrayList<>();

}
