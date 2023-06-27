import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeSchuleBenutzergruppeDaten } from "~/router/apps/benutzergruppe/RouteSchuleBenutzergruppeDaten";
import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import { RouteNode } from "~/router/RouteNode";
import type { RouteApp } from "~/router/RouteApp";
import { RouteDataSchuleBenutzergruppe } from "../benutzergruppe/RouteDataSchuleBenutzergruppe";
import type { BenutzergruppeAuswahlProps } from "~/components/schule/benutzergruppen/SBenutzergruppeAuswahlProps";
import { RouteManager } from "~/router/RouteManager";
import type { BenutzergruppeAppProps } from "~/components/schule/benutzergruppen/SBenutzergruppeAppProps";
import { routeSchule } from "../RouteSchule";
import type { AuswahlChildData } from "~/components/AuswahlChildData";

const SBenutzergruppeAuswahl = () => import("~/components/schule/benutzergruppen/SBenutzergruppeAuswahl.vue")
const SBenutzergruppeApp = () => import("~/components/schule/benutzergruppen/SBenutzergruppeApp.vue")

export class RouteSchuleBenutzergruppe extends RouteNode<RouteDataSchuleBenutzergruppe, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "benutzergruppen", "/schule/benutzergruppe/:id(\\d+)?", SBenutzergruppeApp, new RouteDataSchuleBenutzergruppe());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzergruppen";
		super.setView("liste", SBenutzergruppeAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleBenutzergruppeDaten
		];
		super.defaultChild = routeSchuleBenutzergruppeDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined)
			return routeSchuleBenutzergruppeDaten.getRoute(id);
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzergruppe.size === 0)
				return;
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		}
		// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if(id !== undefined && !this.data.mapBenutzergruppe.has(id))
			return this.getRoute(this.data.mapBenutzergruppe.values().next().value.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzergruppe.get(id) : undefined;
		await this.data.setBenutzergruppe(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzergruppeAuswahlProps {
		return {
			auswahl: () => this.data.auswahl,
			mapBenutzergruppe: this.data.mapBenutzergruppe,
			gotoBenutzergruppe: this.data.gotoBenutzergruppe,
			createBenutzergruppe : this.data.create,
			deleteBenutzergruppen : this.data.deleteBenutzergruppen,
			gotoSchule: routeSchule.gotoSchule
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzergruppeAppProps {
		return {
			auswahl: () => this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
			}
		});
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
		if (value.name === this.data.view.name) return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) throw new Error("Unbekannte Route");
		await RouteManager.doRoute({
			name: value.name,
			params: { id: this.data.auswahl?.id },
		});
		await this.data.setView(node);
	};

}

export const routeSchuleBenutzergruppe = new RouteSchuleBenutzergruppe();
