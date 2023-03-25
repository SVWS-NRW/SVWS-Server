package de.svws_nrw.core.data.schueler;


import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse liefert die KAOA Daten eines Lernabschnitts eines Schülers zurück.
 */
@XmlRootElement
@Schema(description="Die KAOA Daten eines Lernabschnitts eines Schülers.")
@TranspilerDTO
public class SchuelerKAoADaten {

	/** Die ID der KAOA Daten in der Datenbank. */
	@Schema(required = true, description = "Die ID der KAOA Daten in der Datenbank", example="126784")
	public Long id = -1L;

	/** Der Lernabschnitts des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = true, description = "Der Lernabschnitts des Schülers, zu dem diese KAOA Daten gehören.", example = "7522")
	@NotNull
	public  Long abschnitt = -1L;

	/** Der Jahrgaeng des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = true, description = "Der Jahrgaeng des Schülers, zu dem diese KAOA Daten gehören.", example = "09")
	@NotNull
	public String jahrgang = "";

	/** Der Kategorie des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = true, description = "Der Kategorie des Schülers, zu dem diese KAOA Daten gehören.", example = "14")
	@NotNull
	public Long kategorie = -1L;

	/** Das Merkmal des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = true, description = "Das Merkmal des Schülers, zu dem diese KAOA Daten gehören.", example = "69")
	@NotNull
	public Long merkmal = -1L;

	/** Das Zusatzmerkmal des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = true, description = "Das Zusatzmerkmal des Schülers, zu dem diese KAOA Daten gehören.", example = "117")
	public Long zusatzmerkmal;

	/** Die Anschlussoption des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = false, description = "Die Anschlussoption des Schülers, zu dem diese KAOA Daten gehören.", example = "22")
	public Long anschlussoption;

	/** Das Berufsfeld des Schülers, zu dem diese KAOA Daten gehören. */
	@Schema(required = false, description = "Das Berufsfeld des Schülers, zu dem diese KAOA Daten gehören.", example = "12")
	public Long berufsfeld;

	/** Ebene4 dieser KAOA Daten */
	@Schema(required = false, description = "Ebene4 dieser KAOA Daten.", example = "4")
	public Long ebene4;

	/** Die Bemerkung zu diesen KAOA Daten.  */
	@Schema(required = false, description = "Die Bemerkung zu diesen KAOA Daten.", example = "text")
	public String bemerkung;

}
