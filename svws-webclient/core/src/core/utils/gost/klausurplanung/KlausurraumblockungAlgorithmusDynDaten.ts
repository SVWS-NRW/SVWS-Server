import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { MapUtils } from '../../../../core/utils/MapUtils';
import { System } from '../../../../java/lang/System';
import { Random } from '../../../../java/util/Random';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { ArrayUtils } from '../../../../core/utils/ArrayUtils';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import type { List } from '../../../../java/util/List';
import { GostKlausurraumblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurraumblockungKonfiguration';
import { ListUtils } from '../../../../core/utils/ListUtils';

export class KlausurraumblockungAlgorithmusDynDaten extends JavaObject {

	private readonly _random : Random;

	private readonly _regel_optimiere_blocke_in_moeglichst_wenig_raeume : boolean;

	private readonly _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume : boolean;

	private readonly _regel_forciere_selbe_kursklausur_im_selben_raum : boolean;

	private readonly _regel_forciere_selbe_klausurdauer_pro_raum : boolean;

	private readonly _raumAnzahl : number;

	private readonly _raumAt : Array<GostKlausurraumRich>;

	private readonly _raumZuBelegung : Array<number>;

	private readonly _raumZuKlausurdauer : Array<number>;

	private readonly _klausurGruppenAnzahl : number;

	private readonly _klausurGruppen : List<List<GostSchuelerklausurTerminRich>>;

	private readonly _klausurGruppeZuKlausurdauer : Array<number>;

	private readonly _klausurGruppeZuRaum : Array<GostKlausurraumRich | null>;

	private readonly _klausurGruppeZuRaumSave : Array<GostKlausurraumRich | null>;


	/**
	 * Initialisiert alle Datenstrukturen um diese für schnelle Manipulation zur Verfügung zu stellen.
	 *
	 * @param random  Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param config  Das {@link GostKlausurraumblockungKonfiguration}-Eingabr-Objekt der GUI.
	 */
	constructor(random : Random, config : GostKlausurraumblockungKonfiguration) {
		super();
		this._random = random;
		this._regel_optimiere_blocke_in_moeglichst_wenig_raeume = config._regel_optimiere_blocke_in_moeglichst_wenig_raeume;
		this._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
		this._regel_forciere_selbe_kursklausur_im_selben_raum = config._regel_forciere_selbe_kursklausur_im_selben_raum;
		this._regel_forciere_selbe_klausurdauer_pro_raum = config._regel_forciere_selbe_klausurdauer_pro_raum;
		this._raumAnzahl = config.raeume.size();
		this._raumAt = Array(this._raumAnzahl).fill(null);
		this._raumZuBelegung = Array(this._raumAnzahl).fill(0);
		this._raumZuKlausurdauer = Array(this._raumAnzahl).fill(0);
		for (let i : number = 0; i < this._raumAnzahl; i++)
			this._raumAt[i] = config.raeume.get(i);
		this._klausurGruppen = this._erzeugeKlausurGruppen(config.schuelerklausurtermine);
		this._klausurGruppenAnzahl = this._klausurGruppen.size();
		this._klausurGruppeZuRaum = Array(this._klausurGruppenAnzahl).fill(null);
		this._klausurGruppeZuRaumSave = Array(this._klausurGruppenAnzahl).fill(null);
		this._klausurGruppeZuKlausurdauer = Array(this._klausurGruppenAnzahl).fill(0);
		for (let kg : number = 0; kg < this._klausurGruppenAnzahl; kg++)
			this._klausurGruppeZuKlausurdauer[kg] = this._gibErsteKlausurDerGruppe(kg).dauer;
		this.aktionZustandClear();
	}

	private _gibErsteKlausurDerGruppe(kg : number) : GostSchuelerklausurTerminRich {
		const list : List<GostSchuelerklausurTerminRich> = ListUtils.getNonNullElementAtOrException(this._klausurGruppen, kg);
		return ListUtils.getNonNullElementAtOrException(list, 0);
	}

	private _erzeugeKlausurGruppen(klausuren : List<GostSchuelerklausurTerminRich>) : List<List<GostSchuelerklausurTerminRich>> {
		const gruppen : List<List<GostSchuelerklausurTerminRich>> = new ArrayList<List<GostSchuelerklausurTerminRich>>();
		if (this._regel_forciere_selbe_kursklausur_im_selben_raum) {
			const map : HashMap<number, List<GostSchuelerklausurTerminRich>> = new HashMap<number, List<GostSchuelerklausurTerminRich>>();
			for (const klausur of klausuren)
				MapUtils.addToList(map, klausur.idKursklausur, klausur);
			gruppen.addAll(map.values());
		} else {
			for (const klausur of klausuren)
				gruppen.add(ListUtils.create1(klausur));
		}
		return gruppen;
	}

	private aktionZustandClear() : void {
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			this._raumZuBelegung[r] = 0;
			this._raumZuKlausurdauer[r] = -1;
		}
		for (let k : number = 0; k < this._klausurGruppenAnzahl; k++)
			this._klausurGruppeZuRaum[k] = null;
	}

	private aktionSetzeKlausurgruppeInDenRaum(kg : number, r : number) : boolean {
		const gruppe : List<GostSchuelerklausurTerminRich | null> = this._klausurGruppen.get(kg);
		if (this._raumZuBelegung[r] + gruppe.size() > this._raumAt[r].groesse)
			return false;
		if ((this._regel_forciere_selbe_klausurdauer_pro_raum) && (this._raumZuKlausurdauer[r] >= 0) && (this._klausurGruppeZuKlausurdauer[kg] !== this._raumZuKlausurdauer[r]))
			return false;
		this._raumZuBelegung[r] += gruppe.size();
		if (this._regel_forciere_selbe_klausurdauer_pro_raum)
			this._raumZuKlausurdauer[r] = this._klausurGruppeZuKlausurdauer[kg];
		this._klausurGruppeZuRaum[kg] = this._raumAt[r];
		return true;
	}

	private gibMalus(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		let malus : number = 0.0;
		malus += this.gibMalus_nicht_verteiler_klausuren(klausurGruppeZuRaum);
		malus += this.gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(klausurGruppeZuRaum);
		malus += this.gibMalus_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume(klausurGruppeZuRaum);
		return malus;
	}

	private gibMalus_nicht_verteiler_klausuren(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		let malus : number = 0.0;
		for (let i : number = 0; i < klausurGruppeZuRaum.length; i++)
			if (klausurGruppeZuRaum[i] === null)
				malus += this._klausurGruppen.get(i).size() * 1000.0;
		return malus;
	}

	private gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (!this._regel_optimiere_blocke_in_moeglichst_wenig_raeume)
			return 0.0;
		let malus : number = 0.0;
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum1 : GostKlausurraumRich = this._raumAt[r];
			let counter : number = 0;
			for (let k : number = 0; k < this._klausurGruppenAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = klausurGruppeZuRaum[k];
				if (raum2 === null)
					continue;
				if (raum1.id !== raum2.id)
					continue;
				counter++;
			}
			if (counter > 0)
				malus += 1;
		}
		return malus;
	}

	private gibMalus_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (!this._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume)
			return 0.0;
		let maximum : number = 0;
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum1 : GostKlausurraumRich = this._raumAt[r];
			let counter : number = 0;
			for (let k : number = 0; k < this._klausurGruppenAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = klausurGruppeZuRaum[k];
				if (raum2 === null)
					continue;
				if (raum1.id !== raum2.id)
					continue;
				counter++;
			}
			maximum = Math.max(maximum, counter);
		}
		return 1.0 * maximum;
	}

	/**
	 * Speichert die aktuelle Raum-Klausur-Zuordnung sowie die Güte der Bewertung,
	 * falls er besser ist als der zuvor gespeicherte Zustand.
	 */
	private aktionSpeichernFallsBesser() : void {
		const malusSave : number = this.gibMalus(this._klausurGruppeZuRaumSave);
		const malus : number = this.gibMalus(this._klausurGruppeZuRaum);
		if (malus >= malusSave)
			return;
		System.arraycopy(this._klausurGruppeZuRaum, 0, this._klausurGruppeZuRaumSave, 0, this._klausurGruppenAnzahl);
	}

	/**
	 * Verteilt alle Klausuren zufällig auf die Räume.
	 * Dabei werden die Räume nacheinander aufgefüllt.
	 */
	aktionKlausurenVerteilenAlgorithmus00_zufaellig() : void {
		this.aktionZustandClear();
		const randomR : Array<number> | null = ArrayUtils.getIndexPermutation(this._raumAnzahl, this._random);
		const randomKG : Array<number> | null = ArrayUtils.getIndexPermutation(this._klausurGruppen.size(), this._random);
		for (const kg of randomKG)
			for (const r of randomR)
				if (this.aktionSetzeKlausurgruppeInDenRaum(kg, r))
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
	 * Lädt den gespeicherten Zustand die {@link GostKlausurraumRich#schuelerklausurterminIDs}-Liste.
	 */
	aktionLadeGespeichertenZustand() : void {
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum : GostKlausurraumRich = this._raumAt[r];
			raum.schuelerklausurterminIDs.clear();
			for (let kg : number = 0; kg < this._klausurGruppenAnzahl; kg++) {
				const raum2 : GostKlausurraumRich | null = this._klausurGruppeZuRaumSave[kg];
				if (raum2 === null)
					continue;
				if (raum2.id !== raum.id)
					continue;
				for (const klausur of this._klausurGruppen.get(kg))
					raum.schuelerklausurterminIDs.add(klausur.id);
			}
		}
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
