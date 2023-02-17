import { GostBlockungListeneintrag, GostHalbjahr, GostJahrgangsdaten, SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { list } from "postcss";
import { shallowRef } from "vue";
import { routeLogin } from "~/router/RouteLogin";
import { RouteManager } from "~/router/RouteManager";
import { routeGost } from "../../RouteGost";
import { routeGostKursplanungHalbjahr } from "./RouteGostKursplanungHalbjahr";

interface RouteStateGostKursplanung {
	// Daten nur abhängig von dem Abiturjahrgang
	abiturjahr: number | undefined;
	jahrgangsdaten: GostJahrgangsdaten | undefined;
	mapSchueler: Map<number, SchuelerListeEintrag>;
	// ... auch abhängig vom ausgewählten Halbjahr der gymnasialen Oberstufe
	halbjahr: GostHalbjahr;
	mapBlockungen: Map<number, GostBlockungListeneintrag>;
	auswahlBlockung: GostBlockungListeneintrag | undefined;
}

export class RouteDataGostKursplanung {

	private static _defaultState : RouteStateGostKursplanung = {
		abiturjahr: undefined,
		jahrgangsdaten: undefined,
		mapSchueler: new Map(),
		halbjahr: GostHalbjahr.EF1,
		mapBlockungen: new Map(),
		auswahlBlockung: undefined,
	}

	private _state = shallowRef<RouteStateGostKursplanung>(RouteDataGostKursplanung._defaultState);

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
			this._state.value = RouteDataGostKursplanung._defaultState;
			return;
		}
		// Lade die Daten für die Kursplanung, die nur vom Abiturjahrgang abhängen
		const jahrgangsdaten = await routeLogin.data.api.getGostAbiturjahrgang(routeLogin.data.schema, abiturjahr)
		const listSchueler = await routeLogin.data.api.getGostAbiturjahrgangSchueler(routeLogin.data.schema, abiturjahr);
		// Lade die Schülerliste des Abiturjahrgangs
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const s of listSchueler)
			mapSchueler.set(s.id, s);
		// Setze den State neu
		this._state.value = {
			abiturjahr: abiturjahr,
			jahrgangsdaten: jahrgangsdaten,
			mapSchueler: mapSchueler,
			halbjahr: this._state.value.halbjahr,
			mapBlockungen: new Map(),
			auswahlBlockung: undefined,
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


	public get halbjahr() : GostHalbjahr {
		return this._state.value.halbjahr;
	}

	public async setHalbjahr(halbjahr: GostHalbjahr) {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (halbjahr === this._state.value.halbjahr)
			return;
		// Lade die Liste der Blockungen
		const listBlockungen = await routeLogin.data.api.getGostAbiturjahrgangBlockungsliste(routeLogin.data.schema, this.abiturjahr, halbjahr.id);
		const mapBlockungen: Map<number, GostBlockungListeneintrag> = new Map();
		for (const bl of listBlockungen)
			mapBlockungen.set(bl.id, bl);
		let auswahlBlockung: GostBlockungListeneintrag | undefined = undefined;
		if (listBlockungen.size() > 0) {
			for (const bl of listBlockungen) {
				if (bl.istAktiv === true) {
					auswahlBlockung = bl;
					break;
				}
			}
			if (auswahlBlockung === undefined)
				auswahlBlockung = listBlockungen.get(0);
		}
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			halbjahr: halbjahr,
			mapBlockungen: mapBlockungen,
			auswahlBlockung: auswahlBlockung,
		};
	}

	public get hatBlockung(): boolean {
		return this._state.value.auswahlBlockung !== undefined;
	}

	public get mapBlockungen(): Map<number, GostBlockungListeneintrag> {
		return this._state.value.mapBlockungen;
	}

	public get auswahlBlockung() : GostBlockungListeneintrag {
		if (this._state.value.auswahlBlockung === undefined)
			throw new Error("Es wurde noch keine gültige Blockung ausgewählt.");
		return this._state.value.auswahlBlockung;
	}

	public async setAuswahlBlockung(value: GostBlockungListeneintrag | undefined) {
		if (this._state.value.abiturjahr === undefined)
			throw new Error("Es kann kein Halbjahr ausgewählt werden, wenn zuvor kein Abiturjahrgang ausgewählt wurde.");
		if (value === undefined) {
			this._state.value = {
				abiturjahr: this._state.value.abiturjahr,
				jahrgangsdaten: this._state.value.jahrgangsdaten,
				mapSchueler: this._state.value.mapSchueler,
				halbjahr: this._state.value.halbjahr,
				mapBlockungen: this._state.value.mapBlockungen,
				auswahlBlockung: undefined,
			};
			return;
		}
		// TODO Anpassung der Blockungs-Daten, des Managers, etc.
		this._state.value = {
			abiturjahr: this._state.value.abiturjahr,
			jahrgangsdaten: this._state.value.jahrgangsdaten,
			mapSchueler: this._state.value.mapSchueler,
			halbjahr: this._state.value.halbjahr,
			mapBlockungen: this._state.value.mapBlockungen,
			auswahlBlockung: value,
		};
	}

	removeBlockung = async () => {
		if (!this.hatBlockung)
			return;
		await routeLogin.data.api.deleteGostBlockung(routeLogin.data.schema, this.auswahlBlockung.id);
		await this.setAuswahlBlockung(undefined);
		await this.gotoHalbjahr(this.halbjahr);
	}

	gotoHalbjahr = async (value: GostHalbjahr) => {
		await RouteManager.doRoute(routeGostKursplanungHalbjahr.getRoute(this.abiturjahr, this.halbjahr.id, undefined));
	}

	gotoBlockung = async (idBlockung : number) => {
		if (idBlockung !== this._state.value.auswahlBlockung?.id)
			await RouteManager.doRoute(routeGostKursplanungHalbjahr.getRoute(this.abiturjahr, this.halbjahr.id, idBlockung));
	}

}