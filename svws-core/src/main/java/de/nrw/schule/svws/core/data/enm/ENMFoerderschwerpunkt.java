package de.nrw.schule.svws.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten in Bezug auf den Förderschwerpunkt-Katalog
 * für das Externe-Noten-Modul ENM.   
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten in Bezug auf den Förderschwerpunkt-Katalog für das Externe-Noten-Modul ENM")
@TranspilerDTO
public class ENMFoerderschwerpunkt {

	/** Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(required = true, description = "Das Kürzel, welche im Rahmen der amtlichen Schulstatistik verwendet wird", example="SH")
	public String kuerzel;

	/** Die textuelle Bezeichnung des Förderschwerpunktes. */
	@Schema(required = true, description = "Die textuelle Bezeichnung des Förderschwerpunktes", example="Sehen (Sehbehinderte)")
	public String beschreibung;
	
}
