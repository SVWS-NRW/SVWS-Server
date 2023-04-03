package de.svws_nrw.core.data.schueler;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-DTO für Katalog-Einträge zu der Übergangsempfehlung eines Schüler nach der 4. Klasse in die
 * 5. Klasse der Sekundarstufe I  
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Übergangsempfehlungen.")
@TranspilerDTO
public class UebergangsempfehlungKatalogEintrag {

	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id = -1;

	/** Das Kürzel der Empfehlung */
	@Schema(description = "das Kürzel der Empfehlung", example = "R")
	public @NotNull String kuerzel = "";

	/** Die Bezeichnung der Übergangsempfehlung. */
	@Schema(description = "die Bezeichnung der Übergangsempfehlung", example = "Realschule")
	public @NotNull String bezeichnung = "";

    /** Der Statistikschlüssel der Übergangsempfehlung */
    @Schema(description = "der Statistikschlüssel der Übergangsempfehlung", example = "10XX")
    public @NotNull String schluessel = "";

	/** Gibt an, in welchem Schuljahr der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem der Eintrag einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem der Eintrag gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public UebergangsempfehlungKatalogEintrag() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 * 
	 * @param id              die ID
	 * @param kuerzel         das Kürzel
	 * @param bezeichnung     die Bezeichnung
     * @param empfehlung      die Empfehlung
     * @param teilweise       die eingeschränkte Empfehlung
	 * @param gueltigVon      das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis      das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public UebergangsempfehlungKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String bezeichnung,
	        final Schulform empfehlung, final Schulform teilweise, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.bezeichnung = bezeichnung;
		if (empfehlung == null) {
		    this.schluessel = "****";
		} else {
		    this.schluessel = empfehlung.daten.nummer;
		    if (teilweise == null) {
	            this.schluessel += "XX";
	        } else { 
	            this.schluessel += teilweise.daten.nummer.toString();
	        }
		}
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
