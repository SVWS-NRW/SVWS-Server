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
public interface CoreType<@NotNull T extends @NotNull CoreTypeData, @NotNull U extends @NotNull Enum<@NotNull U> & @NotNull CoreType<@NotNull T, @NotNull U>> {

	/**
	 * Gibt den Manager des Core-Types zurück.
	 *
	 * @return der Manager
	 */
	@SuppressWarnings("unchecked")
	default @NotNull CoreTypeEnumDataManager<@NotNull T, @NotNull U> getManager() {
		return CoreTypeEnumDataManager.getManager((@NotNull Class<@NotNull U>) this.getClass());
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
		return this.getManager().getEintragBySchuljahrUndWert(schuljahr, (@NotNull U) this);
	}


	/**
	 * Gibt die Historie zu diesem Core-Type zurück.
	 *
	 * @return die Historie
	 */
	@SuppressWarnings("unchecked")
	default @NotNull List<@NotNull T> historie() {
		return this.getManager().getHistorieByWert((@NotNull U) this);
	}

}
