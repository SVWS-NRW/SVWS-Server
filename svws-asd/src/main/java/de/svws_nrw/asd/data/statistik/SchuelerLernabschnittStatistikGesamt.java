package de.svws_nrw.asd.data.statistik;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse liefert die allgemeinen Angaben zu dem Lernabschnitt eines Schülers zurück.
 */
@XmlRootElement
@Schema(description = "Die allgemeinen Angaben zu dem Lernabschnitt eines Schülers.")
@TranspilerDTO
public class SchuelerLernabschnittStatistikGesamt {

	/** Die ID des Lernabschnitts in der Datenbank. */
	@Schema(description = "die ID des Lernabschnitts in der Datenbank", example = "126784")
	public long id;

	/** Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören. */
	@Schema(description = "die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören", example = "42")
	public long idSchuljahresabschnitt;

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	@Schema(description = "die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist", example = "46")
	public Long idKlasse = null;

	/** Das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers. */
	@Schema(description = "das Kürzel der Schulgliederung bzw. des Bildungsgangs des Schülers", example = "B09")
	public String schulgliederung;

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist */
	@Schema(description = "die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist", example = "78")
	public Long idJahrgang = null;

	/** Die bisherige Anzahl der Jahre in der Schuleingangssphase */
	@Schema(description = "die bisherige Anzahl der Jahre in der Schuleingangssphase", example = "2")
	public Integer epJahre = null;

	/** Die ID der Fachklasse des Schülers an einem Berufskolleg */
	@Schema(description = "die ID der Fachklasse des Schülers an einem Berufskolleg", example = "null")
	public Long idFachklasse = null;

	/** Das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag - siehe Core-Type) */
	@Schema(description = "das Kürzel der Organisationsform der Schule in Bezug auf den Schüler (z.B. Ganztag)", example = "null")
	public String organisationsform = null;

	/** Das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse - siehe Core-Type) */
	@Schema(description = "das Kürzel der Klassenart in Bezug auf den Schüler (z.B. Regelklasse)", example = "null")
	public String Klassenart = "RK";

	/** Gibt an, ob eine Schwerbehinderung nachgewiesen ist oder nicht */
	@Schema(description = "gibt an, ob eine Schwerbehinderung nachgwiesen ist oder nicht", example = "false")
	public boolean hatSchwerbehinderungsNachweis = false;

	/** Die ID des Haupförderschwerpunktes des Schülers */
	@Schema(description = "die ID des Haupförderschwerpunktes des Schülers", example = "null")
	public Long idFoerderschwerpunkt1 = null;

	/** Die ID des weiteren Förderschwerpunktes des Schülers */
	@Schema(description = "die ID des weiteren Förderschwerpunktes des Schülers", example = "null")
	public Long idFoerderschwerpunkt2 = null;

	/** Das Kürzel des Versetzungsvermerks */
	@Schema(description = "das Kürzel des Versetzungsvermerks", example = "null")
	public String versetzungsvermerk = null;

	/**
	 * Leerer Standardkonstruktor.
	 */
	public SchuelerLernabschnittStatistikGesamt() {
		// leer
	}

}
