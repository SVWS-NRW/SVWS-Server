package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beinhaltet die Daten für die FehlerartKontexte eines Validators in Bezug auf die Zuordnung
 * von schulformspezifischen Fehlerarten zu Prüfschritten
 */
@XmlRootElement
@Schema(description = "die Information in Bezug auf die Zuordnung von schulformspezifischen Fehlerarten zu PrüfschrittenHistorieneintrag im Rahmen des ValidatorFehlerartKontext")
@TranspilerDTO
public class ValidatorFehlerartKontextPruefschritt {

	/** Die Nummer des Prüfschrittes, auf welchen sich die Definition bezieht (-1 für einen Default für alle Prüfschritte eines Validators) */
	@Schema(description = "Die Nummer des Prüfschrittes, auf welchen sich die Definition bezieht (-1 für einen Default für alle Prüfschritte eines Validators)")
	public int nummer = -1;

	/** Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt")
	public @NotNull List<String> muss = new ArrayList<>();

	/** Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt")
	public @NotNull List<String> kann = new ArrayList<>();

	/** Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt")
	public @NotNull List<String> hinweis = new ArrayList<>();

}
