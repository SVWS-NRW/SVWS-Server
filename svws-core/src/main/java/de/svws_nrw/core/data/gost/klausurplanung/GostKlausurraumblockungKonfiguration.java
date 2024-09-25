package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beinhaltet die Konfiguration für den Blockungs-Algorithmus von Nachschreibterminen.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für den Blockungs-Algorithmus von Nachschreibterminen der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurraumblockungKonfiguration {

	/** Die maximale Zeit, welche für die Blockung verwendet wird  */
	public long maxTimeMillis = 1000L;

	/** Die Liste der angereicherten Schülerklausurtermine. */
	@Schema(description = "die Liste der zu blockenden Schülerklausurtermine", example = "")
	public @NotNull List<GostSchuelerklausurTerminRich> schuelerklausurtermine = new ArrayList<>();

	/** Die Liste der angereicherten Klausurräume. */
	@Schema(description = "die Liste der GostKlausurräume, in die geblockt werden soll", example = "")
	public @NotNull List<GostKlausurraumRich> raeume = new ArrayList<>();

	/** TRUE, dann werden so wenig Räume wie möglich genutzt.
	 *  Falls {@link #_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume} auch TRUE, dann gilt diese Regel primär.*/
	public boolean _regel_optimiere_blocke_in_moeglichst_wenig_raeume = true;

	/** TRUE, dann werden wird auf die Räume gleichmäßig verteilt.*/
	public boolean _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = true;

	/** TRUE, dann müssen die selben Kursklausuren im selben Raum geschrieben werden. */
	public boolean _regel_forciere_selbe_kursklausur_im_selben_raum = true;

	/** TRUE, dann dürfen nur die selben Klausurdauern in einen Raum. */
	public boolean _regel_forciere_selbe_klausurdauer_pro_raum = false;

	/** TRUE, dann dürfen nur die selben Klausurstart-Zeiten in einen Raum. */
	public boolean _regel_forciere_selben_klausurstart_pro_raum = true;

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurraumblockungKonfiguration() {
		super();
	}
}
