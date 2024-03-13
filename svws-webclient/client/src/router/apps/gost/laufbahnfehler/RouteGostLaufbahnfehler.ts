import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostLaufbahnfehlerProps } from "~/components/gost/laufbahnfehler/SGostLaufbahnfehlerProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { RouteDataGostLaufbahnfehler } from "~/router/apps/gost/laufbahnfehler/RouteDataGostLaufbahnfehler";

import { ConfigElement } from "~/components/Config";
import { routeApp } from "../../RouteApp";

const SGostLaufbahnfehler = () => import("~/components/gost/laufbahnfehler/SGostLaufbahnfehler.vue");

export class RouteGostLaufbahnfehler extends RouteNode<RouteDataGostLaufbahnfehler, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.laufbahnfehler", "laufbahnfehler", SGostLaufbahnfehler, new RouteDataGostLaufbahnfehler());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.laufbahnfehler.filterFehler", "user", "true"),
			new ConfigElement("gost.laufbahnfehler.filterExterne", "user", "false")
		]);
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : parseInt(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: abiturjahr }};
		return false;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to.name === this.name) {
			if (this.parent === undefined)
				throw new Error("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const abiturjahr = parseInt(to_params.abiturjahr);
			if (abiturjahr === undefined || abiturjahr === -1)
				return { name: this.parent.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: this.parent.data.mapAbiturjahrgaenge.values().next().value.abiturjahr }};
		}
		return true;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
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
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostLaufbahnfehlerProps {
		return {
			listBelegpruefungsErgebnisse: () => this.data.listBelegpruefungsErgebnisse,
			gostBelegpruefungsArt: () => this.data.gostBelegpruefungsArt,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			gotoLaufbahnplanung: this.data.gotoLaufbahnplanung,
			importLaufbahnplanung: this.data.importLaufbahnplanung,
			exportLaufbahnplanung: this.data.exportLaufbahnplanung,
			getPdfLaufbahnplanung: this.data.getPdfLaufbahnplanung,
			resetFachwahlenAlle: this.data.resetFachwahlenAlle,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
			filterFehler: () => this.data.filterFehler,
			setFilterFehler: this.data.setFilterFehler,
			filterExterne: () => this.data.filterExterne,
			setFilterExterne: this.data.setFilterExterne,
			apiStatus: api.status,
		};
	}
}

export const routeGostLaufbahnfehler = new RouteGostLaufbahnfehler();
