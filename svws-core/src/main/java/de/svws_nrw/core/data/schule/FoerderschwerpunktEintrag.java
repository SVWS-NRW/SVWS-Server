package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
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
	@Schema(description = "die ID des Katalog-Eintrags", example="4711")
	public long id;

	/** Das Kürzel des Eintrags. */
	@Schema(description = "das Kürzel des Eintrags", example="EZ")
	public @NotNull String kuerzel = "";
	
	/** Die textuelle Beschreibung des Katalog-Eintrags. */
	@Schema(description = "die textuelle Beschreibung des Katalog-Eintrags", example="Emotionale und soziale Entwicklung")
	public @NotNull String text = "";

	/** Das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik. */
	@Schema(description = "das Kürzel des Eintrags im Rahmen der amtlichen Schulstatisik", example="EZ")
	public @NotNull String kuerzelStatistik = "";	
	
	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
	
	/** Gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung änderbar sein soll oder nicht", example="true")
	public boolean istAenderbar;
	
}
