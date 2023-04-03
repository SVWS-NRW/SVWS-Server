package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt die Daten eines Eintrages im Katalog der Schulträger.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schulträger.")
@TranspilerDTO
public class SchultraegerKatalogEintrag {

	/** Schulträgernummer des Schulträgers. */
	@Schema(description = "Schulträgernummer des Schulträgers")
	public @NotNull String SchulNr = "";
	

    /** Regionalschlüssel des Schulträgers */
    @Schema(description = "Regionalschlüssel des Schulträgers")
    public String RegSchl;

    /** KoRe */
    @Schema(description = "KoRe")
    public String KoRe;

    /** KoHo */
    @Schema(description = "KoHo")
    public String KoHo;

    /** Bezeichnung 1 des Schulträgers */
    @Schema(description = "Bezeichnung 1 des Schulträgers")
    public String ABez1;

    /** Bezeichnung 2 des Schulträgers */
    @Schema(description = "Bezeichnung 2 des Schulträgers")
    public String ABez2;

    /** Bezeichnung 3 des Schulträgers */
    @Schema(description = "Bezeichnung 3 des Schulträgers")
    public String ABez3;

    /** PLZ des Schulträgers */
    @Schema(description = "PLZ des Schulträgers")
    public String PLZ;

    /** Ort des Schulträgers */
    @Schema(description = "Ort des Schulträgers")
    public String Ort;

    /** Straße des Schulträgers */
    @Schema(description = "Straße des Schulträgers")
    public String Strasse;

    /** Vorwahl des Schulträgers */
    @Schema(description = "Vorwahl des Schulträgers")
    public String TelVorw;

    /** Telefonnummer des Schulträgers */
    @Schema(description = "Telefonnummer des Schulträgers")
    public String Telefon;

    /** Ist immer 00 ??? */
    @Schema(description = "Ist immer 00 ???")
    public String SF;

    /** Öffentlicher oder privater Schulträger */
    @Schema(description = "Öffentlicher oder privater Schulträger")
    public String OeffPri;

    /** Kurzbezeichnung des Schulträgers */
    @Schema(description = "Kurzbezeichnung des Schulträgers")
    public String KurzBez;

    /** Schulbetriebsschlüssel des Schulträgers */
    @Schema(description = "Schulbetriebsschlüssel des Schulträgers")
    public Integer SchBetrSchl;

    /** Datum des Schulbetriebsschlüssels */
    @Schema(description = "Datum des Schulbetriebsschlüssels")
    public String SchBetrSchlDatum;

    /** Schülerzahl laut ASD */
    @Schema(description = "Schülerzahl laut ASD")
    public Integer SchuelerZahlASD;

    /** Schülerzahl laut VS */
    @Schema(description = "Schülerzahl laut VS")
    public Integer SchuelerZahlVS;

    /** Art der Trägerschaft des Schulträgers */
    @Schema(description = "Art der Trägerschaft des Schulträgers")
    public String ArtDerTraegerschaft;

    /** leer siehe SchulNr */
    @Schema(description = "leer siehe SchulNr")
    public String SchultraegerNr;

    /** leer Gliederung */
    @Schema(description = "leer Gliederung")
    public String Schulgliederung;

    /** Leer Ganztagsbetrieb */
    @Schema(description = "Leer Ganztagsbetrieb")
    public String Ganztagsbetrieb;

    /** Aktiv ja nein des Schulträgers */
    @Schema(description = "Aktiv ja nein des Schulträgers")
    public Integer Aktiv;

    /** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public Integer gueltigVon;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public Integer gueltigBis;

}
