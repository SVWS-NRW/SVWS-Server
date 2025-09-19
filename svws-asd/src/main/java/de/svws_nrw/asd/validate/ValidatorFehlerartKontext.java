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

	/** Gibt an, ob der Validator im zebras ausgeführt werden soll. */
	@Schema(description = "gibt an, ob der Validator in Zebras ausgeführt werden soll")
	public boolean zebras = false;

	/** Gibt an, ob der Validator im client ausgeführt werden soll. */
	@Schema(description = "gibt an, ob der Validator in SVWS-Client ausgeführt werden soll")
	public boolean svws = false;

	/** der Präfix-Teil des ASD-Fehlercodes */
	@Schema(description = "der Präfix-Teil des ASD-Fehlercodes")
	public @NotNull String praefix = "";

	/** Die Liste mit den Zuordnungen der Fehlerarten für die einzelnen Prüfschritte eines Validators */
	@Schema(description = "die Liste mit den Zuordnungen der Fehlerarten für die einzelnen Prüfschritte eines Validators")
	public @NotNull List<ValidatorFehlerartKontextPruefschritt> pruefschritte = new ArrayList<>();

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
		// nichts zu tun
	}

}
