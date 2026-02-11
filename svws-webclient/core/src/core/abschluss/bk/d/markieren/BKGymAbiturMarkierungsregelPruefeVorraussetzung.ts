import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelPruefeVorraussetzung extends BKGymAbiturMarkierungsregel {


	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
	}

	/**
	 * Prüft, dass alle sechs Halbjahre gewertet sind.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.varianten.abiturdatenManager.istBewertetQualifikationsPhase()) {
			variante.addLogEintrag(1, "Alle Halbjahre sind gewertet.");
			return;
		}
		variante.addLogEintrag(1, "Nicht alle Halbjahre sind gewertet, Markierung der Kurse nicht möglich.");
		variante.setHatZulassung(false);
		variante.setGestoppt(true);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelPruefeVorraussetzung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelPruefeVorraussetzung', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelPruefeVorraussetzung>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelPruefeVorraussetzung');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelPruefeVorraussetzung(obj: unknown): BKGymAbiturMarkierungsregelPruefeVorraussetzung {
	return obj as BKGymAbiturMarkierungsregelPruefeVorraussetzung;
}
