package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Betriebsdtaen einer Schülers in einem Betriebss.  
 */
@XmlRootElement
@Schema(description = "Die Betriebsdaten eines Schülers in einem Betriebes.")
@TranspilerDTO
public class SchuelerBetriebsdaten {

    /** ID des Datensatzes */
	@Schema(description = "die ID des Datensatzes", example = "4711")
	public long id;

    /** ID des Schülers */
	@Schema(description = "die ID des Schülers", example = "4713")
	public long schueler_id;

	/** AdressID des Betriebeeintrags beim Schüler */
	@Schema(description = "AdressID des Betriebeeintrags beim Schüler", example = "4")
	public long betrieb_id;

	/** ID der Beschäftigungsart des Schülers */
	@Schema(description = "die ID des Schülers", example = "2")
	public Long beschaeftigungsart_id;

	/** Datum Vertragsbeginn des Betriebeeintrags beim Schüler */
	@Schema(description = "Datum Vertragsbeginn des Betriebeeintrags beim Schüler", example = "20.04.2021")
	public String vertragsbeginn;

	/** Datum des Vertragsende des Betriebeeintrags beim Schüler */
	@Schema(description = "Datum des Vertragsende des Betriebeeintrags beim Schüler", example = "12.02.2023")
	public String vertragsende;

	/** Ausbildername des Betriebeeintrags beim Schüler */
	@Schema(description = "Ausbildername des Betriebeeintrags beim Schüler", example = "Martin Stein")
	public String ausbilder;

	/** Betrieb erhält Anschreiben Ja/Nein */
	@Schema(description = "Betrieb erhält Anschreiben Ja/Nein", example = "true")
	public Boolean allgadranschreiben;

	/** Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler */
	@Schema(description = "Gibt an ob es ein Praktikum ist beim Betriebeeintrags beim Schüler", example = "false")
	public Boolean praktikum;

	/** Sortierung des Betriebeeintrags beim Schüler */
	@Schema(description = "Sortierung des Betriebeeintrags beim Schüler", example = "true")
	public Integer sortierung;

	/** AnsprechpartnerID des Betriebeeintrags beim Schüler */
	@Schema(description = "AnsprechpartnerID des Betriebeeintrags beim Schüler", example = "1")
	public Long ansprechpartner_id;
	
	/** BetreuungslehrerID des Betriebeeintrags beim Schüler */
	@Schema(description = "BetreuungslehrerID des Betriebeeintrags beim Schüler", example = "1")
	public Long betreuungslehrer_id;
	
}
