import { JavaObject } from '../../java/lang/JavaObject';
import { IllegalStateException } from '../../java/lang/IllegalStateException';
import { GEAbschlussFach } from '../../core/data/abschluss/GEAbschlussFach';
import { AbschlussErgebnis } from '../../core/data/abschluss/AbschlussErgebnis';
import { GEAbschlussFaecher } from '../../core/data/abschluss/GEAbschlussFaecher';
import { SchulabschlussAllgemeinbildend } from '../../core/types/schule/SchulabschlussAllgemeinbildend';
import { StringBuilder } from '../../java/lang/StringBuilder';
import { List } from '../../java/util/List';
import { GELeistungsdifferenzierteKursart } from '../../core/types/ge/GELeistungsdifferenzierteKursart';
import { Vector } from '../../java/util/Vector';
import { HashSet } from '../../java/util/HashSet';

export class AbschlussManager extends JavaObject {


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation of " + AbschlussManager.class.getName()! + " not allowed")
	}

	/**
	 * Erzeugt ein Ergebnis der Abschlussberechnung unter Angabe, ob dieser erworben
	 * wurde. Die Liste der Nachprüfungsfächer ist leer und ein Log ist nicht zugeordnet.
	 * Sollten Nachprüfungsmöglichkeiten bestehen so ist die Methode
	 * {@link AbschlussManager#getErgebnisNachpruefung} zu nutzen.
	 * und ob dieser erworben wurde.
	 *
	 * @param abschluss   der Abschluss für den das Ergebnis erzeugt wird
	 * @param erworben    true, falls der Abschluss erworben wurde, sonst false
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public static getErgebnis(abschluss : SchulabschlussAllgemeinbildend | null, erworben : boolean) : AbschlussErgebnis {
		const ergebnis : AbschlussErgebnis = new AbschlussErgebnis();
		ergebnis.abschluss = abschluss === null ? null : abschluss.toString();
		ergebnis.erworben = erworben;
		ergebnis.npFaecher = null;
		ergebnis.log = null;
		return ergebnis;
	}

	/**
	 * Erzeugt ein Ergebnis der Abschlussberechnung, wo der Abschluss nicht erreicht wurde, aber ggf.
	 * noch durch Nachprüfungen erreicht werden kann. Ein log wird nicht zugeordnet.
	 *
	 * @param abschluss    der Abschluss für den das Ergebnis erzeugt wird
	 * @param npFaecher    eine Liste von Nachprüfungsfächern, falls eine Nachprüfung möglich ist,
	 *                     ansonsten null oder eine leere Liste
	 *
	 * @return das Ergebnis der Abschlussberechnung
	 */
	public static getErgebnisNachpruefung(abschluss : SchulabschlussAllgemeinbildend | null, npFaecher : List<string> | null) : AbschlussErgebnis {
		const ergebnis : AbschlussErgebnis = new AbschlussErgebnis();
		ergebnis.abschluss = abschluss === null ? null : abschluss.toString();
		ergebnis.erworben = false;
		if ((npFaecher === null) || (npFaecher.isEmpty()))
			ergebnis.npFaecher = null;
		else
			ergebnis.npFaecher = npFaecher;
		ergebnis.log = null;
		return ergebnis;
	}

	/**
	 * Gibt an, ob für einen Abschluss eine Nachprüfungsmöglichkeit besteht.
	 *
	 * @param ergebnis   das Abschluss-Ergebnis bei dem auf eine Nachprüfungsmöglichkeit
	 *                   geprüft werden soll.
	 *
	 * @return true, falls eine Nachprüfungsmöglichkeit besteht, sonst false
	 */
	public static hatNachpruefungsmoeglichkeit(ergebnis : AbschlussErgebnis) : boolean {
		return (ergebnis.npFaecher !== null) && !ergebnis.npFaecher.isEmpty();
	}

	/**
	 * Gibt die Nachprüfungsfächer als Komma-separierten String zurück.
	 *
	 * @param ergebnis   das Abschluss-Ergebnis bei dem die Nachprüfungsmöglichkeiten
	 *                   ausgegeben werden sollen
	 *
	 * @return die Nachprüfungsfächer als Komma-separierten String
	 */
	public static getNPFaecherString(ergebnis : AbschlussErgebnis) : string {
		if (ergebnis.npFaecher === null)
			return "";
		const sb : StringBuilder | null = new StringBuilder();
		for (const fach of ergebnis.npFaecher) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(fach);
		}
		return sb.toString();
	}

	/**
	 * Vergleicht die beiden Abschlüsse, ob sie identisch sind. Ein
	 * Übergabewert null wird als {@link SchulabschlussAllgemeinbildend#OA}
	 * interpretiert.
	 *
	 * @param a   der eine Abschluss
	 * @param b   der andere Abschluss
	 *
	 * @return true, falls sie identisch sind und ansonsten false
	 */
	public static equalsAbschluesse(a : string | null, b : string | null) : boolean {
		if ((a === null) || (SchulabschlussAllgemeinbildend.OA.is(a)))
			return (b === null) || (SchulabschlussAllgemeinbildend.OA.is(b));
		return JavaObject.equalsTranspiler(a, (b));
	}

	/**
	 * Gibt den Abschluss zurück. Im Falle das kein Abschluss angegeben ist
	 * wird {@link SchulabschlussAllgemeinbildend#OA} zurückgegeben.
	 *
	 * @param ergebnis   das Ergebnis
	 *
	 * @return der Abschluss
	 */
	public static getAbschluss(ergebnis : AbschlussErgebnis) : string {
		return ergebnis.abschluss === null ? SchulabschlussAllgemeinbildend.OA.toString() : ergebnis.abschluss;
	}

	/**
	 * Die Methode dient dem Erzeugen eines Faches für die Abschlussberechnung.
	 *
	 * @param kuerzel           das Kürzel des Faches
	 * @param bezeichnung       die Bezeichnung des Faches
	 * @param note              die Note, die in dem Fach erteilt wurde
	 * @param kursart           gibt die Kursart Faches an: leistungsdifferenzierter (E-Kurs, G-Kurs) oder sonstiger Kurs
	 * @param istFremdsprache   gibt an, ob es sich bei dem Fach um eine Fremdsprache handelt oder nicht
	 *
	 * @return das Abschlussfach
	 */
	public static erstelleAbschlussFach(kuerzel : string, bezeichnung : string | null, note : number, kursart : GELeistungsdifferenzierteKursart, istFremdsprache : boolean | null) : GEAbschlussFach {
		const fach : GEAbschlussFach = new GEAbschlussFach();
		fach.kuerzel = kuerzel;
		fach.bezeichnung = (bezeichnung === null || JavaObject.equalsTranspiler("", (bezeichnung))) ? "---" : bezeichnung;
		fach.note = note;
		fach.kursart = kursart.kuerzel;
		fach.istFremdsprache = (istFremdsprache !== null) && istFremdsprache;
		return fach;
	}

	/**
	 * Liefert eine List mit den Fachkürzeln aus der übergebenen Liste mit Abschlussfächern.
	 *
	 * @param faecher   die Liste mit Abschlussfächern
	 *
	 * @return die Liste mit den Fachkürzeln
	 */
	public static getKuerzel(faecher : List<GEAbschlussFach>) : List<string> {
		const result : Vector<string> = new Vector();
		for (let i : number = 0; i < faecher.size(); i++) {
			const fach : GEAbschlussFach = faecher.get(i);
			if ((fach === null) || fach.kuerzel === null)
				continue;
			if (result.contains(fach.kuerzel))
				continue;
			result.add(fach.kuerzel);
		}
		return result;
	}

	/**
	 * Prüft, ob vier leistungsdifferenzierte Fächer belegt wurden. Dabei wird nicht geprüft, ob
	 * es sich um G oder E-Kurse handelt.
	 *
	 * @param abschlussFaecher   die Abschlussfächer
	 *
	 * @return true, falls vier leistungsdifferenzierte Fächer belegt wurden, sonst false
	 */
	public static pruefeHat4LeistungsdifferenzierteFaecher(abschlussFaecher : GEAbschlussFaecher) : boolean {
		if (abschlussFaecher.faecher === null)
			return false;
		let count : number = 0;
		const faecher : List<GEAbschlussFach> = abschlussFaecher.faecher;
		for (const fach of faecher) {
			if (fach === null)
				continue;
			const kursart : GELeistungsdifferenzierteKursart = GELeistungsdifferenzierteKursart.from(fach.kursart);
			if ((kursart as unknown === GELeistungsdifferenzierteKursart.E as unknown) || (kursart as unknown === GELeistungsdifferenzierteKursart.G as unknown))
				count++;
		}
		return (count === 4);
	}

	/**
	 * Prüft, ob Duplikate bei den Kürzeln der Fächer vorkommen. Dies darf zur korrekten
	 * Ausführung des Abschlussalgorithmus nicht vorkommen.
	 *
	 * @param abschlussFaecher   die Abschlussfächer
	 *
	 * @return true, falls keine Duplikate vorkommen, sonst false
	 */
	public static pruefeKuerzelDuplikate(abschlussFaecher : GEAbschlussFaecher) : boolean {
		if (abschlussFaecher.faecher === null)
			return true;
		const kuerzel : HashSet<string> = new HashSet();
		const faecher : List<GEAbschlussFach> = abschlussFaecher.faecher;
		for (const fach of faecher) {
			if ((fach === null) || (fach.kuerzel === null))
				continue;
			if (!kuerzel.add(fach.kuerzel))
				return false;
		}
		return true;
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.abschluss.AbschlussManager'].includes(name);
	}

}

export function cast_de_svws_nrw_core_abschluss_AbschlussManager(obj : unknown) : AbschlussManager {
	return obj as AbschlussManager;
}
