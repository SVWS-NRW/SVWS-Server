package de.svws_nrw.asd.data.schueler;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die Schulbesuchsdaten eines Schülers mit der angegebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Schulbesuchsdaten eines Schüler-Eintrags.")
@TranspilerDTO
public class SchuelerSchulbesuchsdaten {


	/** Die ID des Schülerdatensatzes. */
	@Schema(description = "die ID des Schülerdatensatzes", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;


	// **** Informationen zu der Schule, die vor der Aufnahme besucht wurde

	/** Die ID der vorher besuchten Schule. */
	@Schema(description = "die ID der vorher besuchten Schule", example = "12")
	public Long idVorherigeSchule;

	/** Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule. */
	@Schema(description = "die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule",
			example = "Grundschule (auch Primarstufe der Volkschule)")
	public String vorigeAllgHerkunft;

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	@Schema(description = "das Entlassdatum an der zuvor besuchten Schule", example = "1901-03-11")
	public String vorigeEntlassdatum;

	/** Der Entlassjahrgang an der zuvor besuchten Schule. */
	@Schema(description = "der Entlassjahrgang an der zuvor besuchten Schule", example = "03")
	public String vorigeEntlassjahrgang;

	/** Die ID der Art der letzten Versetzung an der zuvor besuchten Schule. */
	@Schema(description = "die Art der letzten Versetzung an der zuvor besuchten Schule", example = "11")
	public String vorigeArtLetzteVersetzung;

	/** Bemerkungen zu der zuvor besuchten Schule. */
	@Schema(description = "die Art der letzten Versetzung an der zuvor besuchten Schule", example = "diverse")
	public String vorigeBemerkung;

	/** Die ID des Grundes für die Entlassung von der zuvor besuchten Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von der zuvor besuchten Schule", example = "2")
	public Long vorigeEntlassgrundID;

	/** Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde", example = "OA")
	public String vorigeAbschlussartID;


	// **** Informationen zu der Entlassung von der eigenen Schule

	/** Das Entlassdatum von dieser Schule. */
	@Schema(description = "das Entlassdatum von dieser Schule", example = "1902-03-11")
	public String entlassungDatum;

	/** Der Jahrgang bei der Entlassung von dieser Schule. */
	@Schema(description = "er Jahrgang bei der Entlassung von dieser Schule", example = "03")
	public String entlassungJahrgang;

	/** Die ID des Grundes für die Entlassung von dieser Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von dieser Schule", example = "1")
	public Long entlassungGrundID;

	/** Die ID des Abschlusses, welcher an dieser Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an dieser Schule erworben wurde", example = "OA")
	public String entlassungAbschlussartID;


	// **** Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule

	/** Die ID der aufnehmenden Schule nach einer Entlassung. */
	@Schema(description = "die ID der aufnehmenden Schule nach einer Entlassung", example = "12")
	public Long idAufnehmendeSchule;

	/** Das Datum beim Wechsel zu einer aufnehmenden Schule. */
	@Schema(description = "das Datum beim Wechsel zu einer aufnehmenden Schule", example = "2020-01-01")
	public String aufnehmendWechseldatum;

	/** Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat. */
	@Schema(description = "gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat", example = "true")
	public Boolean aufnehmendBestaetigt;


	// **** Informationen zu der besuchten Grundschule

	/** Das Jahr der Einschulung in die Grundschule. */
	@Schema(description = "das Jahr der Einschulung in die Grundschule", example = "2005")
	public Integer grundschuleEinschulungsjahr;

	/** Die ID der Einschulungsart in die Grundschule. */
	@Schema(description = "die ID der Einschulungsart in die Grundschule", example = "51")
	public Long grundschuleEinschulungsartID;

	/** Die ID der Schuleingangsphase der Grundschule. */
	@Schema(description = "die ID der Schuleingangsphase der Grundschule", example = "2")
	public Long idGrundschuleJahreEingangsphase;

	/** Das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I */
	@Schema(description = "das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I", example = "GY")
	public String kuerzelGrundschuleUebergangsempfehlung;


	// **** Informationen zu dem Besuch der Sekundarstufe I

	/** Das Jahr des Wechsels in die Sekundarstufe I. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe I", example = "2009")
	public Integer sekIWechsel;

	/** Das Kürzel der ersten Schulform in der Sekundarstufe I */
	@Schema(description = "das Kürzel der ersten Schulform in der Sekundarstufe I", example = "GY")
	public String sekIErsteSchulform;

	/** Das Jahr des Wechsels in die Sekundarstufe II. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe II", example = "2017")
	public Integer sekIIWechsel;

	/** Die ID der Dauer des Kindergartenbesuchs eines Schülers. */
	@Schema(description = "Die ID der Dauer des Kindergartenbesuchs eines Schülers", example = "1")
	public Long idDauerKindergartenbesuch;

	/** Die ID des Kindergartens. */
	@Schema(description = "die ID des Kindergartens", example = "2")
	public Long idKindergarten;

	/** Schüler wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein). */
	@Schema(description = "gibt an, ob der Schüler zu einem Sprachförderkurs verpflichtet wurde", example = "false")
	public boolean verpflichtungSprachfoerderkurs;

	/** Teilnahme des Schülers an einem Sprachförderkurs (Ja/Nein). */
	@Schema(description = "gibt an, ob der Schüler an einem Sprachförderkurs teilgenommen hat", example = "false")
	public boolean teilnahmeSprachfoerderkurs;


	// **** Informationen zu besonderen Merkmalen für die Statistik

	/** Die Informationen zu den besonderen Merkmalen für die Statistik. */
	@ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class,
			description = "Ein Array mit den Informationen zu den besonderen Merkmalen für die Statistik."))
	public @NotNull List<SchuelerSchulbesuchMerkmal> merkmale = new ArrayList<>();


	// **** Informationen zu allen bisher besuchten Schulen (Array)

	/** Die Informationen zu allen bisher besuchten Schulen. */
	@ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchSchule.class,
			description = "Ein Array mit den Informationen zu allen bisher besuchten Schulen."))
	public @NotNull List<SchuelerSchulbesuchSchule> alleSchulen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerSchulbesuchsdaten() {
		// leer
	}

}
