package de.svws_nrw.module.pdf.drucktypes;

import de.svws_nrw.core.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.utils.DateUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 *  Die Klasse enthält die Daten für Schüler für den Druck.
 *  Sie ist abgeleitet vom CoreType SchuelerStammdaten.
 */
public class DruckSchueler extends SchuelerStammdaten {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 * @param schuelerStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	public DruckSchueler(final SchuelerStammdaten schuelerStammdaten) {

		/*
		Ermittle alle Felder der Klasse des übergebenen Objektes. Da es sich hierbei um ein
		Objekt der Elternklasse handelt, sind alle diese Felder auch Teil dieser Klasse.
		Im Folgenden existiert damit garantiert zu jedem Feld des übergebenen Objektes ein
		gleichnamiges Feld mit gleichem Typ in dieser Klasse.
		*/
		final Field[] felderSchuelerStammdaten = schuelerStammdaten.getClass().getDeclaredFields();

		/*
		Filtere nun aus allen Felder die heraus, die den Modifier Public haben und nicht static sind.
		Dies ist bei den CoreTypes die Regel.
		Dadurch können die Werte später sicher zugewiesen, da die Felder der Elternklasse und dieser
		Klasse übereinstimmen.
		*/
		int modifiers;
		final List<Field> editierbareFelder = new ArrayList<>();
		for (final Field feld : felderSchuelerStammdaten) {
			modifiers = feld.getModifiers();
			if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
				editierbareFelder.add(feld);
		}

		/*
		Ab hier werden nur die Werte der Public Felder aus dem übergebenen Objekt der Elternklasse
		ausgelesen und den Feldern dieser davon abgeleiteten KLasse zugewiesen.
		Da die Felder bereits Public sind, kann die Warnung "This accessibility bypass should be removed"
		(Sonar-Warnung java:S3011) ignoriert werden. Ebenso kann daher der Fehler vom Type
		IllegalAccessException nicht auftreten.
		*/
		for (final Field feld : editierbareFelder) {
			try {
				feld.setAccessible(true);
				feld.set(this, feld.get(schuelerStammdaten));
			} catch (@SuppressWarnings("unused") final IllegalAccessException ignore) {
				// Gemäß obigen Kommentaren sollte hier kein Fehlerfall auftreten.
			}
		}
	}

	/**
	 * Gibt das Geschlecht aus den SchülerStammdaten als Geschlecht zurück
	 * @return Das Geschlecht vom CoreType Geschlecht
	 */
	public Geschlecht getGeschlecht() {
		return Geschlecht.fromValue(this.geschlecht);
	}

	/**
	 * Gibt das Geburtsdatum aus den SchülerStammdaten im deutschen Format zurück
	 * @return Das Geburtsdatum im deutschen Format
	 */
	public String getGeburtsdatum() {
		return DateUtils.gibDatumGermanFormat(this.geburtsdatum);
	}
}
