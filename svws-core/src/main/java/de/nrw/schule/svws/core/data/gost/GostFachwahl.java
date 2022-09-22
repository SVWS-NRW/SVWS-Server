package de.nrw.schule.svws.core.data.gost;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Dies ist ein Core-Type für die Information zu der Fachwahl eines Schülers
 * in der gymnasialen Oberstufe. 
 */
@XmlRootElement
@Schema(description="Informationen zu einer Fachwahl eines Schülers in der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "id", "fachID", "halbjahrID", "schuelerID", "schuelerNachname", "schuelerVorname", "kursartID" }) // TODO fehlt "istSchriftlich" ?
@TranspilerDTO
public class GostFachwahl {

	/** Die ID der Fachwahl */
	@Schema(required = true, description = "Die ID der Fachwahl.", example="4711000004712")
	public long id = -1;
	
	/** Die ID des Faches */
	@Schema(required = true, description = "Die ID des Faches.", example="4711")
	public long fachID = -1;
	
	/** Die ID des Halbjahres der gymnasialen Oberstufe, auf welches sich die Fachwahl bezieht */
	@Schema(required = true, description = "Die ID des Halbjahres der gymnasialen Oberstufe, auf welches sich die Fachwahl bezieht (0-5).", example="3")
	public int halbjahrID = -1;

	/** Die ID des Schülers */
	@Schema(required = true, description = "Die ID des Schülers.", example="4712")
	public long schuelerID = -1;

	/** Die ID des Schülers */
	@Schema(required = true, description = "Der Nachname des Schülers.", example="Mustermann")
	public @NotNull String schuelerNachname = "";

	/** Die ID des Schülers */
	@Schema(required = true, description = "Der Vorname des Schülers.", example="Max")
	public @NotNull String schuelerVorname = "";
	
	/** Die ID der Kursart */
	@Schema(required = true, description = "Die ID der Kursart.", example="4713")
	public long kursartID = -1;
	
	/** Gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht */
	@Schema(required = true, description = "gibt an, ob die Fachwahl ein schriftlicher Kurs ist oder nicht", example="true")
	public boolean istSchriftlich = false;

}
