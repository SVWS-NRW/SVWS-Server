import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeGost, type RouteGost} from "~/router/apps/gost/RouteGost";

import { RouteDataGostFachwahlen } from "~/router/apps/gost/fachwahlen/RouteDataGostFachwahlen";

import { routeGostFachwahlenAllgemein } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAllgemein";
import { routeGostFachwahlenAbitur } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAbitur";
import { routeGostFachwahlenAbiturFach } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenAbiturFach";
import { routeGostFachwahlenFach } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenFach";
import { routeGostFachwahlenFachHalbjahr } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenFachHalbjahr";
import { routeGostFachwahlenHalbjahr } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenHalbjahr";
import { routeGostFachwahlenLeistungskurse } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenLeistungskurse";
import { routeGostFachwahlenZusatzkurse } from "~/router/apps/gost/fachwahlen/RouteGostFachwahlenZusatzkurse";

import type { GostFachwahlenProps } from "~/components/gost/fachwahlen/SGostFachwahlenProps";
import { RouteManager } from "~/router/RouteManager";
import { routeSchuelerLaufbahnplanung } from "../../schueler/laufbahnplanung/RouteSchuelerLaufbahnplanung";


const SGostFachwahlen = () => import("~/components/gost/fachwahlen/SGostFachwahlen.vue");

export class RouteGostFachwahlen extends RouteNode<RouteDataGostFachwahlen, RouteGost> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost.fachwahlen", "fachwahlen", SGostFachwahlen, new RouteDataGostFachwahlen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fachwahlen";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
			routeGostFachwahlenAllgemein,
			routeGostFachwahlenAbitur,
			routeGostFachwahlenAbiturFach,
			routeGostFachwahlenFach,
			routeGostFachwahlenFachHalbjahr,
			routeGostFachwahlenHalbjahr,
			routeGostFachwahlenLeistungskurse,
			routeGostFachwahlenZusatzkurse,
		];
		super.defaultChild = routeGostFachwahlenAllgemein;
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
		const abiturjahr = to_params.abiturjahr === undefined ? undefined : parseInt(to_params.abiturjahr);
		if (abiturjahr === undefined || abiturjahr === -1)
			return { name: routeGost.defaultChild!.name, params: { abiturjahr: routeGost.data.mapAbiturjahrgaenge.values().next().value.abiturjahr }};
		await this.data.setEintrag(abiturjahr);
		if (to.name === this.name)
			return this.defaultChild!.getRoute(abiturjahr);
	}

	public async leave(from: RouteNode<unknown, any>, from_params: RouteParams): Promise<void> {
		await this.data.setEintrag(-1);
	}

	gotoLaufbahnplanung = async (idSchueler: number) => {
		await RouteManager.doRoute(routeSchuelerLaufbahnplanung.getRoute(idSchueler));
	}

	public getRoute(abiturjahr: number) : RouteLocationRaw {
		return { name: this.name, params: { abiturjahr }};
	}

	public getProps(to: RouteLocationNormalized): GostFachwahlenProps {
		return {
			fachwahlstatistik: this.data.fachwahlstatistik,
			doSelect: this.data.doSelect,
			selected: () => this.data.auswahl,
		};
	}

}

export const routeGostFachwahlen = new RouteGostFachwahlen();
