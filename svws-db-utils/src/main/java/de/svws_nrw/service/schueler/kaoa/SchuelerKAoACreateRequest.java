package de.svws_nrw.service.schueler.kaoa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Create-Request-DTO für das Anlegen von KAoA-Daten eines Schülers. Der Schüler ergibt sich aus dem Pfad des Endpunktes,
 * die ID wird beim Anlegen vergeben.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SchuelerKAoACreateRequest {

	/** Die ID des Schuljahresabschnitts, zu dem die KAoA-Daten gehören. */
	@Schema(description = "Der Schuljahresabschnitt, zu dem die KAoA-Daten gehören.", example = "42")
	@NotNull
	public Long idSchuljahresabschnitt;

	/** Die ID des Jahrgangs des Schülers. */
	@Schema(description = "Die ID des Jahrgangs des Schülers, zu dem die KAoA-Daten gehören.", example = "42")
	@NotNull
	public Long idJahrgang;

	/** Die ID der Kategorie. */
	@Schema(description = "Die Kategorie der KAoA-Daten.", example = "14")
	@NotNull
	public Long idKategorie;

	/** Die ID des Merkmals. */
	@Schema(description = "Das Merkmal der KAoA-Daten.", example = "69")
	@NotNull
	public Long idMerkmal;

	/** Die ID des Zusatzmerkmals. */
	@Schema(description = "Das Zusatzmerkmal der KAoA-Daten.", example = "117")
	@NotNull
	public Long idZusatzmerkmal;

	/** Die ID der Anschlussoption. */
	@Schema(description = "Die Anschlussoption der KAoA-Daten.", example = "22")
	public Long idAnschlussoption;

	/** Die ID des Berufsfelds. */
	@Schema(description = "Das Berufsfeld der KAoA-Daten.", example = "12")
	public Long idBerufsfeld;

	/** Die ID der Ebene 4. */
	@Schema(description = "Die Ebene 4 der KAoA-Daten.", example = "4")
	public Long idEbene4;

	/** Die Bemerkung zu den KAoA-Daten. */
	@Schema(description = "Die Bemerkung zu den KAoA-Daten.", example = "text")
	@Size(max = SchuelerKAoAValidator.MAX_LAENGE_BEMERKUNG, message = "Bemerkung darf nicht mehr als {max} Zeichen beinhalten.")
	public String bemerkung;

}
