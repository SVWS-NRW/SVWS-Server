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

	/** Die ID des Schulbesuchdatensatzes. */
	@Schema(description = "die ID des Schulbesuchdatensatzes", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Der Schlüssel des höchsten Schulabschlusses. */
	@Schema(description = "Der Schlüssel des höchsten Schulabschlusses.", example = "K")
	public String schluesselHoechsterSchulabschluss;

	/** Gibt an, ob ein Berufsabschluss vorhanden ist. */
	@Schema(description = "Gibt an, ob ein Berufsabschluss vorhanden ist.", example = "false")
	public boolean berufsabschlussVorhanden;

	/** Die ID der zuvor besuchten Schule. */
	@Schema(description = "die ID der zuvor besuchten Schule", example = "12")
	public Long idVorherigeSchule;

	/** Die ID der HerkunftSchulform (falls zuvor besuchte Schule = Sonstige Schule). */
	@Schema(description = "Die ID der HerkunftSchulform (falls zuvor besuchte Schule = Sonstige Schule).", example = "1000")
	public Long idHerkunftSchulformVorherigeSchule;

	/** Die ID der HerkunftSonstige (falls zuvor besuchte Schule = kein Schulbesuch). */
	@Schema(description = "Die ID der HerkunftSonstige (falls zuvor besuchte Schule = kein Schulbesuch).", example = "1000")
	public Long idHerkunftSonstigeVorherigeSchule;

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	@Schema(description = "Das Entlassdatum an der zuvor besuchten Schule", example = "1901-03-11")
	public String entlassdatumVorherigeSchule;

	/** Das Kürzel des Entlassjahrgangs an der zuvor besuchten Schule. */
	@Schema(description = "Das Kürzel des Entlassjahrgangs an der zuvor besuchten Schule.", example = "03")
	public String kuerzelEntlassjahrgangVorherigeSchule;

	/** Die ID der Herkunftsart der Versetzung an der zuvor besuchten Schule. */
	@Schema(description = "Die ID der Herkunftsart der Versetzung an der zuvor besuchten Schule.", example = "11")
	public String idHerkunftsartVersetzungVorherigeSchule;

	/** Bemerkungen zur zuvor besuchten Schule. */
	@Schema(description = "Bemerkungen zur zuvor besuchten Schule", example = "diverse")
	public String bemerkungVorherigeSchule;

	/** Die ID des Entlassgrundes der zuvor besuchten Schule. */
	@Schema(description = "Die ID des Entlassgrundes der zuvor besuchten Schule. ", example = "2")
	public Long idEntlassgrundVorherigeSchule;


	/** Der Schlüssel des Schulabschlusses (Allgemeinbildend) der zuvor besuchten Schule. */
	@Schema(description = "Der Schlüssel des Schulabschlusses (Allgemeinbildend) der zuvor besuchten Schule.", example = "B")
	public String schluesselAbschlussartAllgemeinbildendVorherigeSchule;

	/** Der Schlüssel des Schulabschlusses (Berufsbildend) der zuvor besuchten Schule. */
	@Schema(description = "Der Schlüssel des Schulabschlusses (Berufsbildend) der zuvor besuchten Schule.", example = "2")
	public String schluesselAbschlussartBerufsbildendVorherigeSchule;

	/** Die ID der Schulgliederung aus Herkunftbildungsgang.json (BK/SB) der zuvor besuchten Schule. */
	@Schema(description = "Die ID der Schulgliederung aus Herkunftbildungsgang.json (BK/SB) der zuvor besuchten Schule.", example = "2000")
	public Long idSchulgliederungVorherigeSchule;

	/** Der Schlüssel des CoreTypes der Fachklasse der zuvor besuchten Schule (BK/SB). */
	@Schema(description = "Der Schlüssel des CoreTypes der Fachklasse der zuvor besuchten Schule (BK/SB).", example = "170-10100")
	public String schluesselCoreTypeFachklasseVorherigeSchule;

	/** Die ID des Hochschulabschlusses aus Hochschulabschluss.json (BK/SB/WB). */
	@Schema(description = "Die ID des Hochschulabschlusses aus Hochschulabschluss.json (BK/SB/WB).", example = "2000")
	public Long idHochschulabschluss;

	/** Das Entlassdatum von dieser Schule. */
	@Schema(description = "Das Entlassdatum von dieser Schule.", example = "1902-03-11")
	public String entlassdatumDieseSchule;

	/** Die Id des Jahrgangs bei der Entlassung von dieser Schule. */
	@Schema(description = "Die Id des Jahrgangs bei der Entlassung von dieser Schule.", example = "3")
	public Long idEntlassjahrgangDieseSchule;

	/** Die ID des Entlassgrundes von dieser Schule. */
	@Schema(description = "Die ID des Entlassgrundes von dieser Schule.", example = "1")
	public Long idEntlassgrundDieseSchule;

	/** Die ID der Abschlussart, welcher an dieser Schule erworben wurde. */
	@Schema(description = "Die ID der Abschlussart, welcher an dieser Schule erworben wurde.", example = "OA")
	public String idAbschlussartDieseSchule;

	/** Die ID der aufnehmenden Schule. */
	@Schema(description = "die ID der aufnehmenden Schule.", example = "12")
	public Long idAufnehmendeSchule;

	/** Das Datum beim Wechsel zu einer aufnehmenden Schule. */
	@Schema(description = "das Datum beim Wechsel zu einer aufnehmenden Schule.", example = "2020-01-01")
	public String wechseldatumAufnehmendeSchule;

	/** Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat. */
	@Schema(description = "Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat.", example = "true")
	public boolean wechselBestaetigtAufnehmendeSchule;

	/** Das Jahr der Einschulung in die Grundschule. */
	@Schema(description = "Das Jahr der Einschulung in die Grundschule.", example = "2005")
	public Integer einschulungsjahrGrundschule;

	/** Die ID der Einschulungsart in die Grundschule. */
	@Schema(description = "die ID der Einschulungsart in die Grundschule.", example = "51")
	public Long idEinschulungsartGrundschule;

	/** Die ID der Schuleingangsphase der Grundschule. */
	@Schema(description = "die ID der Schuleingangsphase der Grundschule.", example = "2")
	public Long idEingangsphaseGrundschule;

	/** Die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I. */
	@Schema(description = "Die ID der Übergangsempfehlung der Grundschule in die Sekundarstufe I.", example = "1")
	public Long idUebergangsempfehlungGrundschule;

	/** Das Jahr des Wechsels in die Sekundarstufe I. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe I.", example = "2009")
	public Integer wechseljahrSekI;

	/** Das Kürzel der ersten Schulform in der Sekundarstufe I */
	@Schema(description = "das Kürzel der ersten Schulform in der Sekundarstufe I.", example = "GY")
	public String kuerzelErsteSchulformSek1;

	/** Das Jahr des Wechsels in die Sekundarstufe II. */
	@Schema(description = "das Jahr des Wechsels in die Sekundarstufe II.", example = "2017")
	public Integer wechseljahrSekII;

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

	/** Die Informationen zu den besonderen Merkmalen für die Statistik. */
	@ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchMerkmal.class,
			description = "Ein Array mit den Informationen zu den besonderen Merkmalen für die Statistik."))
	public @NotNull List<SchuelerSchulbesuchMerkmal> merkmale = new ArrayList<>();

	/** Die Informationen zu allen bisher besuchten Schulen. */
	@ArraySchema(schema = @Schema(implementation = SchuelerSchulbesuchSchule.class,
			description = "Ein Array mit den Informationen zu allen bisher besuchten Schulen."))
	public @NotNull List<SchuelerSchulbesuchSchule> bisherBesuchteSchulen = new ArrayList<>();

}
