import { JavaObject } from '../../../java/lang/JavaObject';
import { ArrayList } from '../../../java/util/ArrayList';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { StundenplanblockungManagerLerngruppe } from '../../../core/utils/stundenplanblockung/StundenplanblockungManagerLerngruppe';

export class StundenplanblockungManagerKlasse extends JavaObject {

	/**
	 * Die Datenbank-ID der Klasse.
	 */
	_id: number = 0;

	/**
	 * Das Kürzel der Klasse. Beispielsweise '05a' oder 'Q1'.
	 */
	_kuerzel: string = "";

	/**
	 * Alle Lerngruppen der Klasse.
	 */
	_menge_gr: List<StundenplanblockungManagerLerngruppe>;


	/**
	 * Erzeugt eine neue Klasse.
	 *
	 * @param pKlasseID  Die Datenbank-ID der Klasse.
	 * @param pKuerzel   Das Kürzel der Klasse.
	 */
	public constructor(pKlasseID: number, pKuerzel: string) {
		super();
		this._id = pKlasseID;
		this._kuerzel = pKuerzel;
		this._menge_gr = new ArrayList();
	}

	/**
	 * Liefert die Datenbank-ID der Klasse.
	 *
	 * @return Die Datenbank-ID der Klasse.
	 */
	public getID(): number {
		return this._id;
	}

	/**
	 * Setzt das Kürzel der Klasse.
	 *
	 * @param pKuerzel  Das neue Kürzel der Klasse.
	 */
	public setKuerzel(pKuerzel: string): void {
		this._kuerzel = pKuerzel;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse'].includes(name);
	}

	public static readonly class = new Class<StundenplanblockungManagerKlasse>('de.svws_nrw.core.utils.stundenplanblockung.StundenplanblockungManagerKlasse');

}

export function cast_de_svws_nrw_core_utils_stundenplanblockung_StundenplanblockungManagerKlasse(obj: unknown): StundenplanblockungManagerKlasse {
	return obj as StundenplanblockungManagerKlasse;
}
