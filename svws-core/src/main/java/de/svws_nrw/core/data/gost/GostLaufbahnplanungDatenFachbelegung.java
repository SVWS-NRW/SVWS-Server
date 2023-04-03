package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Die Klasse enthält die Informationen zu einer Fachbelegung bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Enthält die Informationen zu einer Fachbelegung bei den Laufbahnplanungs-Daten der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungDatenFachbelegung {

	/** Die ID des Faches der Gymnasialen Oberstufe, welches belegt wurde. */
	public long fachID = -1;

	/** Gibt an, als welches Abiturfach das Fach belegt wurde (1,2,3,4 oder null) */
	public Integer abiturFach = null;

	/** Die ID der Gost-Kursart bei den Einzelbelegungen des Faches in den Halbjahren */
	public final @NotNull String[] kursart = new String[GostHalbjahr.maxHalbjahre];

	/** Gibt an, ob die Einzelbelegung des Faches in den Halbjahren schriftlich ist oder nicht */
	public final @NotNull boolean[] schriftlich = new boolean[GostHalbjahr.maxHalbjahre];

}
