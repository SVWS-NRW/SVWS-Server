package de.svws_nrw.core.utils.klausurplan;

import java.util.List;

import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungAlgorithmen;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale;

/**
 * Konfiguration des Algorithmus.
 *
 * Die GUI muss diese Daten definieren und dem Algorithmus beim Aufruf der Methode
 * {@link KlausurterminblockungAlgorithmus#berechne(List, KlausurterminblockungAlgorithmusConfig)} übergeben.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmusConfig {

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
