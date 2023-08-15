package de.svws_nrw.core.utils.klausurplan;

import java.util.List;

import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungAlgorithmen;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusKursarten;
import de.svws_nrw.core.types.gost.klausurplanung.KlausurterminblockungModusQuartale;
import jakarta.validation.constraints.NotNull;

/**
 * Konfiguration des Algorithmus.
 *
 * Die GUI muss diese Daten definieren und dem Algorithmus beim Aufruf der Methode
 * {@link KlausurterminblockungAlgorithmus#berechne(List, KlausurterminblockungAlgorithmusConfig)} übergeben.
 *
 * @author Benjamin A. Bartsch
 */
public class KlausurterminblockungAlgorithmusConfig {

	private long max_time_millis;

	/** Der Typ des Algorithmus, welcher verwendet wird. */
	public @NotNull KlausurterminblockungAlgorithmen algorithmus = KlausurterminblockungAlgorithmen.NORMAL;

	/** Der Modus für die Art, ob und wie die beiden Kursarten LK und GK geblockt werden */
	public @NotNull KlausurterminblockungModusKursarten modusKursarten = KlausurterminblockungModusKursarten.BEIDE;

	/** Der Modus dafür, wie die Klausuren in den Quartalen eines Halbjahres geblockt werden. */
	public @NotNull KlausurterminblockungModusQuartale modusQuartale = KlausurterminblockungModusQuartale.ZUSAMMEN;

	private boolean regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	private boolean regel_bevorzuge_gleiche_kursschienen_pro_termin;


	/**
	 * Der Konstruktor definiert Standardwerte.
	 */
	public KlausurterminblockungAlgorithmusConfig() {
		set_max_time_millis(1000L);
		set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(false);
		set_regel_bevorzuge_gleiche_kursschienen_pro_termin(false);
	}

	/**
	 * Liefert die maximale Blockungszeit.
	 *
	 * @return die maximale Blockungszeit.
	 */
	public long get_max_time_millis() {
		return max_time_millis;
	}

	/**
	 * Setzt die maximale Blockungszeit.
	 *
	 * @param pMaxTimeMillis die maximale Blockungszeit.
	 */
	public void set_max_time_millis(final long pMaxTimeMillis) {
		max_time_millis = pMaxTimeMillis;
	}

	/**
	 * Liefert TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 *
	 * @return TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public boolean get_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin() {
		return regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin;
	}

	/**
	 * TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 *
	 * @param pAktivieren TRUE, falls Kurse mit gleicher Lehrkraft+Fach+Kursart im selben Termin landen sollen.
	 */
	public void set_regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin(final boolean pAktivieren) {
		regel_wenn_lehrkraft_fach_kursart_dann_gleicher_termin = pAktivieren;
	}

	/**
	 * Liefert TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 *
	 * @return TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert ist.
	 */
	public boolean get_regel_bevorzuge_gleiche_kursschienen_pro_termin() {
		return regel_bevorzuge_gleiche_kursschienen_pro_termin;
	}

	/**
	 * TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
	 * @param pAktivieren TRUE, falls die Regel "bevorzuge gleiche Kursschienen pro Termin" aktiviert werden soll.
	 */
	public void set_regel_bevorzuge_gleiche_kursschienen_pro_termin(final boolean pAktivieren) {
		this.regel_bevorzuge_gleiche_kursschienen_pro_termin = pAktivieren;
	}

}
