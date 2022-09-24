package de.nrw.schule.svws.core.data.kursblockung;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/** Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für Eingabedaten, 
 * die dem Schüler-Blockungsalgorithmus übergeben werden. Dabei handelt es sich um eine (Neu-)Zuweisung EINES
 * Schülers auf eine existierende Kurslage. 
 * 
 * */
@XmlRootElement(name = "SchuelerblockungInput")
@Schema(name = "SchuelerblockungInput", description = "Spezifiziert die grundlegende Struktur von JSON-Daten für die Input-Daten des Schülerblockungsalgorithmus.")
@TranspilerDTO
public class SchuelerblockungInput {
	
	// Schüler ID (nötig?)
	
	// Anzahl der Schienen
	
	// Die Fachwahlen des Schülers (fach, kursart) --> Vector?
	
	// Alle in Frage kommenden Kurse
	    // --> Die Schienen (Plural!) des Kurses muss sich ergeben.  
		// --> Pro Kurs ggf. eine Info, ob dieser S. in diesem Kurs fixiert ist oder gesperrt ist.  

}
