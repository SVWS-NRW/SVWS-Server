import type { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import { BenutzerKompetenz, Schulform, ServerMode, DeveloperNotificationException } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { routeError } from "~/router/error/RouteError";
import { routeFaecher, type RouteFaecher } from "~/router/apps/schule/kataloge/faecher/RouteFaecher";
import { RouteDataFaecherStundenplan } from "~/router/apps/schule/kataloge/faecher/stundenplan/RouteDataFaecherStundenplan";
import { api } from "~/router/Api";
import type { FaecherStundenplanProps } from "~/components/schule/kataloge/faecher/stundenplan/FaecherStundenplanProps";
import { ConfigElement } from "@ui";

const SFaecherStundenplan = () => import("~/components/schule/kataloge/faecher/stundenplan/FaecherStundenplan.vue");

export class RouteFaecherStundenplan extends RouteNode<RouteDataFaecherStundenplan, RouteFaecher> {

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.STUNDENPLAN_ALLGEMEIN_ANSEHEN], "schule.faecher.stundenplan", "stundenplan/:idStundenplan(\\d+)?/:wochentyp(\\d+)?/:kw(\\d+\\.\\d+)?", SFaecherStundenplan, new RouteDataFaecherStundenplan());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Stundenplan";
		super.children = [];
		api.config.addElements([
			new ConfigElement("schule.faecher.stundenplan.ganzerStundenplan", "user", "true"),
		]);
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			const { idSchuljahresabschnitt, id: idFach, idStundenplan, wochentyp } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt", "id", "idStundenplan", "wochentyp"]);
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
				kwjahr = parseInt(tmpKW[0]);
				kw = parseInt(tmpKW[1]);
			}
			// Prüfe, ob ein Schüler ausgewählt ist. Wenn nicht dann wechsele in die Schüler-Route zurück.
			if (idFach === undefined) {
				return routeFaecher.getRoute();
			}
			// Lade die Stundenplandaten neu, wenn die ID des Schuljahresabschnittes sich ändert (das passiert beim Laden der Route automatisch)
			if (await this.data.ladeListe()) {
				return this.getRoute();
			}
			// Setze den Stundenplan ...
			await routeFaecherStundenplan.data.setEintrag(idFach, idStundenplan, wochentyp ?? 0, kwjahr, kw);
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		this.data.reset();
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		return {
			idStundenplan: this.data.hatAuswahl ? this.data.auswahl.id : undefined,
			wochentyp: this.data.wochentyp,
			kw: (this.data.kalenderwoche === undefined) ? undefined : this.data.kalenderwoche.jahr + "." + this.data.kalenderwoche.kw,
		};
	}

	public getProps(to: RouteLocationNormalized): FaecherStundenplanProps {
		return {
			apiStatus: api.status,
			getPDF: this.data.getPDF,
			ignoreEmpty: this.data.ganzerStundenplan,
			id: routeFaecher.data.manager.daten().id,
			stundenplan: () => (this.data.mapStundenplaene.size === 0 || !this.data.hasManager) ? undefined : this.data.auswahl,
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

export const routeFaecherStundenplan = new RouteFaecherStundenplan();

