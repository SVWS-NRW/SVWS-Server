import type { RouteLocationNormalized } from "vue-router";
import type { FahrschuelerartenNeuProps } from "~/components/schule/allgemein/fahrschuelerarten/SFahrschuelerartenNeuProps";
import type { RouteFahrschuelerarten } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerarten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { ViewType } from "@ui";
import { api } from "~/router/Api";
import { routeFahrschuelerarten } from "~/router/apps/schule/allgemein/fahrschuelerarten/RouteFahrschuelerarten";

const SFahrschuelerartenNeu = () => import("~/components/schule/allgemein/fahrschuelerarten/SFahrschuelerartenNeu.vue");

export class RouteFahrschuelerartenNeu extends RouteNode<any, RouteFahrschuelerarten> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.fahrschuelerarten.neu", "neu", SFahrschuelerartenNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fahrschülerarten";
		super.setCheckpoint = true;
	}

	public getProps(to: RouteLocationNormalized): FahrschuelerartenNeuProps {
		return {
			manager: () => routeFahrschuelerarten.data.manager,
			addFahrschuelerart: routeFahrschuelerarten.data.addFahrschuelerart,
			goToDefaultView: routeFahrschuelerarten.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			benutzerKompetenzen: api.benutzerKompetenzen,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		}
	}
}

export const routeFahrschuelerartenNeu = new RouteFahrschuelerartenNeu();
