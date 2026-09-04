import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeVermerkartenDaten } from "./RouteVermerkartenDaten";
import { routeVermerkartenNeu } from "./RouteVermerkartenNeu";
import { routeVermerkartenGruppenprozesse } from "./RouteVermerkartenGruppenprozesse";
import { RouteDataVermerkarten } from "./RouteDataVermerkarten";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { VermerkartenListeManager } from "@ui/ui/manager/kataloge/VermerkartenListeManager";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";

const VermerkartenAuswahl = () => import("~/components/schule/kataloge/vermerkarten/VermerkartenAuswahl.vue");
const VermerkartenApp = () => import("~/components/schule/kataloge/vermerkarten/VermerkartenApp.vue");

export class RouteVermerkarten extends RouteAuswahlNode<VermerkartenListeManager, RouteDataVermerkarten, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.vermerkarten",
			String.raw`schule/vermerkarten/:id(\d+)?`, VermerkartenApp, VermerkartenAuswahl, new RouteDataVermerkarten());
		super.mode = ServerMode.STABLE;
		super.text = "Vermerkarten";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeVermerkartenDaten,
			routeVermerkartenNeu,
			routeVermerkartenGruppenprozesse,
		];
		super.defaultChild = routeVermerkartenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() === false) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeVermerkarten = new RouteVermerkarten();
