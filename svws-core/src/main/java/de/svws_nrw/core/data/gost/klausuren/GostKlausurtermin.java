package de.svws_nrw.core.data.gost.klausuren;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einem Gost-Klausurtermin
 */
@XmlRootElement
@Schema(description = "ein Klausurtermin.")
@TranspilerDTO
public class GostKlausurtermin {

	/** Die ID des Klausurtermins. */
	@Schema(description = "die ID des Klausurtermins", example = "4711")
	public long id = -1;

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. */
	@Schema(description = "das Jahr, in welchem der Jahrgang Abitur machen wird", example = "2025")
	public int abijahr = -1;

	/** Das Gost-Halbjahr, in dem die Klausurg geschrieben wird. */
	@Schema(description = "das Gost-Halbjahr, in dem die Klausurg geschrieben wird", example = "3")
	public int halbjahr = -1;

	/** Das Quartal, in welchem die Klausur gechrieben wird. */
	@Schema(description = "das Quartal, in welchem die Klausur gechrieben wird", example = "1")
	public int quartal = -1;

	/** Das Datum des Klausurtermins, falls schon gesetzt. */
	@Schema(description = "das Datum des Klausurtermins, falls schon gesetzt", example = "29.02.2025")
	public String datum = null;

	/** Die Startzeit des Klausurtermins in Minuten seit 0 Uhr, falls schon gesetzt. */
	@Schema(description = "die ID der Unterrichtseinheit in Minuten seit 0 Uhr, falls schon gesetzt", example = "540")
	public Integer startzeit = null;

	/** Die Bezeichnung des Klausurtermins, falls schon gesetzt. */
	@Schema(description = "die Bezeichnung des Klausurtermins", example = "Zentrale Vergleichsarbeit")
	public String bezeichnung = null;

	/** Die textuelle Bemerkung zum Termin, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zum Termin, sofern vorhanden", example = "Im Anschluss kein regulärer Unterricht.")
	public String bemerkung = null;

}
