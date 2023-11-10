package de.svws_nrw.module.pdf.drucktypes;

import de.svws_nrw.core.data.schule.SchuleStammdaten;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


/**
 *  Die Klasse enthält die Daten für die Schule für den Druck.
 *  Sie ist abgeleitet vom CoreType SchuleStammdaten.
 */
public class DruckSchule extends SchuleStammdaten {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 * @param schuleStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	@SuppressWarnings("java:S3011") // Begründung SuppressWarning siehe Kommentare unten.
	public DruckSchule(final SchuleStammdaten schuleStammdaten) {

		/*
		Ermittle alle Felder der Klasse des übergebenen Objektes. Da es sich hierbei um ein
		Objekt der Elternklasse handelt, sind alle diese Felder auch Teil dieser Klasse.
		Im Folgenden existiert damit garantiert zu jedem Feld des übergebenen Objektes ein
		gleichnamiges Feld mit gleichem Typ in dieser Klasse.
		*/
		Field[] felderSchuleStammdaten = schuleStammdaten.getClass().getDeclaredFields();

		/*
		Filtere nun aus allen Felder die heraus, die den Modifier Public haben und nicht static sind.
		Dies ist bei den CoreTypes die Regel.
		Dadurch können die Werte später sicher zugewiesen, da die Felder der Elternklasse und dieser
		Klasse übereinstimmen.
		*/
		int modifiers;
		List<Field> editierbareFelder = new ArrayList<>();
		for (Field feld : felderSchuleStammdaten) {
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
		for (Field feld : editierbareFelder) {
			try {
				feld.setAccessible(true);
				feld.set(this, feld.get(schuleStammdaten));
			} catch (Exception ignore) {
				// Gemäß obigen Kommentaren sollte hier kein Fehlerfall auftreten.
			}
		}
	}

	/**
	 * Gibt den aktuellen Abschnitt des Schuljahres der Schule zurück.
	 * @return Der Abschnitt des Schuljahres.
	 */
	public int aktuellerAbschnitt() {
		return this.abschnitte.stream().filter(s -> s.id == this.idSchuljahresabschnitt).toList().get(0).abschnitt;
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule zurück.
	 * @return Das Schuljahr der Schule.
	 */
	public int aktuellesSchuljahr() {
		return this.abschnitte.stream().filter(s -> s.id == this.idSchuljahresabschnitt).toList().get(0).schuljahr;
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule in Textdarstellung der Form 2023/24 zurück.
	 * @return Das Schuljahr der Schule in Textform.
	 */
	public String aktuellesSchuljahrText() {
		return "%d/%d".formatted(aktuellesSchuljahr(), (aktuellesSchuljahr() % 100) + 1);
	}

	/**
	 * Gibt das aktuelle Schuljahr der Schule mit aktuellem Abschnitt in Textdarstellung der Form 2023/24.1 zurück.
	 * @return Das Schuljahr der Schule mit aktuellem Abschnitt in Textform.
	 */
	public String aktuellesSchuljahrUndAbschnittText() {
		return aktuellesSchuljahrText() +  "." + aktuellerAbschnitt();
	}
}
