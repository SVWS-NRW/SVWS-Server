package de.nrw.schule.svws.core.data.schule;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten den schulspezifischen Förderschwerpunkt-Katalog übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem schulspezifischen Förderschwerpunkt-Katalog.")
@TranspilerDTO
public class FoerderschwerpunktEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(required = true, description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel des Eintrags. */
	@Schema(required = true, description = "das Kürzel des Eintrags", example="EZ")
	public @NotNull String kuerzel = "";
	
	/** Die textuelle Beschreibung des Katalog-Eintrags. */
	@Schema(required = true, description = "die textuelle Beschreibung des Katalog-Eintrags", example="Emotionale und soziale Entwicklung")
	public @NotNull String text = "";

	/** Das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik. */
	@Schema(required = true, description = "das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik", example="EZ")
	public @NotNull String kuerzelStatistik = "";	
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example="true")
	public boolean istAenderbar;
	
}
