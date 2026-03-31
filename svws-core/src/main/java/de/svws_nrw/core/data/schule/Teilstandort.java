package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zu einem Teilstandort.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einem Teilstandort.")
@TranspilerDTO
public class Teilstandort {

	/** Das Adressmerkmal des Teilstandortes (meist ein Buchstabe wie A, B, ...) */
	@Schema(description = "Das Adressmerkmal des Teilstandortes (A...Z)", example = "A")
	public String adrMerkmal;

	/** Die Postleitzahl des Teilstandortes */
	@Schema(description = "Die Postleitzahl des Teilstandortes", example = "40213")
	public String plz;

	/** Der Ort des Teilstandortes */
	@Schema(description = "Der Ort des Teilstandortes", example = "Düsseldorf")
	public String ort;

	/** Der Straßenname des Teilstandortes */
	@Schema(description = "Der Straßenname des Teilstandortes", example = "Muster Str.")
	public String strassenname;

	/** Die Hausnummer des Teilstandortes */
	@Schema(description = "Die Hausnummer des Teilstandortes", example = "4")
	public String hausNr;

	/** Der Hausnummernzusatz des Teilstandortes */
	@Schema(description = "Der Hausnummernzusatz des Teilstandortes", example = "a-c")
	public String hausNrZusatz;

	/** Eine Bemerkung zum Teilstandort */
	@Schema(description = "Eine Bemerkung zum Teilstandort", example = "Hauptgebäude")
	public String bemerkung;

	/** Das Kürzel des Teilstandortes */
	@Schema(description = "Das Kürzel des Teilstandortes", example = "Hauptst.")
	public String kuerzel;

	/** Gibt an, ob der Eintrag in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Eintrag in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;
}
