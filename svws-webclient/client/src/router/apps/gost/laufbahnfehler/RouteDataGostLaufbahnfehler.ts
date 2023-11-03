import { shallowRef } from "vue";

import type { GostBelegpruefungsErgebnisse, List } from "@core";
import { ArrayList, DeveloperNotificationException, GostBelegpruefungsArt} from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";

import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";


interface RouteStateDataGostLaufbahnfehler {
	abiturjahr: number;
	listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
}

export class RouteDataGostLaufbahnfehler  {

	private static _defaultState: RouteStateDataGostLaufbahnfehler = {
		abiturjahr: -1,
		listBelegpruefungsErgebnisse: new ArrayList(),
		gostBelegpruefungsArt: GostBelegpruefungsArt.GESAMT,
	}

	private _state = shallowRef(RouteDataGostLaufbahnfehler._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateDataGostLaufbahnfehler>) {
		this._state.value = Object.assign({ ... RouteDataGostLaufbahnfehler._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateDataGostLaufbahnfehler>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	get gostBelegpruefungsArt(): GostBelegpruefungsArt {
		return this._state.value.gostBelegpruefungsArt;
	}

	get abiturjahr(): number {
		return this._state.value.abiturjahr;
	}

	get listBelegpruefungsErgebnisse(): List<GostBelegpruefungsErgebnisse> {
		return this._state.value.listBelegpruefungsErgebnisse;
	}

	private async updateList(abiturjahr : number, gostBelegpruefungsArt : GostBelegpruefungsArt) {
		if (abiturjahr < 1)
			throw new DeveloperNotificationException(`Fehlerhafte Übergabe des Abiturjahrs: ${abiturjahr}`)
		const listBelegpruefungsErgebnisse = (gostBelegpruefungsArt === GostBelegpruefungsArt.GESAMT)
			? await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(api.schema, abiturjahr)
			: await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseEF1(api.schema, abiturjahr);
		this.setPatchedState({ listBelegpruefungsErgebnisse, gostBelegpruefungsArt, abiturjahr });
	}

	public async setAbiturjahr(abiturjahr: number) {
		await this.updateList(abiturjahr, this._state.value.gostBelegpruefungsArt);
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsArt) => {
		if (gostBelegpruefungsArt === this.gostBelegpruefungsArt)
			return;
		await this.updateList(this.abiturjahr, gostBelegpruefungsArt)
		this.setPatchedState({ gostBelegpruefungsArt });
	}

	gotoLaufbahnplanung = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute(idSchueler));

	getPdfWahlbogen = async(title: string, list: List<number>) => {
		try {
			api.status.start();
			switch (title) {
				case 'Laufbahnwahlbogen':
					return await api.server.pdfGostLaufbahnplanungSchuelerWahlbogen(list, api.schema);
				case 'Laufbahnwahlbogen (nur Belegung)':
					return await api.server.pdfGostLaufbahnplanungSchuelerWahlbogenNurBelegung(list, api.schema);
				default:
					throw new DeveloperNotificationException('Es wurde kein passender Parameter zur Erzeugung des PDF übergeben.')
			}
		} catch(e) {
			throw new DeveloperNotificationException("Fehler beim Herunterladen der Wahlbögen");
		} finally {
			api.status.stop();
		}
	}

	resetFachwahlenAlle = async () => {
		await api.server.resetGostAbiturjahrgangSchuelerFachwahlen(api.schema, this.abiturjahr);
		await this.setAbiturjahr(this.abiturjahr);
	}

}

