import { GostFaecherManager, GostHalbjahr, GostJahrgangsdaten, GostKursklausurManager, LehrerListeEintrag, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { shallowRef } from "vue";
import { routeLogin } from "~/router/RouteLogin";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ApiStatus } from "~/utils/ApiStatus";
import { routeGostKlausurplanung } from "../RouteGostKlausurplanung";
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
	view: RouteNode<any, any>;
}

export class RouteDataGostKlausurplanung {

	public readonly apiStatus: ApiStatus = new ApiStatus();

	private static _defaultState : RouteStateGostKlausurplanung = {
		abiturjahr: undefined,
		jahrgangsdaten: undefined,
		mapSchueler: new Map(),
		faecherManager: new GostFaecherManager(),
		mapLehrer: new Map(),
		halbjahr: GostHalbjahr.EF1,
		kursklausurmanager: undefined,
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
		this.apiStatus.start();
		// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
		const jahrgangsdaten = await routeLogin.data.api.getGostAbiturjahrgang(routeLogin.data.schema, abiturjahr)
		const listSchueler = await routeLogin.data.api.getGostAbiturjahrgangSchueler(routeLogin.data.schema, abiturjahr);
		const listFaecher = await routeLogin.data.api.getGostAbiturjahrgangFaecher(routeLogin.data.schema, abiturjahr);
		const faecherManager = new GostFaecherManager(listFaecher);
		// Lade die Schülerliste des Abiturjahrgangs
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listSchueler)
			mapSchueler.set(s.id, s);
		this.apiStatus.stop();
		// Lade die Lehrerliste
		const listLehrer = await routeLogin.data.api.getLehrer(routeLogin.data.schema);
		const mapLehrer: Map<number, LehrerListeEintrag> = new Map();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		// Setze den State neu
		this._state.value = {
			abiturjahr: abiturjahr,
			jahrgangsdaten: jahrgangsdaten,
			mapSchueler: mapSchueler,
			faecherManager: faecherManager,
			mapLehrer: mapLehrer,
			halbjahr: this._state.value.halbjahr,
			kursklausurmanager: undefined,
			view: this._state.value.view,
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
		// Lade die Liste der Blockungen
		this.apiStatus.start();
		const listKursklausuren = await routeLogin.data.api.getGostKlausurenKursklausurenJahrgangHalbjahr(routeLogin.data.schema, this.abiturjahr, halbjahr.id);
		const listKlausurtermine = await routeLogin.data.api.getGostKlausurenKlausurtermineJahrgangHalbjahr(routeLogin.data.schema, this.abiturjahr, halbjahr.id);
		const kursklausurmanager = new GostKursklausurManager(listKursklausuren, listKlausurtermine);
		this.apiStatus.stop();
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			faecherManager: this._state.value.faecherManager,
			mapLehrer: this._state.value.mapLehrer,
			halbjahr: halbjahr,
			kursklausurmanager: kursklausurmanager,
			view: this._state.value.view,
		};
		return true;
	}


	public get hatKursklausurManager(): boolean {
		return this._state.value.kursklausurmanager !== undefined;
	}

	public get kursklausurmanager(): GostKursklausurManager {
		if (this._state.value.kursklausurmanager === undefined)
			throw new Error("Es wurde noch keine Daten geladen, so dass kein Kurs-Klausur-Manager zur Verfügung steht.");
		return this._state.value.kursklausurmanager;
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

}