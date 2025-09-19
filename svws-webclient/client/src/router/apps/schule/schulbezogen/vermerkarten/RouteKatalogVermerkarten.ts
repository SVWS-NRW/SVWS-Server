import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { VermerkartenListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeKatalogVermerkartenDaten } from "./RouteKatalogVermerkartenDaten";
import { routeKatalogVermerkartenNeu } from "./RouteKatalogVermerkartenNeu";
import { routeKatalogVermerkartenGruppenprozesse } from "./RouteKatalogVermerkartenGruppenprozesse";
import { RouteDataKatalogVermerkarten } from "./RouteDataKatalogVermerkarten";

const SVermerkartenAuswahl = () => import("~/components/schule/schulbezogen/vermerkarten/SVermerkartenAuswahl.vue");
const SVermerkartenApp = () => import("~/components/schule/schulbezogen/vermerkarten/SVermerkartenApp.vue");

export class RouteKatalogVermerkarten extends RouteAuswahlNode<VermerkartenListeManager, RouteDataKatalogVermerkarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.vermerkarten", "schule/vermerkarten/:id(\\d+)?", SVermerkartenApp, SVermerkartenAuswahl, new RouteDataKatalogVermerkarten());
		super.mode = ServerMode.DEV;
		super.text = "Vermerkarten";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.children = [
			routeKatalogVermerkartenDaten,
			routeKatalogVermerkartenNeu,
			routeKatalogVermerkartenGruppenprozesse,
		];
		super.defaultChild = routeKatalogVermerkartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false)
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogVermerkarten = new RouteKatalogVermerkarten();
