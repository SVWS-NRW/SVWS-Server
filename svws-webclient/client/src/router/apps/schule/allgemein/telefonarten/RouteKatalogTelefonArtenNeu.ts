import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import { routeKatalogTelefonArten } from "~/router/apps/schule/allgemein/telefonarten/RouteKatalogTelefonArten";
import type { STelefonArtenNeuProps } from "~/components/schule/allgemein/telefonarten/STelefonArtenNeuProps";
import { api } from "~/router/Api";

const STelefonArtenNeu = () => import("~/components/schule/allgemein/telefonarten/STelefonArtenNeu.vue");

export class RouteKatalogTelefonArtenNeu extends RouteNode<any, RouteKatalogTelefonArten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.telefonarten.neu", "neu", STelefonArtenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Telefonarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): STelefonArtenNeuProps {
		return {
			manager: () => routeKatalogTelefonArten.data.manager,
			add: routeKatalogTelefonArten.data.add,
			gotoDefaultView: routeKatalogTelefonArten.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogTelefonArtenNeu = new RouteKatalogTelefonArtenNeu();
