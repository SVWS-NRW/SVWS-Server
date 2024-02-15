import type { ApiFile, GostBelegpruefungsErgebnisse, List} from "@core";
import { ArrayList, DeveloperNotificationException, GostBelegpruefungsArt, OpenApiError, SimpleOperationResponse } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";


interface RouteStateDataGostLaufbahnfehler extends RouteStateInterface {
	abiturjahr: number;
	listBelegpruefungsErgebnisse: List<GostBelegpruefungsErgebnisse>;
	gostBelegpruefungsArt: GostBelegpruefungsArt;
}

const defaultState = <RouteStateDataGostLaufbahnfehler> {
	abiturjahr: -1,
	listBelegpruefungsErgebnisse: new ArrayList(),
	gostBelegpruefungsArt: GostBelegpruefungsArt.GESAMT,
};

export class RouteDataGostLaufbahnfehler extends RouteData<RouteStateDataGostLaufbahnfehler> {

	public constructor() {
		super(defaultState);
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

	get filterFehler(): boolean {
		return api.config.getValue('gost.laufbahnfehler.filterFehler') === 'true';
	}

	setFilterFehler = async (value: boolean) => {
		await api.config.setValue('gost.laufbahnfehler.filterFehler', value ? "true" : "false");
	}

	get filterExterne(): boolean {
		return api.config.getValue('gost.laufbahnfehler.filterExterne') === 'true';
	}

	setFilterExterne = async (value: boolean) => {
		await api.config.setValue('gost.laufbahnfehler.filterExterne', value ? "true" : "false");
	}

	protected async updateList(abiturjahr : number, gostBelegpruefungsArt : GostBelegpruefungsArt) {
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


	importLaufbahnplanung = async (data: FormData): Promise<SimpleOperationResponse> => {
		api.status.start();
		try {
			const res = await api.server.importGostSchuelerLaufbahnplanungen(data, api.schema);
			api.status.stop();
			return res;
		} catch(e) {
			if ((e instanceof OpenApiError) && (e.response !== null) && ((e.response.status === 400) || (e.response.status === 409))) {
				const json : string = await e.response.text();
				return SimpleOperationResponse.transpilerFromJSON(json);
			}
			const result = new SimpleOperationResponse();
			result.log.add("Fehler bei der Server-Anfrage. ");
			if (e instanceof Error)
				result.log.add("  " + e.message);
			api.status.stop();
			return result;
		}
	}

	exportLaufbahnplanung = async (schueler: List<number>): Promise<ApiFile> => {
		api.status.start();
		const res = await api.server.exportGostSchuelerLaufbahnplanungen(schueler, api.schema);
		api.status.stop();
		return res;
	}

	getPdfLaufbahnplanung = async(title: string, list: List<number>, detaillevel: number) => {
		try {
			api.status.start();
			switch (title) {
				case 'Laufbahnwahlbogen':
					return await api.server.pdfGostLaufbahnplanungSchuelerWahlbogen(list, api.schema);
				case 'Laufbahnwahlbogen (nur Belegung)':
					return await api.server.pdfGostLaufbahnplanungSchuelerWahlbogenNurBelegung(list, api.schema);
				case 'Ergebnisliste Laufbahnwahlen':
					return await api.server.pdfGostLaufbahnplanungSchuelerErgebnisuebersicht(list, api.schema, detaillevel);
				default:
					throw new DeveloperNotificationException('Es wurde kein passender Parameter zur Erzeugung des PDF übergeben.')
			}
		} catch(e) {
			throw new DeveloperNotificationException("Fehler beim Herunterladen der PDF-Datei zur Laufbahnplanung");
		} finally {
			api.status.stop();
		}
	}

	resetFachwahlenAlle = async () => {
		await api.server.resetGostAbiturjahrgangSchuelerFachwahlen(api.schema, this.abiturjahr);
		await this.setAbiturjahr(this.abiturjahr);
	}

}

