package de.svws_nrw.core.adt;

import java.util.Collection;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse definiert statische Methoden um Collections für das Iterieren in einer
 * For-Each-Schleife zur Verfügung zu stellen.
 */
public final class ForEach {


	private ForEach() {
		// leer
	}

	/**
	 * Liefert eine neue Collection, die alle Paare der anderen beiden Collections beinhaltet.
	 *
	 * @param <T>  Der Typ der 1. Collection.
	 * @param <U>  Der Typ der 2. Collection.
	 * @param collection1  Die 1. Collection.
	 * @param collection2  Die 2. Collection.
	 *
	 * @return eine neue Collection, die alle Paare der anderen beiden Collections beinhaltet.
	 */
	public static <@NotNull T, @NotNull U> @NotNull LinkedCollection<@NotNull Pair<@NotNull T, @NotNull U>> pair(final @NotNull Collection<@NotNull T> collection1, final @NotNull Collection<@NotNull U> collection2) {
		final @NotNull LinkedCollection<@NotNull Pair<@NotNull T, @NotNull U>> c = new LinkedCollection<>();

		for (final @NotNull T t : collection1)
			for (final @NotNull U u : collection2)
				c.add(new Pair<>(t, u));

		return c;
	}

}
