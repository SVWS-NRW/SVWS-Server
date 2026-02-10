import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelFremdspracheErste extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse der ersten Fremdsprache
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, varKennung: string | null, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}

	/**
	 * Markiert 4 Kurse der ersten Fremdsprache (immer Englisch)
	 * § 15 Abs. 3 Nr. 2 b) : vier Kurse der aus der Sekundarstufe I fortgeführten (oder der in der Jahrgangsstufe 11 neu einsetzende Fremdsprache)
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		BKGymAbiturMarkierungsregel.markiereFach("Englisch", this.anzahl, variante);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheErste';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheErste'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFremdspracheErste>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheErste');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFremdspracheErste(obj: unknown): BKGymAbiturMarkierungsregelFremdspracheErste {
	return obj as BKGymAbiturMarkierungsregelFremdspracheErste;
}
