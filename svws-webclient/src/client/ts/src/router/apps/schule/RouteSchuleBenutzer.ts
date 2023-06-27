import { BenutzerKompetenz, Schulform, ServerMode } from "@core";
import type { WritableComputedRef } from "vue";
import { computed } from "vue";
import type {
	RouteLocationNormalized,
	RouteLocationRaw,
	RouteParams,
	RouteRecordRaw,
} from "vue-router";
import type { BenutzerAppProps } from "~/components/schule/benutzer/SBenutzerAppProps";
import type { BenutzerAuswahlProps } from "~/components/schule/benutzer/SBenutzerAuswahlProps";
import { routeSchuleBenutzerDaten } from "~/router/apps/benutzer/RouteSchuleBenutzerDaten";
import type { RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { RouteDataSchuleBenutzer } from "~/router/apps/benutzer/RouteDataSchuleBenutzer";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import { routeSchule } from "../RouteSchule";

const SBenutzerAuswahl = () =>
	import("~/components/schule/benutzer/SBenutzerAuswahl.vue");
const SBenutzerApp = () =>
	import("~/components/schule/benutzer/SBenutzerApp.vue");

export class RouteSchuleBenutzer extends RouteNode<RouteDataSchuleBenutzer,RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.ADMIN], "benutzer", "/schule/benutzer/:id(\\d+)?", SBenutzerApp, new RouteDataSchuleBenutzer());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Benutzer";
		super.setView("liste", SBenutzerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [routeSchuleBenutzerDaten];
		super.defaultChild = routeSchuleBenutzerDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id !== undefined) return routeSchuleBenutzer.getRoute(id);
		return true;
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.ladeListe();
		if (to.name === this.name) {
			if (this.data.mapBenutzer.size === 0) return;
			return this.getRoute(this.data.mapBenutzer.values().next().value.id);
		}
		// Weiterleitung an das erste Objekt in der Liste, wenn id nicht vorhanden ist.
		if (id !== undefined && !this.data.mapBenutzer.has(id))
			return this.getRoute(this.data.mapBenutzer.values().next().value.id);
		const eintrag = (id !== undefined) ? this.data.mapBenutzer.get(id) : undefined;
		await this.data.setBenutzer(eintrag);
	}

	public getRoute(id?: number): RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id } };
	}

	public getAuswahlProps(to: RouteLocationNormalized): BenutzerAuswahlProps {
		return {
			auswahl: () => this.data.auswahl,
			mapBenutzer: this.data.mapBenutzer,
			gotoBenutzer: this.data.gotoBenutzer,
			createBenutzerAllgemein: this.data.createBenutzerAllgemein,
			deleteBenutzerAllgemein: this.data.deleteBenutzerAllgemein,
			gotoSchule: routeSchule.gotoSchule
		};
	}

	public getProps(to: RouteLocationNormalized): BenutzerAppProps {
		return {
			auswahl: () => this.data.auswahl,
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	public get childRouteSelector(): WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id }, });
			},
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
		await RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl?.id } });
		await this.data.setView(node);
	};
}

export const routeSchuleBenutzer = new RouteSchuleBenutzer();
