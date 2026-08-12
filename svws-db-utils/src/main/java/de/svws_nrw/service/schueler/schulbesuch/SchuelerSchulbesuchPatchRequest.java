package de.svws_nrw.service.schueler.schulbesuch;

import de.svws_nrw.validation.constraints.NumericString;
import de.svws_nrw.validation.constraints.ValidDateFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import org.openapitools.jackson.nullable.JsonNullable;

public class SchuelerSchulbesuchPatchRequest {

	/** Der Schlüssel des höchsten Schulabschlusses. */
	@Schema(description = "Der Schlüssel des höchsten Schulabschlusses.", example = "2")
	@Size(max = 2)
	public JsonNullable<String> schluesselHoechsterSchulabschluss = JsonNullable.undefined();

	/** Gibt an, ob ein Berufsabschluss vorhanden ist. */
	@Schema(description = "Gibt an, ob ein Berufsabschluss vorhanden ist.", example = "false")
	public JsonNullable<Boolean> berufsabschlussVorhanden = JsonNullable.undefined();

	/** Die ID der zuvor besuchten Schule. */
	@Schema(description = "Die ID der zuvor besuchten Schule.", example = "12")
	public JsonNullable<Long> idVorherigeSchule = JsonNullable.undefined();

	/** Die ID der HerkunftSonstige (falls zuvor besuchte Schule = kein Schulbesuch). */
	@Schema(description = "Die ID der HerkunftSonstige (falls zuvor besuchte Schule = kein Schulbesuch).", example = "1000")
	public JsonNullable<Long> idHerkunftSonstigeVorherigeSchule = JsonNullable.undefined();

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	@Schema(description = "Das Entlassdatum an der zuvor besuchten Schule.", example = "1901-03-11")
	@ValidDateFormat
	public JsonNullable<String> entlassdatumVorherigeSchule = JsonNullable.undefined();

	/** Das Kürzel des Entlassjahrgangs an der zuvor besuchten Schule. */
	@Schema(description = "Das Kürzel des Entlassjahrgangs an der zuvor besuchten Schule.", example = "03")
	@Size(max = 2)
	public JsonNullable<String> kuerzelEntlassjahrgangVorherigeSchule = JsonNullable.undefined();

	/** Die ID der Herkunftsart der Versetzung an der zuvor besuchten Schule. */
	@Schema(description = "Die ID der Herkunftsart der Versetzung an der zuvor besuchten Schule.", example = "11")
	@NumericString
	public JsonNullable<String> idHerkunftsartVersetzungVorherigeSchule = JsonNullable.undefined();

	/** Bemerkungen zur zuvor besuchten Schule. */
	@Schema(description = "Bemerkungen zur zuvor besuchten Schule.", example = "diverse")
	@Size(max = 255)
	public JsonNullable<String> bemerkungVorherigeSchule = JsonNullable.undefined();

	/** Die ID des Entlassgrundes der zuvor besuchten Schule. */
	@Schema(description = "Die ID des Entlassgrundes der zuvor besuchten Schule.", example = "2")
	public JsonNullable<Long> idEntlassgrundVorherigeSchule = JsonNullable.undefined();

	/** Der Schlüssel des Schulabschlusses (Allgemeinbildend) der zuvor besuchten Schule. */
	@Schema(description = "Die ID des Schulabschlusses (Allgemeinbildend) der zuvor besuchten Schule.", example = "B")
	@Size(max = 1)
	public JsonNullable<String> schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.undefined();

	/** Der Schlüssel des Schulabschlusses (Berufsbildend) der zuvor besuchten Schule. */
	@Schema(description = "Die ID des Schulabschlusses (Berufsbildend) der zuvor besuchten Schule.", example = "2")
	@Size(max = 1)
	public JsonNullable<String> schluesselAbschlussartBerufsbildendVorherigeSchule = JsonNullable.undefined();

	/** Die ID der Schulgliederung aus Herkunftbildungsgang.json (BK/SB) der zuvor besuchten Schule. */
	@Schema(description = "Die ID der Schulgliederung aus Herkunftbildungsgang.json (BK/SB) der zuvor besuchten Schule.", example = "2000")
	public JsonNullable<Long> idSchulgliederungVorherigeSchule = JsonNullable.undefined();

