package de.svws_nrw.service.klassen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Enthält die Attribute, die beim Anlegen einer Klasse über den {@link KlassenService} angegeben werden können.
 * Pflicht sind der Schuljahresabschnitt und das Kürzel. Alle übrigen Attribute sind optional. Wird ein optionales Attribut
 * nicht angegeben oder ist es {@code null}, so gilt der Standardwert der Schulform. Beim Jahrgang steht {@code null}
 * für eine jahrgangsübergreifende Klasse. Die Klassenleitungen können nur beim Ändern einer Klasse gesetzt werden.
 * Die Feldnamen stimmen mit dem Core-DTO {@code KlassenDaten} überein.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class KlassenCreateRequest {

	/** Die ID des Schuljahresabschnitts, in dem die Klasse angelegt wird. */
	@Schema(description = "die ID des Schuljahresabschnitts der Klasse", example = "1")
	@NotNull
	public Long idSchuljahresabschnitt;

	/** Das Kürzel der Klasse. Es muss im Schuljahresabschnitt eindeutig sein. */
	@Schema(description = "das Kürzel der Klasse", example = "5a")
	@NotBlank
	@Size(max = 15)
	public String kuerzel;

	/** Die ID des Jahrgangs der Klasse. Der Wert null steht für eine jahrgangsübergreifende Klasse. */
	@Schema(description = "die ID des Jahrgangs der Klasse (null für jahrgangsübergreifende Klassen)", example = "4")
	public Long idJahrgang;

	/** Die Parallelität der Klasse, ein Großbuchstabe von A bis Z. */
	@Schema(description = "die Parallelität der Klasse (A bis Z)", example = "A")
	@Pattern(regexp = "[A-Z]")
	public String parallelitaet;

	/** Die Sortierreihenfolge der Klasse. */
	@Schema(description = "die Sortierreihenfolge der Klasse", example = "1")
	@Min(0)
	public Integer sortierung;

	/** Das Adressmerkmal des Teilstandortes der Klasse, genau ein Zeichen, das kein Leerzeichen ist. */
	@Schema(description = "das Adressmerkmal des Teilstandortes der Klasse", example = "A")
	@Pattern(regexp = "\\S")
	public String teilstandort;

	/** Die Beschreibung der Klasse. */
	@Schema(description = "die Beschreibung der Klasse", example = "Klasse mit bilingualem Zweig")
	@Size(max = 150)
	public String beschreibung;

	/** Die ID der Vorgängerklasse aus dem vorigen Schuljahresabschnitt. */
	@Schema(description = "die ID der Vorgängerklasse aus dem vorigen Schuljahresabschnitt", example = "10")
	public Long idVorgaengerklasse;

	/** Die ID der Folgeklasse aus dem nachfolgenden Schuljahresabschnitt. */
	@Schema(description = "die ID der Folgeklasse aus dem nachfolgenden Schuljahresabschnitt", example = "12")
	public Long idFolgeklasse;

	/** Die ID der Organisationsform bei einer allgemeinbildenden Schulform. */
	@Schema(description = "die ID der allgemeinbildenden Organisationsform", example = "1")
	public Long idAllgemeinbildendOrganisationsform;

	/** Die ID der Organisationsform bei einer berufsbildenden Schulform. */
	@Schema(description = "die ID der berufsbildenden Organisationsform", example = "1")
	public Long idBerufsbildendOrganisationsform;

	/** Die ID der Organisationsform bei einer Schulform der Weiterbildung. */
	@Schema(description = "die ID der Organisationsform am Weiterbildungskolleg", example = "1")
	public Long idWeiterbildungOrganisationsform;

	/** Die ID der Schulgliederung. Bei null oder -1 wird die Standard-Schulgliederung der Schulform verwendet. */
	@Schema(description = "die ID der Schulgliederung (null oder -1 für die Standard-Schulgliederung)", example = "5")
	public Long idSchulgliederung;

	/** Die ID der Klassenart. */
	@Schema(description = "die ID der Klassenart", example = "3")
	public Long idKlassenart;

	/** Die ID der Fachklasse aus dem Fachklassen-Katalog. */
	@Schema(description = "die ID der Fachklasse aus dem Fachklassen-Katalog", example = "100")
	public Long idFachklasse;

	/** Gibt an, ob die Noteneingabe für die Klasse gesperrt ist. */
	@Schema(description = "gibt an, ob die Noteneingabe gesperrt ist", example = "false")
	public Boolean noteneingabeGesperrt;

	/** Gibt an, ob für die Klasse Ankreuzkompetenzen verwendet werden. */
	@Schema(description = "gibt an, ob Ankreuzkompetenzen verwendet werden", example = "false")
	public Boolean verwendungAnkreuzkompetenzen;

	/** Gibt an, ob die Klasse im Sommersemester beginnt. */
	@Schema(description = "gibt an, ob die Klasse im Sommersemester beginnt", example = "false")
	public Boolean beginnSommersemester;

	/** Gibt an, ob die Klasse an einer Justizvollzugsanstalt (JVA) unterrichtet wird. */
	@Schema(description = "gibt an, ob die Klasse an einer JVA unterrichtet wird", example = "false")
	public Boolean istJva;

	/** Gibt an, ob in der Klasse reformpädagogisch unterrichtet wird. */
	@Schema(description = "gibt an, ob in der Klasse reformpädagogisch unterrichtet wird", example = "false")
	public Boolean istReformpaedagogik;

}
