import type { GostKlausurtermin, GostJahrgangsdaten, GostKursklausur, LehrerListeEintrag, SchuelerListeEintrag, GostKlausurvorgabe} from "@core";
import { StundenplanManager} from "@core";
import { KursManager } from "@core";
import { GostFaecherManager, GostHalbjahr, GostKursklausurManager, GostKlausurvorgabenManager } from "@core";
import { shallowRef } from "vue";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import type { RouteNode } from "~/router/RouteNode";
import { routeGostKlausurplanungKalender } from "./RouteGostKlausurplanungKalender";
import { routeGostKlausurplanungKlausurdaten } from "./RouteGostKlausurplanungKlausurdaten";
import { routeGostKlausurplanungKonflikte } from "./RouteGostKlausurplanungKonflikte";
import { routeGostKlausurplanungPlanung } from "./RouteGostKlausurplanungPlanung";
import { routeGostKlausurplanungSchienen } from "./RouteGostKlausurplanungSchienen";

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
		view: routeGostKlausurplanungKlausurdaten,
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
			api.status.stop();
			// Lade die Lehrerliste
			const listLehrer = await api.server.getLehrer(api.schema);
			for (const l of listLehrer)
				mapLehrer.set(l.id, l);
		} else {
			if ((view !== routeGostKlausurplanungKalender) && (view !== routeGostKlausurplanungKlausurdaten))
				view = routeGostKlausurplanungKlausurdaten;
		}
		// Setze den State neu
		this._state.value = {
			abiturjahr: abiturjahr,
			jahrgangsdaten: jahrgangsdaten,
			mapSchueler: mapSchueler,
			faecherManager: faecherManager,
			mapLehrer: mapLehrer,
			halbjahr: this._state.value.halbjahr,
			kursklausurmanager: undefined,
			klausurvorgabenmanager: undefined,
			stundenplanmanager: undefined,
			kursmanager: kursManager,
			view: view,
		};
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

		api.status.start();
		const listKursklausuren = await api.server.getGostKlausurenKursklausurenJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
		const stundenplandaten = await api.server.getStundenplan(api.schema, 1);
		const unterrichte = await api.server.getStundenplanUnterrichte(api.schema, 1);
		const pausenaufsichten = await api.server.getStundenplanPausenaufsichten(api.schema, 1);
		const unterrichtsverteilung = await api.server.getStundenplanUnterrichtsverteilung(api.schema, 1);
		const listKlausurtermine = await api.server.getGostKlausurenKlausurtermineJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
		const kursklausurmanager = new GostKursklausurManager(listKursklausuren, listKlausurtermine);
		const stundenplanmanager = new StundenplanManager(stundenplandaten, unterrichte, pausenaufsichten, unterrichtsverteilung);
		const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgangHalbjahr(api.schema, this.abiturjahr, halbjahr.id);
		const klausurvorgabenmanager = new GostKlausurvorgabenManager(listKlausurvorgaben);
		api.status.stop();
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			faecherManager: this._state.value.faecherManager,
			mapLehrer: this._state.value.mapLehrer,
			halbjahr: halbjahr,
			kursklausurmanager,
			stundenplanmanager,
			klausurvorgabenmanager,
			kursmanager: this._state.value.kursmanager,
			view: this._state.value.view,
		};
		return true;
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

	public async setView(view: RouteNode<any,any>) {
		if ((view !== routeGostKlausurplanungKlausurdaten) &&
			(view !== routeGostKlausurplanungSchienen) &&
			(view !== routeGostKlausurplanungKalender) &&
			(view !== routeGostKlausurplanungPlanung) &&
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
		this.kursklausurmanager.addTermin(termin);
		this.commit();
		api.status.stop();
		return termin;
	}

	loescheKlausurtermin = async (termin: GostKlausurtermin): Promise<boolean> => {
		api.status.start();
		const result = await api.server.deleteGostKlausurenKlausurtermin(api.schema, termin.id);
		this.kursklausurmanager.removeTermin(termin);
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
		Object.assign(this.klausurvorgabenmanager.gibGostKlausurvorgabe(id), vorgabe);
		this.klausurvorgabenmanager.updateKlausurvorgabe(vorgabe);
		this.commit();
		api.status.stop();
		return true;
	}

	loescheKlausurvorgabe = async (vorgabe: GostKlausurvorgabe): Promise<boolean> => {
		api.status.start();
		const result = await api.server.deleteGostKlausurenVorgabe(api.schema, vorgabe.idVorgabe);
		// TODO Falls Constraint verletzt, nicht löschen
		this.klausurvorgabenmanager.removeVorgabe(vorgabe);
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

	patchKlausurtermin = async (termin: Partial<GostKlausurtermin>, id: number): Promise<boolean> => {
		api.status.start();
		await api.server.patchGostKlausurenKlausurtermin(termin, api.schema, id);
		Object.assign(this.kursklausurmanager.gibKlausurtermin(id), termin);
		// this.klausurvorgabenmanager.updateKlausurtermin(termin);
		this.commit();
		api.status.stop();
		return true;
	}

	erzeugeVorgabenAusVorlage = async (): Promise<boolean> => {
		api.status.start();
		await api.server.copyGostKlausurenVorgaben(api.schema, this.abiturjahr, this.halbjahr.id);
		const listKlausurvorgaben = await api.server.getGostKlausurenVorgabenJahrgangHalbjahr(api.schema, this.abiturjahr, this.halbjahr.id);
		this._state.value.klausurvorgabenmanager = new GostKlausurvorgabenManager(listKlausurvorgaben);
		this.commit();
		api.status.stop();
		return true;
	}

}