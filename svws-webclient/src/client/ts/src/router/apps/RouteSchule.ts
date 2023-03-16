import { shallowRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw } from "vue-router";
import { RouteApp } from "~/router/RouteApp";
import { routeSchuleBenutzer } from "~/router/apps/schule/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/schule/RouteSchuleBenutzergruppe";
import { BenutzerKompetenz, Schulform } from "@svws-nrw/svws-core";
import { RouteNode } from "../RouteNode";
import { RouteManager } from "../RouteManager";
import { SchuleAuswahlProps } from "~/components/schule/SSchuleAuswahlProps";
import { AuswahlChildData } from "~/components/AuswahlChildData";
import { routeSchuleDatenaustausch } from "./schule/RouteSchuleDatenaustausch";

interface RouteStateSchule {
	view: RouteNode<any, any>;
}
export class RouteDataSchule {

	private static _defaultState: RouteStateSchule = {
		view: routeSchuleBenutzer,
	}
	private _state = shallowRef(RouteDataSchule._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateSchule>) {
		this._state.value = Object.assign({ ... RouteDataSchule._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateSchule>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeSchule.menu.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Schule gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}
}
const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchuleApp = () => import("~/components/schule/SSchuleApp.vue")

export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule", "/schule", SSchuleApp, new RouteDataSchule());
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Schule";
		super.setView("liste", SSchuleAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
		];
		super.menu = [
			// TODO { title: "Schule bearbeiten", value: "schule_bearbeiten" },
			// TODO { title: "Einstellungen", value: "einstellungen" },
			// TODO { title: "Datensicherung", value: "datensicherung" },
			// TODO { title: "Schuljahreswechsel", value: "schuljahreswechsel" },
			// TODO { title: "Werkzeuge", value: "werkzeuge" },
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeSchuleDatenaustausch,
			// TODO { title: "Hilfe", value: "hilfe" }
		];
		super.defaultChild = undefined;
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuleAuswahlProps {
		return {
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	private getChild(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: {} });
		await this.data.setView(node);
	}

	returnToSchule = async () => await RouteManager.doRoute({ name: this.name, params: {} });

}

export const routeSchule = new RouteSchule();
