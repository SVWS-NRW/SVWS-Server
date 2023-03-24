package de.nrw.schule.svws.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse stellt den Core-Type für Markierung eines Kurses aus den Leistungsdaten 
 * für die Abiturberechnung zur Verfügung.
 */
@XmlRootElement
@Schema(description="Enthält Informationen zu Markierungen eines Kurses für das Abitur.")
@TranspilerDTO
public class AbiturKursMarkierung {

	/** Gibt an, ob der Kurs in die Berechnung eingeht */
	public boolean fuerBerechnung;
	
	/** Gibt an, ob der Kurs auf dem Zeugnis angegeben werden soll. */
	public boolean aufAbiturZeugnis;
	
	
	/**
	 * Erzeugt eine neue Markierung, die angibt, dass der Kurs nicht in die Berechnung eingeht,
	 * aber auf dem Abiturzeugnis erscheinen soll. 
	 */
	public AbiturKursMarkierung() {
		this.fuerBerechnung = false;
		this.aufAbiturZeugnis = true;
	}
	
	/** 
	 * Erzeugt eine neue Markierung. Diese gibt an, dass der Kurs auf dem Abiturzeugnis erscheinen soll.
	 * 
	 * @param fuerBerechnung   gibt an, on der Kurs in die Berechnung eingehen soll oder nicht
	 */
	public AbiturKursMarkierung(final boolean fuerBerechnung) {
		this.fuerBerechnung = fuerBerechnung;
		this.aufAbiturZeugnis = true;
	}
	
	
	/** 
	 * Erzeugt eine neue Markierung.  
	 * 
	 * @param fuerBerechnung      gibt an, on der Kurs in die Berechnung eingehen soll oder nicht
	 * @param aufAbiturZeugnis    gibt an, ob der Kurs auf dem Abiturzeugnis erscheinen soll
	 */
	public AbiturKursMarkierung(final boolean fuerBerechnung, final boolean aufAbiturZeugnis) {
		this.fuerBerechnung = fuerBerechnung;
		this.aufAbiturZeugnis = aufAbiturZeugnis;
	}

}
