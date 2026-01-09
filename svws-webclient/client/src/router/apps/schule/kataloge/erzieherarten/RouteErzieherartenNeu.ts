import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import type { ErzieherartenNeuProps } from "~/components/schule/kataloge/erzieherarten/ErzieherartenNeuProps";
import type { RouteErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { api } from "~/router/Api";

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
			benutzerKompetenzen: api.benutzerKompetenzen,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeErzieherartenNeu = new RouteErzieherartenNeu();
