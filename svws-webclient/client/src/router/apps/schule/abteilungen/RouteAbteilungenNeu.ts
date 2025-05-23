import type { RouteLocationNormalized } from "vue-router";
import type { AbteilungenNeuProps } from "~/components/schule/kataloge/abteilungen/SAbteilungenNeuProps";
import type { RouteAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { routeAbteilungen } from "~/router/apps/schule/abteilungen/RouteAbteilungen";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";

const SAbteilungenNeu = () => import("~/components/schule/kataloge/abteilungen/SAbteilungenNeu.vue");

export class RouteAbteilungenNeu extends RouteNode<any, RouteAbteilungen> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.LEHRERDATEN_AENDERN ], "schule.abteilungen.neu", "neu", SAbteilungenNeu);
		super.types = new Set([ ViewType.HINZUFUEGEN ]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "abteilungen";
		super.setCheckpoint = true;
	}

	goToLehrer = async (idLehrer : number) => {
		await RouteManager.doRoute(routeLehrer.getRoute({id : idLehrer}));
	}

	public getProps(to: RouteLocationNormalized): AbteilungenNeuProps {
		return {
			manager: () => routeAbteilungen.data.manager,
			addAbteilung: routeAbteilungen.data.addAbteilung,
			addKlassenzuordnungen: routeAbteilungen.data.addKlassenzuordnungen,
			goToLehrer: this.goToLehrer,
			goToDefaultView: routeAbteilungen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeAbteilungenNeu = new RouteAbteilungenNeu();
