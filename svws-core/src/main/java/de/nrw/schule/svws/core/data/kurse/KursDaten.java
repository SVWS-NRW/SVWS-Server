package de.nrw.schule.svws.core.data.kurse;

import java.util.List;
import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Kurses.  
 */
@XmlRootElement
@Schema(description="Die Daten eines Kurses.")
@TranspilerDTO
public class KursDaten {

	/** Die ID des Kurses. */
	@Schema(required = true, description = "die ID des Kurses", example="4711")
	public long id;
	
	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(required = true, description = "die ID des Schuljahresabschnittes des Kurses", example="14")
	public long idSchuljahresabschnitt;
	
	/** Das Kürzel des Kurses. */
	@Schema(required = true, description = "das Kürzel des Kurses", example="IF-LK1")
	public @NotNull String kuerzel = "";
	
	/** Die IDs der Jahrgänge, denen der Kurs zugeordnet ist */
	@Schema(required = true, description = "die IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull Vector<@NotNull Long> idJahrgaenge = new Vector<>();
	
	/** Die ID des Faches, dem der Kurs zugeordnet ist */
	@Schema(required = true, description = "die ID des Faches, dem der Kurs zugeordnet ist", example="815")
	public long idFach;
	
	/** Die ID des Kurslehrers. */
	@Schema(required = true, description = "die ID des Kurslehrers", example="42")
	public Long lehrer;

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(required = true, description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example="1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(required = true, description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example="true")
	public boolean istSichtbar;

	/** Die Schüler des Kurses. */
	public @NotNull List<@NotNull Schueler> schueler = new Vector<>();


	// TODO Weitere Daten	
}
