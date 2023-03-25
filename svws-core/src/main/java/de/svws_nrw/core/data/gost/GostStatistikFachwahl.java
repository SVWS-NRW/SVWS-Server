package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Fachwahlinformationen eines Faches in einem Jahrgang der gymnasialen Oberstufe.  
 */
@XmlRootElement
@Schema(description="Die Statistik zu den Fachwahlen eines Jahrgangs zu einem Fach der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostStatistikFachwahl {
	
	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. */
	@Schema(required = true, description = "das Jahr, in welchem der Jahrgang Abitur machen wird", example="2042")
	public int abiturjahr;

	/** Die ID des Faches, für welches die Fachwahldaten ermittelt wurden. */
	@Schema(required = true, description = "die ID des Faches", example="42")
	public long id;

	/** Das eindeutige Kürzel des Faches */
	@Schema(required = true, description = "das eindeutige Kürzel des Faches", example="M")
	public String kuerzel;
	
	/** Die Bezeichnung des Faches */
	@Schema(required = true, description = "die Bezeichnung des Faches ", example="Mathematik")
	public String bezeichnung;
	
	/** Das Statistik-Kürzel des Faches */
	@Schema(required = true, description = "das Statistik-Kürzel des Faches ", example="M")
	public String kuerzelStatistik;
	
	/** Ein Array mit den Fachwahlen der 6 Halbjahre der gymnasialen Oberstufe */
	@ArraySchema(schema = @Schema(implementation = GostStatistikFachwahlHalbjahr.class))
	public @NotNull GostStatistikFachwahlHalbjahr @NotNull[] fachwahlen = new GostStatistikFachwahlHalbjahr[6];

}
