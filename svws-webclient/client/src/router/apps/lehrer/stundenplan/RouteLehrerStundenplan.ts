import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { LehrerStundenplanProps } from "~/components/lehrer/stundenplan/LehrerStundenplanProps";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeLehrer, type RouteLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { RouteDataLehrerStundenplan } from "~/router/apps/lehrer/stundenplan/RouteDataLehrerStundenplan";
import { api } from "~/router/Api";
import { configStateImpl } from "~/states/ConfigStateImpl";
import { Schulform } from "@core/asd/types/schule/Schulform";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";
import { ServerMode } from "@core/core/types/ServerMode";
import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
import { ConfigElement } from "@ui/utils/Config";

const SLehrerStundenplan = () => import("~/components/lehrer/stundenplan/LehrerStundenplan.vue");

export class RouteLehrerStundenplan extends RouteNode<RouteDataLehrerStundenplan, RouteLehrer> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN], "lehrer.stundenplan", String.raw`stundenplan/:idStundenplan(\d+)?/:wochentyp(\d+)?/:kw(\d+\.\d+)?`, SLehrerStundenplan, new RouteDataLehrerStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
		configStateImpl.config.addElements([
			new ConfigElement("lehrer.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: idLehrer, idStundenplan, wochentyp } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id", "idStundenplan", "wochentyp"]);
			const { kw: kwString } = RouteNode.getStringParams(to_params, ["kw"]);
			if (idSchuljahresabschnitt === undefined) {
				throw new DeveloperNotificationException("Beim Aufruf der Route ist kein gültiger Schuljahresabschnitt gesetzt.");
			}
			let kwjahr = undefined;
			let kw = undefined;
			if ((kwString !== undefined) && (kwString !== "") && (wochentyp === undefined)) {
				const tmpKW = kwString.split(".");
				if (tmpKW.length !== 2) {
					throw new DeveloperNotificationException("Die Angabe der Kalenderwoche muss die Form 'Jahr.KW' haben.");
				}
				kwjahr = Number.parseInt(tmpKW[0]);
				kw = Number.parseInt(tmpKW[1]);
			}
			// Prüfe, ob ein Lehrer ausgewählt ist. Wenn nicht dann wechsele in die Lehrer-Route zurück.
			if (idLehrer === undefined) {
				return routeLehrer.getRoute();
			}
			// Lade die Stundenplandaten neu, wenn die ID des Schuljahresabschnittes sich ändert (das passiert beim Laden der Route automatisch)
			if (await this.data.ladeListe(idLehrer)) {
				return this.getRoute();
			}
			// Aktualisiere / Lade ggf. den Stundenplan ...
			if (idStundenplan !== undefined) {
				await routeLehrerStundenplan.data.setEintrag(idLehrer, idStundenplan, wochentyp ?? 0, kwjahr, kw);
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return {
			idStundenplan: (this.data.hasAuswahl === true) ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
	}

	public getProps(to: RouteLocationNormalized): LehrerStundenplanProps {
		return {
			apiStatus: api.status,
			id: routeLehrer.data.manager.daten().id,
			ignoreEmpty: this.data.ganzerStundenplan,
			stundenplan: () => (this.data.mapStundenplaene.size === 0) ? undefined : this.data.auswahl,
			mapStundenplaene: this.data.mapStundenplaene,
			gotoStundenplan: this.data.gotoStundenplan,
			gotoWochentyp: this.data.gotoWochentyp,
			gotoKalenderwoche: this.data.gotoKalenderwoche,
			manager: () => this.data.manager,
			wochentyp: () => this.data.wochentyp,
			kalenderwoche: () => this.data.kalenderwoche,
			ganzerStundenplan: () => this.data.ganzerStundenplan,
			setGanzerStundenplan: this.data.setGanzerStundenplan,
		};
	}

}

export const routeLehrerStundenplan = new RouteLehrerStundenplan();
