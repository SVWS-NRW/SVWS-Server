package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt die Daten eines Eintrages im Katalog der Schulträger.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Schulträger.")
@TranspilerDTO
public class SchultraegerKatalogEintrag {

	/** Schulträgernummer des Schulträgers. */
	@Schema(required = true, description = "Schulträgernummer des Schulträgers")
	public @NotNull String SchulNr = "";
	

    /** Regionalschlüssel des Schulträgers */
    @Schema(required = true, description = "Regionalschlüssel des Schulträgers")
    public String RegSchl;

    /** KoRe */
    @Schema(required = true, description = "KoRe")
    public String KoRe;

    /** KoHo */
    @Schema(required = true, description = "KoHo")
    public String KoHo;

    /** Bezeichnung 1 des Schulträgers */
    @Schema(required = true, description = "Bezeichnung 1 des Schulträgers")
    public String ABez1;

    /** Bezeichnung 2 des Schulträgers */
    @Schema(required = true, description = "Bezeichnung 2 des Schulträgers")
    public String ABez2;

    /** Bezeichnung 3 des Schulträgers */
    @Schema(required = true, description = "Bezeichnung 3 des Schulträgers")
    public String ABez3;

    /** PLZ des Schulträgers */
    @Schema(required = true, description = "PLZ des Schulträgers")
    public String PLZ;

    /** Ort des Schulträgers */
    @Schema(required = true, description = "Ort des Schulträgers")
    public String Ort;

    /** Straße des Schulträgers */
    @Schema(required = true, description = "Straße des Schulträgers")
    public String Strasse;

    /** Vorwahl des Schulträgers */
    @Schema(required = true, description = "Vorwahl des Schulträgers")
    public String TelVorw;

    /** Telefonnummer des Schulträgers */
    @Schema(required = true, description = "Telefonnummer des Schulträgers")
    public String Telefon;

    /** Ist immer 00 ??? */
    @Schema(required = true, description = "Ist immer 00 ???")
    public String SF;

    /** Öffentlicher oder privater Schulträger */
    @Schema(required = true, description = "Öffentlicher oder privater Schulträger")
    public String OeffPri;

    /** Kurzbezeichnung des Schulträgers */
    @Schema(required = true, description = "Kurzbezeichnung des Schulträgers")
    public String KurzBez;

    /** Schulbetriebsschlüssel des Schulträgers */
    @Schema(required = true, description = "Schulbetriebsschlüssel des Schulträgers")
    public Integer SchBetrSchl;

    /** Datum des Schulbetriebsschlüssels */
    @Schema(required = true, description = "Datum des Schulbetriebsschlüssels")
    public String SchBetrSchlDatum;

    /** Schülerzahl laut ASD */
    @Schema(required = true, description = "Schülerzahl laut ASD")
    public Integer SchuelerZahlASD;

    /** Schülerzahl laut VS */
    @Schema(required = true, description = "Schülerzahl laut VS")
    public Integer SchuelerZahlVS;

    /** Art der Trägerschaft des Schulträgers */
    @Schema(required = true, description = "Art der Trägerschaft des Schulträgers")
    public String ArtDerTraegerschaft;

    /** leer siehe SchulNr */
    @Schema(required = true, description = "leer siehe SchulNr")
    public String SchultraegerNr;

    /** leer Gliederung */
    @Schema(required = true, description = "leer Gliederung")
    public String Schulgliederung;

    /** Leer Ganztagsbetrieb */
    @Schema(required = true, description = "Leer Ganztagsbetrieb")
    public String Ganztagsbetrieb;

    /** Aktiv ja nein des Schulträgers */
    @Schema(required = true, description = "Aktiv ja nein des Schulträgers")
    public Integer Aktiv;

    /** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(required = true, description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public Integer gueltigVon;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(required = true, description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public Integer gueltigBis;

}
