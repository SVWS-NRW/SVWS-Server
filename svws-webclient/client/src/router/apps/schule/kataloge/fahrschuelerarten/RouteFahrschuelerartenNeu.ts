import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenNeuProps } from "~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenNeuProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";

const FahrschuelerartenNeu = () => import("~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenNeu.vue");

export class RouteFahrschuelerartenNeu extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten.neu", "neu", FahrschuelerartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fahrschülerarten Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FahrschuelerartenNeuProps {
		return {
			manager: () => routeFahrschuelerarten.data.manager,
			add: routeFahrschuelerarten.data.add,
			goToDefaultView: routeFahrschuelerarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFahrschuelerartenNeu = new RouteFahrschuelerartenNeu();
