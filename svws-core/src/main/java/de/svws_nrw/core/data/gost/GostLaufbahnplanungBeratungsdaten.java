package de.svws_nrw.core.data.gost;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Die Klasse enthält die Informationen zur Beratung eines Schülers bei der Laufbahnplanung der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "Die Klasse enthält die Informationen zur Beratung eines Schülers bei der Laufbahnplanung der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostLaufbahnplanungBeratungsdaten {

	/** Die ID des Beratungslehrers, der die letze Beratung durchgeführt hat */
	@Schema(description = "die ID des Beratungslehrers, der die letze Beratung durchgeführt hat", example = "42")
	public Long beratungslehrerID = null;


	/** Das Beratungsdatum der letzten Beratung im Rahmen der Laufbahnplanung */
	@Schema(description = "das Beratungsdatum der letzten Beratung im Rahmen der Laufbahnplanung", example = "22.2.2022")
	public String beratungsdatum = null;

	/** Ein Kommentar zur Beratung */
	@Schema(description = "ein Kommentar zur Beratung", example = "Ein Kommentar")
	public String kommentar = null;

	/** Das Rücklaufdatum des Wahlbogens der letzten Beratung im Rahmen der Laufbahnplanung */
	@Schema(description = "das Rücklaufdatum des Wahlbogens der letzten Beratung im Rahmen der Laufbahnplanung", example = "22.2.2022")
	public String ruecklaufdatum = null;

}
