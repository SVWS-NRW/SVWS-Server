package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.validation.constraints.NumericString;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class SchulbesuchPatchRequest {

	// **** Informationen zu der Schule, die vor der Aufnahme besucht wurde

	/** Die ID der vorher besuchten Schule. */
	@Schema(description = "die ID der vorher besuchten Schule", example = "12")
	public JsonNullable<Long> idVorherigeSchule = JsonNullable.undefined();

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	@Schema(description = "das Entlassdatum an der zuvor besuchten Schule", example = "1901-03-11")
	@ValidDateFormat
	public JsonNullable<String> vorigeEntlassdatum = JsonNullable.undefined();

	/** Der Entlassjahrgang an der zuvor besuchten Schule. */
	@Schema(description = "der Entlassjahrgang an der zuvor besuchten Schule", example = "03")
	@Size(max = 2)
	public JsonNullable<String> vorigeEntlassjahrgang = JsonNullable.undefined();

	/** Die ID der Art der letzten Versetzung an der zuvor besuchten Schule. */
	@Schema(description = "die Art der letzten Versetzung an der zuvor besuchten Schule", example = "11")
	@NumericString
	public JsonNullable<String> vorigeArtLetzteVersetzung = JsonNullable.undefined();

	/** Bemerkungen zu der zuvor besuchten Schule. */
	@Schema(description = "Bemerkungen zu der zuvor besuchten Schule", example = "diverse")
	@Size(max = 255)
	public JsonNullable<String> vorigeBemerkung = JsonNullable.undefined();

	/** Die ID des Grundes für die Entlassung von der zuvor besuchten Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von der zuvor besuchten Schule", example = "2")
	public JsonNullable<Long> vorigeEntlassgrundID = JsonNullable.undefined();

	/** Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde", example = "OA")
	@Size(max = 2)
	public JsonNullable<String> vorigeAbschlussartID = JsonNullable.undefined();


	// **** Informationen zu der Entlassung von der eigenen Schule

	/** Das Entlassdatum von dieser Schule. */
	@Schema(description = "das Entlassdatum von dieser Schule", example = "1902-03-11")
	@ValidDateFormat
	public JsonNullable<String> entlassungDatum = JsonNullable.undefined();

	/** Die Id des Jahrgangs bei der Entlassung von dieser Schule. */
	@Schema(description = "Die Id des Jahrgangs bei der Entlassung von dieser Schule.", example = "3")
	public JsonNullable<Long> idEntlassjahrgang = JsonNullable.undefined();

	/** Die ID des Grundes für die Entlassung von dieser Schule. */
	@Schema(description = "die ID des Grundes für die Entlassung von dieser Schule", example = "1")
	public JsonNullable<Long> entlassungGrundID = JsonNullable.undefined();

	/** Die ID des Abschlusses, welcher an dieser Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an dieser Schule erworben wurde", example = "OA")
	@Size(max = 2)
	public JsonNullable<String> entlassungAbschlussartID = JsonNullable.undefined();


	// **** Informationen zu der aufnehmenden Schule nach einem Wechsel zu einer anderen Schule

	/** Die ID der aufnehmenden Schule nach einer Entlassung. */
	@Schema(description = "die ID der aufnehmenden Schule nach einer Entlassung", example = "12")
	public JsonNullable<Long> idAufnehmendeSchule = JsonNullable.undefined();

	/** Das Datum beim Wechsel zu einer aufnehmenden Schule. */
	@Schema(description = "das Datum beim Wechsel zu einer aufnehmenden Schule", example = "2020-01-01")
	@ValidDateFormat
	public JsonNullable<String> aufnehmendWechseldatum = JsonNullable.undefined();

	/** Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat. */
	@Schema(description = "gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat", example = "true")
	public JsonNullable<Boolean> aufnehmendBestaetigt = JsonNullable.undefined();


	// **** Informationen zu der besuchten Grundschule

	/** Das Jahr der Einschulung in die Grundschule. */
	@Schema(description = "das Jahr der Einschulung in die Grundschule", example = "2005")
	public JsonNullable<Integer> grundschuleEinschulungsjahr = JsonNullable.undefined();

	/** Die ID der Einschulungsart in die Grundschule. */
	@Schema(description = "die ID der Einschulungsart in die Grundschule", example = "51")
	public JsonNullable<Long> grundschuleEinschulungsartID = JsonNullable.undefined();

	/** Die ID der Schuleingangsphase der Grundschule. */
	@Schema(description = "die ID der Schuleingangsphase der Grundschule", example = "2")
	public JsonNullable<Long> idGrundschuleJahreEingangsphase = JsonNullable.undefined();

	/** Die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I */
	@Schema(description = "die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I", example = "1")
	public JsonNullable<Long> idGrundschuleUebergangsempfehlung = JsonNullable.undefined();


	// **** Informationen zu dem Besuch der Sekundarstufe I

	/** Das Jahr des Wechsels in die Sekundarstufe I. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe I", example = "2009")
	public JsonNullable<Integer> sekIWechsel = JsonNullable.undefined();

	/** Das Kürzel der ersten Schulform in der Sekundarstufe I */
	@Schema(description = "das Kürzel der ersten Schulform in der Sekundarstufe I", example = "GY")
	@Size(max = 10)
	public JsonNullable<String> sekIErsteSchulform = JsonNullable.undefined();

	/** Das Jahr des Wechsels in die Sekundarstufe II. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe II", example = "2017")
	public JsonNullable<Integer> sekIIWechsel = JsonNullable.undefined();

	/** Die ID der Dauer des Kindergartenbesuchs eines Schülers. */
	@Schema(description = "Die ID der Dauer des Kindergartenbesuchs eines Schülers", example = "1")
	public JsonNullable<Long> idDauerKindergartenbesuch = JsonNullable.undefined();

	/** Die ID des Kindergartens. */
	@Schema(description = "die ID des Kindergartens", example = "2")
	public JsonNullable<Long> idKindergarten = JsonNullable.undefined();

	/** Schüler wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein). */
	@Schema(description = "gibt an, ob der Schüler zu einem Sprachförderkurs verpflichtet wurde", example = "false")
	public JsonNullable<Boolean> verpflichtungSprachfoerderkurs = JsonNullable.undefined();

	/** Teilnahme des Schülers an einem Sprachförderkurs (Ja/Nein). */
	@Schema(description = "gibt an, ob der Schüler an einem Sprachförderkurs teilgenommen hat", example = "false")
	public JsonNullable<Boolean> teilnahmeSprachfoerderkurs = JsonNullable.undefined();

}
