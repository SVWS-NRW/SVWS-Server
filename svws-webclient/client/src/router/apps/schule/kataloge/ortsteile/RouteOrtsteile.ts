import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeOrtsteileDaten } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileDaten";
import { routeOrtsteileGruppenprozesse } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileGruppenprozesse";
import { routeOrtsteileNeu } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteileNeu";
import { RouteDataOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteDataOrtsteile";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { OrtsteileListeManager } from "@ui/ui/manager/kataloge/OrtsteileListeManager";

const OrtsteileAuswahl = () => import("~/components/schule/kataloge/ortsteile/OrtsteileAuswahl.vue");
const OrtsteileApp = () => import("~/components/schule/kataloge/ortsteile/OrtsteileApp.vue");

export class RouteOrtsteile extends RouteAuswahlNode<OrtsteileListeManager, RouteDataOrtsteile, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.ortsteile", String.raw`schule/ortsteile/:id(\d+)?`, OrtsteileApp, OrtsteileAuswahl, new RouteDataOrtsteile());
		super.mode = ServerMode.STABLE;
		super.text = "Ortsteile";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeOrtsteileDaten,
			routeOrtsteileNeu,
			routeOrtsteileGruppenprozesse,
		];
		super.defaultChild = routeOrtsteileDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeOrtsteile = new RouteOrtsteile();
