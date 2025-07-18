package de.svws_nrw.asd.data.lehrer;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt ein Lehramt eines Lehrers mit der übergebenen ID.
 */
@XmlRootElement
@Schema(description = "Ein Lehramt eines Lehrers.")
@TranspilerDTO
public class LehrerLehramtEintrag {

	/** Die ID des Lehramt-Eintrags. */
	@Schema(description = "Die ID des Lehramt-Eintrags.", example = "4711")
	public long id;

	/** Die ID des Lehrers. */
	@Schema(description = "Die ID des Lehrers.", example = "4711")
	public long idLehrer;

	/** Die Katalog-ID des Lehramtes. */
	@Schema(description = "Die Katalog-ID des Lehramtes.", example = "4712")
	public long idKatalogLehramt;

	/** Die ID des Anerkennungsgrund für das Lehramt. */
	@Schema(description = "Die Katalog-ID des Anerkennungsgrund für das Lehramt.", example = "4713")
	public Long idAnerkennungsgrund;

	/** Die Fachrichtungen des Lehrers für diesen Lehramteintrag. */
	@ArraySchema(schema = @Schema(implementation = LehrerFachrichtungEintrag.class, description = "Ein Array mit den Fachrichtungen des Lehrers für diesen Lehramteintrag."))
	public final @NotNull List<LehrerFachrichtungEintrag> fachrichtungen = new ArrayList<>();

	/** Die Lehrbefähigungen des Lehrers für diesen Lehramteintrag. */
	@ArraySchema(schema = @Schema(implementation = LehrerLehrbefaehigungEintrag.class, description = "Ein Array mit den Lehrbefähigungen des Lehrers für diesen Lehramteintrag."))
	public final @NotNull List<LehrerLehrbefaehigungEintrag> lehrbefaehigungen = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public LehrerLehramtEintrag() {
		// leer
	}

}
