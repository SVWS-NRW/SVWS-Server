import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import type { StundenplanPausenProps } from "~/components/stundenplan/pausen/StundenplanPausenProps";

const StundenplanPausen = () => import("~/components/stundenplan/pausen/StundenplanPausen.vue");

export class RouteStundenplanPausen extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.pausen", "pausen", StundenplanPausen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausen";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name, params: { id }};
	}

	public getProps(to: RouteLocationNormalized): StundenplanPausenProps {
		return {
			stundenplanManager: () => routeStundenplan.data.stundenplanManager,
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			addPausenzeit: routeStundenplan.data.addPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			listPausenzeiten: () => routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
			importAufsichtsbereiche: routeStundenplan.data.importAufsichtsbereiche,
			listAufsichtsbereiche: () => routeStundenplan.data.listAufsichtsbereiche,
			gotoKatalog: routeStundenplan.data.gotoKatalog,
		};
	}

}

export const routeStundenplanPausen = new RouteStundenplanPausen();

