import type { RouteParams } from "vue-router";
import type { FahrschuelerartenListeManager } from "@ui";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataFahrschuelerarten } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteDataFahrschuelerarten";
import { routeFahrschuelerartenDaten } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenDaten";
import { routeFahrschuelerartenNeu } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenNeu";
import { routeFahrschuelerartenGruppenprozesse } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerartenGruppenprozesse";

const SFahrschuelerartenApp = () => import("~/components/schule/allgemein/fahrschuelerarten/SFahrschuelerartenApp.vue");
const SFahrschuelerartenAuswahl = () => import("~/components/schule/allgemein/fahrschuelerarten/SFahrschuelerartenAuswahl.vue");

export class RouteFahrschuelerarten extends RouteAuswahlNode<FahrschuelerartenListeManager, RouteDataFahrschuelerarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten",
			"schule/fahrschuelerarten/:id(\\d+)?", SFahrschuelerartenApp, SFahrschuelerartenAuswahl, new RouteDataFahrschuelerarten());
		super.mode = ServerMode.DEV;
		super.text = "Fahrschülerarten";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeFahrschuelerartenDaten,
			routeFahrschuelerartenNeu,
			routeFahrschuelerartenGruppenprozesse,
		];
		super.defaultChild = routeFahrschuelerartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeFahrschuelerarten = new RouteFahrschuelerarten();
