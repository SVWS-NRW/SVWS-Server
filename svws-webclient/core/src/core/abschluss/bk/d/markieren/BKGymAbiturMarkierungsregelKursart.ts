import { GostAbiturFach } from '../../../../../core/types/gost/GostAbiturFach';
import { BKGymAbiturMarkierungsregel, cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsregel';
import { Class } from '../../../../../java/lang/Class';
import { DeveloperNotificationException } from '../../../../../core/exceptions/DeveloperNotificationException';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export class BKGymAbiturMarkierungsregelKursart extends BKGymAbiturMarkierungsregel {

	/**
	 * die Kursart: LK1, LK2, AB3, AB4, AB5
	 */
	readonly kursart: string;

	/**
	 * die erforderliche Anzahl
	 */
	readonly anzahl: number;


	/**
	 * erstellt eine Regel zur Markierung der geforderten Anzahl Kurse einer Kursart
	 *
	 * @param kursart        die Art des Kurses
	 * @param anzahl         die geforderte Anzahl an Kursen
	 * @param regelkuerzel   das eindeutige Kürzel dieser Regel
	 * @param hinweis        Hinweis für das log
	 * @param bezugAPOBK     Referenz in den APO-BK
	 */
	public constructor(kursart: string, anzahl: number, regelkuerzel: string, hinweis: string, bezugAPOBK: string) {
		super(regelkuerzel, null, hinweis, bezugAPOBK);
		this.kursart = kursart;
		this.anzahl = anzahl;
	}

	/**
	 * Führt die Markierung entsprechend der Kursart durch.
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		const abifach: GostAbiturFach | null = GostAbiturFach.fromKuerzel(this.kursart);
		if (abifach === null)
			throw new DeveloperNotificationException("Die Prüfbedingung " + this.kuerzel + " enthält die unzulässige Kursart '" + this.kursart + "'.")
		const abiFachID: number | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getAbiFachID(abifach);
		if (abiFachID === null) {
			variante.addLogEintrag(1, "Fehler: Eine entsprechende Belegung konnte nicht gefunden werden.");
			variante.setHatZulassung(false);
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => ((markierung !== null) && (markierung.fachID === abiFachID) && (markierung.punkte !== null) && (markierung.punkte > 0)) };
		const verbleibend: number = variante.markiereKursanzahl(this.anzahl, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, this.anzahl, 1);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKursart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel', 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKursart'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregelKursart>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregelKursart');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregelKursart(obj: unknown): BKGymAbiturMarkierungsregelKursart {
	return obj as BKGymAbiturMarkierungsregelKursart;
}
