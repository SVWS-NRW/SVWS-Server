package de.svws_nrw.asd.data.schueler.neuanlage;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Dieses Core-DTO beinhaltet die Information zum Anlegen eines Schuelers.
 */
@XmlRootElement
@Schema(description = " * Dieses Core-DTO beinhaltet die Information zum Anlegen eines Schuelers.")
@TranspilerDTO
public class SchuelerNeuanlage {

	/** Die Stammdaten eines Schuelers. */
	@Schema(description = "Die Stammdaten eines Schuelers.")
	public SchuelerStammdaten schuelerStammdaten;

	/** Die Lernabschnittsdaten eines Schuelers. */
	@Schema(description = "Die Lernabschnittsdaten eines Schuelers.")
	public SchuelerLernabschnittsdaten schuelerLernabschnittsdaten;

	/** Die SchuelerSchulbesuchsdaten eines Schuelers. */
	@Schema(description = "Die SchuelerSchulbesuchsdaten eines Schuelers.")
	public SchuelerSchulbesuchsdaten schuelerSchulbesuchsdaten;

}
