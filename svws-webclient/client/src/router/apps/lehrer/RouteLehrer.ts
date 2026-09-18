import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { useLeitungsfunktionenState } from "@ui/states/kataloge/LeitungsfunktionenState";
import { useOrteState } from "@ui/states/kataloge/OrteState";
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
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
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
			const orteState = useOrteState();
			const leitungsfunktionenState = useLeitungsfunktionenState();
			await Promise.all([orteState.init(), leitungsfunktionenState.init()]);
		}
		try {
			const { idSchuljahresabschnitt, id: paramId } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id"]);
			if (idSchuljahresabschnitt === undefined) {
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			}
			let id = paramId;
			if ((paramId === undefined) && isEntering) {
				const lastId = Number.parseInt(configStateImpl.nonPersistentConfig.getValue(`${this.name}.auswahl.id`));
				if (!Number.isNaN(lastId)) {
					id = lastId;
				}
			}
			if (isEntering && to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN, ViewType.NEU])) {
				return this.getRouteView(this.data.view, { id: id ?? '' });
			}
			// Daten zum ausgewählten Schuljahresabschnitt und Lehrer laden
			const lehrerAuswahlState = useLehrerAuswahlState();
			const idNeu = await lehrerAuswahlState.init(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id)) {
				return this.data.defaultView.getRoute({ id: idNeu });
			}

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
				return this.getRouteView(to, { id: '' });
			}

			if (to.hasType(ViewType.GRUPPENPROZESSE)) {
				await lehrerAuswahlState.gotoGruppenprozessView(false);
			} else if (to.hasType(ViewType.HINZUFUEGEN)) {
				await lehrerAuswahlState.gotoHinzufuegenView(false);
			} else if (to.hasType(ViewType.NEU)) {
				await lehrerAuswahlState.gotoSchnelleingabeView(false);
			} else {
				await lehrerAuswahlState.gotoDefaultView(id);
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

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		const lehrerAuswahlState = useLehrerAuswahlState();
		if (lehrerAuswahlState.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}
		this.data.reset();
		lehrerAuswahlState.reset();
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
		const id = lehrerAuswahlState.manager.auswahlID();
		if (id !== null) {
			params.id = id;
		}
		return params;
	}


	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		const lehrerAuswahlState = useLehrerAuswahlState();
		if (!lehrerAuswahlState.manager.hasDaten()) {
			return;
		}
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name)) {
			return this.getRouteView(routeLehrerStundenplan);
		}
		return this.getRouteSelectedChild();
	};

}

export const routeLehrer = new RouteLehrer();
