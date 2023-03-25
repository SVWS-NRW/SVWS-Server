package de.svws_nrw.core.data.betrieb;

import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Stammdaten eines Betriebs.  
 */
@XmlRootElement
@Schema(description="Die Stammdaten eines Betriebes.")
@TranspilerDTO
public class BetriebStammdaten {

    /** ID der weiteren Adresse (Betriebe) */
	@Schema(required = true, description = "die ID des Betriebes", example="4711")
	public long id;

	/** Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart */
	@Schema(required = false, description = " Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart", example="4711")
	public Long adressArt;

	/** Name1 des Betriebs */
	@Schema(required = false, description = " Name1 des Betriebs", example="4711")
	public String name1;

	/** Name2 des Betriebs */
	@Schema(required = false, description = " Name2 des Betriebs", example="4711")
	public String name2;

	/** Straßenname des Betriebsdatensatz */
	@Schema(required = false, description = " Straßenname des Betriebsdatensatz", example="4711")
	public String strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Schema(required = false, description = " Hausnummer wenn getrennt gespeichert", example="4711")
	public String hausnr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Schema(required = false, description = " Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden", example="4711")
	public String hausnrzusatz;

	/** OrtID des Betriebs */
	@Schema(required = false, description = " OrtID des Betriebs", example="4711")
	public Long ort_id;

	/** PLZ des Betriebs */
	@Schema(required = false, description = " PLZ des Betriebs", example="4711")
	public String plz;

	/** Telefonnummer1 des Betriebs */
	@Schema(required = false, description = " Telefonnummer1 des Betriebs", example="4711")
	public String telefon1;

	/** Telefonnummer2 des Betriebs */
	@Schema(required = false, description = " Telefonnummer2 des Betriebs", example="4711")
	public String telefon2;

	/** Faxnummer des Betriebs */
	@Schema(required = false, description = " Faxnummer des Betriebs", example="4711")
	public String fax;

	/** E-MailAdresse des Betriebes */
	@Schema(required = false, description = " E-MailAdresse des Betriebes", example="4711")
	public String email;

	/** Bemerkung zum Betrieb */
	@Schema(required = false, description = " Bemerkung zum Betrieb ", example="4711")
	public String bemerkungen;

	/** Sortierung des Betriebsdatensatz */
	@Schema(required = false, description = " Sortierung des Betriebsdatensatz ", example="4711")
	public Integer sortierung;

	/** Gibt an ob der Betrieb ausbildet Ja Nein */
	@Schema(required = false, description = " Gibt an ob der Betrieb ausbildet Ja Nein ", example="true")
	public Boolean ausbildungsbetrieb;

	/** Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein */
	@Schema(required = false, description = " Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein ", example="true")
	public Boolean bietetPraktika;

	/** Brache des Betriebs */
	@Schema(required = false, description = " Brache des Betriebs ", example="true")
	public String branche;

	/** Adresszusatz zum Betrieb */
	@Schema(required = false, description = " Brache des Betriebs ", example="true")
	public String zusatz1;

	/** Adresszusatz2 zum Betrieb */
	@Schema(required = false, description = " Adresszusatz2 zum Betrieb ", example="Adresszusatz2")
	public String zusatz2;

	/** Sichtbarkeit des Datensatzes */
    @Schema(required = false, description = " Sichtbarkeit des Datensatzes ", example="true")
	public Boolean Sichtbar;

	/** Datensatz ist änderbar Ja Nein */
	@Schema(required = false, description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean Aenderbar;

	/** Bezeichnung des Maßnahmenträgers */
	@Schema(required = false, description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean Massnahmentraeger;

	/** Belehrung nach Infektionsschutzgesetz notwendig Ja Nein */
	@Schema(required = false, description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean BelehrungISG;

	/** GU_ID des Betriebsdatensatzes (für Import zur Erkennung) */
	@Schema(required = false, description = " GU_ID des Betriebsdatensatzes (für Import zur Erkennung)  ", example="true")
	public String GU_ID;

	/** Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt? */
	@Schema(required = false, description = " Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?  ", example="true")
	public Boolean ErwFuehrungszeugnis;

	/** Externe ID des Betriebsdatensatzes */
	@Schema(required = false, description = " Externe ID des Betriebsdatensatzes  ", example="1234")
	public String ExtID;
    
	/** Ein Array mit den Ansprechpartnern im Betrieb. */
	@ArraySchema(schema = @Schema(required = true, implementation = BetriebAnsprechpartner.class, description = "Ein Array mit den Ansprechpartnern im Betrieb."))
	public @NotNull Vector<@NotNull BetriebAnsprechpartner> ansprechpartner = new Vector<>();
	
}
