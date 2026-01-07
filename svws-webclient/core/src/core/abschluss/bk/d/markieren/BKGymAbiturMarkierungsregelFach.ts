import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelFach extends BKGymAbiturMarkierungsregel {

	/**
	 * die Bezeichnung des Fachs
	 */
	readonly fachbezeichnung: string;

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse eines Fachs
	 *
	 * @param fachbezeichnung   die Art des Kurses
	 * @param anzahl            die geforderte Anzahl an Kursen
	 * @param regelkuerzel      das eindeutige Kürzel dieser Regel
	 * @param hinweis           Hinweis für das log
	 * @param bezugAPOBK        Referenz in den APO-BK
	 */
	public constructor(fachbezeichnung: string, anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.fachbezeichnung = fachbezeichnung;
		this.anzahl = anzahl;
	}

	/**
	 * Führt die Markierung entsprechend des Fachs durch.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		BKGymAbiturMarkierungsregel.markiereFach(this.fachbezeichnung, this.anzahl, variante);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFach';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFach'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFach>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFach');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFach(obj: unknown): BKGymAbiturMarkierungsregelFach {
	return obj as BKGymAbiturMarkierungsregelFach;
}
