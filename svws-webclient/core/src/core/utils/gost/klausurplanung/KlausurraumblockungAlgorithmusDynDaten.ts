import { JavaObject } from '../../../../java/lang/JavaObject';
import { HashMap } from '../../../../java/util/HashMap';
import { ArrayList } from '../../../../java/util/ArrayList';
import { MapUtils } from '../../../../core/utils/MapUtils';
import { JavaMath } from '../../../../java/lang/JavaMath';
import { System } from '../../../../java/lang/System';
import type { Comparator } from '../../../../java/util/Comparator';
import { GostSchuelerklausurTerminRich } from '../../../../core/data/gost/klausurplanung/GostSchuelerklausurTerminRich';
import { Random } from '../../../../java/util/Random';
import { ArrayUtils } from '../../../../core/utils/ArrayUtils';
import { GostKlausurraumRich } from '../../../../core/data/gost/klausurplanung/GostKlausurraumRich';
import type { List } from '../../../../java/util/List';
import { ListUtils } from '../../../../core/utils/ListUtils';
import { GostKlausurraumblockungKonfiguration } from '../../../../core/data/gost/klausurplanung/GostKlausurraumblockungKonfiguration';

export class KlausurraumblockungAlgorithmusDynDaten extends JavaObject {

	private static readonly MALUS_NICHT_VERTEILT : number = 1000000.0;

	private static readonly MALUS_MOEGLICHST_WENIG_RAEUME : number = 1000.0;

	private static readonly MALUS_MOEGLICHST_GLEICHVERTEILT_AUF_RAEUME : number = 1.0;

	private static readonly _compRaeume : Comparator<GostKlausurraumRich> = { compare : (o1: GostKlausurraumRich, o2: GostKlausurraumRich) => {
		if (o1.groesse < o2.groesse)
			return -1;
		if (o1.groesse > o2.groesse)
			return +1;
		if (o1.id < o2.id)
			return -1;
		if (o1.id > o2.id)
			return +1;
		return 0;
	} };

	private static readonly _compKlausurGruppen : Comparator<List<GostSchuelerklausurTerminRich>> = { compare : (o1: List<GostSchuelerklausurTerminRich>, o2: List<GostSchuelerklausurTerminRich>) => {
		if (o1.size() < o2.size())
			return -1;
		if (o1.size() > o2.size())
			return +1;
		const k1 : GostSchuelerklausurTerminRich = ListUtils.getNonNullElementAtOrException(o1, 0);
		const k2 : GostSchuelerklausurTerminRich = ListUtils.getNonNullElementAtOrException(o2, 0);
		if (k1.id < k2.id)
			return -1;
		if (k1.id > k2.id)
			return +1;
		return 0;
	} };

	private readonly _random : Random;

	private readonly _config : GostKlausurraumblockungKonfiguration;

	private readonly _regel_optimiere_blocke_in_moeglichst_wenig_raeume : boolean;

	private readonly _regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume : boolean;

	private readonly _regel_forciere_selbe_kursklausur_im_selben_raum : boolean;

	private readonly _regel_forciere_selbe_klausurdauer_pro_raum : boolean;

	private readonly _regel_forciere_selben_klausurstart_pro_raum : boolean;

	private readonly _raumAnzahl : number;

	private readonly _raumAt : Array<GostKlausurraumRich>;

	private readonly _raumSortiertAufsteigend : Array<number>;

	private readonly _raumSortiertAbsteigend : Array<number>;

	private readonly _raumZuBelegung : Array<number>;

	private readonly _raumZuKlausurdauer : Array<number>;

	private readonly _raumZuKlausurstart : Array<number>;

	private readonly _klausurGruppenAnzahl : number;

	private readonly _klausurGruppen : List<List<GostSchuelerklausurTerminRich>>;

	private readonly _klausurGruppenAufsteigend : Array<number>;

	private readonly _klausurGruppenAbsteigend : Array<number>;

	private readonly _klausurGruppeZuKlausurdauer : Array<number>;

