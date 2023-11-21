package de.svws_nrw.module.pdf.reptypes;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.module.pdf.reptypes.utils.RepTypesUtils;


/**
 *  Die Klasse enthält die Daten für Schüler für den Druck.
 *  Sie ist abgeleitet vom CoreType SchuelerStammdaten.
 */
public class RepSchueler extends SchuelerStammdaten {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 * @param schuelerStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	public RepSchueler(final SchuelerStammdaten schuelerStammdaten) {
		RepTypesUtils.setFelderAusSuperklassenObjekt(schuelerStammdaten, this);
	}

	/**
	 * Gibt das Geschlecht aus den SchülerStammdaten als Geschlecht zurück
	 * @return Das Geschlecht vom CoreType Geschlecht
	 */
	public Geschlecht getGeschlecht() {
		return Geschlecht.fromValue(this.geschlecht);
	}
}
