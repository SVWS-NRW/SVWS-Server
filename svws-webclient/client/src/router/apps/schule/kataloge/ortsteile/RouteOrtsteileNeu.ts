import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import type { RouteOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";
import type { OrtsteileNeuProps } from "~/components/schule/kataloge/ortsteile/OrtsteileNeuProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ViewType } from "@ui/ui/nav/ViewType";

const OrtsteileNeu = () => import("~/components/schule/kataloge/ortsteile/OrtsteileNeu.vue");

export class RouteOrtsteileNeu extends RouteNode<any, RouteOrtsteile> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.ortsteile.neu", "neu", OrtsteileNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Ortsteil Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): OrtsteileNeuProps {
		return {
			manager: () => routeOrtsteile.data.manager,
			add: routeOrtsteile.data.add,
			goToDefaultView: routeOrtsteile.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeOrtsteileNeu = new RouteOrtsteileNeu();
