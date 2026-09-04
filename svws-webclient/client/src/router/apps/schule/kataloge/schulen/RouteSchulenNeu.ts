import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { SchulenNeuProps } from "~/components/schule/kataloge/schulen/SchulenNeuProps";
import type { RouteSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const SchulenNeu = () => import("~/components/schule/kataloge/schulen/SchulenNeu.vue");

export class RouteSchulenNeu extends RouteNode<any, RouteSchulen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.schulen.neu", "neu", SchulenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): SchulenNeuProps {
		return {
			manager: () => routeSchulen.data.manager,
			add: routeSchulen.data.add,
			gotoDefaultView: routeSchulen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeSchulenNeu = new RouteSchulenNeu();
