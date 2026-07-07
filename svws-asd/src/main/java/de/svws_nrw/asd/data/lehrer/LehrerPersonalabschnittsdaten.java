package de.svws_nrw.asd.data.lehrer;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die abschnittsbezogenen Personaldaten eines Lehrers mit der übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Die Personaldaten eines Lehrer-Eintrags.")
@TranspilerDTO
public class LehrerPersonalabschnittsdaten {

	/** ID der LehrerPersonalabschnittsdaten. */
	@Schema(description = "ID der LehrerPersonalabschnittsdaten.", example = "126784")
	public long id;

	/** ID des Lehrers. */
	@Schema(description = "ID des Lehrers.", example = "4711")
	public long idLehrer;

	/** ID des Schuljahresabschnitts zu diesen Abschnittsdaten. */
	@Schema(description = "ID des Schuljahresabschnitts zu diesen Abschnittsdaten.", example = "1")
	public long idSchuljahresabschnitt;

	/** Pflichtstundensoll des Lehrers. */
	@Schema(description = "Pflichtstundensoll des Lehrers.", example = "18.5")
	public Double pflichtstundensoll;

	/** ID des Rechtsverhältnisses des Lehrers (z.B. Beamter auf Lebenszeit). */
	@Schema(description = "ID des Rechtsverhältnisses des Lehrers (z.B. Beamter auf Lebenszeit).", example = "1")
	public Long idRechtsverhaeltnis;

	/** ID der Beschäftigungsart (z.B. Vollzeit, Teilzeit). */
	@Schema(description = "ID der Beschäftigungsart (z.B. Vollzeit, Teilzeit).", example = "1")
	public Long idBeschaeftigungsart;

	/** ID des Einsatzstatus (z.B. Stammschule, nur hier tätig). */
	@Schema(description = "ID des Einsatzstatus (z.B. Stammschule, nur hier tätig).", example = "1")
	public Long idEinsatzstatus;

	/** Schulnummer der Stammschule, falls abweichend. */
	@Schema(description = "Schulnummer der Stammschule, falls abweichend.", example = "168890")
	public String stammschulnummer;

	/** Allgemeine Anrechnungsstunden zu den Abschnittsdaten des Lehrers. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Allgemeine Anrechnungsstunden zu den Abschnittsdaten des Lehrers."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> anrechnungen = new ArrayList<>();

	/** Mehrleistungsstunden (Mehrarbeit) zum Pflichtstundensoll in den Abschnittsdaten des Lehrers. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Mehrleistungsstunden (Mehrarbeit) zum Pflichtstundensoll in den Abschnittsdaten des Lehrers."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> mehrleistung = new ArrayList<>();

	/** Minderleistungsstunden (Minderarbeit) vom Pflichtstundensoll in den Abschnittsdaten des Lehrers. */
	@ArraySchema(schema = @Schema(implementation = LehrerPersonalabschnittsdatenAnrechnungsstunden.class,
			description = "Minderleistungsstunden (Minderarbeit) vom Pflichtstundensoll in den Abschnittsdaten des Lehrers."))
	public final @NotNull List<LehrerPersonalabschnittsdatenAnrechnungsstunden> minderleistung = new ArrayList<>();

	/** Schulspezifische Funktionen des Lehrers in diesem Abschnitt. */
	@ArraySchema(schema = @Schema(implementation = LehrerFunktion.class,
			description = "Schulspezifische Funktionen des Lehrers in diesem Abschnitt."))
	public final @NotNull List<LehrerFunktion> funktionen = new ArrayList<>();

}
