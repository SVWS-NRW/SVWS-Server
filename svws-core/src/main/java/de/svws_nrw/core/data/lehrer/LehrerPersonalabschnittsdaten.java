package de.svws_nrw.core.data.lehrer;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.lehrer.LehrerEinsatzstatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die abschnittsbezogenen Personaldaten eines Lehrers mit der
 * übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Personaldaten eines Lehrer-Eintrags.")
@TranspilerDTO
public class LehrerPersonalabschnittsdaten {

	/** Die ID des Abschnitts für den Lehrer in der Datenbank. */
	@Schema(description = "die ID des Lernabschnitts in der Datenbank", example = "126784")
	public long id;

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	public long idLehrer;

	/** Die ID des Schuljahresabschnitts, zu welchem diese Abschnittdaten gehören. */
	@Schema(description = "die ID des Schuljahresabschnitts, zu welchem diese Abschnittdaten gehören", example = "42")
	public long idSchuljahresabschnitt;

	/** Das Pflichtstundensoll des Lehrers. */
	@Schema(description = "Das Pflichtstundensoll des Lehrers.", example = "18.5")
	public Double pflichtstundensoll;

	/** Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog. */
	@Schema(description = "Das Rechtsverhältnis unter welchem der Lehrer beschäftigt ist (z.B. Beamter auf Lebenszeit) - siehe Statistik-Katalog.", example = "L")
	public String rechtsverhaeltnis;

	/** Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog. */
	@Schema(description = "Die Art der Beschäftigung (Vollzeit, Teilzeit, etc.) - siehe Statistik-Katalog.", example = "T")
	public String beschaeftigungsart;

	/** [ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Core-Type {@link LehrerEinsatzstatus} */
	@Schema(description = "[ASD] Der Einsatzstatus (z.B. Stammschule, nur hier tätig) - siehe Statistik-Katalog.", example = "*")
	public String einsatzstatus;

	/** Die Schulnummer der Stammschule, sofern diese abweicht. */
	@Schema(description = "Die Schulnummer der Stammschule, sofern diese abweicht.", example = "168890")
	public String stammschulnummer;


	// TODO Mehr- und Minder leistungen als Array mit weiteren DTO-Klassen
}
