import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import type { BetriebeNeuProps } from "~/components/schule/kataloge/betriebe/BetriebeNeuProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const BetriebeNeu = () => import("~/components/schule/kataloge/betriebe/BetriebeNeu.vue");

export class RouteBetriebeNeu extends RouteNode<any, RouteBetriebe> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.betriebe.neu", "neu", BetriebeNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Betrieb Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): BetriebeNeuProps {
		return {
			manager: () => routeBetriebe.data.manager,
			add: routeBetriebe.data.add,
			goToDefaultView: routeBetriebe.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeBetriebeNeu = new RouteBetriebeNeu();
