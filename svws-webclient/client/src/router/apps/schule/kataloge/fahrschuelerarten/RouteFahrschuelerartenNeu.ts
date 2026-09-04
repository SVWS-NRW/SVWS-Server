import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenNeuProps } from "~/components/schule/kataloge/fahrschuelerarten/FahrschuelerartenNeuProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

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
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFahrschuelerartenNeu = new RouteFahrschuelerartenNeu();
