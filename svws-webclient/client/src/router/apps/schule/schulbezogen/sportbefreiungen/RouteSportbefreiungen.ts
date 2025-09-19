import type { RouteParams } from "vue-router";
import type { SportbefreiungenListeManager } from "@ui";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup} from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataSportbefreiungen} from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteDataSportbefreiungen";
import { routeSportbefreiungenNeu } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenNeu";
import { routeSportbefreiungenDaten } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenDaten";
import { routeSportbefreiungenGruppenprozesse } from "~/router/apps/schule/schulbezogen/sportbefreiungen/RouteSportbefreiungenGruppenprozesse";

const SSportbefreiungenApp = () => import("~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenApp.vue");
const SSportbefreiungenAuswahl = () => import("~/components/schule/schulbezogen/sportbefreiungen/SSportbefreiungenAuswahl.vue");

export class RouteSportbefreiungen extends RouteAuswahlNode<SportbefreiungenListeManager, RouteDataSportbefreiungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.sportbefreiungen",
			"schule/sportbefreiungen/:id(\\d+)?", SSportbefreiungenApp, SSportbefreiungenAuswahl, new RouteDataSportbefreiungen());
		super.mode = ServerMode.DEV;
		super.text = "Sportbefreiungen";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeSportbefreiungenDaten,
			routeSportbefreiungenNeu,
			routeSportbefreiungenGruppenprozesse,
		];
		super.defaultChild = routeSportbefreiungenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeSportbefreiungen = new RouteSportbefreiungen();
