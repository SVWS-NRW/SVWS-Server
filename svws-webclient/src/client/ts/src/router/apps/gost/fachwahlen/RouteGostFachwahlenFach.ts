import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenFachProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachProps";


const SGostFachwahlenFach = () => import("~/components/gost/fachwahlen/SGostFachwahlenFach.vue");

export class RouteGostFachwahlenFach extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen.fach", "fach/:idfach(\\d+)?", SGostFachwahlenFach);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fachspezifisch";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? undefined : parseInt(params.abiturjahr);
		return (abiturjahr === undefined) || (abiturjahr === -1);
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if ((to_params.abiturjahr instanceof Array) || (to_params.idfach instanceof Array))
			return new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		const idFach = !to_params.idfach ? undefined : parseInt(to_params.idfach);
	}

	public getRoute(abiturjahr: number, idfach: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr, idfach: idfach }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenFachProps {
		return {
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
		};
	}

}

export const routeGostFachwahlenFach = new RouteGostFachwahlenFach();
