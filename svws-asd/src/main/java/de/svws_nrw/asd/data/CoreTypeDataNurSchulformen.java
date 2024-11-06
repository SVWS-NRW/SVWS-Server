package de.svws_nrw.asd.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Schnittstelle ist eine Erweiterung zum grundlegenden Aufbau von Core-Type-Daten
 * (siehe {@link CoreTypeData}. Mit dem Attribute {@link CoreTypeDataNurSchulformen#schulformen}
 * bietet es die Möglichkeit die Gültigkeit von Core-Typ-Daten einzuschränken. Hierzu können
 * die Bezeichner der erlaubten Schulformen in der Liste ergänzt werden. Eine leere Liste bedeutet
 * keine Einschränkung, d.h. die Core-Type-Daten sind für alle Schulformen zulässig.
 *
 * Anmerkung: Für die Möglichkeit die Klasse zu transpilieren, darf diese nicht als
 *   "abstract" gekennzeichnet sein.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog eines Core-Types, ggf. mit Einscränkungen in Bezug auf die Schulformen.")
@TranspilerDTO
public class CoreTypeDataNurSchulformen extends CoreTypeData {

	/** Die Kürzel der Schulformen, bei welchen der Jahrgang vorkommt. */
	@Schema(description = "die Kürzel der Schulformen, bei welchen der Jahrgang vorkommt")
	public @NotNull List<String> schulformen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public CoreTypeDataNurSchulformen() {
		// leer
	}

}
