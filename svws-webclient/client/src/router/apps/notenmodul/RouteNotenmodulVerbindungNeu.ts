import type { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeNotenmodulAdministration, type RouteNotenmodulAdministration } from "./RouteNotenmodulAdministration";
import type { NotenmodulVerbindungNeuProps } from "~/components/notenmodul/NotenmodulVerbindungNeuProps";
import { abschnittStateImpl } from "~/states/AbschnittStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { ViewType } from "@ui/ui/nav/ViewType";

const NotenmodulVerbindungNeu = () => import("~/components/notenmodul/NotenmodulVerbindungNeu.vue");

export class RouteNotenmodulVerbindungNeu extends RouteNode<any, RouteNotenmodulAdministration> {

	public constructor() {
		super(Schulform.values(), [
			BenutzerKompetenz.NOTENMODUL_ADMINISTRATION,
		], "notenmodul.administration.neu", "neu", NotenmodulVerbindungNeu);
		super.types = new Set([ViewType.HINZUFUEGEN]);
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Verbindung Neu";
		super.setCheckpoint = true;
	}

	public getRoute(): RouteLocationRaw {
		return { name: this.name, params: { idSchuljahresabschnitt: abschnittStateImpl.auswahl.id, id: "" } };
	}

	public getProps(to: RouteLocationNormalized): NotenmodulVerbindungNeuProps {
		return {
			manager: () => routeNotenmodulAdministration.data.manager,
			addCredentials: routeNotenmodulAdministration.data.wenomAddCredentials,
			gotoDefaultView: routeNotenmodulAdministration.data.gotoDefaultView,
			checkpoint: this.checkpoint,
			continueRoutingAfterCheckpoint: () => RouteManager.continueRoutingAfterCheckpoint(),
		};
	}
}

export const routeNotenmodulVerbindungNeu = new RouteNotenmodulVerbindungNeu();
