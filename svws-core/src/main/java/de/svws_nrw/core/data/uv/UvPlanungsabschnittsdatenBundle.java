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
 * Sie liefert eine Sammlung aller zu einem Planungsabschnitt einer Unterrichtsverteilung gehörigen Daten.
 */
@XmlRootElement
@Schema(description = "die Sammlung aller zu einem Planungsabschnitt einer Unterrichtsverteilung gehörigen Daten.")
@TranspilerDTO
public class UvPlanungsabschnittsdatenBundle {

	/** Der Planungsabschnitt */
	@Schema(description = "der Planungsabschnitt")
	public @NotNull UvPlanungsabschnitt planungsabschnitt = new UvPlanungsabschnitt();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittLehrer.class, description = "Ein Array mit "))
	public @NotNull List<UvPlanungsabschnittLehrer> planungsabschnittlehrer = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittSchueler.class, description = "Ein Array mit "))
	public @NotNull List<UvPlanungsabschnittSchueler> planungsabschnittschueler = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvPlanungsabschnittZeitraster.class, description = "Ein Array mit "))
	public @NotNull List<UvPlanungsabschnittZeitraster> planungsabschnittzeitraster = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvKlasse.class, description = "Ein Array mit "))
	public @NotNull List<UvKlasse> klassen = new ArrayList<>();

	/** Ein Array mit den Klassenlehrer-Zuordnungen des Planungsabschnitts */
	@ArraySchema(schema = @Schema(implementation = UvKlassenLehrer.class, description = "Ein Array mit den Klassenlehrer-Zuordnungen"))
	public @NotNull List<UvKlassenLehrer> klassenlehrer = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvKurs.class, description = "Ein Array mit "))
	public @NotNull List<UvKurs> kurse = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvSchuelergruppe.class, description = "Ein Array mit "))
	public @NotNull List<UvSchuelergruppe> schuelergruppen = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvSchuelergruppeSchueler.class, description = "Ein Array mit "))
	public @NotNull List<UvSchuelergruppeSchueler> schuelergruppenschueler = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvSchiene.class, description = "Ein Array mit "))
	public @NotNull List<UvSchiene> schienen = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvLerngruppenLehrer.class, description = "Ein Array mit "))
	public @NotNull List<UvLerngruppenLehrer> lerngruppenlehrer = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvLerngruppenSchiene.class, description = "Ein Array mit "))
	public @NotNull List<UvLerngruppenSchiene> lerngruppenschienen = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvLerngruppe.class, description = "Ein Array mit "))
	public @NotNull List<UvLerngruppe> lerngruppen = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvUnterricht.class, description = "Ein Array mit "))
	public @NotNull List<UvUnterricht> unterrichte = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvUnterrichtRaum.class, description = "Ein Array mit "))
	public @NotNull List<UvUnterrichtRaum> unterrichtraeume = new ArrayList<>();

	/** Ein Array mit  */
	@ArraySchema(schema = @Schema(implementation = UvUnterrichtLerngruppenlehrer.class, description = "Ein Array mit "))
	public @NotNull List<UvUnterrichtLerngruppenlehrer> unterrichtlerngruppenlehrer = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public UvPlanungsabschnittsdatenBundle() {
		super();
	}

}
