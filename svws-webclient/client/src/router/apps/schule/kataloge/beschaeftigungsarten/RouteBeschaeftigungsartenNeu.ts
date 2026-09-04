import type { RouteLocationNormalized } from "vue-router";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import type { BeschaeftigungsartenNeuProps } from "~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeuProps";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const BeschaeftigungsartenNeu = () => import("~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeu.vue");

export class RouteBeschaeftigungsartenNeu extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten.neu", "neu", BeschaeftigungsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Beschäftigungsarten";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): BeschaeftigungsartenNeuProps {
		return {
			manager: () => routeBeschaeftigungsarten.data.manager,
			add: routeBeschaeftigungsarten.data.add,
			goToDefaultView: routeBeschaeftigungsarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeBeschaeftigungsartenNeu = new RouteBeschaeftigungsartenNeu();
