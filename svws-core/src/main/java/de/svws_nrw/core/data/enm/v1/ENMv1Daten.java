package de.svws_nrw.core.data.enm.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten für das
 * Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die grundlegende Struktur von JSON-Daten für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMv1Daten {

	/** Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1 */
	@Schema(description = "Die Revision des ENM-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und ansonsten aufsteigend ab 1.",
			example = "1")
	public int enmRevision = 1;

	/** Die Schulnummer, für welche die ENM-Daten generiert wurden. */
	@Schema(description = "Die Schulnummer, für welche die ENM-Daten generiert wurden.", example = "100815")
	public int schulnummer;

	/** Das Schuljahr, für welches die ENM-Daten generiert wurden. */
	@Schema(description = "Das Schuljahr, für welches die ENM-Daten generiert wurden.", example = "2021")
	public int schuljahr;

	/** Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus) */
	@Schema(description = "Die Anzahl der Abschnitte an der Schule (2: Halbjahrsmodus, 4: Quartalsmodus)", example = "2")
	public int anzahlAbschnitte;

	/** Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden. */
	@Schema(description = "Gibt an, für welchen Abschnitt innerhalb des Schuljahres die ENM-Daten generiert wurden.", example = "2")
	public int aktuellerAbschnitt;

	/** Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll. */
	@Schema(description = "Gibt den öffentlichen Schlüssel an, welcher für die Verschlüsselung und den Rückversand der Datei genutzt werden soll.",
			example = "")
	public String publicKey;

	/** Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. Ist die ID = NULL, enthält das Objekt alle Lehrerdaten. */
	@Schema(description = "Gibt die SVWS-ID des Lehrers an, für den die externe Notendatei generiert wurde. Ist die ID = NULL, enthält das Objekt alle Lehrerdaten.",
			example = "42")
	public Long lehrerID;

	/** Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht. */
	@Schema(description = "Gibt an, ob die Fehlstunden-Eingabe durch das externe Notenmodul erlaubt ist oder nicht. ", example = "true")
	public boolean fehlstundenEingabe;

	/** Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht. */
	@Schema(description = "Gibt an, ob die Fehlstunden für die Sekundarstufe I fachbezogen eingetragen werden oder nicht.", example = "false")
	public boolean fehlstundenSIFachbezogen;

	/** Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht. */
	@Schema(description = "Gibt an, ob die Fehlstunden für die Sekundarstufe II fachbezogen eingetragen werden oder nicht.", example = "true")
	public boolean fehlstundenSIIFachbezogen;

	/** Gibt das Kürzel der Schulform der Schule an. */
	@Schema(description = "Gibt das Kürzel der Schulform der Schule an.", example = "GY")
	public String schulform;

	/** Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll (z.B. mail@schule.nrw.de). */
	@Schema(description = "Gibt die Mailadresse an, an welche die verschlüsselte Datei zurückgesendet werden soll.", example = "mail@schule.nrw.de")
	public String mailadresse;

	/** Der Katalog mit den gültigen Einträgen von Noten (als Übersicht für das ENM-Tool) */
	@Schema(description = "Ein Array mit den gültigen Katalog-Einträgen für Noten (als Übersicht für das ENM-Client-Tool).")
	public @NotNull List<ENMv1Note> noten = new ArrayList<>();

	/** Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool) */
	@Schema(description = "Der Katalog mit den gültigen Einträgen von Förderschwerpunkten (als Übersicht für das ENM-Tool).")
	public @NotNull List<ENMv1Foerderschwerpunkt> foerderschwerpunkte = new ArrayList<>();

	/** Die Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind.  */
	@ArraySchema(schema = @Schema(implementation = ENMv1Jahrgang.class,
			description = "Ein Array mit den Informationen zu den einzelnen Jahrgängen, die in der Notendatei enthalten sind."))
	public @NotNull List<ENMv1Jahrgang> jahrgaenge = new ArrayList<>();

	/** Die Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind.  */
	@ArraySchema(schema = @Schema(implementation = ENMv1Klasse.class,
			description = "Ein Array mit den Informationen zu den einzelnen Klassen, die in der Notendatei enthalten sind."))
	public @NotNull List<ENMv1Klasse> klassen = new ArrayList<>();

	/** Die Informationen der vordefinierten Floskelgruppen und deren Floskeln. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Floskelgruppe.class,
			description = "Ein Array mit den Informationen der vordefinierten Floskelgruppen und deren Floskeln."))
	public @NotNull List<ENMv1Floskelgruppe> floskelgruppen = new ArrayList<>();

	/** Die Informationen zu Lehrern, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Lehrer.class,
			description = "Ein Array mit den Informationen zu Lehrern, die in der Notendatei vorhanden sind."))
	public @NotNull List<ENMv1Lehrer> lehrer = new ArrayList<>();

	/** Die Informationen zu den Fächern, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Fach.class,
			description = "Ein Array mit den Informationen zu den Fächern, die in der Notendatei vorhanden sind."))
	public @NotNull List<ENMv1Fach> faecher = new ArrayList<>();

	/** Der Katalog der Ankreuzkompetenzen (Grundschulzeugnisse und Inklusionszeugnisse) */
	@Schema(description = "der Katalog der Ankreuzkompetenzen (Grundschulzeugnisse und Inklusionszeugnisse)")
	public @NotNull ENMv1AnkreuzkompetenzenKatalog ankreuzkompetenzen = new ENMv1AnkreuzkompetenzenKatalog();

	// TODO Katalog der Kursarten

	/** Die Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Teilleistungsart.class,
			description = "Ein Array mit den Informationen zu den Teilleistungsarten, die in der Notendatei vorhanden sind."))
	public @NotNull List<ENMv1Teilleistungsart> teilleistungsarten = new ArrayList<>();

	/** Die Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Lerngruppe.class,
			description = "Ein Array mit den Informationen zu den Lerngruppen (Klassen und Kurse), die in der Notendatei vorhanden sind."))
	public @NotNull List<ENMv1Lerngruppe> lerngruppen = new ArrayList<>();

	/** Die Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden. */
	@ArraySchema(schema = @Schema(implementation = ENMv1Schueler.class,
			description = "Ein Array mit den Informationen zu den Schülern, deren Noten in dieser Notendatei verwaltet werden."))
	public @NotNull List<ENMv1Schueler> schueler = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ENMv1Daten() {
		// leer
	}

}


