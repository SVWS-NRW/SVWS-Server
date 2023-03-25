package de.svws_nrw.core.data.abschluss;

import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt einen Service für die Abschlussberechnung an einer
 * berufsbildenden Schule zur Verfügung.
 */
@XmlRootElement
@Schema(description="gibt die Informationen zu dem Ergebnis der Abschlussberechnung an.")
@TranspilerDTO
public class AbschlussErgebnisBerufsbildend {

	/** Gibt an, ob der Berufschulabschluss erfolgreich erworben wurde. */
	@Schema(required = true, description = "gibt an, ob der Berufschulabschluss erfolgreich erworben wurde", example="true")
    public boolean hatBSA;

	/** Gibt die Note des Berufsschulabschlusses an, selbst wenn dieser nicht erreicht wurde. */
	@Schema(required = true, description = "gibt die Note des Berufsschulabschlusses an, selbst wenn dieser nicht ereicht wurde", example="true")
	public double note;

	/** Gibt an, ob der Berufabschluss insgesamt erfolgreich erworben wurde, falls genügend Informationen vorliegen. */
	@Schema(required = false, description = "gibt an, ob der Berufabschluss insgesamt erfolgreich erworben wurde, falls genügend Informationen vorliegen", example="true")
	public Boolean hatBA;

	/** Gibt an, welcher allgemeinbildende Abschluss ggf. zusätzlich erreicht wurde, falls er nicht bereits vorher erreicht wurde. */
	@Schema(required = true, description = "gibt an, welcher allgemeinbildende Abschluss ggf. zusätzlich erreicht wurde, falls er nicht bereits vorher erreicht wurde", example="MSA-Q")
	public String abschlussAllgemeinbildend;
	
	/** Der Log der Abschlussberechnung. */
	@ArraySchema(schema = @Schema(required = false, description = "der Log der Abschlussberechnung", example="Ein Log, der die Entscheidungen bei der Prüfung des Abschlusses verdeutlicht"))
	public List<@NotNull String> log;
    
}
