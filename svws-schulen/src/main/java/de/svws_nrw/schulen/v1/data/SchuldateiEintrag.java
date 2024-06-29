package de.svws_nrw.schulen.v1.data;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt den grundlegenden Aufbau von Einträgen in der
 * Schuldatei
 */
@XmlRootElement
@Schema(description = "ein Eintrag der Schuldatei.")
@TranspilerDTO
public class SchuldateiEintrag {

	/** Gibt an, ab wann der Eintrag gültig ist */
	@Schema(description = "gibt an, ab wann der Eintrag gültig ist")
	public String gueltigab;

	/** Gibt an, bis wann der Eintrag gültig ist */
	@Schema(description = "gibt an, bis wann der Eintrag gültig ist")
	public String gueltigbis;

	/** Das Änderungsdatum der letzten Änderung des Eintrags an*/
	@Schema(description = "das Änderungsdatum der letzten Änderung des Eintrags")
	public String geaendertam;


	/**
	 * Erstellt einen neuen Eintrag der Schuldatei
	 */
	public SchuldateiEintrag() {
		// Die Initialisierung mit Defaults erfolgt direkt über die Attribute
	}

}
