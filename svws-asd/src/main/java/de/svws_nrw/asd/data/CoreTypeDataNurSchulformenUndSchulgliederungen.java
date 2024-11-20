package de.svws_nrw.asd.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.schule.SchulformSchulgliederung;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Schnittstelle ist eine Erweiterung zum grundlegenden Aufbau von Core-Type-Daten
 * (siehe {@link CoreTypeData}. Mit dem Attribute {@link CoreTypeDataNurSchulformenUndSchulgliederungen#zulaessig}
 * bietet es die Möglichkeit die Gültigkeit von Core-Typ-Daten einzuschränken. Hierzu werden
 * die Bezeichner der erlaubten Schulformen in Kombination mit den Bezeichnern der erlaubten Schulgliederungen
 * in der Liste eingetragen. Wird bei einem Eintrag die Schulgliederung auf null gesetzt, so sind alle Schulgliederungen
 * erlaubt. Eine leere Liste bedeutet, dass alle möglichen Schulform- und Schulgliederungskombinationen zulässig sind.
 *
 * Anmerkung: Für die Möglichkeit die Klasse zu transpilieren, darf diese nicht als
 *   "abstract" gekennzeichnet sein.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog eines Core-Types, ggf. mit Einschränkungen in Bezug auf die Schulformen und Schulgliederung.")
@TranspilerDTO
public class CoreTypeDataNurSchulformenUndSchulgliederungen extends CoreTypeData {

	/** Die Informationen zu Schulformen und -gliederungen, wo die Core-Type-Daten zulässig sind. */
	@Schema(description = "die Informationen zu Schulformen und -gliederungen, wo die Core-Type-Daten zulässig sind.")
	public @NotNull List<SchulformSchulgliederung> zulaessig = new ArrayList<>();


	/**
	 * Leerer Standardkonstruktor.
	 */
	public CoreTypeDataNurSchulformenUndSchulgliederungen() {
		// leer
	}

}
