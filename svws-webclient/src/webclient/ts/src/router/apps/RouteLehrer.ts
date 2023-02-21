import { LehrerListeEintrag, LehrerStammdaten } from "@svws-nrw/svws-core-ts";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
import { routeLehrerPersonaldaten } from "~/router/apps/lehrer/RouteLehrerPersonaldaten";
import { routeLehrerUnterrichtsdaten } from "~/router/apps/lehrer/RouteLehrerUnterrichtsdaten";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { routeLogin } from "../RouteLogin";
import { RouteManager } from "../RouteManager";
import { LehrerAppProps } from "~/components/lehrer/SLehrerAppProps";
import { LehrerAuswahlProps } from "~/components/lehrer/SLehrerAuswahlProps";


export class RouteDataLehrer {

	auswahl: ShallowRef<LehrerListeEintrag | undefined> = shallowRef(undefined);
	mapLehrer: Map<number, LehrerListeEintrag> = new Map();
	_stammdaten: LehrerStammdaten | undefined = undefined;

	public async ladeListe() {
		const listLehrer = await routeLogin.data.api.getLehrer(routeLogin.data.schema);
		const mapLehrer = new Map<number, LehrerListeEintrag>();
		for (const l of listLehrer)
			mapLehrer.set(l.id, l);
		this.mapLehrer = mapLehrer;
	}

	public async onSelect(item?: LehrerListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
			this._stammdaten = undefined;
		} else {
			this.auswahl.value = item;
			this._stammdaten = await routeLogin.data.api.getLehrerStammdaten(routeLogin.data.schema, item.id);
		}
	}

	get stammdaten(): LehrerStammdaten {
		if (this._stammdaten === undefined)
			throw new Error("Unerwarteter Fehler: Lehrerstammdaten nicht initialisiert");
		return this._stammdaten;
	}

	gotoLehrer = async (value: LehrerListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeLehrer.name, params: { } });
			return;
		}
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

}


const SLehrerAuswahl = () => import("~/components/lehrer/SLehrerAuswahl.vue")
const SLehrerApp = () => import("~/components/lehrer/SLehrerApp.vue")


export class RouteLehrer extends RouteNode<RouteDataLehrer, RouteApp> {

	public constructor() {
		super("lehrer", "/lehrkraefte/:id(\\d+)?", SLehrerApp, new RouteDataLehrer());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Lehrkräfte";
		super.setView("liste", SLehrerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeLehrerIndividualdaten,
			routeLehrerPersonaldaten,
			routeLehrerUnterrichtsdaten
		];
		super.defaultChild = routeLehrerIndividualdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.ladeListe(); // Laden der Lehrerliste für die Auswahl
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const idLehrer = !to_params.id ? undefined : parseInt(to_params.id);
		if (idLehrer === undefined) {
			await this.data.onSelect(undefined);
			return;
		}
		const eintrag = this.data.mapLehrer.get(idLehrer);
		if (eintrag === undefined) {
			if (this.data.mapLehrer.size === 0)
				return this.getRoute(undefined);
			return this.getRoute(this.data.mapLehrer.values().next().value);
		}
		await this.data.onSelect(eintrag);
	}

	public getRoute(id: number | undefined) : RouteLocationRaw {
		const redirect_name: string = (routeLehrer.selectedChild === undefined) ? routeLehrerIndividualdaten.name : routeLehrer.selectedChild.name;
		return { name: redirect_name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): LehrerAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			mapLehrer: this.data.mapLehrer,
			setLehrer: this.data.gotoLehrer,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}
	public getProps(to: RouteLocationNormalized): LehrerAppProps {
		return {
			stammdaten: this.data.stammdaten,
		};
	}

	public get childRouteSelector() : WritableComputedRef<RouteRecordRaw> {
		return computed({
			get: () => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				void RouteManager.doRoute({ name: value.name, params: { id: this.data.auswahl.value?.id } });
			}
		});
	}

}

export const routeLehrer = new RouteLehrer();