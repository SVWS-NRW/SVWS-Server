import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteSchuleMenuGroup } from "~/router/apps/schule/RouteSchuleMenuGroup";
import { RouteDataLeitungsfunktionen } from "./RouteDataLeitungsfunktionen";
import { routeLeitungsfunktionenDaten } from "./RouteLeitungsfunktionenDaten";
import { routeLeitungsfunktionenNeu } from "./RouteLeitungsfunktionenNeu";
import { routeLeitungsfunktionenGruppenprozesse } from "./RouteLeitungsfunktionenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { LeitungsfunktionenListeManager } from "@ui/ui/manager/kataloge/LeitungsfunktionenListeManager";

const LeitungsfunktionenApp = () => import("~/components/schule/kataloge/leitungsfunktionen/LeitungsfunktionenApp.vue");
const LeitungsfunktionenAuswahl = () => import("~/components/schule/kataloge/leitungsfunktionen/LeitungsfunktionenAuswahl.vue");

export class RouteLeitungsfunktionen extends RouteAuswahlNode<LeitungsfunktionenListeManager, RouteDataLeitungsfunktionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.leitungsfunktionen",
			String.raw`schule/leitungsfunktionen/:id(\d+)?`, LeitungsfunktionenApp, LeitungsfunktionenAuswahl, new RouteDataLeitungsfunktionen());
		super.text = "Leitungsfunktionen";
		super.mode = ServerMode.STABLE;
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeLeitungsfunktionenDaten,
			routeLeitungsfunktionenNeu,
			routeLeitungsfunktionenGruppenprozesse,
		];
		super.defaultChild = routeLeitungsfunktionenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeLeitungsfunktionen = new RouteLeitungsfunktionen();
