package de.svws_nrw.asd.data.statistik;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 */
@XmlRootElement
@Schema(description = "Die Stammdaten eines Lehrer-Eintrags.")
@TranspilerDTO
public class LehrerStatistikGesamt {

	//**** Nicht Abschnittsbezogene Daten

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Das Kürzel des Lehrers. */
	@Schema(description = "Das Kürzel des Lehrers.", example = "MUS")
	public @NotNull String kuerzel = "";

	/** Der Vorname des Lehrers. */
	@Schema(description = "Der Vorname des Lehrers.", example = "Max")
	public @NotNull String vorname = "";

	/** Der Nachname des Lehrers. */
	@Schema(description = "Der Nachname des Lehrers.", example = "Mustermann")
	public @NotNull String nachname = "";

	/** Das Geburtsdatum des Lehrers. */
	@Schema(description = "Das Geburtsdatum des Lehrers.", example = "1911-11-11")
	public String geburtsdatum;

	/** Die ID des Geschlechtes */
	@Schema(description = "die ID des Geschlechtes", example = "3")
	public int geschlecht;

	/** Ggf. die ID für die Staatsangehörigkeit des Lehrers. */
	@Schema(description = "Ggf. die ID für die Staatsangehörigkeit des Lehrers.", example = "000")
	public String staatsangehoerigkeitID;

	/** Die Lehrämter des Lehrers. */
	@ArraySchema(schema = @Schema(implementation = LehrerLehramtEintrag.class, description = "Ein Array mit den Lehrämtern des Lehrers."))
	public final @NotNull List<LehrerLehramtEintrag> lehraemter = new ArrayList<>();

	//**** Abschnittsbezogene Daten

	/** Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog. */
	@Schema(description = "Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.",
			example = "L")
	public String rechtsverhaeltnis;

	/** Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog. */
	@Schema(description = "Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.", example = "T")
	public String beschaeftigungsart;

	/** [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) */
	@Schema(description = "[ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Statistik-Katalog. Ein leerer Eintrag wird als DEFAULT interpretiert, und bedeutet \"Nur an Stammschule tätig.\"",
			example = "A")
	public String einsatzstatus;

	/** Das Pflichtstundensoll des Lehrers. */
	@Schema(description = "Das Pflichtstundensoll des Lehrers.", example = "18.5")
	public Double pflichtstundensoll;

	/** Die allgemeinen Anrechnungsstunden, die den Abschnittsdaten des Lehrers zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Ein Array mit den allgemeinen Anrechnungsstunden, die den Abschnittsdaten des Lehrers zugeordnet sind."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen = new ArrayList<>();

	/** Die Stunden, welche Mehrarbeitsgründe haben, dem Pflichtstundensoll hinzuzufügen sind und die den Abschnittsdaten des Lehrers zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Ein Array mit den Stunden, welche Mehrarbeitsgründe haben, dem Pflichtstundensoll hinzuzufügen sind und die den Abschnittsdaten des Lehrers zugeordnet sind."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung = new ArrayList<>();

	/** Die Stunden, welche Minderarbeitsgründe haben, dem Pflichtstundensoll wegzunehmen sind und die den Abschnittsdaten des Lehrers zugeordnet sind. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Ein Array mit den Stunden, welche Minderarbeitsgründe haben, dem Pflichtstundensoll wegzunehmen sind und die den Abschnittsdaten des Lehrers zugeordnet sind."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerStatistikGesamt() {
		// leer
	}

}
