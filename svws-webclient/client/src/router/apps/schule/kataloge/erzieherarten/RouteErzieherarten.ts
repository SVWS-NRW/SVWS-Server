import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteDataErzieherarten";
import { routeErzieherartenDaten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenDaten";
import { routeErzieherartenNeu } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenNeu";
import { routeErzieherartenGruppenprozesse } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherartenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { ErzieherartListeManager } from "@ui/ui/manager/kataloge/ErzieherartListeManager";

const ErzieherartenAuswahl = () => import("~/components/schule/kataloge/erzieherarten/ErzieherartenAuswahl.vue");
const ErzieherartenApp = () => import("~/components/schule/kataloge/erzieherarten/ErzieherartenApp.vue");

export class RouteErzieherarten extends RouteAuswahlNode<ErzieherartListeManager, RouteDataErzieherarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.erzieherarten", String.raw`schule/erzieherarten/:id(\d+)?`, ErzieherartenApp, ErzieherartenAuswahl, new RouteDataErzieherarten());
		super.mode = ServerMode.STABLE;
		super.text = "Erzieherarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeErzieherartenDaten,
			routeErzieherartenNeu,
			routeErzieherartenGruppenprozesse,
		];
		super.defaultChild = routeErzieherartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeErzieherarten = new RouteErzieherarten();
