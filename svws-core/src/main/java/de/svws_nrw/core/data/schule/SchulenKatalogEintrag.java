package de.svws_nrw.core.data.schule;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt die Daten eines Eintrages im Katalog der Schulen.
 */
@XmlRootElement
@Schema(description="ein Eintrag in dem Katalog der Schulen.")
@TranspilerDTO
public class SchulenKatalogEintrag {

	/** Die Schulnummer. */
	@Schema(required = true, description = "die Schulnummer", example="100001")
	public @NotNull String SchulNr = "";
	
    /** Regionalschlüssel der Schule */
    @Schema(required = true, description = "Regionalschlüssel der Schule")
    public String RegSchl;

    /** Feld KoRe */
    @Schema(required = true, description = "Feld KoRe")
    public double KoRe;

    /** Feld KoHo */
    @Schema(required = true, description = "Feld KoHo")
    public double KoHo;

    /** Bezeichnung 1 der Schule */
    @Schema(required = true, description = "Bezeichnung 1 der Schule")
    public String ABez1;

    /** Bezeichnung 2 der Schule */
    @Schema(required = true, description = "Bezeichnung 2 der Schule")
    public String ABez2;

    /** Bezeichnung 3 der Schule */
    @Schema(required = true, description = "Bezeichnung 3 der Schule")
    public String ABez3;

    /** Postleitzahl der Schule */
    @Schema(required = true, description = "Postleitzahl der Schule")
    public String PLZ;

    /** Ort der Schule */
    @Schema(required = true, description = "Ort der Schule")
    public String Ort;

    /** Straße der Schule */
    @Schema(required = true, description = "Straße der Schule")
    public String Strasse;

    /** Telefonvorwahl der Schule */
    @Schema(required = true, description = "Telefonvorwahl der Schule")
    public String TelVorw;

    /** Telefonnummer der Schule */
    @Schema(required = true, description = "Telefonnummer der Schule")
    public String Telefon;

    /** Faxvorwahl der Schule */
    @Schema(required = true, description = "Faxvorwahl der Schule")
    public String FaxVorw;

    /** Faxnummer der Schule */
    @Schema(required = true, description = "Faxnummer der Schule")
    public String Fax;

    /** Modemvorwahl der Schule */
    @Schema(required = true, description = "Modemvorwahl der Schule")
    public String ModemVorw;

    /** Modem-Telefonnummer der Schule */
    @Schema(required = true, description = "Modem-Telefonnummer der Schule")
    public String Modem;

    /** Schulform der Schule */
    @Schema(required = true, description = "Schulform der Schule")
    public String SF;

    /** OeffPri */
    @Schema(required = true, description = "OeffPri")
    public String OeffPri;

    /** Kurzbezeichnung der Schule */
    @Schema(required = true, description = "Kurzbezeichnung der Schule")
    public String KurzBez;

    /** Schulbetriebsschlüssel der Schule */
    @Schema(required = true, description = "Schulbetriebsschlüssel der Schule")
    public Integer SchBetrSchl;

    /** Datum des Schulbetriensschlüssels der Schule */
    @Schema(required = true, description = "Datum des Schulbetriensschlüssels der Schule")
    public String SchBetrSchlDatum;

    /** Art der Trägerschaft der Schule */
    @Schema(required = true, description = "Art der Trägerschaft der Schule")
    public String ArtDerTraegerschaft;

    /** Schulträgernummer der Schule */
    @Schema(required = true, description = "Schulträgernummer der Schule")
    public String SchultraegerNr;

    /** Schulgliederung der Schule */
    @Schema(required = true, description = "Schulgliederung der Schule")
    public String Schulgliederung;

    /** Schulart */
    @Schema(required = true, description = "Schulart")
    public String Schulart;

    /** Gibt an ob die Schule Ganztagsbetrieb hat */
    @Schema(required = true, description = "Gibt an ob die Schule Ganztagsbetrieb hat")
    public String Ganztagsbetrieb;

    /** Förderschwerpunkte der Schule */
    @Schema(required = true, description = "Förderschwerpunkte der Schule")
    public String FSP;

    /** Verbund */
    @Schema(required = true, description = "Verbund")
    public String Verbund;

    /** Bus */
    @Schema(required = true, description = "Bus")
    public String Bus;

    /** Fachberater der Schule */
    @Schema(required = true, description = "Fachberater der Schule")
    public Integer Fachberater;

    /** FachberHauptamtl */
    @Schema(required = true, description = "FachberHauptamtl")
    public Integer FachberHauptamtl;

    /** TelNrDBSalt */
    @Schema(required = true, description = "TelNrDBSalt")
    public String TelNrDBSalt;

    /** RP */
    @Schema(required = true, description = "RP")
    public String RP;

    /** Email-Adresse der Schule */
    @Schema(required = true, description = "Email-Adresse der Schule")
    public String Email;

    /** Website der Schule */
    @Schema(required = true, description = "Website der Schule")
    public String URL;

    /** Bemerkung zur Schule */
    @Schema(required = true, description = "Bemerkung zur Schule")
    public String Bemerkung;

    /** Gibt an ob die Schule eine CD für ASDPC32 möchte */
    @Schema(required = true, description = "Gibt an ob die Schule eine CD für ASDPC32 möchte")
    public Integer CD;

    /** Stift */
    @Schema(required = true, description = "Stift")
    public Integer Stift;

    /** Gibt an ob die Schule offenen Ganztag hat */
    @Schema(required = true, description = "Gibt an ob die Schule offenen Ganztag hat")
    public String OGTS;

    /** SELB */
    @Schema(required = true, description = "SELB")
    public String SELB;

    /** Gibt an ob die Schule Internatsplätze hat */
    @Schema(required = true, description = "Gibt an ob die Schule Internatsplätze hat")
    public String Internat;

    /** Anzahl der Internatsplätze */
    @Schema(required = true, description = "Anzahl der Internatsplätze")
    public Integer InternatPlaetze;

    /** Schulmailadresse */
    @Schema(required = true, description = "Schulmailadresse")
    public String SMail;

    /** Hat die Schule Sport im Abitur? */
    @Schema(required = true, description = "Hat die Schule Sport im Abitur?")
    public String SportImAbi;

    /** Nimmt die Schule am Projekt Talentschule teil? */
    @Schema(required = true, description = "Nimmt die Schule am Projekt Talentschule teil?")
    public String Tal;

    /** Ist die konfessionelle Kooperation an dieser Schule genehmigt? */
    @Schema(required = true, description = "Ist die konfessionelle Kooperation an dieser Schule genehmigt?")
    public String KonKop;

    /** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(required = true, description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public Integer gueltigVon;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(required = true, description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public Integer gueltigBis;

}
