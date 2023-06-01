import type { GostBelegpruefungsErgebnisse, List } from "@svws-nrw/svws-core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostLaufbahnplanungProps } from "~/components/gost/laufbahnplanung/SGostLaufbahnplanungProps";
import type { RouteGost } from "~/router/apps/RouteGost";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException, Schulform } from "@svws-nrw/svws-core";
import { shallowRef } from "vue";
import { ConfigElement } from "~/components/Config";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeSchuelerLaufbahnplanung } from "../schueler/RouteSchuelerLaufbahnplanung";


interface RouteStateDataGostLaufbahnplanung {
	abiturjahr: number;
	listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: 'ef1'|'gesamt';
}
export class RouteDataGostLaufbahnplanung  {

	private static _defaultState: RouteStateDataGostLaufbahnplanung = {
		abiturjahr: -1,
		listBelegpruefungsErgebnisse: new ArrayList(),
		gostBelegpruefungsArt: 'gesamt',
	}

	private _state = shallowRef(RouteDataGostLaufbahnplanung._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostLaufbahnplanung>) {
		this._state.value = Object.assign({ ... RouteDataGostLaufbahnplanung._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostLaufbahnplanung>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get gostBelegpruefungsArt(): 'ef1'|'gesamt' {
		return this._state.value.gostBelegpruefungsArt;
	}

	get abiturjahr(): number {
		return this._state.value.abiturjahr;
	}

	get listBelegpruefungsErgebnisse(): List<GostBelegpruefungsErgebnisse> {
		return this._state.value.listBelegpruefungsErgebnisse;
	}

	private async updateList(abiturjahr : number, gostBelegpruefungsArt : 'ef1'|'gesamt') {
		if (abiturjahr < 1)
			throw new DeveloperNotificationException(`Fehlerhafte Übergabe des Abiturjahrs: ${abiturjahr}`)
		const listBelegpruefungsErgebnisse = (gostBelegpruefungsArt === 'gesamt')
			? await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(api.schema, abiturjahr)
			: await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseEF1(api.schema, abiturjahr);
		this.setPatchedState({ listBelegpruefungsErgebnisse, gostBelegpruefungsArt, abiturjahr });
	}

	public async setAbiturjahr(abiturjahr: number) {
		await this.updateList(abiturjahr, this._state.value.gostBelegpruefungsArt);
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: 'ef1'|'gesamt') => {
		if (gostBelegpruefungsArt === this.gostBelegpruefungsArt)
			return;
		await this.updateList(this.abiturjahr, gostBelegpruefungsArt)
		this.setPatchedState({ gostBelegpruefungsArt });
	}

	gotoLaufbahnplanung = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute(idSchueler));

	getPdfWahlbogen = async() => {
		try {
			api.status.start();
			return await api.server.getGostAbiturjahrgangPDFWahlboegen(api.schema, this.abiturjahr);
		} catch(e) {
			throw new DeveloperNotificationException("Fehler beim Herunterladen der Wahlbögen");
		} finally {
			api.status.stop();
		}
	}

}

const SGostLaufbahnplanung = () => import("~/components/gost/laufbahnplanung/SGostLaufbahnplanung.vue");

export class RouteGostLaufbahnplanung extends RouteNode<RouteDataGostLaufbahnplanung, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.laufbahnplanung", "laufbahnplanung", SGostLaufbahnplanung, new RouteDataGostLaufbahnplanung());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnplanung";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.laufbahnplanung.filterFehler", "user", "true")
		]);
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? undefined : parseInt(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to.name === this.name) {
			if (this.parent === undefined)
				throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const abiturjahr = parseInt(to_params.abiturjahr);
			if (abiturjahr === undefined || abiturjahr === -1)
				return { name: this.parent.defaultChild!.name, params: { abiturjahr: this.parent.data.mapAbiturjahrgaenge.values().next().value.abiturjahr }};
		}
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		// Prüfe das Abiturjahr
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined)
			throw new Error("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
		await this.data.setAbiturjahr(abiturjahr);
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostLaufbahnplanungProps {
		return {
			config: api.config,
			listBelegpruefungsErgebnisse: () => this.data.listBelegpruefungsErgebnisse,
			gostBelegpruefungsArt: () => this.data.gostBelegpruefungsArt,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			gotoLaufbahnplanung: this.data.gotoLaufbahnplanung,
			getPdfWahlbogen: this.data.getPdfWahlbogen,
			abiturjahr: this.data.abiturjahr,
			apiStatus: api.status,
		};
	}

}

export const routeGostLaufbahnplanung = new RouteGostLaufbahnplanung();
