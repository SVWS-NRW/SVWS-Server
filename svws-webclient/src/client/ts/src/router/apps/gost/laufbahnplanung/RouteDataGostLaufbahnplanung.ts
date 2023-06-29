import { shallowRef } from "vue";

import type { GostBelegpruefungsErgebnisse, List } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";


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

