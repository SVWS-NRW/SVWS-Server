import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import { BenutzerKompetenz, DeveloperNotificationException, ServerMode } from "@core";

import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeGost, type RouteGost } from "~/router/apps/gost/RouteGost";
import { routeGostAbiturZulassung } from "~/router/apps/gost/abitur/RouteGostAbiturZulassung";
import { routeGostAbiturPruefungsuebersicht } from "./RouteGostAbiturPruefungsuebersicht";
import { RouteDataGostAbitur } from "~/router/apps/gost/abitur/RouteDataGostAbitur";

import type { GostAbiturProps } from "~/components/gost/abitur/GostAbiturProps";
import type { TabData } from "@ui";
import { schulformenGymOb } from "~/router/RouteHelper";
import { api } from "~/router/Api";
import { routeGostAbiturNoteneingabe } from "./RouteGostAbiturNoteneingabe";

const GostAbitur = () => import("~/components/gost/abitur/GostAbitur.vue");


export class RouteGostAbitur extends RouteNode<RouteDataGostAbitur, RouteGost> {

	public constructor() {
		super(schulformenGymOb, [
			BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN,
			BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN,
		], "gost.abitur", "abitur", GostAbitur, new RouteDataGostAbitur());
		super.mode = ServerMode.DEV;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Abitur";
		this.isHidden = (params?: RouteParams) => {
			return this.checkHidden(params);
		}
		super.children = [
			routeGostAbiturZulassung,
			routeGostAbiturNoteneingabe,
			routeGostAbiturPruefungsuebersicht,
		];
		super.defaultChild = routeGostAbiturZulassung;
	}

	protected checkHidden(params?: RouteParams) {
		try {
			const { abiturjahr } = (params !== undefined) ? RouteNode.getIntParams(params, ["abiturjahr"]) : {abiturjahr: undefined};
			if (abiturjahr === undefined)
				return false;
			const eintrag = routeGost.data.mapAbiturjahrgaenge.get(abiturjahr);
			if (eintrag === undefined)
				return false;
			if ((eintrag.abiturjahr !== -1)
				&& (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_ALLGEMEIN)
					|| (api.benutzerHatKompetenz(BenutzerKompetenz.ABITUR_ANSEHEN_FUNKTIONSBEZOGEN) && api.benutzerKompetenzenAbiturjahrgaenge.has(eintrag.abiturjahr)))
				&& ((eintrag.jahrgang === 'Q1') || (eintrag.jahrgang === 'Q2')))
				return false;
			return routeGost.getRouteDefaultChild({ abiturjahr });
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			const { abiturjahr } = RouteNode.getIntParams(to_params, [ "abiturjahr" ]);
			if (abiturjahr === undefined)
				throw new DeveloperNotificationException("Fehler: Kein Abiturjahrgang in der URL angegeben.");
			const eintrag = routeGost.data.mapAbiturjahrgaenge.get(abiturjahr);
			if (eintrag === undefined)
				return routeGost.getRoute({ abiturjahr });
			try {
				await this.data.setAbiturjahr(eintrag, isEntering);
			} catch(error) {
				return routeGost.getRoute({ abiturjahr });
			}
			if (to === this)
				return this.getRouteView(this.data.view);
			if (!to.name.startsWith(this.data.view.name))
				for (const child of this.children)
					if (to.name.startsWith(child.name))
						this.data.setView(child, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public getProps(to: RouteLocationNormalized): GostAbiturProps {
		return {
			tabManager: () => this.createTabManagerByChildren(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute(this.getRouteView(node));
		this.data.setView(node, this.children);
	}

}

export const routeGostAbitur = new RouteGostAbitur();

