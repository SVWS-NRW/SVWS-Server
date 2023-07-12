import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenFachHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahrProps";


const SGostFachwahlenFachHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahr.vue");

export class RouteGostFachwahlenFachHalbjahr extends RouteNode<unknown, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen.fach.halbjahr", "fach/:idfach(\\d+)/halbjahr/:idhalbjahr(\\d+)", SGostFachwahlenFachHalbjahr);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Fach- und Halbjahresbezogen";
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
		if ((to_params.idhalbjahr instanceof Array) || (to_params.idfach instanceof Array))
			return new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idFach = parseInt(to_params.idfach);
		const idHalbjahr = parseInt(to_params.idhalbjahr);
		const halbjahr = GostHalbjahr.fromID(idHalbjahr);
		if (halbjahr === null)
			return new Error("Fehler: Das Halbjahr " + to_params.idhalbjahr + " ist ungültig");
	}

	public getRoute(abiturjahr: number, idfach: number, halbjahr: GostHalbjahr) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr, idfach: idfach, idhalbjahr: halbjahr.id }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenFachHalbjahrProps {
		return {
			fachwahlen: routeGostFachwahlen.data.fachwahlen,
		};
	}

}

export const routeGostFachwahlenFachHalbjahr = new RouteGostFachwahlenFachHalbjahr();
