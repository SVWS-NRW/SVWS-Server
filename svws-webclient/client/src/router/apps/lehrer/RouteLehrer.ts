import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

import { routeLehrerAllgemeinesGruppenprozesse } from "~/router/apps/lehrer/allgemeines/RouteLehrerAllgemeinesGruppenprozesse";
import { routeLehrerEinwilligungen } from "~/router/apps/lehrer/einwilligungen/RouteLehrerEinwilligungen";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { routeLehrerIndividualdatenGruppenprozesse } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdatenGruppenprozesse";
import { routeLehrerLernplattformen } from "~/router/apps/lehrer/lernplattformen/RouteLehrerLernplattformen";
import { RouteDataLehrer } from "~/router/apps/lehrer/RouteDataLehrer";
import { routeLehrerNeu } from "~/router/apps/lehrer/RouteLehrerNeu";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeError } from "~/router/error/RouteError";
import { updateAuswahlRoute } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { einwilligungsartenStateImpl } from "~/states/kataloge/EinwilligungsartenStateImpl";
import { lernplattformenStateImpl } from "~/states/kataloge/LernplattformenStateImpl";
import { orteStateImpl } from "~/states/kataloge/OrteStateImpl";
import { schulinterneFunktionenStateImpl } from "~/states/kataloge/SchulinterneFunktionenStateImpl";
import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

import { routeLehrerStundenplan } from "./stundenplan/RouteLehrerStundenplan";

const LehrerAuswahl = () => import("~/components/lehrer/LehrerAuswahl.vue");
const LehrerApp = () => import("~/components/lehrer/LehrerApp.vue");

export class RouteLehrer extends RouteTabNode<RouteDataLehrer, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.LEHRERDATEN_ANSEHEN], "lehrer", String.raw`lehrkraefte/:id(\d+)?`, LehrerApp, new RouteDataLehrer());
		super.setView("liste", LehrerAuswahl, (_route) => <any>{
			pendingStateManagerRegistry: () => this.data.pendingStateManagerRegistry,
		});
		super.mode = ServerMode.STABLE;
		super.text = "Lehrkräfte";
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerStundenplan,
			routeLehrerUnterrichtsdaten,
			routeLehrerEinwilligungen,
			routeLehrerLernplattformen,
			routeLehrerAllgemeinesGruppenprozesse,
			routeLehrerIndividualdatenGruppenprozesse,
			routeLehrerNeu,
		];
		super.defaultChild = routeLehrerIndividualdaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-briefcase-line";
		configStateImpl.config.addElements([
			new ConfigElement("lehrer.auswahl.filterNurSichtbar", "user", "true"),
			new ConfigElement("lehrer.auswahl.filterNurStatistikrelevant", "user", "true"),
		]);
		configStateImpl.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		if (isEntering) {
			await this.initiateKataloge();
		}
		try {
			const redirect = await updateAuswahlRoute({
				route: this,
				state: useLehrerAuswahlState(),
				to,
				ids: RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]),
				isEntering,
			});
			if (redirect !== undefined) {
				return redirect;
			}

			if (to.name === this.name) {
				return await this.doUpdateIfTarget(from);
			}
			if (!to.name.startsWith(this.data.view.name)) {
				for (const child of this.children) {
					if (to.name.startsWith(child.name)) {
						this.data.setView(child, this.children);
					}
				}
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	private async initiateKataloge() {
		await Promise.all([
			orteStateImpl.init(),
			schulinterneFunktionenStateImpl.init(),
			lernplattformenStateImpl.init(),
			einwilligungsartenStateImpl.init(),
		]);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		const lehrerAuswahlState = useLehrerAuswahlState();
		if (lehrerAuswahlState.navigator.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}
		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, ['id']);
		await configStateImpl.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}


	/**
	 * Fügt die ID zu der Route hinzu. Diese Methode kann überschrieben werden, wenn neben
	 * der ID noch weitere Parameter benötigt werden.
	 *
	 * @returns die Routing-Parameter mit der ID.
	 */
	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const params = <RouteParamsRawGeneric>{};
		const lehrerAuswahlState = useLehrerAuswahlState();
		if (!lehrerAuswahlState.isAvailable) {
			return params;
		}
		const id = lehrerAuswahlState.idSelected;
		if (id !== null) {
			params.id = id;
		}
		return params;
	}


	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		const lehrerAuswahlState = useLehrerAuswahlState();
		if (!lehrerAuswahlState.stammdaten.hasDaten) {
			return;
		}
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name)) {
			return this.getRouteView(routeLehrerStundenplan);
		}
		return this.getRouteSelectedChild();
	};

}

export const routeLehrer = new RouteLehrer();
