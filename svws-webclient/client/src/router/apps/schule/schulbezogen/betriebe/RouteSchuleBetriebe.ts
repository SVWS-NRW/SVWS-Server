import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import type { BetriebListeEintrag} from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";

import type { TabData } from "@ui";
import type { BetriebeAppProps } from "~/components/schule/schulbezogen/betriebe/SBetriebeAppProps";
import type { BetriebeAuswahlProps } from "~/components/schule/schulbezogen/betriebe/SBetriebeAuswahlProps";
import { RouteDataSchuleBetriebe } from "./RouteDataSchuleBetriebe";
import { routeSchuleBetriebeDaten } from "./RouteSchuleBetriebeDaten";
import { RouteSchuleMenuGroup } from "../../RouteSchuleMenuGroup";
import { routeError } from "~/router/error/RouteError";
import { api } from "~/router/Api";

const SBetriebeAuswahl = () => import("~/components/schule/schulbezogen/betriebe/SBetriebeAuswahl.vue")
const SBetriebeApp = () => import("~/components/schule/schulbezogen/betriebe/SBetriebeApp.vue")

export class RouteSchuleBetriebe extends RouteNode<RouteDataSchuleBetriebe, RouteApp>{

	public constructor(){
		super(Schulform.values(), [ BenutzerKompetenz.KATALOG_EINTRAEGE_ANSEHEN, BenutzerKompetenz.KATALOG_EINTRAEGE_AENDERN ], "schule.betriebe", "schule/betriebe/:id(\\d+)?", SBetriebeApp, new RouteDataSchuleBetriebe());
		super.mode = ServerMode.ALPHA;
		super.propHandler = (route) => this.getProps(route);
		super.text="Betriebe";
		super.menugroup = RouteSchuleMenuGroup.SCHULBEZOGEN;
		super.setView("liste", SBetriebeAuswahl, (route) => this.getAuswahlProps(route));

		super.children = [
			routeSchuleBetriebeDaten,
		];
		super.defaultChild = routeSchuleBetriebeDaten;

	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { id } = RouteNode.getIntParams(to_params, ["id"]);
			if (isEntering)
				await this.data.ladeListe();
			if (this.data.mapKatalogeintraege.size < 1)
				return;
			let eintrag: BetriebListeEintrag | undefined;
			if ((id === undefined) && this.data.auswahl)
				return this.getRouteDefaultChild();
			if (id === undefined) {
				eintrag = this.data.mapKatalogeintraege.get(0);
				return this.getRouteDefaultChild({ id: eintrag?.id });
			}
			eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined)
				return;
			await this.data.setEintrag(eintrag);
			if (to.name === this.name)
				return this.getRouteDefaultChild();
		} catch(e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
		return { id : this.data.auswahl?.id ?? undefined };
	}

	public getAuswahlProps(to: RouteLocationNormalized): BetriebeAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			auswahl: this.data.auswahl,
			mapKatalogeintraege: this.data.mapKatalogeintraege,
			mapBeschaeftigungsarten: this.data.mapBeschaeftigungsarten,
			mapOrte: routeApp.data.mapOrte,
			mapOrtsteile: routeApp.data.mapOrtsteile,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			gotoEintrag: this.data.gotoEintrag,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

	public getProps(to: RouteLocationNormalized): BetriebeAppProps {
		return {
			auswahl: this.data.auswahl,
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(node.getRoute());
		this.data.setView(node, this.children);
	}


}
export const routeSchuleBetriebe = new RouteSchuleBetriebe();
