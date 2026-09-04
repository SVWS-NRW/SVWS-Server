import type { RouteLocationNormalized, RouteParamsRawGeneric } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { routeKlassen, type RouteKlassen } from "~/router/apps/klassen/RouteKlassen";
import { RouteManager } from "~/router/RouteManager";
import type { KlassenNeuProps } from "~/components/klassen/KlassenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const KlassenNeu = () => import("~/components/klassen/KlassenNeu.vue");

export class RouteKlassenNeu extends RouteNode<any, RouteKlassen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN], "klassen.neu", "neu", KlassenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Klasse Neu";
		super.setCheckpoint = true;
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return { id: "" };
	}

	public getProps(to: RouteLocationNormalized): KlassenNeuProps {
		return {
			manager: () => routeKlassen.data.manager,
			add: routeKlassen.data.add,
			gotoDefaultView: routeKlassen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}

}

export const routeKlassenNeu = new RouteKlassenNeu();
