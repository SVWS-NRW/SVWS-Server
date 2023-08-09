import { computed, shallowRef } from "vue";

import type { GostKlausurtermin, GostJahrgangsdaten, GostKursklausur, LehrerListeEintrag, SchuelerListeEintrag, GostKlausurvorgabe, GostKlausurraum, Schuljahresabschnitt, List, GostSchuelerklausur, GostKlausurenCollectionSkrsKrs} from "@core";
import { GostKlausurraumManager, StundenplanManager, KursManager, GostFaecherManager, GostHalbjahr, GostKursklausurManager, GostKlausurvorgabenManager, ListUtils, Arrays, StundenplanListeEintrag, StundenplanListUtils } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";

import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungVorgaben";
import { routeGostKlausurplanungKonflikte } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKonflikte";
import { routeGostKlausurplanungRaumzeit } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungRaumzeit";
import { routeGostKlausurplanungSchienen } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungSchienen";

interface RouteStateGostKlausurplanung {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	faecherManager: GostFaecherManager;
	// ... die mit dem Abiturjahrgang aktualisiert/mitgeladen werden
	mapLehrer: Map<number, LehrerListeEintrag>;
	// ... auch abhängig vom ausgewählten Halbjahr der gymnasialen Oberstufe
	halbjahr: GostHalbjahr;
	kursklausurmanager: GostKursklausurManager | undefined;
	klausurvorgabenmanager: GostKlausurvorgabenManager | undefined;
	stundenplanmanager: StundenplanManager | undefined;
	kursmanager: KursManager;
	quartalsauswahl: 0 | 1 | 2 ;
	view: RouteNode<any, any>;
}

export class RouteDataGostKlausurplanung {

	private static _defaultState : RouteStateGostKlausurplanung = {
		abiturjahr: undefined,
		jahrgangsdaten: undefined,
		mapSchueler: new Map(),
		faecherManager: new GostFaecherManager(),
		mapLehrer: new Map(),
		halbjahr: GostHalbjahr.EF1,
		kursklausurmanager: undefined,
		klausurvorgabenmanager: undefined,
		stundenplanmanager: undefined,
		kursmanager: new KursManager(),
		quartalsauswahl: 0,
		view: routeGostKlausurplanungVorgaben,
	}

