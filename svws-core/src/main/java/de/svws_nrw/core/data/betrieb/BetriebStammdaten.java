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
	@Schema(description = "die ID des Betriebes", example="4711")
	public long id;

	/** Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart */
	@Schema(description = " Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart", example="4711")
	public Long adressArt;

	/** Name1 des Betriebs */
	@Schema(description = " Name1 des Betriebs", example="4711")
	public String name1;

	/** Name2 des Betriebs */
	@Schema(description = " Name2 des Betriebs", example="4711")
	public String name2;

	/** Straßenname des Betriebsdatensatz */
	@Schema(description = " Straßenname des Betriebsdatensatz", example="4711")
	public String strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Schema(description = " Hausnummer wenn getrennt gespeichert", example="4711")
	public String hausnr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Schema(description = " Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden", example="4711")
	public String hausnrzusatz;

	/** OrtID des Betriebs */
	@Schema(description = " OrtID des Betriebs", example="4711")
	public Long ort_id;

	/** PLZ des Betriebs */
	@Schema(description = " PLZ des Betriebs", example="4711")
	public String plz;

	/** Telefonnummer1 des Betriebs */
	@Schema(description = " Telefonnummer1 des Betriebs", example="4711")
	public String telefon1;

	/** Telefonnummer2 des Betriebs */
	@Schema(description = " Telefonnummer2 des Betriebs", example="4711")
	public String telefon2;

	/** Faxnummer des Betriebs */
	@Schema(description = " Faxnummer des Betriebs", example="4711")
	public String fax;

	/** E-MailAdresse des Betriebes */
	@Schema(description = " E-MailAdresse des Betriebes", example="4711")
	public String email;

	/** Bemerkung zum Betrieb */
	@Schema(description = " Bemerkung zum Betrieb ", example="4711")
	public String bemerkungen;

	/** Sortierung des Betriebsdatensatz */
	@Schema(description = " Sortierung des Betriebsdatensatz ", example="4711")
	public Integer sortierung;

	/** Gibt an ob der Betrieb ausbildet Ja Nein */
	@Schema(description = " Gibt an ob der Betrieb ausbildet Ja Nein ", example="true")
	public Boolean ausbildungsbetrieb;

	/** Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein */
	@Schema(description = " Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein ", example="true")
	public Boolean bietetPraktika;

	/** Brache des Betriebs */
	@Schema(description = " Brache des Betriebs ", example="true")
	public String branche;

	/** Adresszusatz zum Betrieb */
	@Schema(description = " Brache des Betriebs ", example="true")
	public String zusatz1;

	/** Adresszusatz2 zum Betrieb */
	@Schema(description = " Adresszusatz2 zum Betrieb ", example="Adresszusatz2")
	public String zusatz2;

	/** Sichtbarkeit des Datensatzes */
    @Schema(description = " Sichtbarkeit des Datensatzes ", example="true")
	public Boolean Sichtbar;

	/** Datensatz ist änderbar Ja Nein */
	@Schema(description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean Aenderbar;

	/** Bezeichnung des Maßnahmenträgers */
	@Schema(description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean Massnahmentraeger;

	/** Belehrung nach Infektionsschutzgesetz notwendig Ja Nein */
	@Schema(description = " Datensatz ist änderbar Ja Nein ", example="true")
	public Boolean BelehrungISG;

	/** GU_ID des Betriebsdatensatzes (für Import zur Erkennung) */
	@Schema(description = " GU_ID des Betriebsdatensatzes (für Import zur Erkennung)  ", example="true")
	public String GU_ID;

	/** Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt? */
	@Schema(description = " Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt?  ", example="true")
	public Boolean ErwFuehrungszeugnis;

	/** Externe ID des Betriebsdatensatzes */
	@Schema(description = " Externe ID des Betriebsdatensatzes  ", example="1234")
	public String ExtID;
    
	/** Ein Array mit den Ansprechpartnern im Betrieb. */
	@ArraySchema(schema = @Schema(implementation = BetriebAnsprechpartner.class, description = "Ein Array mit den Ansprechpartnern im Betrieb."))
	public @NotNull Vector<@NotNull BetriebAnsprechpartner> ansprechpartner = new Vector<>();
	
}
