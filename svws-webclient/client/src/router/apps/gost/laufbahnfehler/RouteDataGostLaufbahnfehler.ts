import type { ApiFile, GostBelegpruefungsErgebnisse, List, ReportingParameter } from "@core";
import { ArrayList, DeveloperNotificationException, GostBelegpruefungsArt, OpenApiError, ReportingReportvorlage, SimpleOperationResponse } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";
import { routeSchuelerSprachen } from "../../schueler/sprachen/RouteSchuelerSprachen";
import { routeApp } from "~/router/apps/RouteApp";


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
	};

	get filterExterne(): boolean {
		return api.config.getValue('gost.laufbahnfehler.filterExterne') === 'true';
	}

	setFilterExterne = async (value: boolean) => {
		await api.config.setValue('gost.laufbahnfehler.filterExterne', value ? "true" : "false");
	};

	get filterNurMitFachwahlen(): boolean {
		return api.config.getValue('gost.laufbahnfehler.filterNurMitFachwahlen') === 'true';
	}

	setFilterNurMitFachwahlen = async (value: boolean) => {
		await api.config.setValue('gost.laufbahnfehler.filterNurMitFachwahlen', value ? "true" : "false");
	};

	get filterNeuaufnahmen(): boolean {
		return api.config.getValue('gost.laufbahnfehler.filterNeuaufnahmen') === 'true';
	}

	setFilterNeuaufnahmen = async (value: boolean) => {
		await api.config.setValue('gost.laufbahnfehler.filterNeuaufnahmen', value ? "true" : "false");
	};

	protected async updateList(abiturjahr: number, gostBelegpruefungsArt: GostBelegpruefungsArt) {
		if (abiturjahr < 1) {
			throw new DeveloperNotificationException(`Fehlerhafte Übergabe des Abiturjahrs: ${abiturjahr}`);
		}
		const listBelegpruefungsErgebnisse = (gostBelegpruefungsArt === GostBelegpruefungsArt.GESAMT)
			? await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseGesamt(api.schema, abiturjahr)
			: await api.server.getGostAbiturjahrgangBelegpruefungsergebnisseEF1(api.schema, abiturjahr);
		this.setPatchedState({ listBelegpruefungsErgebnisse, gostBelegpruefungsArt, abiturjahr });
	}

	public async setAbiturjahr(abiturjahr: number) {
		await this.updateList(abiturjahr, this._state.value.gostBelegpruefungsArt);
	}

	setGostBelegpruefungsArt = async (gostBelegpruefungsArt: GostBelegpruefungsArt) => {
		if (gostBelegpruefungsArt === this.gostBelegpruefungsArt) {
			return;
		}
		await this.updateList(this.abiturjahr, gostBelegpruefungsArt);
		this.setPatchedState({ gostBelegpruefungsArt });
	};

	gotoLaufbahnplanung = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute({ id: idSchueler }));

	gotoSprachenfolge = async (idSchueler: number) =>
		await RouteManager.doRoute(routeSchuelerSprachen.getRoute({ id: idSchueler }));

	importLaufbahnplanung = async (data: FormData): Promise<void> => {
		api.status.start();
		await api.server.importGostSchuelerLaufbahnplanungen(data, api.schema);
		await this.setAbiturjahr(this.abiturjahr);
		api.status.stop();
	};

	exportLaufbahnplanung = async (schueler: List<number>): Promise<ApiFile> => {
		api.status.start();
		const res = await api.server.exportGostSchuelerLaufbahnplanungen(schueler, api.schema);
		api.status.stop();
		return res;
	};

	getPdfLaufbahnplanung = async (reportingParameter: ReportingParameter) => {
		try {
			api.status.start();
			return await api.server.pdfReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	sendEmailPdfLaufbahnplanung = async (reportingParameter: ReportingParameter) => {
		try {
			api.status.start();
			return await api.server.emailReport(reportingParameter, api.schema);
		} finally {
			api.status.stop();
		}
	};

	resetFachwahlenAlle = async (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => {
		if ([...ergebnisse].length === this.listBelegpruefungsErgebnisse.size()) {
			await api.server.resetGostAbiturjahrgangSchuelerFachwahlen(api.schema, this.abiturjahr);
		} else {
			for (const ergebnis of ergebnisse) {
				await api.server.resetGostSchuelerFachwahlen(api.schema, ergebnis.schueler.id);
			}
		}
		await this.setAbiturjahr(this.abiturjahr);
	};

	loeschenFachwahlenSelected = async (ergebnisse: Iterable<GostBelegpruefungsErgebnisse>) => {
		const idsSchueler = new ArrayList<number>();
		for (const ergebnis of ergebnisse) {
			idsSchueler.add(ergebnis.schueler.id);
		}
		await api.server.deleteGostSchuelerFachwahlenMultiple(idsSchueler, api.schema);
		await this.setAbiturjahr(this.abiturjahr);
	};

}
