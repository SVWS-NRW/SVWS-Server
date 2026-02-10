import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelFremdsprachePruefeNeue extends BKGymAbiturMarkierungsregel {

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Prüfung der Belegung der zweiten Fremdsprache in der Qualifikationsphase
	 *
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.anzahl = anzahl;
	}

	/**
	 * Prüft, dass alle 4 neuen Fremdsprachenkurse mehr als 0 Punkte haben, wenn keine ausreichende Fremdsprachenbelegung in SEK-I.
	 * Gegebenenfalls wird der Erfolg der Variante auf false gesetzt.
	 * § 15 Abs. 2 Nr. 4 : Schülerinnen und Schüler, die in der Sekundarstufe I keinen durchgängigen Unterricht in einer zweiten Fremdsprache im Umfang
	 * von mindestens vier Jahren erhalten haben, dürfen zum Erwerb der allgemeinen Hochschulreife in keinem der vier in der Qualifikationsphase
	 * belegten Kurse der in der Jahrgangsstufe 11 neu einsetzenden Fremdsprache mit null Punkten bewertet worden sein.
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.varianten.abiturdatenManager.getZweiteFremdspracheInSekIErfuellt()) {
			variante.addLogEintrag(1, "Ausreichende Belegung einer zweiten Fremdsprache in der SekI liegt vor.");
			return;
		}
		const zweiteFremdspracheID: number | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getZweiteFremdspracheID();
		if (zweiteFremdspracheID === null) {
			variante.addLogEintrag(1, "Fehler: Es fehlt die Belegung der zweiten Fremdsprache, da nicht in der SekI abgedeckt.");
			variante.setHatZulassung(false);
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (markierung.fachID === zweiteFremdspracheID) && (markierung.punkte !== null) && (markierung.punkte > 0) };
		const verbleibend: number = variante.pruefeKursanzahl(this.anzahl, bedingung);
		if (verbleibend === 0)
			variante.addLogEintrag(1, "Alle Kurshalbjahre in der Qualifikationsphase mit mindestens einem Punkt abgeschlossen.");
		else {
			variante.addLogEintrag(1, "Fehler: Nur " + (this.anzahl - verbleibend) + " von " + this.anzahl + " Kursen haben mehr als 0 Punkte.");
			variante.setHatZulassung(false);
		}
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdsprachePruefeNeue';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdsprachePruefeNeue'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelFremdsprachePruefeNeue>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelFremdsprachePruefeNeue');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelFremdsprachePruefeNeue(obj: unknown): BKGymAbiturMarkierungsregelFremdsprachePruefeNeue {
	return obj as BKGymAbiturMarkierungsregelFremdsprachePruefeNeue;
}
