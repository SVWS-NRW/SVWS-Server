import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { GostLaufbahnfehlerProps } from "~/components/gost/laufbahnfehler/SGostLaufbahnfehlerProps";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { RouteDataGostLaufbahnfehler } from "~/router/apps/gost/laufbahnfehler/RouteDataGostLaufbahnfehler";

import { ConfigElement } from "~/components/Config";
import { routeApp } from "../../RouteApp";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";

const SGostLaufbahnfehler = () => import("~/components/gost/laufbahnfehler/SGostLaufbahnfehler.vue");

export class RouteGostLaufbahnfehler extends RouteNode<RouteDataGostLaufbahnfehler, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.laufbahnfehler", "laufbahnfehler", SGostLaufbahnfehler, new RouteDataGostLaufbahnfehler());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Laufbahnen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		api.config.addElements([
			new ConfigElement("gost.laufbahnfehler.filterFehler", "user", "true"),
			new ConfigElement("gost.laufbahnfehler.filterExterne", "user", "false"),
			new ConfigElement("gost.laufbahnfehler.filterNurMitFachwahlen", "user", "false"),
		]);
	}

	public checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = params ? RouteNode.getIntParams(params, ["abiturjahr"]) : { abiturjahr: null };
			if ((abiturjahr === null) || (abiturjahr === -1))
				return { name: routeGost.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
			return false;
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			if (to.name === this.name) {
				if (this.parent === undefined)
					throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
				const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
				if (abiturjahr === undefined || abiturjahr === -1) {
					const [alternativ] = this.parent.data.mapAbiturjahrgaenge.values();
					return { name: this.parent.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr: alternativ.abiturjahr }};
				}
			}
			return true;
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (abiturjahr === undefined)
				throw new DeveloperNotificationException("Fehler: Das Abiturjahr darf an dieser Stelle nicht undefined sein.");
			await this.data.setAbiturjahr(abiturjahr);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): GostLaufbahnfehlerProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenAbiturjahrgaenge: api.benutzerKompetenzenAbiturjahrgaenge,
			listBelegpruefungsErgebnisse: () => this.data.listBelegpruefungsErgebnisse,
			gostBelegpruefungsArt: () => this.data.gostBelegpruefungsArt,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			gotoLaufbahnplanung: this.data.gotoLaufbahnplanung,
			gotoSprachenfolge: this.data.gotoSprachenfolge,
			importLaufbahnplanung: this.data.importLaufbahnplanung,
			exportLaufbahnplanung: this.data.exportLaufbahnplanung,
			getPdfLaufbahnplanung: this.data.getPdfLaufbahnplanung,
			resetFachwahlenAlle: this.data.resetFachwahlenAlle,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
			filterFehler: () => this.data.filterFehler,
			setFilterFehler: this.data.setFilterFehler,
			filterExterne: () => this.data.filterExterne,
			setFilterExterne: this.data.setFilterExterne,
			filterNurMitFachwahlen: () => this.data.filterNurMitFachwahlen,
			setFilterNurMitFachwahlen: this.data.setFilterNurMitFachwahlen,
			apiStatus: api.status,
		};
	}
}

export const routeGostLaufbahnfehler = new RouteGostLaufbahnfehler();
