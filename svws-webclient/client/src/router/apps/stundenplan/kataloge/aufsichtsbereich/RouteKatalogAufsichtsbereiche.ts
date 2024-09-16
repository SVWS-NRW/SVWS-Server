import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { StundenplanAufsichtsbereich } from "@core";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import type { RouteApp } from "~/router/apps/RouteApp";
import { routeApp } from "~/router/apps/RouteApp";
import { routeKatalogAufsichtsbereichDaten } from "~/router/apps/stundenplan/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereichDaten";

import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { AufsichtsbereicheAppProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAppProps";
import type { AufsichtsbereicheAuswahlProps } from "~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahlProps";
import { RouteDataKatalogAufsichtsbereiche } from "./RouteDataKatalogAufsichtsbereiche";
import { routeStundenplan } from "../../RouteStundenplan";
import { routeStundenplanKataloge } from "../RouteStundenplanKataloge";


const SAufsichtsbereicheAuswahl = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheAuswahl.vue")
const SAufsichtsbereicheApp = () => import("~/components/stundenplan/kataloge/aufsichtsbereiche/SAufsichtsbereicheApp.vue")

export class RouteKatalogAufsichtsbereiche extends RouteNode<RouteDataKatalogAufsichtsbereiche, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.aufsichtsbereiche", "stundenplan/kataloge/aufsichtsbereiche/:id(\\d+)?", SAufsichtsbereicheApp, new RouteDataKatalogAufsichtsbereiche());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Aufsichtsbereiche";
		super.setView("liste", SAufsichtsbereicheAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogAufsichtsbereichDaten
		];
		super.defaultChild = routeKatalogAufsichtsbereichDaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
		if (to_params.id instanceof Array)
			throw new DeveloperNotificationException("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().isEmpty())
			return;
		let eintrag: StundenplanAufsichtsbereich | undefined;
		if (!to_params.id && this.data.auswahl)
			return this.getRoute(this.data.auswahl.id);
		if (!to_params.id) {
			eintrag = this.data.stundenplanManager.aufsichtsbereichGetMengeAsList().get(0);
			return this.getRoute(eintrag.id);
		}
		else {
			const id = parseInt(to_params.id);
			eintrag = this.data.stundenplanManager.aufsichtsbereichGetByIdOrException(id);
		}
		await this.data.setEintrag(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		const name = (this.data.auswahl === undefined && id === undefined) ? this.name : this.defaultChild!.name;
		return { name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): AufsichtsbereicheAuswahlProps {
		return {
			auswahl: this.data.auswahl,
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			gotoEintrag: this.data.gotoEintrag,
			addEintrag: this.data.addEintrag,
			deleteEintraege: this.data.deleteEintraege,
			returnToKataloge: routeStundenplanKataloge.returnToKataloge,
			returnToStundenplan: routeStundenplan.returnToStundenplan,
			stundenplanManager: () => this.data.stundenplanManager,
		};
	}

	public getProps(to: RouteLocationNormalized): AufsichtsbereicheAppProps {
		return {
			auswahl: this.data.auswahl,
			// Props für die Navigation
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
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: this.data.auswahl?.id } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogAufsichtsbereiche = new RouteKatalogAufsichtsbereiche();
