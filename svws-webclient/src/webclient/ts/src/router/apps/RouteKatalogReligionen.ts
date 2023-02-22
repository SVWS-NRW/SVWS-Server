import { List, ReligionEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeKatalogReligionDaten } from "~/router/apps/religion/RouteKatalogReligionDaten";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteManager } from "../RouteManager";
import { ReligionenAppProps } from "~/components/kataloge/religionen/SReligionenAppProps";
import { ReligionenAuswahlProps } from "~/components/kataloge/religionen/SReligionenAuswahlPops";
import { api } from "../Api";

export class RouteDataKatalogReligionen {
	auswahl: ShallowRef<ReligionEintrag | undefined> = shallowRef(undefined);
	listReligionen: List<ReligionEintrag> = new Vector();
	mapReligionen: Map<number, ReligionEintrag> = new Map();

	public async ladeListe() {
		this.listReligionen = await api.server.getReligionen(api.schema);
		const mapKurse = new Map<number, ReligionEintrag>();
		for (const l of this.listReligionen)
			mapKurse.set(l.id, l);
		this.mapReligionen = mapKurse;
	}

	public async onSelect(item?: ReligionEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setReligion = async (value: ReligionEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeKatalogReligion.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKatalogReligion.selectedChild === undefined) ? routeKatalogReligionDaten.name : routeKatalogReligion.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}
}
const SReligionenAuswahl = () => import("~/components/kataloge/religionen/SReligionenAuswahl.vue")
const SReligionenApp = () => import("~/components/kataloge/religionen/SReligionenApp.vue")

export class RouteKatalogReligionen extends RouteNode<RouteDataKatalogReligionen, RouteApp> {

	public constructor() {
		super("religionen", "/kataloge/religion/:id(\\d+)?", SReligionenApp, new RouteDataKatalogReligionen());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Religion";
		super.setView("liste", SReligionenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogReligionDaten
		];
		super.defaultChild = routeKatalogReligionDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapReligionen.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapReligionen.values().next().value.id);
		}
		await this.data.ladeListe();
	}


	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.onSelect(this.data.mapReligionen.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ReligionenAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listReligionen: this.data.listReligionen,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt,
			setReligion: this.data.setReligion
		};
	}

	public getProps(to: RouteLocationNormalized): ReligionenAppProps {
		return {
			auswahl: this.data.auswahl.value,
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

export const routeKatalogReligion = new RouteKatalogReligionen();
