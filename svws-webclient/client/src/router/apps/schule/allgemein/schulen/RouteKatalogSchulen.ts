import type { RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { RouteNode } from "~/router/RouteNode";
import type { SchulenAuswahlProps } from "~/components/schule/allgemein/schulen/SSchulenAuswahlProps";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { KatalogSchuleListeManager } from "@ui";
import { routeKatalogSchuleDaten } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchuleDaten";
import { RouteDataKatalogSchulen } from "./RouteDataKatalogSchulen";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { routeKatalogSchuleNeu } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchuleNeu";
import { routeKatalogSchuleGruppenprozesse } from "~/router/apps/schule/allgemein/schulen/RouteKatalogSchuleGruppenprozesse";
import { routeApp } from "~/router/apps/RouteApp";

const SSchulenAuswahl = () => import("~/components/schule/allgemein/schulen/SSchulenAuswahl.vue");
const SSchulenApp = () => import("~/components/schule/allgemein/schulen/SSchulenApp.vue");

export class RouteKatalogSchulen extends RouteAuswahlNode<KatalogSchuleListeManager, RouteDataKatalogSchulen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.schulen", "schule/schulen/:id(\\d+)?", SSchulenApp, SSchulenAuswahl, new RouteDataKatalogSchulen());
		super.mode = ServerMode.DEV;
		super.text = "Schulen";
		super.menugroup = RouteSchuleMenuGroup.ALLGEMEIN;
		super.children = [
			routeKatalogSchuleDaten,
			routeKatalogSchuleNeu,
			routeKatalogSchuleGruppenprozesse,
		];
		super.defaultChild = routeKatalogSchuleDaten;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlListProps = (props) => (<SchulenAuswahlProps> {
			...props,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
		})
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (!this.data.manager.hasDaten())
			return;
		return this.getRouteSelectedChild();
	};

}

export const routeKatalogSchulen = new RouteKatalogSchulen();
