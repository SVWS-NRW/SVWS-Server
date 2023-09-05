import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { routeStundenplan, type RouteStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import type { StundenplanDatenProps } from "~/components/stundenplan/daten/SStundenplanDatenProps";

const SStundenplanDaten = () => import("~/components/stundenplan/daten/SStundenplanDaten.vue");

export class RouteStundenplanDaten extends RouteNode<unknown, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.daten", "daten", SStundenplanDaten);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
	}

	public async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
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
			listJahrgaenge: routeStundenplan.data.listJahrgaenge,
			addJahrgang: routeStundenplan.data.addJahrgang,
			removeJahrgang: routeStundenplan.data.removeJahrgang,
		};
	}

}

export const routeStundenplanDaten = new RouteStundenplanDaten();

