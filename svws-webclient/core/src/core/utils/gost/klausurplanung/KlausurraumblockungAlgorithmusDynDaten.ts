import { JavaObject } from '../../../../java/lang/JavaObject';
import { Random } from '../../../../java/util/Random';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { PairNN } from '../../../../core/adt/PairNN';
import { ArrayUtils } from '../../../../core/utils/ArrayUtils';
import { ArrayList } from '../../../../java/util/ArrayList';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurraumblockungKonfiguration';
import { System } from '../../../../java/lang/System';

export class KlausurraumblockungAlgorithmusDynDaten extends JavaObject {

	private readonly _random : Random;

	private readonly _regel_aehnliche_klausurdauer_pro_raum : number;

	private readonly _regel_blocke_in_moeglichst_wenig_raeume : number;

	private readonly _regel_selbe_kursklausur_in_selben_raum : number;

	private readonly _raumAnzahl : number;

	private readonly _raumAt : Array<GostKlausurraumRich>;

	private readonly _raumAtBelegt : Array<number>;

	private readonly _klausurAnzahl : number;

	private readonly _klausurAt : Array<GostSchuelerklausurTerminRich>;

	private readonly _klausurZuRaum : Array<GostKlausurraumRich | null>;

	private readonly _klausurZuRaumSave : Array<GostKlausurraumRich | null>;


	/**
	 * Initialisiert alle Datenstrukturen um diese für schnelle Manipulation zur Verfügung zu stellen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param config  Das {@link GostKlausurraumblockungKonfiguration}-Eingabr-Objekt der GUI.
	 */
	constructor(random : Random, config : GostKlausurraumblockungKonfiguration) {
		super();
		this._random = random;
		this._regel_aehnliche_klausurdauer_pro_raum = config._regel_aehnliche_klausurdauer_pro_raum;
		this._regel_blocke_in_moeglichst_wenig_raeume = config._regel_blocke_in_moeglichst_wenig_raeume;
		this._regel_selbe_kursklausur_in_selben_raum = config._regel_selbe_kursklausur_in_selben_raum;
		this._raumAnzahl = config.raeume.size();
		this._raumAt = Array(this._raumAnzahl).fill(null);
		this._raumAtBelegt = Array(this._raumAnzahl).fill(0);
		for (let i : number = 0; i < this._raumAnzahl; i++)
			this._raumAt[i] = config.raeume.get(i);
		this._klausurAnzahl = config.schuelerklausurtermine.size();
		this._klausurAt = Array(this._klausurAnzahl).fill(null);
		this._klausurZuRaum = Array(this._klausurAnzahl).fill(null);
		this._klausurZuRaumSave = Array(this._klausurAnzahl).fill(null);
		for (let i : number = 0; i < this._klausurAnzahl; i++)
			this._klausurAt[i] = config.schuelerklausurtermine.get(i);
		this.aktionZustandClear();
	}

	private aktionZustandClear() : void {
		for (let r : number = 0; r < this._raumAnzahl; r++)
			this._raumAtBelegt[r] = 0;
		for (let k : number = 0; k < this._klausurAnzahl; k++)
			this._klausurZuRaum[k] = null;
	}

	private gibMalus(klausurZuRaum : Array<GostKlausurraumRich | null>) : number {
		let malus : number = 0.0;
		for (let i : number = 0; i < this._klausurAnzahl; i++)
			if (klausurZuRaum[i] === null)
				malus += 1000.0;
		malus += this.gibMalus_regel_aehnliche_klausurdauer_pro_raum(klausurZuRaum);
		malus += this.gibMalus_regel_blocke_in_moeglichst_wenig_raeume(klausurZuRaum);
		malus += this.gibMalus_regel_selbe_kursklausur_in_selben_raum(klausurZuRaum);
		return malus;
	}

	private gibMalus_regel_aehnliche_klausurdauer_pro_raum(klausurZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (this._regel_aehnliche_klausurdauer_pro_raum <= 0.0)
			return 0;
		let malus : number = 0.0;
		for (let k1 : number = 0; k1 < this._klausurAnzahl; k1++)
			for (let k2 : number = k1 + 1; k2 < this._klausurAnzahl; k2++)
				if ((this._klausurAt[k1].dauer !== this._klausurAt[k2].dauer) && KlausurraumblockungAlgorithmusDynDaten.gibKlausurImSelbenRaum(klausurZuRaum, k1, k2))
					malus += this._regel_aehnliche_klausurdauer_pro_raum;
		return malus;
	}

