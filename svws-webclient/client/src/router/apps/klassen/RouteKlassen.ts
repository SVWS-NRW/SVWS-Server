import type { RouteLocationNormalizedGeneric, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";
import { ViewType } from "@ui/ui/nav/ViewType";
import { ConfigElement } from "@ui/utils/Config";

import { RouteDataKlassen } from "~/router/apps/klassen/RouteDataKlassen";
import { routeKlassenDaten } from "~/router/apps/klassen/RouteKlassenDaten";
import { routeKlassenStundenplan } from "~/router/apps/klassen/stundenplan/RouteKlassenStundenplan";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeError } from "~/router/error/RouteError";
import { RouteNode } from "~/router/RouteNode";
import { RouteTabNode } from "~/router/RouteTabNode";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { useKlassenState } from "~/states/klassen/KlassenState";

import { routeKlasseGruppenprozesse } from "./RouteKlassenGruppenprozesse";
import { routeKlassenNeu } from "./RouteKlassenNeu";


const KlassenAuswahl = () => import("~/components/klassen/KlassenAuswahl.vue");
const KlassenApp = () => import("~/components/klassen/KlassenApp.vue");

export class RouteKlassen extends RouteTabNode<RouteDataKlassen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "klassen", String.raw`klassen/:id(-?\d+)?`, KlassenApp, new RouteDataKlassen());
		super.setView("liste", KlassenAuswahl, (_route) => <RouteLocationNormalizedGeneric>{});
		super.mode = ServerMode.STABLE;
		super.text = "Klassen";
		super.children = [
			routeKlassenDaten,
			routeKlassenNeu,
			routeKlassenStundenplan,
			routeKlasseGruppenprozesse,
		];
		super.defaultChild = routeKlassenDaten;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-team-line";
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
			const klassenState = useKlassenState();
			const idNeu = await klassenState.init(idSchuljahresabschnitt, isEntering);
			if ((idNeu !== null) && (idNeu !== id)) {
				return this.data.defaultView.getRoute({ id: idNeu });
			}

			// Wenn einer der folgenden Routen Types aufgerufen wird, wird hier ein Redirect initiiert, sobald eine ID in der URL enthalten ist.
			if (to.hasOneOfTypes([ViewType.GRUPPENPROZESSE, ViewType.HINZUFUEGEN]) && (id !== undefined)) {
				return this.getRouteView(to, { id: '' });
			}

			if (to.hasType(ViewType.GRUPPENPROZESSE)) {
				await klassenState.gotoGruppenprozessView(false);
			} else if (to.hasType(ViewType.HINZUFUEGEN)) {
				await klassenState.gotoHinzufuegenView(false);
			} else if (to.hasType(ViewType.NEU)) {
				await klassenState.gotoSchnelleingabeView(false);
			} else {
				await klassenState.gotoDefaultView(id);
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
		const klassenState = useKlassenState();
		if (klassenState.activeViewType !== ViewType.DEFAULT) {
			this._selectedChild.value = undefined;
		}
		this.data.reset();
		klassenState.reset();
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
		const params = {};
		const klassenState = useKlassenState();
		if (!klassenState.isAvailable) {
			return params;
		}
		const id = klassenState.manager.auswahlID();
		if (id !== null) {
			this.data.addID(params, id);
		}
		return params;
	}


	protected doUpdateIfTarget = async (from: RouteNode<any, any> | undefined) => {
		const klassenState = useKlassenState();
		if (!klassenState.manager.hasDaten()) {
			return;
		}
		if ((from !== undefined) && (/(\.|^)stundenplan/).test(from.name)) {
			return this.getRouteView(routeKlassenStundenplan);
		}
		return this.getRouteSelectedChild();
	};

}

export const routeKlassen = new RouteKlassen();
