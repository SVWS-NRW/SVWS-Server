package de.svws_nrw.asd.types;

import java.util.List;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;


/**
 * Dieses Interface beschreibt die Schnittstelle, welche Core-Types allgemein zur Verfügung
 * stellen.
 *
 * @param <T> der Typ des Katalog-Eintrags des implementierenden Core-Types
 * @param <U> der Typ des implementierenden Core-Types
 */
public interface CoreType<T extends CoreTypeData, U extends CoreType<T, U>> extends Comparable<U> {

	/**
	 * Gibt den Manager des Core-Types zurück.
	 *
	 * @return der Manager
	 */
	@SuppressWarnings("unchecked")
	default @NotNull CoreTypeDataManager<T, U> getManager() {
		return CoreTypeDataManager.getManager((@NotNull Class<U>) this.getClass());
	}


	/**
	 * Gibt die Daten aus der Historie zu diesem Core-Type zurück.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Daten aus der Historie
	 */
	@SuppressWarnings("unchecked")
	default @AllowNull T daten(final int schuljahr) {
		return this.getManager().getEintragBySchuljahrUndWert(schuljahr, (@NotNull U) this);
	}


	/**
	 * Gibt die Statistik-ID zu diesem Core-Type zurück.
	 *
	 * @return die Statistik ID
	 */
	@SuppressWarnings("unchecked")
	default String statistikId() {
		return this.getManager().getStatistikIdByWert((@NotNull U) this);
	}


	/**
	 * Gibt die Historie zu diesem Core-Type zurück.
	 *
	 * @return die Historie
	 */
	@SuppressWarnings("unchecked")
	default @NotNull List<T> historie() {
		return this.getManager().getHistorieByWert((@NotNull U) this);
	}

    /**
     * Gibt den Bezeichner des CoreType-Wertes zurück.
     *
     * @return der Bezeichner
     */
	@NotNull String name();

    /**
     * Gibt einen Ordinal-Wert für den CoreType-Wert ähnlich wie bei Enum-Konstanten zurück.
     *
     * @return der Ordinal-Wert
     */
	int ordinal();

    /**
     * Gibt den Bezeichner des CoreType-Wertes zurück.
     *
     * @return der Bezeichner
     */
	@Override
    int compareTo(@NotNull U other);

}
