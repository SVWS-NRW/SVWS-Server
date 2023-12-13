import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { StundenplanZeitrasterPausenzeitProps } from "~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeitProps";
import type { LehrerListeEintrag} from "@core";
import { ArrayList, BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import type { RouteKatalogZeitraster} from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitraster";
import { routeKatalogZeitraster } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitraster";

const SStundenplanZeitrasterPausenzeit = () => import("~/components/stundenplan/zeitrasterPausenzeit/SStundenplanZeitrasterPausenzeit.vue");

export class RouteKatalogZeitrasterDaten extends RouteNode<unknown, RouteKatalogZeitraster> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster.daten", "daten", SStundenplanZeitrasterPausenzeit);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.name};
	}

	public getProps(to: RouteLocationNormalized): StundenplanZeitrasterPausenzeitProps {
		return {
			stundenplanManager: () => routeKatalogZeitraster.data.stundenplanManager,
			listLehrer: new ArrayList<LehrerListeEintrag>(),
			patchPausenzeit: async ()=>{},//routeKatalogZeitraster.data.patchPausenzeit,
			addPausenzeit: async ()=>{},//routeKatalogZeitraster.data.addPausenzeit,
			removePausenzeiten: async ()=>{},//routeKatalogZeitraster.data.removePausenzeiten,
			importPausenzeiten: async ()=>{},//routeKatalogZeitraster.data.importPausenzeiten,
			patchZeitraster: routeKatalogZeitraster.data.patchZeitraster,
			addZeitraster: routeKatalogZeitraster.data.addZeitraster,
			removeZeitraster: routeKatalogZeitraster.data.removeZeitraster,
			importZeitraster: undefined,//routeKatalogZeitraster.data.importZeitraster,
			addAufsichtUndBereich: async ()=>{},//routeKatalogZeitraster.data.addAufsichtUndBereich,
			selected: () => routeKatalogZeitraster.data.selected,
			setSelection: routeKatalogZeitraster.data.setSelection,
		};
	}

}

export const routeKatalogZeitrasterDaten = new RouteKatalogZeitrasterDaten();

