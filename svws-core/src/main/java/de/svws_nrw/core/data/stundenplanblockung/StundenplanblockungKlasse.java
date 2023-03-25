package de.svws_nrw.core.data.stundenplanblockung;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.stundenplanblockung.StundenplanblockungAlgorithmus;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/** 
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Klasse bei {@link StundenplanblockungInput}. <br>
 * Beispielklassen wären '05c', '09a', aber auch 'EF', 'Q1' und 'Q2'. <br>   
 * Für jede Klasse wird vom {@link StundenplanblockungAlgorithmus} ein Stundenplan berechnet. 
 * 
 * @author Benjamin A. Bartsch
 */
@XmlRootElement(name = "StundenplanblockungKlasse")
@Schema(name = "StundenplanblockungKlasse", description = "Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für eine Klasse bei {@link StundenplanblockungInput}.")
@TranspilerDTO
public class StundenplanblockungKlasse {

	/** Die Datenbank-ID der Klasse. */
	public long id;

	/** Das Kürzel der Lehrkraft. Beispielsweise '07c' oder 'Q1'. */
	public @NotNull String kuerzel = "";

}
