import type { RouteLocationNormalized } from "vue-router";
import type { SchuleVermerkartenNeuProps } from "~/components/schule/schulbezogen/vermerkarten/SVermerkartenNeuProps";
import type { RouteKatalogVermerkarten } from "./RouteKatalogVermerkarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeKatalogVermerkarten } from "./RouteKatalogVermerkarten";
import { api } from "~/router/Api";

const SVermerkartenNeu = () => import("~/components/schule/schulbezogen/vermerkarten/SVermerkartenNeu.vue");

export class RouteKatalogVermerkartNeu extends RouteNode<any, RouteKatalogVermerkarten> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.vermerkarten.neu", "neu", SVermerkartenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SchuleVermerkartenNeuProps {
		return {
			manager: () => routeKatalogVermerkarten.data.manager,
			add: routeKatalogVermerkarten.data.add,
			gotoDefaultView: routeKatalogVermerkarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeKatalogVermerkartenNeu = new RouteKatalogVermerkartNeu();
