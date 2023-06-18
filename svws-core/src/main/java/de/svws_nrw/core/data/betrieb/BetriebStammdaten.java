package de.svws_nrw.core.data.betrieb;

import java.util.ArrayList;
import java.util.List;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet.
 * Sie beschreibt die Stammdaten eines Betriebs.
 */
@XmlRootElement
@Schema(description = "Die Stammdaten eines Betriebes.")
@TranspilerDTO
public class BetriebStammdaten {

    /** ID der weiteren Adresse (Betriebe) */
    @Schema(description = "die ID des Betriebes", example = "4711")
    public long id = -1;

    /** Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart */
    @Schema(description = "Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart", example = "4711")
    public Long adressArt = null;

    /** Name1 des Betriebs */
    @Schema(description = "Name1 des Betriebs", example = "4711")
    public @NotNull String name1 = "";

    /** Name2 des Betriebs */
    @Schema(description = "Name2 des Betriebs", example = "4711")
    public @NotNull String name2 = "";

    /** Straßenname des Betriebsdatensatz */
    @Schema(description = "Straßenname des Betriebsdatensatz", example = "4711")
    public @NotNull String strassenname = "";

    /** Hausnummer wenn getrennt gespeichert */
    @Schema(description = "Hausnummer wenn getrennt gespeichert", example = "4711")
    public @NotNull String hausnr = "";

    /** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
    @Schema(description = "Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden", example = "4711")
    public @NotNull String hausnrzusatz = "";

    /** OrtID des Betriebs */
    @Schema(description = "OrtID des Betriebs", example = "4711")
    public Long ort_id = null;

    /** Erste Telefonnummer des Betriebs */
    @Schema(description = "Erste Telefonnummer des Betriebs", example = "4711")
    public @NotNull String telefon1 = "";

    /** Zweite Telefonnummer des Betriebs */
    @Schema(description = "Zweite Telefonnummer des Betriebs", example = "4711")
    public @NotNull String telefon2 = "";

    /** Faxnummer des Betriebs */
    @Schema(description = "Faxnummer des Betriebs", example = "4711")
    public @NotNull String fax = "";

    /** E-MailAdresse des Betriebes */
    @Schema(description = "E-MailAdresse des Betriebes", example = "4711")
    public @NotNull String email = "";

    /** Bemerkung zum Betrieb */
    @Schema(description = "Bemerkung zum Betrieb ", example = "4711")
    public @NotNull String bemerkungen = "";

    /** Sortierung des Betriebsdatensatz */
    @Schema(description = "Sortierung des Betriebsdatensatz ", example = "4711")
    public int sortierung = 32000;

    /** Gibt an, ob der Betrieb ausbildet */
    @Schema(description = "Gibt an, ob der Betrieb ausbildet. ", example = "true")
    public boolean ausbildungsbetrieb = false;

    /** Gibt an, ob der Betrieb Praktikumsplätze bietet */
    @Schema(description = "Gibt an, ob der Betrieb Praktikumsplätze anbietet.", example = "true")
    public boolean bietetPraktika = false;

    /** Branche des Betriebs */
    @Schema(description = "Branche des Betriebs ", example = "Einzelhandel")
    public @NotNull String branche = "";

    /** Adresszusatz zum Betrieb */
    @Schema(description = "Adresszusatz des Betriebs ", example = "Adresszusatz")
    public @NotNull String zusatz1 = "";

    /** Adresszusatz2 zum Betrieb */
    @Schema(description = "Adresszusatz2 zum Betrieb ", example = "Adresszusatz2")
    public @NotNull String zusatz2 = "";

    /** Sichtbarkeit des Datensatzes */
    @Schema(description = "Sichtbarkeit des Datensatzes ", example = "true")
    public boolean Sichtbar = true;

    /** Datensatz ist änderbar Ja Nein */
    @Schema(description = "Datensatz ist änderbar Ja Nein ", example = "true")
    public boolean Aenderbar = true;

    /** Bezeichnung des Maßnahmenträgers */
    @Schema(description = "Bezeichnung des Maßnahmenträgers ", example = "true")
    public boolean Massnahmentraeger = false;

    /** Belehrung nach Infektionsschutzgesetz notwendig Ja Nein */
    @Schema(description = "Belehrung nach Infektionsschutzgesetz notwendig Ja Nein ", example = "true")
    public boolean BelehrungISG = false;

    /** GU_ID des Betriebsdatensatzes (für Import zur Erkennung) */
    @Schema(description = "GU_ID des Betriebsdatensatzes (für Import zur Erkennung)  ", example = "ID")
    public @NotNull String GU_ID = "";

    /** Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt? */
    @Schema(description = "Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?  ", example = "true")
    public boolean ErwFuehrungszeugnis = false;

    /** Externe ID des Betriebsdatensatzes */
    @Schema(description = "Externe ID des Betriebsdatensatzes  ", example = "1234")
    public String ExtID = null;

    /** Ein Array mit den Ansprechpartnern im Betrieb. */
    @ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class, description = "Ein Array mit den Ansprechpartnern im Betrieb."))
    public @NotNull List<@NotNull BetriebAnsprechpartner> ansprechpartner = new ArrayList<>();

}
