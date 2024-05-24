package de.svws_nrw.schuldatei.v1.data;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beschreibt einen Katalog-Eintrag eines Katalogs
 * der Schuldatei.
 */
@XmlRootElement
@Schema(description = "ein Katalog-Eintrag eines Katalogs der Schuldatei.")
@TranspilerDTO
public class SchuldateiKatalogeintrag extends SchuldateiEintrag {

    /** Der Katalog, welchem der Eintrag zugeordnet ist */
    @Schema(description = "der Katalog, welchem der Eintrag zugeordnet ist")
    public @NotNull String katalog = "";

    /** Der Schlüssel des Katalog-Eintrags */
    @Schema(description = "der Schlüssel des Katalog-Eintrags")
    public @NotNull String schluessel = "";

    /** Der Wert des Katalog-Eintrags */
    @Schema(description = "der Wert des Katalog-Eintrags")
    public @NotNull String wert = "";

    /** Die Bezeichnung */
    @Schema(description = "die Bezeichnung")
    public @NotNull String bezeichnung = "";


    /**
     * Erstellt einen neuen Eintrag zu einer Adresse einer Organisationseinheit der Schuldatei
     */
    public SchuldateiKatalogeintrag() {
        // Die Initialisierung mit Defaults erfolgt direkt über die Attribute
    }

}
