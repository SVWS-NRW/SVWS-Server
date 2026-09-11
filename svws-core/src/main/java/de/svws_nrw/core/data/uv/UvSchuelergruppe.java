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
 * Sie liefert die Informationen zu einer UV-Planungsabschnitt-Schülergruppe.
 */
@XmlRootElement
@Schema(description = "die Informationen zu einer UV-Planungsabschnitt-Schülergruppe.")
@TranspilerDTO
public class UvSchuelergruppe {

    /** Die ID der Schülergruppe. */
    @Schema(description = "die ID der Schülergruppe", example = "815")
    public long id = -1;

	/** Die ID des Planungsabschnitts. */
	@Schema(description = "die ID des Planungsabschnitts", example = "815")
	public long idPlanungsabschnitt = -1;

	/** Die Bezeichnung der Schülergruppe. */
	@Schema(description = "die Bezeichnung der Schülergruppe.", example = "Religion 5ab")
	public @NotNull String bezeichnung = "";

	/** Ein Array mit den IDs der erlaubten Jahrgänge. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der erlaubten Jahrgänge."))
	public @NotNull List<Long> idsJahrgaengeErlaubt = new ArrayList<>();

	/** Ein Array mit den IDs der erlaubten Gruppen. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "ein Array mit den IDs der erlaubten Gruppen."))
	public @NotNull List<Long> idsGruppenErlaubt = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public UvSchuelergruppe() {
		// leer
	}

    /**
     * Gibt eine String-Repräsentation des UvSchuelergruppe-Objekts zurück.
     */
    @Override
    public String toString() {
		return "UvSchuelergruppe{id=" + id
				+ ", idPlanungsabschnitt=" + idPlanungsabschnitt
				+ ", bezeichnung=" + bezeichnung
				+ ", idsJahrgaengeErlaubt=" + idsJahrgaengeErlaubt
				+ ", idsGruppenErlaubt=" + idsGruppenErlaubt
				+ "}";
    }

    /**
     * Vergleicht, ob das aktuelle Objekt dasselbe ist wie ein anderes übergebenes Objekt.
     *
     * @param another das zu vergleichende Objekt
     * @return true, falls die Objekte identisch sind, sonst false
     */
    @Override
    public boolean equals(final Object another) {
        return (another instanceof final UvSchuelergruppe ano) && (this.id == ano.id);
    }

    /**
     * Erzeugt den Hashcode zum Objekt auf Basis des id-Attributs.
     *
     * @return den HashCode zum Objekt auf Basis des id-Attributs.
     */
    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

}
