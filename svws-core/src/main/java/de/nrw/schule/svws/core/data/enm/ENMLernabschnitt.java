package de.nrw.schule.svws.core.data.enm;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu dem Lernabschnitt
 * des Schülers für das Externe-Noten-Modul ENM. 
 */
@XmlRootElement
@Schema(description="Spezifiziert die Struktur von JSON-Daten zu dem Lernabschnitt des Schülers für das Externe-Noten-Modul ENM. ")
@TranspilerDTO
public class ENMLernabschnitt {

	/** 
	 * Die ID des Lernabschnittes in der SVWS-DB - kann zum Prüfen verwendet werden, ob der 
	 * zuvor exportierte Lernabschnitt in der DB noch gültig ist  
	 */
	@Schema(required = true, description = "Die ID des Lernabschnittes in der SVWS-DB - kann zum Prüfen verwendet werden, ob der"
			+ " zuvor exportierte Lernabschnitt in der DB noch gültig ist.", example="123456")
    public long id;
 
    /** Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittel werden.", example="23")
    public Integer fehlstundenGesamt;

    /** Gibt den Zeitstempel der letzten Änderung für die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt den Zeitstempel der letzten Änderung für die Anzahl der gesamten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden.", example="2013-11-14 13:12:48.774")
    public String tsFehlstundenGesamt;

    /** Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittel werden.", example="0")
    public Integer fehlstundenGesamtUnentschuldigt;
	
    /** Gibt den Zeitstempel der letzten Änderung für die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittelt werden. */
	@Schema(required = false, description = "Gibt den Zeitstempel der letzten Änderung für die Anzahl der unentschuldigten Fehlstunden an, sofern diese abschnittsbezogen ermittel werden.", example="2013-11-14 13:12:48.774")
    public String tsFehlstundenGesamtUnentschuldigt;
	
    /** Die Prüfungsordnung, die in dem Lernabschnitt verwendet werden muss */
    @Schema(required = true, description = "Die Prüfungsordnung, die in dem Lernabschnitt verwendet werden muss.", example="GE-APO-SI-05")
    // TODO Core Type Pruefungsordnung 
    public String pruefungsordnung;

	/** Das Kürzel der Note für den Lernbereich 1, die vergeben wurde. */
    @Schema(required = true, description = "Das Kürzel der Note für den Lernbereich 1, die vergeben wurde.", example="4")
    public String lernbereich1note;
    
	/** Das Kürzel der Note für den Lernbereich 2, die vergeben wurde. */
    @Schema(required = true, description = "Das Kürzel der Note für den Lernbereich 2, die vergeben wurde.", example="2")
    public String lernbereich2note;

	/** Das Kürzel des Haupförderschwerpunktes oder null bei keinem Haupförderschwerpunkt */
    @Schema(required = true, description = "Das Kürzel des Haupförderschwerpunktes oder null bei keinem Haupförderschwerpunkt.", example="LB")
    public String foerderschwerpunkt1;
    
	/** Das Kürzel des weiteren Förderschwerpunktes oder null bei keinem weiteren Förderschwerpunkt */
    @Schema(required = true, description = "Das Kürzel des weiteren Förderschwerpunktes oder null bei keinem weiteren Förderschwerpunkt.", example="ES")
    public String foerderschwerpunkt2;
	
}
