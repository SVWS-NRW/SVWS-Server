import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, GostHalbjahr, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { routeGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlen";

import type { GostFachwahlenHalbjahrProps } from "~/components/gost/fachwahlen/SGostFachwahlenHalbjahrProps";
import { ref } from "vue";


const SGostFachwahlenHalbjahr = () => import("~/components/gost/fachwahlen/SGostFachwahlenHalbjahr.vue");

export class RouteGostFachwahlenHalbjahr extends RouteNode<unknown, RouteGost> {

	private _halbjahr = ref<GostHalbjahr>(GostHalbjahr.EF1);

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen.halbjahr", "halbjahr/:idhalbjahr(\\d+)?", SGostFachwahlenHalbjahr);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen - Halbjahresbezogen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
	}

	public checkHidden(params?: RouteParams) {
		if (params?.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const abiturjahr = (params === undefined) || !params.abiturjahr ? null : parseInt(params.abiturjahr);
		if ((abiturjahr === null) || (abiturjahr === -1))
			return { name: routeGost.defaultChild!.name, params: { abiturjahr: abiturjahr }};
		return false;
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.idhalbjahr instanceof Array)
			return new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idHalbjahr = parseInt(to_params.idhalbjahr);
		const halbjahr = GostHalbjahr.fromID(idHalbjahr);
		if (halbjahr === null)
			return new Error("Fehler: Das Halbjahr " + to_params.idhalbjahr + " ist ungültig");
		this._halbjahr.value = halbjahr;
	}

	public getRoute(abiturjahr: number, halbjahr: GostHalbjahr) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr, idhalbjahr: halbjahr.id }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenHalbjahrProps {
		return {
			gotoLaufbahnplanung: routeGostFachwahlen.gotoLaufbahnplanung,
			fachwahlstatistik: routeGostFachwahlen.data.fachwahlstatistik,
			fachwahlenManager: routeGostFachwahlen.data.fachwahlenManager,
			mapSchueler: routeGostFachwahlen.data.mapSchueler,
			faecherManager: routeGost.data.faecherManager,
			halbjahr: this._halbjahr.value,
		};
	}

}

export const routeGostFachwahlenHalbjahr = new RouteGostFachwahlenHalbjahr();
