import { JavaObject, cast_java_lang_Object } from '../../../java/lang/JavaObject';
import { JavaString, cast_java_lang_String } from '../../../java/lang/JavaString';
import { Schuljahresabschnitt, cast_de_nrw_schule_svws_core_data_schule_Schuljahresabschnitt } from '../../../core/data/schule/Schuljahresabschnitt';

export class SchuljahresAbschnittsManager extends JavaObject {

	/**
	 *  Der aktuelle Schuljahresabschnitt
	 */
	private abschnitt : Schuljahresabschnitt;

	/**
	 *  Die Anzahl an Schuljahresabschnitten an dieser Schule
	 */
	private anzahlAbschnitte : number = 0;


	/**
	 * Konstruktor für den SchuljahresManager mit Schuljahresabschnitt und der
	 * Anzahl an Abschnitten für diese Schule
	 * 
	 * @param schuljahresabschnitt der Schuljahresabschnitt für den dieser Manager
	 *                             die Stringrepräsentation bestimmen soll
	 * @param anzahlAbschnitte     die Anzahl an Schuljahresabschnitten dieser
	 *                             Schule
	 */
	public constructor(schuljahresabschnitt : Schuljahresabschnitt, anzahlAbschnitte : number) {
		super();
		this.abschnitt = schuljahresabschnitt;
		this.anzahlAbschnitte = anzahlAbschnitte;
	}

	/**
	 * Gibt den Schuljahresabschnitt dieses Managers als Stringrepräsentation
	 * wieder. Arbeitet mit den Konstruktorparametern für Schuljahresabschnitt und
	 * die Anzahl der Abschnitte
	 * 
	 * @return einen String, der den Schuljahresabschnitt wiedergibt, bspw:<br>
	 *         {@code S2 2022}<br>
	 *         {@code Q4 2022}<br>
	 *         {@code 4/6 2022}<br>
	 * 
	 */
	public getSchuljahresAbschnittAsString() : string {
		return SchuljahresAbschnittsManager.createSchuljahresAbschnittString(this.abschnitt, this.anzahlAbschnitte);
	}

	/**
	 * Gibt den Schuljahresabschnitt dieses Managers als Stringrepräsentation
	 * wieder. Diese Methode dient dazu, abweichende Parameter als im Konstruktor
	 * angeben zu können
	 * 
	 * @param abschnitt        der Schuljahresabschnitt für den die
	 *                         Stringrepräsentation bestimmt werden soll
	 * @param anzahlAbschnitte die Anzahl an Schuljahresabschnitten dieser Schule
	 * 
	 * @return einen String, der den Schuljahresabschnitt wiedergibt, bspw:<br>
	 *         {@code S2 2022}<br>
	 *         {@code Q4 2022}<br>
	 *         {@code 4/6 2022}<br>
	 * 
	 */
	public static createSchuljahresAbschnittString(abschnitt : Schuljahresabschnitt, anzahlAbschnitte : number) : string {
		if (anzahlAbschnitte <= 1) {
			return "" + abschnitt.schuljahr;
		} else 
			if (anzahlAbschnitte > 1 && anzahlAbschnitte < 5) {
				return SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(anzahlAbschnitte)! + abschnitt.abschnitt + " " + abschnitt.schuljahr;
			} else {
				return abschnitt.abschnitt + "/" + anzahlAbschnitte + " " + abschnitt.schuljahr;
			}
	}

	/**
	 * Gibt abhängig von der für diesen Manager konfigurierten Anzahl der
	 * Schuljahresabschnitte im Jahr ein Abschnittskuerzel wieder:<br>
	 * 
	 * @return S (für Semester), wenn es 2 Abschnitte gibt<br>
	 *         T (Für Trimester), wenn es 3 Abschnitte gibt<br>
	 *         Q (für Quartale), wenn es 4 Abschnitte gibt<br>
	 *         leerer String, bei 1 oder mehr als 4 Abschnitten<br>
	 */
	public getRepresentationForAnzahlAbschnitte() : string {
		return SchuljahresAbschnittsManager.createRepresentationForAnzahlAbschnitte(this.anzahlAbschnitte);
	}

	/**
	 * Gibt abhängig von der Anzahl der Schuljahresabschnitte im Jahr ein
	 * Abschnittskuerzel wieder. Mit dieser Methode kann ein vom
	 * Konstruktorparameter abweichender Wert bestimmt werden:<br>
	 * 
	 * @param anzahlAbschnitte Anzahl der Abschnitte in einem Schuljahr
	 * @return S (für Semester), wenn es 2 Abschnitte gibt<br>
	 *         T (Für Trimester), wenn es 3 Abschnitte gibt<br>
	 *         Q (für Quartale), wenn es 4 Abschnitte gibt<br>
	 *         leerer String, bei 1 oder mehr als 4 Abschnitten<br>
	 */
	public static createRepresentationForAnzahlAbschnitte(anzahlAbschnitte : number) : string {
		if (anzahlAbschnitte === 2) 
			return "S"; else 
			if (anzahlAbschnitte === 3) 
				return "T"; else 
				if (anzahlAbschnitte === 4) 
					return "Q"; else 
					return "";
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.utils.schule.SchuljahresAbschnittsManager'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_utils_schule_SchuljahresAbschnittsManager(obj : unknown) : SchuljahresAbschnittsManager {
	return obj as SchuljahresAbschnittsManager;
}
