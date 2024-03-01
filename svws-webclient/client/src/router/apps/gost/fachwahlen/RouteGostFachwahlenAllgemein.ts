import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenAllgemeinProps } from "~/components/gost/fachwahlen/SGostFachwahlenAllgemeinProps";


const SGostFachwahlenAllgemein = () => import("~/components/gost/fachwahlen/SGostFachwahlenAllgemein.vue");

export class RouteGostFachwahlenAllgemein extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen.allgemein", "allgemein", SGostFachwahlenAllgemein);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Allgemein";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : parseInt(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { abiturjahr }};
		return false;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.abiturjahr instanceof Array)
			return new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		// const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenAllgemeinProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
		};
	}

}

export const routeGostFachwahlenAllgemein = new RouteGostFachwahlenAllgemein();
