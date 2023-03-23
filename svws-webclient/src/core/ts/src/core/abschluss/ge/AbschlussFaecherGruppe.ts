import { JavaObject } from '../../../java/lang/JavaObject';
import { GEAbschlussFach } from '../../../core/data/abschluss/GEAbschlussFach';
import { StringBuilder } from '../../../java/lang/StringBuilder';
import { List } from '../../../java/util/List';
import { Vector } from '../../../java/util/Vector';
import { GELeistungsdifferenzierteKursart } from '../../../core/types/ge/GELeistungsdifferenzierteKursart';
import { AbschlussManager } from '../../../core/abschluss/AbschlussManager';
import { Predicate } from '../../../java/util/function/Predicate';

export class AbschlussFaecherGruppe extends JavaObject {

	/**
	 * Eine Liste mit allen Fächern dieser Fachgruppe
	 */
	private readonly faecher : Vector<GEAbschlussFach> = new Vector();


	/**
	 * Erzeugt eine Fächergruppe aus den angegebenen Fächern und den vorgegebenen Kriterien.
	 *
	 * @param faecherAlle       eine Liste aller vorhandenen Leistungen
	 * @param faecherNutzen     nur die gelisteten Fächer nutzen, null bedeutet grundsätzlich alle benoteten Fächer nutzen (außer den gefilterten)
	 * @param faecherFiltern    null bedeutet keinen Filter verwenden, ansonsten werden die gelisteten Fächer gefiltert
	 */
	public constructor(faecherAlle : List<GEAbschlussFach>, faecherNutzen : List<string> | null, faecherFiltern : List<string> | null) {
		super();
		if (faecherAlle === null)
			return;
		for (let i : number = 0; i < faecherAlle.size(); i++) {
			let fach : GEAbschlussFach = faecherAlle.get(i);
			if (fach.kuerzel === null)
				continue;
			if ((faecherFiltern !== null) && faecherFiltern.contains(fach.kuerzel))
				continue;
			if ((faecherNutzen !== null) && !faecherNutzen.contains(fach.kuerzel))
				continue;
			this.faecher.add(AbschlussManager.erstelleAbschlussFach(fach.kuerzel, fach.bezeichnung, fach.note, GELeistungsdifferenzierteKursart.from(fach.kursart), fach.istFremdsprache));
		}
	}

