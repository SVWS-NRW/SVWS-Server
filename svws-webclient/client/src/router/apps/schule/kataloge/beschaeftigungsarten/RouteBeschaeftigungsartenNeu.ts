import type { RouteLocationNormalized } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import type { RouteBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import type { BeschaeftigungsartenNeuProps } from "~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeuProps";

const BeschaeftigungsartenNeu = () => import("~/components/schule/kataloge/beschaeftigungsarten/BeschaeftigungsartenNeu.vue");

export class RouteBeschaeftigungsartenNeu extends RouteNode<any, RouteBeschaeftigungsarten> {

	public constructor() {
		super([Schulform.BK, Schulform.SB, Schulform.WB], [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.beschaeftigungsarten.neu", "neu", BeschaeftigungsartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
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
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeBeschaeftigungsartenNeu = new RouteBeschaeftigungsartenNeu();
