import { JavaObject } from '../../../../../java/lang/JavaObject';
import { Class } from '../../../../../java/lang/Class';
import { BKGymAbiturMarkierungsVariante } from '../../../../../core/abschluss/bk/d/markieren/BKGymAbiturMarkierungsVariante';
import type { Predicate } from '../../../../../java/util/function/Predicate';
import { BKGymAbiturMarkierungsalgorithmusMarkierung } from '../../../../../core/data/bk/abi/BKGymAbiturMarkierungsalgorithmusMarkierung';

export abstract class BKGymAbiturMarkierungsregel extends JavaObject {

	/**
	 * Das eindeutige Kürzel der Regel
	 */
	protected readonly kuerzel: string;

	/**
	 * Die Kennung der zu bearbeitenden Variante, null für alle
	 */
	protected readonly kennungVariante: string | null;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	protected readonly hinweis: string;

	/**
	 * Der Text des Fehlers, der ausgegeben wird
	 */
	protected readonly bezugAPOBK: string;


	/**
	 * Erzeugt eine Markierungsregel-Objekt.
	 * Kindklassen enthalten weitere regelspezifische Attribute
	 *
	 * @param kuerzel     das eindeutige Kürzel dieser Regel
	 * @param varKennung  Kennung der zu bearbeitenden Variante oder null für alle
	 * @param hinweis     Hinweis für das log
	 * @param bezugAPOBK  Referenz in den APO-BK
	 */
	protected constructor(kuerzel: string, varKennung: string | null, hinweis: string, bezugAPOBK: string) {
		super();
		this.kuerzel = kuerzel;
		this.kennungVariante = varKennung;
		this.hinweis = hinweis;
		this.bezugAPOBK = bezugAPOBK;
	}

	/**
	 * Die Methode, die die Markierung durchführt
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	public markiere(variante: BKGymAbiturMarkierungsVariante): void {
		// empty block
	}

	/**
	 * Prüft ob die Regel für die Variante angewendet werden soll und gibt dann auch
	 * die Regel ins Log aus.
	 *
	 * @param variante   die zu bearbeitende Variante
	 */
	public exec(variante: BKGymAbiturMarkierungsVariante): void {
		if (variante.istGestoppt())
			return;
		if ((this.kennungVariante !== null) && !JavaObject.equalsTranspiler(this.kennungVariante, (variante.getKennung())))
			return;
		variante.addLogEintrag(0, "Regel " + this.kuerzel + ": " + this.hinweis + " entsprechend " + this.bezugAPOBK);
		this.markiere(variante);
	}

	/**
	 * Markiert Kurse entsprechend der Fachbezeichnung.
	 * Ist hier implementiert, da von mehreren Regeln benötigt.
	 *
	 * @param fachbezeichnung   die Bezeichnung des Fachs
	 * @param anzahl            die Anzahl zu markierender Kurse
	 * @param variante          die Variante auf der markiert wird
	 */
	public static markiereFach(fachbezeichnung: string, anzahl: number, variante: BKGymAbiturMarkierungsVariante): void {
		const fachID: number | null = variante.varianten.abiturdatenManager.getFachbelegungManager().getFachIDByBezeichnung(fachbezeichnung);
		if (fachID === null) {
			variante.addLogEintrag(1, "Fehler: Eine entsprechende Belegung konnte nicht gefunden werden.");
			variante.setHatZulassung(false);
			return;
		}
		const bedingung: Predicate<BKGymAbiturMarkierungsalgorithmusMarkierung> = { test: (markierung: BKGymAbiturMarkierungsalgorithmusMarkierung | null) => (markierung !== null) && (fachID === markierung.fachID) && (markierung.punkte !== null) && (markierung.punkte > 0) };
		const anzBereitsMarkiert: number = variante.zaehleMarkierte(bedingung);
		let verbleibend: number = anzahl;
		if (anzBereitsMarkiert > 0) {
			if (anzBereitsMarkiert >= verbleibend) {
				variante.addLogEintrag(1, "Die erforderliche Anzahl an Kursen ist bereits markiert.");
				return;
			}
			variante.addLogEintrag(1, "" + anzBereitsMarkiert + " Kurse sind bereits markiert.");
			verbleibend -= anzBereitsMarkiert;
		}
		verbleibend = variante.markiereKursanzahl(verbleibend, bedingung);
		variante.addLogAnzahlMarkierungen(verbleibend, anzahl, 1);
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel'].includes(name);
	}

	public static readonly class = new Class<BKGymAbiturMarkierungsregel>('de.svws_nrw.core.abschluss.bk.d.markieren.BKGymAbiturMarkierungsregel');

}

export function cast_de_svws_nrw_core_abschluss_bk_d_markieren_BKGymAbiturMarkierungsregel(obj: unknown): BKGymAbiturMarkierungsregel {
	return obj as BKGymAbiturMarkierungsregel;
}
