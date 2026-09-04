import type { RouteLocationNormalized } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { ErzieherartenNeuProps } from "~/components/schule/kataloge/erzieherarten/ErzieherartenNeuProps";
import type { RouteErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const ErzieherartenNeu = () => import("~/components/schule/kataloge/erzieherarten/ErzieherartenNeu.vue");

export class RouteErzieherartenNeu extends RouteNode<any, RouteErzieherarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.erzieherarten.neu", "neu", ErzieherartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Erzieherarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): ErzieherartenNeuProps {
		return {
			manager: () => routeErzieherarten.data.manager,
			add: routeErzieherarten.data.add,
			gotoDefaultView: routeErzieherarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeErzieherartenNeu = new RouteErzieherartenNeu();
