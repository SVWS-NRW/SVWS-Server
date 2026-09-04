import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { KonfessionenNeuProps } from "~/components/schule/kataloge/konfessionen/KonfessionenNeuProps";
import type { RouteKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const KonfessionenNeu = () => import("~/components/schule/kataloge/konfessionen/KonfessionenNeu.vue");

export class RouteKonfessionenNeu extends RouteNode<any, RouteKonfessionen> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.konfessionen.neu", "neu", KonfessionenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Konfessionen Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): KonfessionenNeuProps {
		return {
			manager: () => routeKonfessionen.data.manager,
			add: routeKonfessionen.data.add,
			gotoDefaultView: routeKonfessionen.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeKonfessionenNeu = new RouteKonfessionenNeu();
