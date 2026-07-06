package de.svws_nrw.asd.data.schule;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten einer Fachklasse.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der Fachklassen.")
@TranspilerDTO
public class FachklasseKatalogEintrag extends CoreTypeData {

	/** Teil 1 des Fachklassen Schlüssels. */
	@Schema(description = "Teil 1 des Fachklassen Schlüssels", example = "101")
	public String fkSchluessel;

	/** Teil 2 des Fachklassen Schlüssels. */
	@Schema(description = "Teil 2 des Fachklassen Schlüssels", example = "00")
	public String fkSchluessel2;

	/** Hier wird ein Fachklasseneintrag einem bkIndex zugeordnet. Aus der Schulgliederung kann der bkIndex ermittelt werden, um die korrekte Fachklasse zu ermitteln. */
	@Schema(description = "Hier wird ein Fachklasseneintrag einem bkIndex zugeordnet. Aus der Schulgliederung kann der bkIndex ermittelt werden, um die korrekte Fachklasse zu ermitteln", example = "10")
	public @NotNull Integer bkIndex = -1;

	/** ID des DQRNiveaus im CoreType DQRNiveau. */
	@Schema(description = "ID des DQRNiveaus", example = "1")
	public String dqrNiveau;

	/** Gibt an, ob die Fachklassen ausgelaufen ist oder nicht */
	@Schema(description = "gibt an, ob die Fachklassen ausgelaufen ist oder nicht", example = "false")
	public boolean istAusgelaufen = false;

	/** Die Gruppe des Berufsfeldes. */
	@Schema(description = "die Gruppe des Berufsfeldes", example = "T")
	public String berufsfeldGruppe;

	/** Das Berufsfeld. */
	@Schema(description = "das Berufsfeld", example = "MT")
	public String berufsfeld;

	/** Ebene 1 des Berufsfeldes */
	@Schema(description = "Ebene 1 des Berufsfeldes", example = "TE")
	public String ebene1;

	/** Ebene 2 des Berufsfeldes */
	@Schema(description = "Ebene 2 des Berufsfeldes", example = "ME")
	public String ebene2;

	/** Ebene 3 des Berufsfeldes */
	@Schema(description = "Ebene 3 des Berufsfeldes", example = "")
	public String ebene3;

	/** Die Bezeichnung der Fachklasse (männlich) */
	@Schema(description = "die Bezeichnung der Fachklasse (männlich)", example = "Metalltechnik")
	public @NotNull String bezeichnungM = "";

	/** Die Bezeichnung der Fachklasse (weiblich) */
	@Schema(description = "die Bezeichnung der Fachklasse (weiblich)", example = "Metalltechnik")
	public @NotNull String bezeichnungW = "";

}
