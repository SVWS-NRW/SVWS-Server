import type { RouteLocationNormalized } from "vue-router";
import type { VermerkartenNeuProps } from "~/components/schule/kataloge/vermerkarten/VermerkartenNeuProps";
import type { RouteVermerkarten } from "./RouteVermerkarten";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeVermerkarten } from "./RouteVermerkarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const VermerkartenNeu = () => import("~/components/schule/kataloge/vermerkarten/VermerkartenNeu.vue");

export class RouteVermerkartenNeu extends RouteNode<any, RouteVermerkarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.vermerkarten.neu", "neu", VermerkartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Vermerkart Neu";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): VermerkartenNeuProps {
		return {
			manager: () => routeVermerkarten.data.manager,
			add: routeVermerkarten.data.add,
			goToDefaultView: routeVermerkarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeVermerkartenNeu = new RouteVermerkartenNeu();
