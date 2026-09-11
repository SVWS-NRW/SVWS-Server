import type { RouteParams } from "vue-router";

import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ServerMode } from "@core/core/types/ServerMode";
import type { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";
import { AppMenuGroup } from "@ui/ui/nav/AppMenuGroup";

import SUvApp from "~/components/unterrichtsverteilung/SUvApp.vue";
import SUvPlanungsabschnitteAuswahl from "~/components/unterrichtsverteilung/SUvPlanungsabschnitteAuswahl.vue";
import type { RouteApp } from "~/router/apps/RouteApp";
import { routeUvGrunddatenFaecher } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenFaecher";
import { routeUvGrunddatenLehrer } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenLehrer";
import { routeUvGrunddatenRaeumeGruppen } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenRaeumeGruppen";
import { routeUvGrunddatenStundentafeln } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenStundentafeln";
import { routeUvGrunddatenZeitraster } from "~/router/apps/unterrichtsverteilung/grunddaten/RouteUvGrunddatenZeitraster";
import { RouteDataUv } from "~/router/apps/unterrichtsverteilung/RouteDataUv";
import { routeUvKlassen } from "~/router/apps/unterrichtsverteilung/RouteUvKlassen";
import { routeUvKurse } from "~/router/apps/unterrichtsverteilung/RouteUvKurse";
import { routeUvLerngruppen } from "~/router/apps/unterrichtsverteilung/RouteUvLerngruppen";
import { routeUvPlanungsabschnitt } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnitte";
import { routeUvPlanungsabschnittGruppenprozesse } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnittGruppenprozesse";
import { routeUvPlanungsabschnittNeu } from "~/router/apps/unterrichtsverteilung/RouteUvPlanungsabschnittNeu";
import { routeUvSchienen } from "~/router/apps/unterrichtsverteilung/RouteUvSchienen";
import { routeUvSchueler } from "~/router/apps/unterrichtsverteilung/RouteUvSchueler";
import { routeUvSchuelergruppen } from "~/router/apps/unterrichtsverteilung/RouteUvSchuelergruppen";
import { routeUvUnterrichte } from "~/router/apps/unterrichtsverteilung/RouteUvUnterrichte";
import { routeError } from "~/router/error/RouteError";
import { RouteAuswahlNode } from "~/router/RouteAuswahlNode";
import { RouteNode } from "~/router/RouteNode";

export class RouteUv extends RouteAuswahlNode<UvPlanungsabschnitteListeManager, RouteDataUv, RouteApp> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN], "uv", String.raw`uv/:id(-?\d+)?`, SUvApp, SUvPlanungsabschnitteAuswahl, new RouteDataUv());
		super.text = "Unterrichtsverteilung";
		super.mode = ServerMode.DEV;
		super.menugroup = AppMenuGroup.MAIN;
		super.icon = "i-ri-calendar-2-line";
		super.children = [
			routeUvPlanungsabschnitt,
			routeUvGrunddatenLehrer,
			routeUvSchueler,
			routeUvSchuelergruppen,
			routeUvKlassen,
			routeUvKurse,
			routeUvSchienen,
			routeUvLerngruppen,
			routeUvUnterrichte,
			routeUvGrunddatenRaeumeGruppen,
			routeUvGrunddatenFaecher,
			routeUvGrunddatenStundentafeln,
			routeUvGrunddatenZeitraster,
			routeUvPlanungsabschnittNeu,
			routeUvPlanungsabschnittGruppenprozesse,
		];
		super.defaultChild = routeUvPlanungsabschnitt;
		super.updateIfTarget = this.doUpdateIfTarget;
		super.getAuswahlProps = (props) => ({
			activeViewType: props.activeViewType,
			tabManager: props.tabManager,
			manager: props.manager,
		});
	}

	protected doUpdateIfTarget = async (to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined) => {
		if (this.data.manager.hasDaten() !== true) {
			return;
		}
		const params = { ...to_params, id: String(this.data.manager.auswahl().id) };
		const child = this.selectedChild ?? this.defaultChild;
		const hidden = child?.hidden(params);
		if (hidden !== undefined && hidden !== false) {
			return hidden;
		}
		return this.getRouteSelectedChild(params);
	};

	public static tabsCheckHidden(isGrunddaten: boolean, isAbschnittsdaten: boolean, params?: RouteParams) {
		if (params === undefined) {
			return false;
		}
		try {
			const { id } = RouteNode.getIntParams(params, ["id"]);
			if ((id !== undefined) && (id !== -1)) {
				if (!isAbschnittsdaten) {
					return { name: routeUvPlanungsabschnitt.name, params };
				}
			} else if (!isGrunddaten) {
				return { name: routeUvGrunddatenLehrer.name, params };
			}
			return false;
		} catch (e) {
			return routeError.getSimpleErrorRoute(e as DeveloperNotificationException);
		}
	}

	/** Löst einen Auswahlparameter innerhalb der für eine UV-Ansicht zulässigen Objekte auf. */
	public static resolveAuswahl<T>(value: string | string[] | undefined, items: Iterable<T>, getId: (item: T) => number): T | undefined {
		if ((value === undefined) || (value === '')) {
			return undefined;
		}
		if (Array.isArray(value) || !/^\d+$/.test(value) || !Number.isSafeInteger(Number(value))) {
			throw new DeveloperNotificationException('Ungültige UV-Objekt-ID im URL-Pfad.');
		}
		const id = Number(value);
		for (const item of items) {
			if (getId(item) === id) {
				return item;
			}
		}
		throw new DeveloperNotificationException('Das UV-Objekt ist in diesem Bereich bzw. Planungsabschnitt nicht vorhanden.');
	}

}

export const routeUv = new RouteUv();
