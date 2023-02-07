package de.nrw.schule.svws.core.data.betrieb;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Ansprechpartner eines Betriebs.  
 */
@XmlRootElement
@Schema(description="Die Ansprechpartner eines Betriebes.")
@TranspilerDTO
public class BetriebAnsprechpartner {
	
    /** ID des Ansprechpartners */
	@Schema(required = true, description = "die ID des Ansprechpartners", example="4711")
	public long id;

    /** ID des Betriebs, dem der Ansprechpartner zugeordnet ist */
	@Schema(required = true, description = "die ID des Betriebs, dem der Ansprechpartner zugeorndet ist", example="4712")
	public long betrieb_id;
	
	/** Name des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Name des Ansprechpartners", example="Bergmann")
	public String name;

	/** Vorname des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Vorname des Ansprechpartners im Betrieb", example="Theodor")
	public String vorname;

	/** Anrede des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Anrede des Ansprechpartners im Betrieb", example="Herr")
	public String anrede;

	/** Telefonnummer des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Telefonnummer des Ansprechpartners im Betrieb", example="02170458241")
	public String telefon;

	/** Email-Adresse des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Email-Adresse des Ansprechpartners im Betrieb", example="test@email.de")
	public String email;

	/** ggf Abteilung des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "ggf Abteilung des Ansprechpartners im Betrieb", example="IT Bereich")
	public String abteilung;

	/** Titel des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "Titel des Ansprechpartners im Betrieb", example="Prof.")
	public String titel;

	/** GU_ID des Ansprechpartners im Betrieb */
	@Schema(required = true, description = "GU_ID des Ansprechpartners im Betrieb", example="23")
	public String GU_ID;

}
