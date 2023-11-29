package de.svws_nrw.transpiler.test;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Dieses Interface beschreibt die Schnittstelle, welche Core-Types allgemein zur Verfügung
 * stellen.
 *
 * @param <T> der Typ des Katalog-Eintrags des implementierenden Core-Types
 * @param <U> der Typ des implementierenden Core-Types
 */
public interface CoreType<T extends CoreTypeData, U extends Enum<U> & CoreType<T, U>> {

	/**
	 * Gibt den Manager des Core-Types zurück.
	 *
	 * @return der Manager
	 */
	@SuppressWarnings("unchecked")
	default CoreTypeEnumDataManager<T, U> getManager() {
		return CoreTypeEnumDataManager.getManager((@NotNull Class<U>) this.getClass());
	}


	/**
	 * Gibt die Daten aus der Historie zu diesem Core-Type zurück.
	 *
	 * @param schuljahr   das zu prüfende Schuljahr
	 *
	 * @return die Daten aus der Historie
	 */
	@SuppressWarnings("unchecked")
	default T daten(final int schuljahr) {
		return getManager().getEintragBySchuljahrUndWert(schuljahr, (U) this);
	}


	/**
	 * Gibt die Historie zu diesem Core-Type zurück.
	 *
	 * @return die Historie
	 */
	@SuppressWarnings("unchecked")
	default @NotNull List<@NotNull T> historie() {
		return getManager().getHistorieByWert((U) this);
	}

}
