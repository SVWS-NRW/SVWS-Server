package de.svws_nrw.core.data.lernplattform.v1;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die Datenstruktur für die Übermittlung/Export von Lernplattform Informationen.
 */
@XmlRootElement
@Schema(description = "Datenstruktur für die Übermittlung/Export von Lernplattform Informationen.")
@TranspilerDTO
public class LernplattformV1Export {

	/** Die Revision des Lernplattform-Datenformates, um zu überprüfen, ob die Datei in dem richtigen Format vorliegt (-1 für Entwickler-Revisionen und
	 * ansonsten aufsteigend ab 1 */
	@Schema(description = "Die Revisionsnummer des Rückgabeformates der Daten.", example = "1")
	public int revision = -1;

	/** Gibt die ID der Lernplattform an. */
	@Schema(description = "Enthält die ID der Lernplattform.", example = "1")
	public Long lernplattformId;

	/** Enthält die Bezeichnung der Lernplattform. */
	@Schema(description = "Enthält die Bezeichnung der Lernplattform.", example = "Iserv")
	public String lernplattformBezeichnung;

	/** Der Titel des Inhalts der Datei.*/
	@Schema(description = "Der Titel des Inhalts.", example = "LernplattformenExport.json")
	public String titel = "JSON für Lernplattformen";

	/** Der Titel des Inhalts der Datei.*/
	@Schema(description = "Beschreibung für den Inhalt der Daten", example = "Diese JSON stellt Daten für ein internes Update der Lernplattformen bereit.")
	public String beschreibung = "Diese JSON beinhaltet alle Daten für die Synchronisation für eine Lernplattform.";

	/** Der Titel des Inhalts der Datei.*/
	@Schema(description = "Autor der generierten Daten", example = "MSB SVWS")
	public String autor = "MSB SVWS";

	/** Der Zeitstempel für die Anfragen nach der Lernplattform-Datei.*/
	@Schema(description = "Zeitpunkt, an dem die Anfrage gestellt wurde", example = "2013-11-14 13:12:48.774")
	public String anfrageZeitpunkt;

	/** Der Zeitstempel für die Antwort der Lernplattform-Datei.*/
	@Schema(description = "Zeitpunkt, an dem die Antwort zurückgeschickt wurde", example = "2013-11-14 13:12:48.774")
	public String antwortZeitpunkt;

	/** Die Schulnummer, für welche die Lernplattform-Daten generiert wurde. */
	@Schema(description = "Nummer der Schule", example = "100815")
	public long schulnummer;

	/** Das Schuljahr, für welches die Lernplattform-Daten generiert wurde. */
	@Schema(description = "Schuljahr", example = "2025")
	public int schuljahr;

	/** Gibt an, für welchen Abschnitt innerhalb des Schuljahres die Lernplattform-Daten generiert wurde. */
	@Schema(description = "Abschnitt des Schuljahres", example = "2")
	public int schuljahresabschnitt;

	/** Gibt an, für welchen Abschnitt innerhalb des Schuljahres die Lernplattform-Daten generiert wurden. */
	@Schema(description = "Vollständiger Name der Schule",
			example = "Albert Zweistein Gymnasium Städt. Gymnasium für Jungen und Mädchen - Sekundarstufen I und II -")
	public String schulbezeichnung;

	/** Gibt an, welche E-Mail-Adresse für die Schule hinterlegt ist. */
	@Schema(description = "Kontakt E-Mail-Adresse der Schule", example = "schule@mail.de")
	public String mailadresse;

	/** Die Informationen zu den einzelnen Jahrgängen.*/
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Jahrgang.class, description = "Ein Array mit den Informationen zu den einzelnen Jahrgängen"))
	public final @NotNull List<LernplattformV1Jahrgang> jahrgaenge = new ArrayList<>();

	/** Die Informationen zu den einzelnen Klassen.*/
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Klasse.class, description = "Ein Array mit den Informationen zu den einzelnen Klassen."))
	public final @NotNull List<LernplattformV1Klasse> klassen = new ArrayList<>();

	/** Die Informationen zu Lehrern. */
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Lehrer.class, description = "Ein Array mit den Informationen zu Lehrern."))
	public final @NotNull List<LernplattformV1Lehrer> lehrer = new ArrayList<>();

	/** Die Informationen zu den Fächern. */
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Fach.class, description = "Ein Array mit den Informationen zu den Fächern."))
	public final @NotNull List<LernplattformV1Fach> faecher = new ArrayList<>();

	/** Die Informationen zu den Lerngruppen (Klassen und Kurse). */
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Lerngruppe.class, description = "Ein Array mit den Informationen zu den Lerngruppen."))
	public final @NotNull List<LernplattformV1Lerngruppe> lerngruppen = new ArrayList<>();

	/** Die Informationen zu den Schülern. */
	@ArraySchema(schema = @Schema(implementation = LernplattformV1Schueler.class, description = "Ein Array mit den Informationen zu den Schülern."))
	public final @NotNull List<LernplattformV1Schueler> schueler = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LernplattformV1Export() {
		// leer
	}
}
