import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelFremdspracheZweiteNur extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse der zweiten Fremdsprache
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param varKennung     Kennung der zu bearbeitenden Variante oder null für alle
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, varKennung: string | null, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, varKennung, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}

	/**
	 * Führt die Markierung der zweiten Fremdsprache durch mit der angegebenen Anzahl.
	 *
	 * Entweder als zwei Pflichtkurse zusätzlich zur ersten Fremdsprache
	 * oder nur die 4 Kurse der zweiten Fremdsprache. Englisch wird erstmal nicht eingebracht.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const zweiteFremdsprache: string | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (zweiteFremdsprache === null) {
			variante.addLogEintrag(1, "Fehler: Eine zweite Fremdsprache wurde nicht belegt.");
			variante.setHatZulassung(false);
			return;
		}
		BKGymAbiturMarkierungsregel.markiereFach(zweiteFremdsprache, this.anzahl, variante);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNur';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNur'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFremdspracheZweiteNur>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNur');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFremdspracheZweiteNur(obj: unknown): BKGymAbiturMarkierungsregelFremdspracheZweiteNur {
	return obj as BKGymAbiturMarkierungsregelFremdspracheZweiteNur;
}
