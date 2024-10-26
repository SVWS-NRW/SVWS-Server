package de.svws_nrw.core.data.abschluss;

import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Die Klasse enthält das Ergebnis einer Abschlussberechnung für einen
 * allgemeinbildenden Abschluss.
 */
@XmlRootElement
@Schema(description = "gibt die Informationen zu dem Ergebnis der Abschlussberechnung an.")
@TranspilerDTO
public class AbschlussErgebnis {
	/** Gibt an, ob der Abschluss erfolgreich erworben wurde, bzw. bei einer Prognose, ob ein ein Abschluss erworben wurde. */
	@Schema(description = "gibt an, ob der Abschluss erfolgreich erworben wurde, bzw. bei einer Prognose, ob ein ein Abschluss vermutlich erworben wird.",
			example = "true")
	public boolean erworben = false;

	/** Gibt an, welcher Abschluss geprüft wurde. */
	@Schema(description = "gibt an, welcher Abschluss geprüft wurde.", example = "MSA-Q")
	public String abschluss = null;

	/** Eine Liste der Kuerzel für mögliche Nachprüfungsfächer. */
	@ArraySchema(schema = @Schema(description = "eine Liste der Kuerzel für mögliche Nachprüfungsfächer.", example = "M"))
	public List<String> npFaecher = null;

	/** Der Log der Abschlussberechnung. */
	@ArraySchema(schema = @Schema(description = "der Log der Abschlussberechnung.",
			example = "Ein Log, der die Entscheidungen bei der Prüfung des Abschlusses verdeutlicht"))
	public List<String> log = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public AbschlussErgebnis() {
		// leer
	}

}
