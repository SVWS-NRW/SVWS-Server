import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeJahrgaenge } from "./RouteJahrgaenge";
import type { RouteJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import type { JahrgaengeNeuProps } from "~/components/schule/kataloge/jahrgaenge/JahrgaengeNeuProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const JahrgaengeNeu = () => import("~/components/schule/kataloge/jahrgaenge/JahrgaengeNeu.vue");

export class RouteJahrgaengeNeu extends RouteNode<any, RouteJahrgaenge> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.jahrgaenge.neu", "neu", JahrgaengeNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Jahrgang Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): JahrgaengeNeuProps {
		return {
			manager: () => routeJahrgaenge.data.manager,
			add: routeJahrgaenge.data.add,
			goToDefaultView: routeJahrgaenge.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeJahrgaengeNeu = new RouteJahrgaengeNeu();
