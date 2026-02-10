import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';

export class BKGymAbiturMarkierungsregelFremdspracheZweiteNeu extends BKGymAbiturMarkierungsregel {

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
	 * Markiert zusätzlich zur ersten Fremdsprache zwei Kurse der zweiten Fremdsprache, wenn die Belegung in der SekI nicht reicht.
	 * § 15 Abs. 3 Nr. 2 f) : zum Erwerb der allgemeinen Hochschulreife ergänzend zwei Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache,
	 * wenn Schülerinnen und Schüler in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.varianten.abiturdatenManager.getZweiteFremdspracheInSekIErfuellt()) {
			variante.addLogEintrag(1, "Die Belegung der zweiten Fremdsprache in der SekI sind ausreichend.");
			return;
		}
		const zweiteFremdsprache: string | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheBezeichnung();
		if (zweiteFremdsprache === null) {
			variante.addLogEintrag(1, "Fehler: Eine zweite Fremdsprache wurde nicht belegt.");
			variante.setHatZulassung(false);
			return;
		}
		BKGymAbiturMarkierungsregel.markiereFach(zweiteFremdsprache, this.anzahl, variante);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNeu';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNeu'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFremdspracheZweiteNeu>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdspracheZweiteNeu');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFremdspracheZweiteNeu(obj: unknown): BKGymAbiturMarkierungsregelFremdspracheZweiteNeu {
	return obj as BKGymAbiturMarkierungsregelFremdspracheZweiteNeu;
}
