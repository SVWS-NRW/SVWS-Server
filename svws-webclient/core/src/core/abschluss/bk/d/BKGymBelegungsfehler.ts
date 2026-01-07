import { BKGymBelegungsfehlerArt } from '../../../../core/types/bk/BKGymBelegungsfehlerArt';
import { JavaObject } from '../../../../java/lang/JavaObject';
import { Class } from '../../../../java/lang/Class';
import { BKGymBelegungsfehlerTyp } from '../../../../core/types/bk/BKGymBelegungsfehlerTyp';

export class BKGymBelegungsfehler extends JavaObject {

	/**
	 * Der eindeutige Code des Belegungsfehlers
	 */
	public readonly code: string;

	/**
	 * Die Art des Fehlers
	 */
	public readonly art: BKGymBelegungsfehlerArt;

	/**
	 * Der Wert des Fehlers höhere Werte gleich schwerwiegenderer Fehler
	 */
	public readonly wert: number;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	public readonly text: string;


	/**
	 *  Erstellt einen Belegungsfehler mit den angegebenen Daten
	 *
	 * @param fehlertyp   der Belegungsfehler, der mit den args präzisiert wird
	 * @param args        die Parameter für den Belegungsfehler.
	 */
	public constructor(fehlertyp: BKGymBelegungsfehlerTyp, ...args: Array<unknown>) {
		super();
		this.code = fehlertyp.code;
		this.art = fehlertyp.art;
		this.wert = fehlertyp.wert;
		this.text = fehlertyp.format(...args);
	}

	/**
	 * Reicht die Information, ob es ein Fehler ist, durch
	 *
	 * @return true, wenn Fehler, sonst false
	 */
	public istFehler(): boolean {
		return (this.art as unknown !== BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	/**
	 * Reicht die Information, ob die Fehlerart HINWEIS vorliegt
	 *
	 * @return true, wenn Fehler, sonst false
	 */
	public istInfo(): boolean {
		return (this.art as unknown === BKGymBelegungsfehlerArt.HINWEIS as unknown);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler'].includes(name);
	}

	public static readonly class = new Class<BKGymBelegungsfehler>('de.svws_nrw.core.abschluss.bk.d.BKGymBelegungsfehler');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_BKGymBelegungsfehler(obj: unknown): BKGymBelegungsfehler {
	return obj as BKGymBelegungsfehler;
}
