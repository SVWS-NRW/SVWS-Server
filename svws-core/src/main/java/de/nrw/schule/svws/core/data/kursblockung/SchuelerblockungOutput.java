package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für Rückgabedaten, 
 * die dem Schüler-Blockungsalgorithmus übergeben werden. Dabei handelt es sich um eine (Neu-)Zuweisung EINES
 * Schülers auf eine existierende Kurslage. 
 * 
 * */
@XmlRootElement(name = "SchuelerblockungOutput")
@Schema(name = "SchuelerblockungOutput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Output-Daten des Schülerblockungsalgorithmus.")
@TranspilerDTO
public class SchuelerblockungOutput {
	
	// Schüler ID (nötig?)
	
	// ???
	

}
