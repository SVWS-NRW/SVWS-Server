import { JavaObject } from '../../java/lang/JavaObject';
import type { JavaSet } from '../../java/util/JavaSet';
import { java_util_Set_of } from '../../java/util/JavaSet';
import { Class } from '../../java/lang/Class';

export class NamensManager extends JavaObject {

	/**
	 *   Namenszusätze in Europa:
	 *   Ist noch mit IT.NRW abzustimmen, was davon umgesetzt werden soll.
	 *   Deutschsprachiger Raum : von, zu, vom, vonder, zum, zur
	 *   Niederlande : van, van de, van der, van den, de, ten
	 *   Belgien : de, de la, de l’, van, van der,
	 *   Frankreich : de, du, des, de la, le, la
	 *   Spanien : de, del, de la, los, las, y
	 *   Italien : di, della, del, dei, da
	 *   Portugal : de, da, do, dos, das
	 *   Großbritannien : of, ap (walisisch), fitz (anglo-normannisch),
	 *   Skandinavien : af, von, son, dotter
	 *   Polen : z, de
	 *   Ungarn : de, von, fi
	 *   Russland und Osteuropa : von, de
	 *
	 *   Gesamt:
	 *   1-teilig: af, ap, da, das, de, dei, del, della, des, di, do, dos, dotter, du, fi, fitz, la, las, le, los, of, son, ten, van, vom, von, vonder, y, z, zu, zum, zur
	 *   2-teilig: de la, de l’, van de, van den, van der
	 */
	private static readonly zusaetze: JavaSet<string> = java_util_Set_of("de", "te", "zu", "da", "von", "van", "vom", "thor");

	private static readonly zusaetzeZweiteilig: JavaSet<string> = java_util_Set_of("de la");


	public constructor() {
		super();
	}

	/**
	 * Entfernt ggf. die in "zusaetze" oder "zusaetzeZweiteilig" aufgeführten Zusätze, welche dem Nachnamen
	 * vorangestellt sein können. Diese Methode wird zur Prüfung des Anfangsbuchstabens des Nachnamens
	 * verwendet.
	 *
	 * @param nachname   der Nachname
	 *
	 * @return der Nachname mit ggf. entferntem Vornamen
	 */
	public static getOhneZusatz(nachname: string | null): string {
		if (nachname === null)
			return "";
		const teile: Array<string> = nachname.split(" ", 3);
		if (teile.length === 3 && NamensManager.zusaetzeZweiteilig.contains(teile[0] + " " + teile[1]))
			return teile[2];
		if (teile.length === 3 && NamensManager.zusaetze.contains(teile[0]))
			return teile[1] + " " + teile[2];
		if (teile.length === 2 && NamensManager.zusaetze.contains(teile[0]))
			return teile[1];
		return nachname;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.NamensManager';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.NamensManager'].includes(name);
	}

	public static readonly class = new Class<NamensManager>('de.svws_nrw.asd.validate.NamensManager');

}

export function cast_de_svws_nrw_asd_validate_NamensManager(obj: unknown): NamensManager {
	return obj as NamensManager;
}
