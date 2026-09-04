import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteDataFloskelgruppen";
import { routeFloskelgruppenDaten } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenDaten";
import { routeFloskelgruppenNeu } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenNeu";
import { routeFloskelgruppenGruppenprozesse } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { FloskelgruppenListeManager } from "@ui/ui/manager/kataloge/FloskelgruppenListeManager";

const FloskelgruppenApp = () => import("~/components/schule/kataloge/floskelgruppen/FloskelgruppenApp.vue");
const FloskelgruppenAuswahl = () => import("~/components/schule/kataloge/floskelgruppen/FloskelgruppenAuswahl.vue");

export class RouteFloskelgruppen extends RouteAuswahlNode<FloskelgruppenListeManager, RouteDataFloskelgruppen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.floskelgruppen",
			String.raw`schule/floskelgruppen/:id(\d+)?`, FloskelgruppenApp, FloskelgruppenAuswahl, new RouteDataFloskelgruppen());
		super.mode = ServerMode.STABLE;
		super.text = "Floskelgruppen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeFloskelgruppenDaten,
			routeFloskelgruppenNeu,
			routeFloskelgruppenGruppenprozesse,
		];
		super.defaultChild = routeFloskelgruppenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeFloskelgruppen = new RouteFloskelgruppen();
