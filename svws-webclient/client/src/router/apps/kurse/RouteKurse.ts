import type { RouteLocationNormalizedGeneric, RouteLocationRaw, RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

import { RouteDataKurse } from "~/router/apps/kurse/RouteDataKurse";
import { routeKursDaten } from "~/router/apps/kurse/RouteKursDaten";
import { type RouteApp } from "~/router/apps/RouteApp";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

import { routeKurseGruppenprozesse } from "./RouteKurseGruppenprozesse";
import { routeKurseNeu } from "./RouteKurseNeu";


const SKurseAuswahl = () => import("~/components/kurse/SKurseAuswahl.vue");
const SKurseApp = () => import("~/components/kurse/SKurseApp.vue");

export class RouteKurse extends RouteTabNode<RouteDataKurse, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "kurse", String.raw`kurse/:id(\d+)?`, SKurseApp, new RouteDataKurse());
		super.setView("liste", SKurseAuswahl, (_route) => <RouteLocationNormalizedGeneric>{});
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
			// Daten zum ausgewählten Schuljahresabschnitt und Schüler laden
			const kurseAuswahlState = useKurseAuswahlState();
			const idNeu = await kurseAuswahlState.init(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id)) {
				return this.data.defaultView.getRoute({ id: idNeu });
			}

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
				return this.getRouteView(to, { id: '' });
			}

			if (to.hasType(ViewType.GRUPPENPROZESSE)) {
				await kurseAuswahlState.gotoGruppenprozessView(false);
			} else if (to.hasType(ViewType.HINZUFUEGEN)) {
				await kurseAuswahlState.gotoHinzufuegenView(false);
			} else if (to.hasType(ViewType.NEU)) {
				await kurseAuswahlState.gotoSchnelleingabeView(false);
			} else {
				await kurseAuswahlState.gotoDefaultView(id);
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
		const kurseAuswahlState = useKurseAuswahlState();
		if (kurseAuswahlState.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}
		this.data.reset();
		const { id } = RouteNode.getStringParams(from_params, ['id']);
		await configStateImpl.nonPersistentConfig.setValue(`${this.name}.auswahl.id`, id ?? "");
	}

	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		return this.getRouteSelectedChild();
	};

}

export const routeKurse = new RouteKurse();
