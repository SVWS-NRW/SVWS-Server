import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";

import { RouteDataGostFaecher } from "~/router/apps/gost/faecher/RouteDataGostFaecher";

import type { GostFaecherProps } from "~/components/gost/faecher/SGostFaecherProps";
import { routeApp } from "../../RouteApp";
import { api } from "~/router/Api";


const SGostFaecher = () => import("~/components/gost/faecher/SGostFaecher.vue");

export class RouteGostFaecher extends RouteNode<RouteDataGostFaecher, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.faecher", "faecher", SGostFaecher, new RouteDataGostFaecher());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = !to_params.abiturjahr ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined)
			return routeGost.getRoute();
		return true;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.parent === undefined)
			throw new DeveloperNotificationException("Fehler: Die Route ist ungültig - Parent ist nicht definiert");
		const id = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		await this.data.setEintrag(id);
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFaecherProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			benutzerdaten: api.benutzerdaten,
			// beratungslehrer: () => this.data.beratungslehrer,
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
