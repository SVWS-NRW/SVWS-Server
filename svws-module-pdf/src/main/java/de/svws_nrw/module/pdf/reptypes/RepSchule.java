package de.svws_nrw.module.pdf.reptypes;
import de.svws_nrw.core.data.schule.SchuleStammdaten;
import de.svws_nrw.module.pdf.reptypes.utils.RepTypesUtils;


/**
 *  Die Klasse enthält die Daten für die Schule für den Druck.
 *  Sie ist abgeleitet vom CoreType SchuleStammdaten.
 */
public class RepSchule extends SchuleStammdaten {

	/**
	 * Weist die Daten zu den Feldern der Elternklasse zu, wenn ein Elternklassenobjekt übergeben wird.
	 * @param schuleStammdaten Daten in Form eines Objektes der Elternklasse
	 */
	public RepSchule(final SchuleStammdaten schuleStammdaten) {
		RepTypesUtils.setFelderAusSuperklassenObjekt(schuleStammdaten, this);
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
