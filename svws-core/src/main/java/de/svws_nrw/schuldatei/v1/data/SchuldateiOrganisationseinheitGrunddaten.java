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
public class SchuldateiOrganisationseinheitGrunddaten {

	/** Die ID der Grunddaten. */
	@Schema(description = "die ID dieses Eintrags", example = "4711")
	public Integer id;

	/** Die Schulnummer. */
	@Schema(description = "die Schulnummer", example = "100001")
	public @NotNull Integer schulnummer = 0;

	/** Die Kurzbezeichnung der Organisationseinheit */
	@Schema(description = "die Kurzbezeichnung der Organisationseinheit", example = "Düsseldorf, MSB")
	public @NotNull String kurzbezeichnung = "";

	/** Der Rechtsstatus der Organisationseinheit 1=öffentlich, 2=privat*/
	@Schema(description = "Der Rechtsstatus der Organisationseinheit", example = "1")
	public String rechtsstatus;

    /** Schulträgernummer der Organisationseinheit */
    @Schema(description = "Schulträgernummer der Organisationseinheit")
    public String schultraegernummer;

    /** Art der Trägerschaft der Schule */
    @Schema(description = "Art der Trägerschaft des Schulträgers", example = "00")
    public String artdertraegerschaft;

    /** Betriebsschlüssel der Schule */
    @Schema(description = "Betriebsschlüssel der Schule")
    public String schulbetriebsschluessel;

    /** Kapitel der Schule */
    @Schema(description = "Kapitel der Schule")
    public String kapitel;

    /** Obere Schulaufsicht der Schule */
    @Schema(description = "Obere Schulaufsicht der Schule", example = "001")
    public String obereschulaufsicht;

    /** Untere Schulaufsicht der Schule */
    @Schema(description = "Untere Schulaufsicht der Schule")
    public String untereschulaufsicht;

    /** Zentrum für schulpraktische Lehrerausbildung ZFSL */
    @Schema(description = "Zentrum für schulpraktische Lehrerausbildung ZFSL", example = "503010")
    public String zfsl;

    /** Dienststellenschlüssel der Organisationseinheit */
    @Schema(description = "Dienststellenschlüssel der Organisationseinheit", example = "M005")
    public String dienststellenschluessel;

    /** Personalteilbereich der Organisationseinheit */
    @Schema(description = "Personalteilbereich der Organisationseinheit", example = "2160")
    public String ptb;

    /** Gibt an ob die Schule Internatsbetrieb hat */
    @Schema(description = "Gibt die Art des Internatbetriebs an")
    public String internatsbetrieb;

    /** Anzahl der Internatsplätze */
    @Schema(description = "Anzahl der Internatsplätze")
    public Integer internatsplaetze;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public String gueltigab;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public String gueltigbis;

    /** Gibt das Änderungsdatum des Eintrags an*/
    @Schema(description = "Gibt das Änderungsdatum des Eintrags an")
    public String geaendertam;

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