	/** Der Schlüssel des CoreTypes der Fachklasse der zuvor besuchten Schule (BK/SB). */
	@Schema(description = "Der Schlüssel des CoreTypes der Fachklasse der zuvor besuchten Schule (BK/SB).", example = "170-10100")
	public JsonNullable<@Size(max = 10) String> schluesselCoreTypeFachklasseVorherigeSchule = JsonNullable.undefined();

	/** Das Entlassdatum von dieser Schule. */
	@Schema(description = "Das Entlassdatum von dieser Schule.", example = "1902-03-11")
	@ValidDateFormat
	public JsonNullable<String> entlassdatumDieseSchule = JsonNullable.undefined();

	/** Die Id des Jahrgangs bei der Entlassung von dieser Schule. */
	@Schema(description = "Die Id des Jahrgangs bei der Entlassung von dieser Schule.", example = "3")
	public JsonNullable<Long> idEntlassjahrgangDieseSchule = JsonNullable.undefined();

	/** Die ID des Entlassgrundes von dieser Schule. */
	@Schema(description = "Die ID des Entlassgrundes von dieser Schule.", example = "1")
	public JsonNullable<Long> idEntlassgrundDieseSchule = JsonNullable.undefined();

	/** Die ID der Abschlussart, welcher an dieser Schule erworben wurde. */
	@Schema(description = "Die ID der Abschlussart, welcher an dieser Schule erworben wurde.", example = "OA")
	@Size(max = 2)
	public JsonNullable<String> idAbschlussartDieseSchule = JsonNullable.undefined();

	/** Die ID der aufnehmenden Schule nach einer Entlassung. */
	@Schema(description = "die ID der aufnehmenden Schule nach einer Entlassung", example = "12")
	public JsonNullable<Long> idAufnehmendeSchule = JsonNullable.undefined();

	/** Das Datum beim Wechsel zu einer aufnehmenden Schule. */
	@Schema(description = "das Datum beim Wechsel zu einer aufnehmenden Schule", example = "2020-01-01")
	@ValidDateFormat
	public JsonNullable<String> wechseldatumAufnehmendeSchule = JsonNullable.undefined();

	/** Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat. */
	@Schema(description = "gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat", example = "true")
	public JsonNullable<Boolean> wechselBestaetigtAufnehmendeSchule = JsonNullable.undefined();

	/** Das Jahr der Einschulung in die Grundschule. */
	@Schema(description = "Das Jahr der Einschulung in die Grundschule.", example = "2005")
	public JsonNullable<Integer> einschulungsjahrGrundschule = JsonNullable.undefined();

	/** Die ID der Einschulungsart in die Grundschule. */
	@Schema(description = "die ID der Einschulungsart in die Grundschule.", example = "51")
	public JsonNullable<Long> idEinschulungsartGrundschule = JsonNullable.undefined();

	/** Die ID der Schuleingangsphase der Grundschule. */
	@Schema(description = "die ID der Schuleingangsphase der Grundschule.", example = "2")
	public JsonNullable<Long> idEingangsphaseGrundschule = JsonNullable.undefined();

	/** Die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I */
	@Schema(description = "die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I", example = "1")
	public JsonNullable<Long> idUebergangsempfehlungGrundschule = JsonNullable.undefined();

	/** Das Jahr des Wechsels in die Sekundarstufe I. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe I.", example = "2009")
	public JsonNullable<Integer> wechseljahrSekI = JsonNullable.undefined();

	/** Das Kürzel der ersten Schulform in der Sekundarstufe I */
	@Schema(description = "das Kürzel der ersten Schulform in der Sekundarstufe I.", example = "GY")
	@Size(max = 10)
	public JsonNullable<String> kuerzelErsteSchulformSek1 = JsonNullable.undefined();

	/** Das Jahr des Wechsels in die Sekundarstufe II. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe II", example = "2017")
	public JsonNullable<Integer> wechseljahrSekII = JsonNullable.undefined();

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
