package de.svws_nrw.asd.data.statistik;

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
public class SchuelerStatistikGesamt {

	/** Die ID des Schülerdatensatzes. */
	@Schema(description = "die ID des Schülerdatensatzes", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Die ID des Geschlechtes */
	@Schema(description = "die ID des Geschlechtes", example = "3")
	public int geschlecht;

	/** Das Geburtsdatum des Schülerdatensatzes. */
	@Schema(description = "das Geburtsdatum", example = "11.11.1911")
	public String geburtsdatum;

	/** Die ID des Wohnortes des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID des Wohnortes", example = "4711")
	public Long wohnortID;

	/** Die ID der Staatsangehörigkeit des Schülerdatensatzes. */
	@Schema(description = "die ID der Staatsangehörigkeit", example = "000")
	public String staatsangehoerigkeitID;

	/** Die ID der Religion des Schülerdatensatzes. */
	@Schema(description = "ggf. die ID der Religion", example = "4711")
	public Long religionID;

	/** Die ID des Status des Schülerdatensatzes. */
	@Schema(description = "die ID des aktuellen Schülerstatus", example = "2")
	public int status;

	/** Das Datum der Religionsabmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsabmeldung", example = "11.11.1911")
	public String religionabmeldung;

	/** Das Datum der Religionsanmeldung des Schülerdatensatzes. */
	@Schema(description = "das Datum der Religionsanmeldung", example = "12.12.1912")
	public String religionanmeldung;

	/** Die Anrechungszeit in Monaten für den Beginn des Bildungsganges des Berufskolleg (z.B. 0,6,12,18). */
	@Schema(description = "die Anrechungszeit in Monaten für den Beginn des Berufskolleg (z.B. 0,6,12,18)", example = "12")
	public Integer bkAvzo;

	// **** Daten zum Migrationshintergrund

	/** Gibt an, ob ein Migrationshintergrund bei dems Schülerdatensatz vorhanden ist. */
	@Schema(description = "gibt an, ob ein Migrationshintergrund vorhanden ist", example = "true")
	public boolean hatMigrationshintergrund;

	/** Das Zuzugsjahr des Schülerdatensatzes. */
	@Schema(description = "ggf. das Zuzugsjahr", example = "2013")
	public Integer zuzugsjahr;

	/** Das Geburtsland des Schülerdatensatzes. */
	@Schema(description = "ggf. das Geburtsland", example = "Brasilien")
	public String geburtsland;

	/** Die Verkehrssprache der Familie des Schülerdatensatzes. */
	@Schema(description = "ggf. die in der Familie hauptsächlich gesprochen Sprache", example = "Portugiesisch")
	public String verkehrspracheFamilie;

	/** Das Geburtsland des Vaters des Schülerdatensatzes. */
	@Schema(description = "ggf. das Geburtsland des Vaters", example = "Argentinien")
	public String geburtslandVater;

	/** Das Geburtsland der Mutter des Schülerdatensatzes. */
	@Schema(description = "ggf. das Geburtsland der Mutter", example = "Brasilien")
	public String geburtslandMutter;

	// ****

	/** Die allgemeinen Angaben zu den Lernabschnitten der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerLernabschnittStatistikGesamt.class,
			description = "Ein Array mit den allgemeinen Angaben zu den Lernabschnitten der Schüler."))
	public @NotNull List<SchuelerLernabschnittStatistikGesamt> lernabschnitte = new ArrayList<>();

	// **** Informationen zu der Schule, die vor der Aufnahme besucht wurde

	/** Die Schulnr der vorher besuchten Schule. */
	@Schema(description = "Die Schulnr der vorher besuchten Schule", example = "123456")
	public String vorherigeSchuleNr;

	/** Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule. */
	@Schema(description = "die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule",
			example = "Grundschule (auch Primarstufe der Volkschule)")
	public String vorigeAllgHerkunft;

	/** Die ID der Art der letzten Versetzung an der zuvor besuchten Schule. */
	@Schema(description = "die Art der letzten Versetzung an der zuvor besuchten Schule", example = "11")
	public String vorigeArtLetzteVersetzung;

	/** Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde", example = "OA")
	public String idVorigeAbschlussart;

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	@Schema(description = "das Entlassdatum an der zuvor besuchten Schule", example = "1901-03-11")
	public String vorigeEntlassdatum;

	/** Der Entlassjahrgang an der zuvor besuchten Schule. */
	@Schema(description = "der Entlassjahrgang an der zuvor besuchten Schule", example = "03")
	public String vorigeEntlassjahrgang;

	/** Das Entlassdatum von dieser Schule. */
	@Schema(description = "das Entlassdatum von dieser Schule", example = "1902-03-11")
	public String entlassungDatum;

	/** Die ID des Abschlusses, welcher an dieser Schule erworben wurde. */
	@Schema(description = "die ID des Abschlusses, welcher an dieser Schule erworben wurde", example = "OA")
	public String idEntlassungAbschlussart;

	/** Ist Schüler einer Justizvollzugsanstalt. */
	@Schema(description = "ist Schüler einer Justizvollzugsanstalt", example = "True")
	public boolean istJvaSchueler;

	// TODO Nur BK, SB: boolean Wert, ob es sich um einen Ausbildungsbetrieb handelt.
	// TODO Nur BK, SB: boolean Wert, ob es sich um einen Träger handelt.

	// **** Informationen zu der besuchten Grundschule

	/** Die ID der Einschulungsart in die Grundschule. */
	@Schema(description = "die ID der Einschulungsart in die Grundschule", example = "51")
	public Long idGrundschuleEinschulungsart;

	/** Das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I */
	@Schema(description = "das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I", example = "GY")
	public String kuerzelGrundschuleUebergangsempfehlung;

	// **** Ggf. Informationen zum Abitur

	/** Die Daten zum Abitur (sofern vorhanden). */
	@Schema(description = "die Daten zum Abitur (sofern vorhanden)")
	public AbiturStatistikGesamt abitur;

	// TODO Informationen zu besonderen Merkmalen für die Statistik

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerStatistikGesamt() {
		// leer
	}

}
