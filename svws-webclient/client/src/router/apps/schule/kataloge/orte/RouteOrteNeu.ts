import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import type { OrteNeuProps } from "~/components/schule/kataloge/orte/OrteNeuProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const OrteNeu = () => import("~/components/schule/kataloge/orte/OrteNeu.vue");

export class RouteOrteNeu extends RouteNode<any, RouteOrte> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.orte.neu", "neu", OrteNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ort Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): OrteNeuProps {
		return {
			manager: () => routeOrte.data.manager,
			add: routeOrte.data.add,
			goToDefaultView: routeOrte.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeOrteNeu = new RouteOrteNeu();
