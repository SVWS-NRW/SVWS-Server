package de.nrw.schule.svws.core.data.schueler;

import java.util.Vector;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt eine Auswahl von Daten eines Schülereintrags aus einer Liste.  
 */
@XmlRootElement
@Schema(description="ein Eintrag eines Schülers in der Schülerliste.")
@TranspilerDTO
public class SchuelerListeEintrag {

	/** Die ID des Schülers. */
	@Schema(required = true, description = "die ID des Schülers", example="4711")
	public long id;
	
	/** Der Nachname des Schülers. */
	@Schema(required = true, description = "der Nachname des Schülers", example="Mustermann")
	public @NotNull String nachname = "";
	
	/** Der Vorname des Schülers. */
	@Schema(required = true, description = "der Vorname des Schülers", example="Max")
	public @NotNull String vorname = "";

	/** Die ID der aktuellen Klasse des Schülers.*/
	@Schema(required = true, description = "die ID der aktuellen Klasse des Schülers", example="47")
	public @NotNull Long idKlasse = -1L;

	/** Der aktuelle Jahrgang des Schülers.*/
	@Schema(required = true, description = "der aktuelle Jahrgang des Schülers", example="09")
	public @NotNull String jahrgang = "";
	
	/** Das Kürzel der aktuellen Schulgliederung des Schülers */
	@Schema(required = true, description = "das Kürzel der aktuellen Schulgliederung des Schülers", example="GY9")
	public @NotNull String schulgliederung = "";
	
	/** Die Bezeichnung des Status des Schülers (Aktiv, Extern, etc.).*/
	@Schema(required = true, description = "die Bezeichnung des Status des Schülers (Aktiv, Extern, etc.)", example="Aktiv")
	public @NotNull String status = "";
	
	/** Die ID des Schuljahresabschnittes des Schülers. */
	@Schema(required = true, description = "die ID des Schuljahresabschnittes des Schülers", example="14")
	public @NotNull Long idSchuljahresabschnitt = -1L;

	/** Die Liste der IDs der belegten Kurse im aktuellen Abschnit  */
	@ArraySchema(schema = @Schema(implementation = Long.class))
	public final @NotNull Vector<@NotNull Long> kurse = new Vector<>();
	
}
