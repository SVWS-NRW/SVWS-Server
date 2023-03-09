import { BenutzerKompetenz, FaecherListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core";
import { computed, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { FaecherAppProps } from "~/components/kataloge/faecher/SFaecherAppProps";
import { FaecherAuswahlProps } from "~/components/kataloge/faecher/SFaecherAuswahlProps";
import { routeKatalogFaecherDaten } from "~/router/apps/faecher/RouteKatalogFaecherDaten";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";

export class RouteDataKatalogFaecher {
	auswahl: ShallowRef<FaecherListeEintrag | undefined> = shallowRef(undefined);
	listFaecher: List<FaecherListeEintrag> = new Vector();
	mapFaecher: Map<number, FaecherListeEintrag> = new Map();

	public async ladeListe() {
		this.listFaecher = await api.server.getFaecher(api.schema);
		const mapKurse = new Map<number, FaecherListeEintrag>();
		for (const l of this.listFaecher)
			mapKurse.set(l.id, l);
		this.mapFaecher = mapKurse;
	}

	public async onSelect(item?: FaecherListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
		} else {
			this.auswahl.value = item;
		}
	}

	setFach = async (value: FaecherListeEintrag | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeKatalogFaecher.name, params: { } });
			return;
		}
		const redirect_name: string = (routeKatalogFaecher.selectedChild === undefined) ? routeKatalogFaecherDaten.name : routeKatalogFaecher.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}}

const SFaecherAuswahl = () => import("~/components/kataloge/faecher/SFaecherAuswahl.vue")
const SFaecherApp = () => import("~/components/kataloge/faecher/SFaecherApp.vue")

export class RouteKatalogFaecher extends RouteNode<RouteDataKatalogFaecher, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "faecher", "/kataloge/faecher/:id(\\d+)?", SFaecherApp, new RouteDataKatalogFaecher());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Fächer";
		super.setView("liste", SFaecherAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogFaecherDaten
		];
		super.defaultChild = routeKatalogFaecherDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams): Promise<any> {
		if ((to.name === this.name) && (to_params.id === undefined)) {
			await this.data.ladeListe();
			if (this.data.mapFaecher.size === 0)
				// TODO Handhabung bei neuer Schule -> Liste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapFaecher.values().next().value.id);
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
			await this.data.onSelect(this.data.mapFaecher.get(id));
		}
	}

	public getRoute(id: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): FaecherAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			listFaecher: this.data.listFaecher,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			setFach: this.data.setFach
		};
	}

	public getProps(to: RouteLocationNormalized): FaecherAppProps {
		return {
			auswahl: this.data.auswahl.value
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

export const routeKatalogFaecher = new RouteKatalogFaecher();
