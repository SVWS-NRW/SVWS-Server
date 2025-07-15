package de.svws_nrw.asd.data.schule;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse beschreibt eine Wahlmöglichkeit für die Abiturfächer ab dem 3. Fach (LKs sind vorgegeben).
 * Für das dritte bis fünfte Fach wird jeweils eine Liste von Fächern angegeben, die beliebig kombiniert
 * werden können.
 */
@XmlRootElement
@Schema(description = "Wahlmöglichkeit für das dritte bis fünfte Abiturfach")
@TranspilerDTO
public class BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit {

	/** mögliche Fächer für das 3. Abiturfach */
	@Schema(description = "mögliche Fächer für das 3. Abiturfach", example = "Deutsch,Englisch")
	public @NotNull List<String> abifach3 = new ArrayList<>();

	/** mögliche Fächer für das 4. Abiturfach */
	@Schema(description = "mögliche Fächer für das 4. Abiturfach", example = "Deutsch,Englisch")
	public @NotNull List<String> abifach4 = new ArrayList<>();

	/** mögliche Fächer für das 5. Abiturfach */
	@Schema(description = "mögliche Fächer für das 5. Abiturfach", example = "Deutsch,Englisch")
	public @NotNull List<String> abifach5 = new ArrayList<>();

	/**
	 * Leerer Standardkonstruktor.
	 */
	public BeruflichesGymnasiumStundentafelAbiturfaecherWahlmoeglichkeit() {
		// leer
	}

}
