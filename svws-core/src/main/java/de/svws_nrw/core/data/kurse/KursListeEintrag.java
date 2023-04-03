package de.svws_nrw.core.data.kurse;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten für die Kurse übergeben werden.  
 */
@XmlRootElement
@Schema(description="ein Eintrag eines Kurses in der Liste der Kurse.")
@TranspilerDTO
public class KursListeEintrag {

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses", example="4711")
	public long id;
	
	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(description = "die ID des Schuljahresabschnittes des Kurses", example="14")
	public long idSchuljahresabschnitt;
	
	/** Das Kürzel des Kurses. */
	@Schema(description = "das Kürzel des Kurses", example="IF-LK1")
	public @NotNull String kuerzel = "";
	
	/** Die IDs der Jahrgänge, denen der Kurs zugeordnet ist */
	@Schema(description = "die IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull Vector<@NotNull Long> idJahrgaenge = new Vector<>();
	
	/** Die ID des Faches, dem der Kurs zugeordnet ist */
	@Schema(description = "die ID des Faches, dem der Kurs zugeordnet ist", example="815")
	public long idFach;
	
	/** Die ID des Kurslehrers. */
	@Schema(description = "die ID des Kurslehrers", example="42")
	public Long lehrer;

	/** Die Schüler des Kurses. */
	public @NotNull List<@NotNull Schueler> schueler = new Vector<>();

	
	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example="1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;
		
}
