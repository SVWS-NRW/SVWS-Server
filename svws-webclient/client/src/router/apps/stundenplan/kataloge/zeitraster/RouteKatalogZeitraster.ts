import type { RouteParams, RouteLocationRaw, RouteLocationNormalized } from "vue-router";
import type { ZeitrasterAppProps } from "~/components/stundenplan/kataloge/zeitraster/SZeitrasterAppProps";
import type { ZeitrasterAuswahlProps } from "~/components/stundenplan/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp} from "../../../RouteApp";
import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeApp } from "../../../RouteApp";
import { routeKatalogZeitrasterDaten } from "./RouteKatalogZeitrasterDaten";
import { RouteDataKatalogZeitraster } from "./RouteDataKatalogZeitraster";
import { routeStundenplan } from "../../RouteStundenplan";
import { routeStundenplanKataloge } from "../RouteStundenplanKataloge";

const SZeitrasterAuswahl = () => import("~/components/stundenplan/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/stundenplan/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "stundenplan.kataloge.zeitraster", "stundenplan/kataloge/zeitraster", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeListe();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			returnToKataloge: routeStundenplanKataloge.returnToKataloge,
			returnToStundenplan: routeStundenplan.returnToStundenplan,
		};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterAppProps {
		return {
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
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
