import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteDataLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteDataLernplattformen";
import { routeLernplattformenDaten } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenDaten";
import { routeLernplattformenNeu } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenNeu";
import { routeLernplattformenGruppenprozesse } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformenGruppenprozesse";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { LernplattformListeManager } from "@ui/ui/manager/kataloge/LernplattformListeManager";

const LernplattformenAuswahl = () =>
	import("~/components/schule/kataloge/lernplattformen/LernplattformenAuswahl.vue");
const LernplattformenApp = () =>
	import("~/components/schule/kataloge/lernplattformen/LernplattformenApp.vue");

export class RouteLernplattformen extends RouteAuswahlNode<LernplattformListeManager, RouteDataLernplattformen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN],
			"schule.lernplattformen", String.raw`schule/lernplattformen/:id(\d+)?`, LernplattformenApp, LernplattformenAuswahl, new RouteDataLernplattformen());
		super.mode = ServerMode.STABLE;
		super.text = "Lernplattformen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeLernplattformenDaten,
			routeLernplattformenNeu,
			routeLernplattformenGruppenprozesse,
		];
		super.defaultChild = routeLernplattformenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeLernplattformen = new RouteLernplattformen();
