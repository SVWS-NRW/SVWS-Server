import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";

import type { StundenplanPausenProps } from "~/components/stundenplan/pausen/StundenplanPausenProps";
import { api } from "~/router/Api";

const StundenplanPausen = () => import("~/components/stundenplan/pausen/StundenplanPausen.vue");

export class RouteStundenplanPausen extends RouteNode<any, RouteStundenplan> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN,
		], "stundenplan.pausen", "pausen", StundenplanPausen);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Pausen";
		this.isHidden = (params?: RouteParams) => RouteStundenplan.katalogeCheckHidden(false, this, params);
	}

	public getProps(to: RouteLocationNormalized): StundenplanPausenProps {
		return {
			schulform: api.schulform,
			serverMode: api.mode,
			benutzerKompetenzen: api.benutzerKompetenzen,
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			importPausenzeiten: routeStundenplan.data.importPausenzeiten,
			listPausenzeiten: () => routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
			importAufsichtsbereiche: routeStundenplan.data.importAufsichtsbereiche,
			listAufsichtsbereiche: () => routeStundenplan.data.listAufsichtsbereiche,
			wochentyp: () => 0,
			updateAufsichtBereich: routeStundenplan.data.updateAufsichtBereich,
			addAufsicht: routeStundenplan.data.addAufsicht,
			removeAufsicht: routeStundenplan.data.removeAufsicht,
			patchAufsicht: routeStundenplan.data.patchAufsicht,
			apiStatus: api.status,
			gotoKatalog: routeStundenplan.data.gotoKatalog,
		};
	}

}

export const routeStundenplanPausen = new RouteStundenplanPausen();

