import type { RouteLocationNormalized } from "vue-router";
import type { VermerkartenNeuProps } from "~/components/schule/kataloge/vermerkarten/VermerkartenNeuProps";
import type { RouteVermerkarten } from "./RouteVermerkarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { RouteManager } from "~/router/RouteManager";
import { routeVermerkarten } from "./RouteVermerkarten";
import { api } from "~/router/Api";

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
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}
}

export const routeVermerkartenNeu = new RouteVermerkartenNeu();
