package de.nrw.schule.svws.core.data.schueler;

import javax.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse enthält einen Listeneintrag für die Liste der Schüler-Lernabschnitte.  
 */
@XmlRootElement
@Schema(description="Ein Listeneintrag für die Liste der Schüler-Lernabschnitte.")
@TranspilerDTO
public class SchuelerLernabschnittListeEintrag {

	/** Die ID des Lernabschnitts in der Datenbank. */
	@Schema(required = true, description = "die ID des Lernabschnitts in der Datenbank", example="126784")
	public long id;
	
	/** Die ID des Schülers, zu dem diese Lernabschnittdaten gehören. */
	@Schema(required = true, description = "die ID des Schülers, zu dem diese Lernabschnittdaten gehören", example="4785")
	public long schuelerID;

	/** Die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören. */
	@Schema(required = true, description = "die ID des Schuljahresabschnitts, zu welchem diese Lernabschnittdaten gehören", example="42")
	public long schuljahresabschnitt;
	
	/** Eine Nr, zur Unterscheidung von Lernabschnittsdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.). */
	@Schema(required = false, description = "eine Nr, zur Unterscheidung von Lernabschnissdaten, wenn beim Schüler mehrere Lernabschnitt in einem Schuljahresabschnitt vorliegen (z.B. Wechsel einer Klasse, NULL=aktueller Abschnitt, 1=vor dem ersten Wechsel, 2=vor dem zweiten Wechsel, usw.)", example="NULL")	
	public Integer wechselNr = null;

	/** Gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht */
	@Schema(required = true, description = "gibt an, ob es sich um einen gewerteten Abschnitt handelt oder nicht", example="true")	
	public boolean istGewertet = true;

	/** Gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht */
	@Schema(required = true, description = "gibt an, ob es sich bei dem Abschnitt um einen wiederholten Abschnitt handelt oder nicht", example="false")	
	public boolean istWiederholung = false;

	/** Die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist. */
	@Schema(required = true, description = "die Prüfungsordnung, die in dem Lernabschnitt bei dem Schüler anzuwenden ist.", example="APO-GOSt(B)10/G8")
	public @NotNull String pruefungsOrdnung = "";

	/** Die ID der Klasse des Schülers. */
	@Schema(required = true, description = "die ID der Klasse des Schülers", example="46")
	public long klassenID = -1;

	/** Die Bezeichnung der Klasse des Schülers */
	@Schema(required = true, description = "die Bezeichnung der Klasse des Schülers", example="7a")	
	public @NotNull String klasse = "";

	/** Die Statistik-Bezeichnung der Klasse des Schülers */
	@Schema(required = true, description = "die Statistik-Bezeichnung der Klasse des Schülers", example="07A")	
	public @NotNull String klasseStatistik = "";

	/** Die ID des Jahrgangs des Schülers */
	@Schema(required = true, description = "die ID des Jahrgangs des Schülers", example="78")
	public long jahrgangID = -1;
	
	/** Die Statistik-Bezeichnung des Jahrgangs des Schülers */
	@Schema(required = true, description = "die Statistik-Bezeichnung des Jahrgangs des Schülers", example="07")	
	public @NotNull String jahrgang = "";

}
