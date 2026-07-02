package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.AnrechnungsantragBKAZVOKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik AnrechnungsantragBKAZVO.
 */
public class AnrechnungsantragBKAZVO extends CoreTypeSimple<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO> {

	/**
	 * Erstellt eine AnrechnungsantragBKAZVO
	 */
	public AnrechnungsantragBKAZVO() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO> manager) {
		CoreTypeDataManager.putManager(AnrechnungsantragBKAZVO.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<AnrechnungsantragBKAZVOKatalogEintrag, AnrechnungsantragBKAZVO> data() {
		return CoreTypeDataManager.getManager(AnrechnungsantragBKAZVO.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull AnrechnungsantragBKAZVO @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(AnrechnungsantragBKAZVO.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public AnrechnungsantragBKAZVO getInstance() {
		return new AnrechnungsantragBKAZVO();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public @NotNull AnrechnungsantragBKAZVOKatalogEintrag getLetzterEintrag() {
		return this.getManager().getHistorieByWert(this).getLast();
	}

}
