package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.data.schule.FormBilingualerUnterrichtKatalogEintrag;
import de.svws_nrw.asd.types.CoreTypeSimple;
import de.svws_nrw.asd.utils.CoreTypeDataManager;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Core-Type für die für die amtliche Schulstatistik FormBilingualerUnterricht.
 */
public class FormBilingualerUnterricht extends CoreTypeSimple<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht> {

	/**
	 * Erstellt eine FormBilingualerUnterricht
	 */
	public FormBilingualerUnterricht() {
		// nichts zu tun
	}

	/**
	 * Initialisiert den Core-Type mit dem angegebenen Manager.
	 *
	 * @param manager   der Manager für die Daten des Core-Types
	 */
	public static void init(final @NotNull CoreTypeDataManager<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht> manager) {
		CoreTypeDataManager.putManager(FormBilingualerUnterricht.class, manager);
	}


	/**
	 * Gibt den Daten-Manager für den Zugriff auf die Core-Type-Daten zurück, sofern dieser initialisiert wurde.
	 *
	 * @return der Daten-Manager
	 */
	public static @NotNull CoreTypeDataManager<FormBilingualerUnterrichtKatalogEintrag, FormBilingualerUnterricht> data() {
		return CoreTypeDataManager.getManager(FormBilingualerUnterricht.class);
	}

	/**
	 * Gibt alle Werte des Core-Types zurück.
	 *
	 * @return die Werte des Core-Types als Array
	 */
	public static @NotNull FormBilingualerUnterricht @NotNull [] values() {
		return CoreTypeSimple.valuesByClass(FormBilingualerUnterricht.class);
	}

	/**
	 * Erzeugt eine Instanz dieser Klasse.
	 */
	@Override
	public FormBilingualerUnterricht getInstance() {
		return new FormBilingualerUnterricht();
	}

	/**
	 * Gibt den letzten Historieneintrag zu dem Core-Type-Wert zurück
	 *
	 * @return der letzten Historieneintrag zu dem Core-Type-Wert
	 */
	public @NotNull FormBilingualerUnterrichtKatalogEintrag getLetzterEintrag() {
		return this.getManager().getHistorieByWert(this).getLast();
	}

}
