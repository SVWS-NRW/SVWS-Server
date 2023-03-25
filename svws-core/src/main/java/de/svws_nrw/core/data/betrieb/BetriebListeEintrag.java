package de.svws_nrw.core.data.betrieb;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Auswahl von Daten eines Betriebs aus einer Liste.  
 */
@XmlRootElement
@Schema(description="ein Eintrag eines Betriebs in der Erzieherliste.")
@TranspilerDTO
public class BetriebListeEintrag {

    /** ID der weiteren Adresse (Betriebe) */
	@Schema(required = true, description = "die ID des Betriebes", example="4711")
	public long id;

	/** Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart */
	@Schema(required = false, description = " Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart", example="4711")
	public Long adressArt;

	/** Name1 des Betriebs */
	@Schema(required = false, description = " Name1 des Betriebs", example="4711")
	public String name1;

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

	/** Brache des Betriebs */
	@Schema(required = false, description = " Brache des Betriebs ", example="true")
	public String branche;
    
}
