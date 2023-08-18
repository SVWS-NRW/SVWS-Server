package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungAlgorithmen;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse beinhaltet die Konfiguration für den Blockungs-Algorithmus von Klausurterminen.
 */
@XmlRootElement
@Schema(description = "Die Konfiguration für den Blockungs-Algorithmus von Klausurterminen der gymnasialen Oberstufe.")
@TranspilerDTO
public class GostKlausurterminblockungKonfiguration {

	/** Die maximale Zeit, welche für die Blockung verwendet wird  */
	public long maxTimeMillis = 1000L;

	/** Der Typ des Algorithmus, welcher verwendet wird. */
	public int algorithmus = KlausurterminblockungAlgorithmen.NORMAL.id;

	/** Der Modus für die Art, ob und wie die beiden Kursarten LK und GK geblockt werden */
	public int modusKursarten = KlausurterminblockungModusKursarten.BEIDE.id;

	/** Der Modus dafür, wie die Klausuren in den Quartalen eines Halbjahres geblockt werden. */
	public int modusQuartale = KlausurterminblockungModusQuartale.ZUSAMMEN.id;

	/** True, falls Kurse mit gleicher Lehrkraft+Fach+Kursart in einem Termin geblockt werden sollen. */
	public boolean regelBeiTerminenGleicheLehrkraftFachKursart = false;

	/** True, falls die Regel "bevorzuge gleiche Kursschienen bei Terminen" aktiviert ist. */
	public boolean regelBevorzugeBeiTerminenGleicheKursschienen = false;

}
