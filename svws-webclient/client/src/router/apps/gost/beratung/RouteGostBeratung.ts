import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import type { GostBeratungProps } from "~/components/gost/beratung/SGostBeratungProps";
import { RouteDataGostBeratung } from "~/router/apps/gost/beratung/RouteDataGostBeratung";
import { routeError } from "~/router/error/RouteError";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";

const SGostBeratung = () => import("~/components/gost/beratung/SGostBeratung.vue");

export class RouteGostBeratung extends RouteNode<RouteDataGostBeratung, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN
		], "gost.beratung", "beratung", SGostBeratung, new RouteDataGostBeratung());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Beratung";
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			if (abiturjahr === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Ein Abiturjahrgang muss angegeben sein");
			await this.data.ladeDaten(abiturjahr, isEntering);
		} catch(e) {
			return routeError.getRoute(e as DeveloperNotificationException);
		}
	}


	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
	}


	public getProps(to: RouteLocationNormalized): GostBeratungProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerdaten: api.benutzerdaten,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
			setWahl: this.data.setWahl,
			setGostBelegpruefungsArt: this.data.setGostBelegpruefungsArt,
			gostBelegpruefungsArt: () => this.data.gostBelegpruefungsArt,
			gostBelegpruefungErgebnis: () => this.data.gostBelegpruefungErgebnis,
			abiturdatenManager: () => this.data.abiturdatenManager,
			mapLehrer: this.data.mapLehrer,
			resetFachwahlen: this.data.resetFachwahlen,
			resetFachwahlenAlle: this.data.resetFachwahlenAlle,
			beratungslehrer: () => this.data.beratungslehrer,
			addBeratungslehrer: this.data.addBeratungslehrer,
			removeBeratungslehrer: this.data.removeBeratungslehrer,
			gotoKursblockung: this.data.gotoKursblockung,
		};
	}

}

export const routeGostBeratung = new RouteGostBeratung();
