import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteStundenplan} from "../RouteStundenplan";
import type { StundenplanDatenProps } from "~/components/stundenplan/daten/SStundenplanDatenProps";
import { routeStundenplan} from "../RouteStundenplan";
import { BenutzerKompetenz, Schulform } from "@core";
import { RouteNode } from "~/router/RouteNode";

const SStundenplanDaten = () => import("~/components/stundenplan/daten/SStundenplanDaten.vue");

export class RouteStundenplanDaten extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.daten", "daten", SStundenplanDaten);
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanDatenProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patch: routeStundenplan.data.patch,
			patchRaum: routeStundenplan.data.patchRaum,
			addRaum: routeStundenplan.data.addRaum,
			removeRaeume: routeStundenplan.data.removeRaeume,
			importRaeume: routeStundenplan.data.importRaeume,
			listRaeume: routeStundenplan.data.listRaeume,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			addPausenzeit: routeStundenplan.data.addPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			listPausenzeiten: routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
			importAufsichtsbereiche: routeStundenplan.data.importAufsichtsbereiche,
			listAufsichtsbereiche: routeStundenplan.data.listAufsichtsbereiche,
			patchZeitraster: routeStundenplan.data.patchZeitraster,
			addZeitraster: routeStundenplan.data.addZeitraster,
			removeZeitraster: routeStundenplan.data.removeZeitraster,
			importZeitraster: routeStundenplan.data.importZeitraster,
		};
	}

}

export const routeStundenplanDaten = new RouteStundenplanDaten();

