package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.ReformpaedagogikKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für mögliche Einträge zur Reformpädagogik
 */
public class Reformpaedagogik extends CoreTypeSimple<ReformpaedagogikKatalogEintrag, Reformpaedagogik> {

	/**
	 * Erstellung einer Reformpaedagogik mit Standardwerten
	 */
	public Reformpaedagogik() {
		// leer
	}


	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<ReformpaedagogikKatalogEintrag, Reformpaedagogik> manager) {
		CoreTypeDataManager.putManager(Reformpaedagogik.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<ReformpaedagogikKatalogEintrag, Reformpaedagogik> data() {
		return CoreTypeDataManager.getManager(Reformpaedagogik.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Reformpaedagogik @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Reformpaedagogik.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public Reformpaedagogik getInstance() {
		return new Reformpaedagogik();
	}

}
