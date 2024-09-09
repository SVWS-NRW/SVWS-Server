package de.svws_nrw.asd.validate;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beinhaltet die Daten für die FehlerartKontexte eines Validators für einen Zeitraum
 */
@XmlRootElement
@Schema(description = "ein Historieneintrag der ValidatorFehlerartKontext (Umgebung, Fehlerart=f(Schulform))")
@TranspilerDTO
public class ValidatorFehlerartKontext {

	/** ob der Validator im zebras ausgeführt werden soll. */
	@Schema(description = "ob der Validator in Zebras ausgeführt werden soll")
	public boolean zebras;

	/** ob der Validator im client ausgeführt werden soll. */
	@Schema(description = "ob der Validator in Client ausgeführt werden soll")
	public boolean svws;

	/** Liste der Schulformen, in denen ein harter Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen ein harter Fehler vorliegt")
	public @NotNull List<String> hart = new ArrayList<>();

	/** Liste der Schulformen, in denen ein harter Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen ein Muss-Fehler vorliegt")
	public @NotNull List<String> muss = new ArrayList<>();

	/** Liste der Schulformen, in denen ein harter Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen ein Hinweis erfolgt")
	public @NotNull List<String> hinweis = new ArrayList<>();

	/** Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt. */
	@Schema(description = "gibt an, ab welchem Schuljahr der Fehlerart-Kontext des Validators gilt. Falls schon immer, so ist null gesetzt.", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Fehlerart-Kontext des Validators gilt. Falls für immer, so ist null gesetzt.", example = "null")
	public Integer gueltigBis = null;

	/**
	 * Erstellt einen ValidatorFehlerartKontext mit Standardwerten
	 */
	public ValidatorFehlerartKontext() {
	}

}
