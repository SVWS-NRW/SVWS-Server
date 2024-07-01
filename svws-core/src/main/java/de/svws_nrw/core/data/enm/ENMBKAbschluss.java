package de.svws_nrw.core.data.enm;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Struktur von JSON-Daten zu Abschlüssen
 * am Berufskolleg eines Schülers für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die Struktur von JSON-Daten zu Abschlüssen am Berufskolleg eines Schülers für das Externe-Noten-Modul (ENM)")
@TranspilerDTO
public class ENMBKAbschluss {

	/** Gibt an, ob der Schüler die Zulassung für die Abschlussprüfung hat. */
	@Schema(description = "Gibt an, ob der Schüler die Zulassung für die Abschlussprüfung hat.", example = "true")
	public boolean hatZulassung;

	/** Gibt an, ob der Schüler die Abschlussprüfung bestanden hat. */
	@Schema(description = "Gibt an, ob der Schüler die Abschlussprüfung bestanden hat.", example = "true")
	public boolean hatBestanden;

	/** Gibt an, ob der Schüler die Zulassung zum Erwerb der erweiterten beruflichen Kenntnisse hat. */
	@Schema(description = "Gibt an, ob der Schüler die Zulassung zum Erwerb der erweiterten beruflichen Kenntnisse hat.", example = "true")
	public boolean hatZulassungErweiterteBeruflicheKenntnisse;

	/** Gibt an, ob der Schüler die erweiterten beruflichen Kenntnisse erworben hat. */
	@Schema(description = "Gibt an, ob der Schüler die erweiterten beruflichen Kenntnisse erworben hat.", example = "true")
	public boolean hatErworbenErweiterteBeruflicheKenntnisse;

	/** Das Notenkürzel der Note der praktischen Prüfung. */
	@Schema(description = "Das Notenkürzel der Note der praktischen Prüfung.", example = "2+")
	public String notePraktischePruefung;

	/** Das Notenkürzel der Note aus dem Kolloqium. */
	@Schema(description = "Das Notenkürzel der Note aus dem Kolloqium.", example = "2+")
	public String noteKolloqium;

	/** Gibt an, ob der Schüler die Zulassung zur Berufsabschlussprüfung hat. */
	@Schema(description = "Gibt an, ob der Schüler die Zulassung zur Berufsabschlussprüfung hat.", example = "true")
	public boolean hatZulassungBerufsabschlusspruefung;

	/** Gibt an, ob der Schüler die Berufsabschlussprüfung bestanden hat. */
	@Schema(description = "Gibt an, ob der Schüler die Berufsabschlussprüfung bestanden hat.", example = "true")
	public boolean hatBestandenBerufsabschlusspruefung;

	/** Gibt das Thema der Abschlussarbeit an. */
	@Schema(description = "Gibt das Thema der Abschlussarbeit an.", example = "Die Bedeutung der Milchkuh im 18. Jahrhundert.")
	public String themaAbschlussarbeit;

	/** Gibt an, ob eine Berufsabschlussprüfung vorhanden ist oder nicht. */
	@Schema(description = "Gibt an, ob eine Berufsabschlussprüfung vorhanden ist oder nicht.", example = "true")
	public boolean istVorhandenBerufsabschlusspruefung;

	/** Das Notenkürzel der Note aus der Fachpraxis. */
	@Schema(description = "Das Notenkürzel der Note aus der Fachpraxis.", example = "3-")
	public String noteFachpraxis;

	/** Gibt an, ob der fachpraktische Teil ausreichend ist oder nicht. */
	@Schema(description = "Gibt an, ob der fachpraktische Teil ausreichend ist oder nicht.", example = "true")
	public boolean istFachpraktischerTeilAusreichend;

	/** Die Informationen zu den einzelnen Fächern, die dem Abschluss zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = ENMBKFach.class,
			description = "Ein Array mit den Informationen zu den einzelnen Fächern, die dem Abschluss zugeordnet sind."))
	public @NotNull List<ENMBKFach> faecher = new ArrayList<>();

}
