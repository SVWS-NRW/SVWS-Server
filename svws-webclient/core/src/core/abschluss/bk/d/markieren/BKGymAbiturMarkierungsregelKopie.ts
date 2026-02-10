import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelKopie extends BKGymAbiturMarkierungsregel {

	/**
	 * die Kennung der Variante
	 */
	readonly kennung: string;

	/**
	 * ob für Facharbeit Kopie erstellt wird
	 */
	readonly facharbeit: boolean;


	/**
	 * erstellt eine Regel zur Erzeugung einer neuen Variante
	 *
	 * @param kennung        die Art des Kurses
	 * @param facharbeit     ob Kopie für Variante mit Facharbeit
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(kennung: string, facharbeit: boolean, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.kennung = kennung;
		this.facharbeit = facharbeit;
	}

	/**
	 * Führt die Markierung entsprechend der Kursart durch.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (this.facharbeit && !variante.varianten.abiturdatenManager.istFacharbeitVorhanden())
			return;
		variante.varianten.addVariante(new BKGymAbiturMarkierungsVariante(variante, this.kennung, this.facharbeit));
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKopie';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKopie'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelKopie>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKopie');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelKopie(obj: unknown): BKGymAbiturMarkierungsregelKopie {
	return obj as BKGymAbiturMarkierungsregelKopie;
}
