package de.svws_nrw.core.data.uv.export;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse beschreibt die planungsabschnittsübergreifenden Grunddaten einer Unterrichtsverteilung.
 */
@Schema(description = "Diese Klasse beschreibt die planungsabschnittsübergreifenden Grunddaten einer Unterrichtsverteilung.")
@TranspilerDTO
public class UVv1GrunddatenExport {

	/** Ein Array mit den Lehrern der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1LehrerExport.class, description = "Ein Array mit den Lehrern der UV"))
	public @NotNull List<UVv1LehrerExport> lehrer = new ArrayList<>();

	/** Ein Array mit den Räumen der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1RaumExport.class, description = "Ein Array mit den Räumen der UV"))
	public @NotNull List<UVv1RaumExport> raeume = new ArrayList<>();

	/** Ein Array mit den Raumgruppen der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1RaumgruppeExport.class, description = "Ein Array mit den Raumgruppen der UV"))
	public @NotNull List<UVv1RaumgruppeExport> raumgruppen = new ArrayList<>();

	/** Ein Array mit den Stundentafeln der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1StundentafelExport.class, description = "Ein Array mit den Stundentafeln der UV"))
	public @NotNull List<UVv1StundentafelExport> stundentafeln = new ArrayList<>();

	/** Ein Array mit den Zeitrastern der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1ZeitrasterExport.class, description = "Ein Array mit den Zeitrastern der UV"))
	public @NotNull List<UVv1ZeitrasterExport> zeitraster = new ArrayList<>();

	/** Ein Array mit den Fächern der UV. */
	@ArraySchema(schema = @Schema(implementation = UVv1FachExport.class, description = "Ein Array mit den Fächern der UV"))
	public @NotNull List<UVv1FachExport> faecher = new ArrayList<>();

}
