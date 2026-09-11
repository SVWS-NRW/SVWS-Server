package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt einen Planungsabschnitt im UV-Export mit allen zugehörigen Daten.
 */
@Schema(description = "Diese Klasse beschreibt einen Planungsabschnitt im UV-Export mit allen zugehörigen Daten.")
@TranspilerDTO
public class UVv1PlanungsabschnittExport {

	/** Die UV-ID des Planungsabschnitts. */
	@Schema(description = "die UV-ID des Planungsabschnitts", example = "815")
	public long uvId = -1;

	/** Das Schuljahr, zu dem der Planungsabschnitt gehört. */
	@Schema(description = "das Schuljahr, zu dem der Planungsabschnitt gehört.", example = "2025")
	public int schuljahr = -1;

	/** Gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein. */
	@Schema(description = "gibt an, ob der Planungsabschnitt aktiv ist. An einem Stichtag darf nur ein Planungsabschnitt aktiv sein.", example = "true")
	public boolean aktiv = false;

	/** Das Datum des Gültigkeitsbeginns des Planungsabschnitts. */
	@Schema(description = "das Datum des Gültigkeitsbeginns des Planungsabschnitts", example = "2025-02-09")
	public @NotNull String gueltigVon = "";

	/** Das Datum des Gültigkeitsendes des Planungsabschnitts. */
	@Schema(description = "das Datum des Gültigkeitsendes des Planungsabschnitts", example = "2025-04-13")
	public String gueltigBis = "";

	/** Die optionale Beschreibung oder der Kommentar zum Planungsabschnitt. */
	@Schema(description = "die optionale Beschreibung oder der Kommentar zum Planungsabschnitt", example = "Weggang Q2")
	public String beschreibung = null;

	/** Ein Array mit den UV-IDs der Lehrer des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = Long.class, description = "Ein Array mit den UV-IDs der Lehrer des Planungsabschnitts"))
	public @NotNull List<Long> lehrerUvIds = new ArrayList<>();

	/** Ein Array mit den Schülern des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1PlanungsabschnittSchuelerExport.class, description = "Ein Array mit den Schülern des Planungsabschnitts"))
	public @NotNull List<UVv1PlanungsabschnittSchuelerExport> schueler = new ArrayList<>();

	/** Ein Array mit den Zeitraster-Zuordnungen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1PlanungsabschnittZeitrasterExport.class, description = "Ein Array mit den Zeitraster-Zuordnungen des Planungsabschnitts"))
	public @NotNull List<UVv1PlanungsabschnittZeitrasterExport> zeitraster = new ArrayList<>();

	/** Ein Array mit den Klassen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1KlasseExport.class, description = "Ein Array mit den Klassen des Planungsabschnitts"))
	public @NotNull List<UVv1KlasseExport> klassen = new ArrayList<>();

	/** Ein Array mit den Kursen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1KursExport.class, description = "Ein Array mit den Kursen des Planungsabschnitts"))
	public @NotNull List<UVv1KursExport> kurse = new ArrayList<>();

	/** Ein Array mit den Schülergruppen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1SchuelergruppeExport.class, description = "Ein Array mit den Schülergruppen des Planungsabschnitts"))
	public @NotNull List<UVv1SchuelergruppeExport> schuelergruppen = new ArrayList<>();

	/** Ein Array mit den Schienen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1SchieneExport.class, description = "Ein Array mit den Schienen des Planungsabschnitts"))
	public @NotNull List<UVv1SchieneExport> schienen = new ArrayList<>();

	/** Ein Array mit den Lerngruppen des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1LerngruppeExport.class, description = "Ein Array mit den Lerngruppen des Planungsabschnitts"))
	public @NotNull List<UVv1LerngruppeExport> lerngruppen = new ArrayList<>();

	/** Ein Array mit den Unterrichtseinheiten des Planungsabschnitts. */
	@ArraySchema(schema = @Schema(implementation = UVv1UnterrichtExport.class, description = "Ein Array mit den Unterrichtseinheiten des Planungsabschnitts"))
	public @NotNull List<UVv1UnterrichtExport> unterrichte = new ArrayList<>();

}
