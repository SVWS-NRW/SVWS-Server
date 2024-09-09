package de.svws_nrw.asd.types.fach;

import de.svws_nrw.asd.data.fach.SprachreferenzniveauKatalogEintrag;
import de.svws_nrw.asd.types.CoreType;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für die Sprachreferenzniveaus.
 */
public enum Sprachreferenzniveau implements @NotNull CoreType<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> {

	/** Referenzniveau nach GeR A1. */
	A1,

	/** Referenzniveau nach GeR A1 Plus */
	A1P,

	/** Referenzniveau nach GeR A1A2 */
	A1A2,

	/** Referenzniveau nach GeR A2 */
	A2,

	/** Referenzniveau nach GeR A2 Plus */
	A2P,

	/** Referenzniveau nach GeR A2B1. */
	A2B1,

	/** Referenzniveau nach GeR B1. */
	B1,

	/** Referenzniveau nach GeR B1 Plus. */
	B1P,

	/** Referenzniveau nach GeR B1B2. */
	B1B2,

	/** Referenzniveau nach GeR B2. */
	B2,

	/** Referenzniveau nach GeR B2C1. */
	B2C1,

	/** Referenzniveau nach GeR C1. */
	C1,

	/** Referenzniveau nach GeR C2. */
	C2;

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> manager) {
		CoreTypeDataManager.putManager(Sprachreferenzniveau.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<SprachreferenzniveauKatalogEintrag, Sprachreferenzniveau> data() {
		return CoreTypeDataManager.getManager(Sprachreferenzniveau.class);
	}



	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem übergebenen Niveau.
	 *
	 * @param other   das andere Sprachreferenzniveau
	 *
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public int compare(final Sprachreferenzniveau other) {
		if (other == null)
			return 1;
		return this.compareTo(other);
	}


	/**
	 * Vergleicht dieses Sprachreferenzniveau mit dem Niveau des übergebenen Kürzels.
	 *
	 * @param kuerzel   das Kürzel des anderen Sprachreferenzniveaus
	 *
	 * @return -1 (kleiner), 0 (gleich) oder 1 (größer)
	 */
	public int compareByKuerzel(final String kuerzel) {
		if (kuerzel == null)
			return 1;
		return this.compare(Sprachreferenzniveau.data().getWertByKuerzel(kuerzel));
	}

}
