package de.svws_nrw.schuldatei.v1.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt eine Adresse einer Organisationseinheit
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "die Grunddaten einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitAdresse {

	/** Die ID des Adress-Eintrags. */
	@Schema(description = "die ID des Adress-Eintrags", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Nummer der Liegenschaft der Organisationseinheit */
	@Schema(description = "Die Nummer der Liegenschaft der Organisationseinheit", example = "1")
	public @NotNull Integer liegenschaft = 0;

    /** Straße der Adresse der Organisationseinheit */
    @Schema(description = "Straße der Adresse der Organisationseinheit")
    public String strasse;

    /** Postleitzahl der Schule */
    @Schema(description = "Postleitzahl der Adresse der Organisationseinheit")
    public String postleitzahl;

    /** Ort der Schule */
    @Schema(description = "Ort der Adresse der Organisationseinheit")
    public String ort;

    /** Regionalschlüssel der Schule */
    @Schema(description = "Regionalschlüssel der Adresse der Organisationseinheit")
    public String regionalschluessel;

    /** Qualität der Verortung */
    @Schema(description = "Qualität der Verortung")
    public Long qualitaetverortung;

    /** Koordinatenrechtswert der Adresse */
    @Schema(description = "Koordinatenrechtswert der Adresse")
    public Long koordinaterechtswert;

    /** Koordinatenhochwert der Adresse */
    @Schema(description = "Koordinatenhochwert der Adresse")
    public Long koordinatehochwert;

	/** Der Adresstyp */
	@Schema(description = "Adresstypid der Adresse", example = "1")
	public String  adresstypeid;

	/** Das Standortkennzeichen */
	@Schema(description = "Standortkennzeichen des Teilstandorts", example = "01")
	public String standortkennzeichen;

	/** Das Adresskennzeichnen des Teilstandorts (ein Buchstabe) */
	@Schema(description = "Adresskennzeichen des Teilstandors", example = "A")
	public @NotNull String adresskennzeichen = "";

	/** Hauptstandortadresse */
	@Schema(description = "Hauptstandortadresse (bisher nicht beleget)", example = "")
	public @NotNull String hauptstandortadresse = "";

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public String gueltigab;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public String gueltigbis;

    /** Gibt das Änderungsdatum des Eintrags an*/
    @Schema(description = "Gibt das Änderungsdatum des Eintrags an")
    public String geaendertam;


    /**
     * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
     */
    public SchuldateiOrganisationseinheitAdresse() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
