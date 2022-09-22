package de.nrw.schule.svws.core.types;

import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt die Core-Types für 
 * die Farben der Fächergruppen zur Verfügung.
 * Core-Types dienen als grundlegende abstrakte Datentypen sowohl für die Core-Algorithmen
 * als auch für die OpenAPI-Schnittstelle.
 */
public class RGBFarbe {
	
	private int red; 

	private int green;
	
	private int blue;
	
	
	
	/** 
	 * Setzt die Farbwerte aus den Werten red, blue, green zusammen. 
	 * 
	 * @param color24Bit        RGB-Farbwerte
	 * 
	 * */ 
	public RGBFarbe(int color24Bit) {
		if (color24Bit == 0) {
			red = green = blue = 255;
		} else {
			red = color24Bit & 0xFF;
			green = (color24Bit >> 8) & 0xFF;
			blue = color24Bit >> 16;
		}
	}
	
	
	/**
	 * Erstellt eine neue Farbe mit den angegebenen RGB-Werten.
	 * 
	 * @param red     der Rot-Anteil (0-255)
	 * @param green   der Grün-Anteil (0-255)
	 * @param blue    der Blau-Anteil (0-255)
	 */
	public RGBFarbe(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}


	@Override
	public @NotNull String toString() {
		return "rgb(" + red + ", " + green + ", " + blue + ")";
	}


    /** 
     * Holt den Wert des Farbwerts rot
     * 
     * @return Farbwert rot
     */
	public int getRed() {
		return red;
	}


    /** 
     * Bestimmt den Farbwert für rot
     * 
     * @param red          Farbwert rot
     *    
     */
	public void setRed(int red) {
		this.red = red;
	}


    /** 
     * Holt den Wert des Farbwerts grün
     * 
     * @return Farbwert grün
     */
	public int getGreen() {
		return green;
	}


    /** 
     * Bestimmt den Farbwert für grün
     * 
     * @param green          Farbwert grün
     *    
     */
	public void setGreen(int green) {
		this.green = green;
	}


    /** 
     * Holt den Wert des Farbwerts blau
     * 
     * @return Farbwert blau
     */
	public int getBlue() {
		return blue;
	}


    /** 
     * Bestimmt den Farbwert für blau
     * 
     * @param blue          Farbwert blau
     *    
     */
	public void setBlue(int blue) {
		this.blue = blue;
	}

}
