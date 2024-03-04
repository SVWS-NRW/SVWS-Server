package de.svws_nrw.core.data.klassen;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten einer Klasse.
 */
@XmlRootElement
@Schema(description = "Die Daten einer Klasse.")
@TranspilerDTO
public class KlassenDaten {

	/** Die ID der Klasse. */
	@Schema(description = "die ID der Klasse", example = "4709")
	public long id;

	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(description = "die ID des Schuljahresabschnittes des Kurses", example = "14")
	public long idSchuljahresabschnitt;

	/** Das Kürzel der Klasse. */
	@Schema(description = "das Kürzel der Klasse", example = "06b")
	public String kuerzel;

	/** Die ID des zugeordneten Jahrgangs, dem die Klasse zugeordnet ist */
	@Schema(description = "die ID des zugeordneten Jahrgangs", example = "815")
	public Long idJahrgang;

	/** Das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z). */
	@Schema(description = "das Kürzel für die Parallelität der Klasse innerhalb des Jahrgangs (A-Z)", example = "B")
	public String parallelitaet;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Die Liste der IDs der Klassenleitungen der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public @NotNull List<@NotNull Long> klassenLeitungen = new ArrayList<>();

	/** Die Schüler der Klasse. */
	@ArraySchema(schema = @Schema(implementation = Schueler.class))
	public @NotNull List<@NotNull Schueler> schueler = new ArrayList<>();

	/** Adressmerkmal des Teilstandorts für die Klasse */
	@Schema(description = "Adressmerkmal des Teilstandorts für die Klasse", example = "A")
	public @NotNull String teilstandort = "";

	/** Eine zusätzliche Beschreibung zu der Klasse */
	@Schema(description = "Eine zusätzliche Beschreibung zu der Klasse", example = "09b - bilinguale Klasse")
	public @NotNull String beschreibung = "";

	/** Die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null */
	@Schema(description = "die ID der Vorgängerklasse, sofern im vorigen Schuljahresabschnitt definiert - ansonsten null", example = "4711")
	public Long idVorgaengerklasse = null;

	/** Das Kürzel der Vorgängerklasse vor der letzen Versetzung. */
	@Schema(description = "das Kürzel der Vorgängerklasse vor der letzen Versetzung.", example = "08b")
	public String kuerzelVorgaengerklasse = null;

	/** Die ID der Folgeklasse, sofern im folgenden Schuljahresabschnitt definiert - ansonsten null */
	@Schema(description = "die ID der Folgeklasse, sofern im folgenden Schuljahresabschnitt definiert - ansonsten null", example = "4712")
	public Long idFolgeklasse = null;

	/** Das Kürzel der Folgeklasse nach der nächsten Versetzung. */
	@Schema(description = "das Kürzel der Folgeklasse nach der nächsten Versetzung.", example = "10b")
	public String kuerzelFolgeklasse = null;

	/** Die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich */
	@Schema(description = "die ID für die Organisationsform der Klasse im allgemeinbildenden Bereich", example = "4711")
	public Long idAllgemeinbildendOrganisationsform = null;

	/** Die ID für die Organisationsform der Klasse im berufsbildenden Bereich */
	@Schema(description = "die ID für die Organisationsform der Klasse im berufsbildenden Bereich", example = "4711")
	public Long idBerufsbildendOrganisationsform = null;

	/** Die ID für die Organisationsform der Klasse im Weiterbildungsbereich */
	@Schema(description = "die ID für die Organisationsform der Klasse im Weiterbildungsbereich", example = "4711")
	public Long idWeiterbildungOrganisationsform = null;

	/** Die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird. */
	@Schema(description = "die zugewiesene Prüfungsordnung, welche in Schild 3 genutzt wird", example = "APO-SI 05: Jahrgänge 5-10")
	public String pruefungsordnung = null;

	/** Die ID für die Schulgliederung der Klasse */
	@Schema(description = "die ID für die Schulgliederung der Klasse", example = "4711")
	public long idSchulgliederung = -1;

	/** Die ID für Klassenart */
	@Schema(description = "die ID für Klassenart", example = "4711")
	public long idKlassenart = -1;

	/** Gibt an, ob die Noteneingabe gesperrt ist */
	@Schema(description = "gibt an, ob die Noteneingabe gesperrt ist", example = "false")
	public boolean noteneingabeGesperrt = false;

	/** Gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden. */
	@Schema(description = "gibt an, ob Ankreuzkompetenzen für die Klasse verwendet werden", example = "false")
	public boolean verwendungAnkreuzkompetenzen = false;

	/** Die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null */
	@Schema(description = "die ID der Fachklasse, falls es sich um eine Klasse an einem Berufskolleg handelt oder null", example = "4711")
	public Long idFachklasse = null;

	/** Gibt am WBK an, ob die Klassen im Sommersemester angefangen hat. */
	@Schema(description = "gibt am WBK an, ob die Klassen im Sommersemester angefangen hat", example = "false")
	public boolean beginnSommersemester = false;

}
