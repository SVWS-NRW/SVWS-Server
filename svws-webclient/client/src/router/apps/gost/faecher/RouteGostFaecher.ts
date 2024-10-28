import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { RouteDataGostFaecher } from "~/router/apps/gost/faecher/RouteDataGostFaecher";

import type { GostFaecherProps } from "~/components/gost/faecher/SGostFaecherProps";
import { api } from "~/router/Api";
import { schulformenGymOb } from "~/router/RouteHelper";
import { routeError } from "~/router/error/RouteError";


const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostFaecher, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN,
			BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN,
		], "gost.faecher", "faecher", SGostFaecher, new RouteDataGostFaecher());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (abiturjahr === undefined)
				return routeGost.getRoute();
			return true;
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, ["abiturjahr"]);
			if (this.parent === undefined)
				throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
			await this.data.setEintrag(abiturjahr);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public getProps(to: RouteLocationNormalized): GostFaecherProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerKompetenzenAbiturjahrgaenge: api.benutzerKompetenzenAbiturjahrgaenge,
			faecherManager: () => routeGost.data.faecherManager,
			patchFach: routeGost.data.patchFach,
			patchFachkombination: this.data.patchFachkombination,
			addFachkombination: this.data.addFachkombination,
			removeFachkombination: this.data.removeFachkombination,
			patchJahrgangsdaten: routeGost.data.patchJahrgangsdaten,
			jahrgangsdaten: () => routeGost.data.jahrgangsdaten,
			mapFachkombinationen: () => this.data.mapFachkombinationen
		};
	}

}

export const routeGostFaecher = new RouteGostFaecher();
