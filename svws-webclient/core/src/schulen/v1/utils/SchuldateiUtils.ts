import { JavaObject } from '../../../java/lang/JavaObject';
import { JavaInteger } from '../../../java/lang/JavaInteger';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { SchuldateiEintrag } from '../../../schulen/v1/data/SchuldateiEintrag';
import { NumberFormatException } from '../../../java/lang/NumberFormatException';
import { Class } from '../../../java/lang/Class';
import { JavaString } from '../../../java/lang/JavaString';
import { IllegalArgumentException } from '../../../java/lang/IllegalArgumentException';
import type { Comparator } from '../../../java/util/Comparator';

export class SchuldateiUtils extends JavaObject {

	/**
	 * Das Anfangs-Schuljahr in einem Zeitraum, der abgibt, dass der Wert schon immer gültig war
	 */
	public static readonly _immerGueltigAb : number = 1980;

	/**
	 * Das End-Schuljahr in einem Zeitraum, der abgibt, dass der Wert in seiner Gültigkeit noch nicht eingeschränkt ist
	 */
	public static readonly _immerGueltigBis : number = 9999;

	/**
	 * Der Comparator zur Sortierung der Zeiträume gueltigab - gueltigbis in absteigender Reihenfolge
	 */
	public static readonly _comparatorSchuldateieintragZeitraumDescending : Comparator<SchuldateiEintrag> = { compare : (a: SchuldateiEintrag, b: SchuldateiEintrag) => {
		if (JavaObject.equalsTranspiler(b.gueltigab, (a.gueltigab)))
			return SchuldateiUtils.compare(b.gueltigbis, a.gueltigbis);
		return SchuldateiUtils.compare(b.gueltigab, a.gueltigab);
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	/**
	 * Wandelt das übergebene Datum als String mit dem Format 'DD.MM.YYYY' in ein
	 * Integer mit den Angaben für Tag, Monat und Jahr um.
	 *
	 * @param date   das Datum als String
	 *
	 * @return das umgewandelte Datum
	 *
	 * @throws IllegalArgumentException falls der String sich nicht parsen lässt oder
	 *     die Datumsangabe fehlerhaft ist
	 */
	private static splitDate(date : string) : Array<number> {
		const dmy : Array<string> = date.split("\\.");
		if (dmy.length !== 3)
			throw new IllegalArgumentException("Der Datumswert '" + date + "' ist fehlerhaft.")
		try {
			const result : Array<number> = Array(3).fill(0);
			result[0] = JavaInteger.parseInt(dmy[0]);
			if ((result[0] < 1) || (result[0] > 31))
				throw new NumberFormatException("Die Angabe des Tages ist fehlerhaft.")
			result[1] = JavaInteger.parseInt(dmy[1]);
			if ((result[1] < 1) || (result[1] > 12))
				throw new NumberFormatException("Die Angabe des Monats ist fehlerhaft.")
			result[2] = JavaInteger.parseInt(dmy[2]);
			return result;
		} catch(nfe : any) {
			throw new IllegalArgumentException("Der Datumswert '" + date + "' ist fehlerhaft.")
		}
	}

	/**
	 * Ermittlung des Schuljahres in der Zeitpunkt des angegebenen Datumstrings liegt
	 *
	 * @param date		das Datum als String
	 *
	 * @return @NotNull INTEGER  das Schuljahr. 1980 für gilt seit immer (jahr .lte. 1980), 9999 gilt für immer (Jahr .gte. 3000), sonst das Schuljahr
	 *
	 * @throws IllegalArgumentException im Fehlerfall
	 */
	public static schuljahrAusDatum(date : string) : number {
		const dmy : Array<number> = SchuldateiUtils.splitDate(date);
		let jahr : number = dmy[2];
		if (dmy[1] < 8)
			jahr--;
		return jahr;
	}

	/**
	 * Prüft, ob das Datum a früher liegt als das Datum b. Es wird eine Datumsangabe der Form
	 * 'DD.MM.YYYY' erwartet,
	 *
	 * @param a   das Datum a
	 * @param b   das Datum b
	 *
	 * @return true, wenn es früher liegt
	 *
	 * @throws IllegalArgumentException wenn die Datumsangaben fehlerhaft sind
	 */
	public static istFrueher(a : string | null, b : string | null) : boolean {
		if ((a === null) || (JavaString.isBlank(a)))
			return false;
		if ((b === null) || (JavaString.isBlank(b)))
			return true;
		const dmyA : Array<number> = SchuldateiUtils.splitDate(a);
		const dmyB : Array<number> = SchuldateiUtils.splitDate(b);
		let cmp : number = JavaInteger.compare(dmyA[2], dmyB[2]);
		if (cmp < 0)
			return true;
		if (cmp > 0)
			return false;
		cmp = JavaInteger.compare(dmyA[1], dmyB[1]);
		if (cmp < 0)
			return true;
		if (cmp > 0)
			return false;
		cmp = JavaInteger.compare(dmyA[0], dmyB[0]);
		return (cmp < 0);
	}

	/**
	 * Gibt den Vergleichswert der beiden Daten wie bei einem Comparator zurück
	 * Die Strings werden in der Form 'DD.MM.YYYY' erwartet
	 *
	 * @param a   das Datum a
	 * @param b   das Datum b
	 *
	 * @return -1,0,1  wie beim Comparator
	 *
	 * @throws IllegalArgumentException wenn die Datumsangaben fehlerhaft sind
	 */
	public static compare(a : string | null, b : string | null) : number {
		if ((a === null) || (JavaString.isBlank(a)))
			return 1;
		if ((b === null) || (JavaString.isBlank(b)))
			return -1;
		const dmyA : Array<number> = SchuldateiUtils.splitDate(a);
		const dmyB : Array<number> = SchuldateiUtils.splitDate(b);
		let cmp : number = JavaInteger.compare(dmyA[2], dmyB[2]);
		if (cmp !== 0)
			return cmp;
		cmp = JavaInteger.compare(dmyA[1], dmyB[1]);
		if (cmp !== 0)
			return cmp;
		return JavaInteger.compare(dmyA[0], dmyB[0]);
	}

	/**
	 * Prüft, ob der Schuldatei-Eintrag in mindestens einem Teil des angebenen Schuljahres gültig ist oder nicht.
	 *
	 * @param schuljahr   das Schuljahr
	 * @param eintrag     der Eintrag
	 *
	 * @return true, wenn der Eintrag in dem Schuljahr gültig ist.
	 *
	 * @throws IllegalArgumentException falls die Formatierung der Datumswerte im Schuldatei-Eintrag fehlerhaft sind
	 */
	public static pruefeSchuljahr(schuljahr : number, eintrag : SchuldateiEintrag) : boolean {
		if ((eintrag.gueltigab !== null) && (!JavaString.isBlank(eintrag.gueltigab))) {
			const dmy : Array<number> = SchuldateiUtils.splitDate(eintrag.gueltigab);
			if (!((dmy[2] <= schuljahr) || ((dmy[2] === (schuljahr + 1)) && (dmy[1] < 8))))
				return false;
		}
		if ((eintrag.gueltigbis !== null) && (!JavaString.isBlank(eintrag.gueltigbis))) {
			const dmy : Array<number> = SchuldateiUtils.splitDate(eintrag.gueltigbis);
			if (!((dmy[2] >= (schuljahr + 1)) || ((dmy[2] === schuljahr) && (dmy[1] > 7))))
				return false;
		}
		return true;
	}

	/**
	 * Prüft, ob zwei SchuldateiEintrag zeitliche Überlappung haben
	 * Keine zeitliche Überlappung liegt vor
	 * |----1a----|                 |----1b----|
	 *               |----2----|
	 * keine Überlappung bei:  (1.bis kleiner 2.ab || 2.bis kleiner 1.ab)
	 *       Überlappung bei: !(1.bis kleiner 2.ab || 2.bis kleiner 1.ab)
	 *
	 * @param eintrag1		der eine Eintrag
	 * @param eintrag2		der andere Eintrag
	 *
	 * @return				ob die Einträge überlappend sind
	 */
	public static pruefeUeberlappung(eintrag1 : SchuldateiEintrag, eintrag2 : SchuldateiEintrag) : boolean {
		return !(SchuldateiUtils.istFrueher(eintrag1.gueltigbis, eintrag2.gueltigab) || SchuldateiUtils.istFrueher(eintrag2.gueltigbis, eintrag1.gueltigab));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.schulen.v1.utils.SchuldateiUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.schulen.v1.utils.SchuldateiUtils'].includes(name);
	}

	public static class = new Class<SchuldateiUtils>('de.svws_nrw.schulen.v1.utils.SchuldateiUtils');

}

export function cast_de_svws_nrw_schulen_v1_utils_SchuldateiUtils(obj : unknown) : SchuldateiUtils {
	return obj as SchuldateiUtils;
}
