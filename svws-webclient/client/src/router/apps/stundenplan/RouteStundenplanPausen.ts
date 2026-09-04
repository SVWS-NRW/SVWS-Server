import type { RouteLocationNormalized, RouteParams } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteStundenplan, routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import type { StundenplanPausenProps } from "~/components/stundenplan/pausen/StundenplanPausenProps";
import { api } from "~/router/Api";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";

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
			stundenplanManager: () => routeStundenplan.data.manager.daten(),
			patchPausenzeit: routeStundenplan.data.patchPausenzeit,
			removePausenzeiten: routeStundenplan.data.removePausenzeiten,
			listPausenzeiten: () => routeStundenplan.data.listPausenzeiten,
			patchAufsichtsbereich: routeStundenplan.data.patchAufsichtsbereich,
			addAufsichtsbereich: routeStundenplan.data.addAufsichtsbereich,
			removeAufsichtsbereiche: routeStundenplan.data.removeAufsichtsbereiche,
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

