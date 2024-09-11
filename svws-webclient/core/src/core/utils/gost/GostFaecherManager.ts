import { JavaObject } from '../../../java/lang/JavaObject';
import { GostFach, cast_de_svws_nrw_core_data_gost_GostFach } from '../../../core/data/gost/GostFach';
import { Fach } from '../../../asd/types/fach/Fach';
import { Fachgruppe } from '../../../asd/types/fach/Fachgruppe';
import { HashMap } from '../../../java/util/HashMap';
import { ArrayList } from '../../../java/util/ArrayList';
import { JavaString } from '../../../java/lang/JavaString';
import { DeveloperNotificationException } from '../../../core/exceptions/DeveloperNotificationException';
import { GostLaufbahnplanungFachkombinationTyp } from '../../../core/types/gost/GostLaufbahnplanungFachkombinationTyp';
import type { Comparator } from '../../../java/util/Comparator';
import { GostFachbereich } from '../../../core/types/gost/GostFachbereich';
import { GostJahrgangFachkombination, cast_de_svws_nrw_core_data_gost_GostJahrgangFachkombination } from '../../../core/data/gost/GostJahrgangFachkombination';
import type { Collection } from '../../../java/util/Collection';
import type { List } from '../../../java/util/List';
import { cast_java_util_List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { FachKatalogEintrag } from '../../../asd/data/fach/FachKatalogEintrag';

export class GostFaecherManager extends JavaObject {

	/**
	 * Sortiert die Fächer anhand ihrer konfigurierten Sortierung
	 */
	public static readonly comp : Comparator<GostFach | null> = { compare : (a: GostFach | null, b: GostFach | null) => GostFachbereich.compareGostFach(a, b) };

	/**
	 * das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 */
	private readonly schuljahr : number;

	/**
	 * Die Liste der Fächer, die im Manager vorhanden sind.
	 */
	private readonly _faecher : List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf ein Fach anhand der ID
	 */
	private readonly _map : HashMap<number, GostFach> = new HashMap<number, GostFach>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fächer anhand des Statistik-Kürzels des Faches
	 */
	private readonly _mapByKuerzel : HashMap<string, List<GostFach>> = new HashMap<string, List<GostFach>>();

	/**
	 * Eine HashMap für den schnellen Zugriff auf die Fremdsprachen-Fächer anhand des Sprachenkürzels
	 */
	private readonly _mapBySprachkuerzel : HashMap<string, List<GostFach>> = new HashMap<string, List<GostFach>>();

	/**
	 * Eine Map für den schnellen Zugriff auf die Fächer, welche als Leitfächer zur Verfügung stehen.
	 */
	private readonly _leitfaecher : List<GostFach> = new ArrayList<GostFach>();

	/**
	 * Die Liste der erforderlichen oder nicht erlaubten Fachkombinationen
	 */
	private readonly _fachkombis : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();

	/**
	 * Die Liste mit den geforderten Fachkombinationen
	 */
	private readonly _fachkombisErforderlich : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();

	/**
	 * Die Liste mit den nicht erlaubten Fachkombinationen
	 */
	private readonly _fachkombisVerboten : List<GostJahrgangFachkombination> = new ArrayList<GostJahrgangFachkombination>();


	/**
	 * Erstelle einen neuen Manager mit einer leeren Fächerliste
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 */
	public constructor(schuljahr : number);

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher   die Liste mit den Fächern
	 */
	public constructor(schuljahr : number, faecher : List<GostFach>);

	/**
	 * Erstellt einen neuen Manager mit den übergebenen Fächern und den
	 * übergebenen geforderten und nicht erlaubten Fächerkombinationen.
	 *
	 * @param schuljahr    das Schuljahr, für welches der Fächer-Manager die Fächer verwaltet
	 * @param faecher      die Liste mit den Fächern
	 * @param fachkombis   die Liste mit den Fächerkombinationen
	 */
	public constructor(schuljahr : number, faecher : List<GostFach>, fachkombis : List<GostJahrgangFachkombination>);

	/**
	 * Implementation for method overloads of 'constructor'
	 */
	public constructor(__param0 : number, __param1? : List<GostFach>, __param2? : List<GostJahrgangFachkombination>) {
		super();
		if (((__param0 !== undefined) && typeof __param0 === "number") && (__param1 === undefined) && (__param2 === undefined)) {
			const schuljahr : number = __param0 as number;
			this.schuljahr = schuljahr;
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && (__param2 === undefined)) {
			const schuljahr : number = __param0 as number;
			const faecher : List<GostFach> = cast_java_util_List(__param1);
			this.schuljahr = schuljahr;
			this.addAll(faecher);
		} else if (((__param0 !== undefined) && typeof __param0 === "number") && ((__param1 !== undefined) && ((__param1 instanceof JavaObject) && (__param1.isTranspiledInstanceOf('java.util.List'))) || (__param1 === null)) && ((__param2 !== undefined) && ((__param2 instanceof JavaObject) && (__param2.isTranspiledInstanceOf('java.util.List'))) || (__param2 === null))) {
			const schuljahr : number = __param0 as number;
			const faecher : List<GostFach> = cast_java_util_List(__param1);
			const fachkombis : List<GostJahrgangFachkombination> = cast_java_util_List(__param2);
			this.schuljahr = schuljahr;
			this.addAll(faecher);
			this.addFachkombinationenAll(fachkombis);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Gibt das Schuljahr des Managers zurück, d.h. das Schuljahr, für welches die Fächer der Oberstufe verwaltet werden.
	 *
	 * @return das Schuljahr
	 */
	public getSchuljahr() : number {
		return this.schuljahr;
	}

	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu. Die interne Sortierung wird nicht korrigiert.
	 *
	 * @param fach        das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die ID des Faches negativ ist.
	 */
	private addFachInternal(fach : GostFach) : boolean {
		DeveloperNotificationException.ifSmaller("fach.id", fach.id, 0);
		if (this._map.containsKey(fach.id))
			return false;
		const zf : Fach | null = Fach.getBySchluesselOrDefault(fach.kuerzel);
		const fke : FachKatalogEintrag | null = zf.daten(this.schuljahr);
		if (fke === null)
			return false;
		this._map.put(fach.id, fach);
		let listForKuerzel : List<GostFach> | null = this._mapByKuerzel.get(fach.kuerzel);
		if (listForKuerzel === null) {
			listForKuerzel = new ArrayList();
			this._mapByKuerzel.put(fach.kuerzel, listForKuerzel);
		}
		listForKuerzel.add(fach);
		if (fach.istFremdsprache && fke.istFremdsprache) {
			let listForSprachkuerzel : List<GostFach> | null = this._mapBySprachkuerzel.get(fke.kuerzel);
			if (listForSprachkuerzel === null) {
				listForSprachkuerzel = new ArrayList();
				this._mapBySprachkuerzel.put(fke.kuerzel, listForSprachkuerzel);
			}
			listForSprachkuerzel.add(fach);
		}
		const added : boolean = this._faecher.add(fach);
		if (!GostFachbereich.LITERARISCH_KUENSTLERISCH_ERSATZ.hat(fach)) {
			const fg : Fachgruppe | null = Fach.getBySchluesselOrDefault(fach.kuerzel).getFachgruppe(this.schuljahr);
			if ((fg as unknown !== Fachgruppe.FG_VX as unknown) && (fg as unknown !== Fachgruppe.FG_PX as unknown))
				this._leitfaecher.add(fach);
		}
		return added;
	}

	/**
	 * Führt eine Sortierung der Fächer anhand des Sortierungsfeldes durch.
	 */
	private sort() : void {
		this._faecher.sort(GostFaecherManager.comp);
		this._leitfaecher.sort(GostFaecherManager.comp);
	}

	/**
	 * Fügt die übergebene Fachkombination zu diesem Manager hinzu.
	 *
	 * @param fachkombi   die hinzuzufügende Fachkombination
	 *
	 * @return true, falls die Fachkombination hinzugefügt wurde
	 *
	 * @throws DeveloperNotificationException Falls die Fachkombination nicht zu den Fächern des Managers passt.
	 */
	private addFachkombinationInternal(fachkombi : GostJahrgangFachkombination) : boolean {
		DeveloperNotificationException.ifSmaller("fachkombi.fachID1", fachkombi.fachID1, 0);
		DeveloperNotificationException.ifSmaller("fachkombi.fachID2", fachkombi.fachID2, 0);
		DeveloperNotificationException.ifMapNotContains("_map", this._map, fachkombi.fachID1);
		DeveloperNotificationException.ifMapNotContains("_map", this._map, fachkombi.fachID2);
		DeveloperNotificationException.ifNotInRange("fachkombi.typ", fachkombi.typ, 0, 1);
		const typ : GostLaufbahnplanungFachkombinationTyp = GostLaufbahnplanungFachkombinationTyp.fromValue(fachkombi.typ);
		if (JavaString.isBlank(fachkombi.hinweistext)) {
			const fach1 : GostFach = this.getOrException(fachkombi.fachID1);
			const fach2 : GostFach = this.getOrException(fachkombi.fachID2);
			const kursart1 : string = ((fachkombi.kursart1 === null) || JavaString.isBlank(fachkombi.kursart1)) ? "" : (" als " + fachkombi.kursart1);
			const kursart2 : string = ((fachkombi.kursart2 === null) || JavaString.isBlank(fachkombi.kursart2)) ? "" : (" als " + fachkombi.kursart2);
			fachkombi.hinweistext = fach1.kuerzelAnzeige + kursart1! + ((typ as unknown === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH as unknown) ? " erfordert " : " erlaubt kein ") + fach2.kuerzelAnzeige + kursart2!;
		}
		if (typ as unknown === GostLaufbahnplanungFachkombinationTyp.ERFORDERLICH as unknown) {
			this._fachkombisErforderlich.add(fachkombi);
		} else
			if (typ as unknown === GostLaufbahnplanungFachkombinationTyp.VERBOTEN as unknown) {
				this._fachkombisVerboten.add(fachkombi);
			}
		return this._fachkombis.add(fachkombi);
	}

	/**
	 * Fügt das übergebene Fach zu diesem Manager hinzu und
	 * passt intern die Sortierung der Fächer an.
	 *
	 * @param fach   das hinzuzufügende Fach
	 *
	 * @return true, falls das Fach hinzugefügt wurde
	 */
	public add(fach : GostFach) : boolean;

	/**
	 * Fügt die geforderten oder nicht erlaubte Fächerkombination zu diesem
	 * Manager hinzu.
	 *
	 * @param fachkombi   das hinzuzufügende Fachkombination
	 *
	 * @return true, falls die Fachkombination hinzugefügt wurde
	 */
	public add(fachkombi : GostJahrgangFachkombination) : boolean;

	/**
	 * Implementation for method overloads of 'add'
	 */
	public add(__param0 : GostFach | GostJahrgangFachkombination) : boolean {
		if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostFach'))))) {
			const fach : GostFach = cast_de_svws_nrw_core_data_gost_GostFach(__param0);
			const result : boolean = this.addFachInternal(fach);
			this.sort();
			return result;
		} else if (((__param0 !== undefined) && ((__param0 instanceof JavaObject) && (__param0.isTranspiledInstanceOf('de.svws_nrw.core.data.gost.GostJahrgangFachkombination'))))) {
			const fachkombi : GostJahrgangFachkombination = cast_de_svws_nrw_core_data_gost_GostJahrgangFachkombination(__param0);
			return this.addFachkombinationInternal(fachkombi);
		} else throw new Error('invalid method overload');
	}

	/**
	 * Fügt die Fächer in der übergeben Liste zu diesem Manager hinzu.
	 *
	 * @param faecher   die hinzuzufügenden Fächer
	 *
	 * @return true, falls <i>alle</i> Fächer eingefügt wurden, sonst false
	 */
	public addAll(faecher : Collection<GostFach>) : boolean {
		let result : boolean = true;
		for (const fach of faecher)
			if (!this.addFachInternal(fach))
				result = false;
		this.sort();
		return result;
	}

	/**
	 * Fügt die geforderten und nicht erlaubten Fächerkombinationen in der übergebenen
	 * Liste zu diesem Manager hinzu.
	 *
	 * @param fachkombis   die hinzuzufügenden Fachkombinationen
	 *
	 * @return true, falls <i>alle</i> Fachkombinationen eingefügt wurden, sonst false
	 */
	public addFachkombinationenAll(fachkombis : List<GostJahrgangFachkombination>) : boolean {
		let result : boolean = true;
		for (const fachkombi of fachkombis)
			if (!this.addFachkombinationInternal(fachkombi))
				result = false;
		return result;
	}

	/**
	 * Gibt das Fach mit der angegebenen ID zurück oder null, falls es das Fach nicht gibt.
	 *
	 * @param id   die ID des gesuchten Faches
	 *
	 * @return Das fach mit der angegebenen ID oder null, falls es das Fach nicht gibt.
	 */
	public get(id : number) : GostFach | null {
		return this._map.get(id);
	}

	/**
	 * Liefert das Fach mit der angegebenen ID zurück.
	 *
	 * @param idFach   die ID des gesuchten Faches.
	 *
	 * @return Das Fach mit der angegebenen ID zurück.
	 *
	 * @throws DeveloperNotificationException Falls ein Fach mit der ID nicht bekannt ist.
	 */
	public getOrException(idFach : number) : GostFach {
		return DeveloperNotificationException.ifMapGetIsNull(this._map, idFach);
	}

	/**
	 * Liefert die Liste der Fächer für das angegebene Statistik-Kürzel zurück.
	 *
	 * @param kuerzel   das Statistik-Kürzel des gesuchten Faches
	 *
	 * @return eine Liste der Fächer, welche das angegebene Statistik-Kürzel haben
	 */
	public getByKuerzel(kuerzel : string) : List<GostFach> {
		const faecher : List<GostFach> | null = this._mapByKuerzel.get(kuerzel);
		return (faecher === null) ? new ArrayList() : faecher;
	}

	/**
	 * Liefert die Liste der Fächer für das angegebene Sprachkürzel zurück.
	 *
	 * @param sprache   das Sprachkürzel der gesuchten Sprache
	 *
	 * @return eine Liste der Fächer, welche das angegebene Sprachkürzel haben
	 */
	public getBySprachkuerzel(sprache : string) : List<GostFach> {
		const faecher : List<GostFach> | null = this._mapBySprachkuerzel.get(sprache);
		return (faecher === null) ? new ArrayList() : faecher;
	}

	/**
	 * Gibt zurück, ob die Liste der Fächer leer ist
	 *
	 * @return true, wenn die Liste der Fächer leer ist.
	 */
	public isEmpty() : boolean {
		return this._faecher.isEmpty();
	}

	/**
	 * Liefert die interne Liste der Fächer. Diese sollte nicht
	 * verändert werden.
	 *
	 * @return die interne Liste der Fächer
	 */
	public faecher() : List<GostFach> {
		return new ArrayList<GostFach>(this._faecher);
	}

	/**
	 * Liefert die Liste der Fächer, die nur die schriftlich möglichen Fächer enthält.
	 *
	 * @return die Liste der schriftlich möglichen Fächer
	 */
	public getFaecherSchriftlichMoeglich() : List<GostFach> {
		const faecherSchriftlichMoeglich : List<GostFach> = new ArrayList<GostFach>();
		for (const f of this._faecher) {
			const zf : Fach | null = Fach.getBySchluesselOrDefault(f.kuerzel);
			if ((zf as unknown === Fach.PX as unknown) || (zf as unknown === Fach.VX as unknown) || (zf as unknown === Fach.VO as unknown) || (zf as unknown === Fach.IN as unknown))
				continue;
			faecherSchriftlichMoeglich.add(f);
		}
		return faecherSchriftlichMoeglich;
	}

	/**
	 * Liefert die interne Liste mit den Leitfächern zurück.
	 *
	 * @return die interne Liste mit den Leitfächern
	 */
	public getLeitfaecher() : List<GostFach> {
		return this._leitfaecher;
	}

	/**
	 * Gibt eine Liste aller Fremdsprachen-Kürzel zurück, welche bei
	 * den im Manager enthaltenen Fächer definiert sind.
	 *
	 * @return die Liste der Fremdsprachen-Kürzel
	 */
	public getFremdsprachenkuerzel() : List<string> {
		const result : List<string> = new ArrayList<string>();
		result.addAll(this._mapBySprachkuerzel.keySet());
		result.sort({ compare : (a: string, b: string) => JavaString.compareToIgnoreCase(a, b) });
		return result;
	}

	/**
	 * Liefert die interne Liste mit den Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den Fachkombinationen
	 */
	public getFachkombinationen() : List<GostJahrgangFachkombination> {
		return this._fachkombis;
	}

	/**
	 * Liefert die interne Liste mit den geforderten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den geforderten Fachkombinationen
	 */
	public getFachkombinationenErforderlich() : List<GostJahrgangFachkombination> {
		return this._fachkombisErforderlich;
	}

	/**
	 * Liefert die interne Liste mit den nicht erlaubten Fachkombinationen zurück.
	 *
	 * @return die interne Liste mit den nicht erlaubten Fachkombinationen
	 */
	public getFachkombinationenVerboten() : List<GostJahrgangFachkombination> {
		return this._fachkombisVerboten;
	}

	/**
	 * Gibt an, ob es sich bei dem Fach mit der übergebenen ID um ein Projektkursfach handelt oder nicht.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return true, wenn es sich um ein Projektkurs-Fach handelt und ansonsten false.
	 */
	public fachIstProjektkurs(id : number) : boolean {
		const fach : GostFach | null = this._map.get(id);
		if (fach === null)
			return false;
		return JavaObject.equalsTranspiler("PX", (fach.kuerzel));
	}

	/**
	 * Gibt an, ob es sich bei dem Fach mit der übergebenen ID um einen Vertiefungskurs handelt oder nicht.
	 *
	 * @param id   die ID des Faches
	 *
	 * @return true, wenn es sich um einen Vertiefungskurs handelt und ansonsten false.
	 */
	public fachIstVertiefungskurs(id : number) : boolean {
		const fach : GostFach | null = this._map.get(id);
		if (fach === null)
			return false;
		return JavaObject.equalsTranspiler("VX", (fach.kuerzel));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.GostFaecherManager';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.GostFaecherManager'].includes(name);
	}

	public static class = new Class<GostFaecherManager>('de.svws_nrw.core.utils.gost.GostFaecherManager');

}

export function cast_de_svws_nrw_core_utils_gost_GostFaecherManager(obj : unknown) : GostFaecherManager {
	return obj as GostFaecherManager;
}
