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
	@Schema(description = "die Schulnummer", example="100001")
	public @NotNull String SchulNr = "";
	
    /** Regionalschlüssel der Schule */
    @Schema(description = "Regionalschlüssel der Schule")
    public String RegSchl;

    /** Feld KoRe */
    @Schema(description = "Feld KoRe")
    public double KoRe;

    /** Feld KoHo */
    @Schema(description = "Feld KoHo")
    public double KoHo;

    /** Bezeichnung 1 der Schule */
    @Schema(description = "Bezeichnung 1 der Schule")
    public String ABez1;

    /** Bezeichnung 2 der Schule */
    @Schema(description = "Bezeichnung 2 der Schule")
    public String ABez2;

    /** Bezeichnung 3 der Schule */
    @Schema(description = "Bezeichnung 3 der Schule")
    public String ABez3;

    /** Postleitzahl der Schule */
    @Schema(description = "Postleitzahl der Schule")
    public String PLZ;

    /** Ort der Schule */
    @Schema(description = "Ort der Schule")
    public String Ort;

    /** Straße der Schule */
    @Schema(description = "Straße der Schule")
    public String Strasse;

    /** Telefonvorwahl der Schule */
    @Schema(description = "Telefonvorwahl der Schule")
    public String TelVorw;

    /** Telefonnummer der Schule */
    @Schema(description = "Telefonnummer der Schule")
    public String Telefon;

    /** Faxvorwahl der Schule */
    @Schema(description = "Faxvorwahl der Schule")
    public String FaxVorw;

    /** Faxnummer der Schule */
    @Schema(description = "Faxnummer der Schule")
    public String Fax;

    /** Modemvorwahl der Schule */
    @Schema(description = "Modemvorwahl der Schule")
    public String ModemVorw;

    /** Modem-Telefonnummer der Schule */
    @Schema(description = "Modem-Telefonnummer der Schule")
    public String Modem;

    /** Schulform der Schule */
    @Schema(description = "Schulform der Schule")
    public String SF;

    /** OeffPri */
    @Schema(description = "OeffPri")
    public String OeffPri;

    /** Kurzbezeichnung der Schule */
    @Schema(description = "Kurzbezeichnung der Schule")
    public String KurzBez;

    /** Schulbetriebsschlüssel der Schule */
    @Schema(description = "Schulbetriebsschlüssel der Schule")
    public Integer SchBetrSchl;

    /** Datum des Schulbetriensschlüssels der Schule */
    @Schema(description = "Datum des Schulbetriensschlüssels der Schule")
    public String SchBetrSchlDatum;

    /** Art der Trägerschaft der Schule */
    @Schema(description = "Art der Trägerschaft der Schule")
    public String ArtDerTraegerschaft;

    /** Schulträgernummer der Schule */
    @Schema(description = "Schulträgernummer der Schule")
    public String SchultraegerNr;

    /** Schulgliederung der Schule */
    @Schema(description = "Schulgliederung der Schule")
    public String Schulgliederung;

    /** Schulart */
    @Schema(description = "Schulart")
    public String Schulart;

    /** Gibt an ob die Schule Ganztagsbetrieb hat */
    @Schema(description = "Gibt an ob die Schule Ganztagsbetrieb hat")
    public String Ganztagsbetrieb;

    /** Förderschwerpunkte der Schule */
    @Schema(description = "Förderschwerpunkte der Schule")
    public String FSP;

    /** Verbund */
    @Schema(description = "Verbund")
    public String Verbund;

    /** Bus */
    @Schema(description = "Bus")
    public String Bus;

    /** Fachberater der Schule */
    @Schema(description = "Fachberater der Schule")
    public Integer Fachberater;

    /** FachberHauptamtl */
    @Schema(description = "FachberHauptamtl")
    public Integer FachberHauptamtl;

    /** TelNrDBSalt */
    @Schema(description = "TelNrDBSalt")
    public String TelNrDBSalt;

    /** RP */
    @Schema(description = "RP")
    public String RP;

    /** Email-Adresse der Schule */
    @Schema(description = "Email-Adresse der Schule")
    public String Email;

    /** Website der Schule */
    @Schema(description = "Website der Schule")
    public String URL;

    /** Bemerkung zur Schule */
    @Schema(description = "Bemerkung zur Schule")
    public String Bemerkung;

    /** Gibt an ob die Schule eine CD für ASDPC32 möchte */
    @Schema(description = "Gibt an ob die Schule eine CD für ASDPC32 möchte")
    public Integer CD;

    /** Stift */
    @Schema(description = "Stift")
    public Integer Stift;

    /** Gibt an ob die Schule offenen Ganztag hat */
    @Schema(description = "Gibt an ob die Schule offenen Ganztag hat")
    public String OGTS;

    /** SELB */
    @Schema(description = "SELB")
    public String SELB;

    /** Gibt an ob die Schule Internatsplätze hat */
    @Schema(description = "Gibt an ob die Schule Internatsplätze hat")
    public String Internat;

    /** Anzahl der Internatsplätze */
    @Schema(description = "Anzahl der Internatsplätze")
    public Integer InternatPlaetze;

    /** Schulmailadresse */
    @Schema(description = "Schulmailadresse")
    public String SMail;

    /** Hat die Schule Sport im Abitur? */
    @Schema(description = "Hat die Schule Sport im Abitur?")
    public String SportImAbi;

    /** Nimmt die Schule am Projekt Talentschule teil? */
    @Schema(description = "Nimmt die Schule am Projekt Talentschule teil?")
    public String Tal;

    /** Ist die konfessionelle Kooperation an dieser Schule genehmigt? */
    @Schema(description = "Ist die konfessionelle Kooperation an dieser Schule genehmigt?")
    public String KonKop;

    /** Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit ab welchem Schuljahr an")
    public Integer gueltigVon;

    /** Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(description = "Gibt die Gültigkeit bis zu welchem Schuljahr an")
    public Integer gueltigBis;

}
