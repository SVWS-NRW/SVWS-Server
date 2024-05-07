import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchuleDatenaustauschAuswahlProps } from "~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahlProps";

import { BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { routeApp } from "~/router/apps/RouteApp";
import { routeSchule, type RouteSchule } from "~/router/apps/schule/RouteSchule";

import { routeSchuleDatenaustauschKurs42 } from "~/router/apps/schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschLaufbahnplanung } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschLupo";
import { routeSchuleDatenaustauschENM } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschENM";
import { routeSchuleDatenaustauschWenom } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschWenom";
import { routeSchuleDatenaustauschSchulbewerbung } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustauschSchulbewerbung";

import { RouteDataSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteDataSchuleDatenaustausch";
import { routeSchuleDatenaustauschUntis } from "~/router/apps/schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";


const SSchuleDatenaustauschApp = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschApp.vue")
const SSchuleDatenaustauschAuswahl = () => import("~/components/schule/datenaustausch/SSchuleDatenaustauschAuswahl.vue")

export class RouteSchuleDatenaustausch extends RouteNode<RouteDataSchuleDatenaustausch, RouteSchule> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule.datenaustausch", "schule/datenaustausch", SSchuleDatenaustauschApp, new RouteDataSchuleDatenaustausch());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getNoProps(route);
		super.text = "Datenaustausch";
		super.setView("liste", SSchuleDatenaustauschAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleDatenaustauschLaufbahnplanung,
			routeSchuleDatenaustauschKurs42,
			routeSchuleDatenaustauschSchulbewerbung,
			routeSchuleDatenaustauschENM,
			routeSchuleDatenaustauschWenom,
			routeSchuleDatenaustauschUntis,
		];
		super.defaultChild = routeSchuleDatenaustauschLaufbahnplanung;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams): Promise<any> {
		return this.getRoute();
	}

	public async enter() {
		return this.setChild(this.defaultChild!);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					this.data.setView(child, this.children);
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuleDatenaustauschAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoSchule: routeSchule.gotoSchule,
			// Props für die Navigation
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
		for (const c of this.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		if (value.name === this.data.view?.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.children);
	}

}

export const routeSchuleDatenaustausch = new RouteSchuleDatenaustausch();

