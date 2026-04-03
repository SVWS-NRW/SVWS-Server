package de.svws_nrw.asd.export.data;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird für den Export der Schuldaten (B01) verwendet.
 *
 */
@XmlRootElement
@Schema(description = "Die Daten einer Schule.")
@TranspilerDTO
public class SchuleStatistikExport {

	/** Die eindeutige Schulnummer der Schule */
	@Schema(description = "die Schulnummer der Schule", example = "123456")
	public long schulNr = -1;

	/** Die Schulform der Schule */
	@Schema(description = "das Kürzel der Schulform", example = "GY")
	public @NotNull String schulform = "";

	/** Der erste Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 1 der Schule", example = "Städt. Gymnasium")
	public @NotNull String bezeichnung1 = "";

	/** Der zweite Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 2 der Schule", example = "der Stadt Wuppertal")
	public String bezeichnung2 = "";

	/** Der dritte Teil (von dreien) der Bezeichnung der Schule */
	@Schema(description = "die Bezeichnung 3 der Schule", example = "Albert Einstein")
	public String bezeichnung3 = "";

	/** Der Straßenname der Straße in der die Schule liegt. */
	@Schema(description = "der Straßenname der Straße in der die Schule liegt", example = "Musterweg 123a")
	public String strassenname = "";

	/** Die Postleitzahl des Gebietes in dem die Schule liegt. */
	@Schema(description = "die Postleitzahl der Schule", example = "42287")
	public String plz = "";

	/** Der Ort in dem die Schule liegt. */
	@Schema(description = "der Ort der Schule", example = "Düsseldorf")
	public String ort = "";

	/** Die Telefonnummer der Schule. */
	@Schema(description = "die Telefonnummer der Schule", example = "0211-58670")
	public String telefon = "";

	/** Die Faxnummer der Schule. */
	@Schema(description = "die Faxnummer der Schule", example = "0211-58671")
	public String fax = "";

	/** Die Mailadresse der Schule. */
	@Schema(description = "die Mailadresse der Schule", example = "info@schule.de")
	public String email = "";

	/** Die Adresse der Homepage der Schule (Domain-Name)*/
	@Schema(description = "die Adresse der Homepage der Schule", example = "www.schule.de")
	public String webAdresse = "";

	/** Das Zeitmodell (Unterrichtsstunden- (1) oder Unterrichtsminutenmodell (45)). */
	@Schema(description = "Das Zeitmodel (Unterrichtsstunden- (1) oder Unterrichtsminutenmodell (45))", example = "1")
	public int zeitmodel = 45;

	/** Gebundener Ganztag */
	//TODO Nicht im CopySet...
	@Schema(description = "Gebundener Ganztag", example = "1")
	public int gebundenerGanztag = 0;

	/** Offener Ganztag */
	@Schema(description = "Offener Ganztag", example = "false")
	public boolean istOffenerGanztag = false;

	/** Die Form des offenen Ganztag */
	//TODO Nicht im CopySet...
	@Schema(description = "Die Form des offenen Ganztag", example = "2")
	public String formOffenerGanztag = "";

	/** Ist JVA */
	@Schema(description = "Ist JVA", example = "false")
	public boolean istJva = false;

	/** Bilingualer Unterricht*/
	@Schema(description = "Bilingualer Unterricht", example = "2")
	public int bilingualerUnterricht = 0;

	/** Hat Realschule Hauptbildungsgang */
	@Schema(description = "Hat Realschule Hauptbildungsgang", example = "false")
	public boolean hatRealschuleHauptschulbildungsgang = false;

	/** Hat die Schule internationale Kontakte */
	@Schema(description = "Hat die Schule internationale Kontakte", example = "false")
	public boolean hatInternationaleKontakte = false;

	/** Hat die Schule eine konfessionelle Kooperation */
	@Schema(description = "Hat die Schule eine konfessionelle Kooperation", example = "false")
	public boolean hatKonfessionelleKooperation = false;

	/** Talentschule Form */
	@Schema(description = "Talentschule Form", example = "0")
	public int talentschule = 0;

	/** Reformpädagogik Schulebene */
	@Schema(description = "Reformpedagogik Schulebene", example = "M")
	public String reformpaedagogik = "";

	/** Die Adressen einer Schule (B02). */
	@ArraySchema(schema = @Schema(implementation = SchuleAdressenStatistikExport.class,
			description = "die Adressen einer Schule (B02)"))
	public @NotNull List<SchuleAdressenStatistikExport> adressenStatistikExport = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuleStatistikExport() {
		// leer
	}

}
