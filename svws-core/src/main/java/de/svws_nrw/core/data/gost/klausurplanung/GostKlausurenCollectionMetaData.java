package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert eine Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Gost-Klausurvorgaben, Kursklausuren und Schülerklausuren")
@TranspilerDTO
public class GostKlausurenCollectionMetaData {

	/** Die Liste der Klausurvorgaben. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<GostFach> faecher = new ArrayList<>();

	/** Die Liste der Kursklausuren. */
	@Schema(description = "die ID der Schülerklausur", example = "")
	public @NotNull List<SchuelerListeEintrag> schueler = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<LehrerListeEintrag> lehrer = new ArrayList<>();

	/** Die Liste der Schülerklausuren. */
	@Schema(description = "die ID der Klausurraumstunde", example = "")
	public @NotNull List<KursDaten> kurse = new ArrayList<>();

	/**
	 * Sammelt alle Daten, die für die Klausurplanung der gesamten Oberstufe nötig sind.
	 *
	 * @param addData dd
	 */
	public void addAll(final @NotNull GostKlausurenCollectionMetaData addData) {
		faecher.addAll(addData.faecher);
		final Set<SchuelerListeEintrag> schuelerMenge = new HashSet<>(schueler);
		schuelerMenge.addAll(addData.schueler);
		schueler = new ArrayList<>(schuelerMenge);
		lehrer.addAll(addData.lehrer);
		kurse.addAll(addData.kurse);
	}

}
