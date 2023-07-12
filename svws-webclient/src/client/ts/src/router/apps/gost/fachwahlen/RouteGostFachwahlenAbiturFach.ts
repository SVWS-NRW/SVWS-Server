import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenAbiturFachProps } from "~/components/gost/fachwahlen/SGostFachwahlenAbiturFachProps";


const SGostFachwahlenAbiturFach = () => import("~/components/gost/fachwahlen/SGostFachwahlenAbiturFach.vue");

export class RouteGostFachwahlenAbiturFach extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen.abitur.fach", "abitur/fach/:idfach(\\d+)?", SGostFachwahlenAbiturFach);
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

	public getProps(to: RouteLocationNormalized): GostFachwahlenAbiturFachProps {
		return {
			fachwahlen: routeGostFachwahlen.data.fachwahlen,
		};
	}

}

export const routeGostFachwahlenAbiturFach = new RouteGostFachwahlenAbiturFach();
