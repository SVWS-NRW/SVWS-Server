import type { RouteParams, RouteLocationRaw, RouteLocationNormalized } from "vue-router";
import type { ZeitrasterAppProps } from "~/components/kataloge/zeitraster/SZeitrasterAppProps";
import type { ZeitrasterAuswahlProps } from "~/components/kataloge/zeitraster/SZeitrasterAuswahlProps";
import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp} from "../../RouteApp";
import { routeApp } from "../../RouteApp";
import { routeKataloge } from "../RouteKataloge";
import { RouteDataKatalogZeitraster } from "./RouteDataKatalogZeitraster";
import { routeKatalogZeitrasterDaten } from "./RouteKatalogZeitrasterDaten";
import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

const SZeitrasterAuswahl = () => import("~/components/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster", "/kataloge/zeitraster", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			returnToKataloge: routeKataloge.returnToKataloge
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
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name });
		this.data.setView(node, this.children);
	}
}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
