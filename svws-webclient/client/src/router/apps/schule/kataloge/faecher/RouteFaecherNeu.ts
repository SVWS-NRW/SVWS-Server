import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import type { FaecherNeuProps } from "~/components/schule/kataloge/faecher/FaecherNeuProps";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeFaecher, type RouteFaecher } from "./RouteFaecher";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const FaecherNeu = () => import("~/components/schule/kataloge/faecher/FaecherNeu.vue");

export class RouteFaecherNeu extends RouteNode<any, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.faecher.neu", "neu", FaecherNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fach Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): FaecherNeuProps {
		return {
			manager: () => routeFaecher.data.manager,
			add: routeFaecher.data.add,
			gotoDefaultView: routeFaecher.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeFaecherNeu = new RouteFaecherNeu();
