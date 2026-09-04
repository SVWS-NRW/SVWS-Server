import type { RouteParams } from "vue-router";
import type { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeKonfessionenDaten } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenDaten";
import { RouteDataKonfessionen } from "./RouteDataKonfessionen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKonfessionenGruppenprozesse } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenGruppenprozesse";
import { routeKonfessionenNeu } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionenNeu";
import type { KonfessionenAuswahlProps } from "~/components/schule/kataloge/konfessionen/KonfessionenAuswahlPops";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import type { KonfessionenListeManager } from "@ui/ui/manager/kataloge/KonfessionenListeManager";

const KonfessionenAuswahl = () => import("~/components/schule/kataloge/konfessionen/KonfessionenAuswahl.vue");
const KonfessionenApp = () => import("~/components/schule/kataloge/konfessionen/KonfessionenApp.vue");

export class RouteKonfessionen extends RouteAuswahlNode<KonfessionenListeManager, RouteDataKonfessionen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN], "schule.konfessionen",
			String.raw`schule/konfessionen/:id(\d+)?`, KonfessionenApp, KonfessionenAuswahl, new RouteDataKonfessionen());
		super.mode = ServerMode.STABLE;
		super.text = "Konfessionen";
		super.menugroup = RouteSchuleMenuGroup.KATALOGE;
		super.children = [
			routeKonfessionenDaten,
			routeKonfessionenGruppenprozesse,
			routeKonfessionenNeu,
		];
		super.defaultChild = routeKonfessionenDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<KonfessionenAuswahlProps> {
			...props,
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten()) {
			return;
		}
		return this.getRouteSelectedChild();
	};
}

export const routeKonfessionen = new RouteKonfessionen();