	private gibMalus_regel_blocke_in_moeglichst_wenig_raeume(klausurZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (this._regel_blocke_in_moeglichst_wenig_raeume <= 0.0)
			return 0.0;
		let malus : number = 0.0;
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum1 : GostKlausurraumRich = this._raumAt[r];
			let counter : number = 0;
			for (let k : number = 0; k < this._klausurAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = klausurZuRaum[k];
				if (raum2 === null)
					continue;
				if (raum1.id !== raum2.id)
					continue;
				counter++;
			}
			if (counter > 0)
				malus += this._regel_blocke_in_moeglichst_wenig_raeume;
		}
		return malus;
	}

	private gibMalus_regel_selbe_kursklausur_in_selben_raum(klausurZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (this._regel_selbe_kursklausur_in_selben_raum <= 0.0)
			return 0.0;
		let malus : number = 0.0;
		for (let k1 : number = 0; k1 < this._klausurAnzahl; k1++)
			for (let k2 : number = k1 + 1; k2 < this._klausurAnzahl; k2++)
				if ((this._klausurAt[k1].idKursklausur === this._klausurAt[k2].idKursklausur) && !KlausurraumblockungAlgorithmusDynDaten.gibKlausurImSelbenRaum(klausurZuRaum, k1, k2))
					malus += this._regel_selbe_kursklausur_in_selben_raum;
		return malus;
	}

	private static gibKlausurImSelbenRaum(klausurZuRaum : Array<GostKlausurraumRich | null>, k1 : number, k2 : number) : boolean {
		const raum1 : GostKlausurraumRich | null = klausurZuRaum[k1];
		const raum2 : GostKlausurraumRich | null = klausurZuRaum[k2];
		if (raum1 === null)
			return false;
		if (raum2 === null)
			return false;
		return raum1.id === raum2.id;
	}

	private aktionSetzeKlausurInDenRaum(k : number, r : number) : boolean {
		if (this._raumAtBelegt[r] >= this._raumAt[r].groesse)
			return false;
		if (this._klausurZuRaum[k] !== null)
			return false;
		this._klausurZuRaum[k] = this._raumAt[r];
		this._raumAtBelegt[r]++;
		return true;
	}

	/**
	 * Speichert die aktuelle Raum-Klausur-Zuordnung sowie die Güte der Bewertung,
	 * falls er besser ist als der zuvor gespeicherte Zustand.
	 */
	private aktionSpeichernFallsBesser() : void {
		const malusSave : number = this.gibMalus(this._klausurZuRaumSave);
		const malus : number = this.gibMalus(this._klausurZuRaum);
		if (malus >= malusSave)
			return;
		System.arraycopy(this._klausurZuRaum, 0, this._klausurZuRaumSave, 0, this._klausurAnzahl);
	}

	/**
	 * Verteilt alle Klausuren zufällig auf die Räume.
	 * Dabei werden die Räume nacheinander aufgefüllt.
	 */
	aktionKlausurenVerteilenAlgorithmus00_zufaellig() : void {
		this.aktionZustandClear();
		const randomR : Array<number> | null = ArrayUtils.getIndexPermutation(this._raumAnzahl, this._random);
		const randomK : Array<number> | null = ArrayUtils.getIndexPermutation(this._klausurAnzahl, this._random);
		for (const k of randomK)
			for (const r of randomR)
				if (this.aktionSetzeKlausurInDenRaum(k, r))
					break;
		this.aktionSpeichernFallsBesser();
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 01.
	 */
	aktionKlausurenVerteilenAlgorithmus01() : void {
		// empty block
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 02.
	 */
	aktionKlausurenVerteilenAlgorithmus02() : void {
		// empty block
	}

	/**
	 * Verteilt die Klausuren mit Algorithmus 03.
	 */
	aktionKlausurenVerteilenAlgorithmus03() : void {
		// empty block
	}

	/**
	 * Liefert den gespeicherten Zustand als {@link GostKlausurraumRich}-Ausgabe-Objekt.
	 *
	 * @return den gespeicherten Zustand als {@link GostKlausurraumRich}-Ausgabe-Objekt.
	 */
	gibGespeichertenZustand() : List<PairNN<GostKlausurraumRich, List<GostSchuelerklausurTerminRich>>> {
		const paare : List<PairNN<GostKlausurraumRich, List<GostSchuelerklausurTerminRich>>> = new ArrayList<PairNN<GostKlausurraumRich, List<GostSchuelerklausurTerminRich>>>();
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum : GostKlausurraumRich = this._raumAt[r];
			const klausurenDesRaumes : List<GostSchuelerklausurTerminRich> = new ArrayList<GostSchuelerklausurTerminRich>();
			for (let k : number = 0; k < this._klausurAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = this._klausurZuRaumSave[k];
				if (raum2 === null)
					continue;
				if (raum2.id === raum.id)
					klausurenDesRaumes.add(this._klausurAt[k]);
			}
			paare.add(new PairNN<GostKlausurraumRich, List<GostSchuelerklausurTerminRich>>(raum, klausurenDesRaumes));
		}
		return paare;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmusDynDaten';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.gost.klausurplanung.KlausurraumblockungAlgorithmusDynDaten'].includes(name);
	}

}

export function cast_de_svws_nrw_core_utils_gost_klausurplanung_KlausurraumblockungAlgorithmusDynDaten(obj : unknown) : KlausurraumblockungAlgorithmusDynDaten {
	return obj as KlausurraumblockungAlgorithmusDynDaten;
}
