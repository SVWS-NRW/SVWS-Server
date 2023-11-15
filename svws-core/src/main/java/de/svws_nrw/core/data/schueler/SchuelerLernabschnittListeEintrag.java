package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse enthält einen Listeneintrag für die Liste der Schüler-Lernabschnitte.
 */
@XmlRootElement
@Schema(description = "Ein Listeneintrag für die Liste der Schüler-Lernabschnitte.")
@TranspilerDTO
public class SchuelerLernabschnittListeEintrag {

	/** Die ID des Lernabschnitts in der Datenbank. */
	@Schema(description = "die ID des Lernabschnitts in der Datenbank", example = "126784")
	public long id;
	/** Die ID des Schülers, zu dem diese Lernabschnittdaten gehören. */
	@Schema(description = "die ID des Schülers, zu dem diese Lernabschnittdaten gehören", example = "4785")
	public long schuelerID;

	/** Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören. */
	@Schema(description = "die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören", example = "42")
	public long schuljahresabschnitt;

	/** Das Schuljahr, in welchem der Schuljahresabschnitt liegt */
	@Schema(description = "Das Schuljahr, in welchem der Abschnitt liegt", example = "2024")
	public int schuljahr;

	/** Die Nummer des Abschnitts im Schuljahr */
	@Schema(description = "Die Nummer des Abschnitts im Schuljahr", example = "2")
	public int abschnitt;

	/** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
	@Schema(description = "eine Nr, zur Unterscheidung von Lernabschnissdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, 0=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.)", example = "NULL")
	public int wechselNr = 0;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	@Schema(description = "gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht", example = "true")
	public boolean istGewertet = true;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	@Schema(description = "gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht", example = "false")
	public boolean istWiederholung = false;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	@Schema(description = "die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example = "APO-GOSt(B)10/G8")
	public @NotNull String pruefungsOrdnung = "";

	/** Die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist. */
	@Schema(description = "die ID der Klasse des Schülers oder null, falls keine Klasse zugeordnet ist", example = "46")
	public Long klassenID = null;

	/** Die Bezeichnung der Klasse des Schülers */
	@Schema(description = "die Bezeichnung der Klasse des Schülers", example = "7a")
	public @NotNull String klasse = "";

	/** Die Statistik-Bezeichnung der Klasse des Schülers */
	@Schema(description = "die Statistik-Bezeichnung der Klasse des Schülers", example = "07A")
	public @NotNull String klasseStatistik = "";

	/** Die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist */
	@Schema(description = "die ID des Jahrgangs des Schülers oder null, falls kein Jahrgang zugeordnet ist", example = "78")
	public Long jahrgangID = null;

	/** Die Statistik-Bezeichnung des Jahrgangs des Schülers */
	@Schema(description = "die Statistik-Bezeichnung des Jahrgangs des Schülers", example = "07")
	public @NotNull String jahrgang = "";

}
