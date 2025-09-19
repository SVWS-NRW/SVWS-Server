import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { MerkmaleListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataMerkmale} from "~/router/apps/schule/schulbezogen/merkmale/RouteDataMerkmale";
import { routeMerkmaleGruppenprozesse } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleGruppenprozesse";
import { routeMerkmaleDaten } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleDaten";
import { routeMerkmaleNeu } from "~/router/apps/schule/schulbezogen/merkmale/RouteMerkmaleNeu";

const SMerkmaleApp = () => import("~/components/schule/schulbezogen/merkmale/SMerkmaleApp.vue");
const SMerkmaleAuswahl = () => import("~/components/schule/schulbezogen/merkmale/SMerkmaleAuswahl.vue");

export class RouteMerkmale extends RouteAuswahlNode<MerkmaleListeManager, RouteDataMerkmale, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.merkmale",
			"schule/merkmale/:id(\\d+)?", SMerkmaleApp, SMerkmaleAuswahl, new RouteDataMerkmale());
		super.mode = ServerMode.DEV;
		super.text = "Merkmale";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeMerkmaleDaten,
			routeMerkmaleNeu,
			routeMerkmaleGruppenprozesse,
		];
		super.defaultChild = routeMerkmaleDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeMerkmale = new RouteMerkmale();
