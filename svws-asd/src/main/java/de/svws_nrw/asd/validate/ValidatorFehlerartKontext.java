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

	/** Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt ein Fehler vorliegt")
	public @NotNull List<String> muss = new ArrayList<>();

	/** Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt wahrscheinlich ein Fehler vorliegt")
	public @NotNull List<String> kann = new ArrayList<>();

	/** Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt */
	@Schema(description = "Liste der Schulformen, in denen bei dem Prüfschritt ein Hinweis auf einen möglichen Fehler erfolgt")
	public @NotNull List<String> hinweis = new ArrayList<>();

	/** Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt. */
	@Schema(description = "gibt an, ab welchem Schuljahr der Fehlerart-Kontext des Validators gilt. Falls schon immer, so ist null gesetzt.", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, ab welchem Schuljahr die Laufeigenschaft des Validators gilt. Falls schon immer, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr der Fehlerart-Kontext des Validators gilt. Falls für immer, so ist null gesetzt.",
			example = "null")
	public Integer gueltigBis = null;
	/** Das alte Kürzel des Prüfschritts */
	@Schema(description = "das alte Kürzel des Prüfschritts")
	public String altes_kuerzel = "";

	/** Der Zweig/Bereich der Prüfung */
	@Schema(description = "der Zweig bzw. Bereich der Prüfung")
	public String zweig = "";

	/** Die beteiligten DTOs */
	@Schema(description = "die an der Prüfung beteiligten DTOs")
	public String dtos = "";

	/** UI-Bereich für die Ausführung */
	@Schema(description = "der Bereich in der Benutzeroberfläche, in dem die Prüfung ausgeführt wird")
	public String ausfuehrungsbereich_ui = "";

	/** UI-Bereich für die Anzeige */
	@Schema(description = "der Bereich in der Benutzeroberfläche, in dem das Ergebnis der Prüfung angezeigt wird")
	public String anzeigebereich_ui = "";

	/** Der Fehlertext */
	@Schema(description = "der Text der Fehlermeldung")
	public String text = "";

	/** Zusätzliche Erläuterungen */
	@Schema(description = "zusätzliche Erläuterungen zum Prüfschritt oder zum Fehler")
	public String erlaeuterung = "";

	/** Die fachliche Bedingung als String */
	@Schema(description = "die fachliche Bedingung der Prüfung in Textform")
	public String bedingung = "";

	/** Vorbedingungen für die Prüfung */
	@Schema(description = "eine Liste der Vorbedingungen, die für die Ausführung des Prüfschritts erfüllt sein müssen")
	public @NotNull List<String> vorbedingung = new ArrayList<>();

	/**
	 * Erstellt einen ValidatorFehlerartKontext mit Standardwerten
	 */
	public ValidatorFehlerartKontext() {
		// nichts zu tun
	}

}
