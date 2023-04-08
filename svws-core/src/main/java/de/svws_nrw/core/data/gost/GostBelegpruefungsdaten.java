package de.svws_nrw.core.data.gost;

import java.util.ArrayList;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Daten, die für eine Belegprüfung in der gymnasialen Oberstufe benötigt werden.
 */
@XmlRootElement
@Schema(description = "Die Daten für eine Belegprüfung in der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostBelegpruefungsdaten {

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. */
	@Schema(description = "die Abiturdaten für die Belegprüfung", implementation = Abiturdaten.class)
	public Abiturdaten abiturdaten;

	/** Die Liste der Fächer der gymnasialen Oberstufe, die für die Belegprüfung genutzt werden sollen */
	@ArraySchema(schema = @Schema(implementation = GostBeratungslehrer.class))
	public @NotNull ArrayList<@NotNull GostFach> gostFaecher = new ArrayList<>();

}