	private readonly _klausurGruppeZuKlausurstart : Array<number>;

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
		this._config = config;
		this._regel_optimiere_blocke_in_moeglichst_wenig_raeume = config._regel_optimiere_blocke_in_moeglichst_wenig_raeume;
		this._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume = config._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume;
		this._regel_forciere_selbe_kursklausur_im_selben_raum = config._regel_forciere_selbe_kursklausur_im_selben_raum;
		this._regel_forciere_selbe_klausurdauer_pro_raum = config._regel_forciere_selbe_klausurdauer_pro_raum;
		this._regel_forciere_selben_klausurstart_pro_raum = config._regel_forciere_selben_klausurstart_pro_raum;
		this._raumAnzahl = config.raeume.size();
		this._raumAt = KlausurraumblockungAlgorithmusDynDaten._erzeugeRaeumeSortiert(config.raeume);
		this._raumZuBelegung = Array(this._raumAnzahl).fill(0);
		this._raumZuKlausurdauer = Array(this._raumAnzahl).fill(0);
		this._raumZuKlausurstart = Array(this._raumAnzahl).fill(0);
		this._raumSortiertAufsteigend = Array(this._raumAnzahl).fill(0);
		this._raumSortiertAbsteigend = Array(this._raumAnzahl).fill(0);
		for (let i : number = 0; i < this._raumAnzahl; i++) {
			this._raumSortiertAufsteigend[i] = i;
			this._raumSortiertAbsteigend[i] = this._raumAnzahl - 1 - i;
		}
		this._klausurGruppen = this._erzeugeKlausurGruppenSortiert(config.schuelerklausurtermine);
		this._klausurGruppenAnzahl = this._klausurGruppen.size();
		this._klausurGruppeZuRaum = Array(this._klausurGruppenAnzahl).fill(null);
		this._klausurGruppeZuRaumSave = Array(this._klausurGruppenAnzahl).fill(null);
		this._klausurGruppeZuKlausurdauer = Array(this._klausurGruppenAnzahl).fill(0);
		this._klausurGruppeZuKlausurstart = Array(this._klausurGruppenAnzahl).fill(0);
		this._klausurGruppenAufsteigend = Array(this._klausurGruppenAnzahl).fill(0);
		this._klausurGruppenAbsteigend = Array(this._klausurGruppenAnzahl).fill(0);
		for (let kg : number = 0; kg < this._klausurGruppenAnzahl; kg++) {
			this._klausurGruppeZuKlausurdauer[kg] = this._gibErsteKlausurDerGruppe(kg).dauer;
			this._klausurGruppeZuKlausurstart[kg] = this._gibErsteKlausurDerGruppe(kg).startzeit;
			this._klausurGruppenAufsteigend[kg] = kg;
			this._klausurGruppenAbsteigend[kg] = this._klausurGruppenAnzahl - 1 - kg;
		}
		this.aktionZustandClear();
	}

	private static _erzeugeRaeumeSortiert(raeume : List<GostKlausurraumRich>) : Array<GostKlausurraumRich> {
		const list : List<GostKlausurraumRich> = new ArrayList<GostKlausurraumRich>(raeume);
		list.sort(KlausurraumblockungAlgorithmusDynDaten._compRaeume);
		const copy : Array<GostKlausurraumRich> = Array(list.size()).fill(null);
		for (let i : number = 0; i < copy.length; i++)
			copy[i] = list.get(i);
		return copy;
	}

	private _gibErsteKlausurDerGruppe(kg : number) : GostSchuelerklausurTerminRich {
		const list : List<GostSchuelerklausurTerminRich> = ListUtils.getNonNullElementAtOrException(this._klausurGruppen, kg);
		return ListUtils.getNonNullElementAtOrException(list, 0);
	}

	private _erzeugeKlausurGruppenSortiert(klausuren : List<GostSchuelerklausurTerminRich>) : List<List<GostSchuelerklausurTerminRich>> {
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
		gruppen.sort(KlausurraumblockungAlgorithmusDynDaten._compKlausurGruppen);
		return gruppen;
	}

	private aktionZustandClear() : void {
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			this._raumZuBelegung[r] = 0;
			this._raumZuKlausurdauer[r] = -1;
			this._raumZuKlausurstart[r] = -1;
		}
		for (let k : number = 0; k < this._klausurGruppenAnzahl; k++)
			this._klausurGruppeZuRaum[k] = null;
	}

	private aktionSetzeKlausurgruppeInDenRaum(kg : number, r : number) : boolean {
		const gruppe : List<GostSchuelerklausurTerminRich> = this._klausurGruppen.get(kg);
		if ((this._raumZuBelegung[r] + gruppe.size()) > this._raumAt[r].groesse)
			return false;
		if ((this._regel_forciere_selben_klausurstart_pro_raum) && (this._raumZuKlausurstart[r] >= 0) && (this._klausurGruppeZuKlausurstart[kg] !== this._raumZuKlausurstart[r]))
			return false;
		if ((this._regel_forciere_selbe_klausurdauer_pro_raum) && (this._raumZuKlausurdauer[r] >= 0) && (this._klausurGruppeZuKlausurdauer[kg] !== this._raumZuKlausurdauer[r]))
			return false;
		this._raumZuBelegung[r] += gruppe.size();
		if (this._regel_forciere_selben_klausurstart_pro_raum)
			this._raumZuKlausurstart[r] = this._klausurGruppeZuKlausurstart[kg];
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
				malus += this._klausurGruppen.get(i).size() * KlausurraumblockungAlgorithmusDynDaten.MALUS_NICHT_VERTEILT;
		return malus;
	}

	private gibMalus_regel_optimiere_blocke_in_moeglichst_wenig_raeume(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (!this._regel_optimiere_blocke_in_moeglichst_wenig_raeume)
			return 0.0;
		let malus : number = 0.0;
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum1 : GostKlausurraumRich = this._raumAt[r];
			let counterGruppen : number = 0;
			for (let k : number = 0; k < this._klausurGruppenAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = klausurGruppeZuRaum[k];
				if (raum2 === null)
					continue;
				if (raum1.id !== raum2.id)
					continue;
				counterGruppen++;
			}
			if (counterGruppen > 0)
				malus += KlausurraumblockungAlgorithmusDynDaten.MALUS_MOEGLICHST_WENIG_RAEUME;
		}
		return malus;
	}

	private gibMalus_regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume(klausurGruppeZuRaum : Array<GostKlausurraumRich | null>) : number {
		if (!this._regel_optimiere_blocke_gleichmaessig_verteilt_auf_raeume)
			return 0.0;
		let maximum : number = 0;
		for (let r : number = 0; r < this._raumAnzahl; r++) {
			const raum1 : GostKlausurraumRich = this._raumAt[r];
			let counterKlausuren : number = 0;
			for (let k : number = 0; k < this._klausurGruppenAnzahl; k++) {
				const raum2 : GostKlausurraumRich | null = klausurGruppeZuRaum[k];
				if (raum2 === null)
					continue;
				if (raum1.id !== raum2.id)
					continue;
				counterKlausuren += this._klausurGruppen.get(k).size();
			}
			maximum = Math.max(maximum, counterKlausuren);
		}
		return maximum * KlausurraumblockungAlgorithmusDynDaten.MALUS_MOEGLICHST_GLEICHVERTEILT_AUF_RAEUME;
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

	private aktionKlausurenVerteilenAlgorithmusGeneric(aRaum : Array<number> | null, aKlausurGruppe : Array<number> | null) : void {
		this.aktionZustandClear();
		for (const kg of ((aKlausurGruppe === null) ? ArrayUtils.getIndexPermutation(this._klausurGruppenAnzahl, this._random) : aKlausurGruppe))
			for (const r of ((aRaum === null) ? ArrayUtils.getIndexPermutation(this._raumAnzahl, this._random) : aRaum))
				if (this.aktionSetzeKlausurgruppeInDenRaum(kg, r))
					break;
		this.aktionSpeichernFallsBesser();
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus01_raum_zufaellig_gruppe_zufaellig() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(null, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus02_raum_aufsteigend_gruppe_zufaellig() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAufsteigend, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in zufälliger Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus03_raum_absteigend_gruppe_zufaellig() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAbsteigend, null);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus04_raum_zufaellig_gruppe_aufsteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(null, this._klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus05_raum_aufsteigend_gruppe_aufsteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAufsteigend, this._klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in aufsteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus06_raum_absteigend_gruppe_aufsteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAbsteigend, this._klausurGruppenAufsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in zufälliger Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus07_raum_zufaellig_gruppe_absteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(null, this._klausurGruppenAbsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in aufsteigender Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus08_raum_aufsteigend_gruppe_absteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAufsteigend, this._klausurGruppenAbsteigend);
	}

	/**
	 * Der Algorithmus verteilt Klausurgruppen auf Räume:
	 * <br>Räume in absteigender Reihenfolge.
	 * <br>Klausurgruppen in absteigender Reihenfolge.
	 */
	aktionKlausurenVerteilenAlgorithmus09_raum_absteigend_gruppe_absteigend() : void {
		this.aktionKlausurenVerteilenAlgorithmusGeneric(this._raumSortiertAbsteigend, this._klausurGruppenAbsteigend);
	}

	/**
	 * Lädt den gespeicherten Zustand die {@link GostKlausurraumRich#schuelerklausurterminIDs}-Liste.
	 * Überschreibt {@link GostKlausurraumblockungKonfiguration#schuelerklausurtermine} mit den Klausuren, die nicht verteilt wurden.
	 */
	aktionLadeGespeichertenZustandInDieConfig() : void {
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
		this._config.schuelerklausurtermine.clear();
		for (let kg : number = 0; kg < this._klausurGruppenAnzahl; kg++)
			if (this._klausurGruppeZuRaumSave[kg] === null)
				this._config.schuelerklausurtermine.addAll(this._klausurGruppen.get(kg));
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
