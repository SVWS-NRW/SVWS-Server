package de.svws_nrw.core.data.kataloge;


import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegendenden Konfiguration für die Ankreuzkompetenzen.
 */
@XmlRootElement
@Schema(description = "Die Daten zu der grundlegendenden Konfiguration für die Ankreuzkompetenzen.")
@TranspilerDTO
public final class AnkreuzkompetenzKonfiguration {

	/** Gibt für die einzelnen Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an (hier mit einer Verschiebung von 1 zum Array-Index). */
	@ArraySchema(schema = @Schema(implementation = String.class,
			description = "Gibt für die Stufen 1-5 der Ankreuzkompetenzen die zu verwendenden Texte an."))
	public @NotNull String[] textStufen = new String[5];

	/** Der für die frei definierbare Zeugnisrubrik "Sonstiges" zu verwendenden Text. */
	@Schema(description = "Der für die frei definierbare Zeugnisrubrik \"Sonstiges\" zu verwendenden Text.", example = "100815")
	public String textSonstiges = null;

}
