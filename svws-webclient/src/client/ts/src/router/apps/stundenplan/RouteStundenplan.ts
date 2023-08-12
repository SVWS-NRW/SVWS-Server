import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { StundenplanAuswahlProps } from "~/components/stundenplan/SStundenplanAuswahlProps";
import type { StundenplanAppProps } from "~/components/stundenplan/SStundenplanAppProps";

import { BenutzerKompetenz, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";

import { routeStundenplanDaten } from "~/router/apps/stundenplan/RouteStundenplanDaten";
import { routeStundenplanUnterricht } from "~/router/apps/stundenplan/RouteStundenplanUnterricht";
import { routeStundenplanZeitrasterPausenzeit } from "./RouteStundenplanZeitrasterPausenzeit";

import { RouteDataStundenplan } from "~/router/apps/stundenplan/RouteDataStundenplan";


const SStundenplanAuswahl = () => import("~/components/stundenplan/SStundenplanAuswahl.vue")
const SStundenplanApp = () => import("~/components/stundenplan/SStundenplanApp.vue")

export class RouteStundenplan extends RouteNode<RouteDataStundenplan, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan", "/stundenplan/:id(\\d+)?", SStundenplanApp, new RouteDataStundenplan());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.setView("liste", SStundenplanAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeStundenplanDaten,
			routeStundenplanZeitrasterPausenzeit,
			routeStundenplanUnterricht,
		];
		super.defaultChild = routeStundenplanDaten;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.ladeListe();
		} else {
			const id = parseInt(to_params.id);
			const eintrag = this.data.mapKatalogeintraege.get(id);
			if (eintrag === undefined && this.data.auswahl !== undefined) {
				await this.data.ladeListe();
				return this.getRoute(this.data.auswahl.id);
			}
			else if (eintrag)
				await this.data.setEintrag(eintrag);
		}
		if (to.name === this.name && this.data.auswahl !== undefined)
			return this.getRoute(this.data.auswahl.id);
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id }};
	}

	public getChildRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name = (routeStundenplan.selectedChild === undefined) ? routeStundenplanDaten.name : routeStundenplan.selectedChild.name;
		return { name: redirect_name, params: { id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): StundenplanAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			mapKatalogeintraege: () =>this.data.mapKatalogeintraege,
			gotoEintrag: this.data.gotoEintrag,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			addEintrag: this.data.addEintrag,
			removeEintraege: this.data.removeEintraege,
		};
	}

	public getProps(to: RouteLocationNormalized): StundenplanAppProps {
		return {
			auswahl: this.data.auswahl,
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node);
	}
}

export const routeStundenplan = new RouteStundenplan();
