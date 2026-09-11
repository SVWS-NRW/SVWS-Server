import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';

export class UvKursImportQuellkurs extends JavaObject {

	/**
	 * Die ID des Quellkurses.
	 */
	readonly id: number;

	/**
	 * Die ID des Fachs.
	 */
	readonly idFach: number;

	/**
	 * Die Kursart.
	 */
	readonly kursart: string;

	/**
	 * Die Kursnummer.
	 */
	readonly nummer: number;

	/**
	 * Die Wochenstunden.
	 */
	readonly wochenstunden: number;

	/**
	 * Die IDs der zugeordneten Lehrer.
	 */
	readonly lehrerIds: List<number> = new ArrayList<number>();

	/**
	 * Die IDs der zugeordneten Schüler.
	 */
	readonly schuelerIds: List<number> = new ArrayList<number>();

	/**
	 * Die IDs der zugeordneten Schienen.
	 */
	readonly schienenIds: List<number> = new ArrayList<number>();


	/**
	 * Erstellt einen normalisierten Quellkurs.
	 *
	 * @param id die ID des Quellkurses
	 * @param idFach die Fach-ID
	 * @param kursart die Kursart
	 * @param nummer die Kursnummer
	 * @param wochenstunden die Wochenstunden
	 */
	constructor(id: number, idFach: number, kursart: string, nummer: number, wochenstunden: number) {
		super();
		this.id = id;
		this.idFach = idFach;
		this.kursart = kursart;
		this.nummer = nummer;
		this.wochenstunden = wochenstunden;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.uv.UvKursImportQuellkurs';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.uv.UvKursImportQuellkurs'].includes(name);
	}

	public static readonly class = new Class<UvKursImportQuellkurs>('de.svws_nrw.core.utils.uv.UvKursImportQuellkurs');

}

export function cast_de_svws_nrw_core_utils_uv_UvKursImportQuellkurs(obj: unknown): UvKursImportQuellkurs {
	return obj as UvKursImportQuellkurs;
}
