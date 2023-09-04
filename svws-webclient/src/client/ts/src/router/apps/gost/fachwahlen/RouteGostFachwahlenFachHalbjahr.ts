import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenFachHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahrProps";
import { ref } from "vue";


const SGostFachwahlenFachHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenFachHalbjahr.vue");

export class RouteGostFachwahlenFachHalbjahr extends RouteNode<unknown, RouteGost> {

	private _idFach = ref<number>(-1);
	private _halbjahr = ref<GostHalbjahr>(GostHalbjahr.EF1);

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
		this._idFach.value = parseInt(to_params.idfach);
		const idHalbjahr = parseInt(to_params.idhalbjahr);
		const halbjahr = GostHalbjahr.fromID(idHalbjahr);
		if (halbjahr === null)
			return new Error("Fehler: Das Halbjahr " + to_params.idhalbjahr + " ist ungültig");
		this._halbjahr.value = halbjahr;
	}

	public getRoute(abiturjahr: number, idfach: number, halbjahr: GostHalbjahr) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr, idfach: idfach, idhalbjahr: halbjahr.id }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenFachHalbjahrProps {
		return {
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
			fachID: this._idFach.value,
			halbjahr: this._halbjahr.value,
		};
	}

}

export const routeGostFachwahlenFachHalbjahr = new RouteGostFachwahlenFachHalbjahr();
