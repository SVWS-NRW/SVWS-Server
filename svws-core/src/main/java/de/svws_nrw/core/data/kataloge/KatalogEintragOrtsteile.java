package de.svws_nrw.core.data.kataloge;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für den Ortsteil-Katalog von IT.NRW. Der
 * Katalog wird in den Java-Resourcen unter "daten/csv/Ortsteile.csv"
 * erwartet.
 */
@JsonPropertyOrder({"ID","PLZ","RegSchl","Ort","Ortsteil","Stand","gueltigVon","gueltigBis"})
@XmlRootElement
@Schema(description="ein Eintrag in dem IT.NRW-Katalog der Ortsteile.")
@TranspilerDTO
public class KatalogEintragOrtsteile {

	/** Katalog von IT.NRW PLZ Ortsteile: die ID des Katalog-Eintrags */
	@Schema(required = true, description = "der ID des Katalog-Eintrags", example="44012")
	public long ID = -1;

	/** Katalog von IT.NRW PLZ Ortsteile: die Postleitzahl */
	@Schema(required = true, description = "die Postleitzahl", example="32001")
	public @NotNull String PLZ = "";

    /** Katalog von IT.NRW PLZ Ortsteile: der Regionalschlüssel */
    @Schema(required = true, description = "der Regionalschlüssel", example="05758012")
    public @NotNull String RegSchl = "";

	/** Katalog von IT.NRW PLZ Ortsteile: die Ortsbezeichnung */
	@Schema(required = true, description = "die Ortsbezeichnung", example="Herford")
	public @NotNull String Ort = "";

	/** Katalog von IT.NRW PLZ Ortsteile: die Bezeichnung des Ortsteils */
	@Schema(required = true, description = "die Bezeichnung des Ortsteils", example="Unterherford")
	public @NotNull String Ortsteil = "";

    /** Katalog von IT.NRW PLZ Ortsteile: der Stand des Katalog-Eintrags */
    @Schema(required = true, description = "der Stand des Katalog-Eintrags", example="29.03.2022")
    public @NotNull String Stand = "";

    /** Katalog von IT.NRW PLZ Ortsteile: Gibt die Gültigkeit ab welchem Schuljahr an */
    @Schema(required = false, description = "gibt die Gültigkeit ab welchem Schuljahr an", example="null")
    public Integer gueltigVon = null;

    /** Katalog von IT.NRW PLZ Ortsteile: Gibt die Gültigkeit bis zu welchem Schuljahr an */
    @Schema(required = false, description = "gibt die Gültigkeit bis zu welchem Schuljahr an", example="null")
    public Integer gueltigBis = null;

}
