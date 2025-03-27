import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../RouteApp";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteKatalogReligionen } from "~/router/apps/schule/religionen/RouteKatalogReligionen";
import type { KatalogReligionNeuProps } from "~/components/schule/kataloge/religionen/SKatalogReligionNeuProps";
import { routeKatalogReligionen } from "~/router/apps/schule/religionen/RouteKatalogReligionen";

const SKatalogReligionNeu = () => import("~/components/schule/kataloge/religionen/SKatalogReligionNeu.vue");

export class RouteKatalogReligionNeu extends RouteNode<any, RouteKatalogReligionen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.religionen.neu", "neu", SKatalogReligionNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KatalogReligionNeuProps {
		return {
			religionListeManager: () => routeKatalogReligionen.data.manager,
			add: routeKatalogReligionen.data.add,
			gotoDefaultView: routeKatalogReligionen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogReligionNeu = new RouteKatalogReligionNeu();
