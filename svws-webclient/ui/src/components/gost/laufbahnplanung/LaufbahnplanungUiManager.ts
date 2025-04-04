import { computed } from "vue";
import { AbiturdatenManager, DeveloperNotificationException, GostHalbjahr } from "../../../../../core/src";

/**
 * Laufbahnplanung Oberstufe: Ein Manager für die Verwaltung von UI-Spezifischen Funktionen
 * auf die aktuellen Laufbahn-Beratungsdaten eines Schülers.
 */
export class LaufbahnplanungUiManager {

	/** Der Manager für die Abiturdaten, welcher auch die Belegprüfung durchführt. */
	private manager: () => AbiturdatenManager;

	/** Computed Property: Ein Array mit der Anzahl von anrechenbaren Kursen in den einzelnen Halbjahren */
	private _anrechenbareKurse = computed<number[]>(() => this.manager().getAnrechenbareKurse());

	/** Computed Property: Die Anzahl der anrechenbaren Kurse für Block I des Abiturs */
	public _summeAnrechenbareKurse = computed<number>(() => this.manager().getAnrechenbareKurseBlockI());

	/** Computed Property: Die Wochenstunden, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden. */
	public _wochenstunden = computed<number[]>(() => this.manager().getWochenstunden());

	/** Computed Property: Die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe. */
	public _wochenstundenJahressumme = computed<number>(() => this._wochenstunden.value.reduce((p, c) => p + c, 0) / 2);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase */
	public _wochenstundenDurchschnittEF = computed<number>(() => this.manager().getWochenstundenEinfuehrungsphase() / 2);

	/** Computed Property: Die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase */
	public _wochenstundenDurchschnittQ = computed<number>(() => this.manager().getWochenstundenQualifikationsphase() / 4);


	/**
	 * Erstellt einen neuen UI-Manager auf Basis des übergebenen Abiturdaten-Managers,
	 * welcher die Belegprüfung durchführt.
	 *
	 * @param manager   der Abiturdaten-Manager zur Durchführung der Belegprüfung
	 */
	public constructor(manager: () => AbiturdatenManager) {
		this.manager = manager;
	}


	/**
	 * Gibt die Anzahl der Kurs für das übergebene Halbjahr zurück.
	 */
	public getAnrechenbareKurse(hj : GostHalbjahr) : number {
		return this._anrechenbareKurse.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Kurse genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getAnrechenbareKurseCSS(hj : GostHalbjahr) : string {
		const kurse = this.getAnrechenbareKurse(hj);
		if (hj.istEinfuehrungsphase()) {
			if (kurse < 10)
				return 'svws-ergebnis--not-enough';
			if ((kurse > 9) && (kurse < 11))
				return 'svws-ergebnis--low';
			if ((kurse > 10) && (kurse < 13))
				return 'svws-ergebnis--good';
			return 'svws-ergebnis--more';
		}
		if (kurse < 9)
			return 'svws-ergebnis--not-enough';
		if ((kurse > 8) && (kurse < 10))
			return 'svws-ergebnis--low';
		if ((kurse > 9) && (kurse < 12))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Anzahl der anrechenbaren Kurse für Block I des Abiturs zurück.
	 */
	public get summeAnrechenbareKurse() : number {
		return this._summeAnrechenbareKurse.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der anrechenbaren Kurse für Block I des Abiturs genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getSummeAnrechenbareKurseCSS() : string {
		if (this.summeAnrechenbareKurse < 38)
			return 'svws-ergebnis--not-enough';
		if ((this.summeAnrechenbareKurse > 37) && (this.summeAnrechenbareKurse < 40))
			return 'svws-ergebnis--low';
		if ((this.summeAnrechenbareKurse > 39) && (this.summeAnrechenbareKurse < 43))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Wochenstunden für das übergebene Halbjahr zurück, welche von dem Schüler in den einzelnen Halbjahren
	 * der gymnasialen Oberstufe für das Abitur relevant belegt wurden.
	 */
	public getWochenstunden(hj : GostHalbjahr) : number {
		return this._wochenstunden.value[hj.id];
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Anzahl der Wochenstunden für das angegebene Halbjahr genügt.
	 *
	 * @param hj   das Halbjahr der gymnasialen Oberstufe
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenCSS(hj : GostHalbjahr) : string {
		const wst = this.getWochenstunden(hj);
		if (wst < 30)
			return 'svws-ergebnis--not-enough';
		if ((wst >= 30) && (wst < 33))
			return 'svws-ergebnis--low';
		if ((wst >= 33) && (wst < 37))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die Summe der Jahres-Wochenstunden zurück. Das bedeutet die Summe der
	 * Durchschnitte der drei Jahre der gym. Oberstufe.
	 */
	public get wochenstundenJahressumme() : number {
		return this._wochenstundenJahressumme.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die Summe der Jahres-Wochenstunden genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenJahressummeCSS() : string {
		if (this.wochenstundenJahressumme < 100)
			return 'svws-ergebnis--not-enough';
		if ((this.wochenstundenJahressumme >= 100) && (this.wochenstundenJahressumme < 101))
			return 'svws-ergebnis--low';
		if ((this.wochenstundenJahressumme >= 101) && (this.wochenstundenJahressumme <= 106))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob der Halbjahresdurchschnitt der Wochenstunden genügt.
	 *
	 * @param wst   die Wochenstunden
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	private getWochenstundenDurchschnittCSS(wst: number) : string {
		if (wst < 34)
			return 'svws-ergebnis--not-enough';
		if ((wst >= 34) && (wst < 37))
			return 'svws-ergebnis--good';
		return 'svws-ergebnis--more';
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Einführungsphase zurück.
	 */
	public get wochenstundenDurchschnittEF() : number {
		return this._wochenstundenDurchschnittEF.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Einführungsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittEFCSS() : string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittEF);
	}

	/**
	 * Gibt die durchschnittlichen Wochenstunden in den Halbjahren der Qualifikationsphase zurück.
	 */
	public get wochenstundenDurchschnittQ() : number {
		return this._wochenstundenDurchschnittQ.value;
	}

	/**
	 * Gibt die CSS-Klasse zur Einstufung zurück, ob die durchschnittlichen Wochenstunden in den Halbjahren
	 * der Qualifikationsphase genügt.
	 *
	 * @returns die CSS-Klasse zur Einstufung
	 */
	public getWochenstundenDurchschnittQCSS() : string {
		return this.getWochenstundenDurchschnittCSS(this.wochenstundenDurchschnittQ);
	}

}
