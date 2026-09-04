import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteDataFahrschuelerarten";
import { routeFahrschuelerartenDaten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenDaten";
import { routeFahrschuelerartenNeu } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenNeu";
import { routeFahrschuelerartenGruppenprozesse } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerartenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { FahrschuelerartenListeManager } from "@ui/ui/manager/kataloge/FahrschuelerartenListeManager";

const FahrschuelerartenApp = () => import("~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenApp.vue");
const FahrschuelerartenAuswahl = () => import("~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenAuswahl.vue");

export class RouteFahrschuelerarten extends RouteAuswahlNode<FahrschuelerartenListeManager, RouteDataFahrschuelerarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten",
			String.raw`schule/fahrschuelerarten/:id(\d+)?`, FahrschuelerartenApp, FahrschuelerartenAuswahl, new RouteDataFahrschuelerarten());
		super.mode = ServerMode.STABLE;
		super.text = "Fahrschülerarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeFahrschuelerartenDaten,
			routeFahrschuelerartenNeu,
			routeFahrschuelerartenGruppenprozesse,
		];
		super.defaultChild = routeFahrschuelerartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeFahrschuelerarten = new RouteFahrschuelerarten();
