import type { RouteParams } from "vue-router";
import type { TelefonArtListeManager } from "@ui";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKatalogTelefonArtenDaten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenDaten";
import { routeKatalogTelefonArtenGruppenprozesse } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenGruppenprozesse";
import { routeKatalogTelefonArtenNeu } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArtenNeu";
import { RouteDataKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteDataKatalogTelefonArten";

const STelefonArtenAuswahl = () => import("~/components/schule/allgemein/telefonarten/STelefonArtenAuswahl.vue");
const STelefonArtenApp = () => import("~/components/schule/allgemein/telefonarten/STelefonArtenApp.vue");

export class RouteKatalogTelefonArten extends RouteAuswahlNode<TelefonArtListeManager, RouteDataKatalogTelefonArten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.telefonarten", "schule/telefonarten/:id(\\d+)?", STelefonArtenApp, STelefonArtenAuswahl, new RouteDataKatalogTelefonArten());
		super.mode = ServerMode.DEV;
		super.text = "Telefonarten";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeKatalogTelefonArtenDaten,
			routeKatalogTelefonArtenNeu,
			routeKatalogTelefonArtenGruppenprozesse,
		];
		super.defaultChild = routeKatalogTelefonArtenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};
}

export const routeKatalogTelefonArten = new RouteKatalogTelefonArten();
