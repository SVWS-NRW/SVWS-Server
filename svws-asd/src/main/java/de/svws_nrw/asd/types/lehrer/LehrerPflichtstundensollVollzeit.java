package de.svws_nrw.asd.types.lehrer;

import de.svws_nrw.asd.data.lehrer.LehrerPflichtstundensollVollzeitKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Der Core-Type für Vollzeit-Pflichtstunden-Sollwerte für Lehrer.
 */
public class LehrerPflichtstundensollVollzeit extends CoreTypeSimple<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit> {

	/**
	 * Erstellt einen LehrerPflichtstundensollVollzeit mit Standardwerten
	 */
	public LehrerPflichtstundensollVollzeit() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit> manager) {
		CoreTypeDataManager.putManager(LehrerPflichtstundensollVollzeit.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<LehrerPflichtstundensollVollzeitKatalogEintrag, LehrerPflichtstundensollVollzeit> data() {
		return CoreTypeDataManager.getManager(LehrerPflichtstundensollVollzeit.class);
	}


	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull LehrerPflichtstundensollVollzeit @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(LehrerPflichtstundensollVollzeit.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public LehrerPflichtstundensollVollzeit getInstance() {
		return new LehrerPflichtstundensollVollzeit();
	}

}
