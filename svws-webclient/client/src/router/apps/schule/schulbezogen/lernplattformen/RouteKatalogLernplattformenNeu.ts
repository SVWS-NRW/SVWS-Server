import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { RouteKatalogLernplattformen} from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformen";
import { routeKatalogLernplattformen } from "~/router/apps/schule/schulbezogen/lernplattformen/RouteKatalogLernplattformen";
import type { SLernplattformenNeuProps } from "~/components/schule/schulbezogen/lernplattformen/SLernplattformenNeuProps";
import { api } from "~/router/Api";

const SLernplattformenNeu = () => import("~/components/schule/schulbezogen/lernplattformen/SLernplattformenNeu.vue");

export class RouteKatalogLernplattformenNeu extends RouteNode<any, RouteKatalogLernplattformen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.lernplattformen.neu", "neu", SLernplattformenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lernplattformen Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): SLernplattformenNeuProps {
		return {
			manager: () => routeKatalogLernplattformen.data.manager,
			add: routeKatalogLernplattformen.data.add,
			gotoDefaultView: routeKatalogLernplattformen.data.gotoDefaultView,
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKatalogLernplattformenNeu = new RouteKatalogLernplattformenNeu();
