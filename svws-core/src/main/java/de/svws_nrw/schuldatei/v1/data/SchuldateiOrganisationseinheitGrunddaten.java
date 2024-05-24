package de.svws_nrw.schuldatei.v1.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt die grundlegenden Daten einer Organisationseinheit
 * der Schuldatei. Hier wird nur ein Standort der Organisationseinheit
 * gespeichert, i.A. der Hauptstandort
 */
@XmlRootElement
@Schema(description = "die Grunddaten einer Organisationseinheit der Schuldatei.")
@TranspilerDTO
public class SchuldateiOrganisationseinheitGrunddaten extends SchuldateiEintrag {

	/** Die ID der Grunddaten. */
	@Schema(description = "die ID dieses Eintrags", example = "4711")
	public Integer id = null;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public int schulnummer = 0;

	/** Die Kurzbezeichnung der Organisationseinheit */
	@Schema(description = "die Kurzbezeichnung der Organisationseinheit", example = "Düsseldorf, MSB")
	public @NotNull String kurzbezeichnung = "";

	/** Der Rechtsstatus der Organisationseinheit 1=öffentlich, 2=privat*/
	@Schema(description = "Der Rechtsstatus der Organisationseinheit", example = "1")
	public int rechtsstatus;

    /** Schulträgernummer der Organisationseinheit */
    @Schema(description = "Schulträgernummer der Organisationseinheit")
    public int schultraegernummer = 0;

    /** Art der Trägerschaft der Schule */
    @Schema(description = "Art der Trägerschaft des Schulträgers", example = "00")
    public int artdertraegerschaft = 0;

    /** Betriebsschlüssel der Schule */
    @Schema(description = "Betriebsschlüssel der Schule")
    public int schulbetriebsschluessel = 0;

    /** Kapitel der Schule */
    @Schema(description = "Kapitel der Schule")
    public int kapitel = 0;

    /** Obere Schulaufsicht der Schule */
    @Schema(description = "Obere Schulaufsicht der Schule", example = "001")
    public int obereschulaufsicht = 0;

    /** Untere Schulaufsicht der Schule */
    @Schema(description = "Untere Schulaufsicht der Schule")
    public int untereschulaufsicht = 0;

    /** Zentrum für schulpraktische Lehrerausbildung ZFSL */
    @Schema(description = "Zentrum für schulpraktische Lehrerausbildung ZFSL", example = "503010")
    public int zfsl = 0;

    /** Dienststellenschlüssel bzw. Personalbereich der Organisationseinheit */
    @Schema(description = "Dienststellenschlüssel (Personalbereich) der Organisationseinheit", example = "M005")
    public int dienststellenschluessel = 0;

    /** Personalteilbereich der Organisationseinheit */
    @Schema(description = "Personalteilbereich der Organisationseinheit", example = "2160")
    public String ptb = null;

    /** Gibt an ob die Schule Internatsbetrieb hat */
    @Schema(description = "Gibt die Art des Internatbetriebs an")
    public String internatsbetrieb = null;

    /** Anzahl der Internatsplätze */
    @Schema(description = "Anzahl der Internatsplätze")
    public Integer internatsplaetze = null;

	/** Die Schulformen der Organisationseinheit:Schule (zeitl. Verlaufsliste)*/
	@ArraySchema(schema = @Schema(implementation = SchuldateiOrganisationseinheitSchulform.class))
	public final @NotNull List<@NotNull SchuldateiOrganisationseinheitSchulform> schulform = new ArrayList<>();


    /**
     * Erstellt neue Grunddaten für eine Organiationseinheit der Schuldatei
     */
    public SchuldateiOrganisationseinheitGrunddaten() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
