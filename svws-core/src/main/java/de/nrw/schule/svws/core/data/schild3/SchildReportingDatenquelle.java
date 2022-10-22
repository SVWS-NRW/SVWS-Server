package de.nrw.schule.svws.core.data.schild3;

import java.util.List;
import java.util.Vector;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse ist eine DTO-Klasse für eine Datenquelle für das 
 * Schild-Reporting
 */
@XmlRootElement
@Schema(description="Datenquellendefinition für das Schild-Reporting")
@TranspilerDTO
public class SchildReportingDatenquelle {

    /** Der Name der Datenquelle */
    @Schema(required = true, description = "der Name der Datenquelle", example = "SchülerLernabschnitte")
    public @NotNull String name = "";

    /** Die Beschreibung der Datenquelle */
    @Schema(required = true, description = "die Beschreibung der Datenquelle", example = "Die Lernabschnitte eines Schülers")
    public @NotNull String beschreibung = "";
    
    /** Der Name der Master-Datenquelle */
    @Schema(required = false, description = "der Name der Master-Datenquelle", example = "Schueler")
    public String master = null;
    
    /** Der Name des identifizierenden Attributs der Master-Datenquelle */
    @Schema(required = true, description = "der Name des identifizierenden Attributs der Master-Datenquelle", example = "id")
    public String masterattribut = null;

    /** Der Typ des Attributs der Master-Datenquelle (z.B. die ID) */
    @Schema(required = true, description = "der Typ des Attributs der Master-Datenquelle (z.B. die ID)", example = "integer")
    public String mastertyp = null;

    /** Der Name des Attributs dieser Datenquelle, welches für die Verbindung zu der Master-Datenquelle genutzt wird */
    @Schema(required = true, description = "der Name des Attributs dieser Datenquelle, welches für die Verbindung zu der Master-Datenquelle genutzt wird", example = "schuelerID")
    public String linkattribut = null; 

    /** Die Liste der JSON-Attribute für diese Datenquelle. */
    @ArraySchema(schema = @Schema(required = true, implementation = SchildReportingDatenquelleAttribut.class, 
            description = "Die Liste der JSON-Attribute für diese Datenquelle."))
    public @NotNull List<@NotNull SchildReportingDatenquelleAttribut> attribute = new Vector<>();

}
