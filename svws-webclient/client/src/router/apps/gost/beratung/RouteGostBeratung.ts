import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import type { GostBeratungProps } from "~/components/gost/beratung/SGostBeratungProps";
import { routeError } from "~/router/error/RouteError";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { ConfigElement } from "@ui";
import { gostLaufbahnplanungStateImpl } from "~/states/GostLaufbahnplanungStateImpl";

const SGostBeratung = () => import("~/components/gost/beratung/SGostBeratung.vue");

export class RouteGostBeratung extends RouteNode<any, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.beratung", "beratung", SGostBeratung);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Beratung";
		api.config.addElements([new ConfigElement("app.gost.beratung.modus", "user", "normal")]);
		api.config.addElements([new ConfigElement("app.gost.beratung.faecher.anzeigen", "user", "alle")]);
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams): Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (this.parent === undefined) {
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			}
			if (abiturjahr === undefined) {
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Ein Abiturjahrgang muss angegeben sein");
			}
			await gostLaufbahnplanungStateImpl.ladeAbijahrgangsDaten(abiturjahr);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): GostBeratungProps {
		return {
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerdaten: api.benutzerdaten,
			config: () => api.config,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
		};
	}

}

export const routeGostBeratung = new RouteGostBeratung();
