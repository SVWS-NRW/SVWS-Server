package de.svws_nrw.asd.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt die Struktur der Statistikdaten, welche von einer
 * Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden.
 */
@XmlRootElement
@Schema(description = "Die gesamten Statistikdaten der Schule, welche von einer Schule bei der Erfassung der amtlichen Schulstatistik übertragen werden")
@TranspilerDTO
public class SchuleStatistikdatenGesamt {

	/** Die Stammdaten der Schule */
	@Schema(description = "die Stammdaten der Schule")
	public @NotNull SchuleStammdaten stammdaten = new SchuleStammdaten();

	// TODO die weiteren Attribute nach und nach ergänzen...

}
