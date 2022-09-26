package de.nrw.schule.svws.core.data.enm;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für das 
 * Externe-Noten-Modul ENM.   
 */
@XmlRootElement
@Schema(description="Spezifiziert die grundlegende Struktur von JSON-Daten für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMDaten {

	/** Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1 */
	@Schema(required = true, description = "Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.", example="1")
	public int enmRevision = -1;
	
	/** Die Schulnummer, für welche die ENM-Daten generiert wurden. */
	@Schema(required = true, description = "Die Schulnummer, für welche die ENM-Daten generiert wurden.", example="100815")
	public int schulnummer;
	
	/** Das Schuljahr, für welches die ENM-Daten generiert wurden. */
	@Schema(required = true, description = "Das Schuljahr, für welches die ENM-Daten generiert wurden.", example="2021")
	public int schuljahr;

	/** Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus) */
	@Schema(required = true, description = "Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)", example="2")
	public int anzahlAbschnitte;

	/** Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden. */
	@Schema(required = true, description = "Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden.", example="2")
	public int aktuellerAbschnitt;

	/** Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll. */
	@Schema(required = false, description = "Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll.", example="")
	public String publicKey;
	
	/** Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. */
	@Schema(required = true, description = "Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde.", example="42")
	public long lehrerID;

	/** Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht. */
	@Schema(required = true, description = "Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht. ", example="true")
	public boolean fehlstundenEingabe;

	/** Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht. */
	@Schema(required = true, description = "Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht.", example="false")
	public boolean fehlstundenSIFachbezogen;

	/** Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht. */
	@Schema(required = true, description = "Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht.", example="true")
	public boolean fehlstundenSIIFachbezogen;

	/** Gibt das Kürzel der Schulform der Schule an. */
	@Schema(required = true, description = "Gibt das Kürzel der Schulform der Schule an.", example="GY")
	public String schulform;

	/** Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll (z.B. mail@schule.nrw.de). */
	@Schema(required = false, description = "Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll.", example="mail@schule.nrw.de")
	public String mailadresse;

	/** Der Katalog mit den gültigen Einträgen von Noten (als Übersicht für das ENM-Tool) */
	@Schema(required = false, description = "Ein Array mit den gültigen Katalog-Einträgen für Noten (als Übersicht für das ENM-Client-Tool).")
	public @NotNull Vector<@NotNull ENMNote> noten = new Vector<>();
	
	/** Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool) */
	@Schema(required = false, description = "Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool).")
	public@NotNull  Vector<@NotNull ENMFoerderschwerpunkt> foerderschwerpunkte = new Vector<>();
	
	/** Die Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind.  */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMJahrgang.class, description = "Ein Array mit den Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind."))
	public @NotNull Vector<@NotNull ENMJahrgang> jahrgaenge = new Vector<>();
	
	/** Die Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind.  */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMKlasse.class, description = "Ein Array mit den Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind."))
	public @NotNull Vector<@NotNull ENMKlasse> klassen = new Vector<>();
	
	/** Die Informationen der vordefinierten Floskelgruppen und deren Floskeln. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMFloskelgruppe.class, description = "Ein Array mit den Informationen der vordefinierten Floskelgruppen und deren Floskeln."))
	public @NotNull Vector<@NotNull ENMFloskelgruppe> floskelgruppen = new Vector<>();

	/** Die Informationen zu Lehrern, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMLehrer.class, description = "Ein Array mit den Informationen zu Lehrern, die in der Notendatei vorhanden sind."))
	public @NotNull Vector<@NotNull ENMLehrer> lehrer = new Vector<>();

	/** Die Informationen zu den Fächern, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMFach.class, description = "Ein Array mit den Informationen zu den Fächern, die in der Notendatei vorhanden sind."))
	public @NotNull Vector<@NotNull ENMFach> faecher = new Vector<>();

	// TODO Katalog der Kursarten
	
	/** Die Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(required = false, implementation = ENMTeilleistungsart.class, description = "Ein Array mit den Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind."))
	public @NotNull Vector<@NotNull ENMTeilleistungsart> teilleistungsarten = new Vector<>();

	/** Die Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMLerngruppe.class, description = "Ein Array mit den Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind."))
	public @NotNull Vector<@NotNull ENMLerngruppe> lerngruppen = new Vector<>();

	/** Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden. */
	@ArraySchema(schema = @Schema(required = true, implementation = ENMSchueler.class, description = "Ein Array mit den Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden."))
	public @NotNull Vector<@NotNull ENMSchueler> schueler = new Vector<>();

}


