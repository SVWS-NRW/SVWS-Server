import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import type { LehrerNeuProps } from "~/components/lehrer/LehrerNeuProps";
import type { RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const LehrerNeu = () => import("~/components/lehrer/LehrerNeu.vue");

export class RouteLehrerNeu extends RouteNode<any, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_AENDERN], "lehrer.neu", "neu", LehrerNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrer Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): LehrerNeuProps {
		return {
			lehrerListeManager: () => routeLehrer.data.manager,
			add: routeLehrer.data.add,
			gotoDefaultView: routeLehrer.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeLehrerNeu = new RouteLehrerNeu();
