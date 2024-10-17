package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung von Metadaten zu Klausurdaten.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Metadaten zu Klausurdaten.")
@TranspilerDTO
public class GostKlausurenCollectionMetaData {

	/** Ein Array mit den Daten der Fächer. */
	@ArraySchema(schema = @Schema(implementation = GostFach.class, description = "Ein Array mit den Daten der Fächer."))
	public @NotNull List<GostFach> faecher = new ArrayList<>();

	/** Ein Array mit den Daten der Schüler. */
	@ArraySchema(schema = @Schema(implementation = SchuelerListeEintrag.class, description = "Ein Array mit den Daten der Schüler."))
	public @NotNull List<SchuelerListeEintrag> schueler = new ArrayList<>();

	/** Ein Array mit den Daten der Lehrer. */
	@ArraySchema(schema = @Schema(implementation = LehrerListeEintrag.class, description = "Ein Array mit den Daten der Lehrer."))
	public @NotNull List<LehrerListeEintrag> lehrer = new ArrayList<>();

	/** Ein Array mit den Daten der Kurse. */
	@ArraySchema(schema = @Schema(implementation = KursDaten.class, description = "Ein Array mit den Daten der Kurse."))
	public @NotNull List<KursDaten> kurse = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenCollectionMetaData() {
		super();
	}

}