	private _state = shallowRef<RouteStateGostKlausurplanung>(RouteDataGostKlausurplanung._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateGostKlausurplanung>) {
		this._state.value = Object.assign({ ... RouteDataGostKlausurplanung._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateGostKlausurplanung>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}


	public get hatAbiturjahr(): boolean {
		return this._state.value.abiturjahr !== undefined;
	}

	public get abiturjahrIstVorlage() : boolean {
		return (this._state.value.abiturjahr !== undefined) && (this._state.value.abiturjahr === -1);
	}

	public get abiturjahr() : number {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es wurde noch kein Abiturjahrgang geladen.");
		return this._state.value.abiturjahr;
	}

	public async setAbiturjahr(abiturjahr: number | undefined) {
		if (abiturjahr === this._state.value.abiturjahr)
			return;
		if (abiturjahr === undefined) {
			this._state.value = RouteDataGostKlausurplanung._defaultState;
			return;
		}
		try {
			api.status.start();
			// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
			const jahrgangsdaten = await api.server.getGostAbiturjahrgang(api.schema, abiturjahr)
			const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, abiturjahr);
			const faecherManager = new GostFaecherManager(listFaecher);
			const mapSchueler = new Map<number, SchuelerListeEintrag>();
			const mapLehrer: Map<number, LehrerListeEintrag> = new Map();
			let view: RouteNode<any, any> = this._state.value.view;
			// TODO schieben in getHalbjahr und durch getKurseFuerAbschnitt ersetzen
			const listKurse = await api.server.getKurse(api.schema);
			const kursManager = new KursManager(listKurse);

			if (abiturjahr !== -1) {
				const listSchueler = await api.server.getGostAbiturjahrgangSchueler(api.schema, abiturjahr);
				// Lade die Schülerliste des Abiturjahrgangs
				for (const s of listSchueler)
					mapSchueler.set(s.id, s);
				// Lade die Lehrerliste
				const listLehrer = await api.server.getLehrer(api.schema);
				for (const l of listLehrer)
					mapLehrer.set(l.id, l);
			} else {
				if ((view !== routeGostKlausurplanungKalender) && (view !== routeGostKlausurplanungVorgaben))
					view = routeGostKlausurplanungVorgaben;
			}
			// Setze den State neu
			this.setPatchedDefaultState({
				abiturjahr: abiturjahr,
				jahrgangsdaten: jahrgangsdaten,
				mapSchueler: mapSchueler,
				faecherManager: faecherManager,
				mapLehrer: mapLehrer,
				halbjahr: this._state.value.halbjahr,
				kursmanager: kursManager,
				view: view,
			});
		} finally {
			api.status.stop();
		}
	}

	public get hatJahrgangsdaten(): boolean {
		return this._state.value.jahrgangsdaten !== undefined;
	}

	public get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._state.value.jahrgangsdaten === undefined)
			throw new Error("Es wurde noch kein Abiturjahrgang geladen, so dass keine Jahrgangsdaten zur Verfügung stehen.");
		return this._state.value.jahrgangsdaten;
	}

	public get mapSchueler(): Map<number, SchuelerListeEintrag> {
		return this._state.value.mapSchueler;
	}

	public get faecherManager() : GostFaecherManager {
		return this._state.value.faecherManager;
	}

	public get kursManager() : KursManager {
		return this._state.value.kursmanager;
	}

	public get mapLehrer(): Map<number, LehrerListeEintrag> {
		return this._state.value.mapLehrer;
	}


	public get halbjahr() : GostHalbjahr {
		return this._state.value.halbjahr;
	}

	public async setHalbjahr(halbjahr: GostHalbjahr): Promise<boolean> {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (halbjahr === this._state.value.halbjahr)
			return false;
		try {
			api.status.start();
			const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
			const klausurvorgabenmanager = new GostKlausurvorgabenManager(listKlausurvorgaben, this.faecherManager);
			if (this._state.value.abiturjahr === -1) {
				this.setPatchedState({
					halbjahr: halbjahr,
					kursklausurmanager: undefined,
					stundenplanmanager: undefined,
					klausurvorgabenmanager: klausurvorgabenmanager,
				});
				return true;
			}
			const schuljahr = halbjahr.getSchuljahrFromAbiturjahr(this._state.value.abiturjahr);
			const abschnitt : Schuljahresabschnitt | undefined = api.getAbschnittBySchuljahrUndHalbjahr(schuljahr, halbjahr.halbjahr);
			if (abschnitt === undefined) {
				this.setPatchedState({
					halbjahr: halbjahr,
					kursklausurmanager: undefined,
					stundenplanmanager: undefined,
					klausurvorgabenmanager: klausurvorgabenmanager,
				});
				return true;
			}
			//				throw new Error("Schuljahresabschnitt konnte nicht ermittelt werden.");
			const kursklausurmanager = await this.reloadKursklausurmanager(halbjahr);
			const listStundenplaene = await api.server.getStundenplanlisteFuerAbschnitt(api.schema, abschnitt.id);
			if (listStundenplaene.isEmpty()) {
				this.setPatchedState({
					halbjahr: halbjahr,
					kursklausurmanager,
					stundenplanmanager: undefined,
					klausurvorgabenmanager: klausurvorgabenmanager,
				});
				return true;
			}
			const stundenplan = StundenplanListUtils.get(listStundenplaene, new Date().toISOString().substring(0, 10));
			if (stundenplan === null)
				throw new Error("Es konnte kein aktiver Stundenplan gefunden werden.");
			const stundenplandaten = await api.server.getStundenplan(api.schema, stundenplan.id);
			const unterrichte = await api.server.getStundenplanUnterrichte(api.schema, stundenplan.id);
			const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, stundenplan.id);
			const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, stundenplan.id);
			//const listKlausurtermine = await api.server.getGostKlausurenKlausurtermineJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
			//const listKursklausuren = await api.server.getGostKlausurenKursklausurenJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
			//const kursklausurmanager = new GostKursklausurManager(listKursklausuren, listKlausurtermine);
			const stundenplanmanager = new StundenplanManager(stundenplandaten, unterrichte, pausenaufsichten, unterrichtsverteilung);
			this.setPatchedState({
				halbjahr: halbjahr,
				kursklausurmanager,
				stundenplanmanager,
				klausurvorgabenmanager,
			});
			return true;
		} finally {
			api.status.stop();
		}
	}

	public async reloadKursklausurmanager(halbjahr: GostHalbjahr | null) : Promise<GostKursklausurManager> {
		//try {
		const listKlausurtermine = await api.server.getGostKlausurenKlausurtermineJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr !== null ? halbjahr.id : this._state.value.halbjahr.id);
		const listKursklausuren = await api.server.getGostKlausurenKursklausurenJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr !== null ? halbjahr.id : this._state.value.halbjahr.id);
		return new GostKursklausurManager(listKursklausuren, listKlausurtermine);
		/*this.setPatchedState({
			kursklausurmanager,
		});*/
	}

	public get hatStundenplanManager(): boolean {
		return this._state.value.stundenplanmanager !== undefined;
	}

	public get stundenplanmanager(): StundenplanManager {
		if (this._state.value.stundenplanmanager === undefined)
			throw new Error("Es wurde noch keine Daten geladen, so dass kein Stundenplan-Manager zur Verfügung steht.");
		return this._state.value.stundenplanmanager;
	}


	public get hatKursklausurManager(): boolean {
		return this._state.value.kursklausurmanager !== undefined;
	}

	public get kursklausurmanager(): GostKursklausurManager {
		if (this._state.value.kursklausurmanager === undefined)
			throw new Error("Es wurde noch keine Daten geladen, so dass kein Kurs-Klausur-Manager zur Verfügung steht.");
		return this._state.value.kursklausurmanager;
	}

	public get hatKlausurvorgabenManager(): boolean {
		return this._state.value.klausurvorgabenmanager !== undefined;
	}

	public get klausurvorgabenmanager(): GostKlausurvorgabenManager {
		if (this._state.value.klausurvorgabenmanager === undefined)
			throw new Error("Es wurde noch keine Daten geladen, so dass kein Klausur-Vorgaben-Manager zur Verfügung steht.");
		return this._state.value.klausurvorgabenmanager;
	}

	quartalsauswahl = computed({
		get: () : 0 | 1 | 2 => this._state.value.quartalsauswahl,
		set: (value: 0 | 1 | 2): void => {
			this._state.value.quartalsauswahl = value;
			this.commit();
		}
	  });

	public async setView(view: RouteNode<any,any>) {
		if ((view !== routeGostKlausurplanungVorgaben) &&
			(view !== routeGostKlausurplanungSchienen) &&
			(view !== routeGostKlausurplanungKalender) &&
			(view !== routeGostKlausurplanungRaumzeit) &&
			(view !== routeGostKlausurplanungKonflikte))
			throw new Error("Die gewählte Ansicht für die Klausurplanung wird nicht unterstützt. ");
		this.setPatchedState({ view: view });
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}


	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(this.view.getRoute(this.abiturjahr, value.id));
	}

	erzeugeKlausurtermin = async (quartal: number): Promise<GostKlausurtermin> => {
		api.status.start();
		const termin = await api.server.createGostKlausurenKlausurtermin(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
		this.kursklausurmanager.addKlausurtermin(termin);
		this.commit();
		api.status.stop();
		return termin;
	}

	loescheKlausurtermine = async (termine: List<GostKlausurtermin>): Promise<boolean> => {
		api.status.start();
		const terminIds = Arrays.asList((termine.toArray() as GostKlausurtermin[]).map((termin) => termin.id));
		const result = await api.server.deleteGostKlausurenKlausurtermine(terminIds, api.schema);
		this.kursklausurmanager.removeKlausurtermine(terminIds);
		this.commit();
		api.status.stop();
		return true;
	}

	setTerminToKursklausur = async (idTermin: number | null, klausur: GostKursklausur): Promise<boolean> => {
		api.status.start();
		klausur.idTermin = idTermin;
		await api.server.patchGostKlausurenKursklausur({idTermin: idTermin}, api.schema, klausur.id);
		this.kursklausurmanager.updateKursklausur(klausur);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeKlausurvorgabe = async (vorgabe: GostKlausurvorgabe): Promise<GostKlausurvorgabe> => {
		api.status.start();
		vorgabe.abiJahrgang = this.abiturjahr;
		vorgabe.halbjahr = this.halbjahr.id;
		const neueVorgabe = await api.server.createGostKlausurenVorgabe(vorgabe, api.schema);
		this.klausurvorgabenmanager.addKlausurvorgabe(neueVorgabe);
		this.commit();
		api.status.stop();
		return neueVorgabe;
	}

	patchKlausurvorgabe = async (vorgabe: Partial<GostKlausurvorgabe>, id: number): Promise<boolean> => {
		api.status.start();
		await api.server.patchGostKlausurenVorgabe(vorgabe, api.schema, id);
		const oldVorgabe = this.klausurvorgabenmanager.gibGostKlausurvorgabe(id);
		Object.assign(oldVorgabe, vorgabe);
		this.klausurvorgabenmanager.updateKlausurvorgabe(oldVorgabe);
		this.commit();
		api.status.stop();
		return true;
	}

	loescheKlausurvorgabe = async (vorgabe: GostKlausurvorgabe): Promise<boolean> => {
		api.status.start();
		const result = await api.server.deleteGostKlausurenVorgabe(api.schema, vorgabe.idVorgabe);
		// TODO Falls Constraint verletzt, nicht löschen
		this.klausurvorgabenmanager.removeVorgabe(vorgabe.idVorgabe);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeKursklausurenAusVorgaben = async (quartal: number): Promise<boolean> => {
		api.status.start();
		const result = await api.server.createGostKlausurenKursklausurenJahrgangHalbjahrQuartal(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
		this.kursklausurmanager.addKlausuren(result);
		this.commit();
		api.status.stop();
		return true;
	}

	patchKlausurterminDatum = async (id: number, termin: Partial<GostKlausurtermin>): Promise<boolean> => {
		api.status.start();
		const oldTtermin: GostKlausurtermin = this.kursklausurmanager.gibKlausurtermin(id);
		if (termin.datum !== undefined && oldTtermin.datum !== termin.datum)
			this.kursklausurmanager.patchKlausurterminDatum(id, termin.datum);
		Object.assign(oldTtermin, termin);
		await api.server.patchGostKlausurenKlausurtermin(termin, api.schema, id);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeVorgabenAusVorlage = async (quartal: number): Promise<boolean> => {
		api.status.start();
		await api.server.copyGostKlausurenVorgaben(api.schema, this.abiturjahr, this.halbjahr.id, quartal);
		const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgangHalbjahr(api.schema, this.abiturjahr, this.halbjahr.id);
		this._state.value.klausurvorgabenmanager = new GostKlausurvorgabenManager(listKlausurvorgaben, this.faecherManager);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeKlausurraum = async (raum: GostKlausurraum): Promise<GostKlausurraum> => {
		api.status.start();
		const neuerRaum = await api.server.createGostKlausurenRaum(raum, api.schema);
		this.commit();
		api.status.stop();
		return neuerRaum;
	}

	patchKlausurraum = async (id: number, raum: Partial<GostKlausurraum>, manager: GostKlausurraumManager): Promise<boolean> => {
		api.status.start();
		const oldRaum: GostKlausurraum = manager.getKlausurraum(id);
		Object.assign(oldRaum, raum);
		manager.patchKlausurraum(oldRaum);
		Object.assign(oldRaum, raum);
		await api.server.patchGostKlausurenRaum(raum, api.schema, id);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeKlausurraummanager = async (termin: GostKlausurtermin): Promise<GostKlausurraumManager> => {
		api.status.start();
		const raeume = await api.server.getGostKlausurenRaeumeTermin(api.schema, termin.id);
		const krsCollection = await api.server.getGostKlausurenSchuelerraumstundenTermin(api.schema, termin.id);
		const schuelerklausuren = await api.server.getGostKlausurenSchuelerklausuren(api.schema, termin.id);
		this.commit();
		api.status.stop();
		return new GostKlausurraumManager(raeume, krsCollection.raumstunden, krsCollection.skRaumstunden, schuelerklausuren);
	}

	setzeRaumZuSchuelerklausuren = async (raum: GostKlausurraum, sks: List<GostSchuelerklausur>, manager: GostKlausurraumManager): Promise<GostKlausurenCollectionSkrsKrs> => {
		api.status.start();
		const skids = Arrays.asList((sks.toArray() as GostSchuelerklausur[]).map(sk => sk.idSchuelerklausur));
		const collectionSkrsKrs = await api.server.setzeGostSchuelerklausurenZuRaum(skids, api.schema, raum.id);
		manager.setzeRaumZuSchuelerklausuren(skids, collectionSkrsKrs);
		this.commit();
		api.status.stop();
		return collectionSkrsKrs;
	}

	/*persistKlausurblockung = async (blockung: List<List<number>>): Promise<boolean> => {
		api.status.start();
		await api.server.blockGostKlausurenKursklausuren(blockung, api.schema);
		await this.reloadKursklausurmanager(null);
		this.commit();
		api.status.stop();
		return true;
	}*/

}