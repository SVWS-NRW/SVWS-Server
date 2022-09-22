package de.nrw.schule.svws.core.data.abschluss;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse enthält das Ergebnis einer Abschlussberechnung für einen
 * allgemeinbildenden Abschluss.
 */
@XmlRootElement
@Schema(description="gibt die Informationen zu dem Ergebnis der Abschlussberechnung an.")
@TranspilerDTO
public class AbschlussErgebnis {

	/** Gibt an, ob der Abschluss erfolgreich erworben wurde, bzw. bei einer Prognose, ob ein ein Abschluss erworben wurde. */
	@Schema(required = true, description = "gibt an, ob der Abschluss erfolgreich erworben wurde, bzw. bei einer Prognose, ob ein ein Abschluss vermutlich erworben wird.", example="true")
    public boolean erworben = false;

	/** Gibt an, welcher Abschluss geprüft wurde. */
	@Schema(required = true, description = "gibt an, welcher Abschluss geprüft wurde.", example="MSA-Q")
	public String abschluss = null;
	
	/** Eine Liste der Kuerzel für mögliche Nachprüfungsfächer. */
	@ArraySchema(schema = @Schema(required = true, description = "eine Liste der Kuerzel für mögliche Nachprüfungsfächer.", example="M"))
	public List<@NotNull String> npFaecher = null;
	
	/** Der Log der Abschlussberechnung. */
	@ArraySchema(schema = @Schema(required = false, description = "der Log der Abschlussberechnung.", example="Ein Log, der die Entscheidungen bei der Prüfung des Abschlusses verdeutlicht"))
	public List<@NotNull String> log = null;
    
}
