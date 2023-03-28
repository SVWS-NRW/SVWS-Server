package de.svws_nrw.core.data.benutzer;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenzGruppe;
import de.svws_nrw.core.types.schule.Schulform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt den Katalog der Benutzerkompetenzen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag im Katalog der Benutzerkompetenzen.")
@TranspilerDTO
public class BenutzerKompetenzKatalogEintrag {

	/** Die ID der Benutzerkompetenz. */
	@Schema(description = "die ID der Benutzerkompetenz", example = "11")
	public long id = -1;

	/** Die ID der zugehörigen Benutzerkompetenzgruppe. */
	@Schema(description = "die ID der zugehörigen Benutzerkompetenzgruppe", example = "100")
	public @NotNull long gruppe_id = -1;

	/** Die Bezeichnung der Benutzerkompetenz. */
	@Schema(description = "die Bezeichnung der Benutzerkompetenz", example = "Ansehen")
	public @NotNull String bezeichnung = "";
	
	/** Die Schulformen. */
    @Schema(required = true, description = "die Schulformen, bei denen die Kompetenz zulässig ist", example="Liste")
    public List<@NotNull Long> nurSchulformen = null;
	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public BenutzerKompetenzKatalogEintrag() {
	}


	
	
	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id             die ID
	 * @param gruppe         die Gruppe, welcher die Benutzerkompetenz zugeordnet ist
	 * @param bezeichnung    die Bezeichnung der Benutzerkompetenz
	 * @param schulformen    die Schulformen, bei denen die Kompetenz zulässig ist.
	 */
	public BenutzerKompetenzKatalogEintrag(final long id, final @NotNull BenutzerKompetenzGruppe gruppe, final @NotNull String bezeichnung,  final List<@NotNull Schulform> schulformen ) {
		this.id = id;
		this.bezeichnung = bezeichnung;
		this.gruppe_id = gruppe.daten.id; 
		//this.nurSchulformen = schulformen;
		if(schulformen != null) {
		  this.nurSchulformen =new Vector<>();
		  for (final @NotNull Schulform schulform : schulformen)
	            this.nurSchulformen.add(schulform.daten.id);
		}
	}
	
	
	
	
	
	
	
	
	
	
}
