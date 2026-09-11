package de.svws_nrw.core.data.uv;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu einer Schiene innerhalb eines UV-Planungsabschnitts.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer Schiene innerhalb eines UV-Planungsabschnitts.")
@TranspilerDTO
public class UvSchiene {

	/** Die ID der Schiene (eindeutig, planungsspezifisch). */
	@Schema(description = "die ID der Schiene (eindeutig, planungsspezifisch)", example = "4711")
	public long id = -1;

	/** Die ID des Planungsabschnitts, zu dem die Schiene gehört. */
	@Schema(description = "die ID des Planungsabschnitts, zu dem die Schiene gehört", example = "102")
	public long idPlanungsabschnitt = -1;

	/** Die laufende Nummer der Schiene innerhalb des Planungsabschnitts. */
	@Schema(description = "die laufende Nummer der Schiene innerhalb des Planungsabschnitts", example = "3")
	public int nummer = 0;

	/** Die Bezeichnung der Schiene. */
	@Schema(description = "die Bezeichnung der Schiene", example = "Schiene A")
	public String bezeichnung = null;

	/** Ein Array mit den IDs der erlaubten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der erlaubten Jahrgänge."))
	public @NotNull List<Long> idsJahrgaengeErlaubt = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvSchiene() {
		// leer
	}

	/**
	 * Gibt eine String-Repräsentation des Objekts zurück.
	 *
	 * @return die String-Darstellung der Schiene
	 */
	@Override
	public String toString() {
		return "UvSchiene{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", nummer=" + nummer
				+ ", bezeichnung=" + bezeichnung
				+ ", idsJahrgaengeErlaubt=" + idsJahrgaengeErlaubt
				+ "}";
	}

	/**
	 * Erzeugt den Hashcode zum Objekt auf Basis der ID.
	 *
	 * @return der HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	/**
	 * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
	 *
	 * @param obj das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object obj) {
		return (obj instanceof final UvSchiene other) && (this.id == other.id);
	}

}
