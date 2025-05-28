package de.svws_nrw.asd.types.schueler;

import de.svws_nrw.asd.data.schueler.EinschulungsartKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik möglichen Einschulungsart.
 */
public class Einschulungsart extends CoreTypeSimple<EinschulungsartKatalogEintrag, Einschulungsart> {

	/**
	 * Erstellt eine Einschulungsart mit Standardwerten
	 */
	public Einschulungsart() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<EinschulungsartKatalogEintrag, Einschulungsart> manager) {
		CoreTypeDataManager.putManager(Einschulungsart.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<EinschulungsartKatalogEintrag, Einschulungsart> data() {
		return CoreTypeDataManager.getManager(Einschulungsart.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull Einschulungsart @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(Einschulungsart.class);
	}

	/**
	 * Erzeugt eine Instance dieser Klasse.
	 */
	@Override
	public Einschulungsart getInstance() {
		return new Einschulungsart();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public @NotNull EinschulungsartKatalogEintrag getLetzterEintrag() {
		return this.getManager().getHistorieByWert(this).getLast();
	}

}
