package de.svws_nrw.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten in Bezug auf den Förderschwerpunkt-Katalog
 * für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten in Bezug auf den Förderschwerpunkt-Katalog für das Externe-Noten-Modul ENM")
@TranspilerDTO
public class ENMFoerderschwerpunkt {

	/** Die ID des Förderschwerpunktes. */
	@Schema(description = "die ID des Förderschwerpunktes", example = "42")
	public long id;

	/** Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(description = "Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird", example = "SH")
	public String kuerzel;

	/** Die textuelle Bezeichnung des Förderschwerpunktes. */
	@Schema(description = "Die textuelle Bezeichnung des Förderschwerpunktes", example = "Sehen (Sehbehinderte)")
	public String beschreibung;

}
