package de.svws_nrw.core.utils;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.adt.map.HashMap2D;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse bietet hilfreiche statische Methoden für {@link HashMap2D}
 *
 * @author Benjamin A. Bartsch
 */
public final class Map2DUtils {

	private Map2DUtils() {
	}

    /**
     * Liefert die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     *
     * @param <K1>  Der Typ des 1. Schlüssels.
     * @param <K2>  Der Typ des 2. Schlüssels.
     * @param <V>   Der Typ der Objekte in der ArrayList.
     * @param map2D   Die Map, welche (K1, K2) auf "ArrayList of V" abbildet.
     * @param key1  Der 1. Schlüssel.
     * @param key2  Der 2. Schlüssel.
     *
     * @return die "ArrayList of V" des Schlüssels. Erstellt eine leere "ArrayList of V", falls eine solche Zuordnung nicht existierte.
     */
	public static <@NotNull K1, @NotNull K2, @NotNull V>  @NotNull List<@NotNull V> getOrCreateArrayList(final @NotNull HashMap2D<@NotNull K1, @NotNull K2, @NotNull List<@NotNull V>> map2D, final @NotNull K1 key1, final @NotNull K2 key2) {
		final List<@NotNull V> list = map2D.getOrNull(key1, key2);
		if (list != null)
			return list;

		final @NotNull ArrayList<@NotNull V> listNeu = new ArrayList<>();
		map2D.put(key1, key2, listNeu);
		return listNeu;
	}

}
