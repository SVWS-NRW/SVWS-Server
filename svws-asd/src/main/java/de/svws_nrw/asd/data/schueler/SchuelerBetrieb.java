package de.svws_nrw.asd.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Betriebsdtaen einer Schülers in einem Betriebss.
 */
@XmlRootElement
@Schema(description = "Die Betriebsdaten eines Schülers in einem Betrieb.")
@TranspilerDTO
public class SchuelerBetrieb {

	/** Die ID des Betriebseintrags beim Schüler */
	@Schema(description = "Die ID des Betriebseintrags beim Schüler", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID des Schülers */
	@Schema(description = "Die ID des Schülers", example = "4713")
	public long idSchueler;

	/** Die ID des Betriebs */
	@Schema(description = "Die ID des Betriebs", example = "4")
	public long idBetrieb;

	/** Die ID des Ansprechpartners */
	@Schema(description = "Die ID des Ansprechpartners", example = "1")
	public Long idAnsprechpartner;

	/** Die ID des Betreuungslehrers */
	@Schema(description = "Die ID des Betreuungslehrers", example = "1")
	public Long idBetreuungslehrer;

	/** ID der Beschäftigungsart des Schülers */
	@Schema(description = "ID der Beschäftigungsart des Schülers", example = "2")
	public Long idBeschaeftigungsart;

	/** Der Name des Ausbilders */
	@Schema(description = "Der Name des Ausbilders", example = "Martin Stein")
	public String nameAusbilder;

	/** Das Datum des Vertragsbeginns */
	@Schema(description = "Das Datum des Vertragsbeginns", example = "2020-20-04")
	public String vertragsbeginn;

	/** Das Datum des Vertragsendes */
	@Schema(description = "Das Datum des Vertragsendes", example = "2020-20-04")
	public String vertragsende;

	/** Betrieb erhält Anschreiben */
	@Schema(description = "Betrieb erhält Anschreiben", example = "true")
	public Boolean erhaeltAnschreiben;

	/** Gibt an ob es ein Praktikum ist */
	@Schema(description = "Gibt an ob es ein Praktikum ist", example = "false")
	public Boolean istPraktikum;

	/** Die Sortierung des Betriebseintrags */
	@Schema(description = "Die Sortierung des Betriebseintrags", example = "true")
	public Integer sortierung;

}