	/**
	 * Prüft, ob die gelisteten Fächer in der Fächergruppe sind und nur diese.
	 *
	 * @param faecherAbgleich   die Fächer, welche in der Fächergruppe sein sollen.
	 *
	 * @return true, falls die angegebenen Fächer und nur diese in der Fächergruppe sind, ansonsten false.
	 */
	public istVollstaendig(faecherAbgleich : List<string> | null) : boolean {
		if (faecherAbgleich === null)
			return true;
		if (this.isEmpty())
			return false;
		for (let kuerzel of faecherAbgleich) {
			if (!this.contains(kuerzel))
				return false;
		}
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (!faecherAbgleich.contains(fach.kuerzel))
				return false;
		}
		return true;
	}

	/**
	 * Gibt zurück, ob die Fächergruppe leer ist oder mindestens ein Fach beinhaltet.
	 *
	 * @return true, falls die Fächergruppe leer ist, ansonsten false
	 */
	public isEmpty() : boolean {
		return (this.faecher === null) || (this.faecher.isEmpty());
	}

	/**
	 * Prüft, ob das Fach mit dem angegebenen Fachkürzel in der Fächergruppe enthalten ist
	 * oder nicht.
	 *
	 * @param kuerzel   das Kürzel des Faches, welches geprüft werden soll.
	 *
	 * @return true, falls das Fach vorhanden ist, und ansonsten false
	 */
	public contains(kuerzel : string | null) : boolean {
		if (kuerzel === null)
			return false;
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if ((fach !== null) && (JavaObject.equalsTranspiler(fach.kuerzel, (kuerzel))))
				return true;
		}
		return false;
	}

	/**
	 * Entfernt alle Fächer aus der Fächergruppe, die dem übergebenen Filter entsprechen.
	 *
	 * @param filter   die Funktion, um die zu entfernenden Fächer zu bestimmen
	 *
	 * @return die Liste der tatsächlich entfernten Fächer
	 */
	public entferneFaecher(filter : Predicate<GEAbschlussFach>) : List<GEAbschlussFach> {
		let selected : Vector<GEAbschlussFach> = new Vector();
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach))
				selected.add(fach);
		}
		this.faecher.removeAll(selected);
		return selected;
	}

	/**
	 * Bestimmt das Fach, welches dem übergebenen Filter entspricht. Entsprechen
	 * mehrere Fächer dem Filterkriterium, so wird nur das erste Fach
	 * der internen Liste zurückgegeben.
	 *
	 * @param filter   die Funktion, die das Kriterium für das gesuchte Fach angibt.
	 *
	 * @return das Fach, sofern eines gefunden wurde, ansonsten false
	 */
	public getFach(filter : Predicate<GEAbschlussFach>) : GEAbschlussFach | null {
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach))
				return fach;
		}
		return null;
	}

	/**
	 * Bestimmt alle Fächer, welche dem übergebenen Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return eine Liste der Fächer, die dem Filterkriterium entsprechen
	 */
	public getFaecher(filter : Predicate<GEAbschlussFach>) : List<GEAbschlussFach> {
		let result : Vector<GEAbschlussFach> = new Vector();
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach))
				result.add(fach);
		}
		return result;
	}

	/**
	 * Gibt die Anzahl der Fächer zurück, welche dem übergebenen Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return die Anzahl der Fächer
	 */
	public getFaecherAnzahl(filter : Predicate<GEAbschlussFach>) : number {
		let count : number = 0;
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach))
				count++;
		}
		return count;
	}

	/**
	 * Bestimmt die Kürzel aller Fächer, welche dem übergebenen Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return eine Liste der Kürzel der Fächer, die dem Filterkriterium entsprechen
	 */
	public getKuerzel(filter : Predicate<GEAbschlussFach>) : List<string> {
		let result : Vector<string> = new Vector();
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach) && (fach.kuerzel !== null))
				result.add(fach.kuerzel);
		}
		return result;
	}

	/**
	 * Erstellt eine Zeichenkette mit einer Komma-separierten Liste der Kürzel aller Fächer,
	 * welche dem übergebenen Filterkriterium entsprechen.
	 *
	 * @param filter   die Funktion, die das Kriterium für die gesuchten Fächer angibt.
	 *
	 * @return die Zeichenkette mit einer Komma-separierten Liste der Fächerkürzel
	 */
	public getKuerzelListe(filter : Predicate<GEAbschlussFach>) : string {
		let sb : StringBuilder = new StringBuilder();
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (filter.test(fach)) {
				if (sb.length() > 0)
					sb.append(", ");
				sb.append(fach.kuerzel);
			}
		}
		return sb.toString();
	}

	/**
	 * Gibt eine Komma-separierte Liste, der Abschlussfächer aus. Dabei wird
	 * die toString Methode der Klasse AbschlussFach verwendet.
	 */
	public toString() : string {
		let sb : StringBuilder = new StringBuilder();
		for (let i : number = 0; i < this.faecher.size(); i++) {
			let fach : GEAbschlussFach = this.faecher.get(i);
			if (sb.length() > 0)
				sb.append(", ");
			let diffkursinfo : string = "";
			if ((fach.kursart === null) || (fach.kuerzel === null))
				continue;
			if (!GELeistungsdifferenzierteKursart.Sonstige.hat(fach.kursart))
				diffkursinfo += fach.kursart + ",";
			sb.append(fach.kuerzel + "(" + diffkursinfo! + fach.note + ")");
		}
		return sb.toString();
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.nrw.schule.svws.core.abschluss.ge.AbschlussFaecherGruppe'].includes(name);
	}

}

export function cast_de_nrw_schule_svws_core_abschluss_ge_AbschlussFaecherGruppe(obj : unknown) : AbschlussFaecherGruppe {
	return obj as AbschlussFaecherGruppe;
}
