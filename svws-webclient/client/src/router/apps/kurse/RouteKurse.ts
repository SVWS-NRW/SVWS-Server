import type { RouteLocationNormalizedGeneric, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import type { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { type RouteApp } from "~/router/apps/RouteApp";
import { routeError } from "~/router/error/RouteError";
import { GruppenprozessRouting } from "~/router/GruppenprozessRouting";
import { updateAuswahlRoute } from "~/router/RouteHelper";
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";


const KurseAuswahl = () => import("~/components/kurse/KurseAuswahl.vue");
const KurseApp = () => import("~/components/kurse/KurseApp.vue");

export class RouteKurse extends RouteTabNode<RouteDataKurse, RouteApp> {

	/** Das Routing der Gruppenprozesse: Guard bei ungespeicherten Änderungen und Aufräumen beim Verlassen */
	private readonly _gruppenprozessRouting = new GruppenprozessRouting(this, () => useKurseAuswahlState().gruppenprozesse);

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse", String.raw`kurse/:id(\d+)?`, KurseApp, new RouteDataKurse());
		super.setView("liste", KurseAuswahl, (_route) => <RouteLocationNormalizedGeneric>{});
		super.mode = ServerMode.STABLE;
		super.text = "Kurse";
		super.children = [
			routeKursDaten,
			routeKurseGruppenprozesse,
			routeKurseNeu,
		];
		super.defaultChild = routeKursDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-book-2-line";
		configStateImpl.config.addElements([
			new ConfigElement("kurse.auswahl.filterNurSichtbar", "user", "true"),
		]);
		configStateImpl.nonPersistentConfig.addElements([
			new ConfigElement(`${this.name}.auswahl.id`, "user", ""),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams,
		isEntering: boolean, redirected: RouteNode<any, any> | undefined): Promise<void | Error | RouteLocationRaw> {
		this._gruppenprozessRouting.update(to, from);

		try {
			const redirect = await updateAuswahlRoute({
				route: this,
				state: useKurseAuswahlState(),
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

	public async leave(from: RouteNode<any, any>, from_params: RouteParams, to: RouteNode<any, any>, to_params: RouteParams): Promise<void> {
		this._gruppenprozessRouting.leave();
		const kurseAuswahlState = useKurseAuswahlState();
		if (kurseAuswahlState.navigator.activeViewType !== ViewType.DEFAULT) {
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
		const kurseAuswahlState = useKurseAuswahlState();
		if (!kurseAuswahlState.isAvailable) {
			return params;
		}
		const id = kurseAuswahlState.idSelected;
		if (id !== null) {
			params.id = id;
		}
		return params;
	}

	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		return this.getRouteSelectedChild();
	};

}

export const routeKurse = new RouteKurse();
