import { GostJahrgang, GostJahrgangsdaten } from "@svws-nrw/svws-core-ts";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { App } from "~/apps/BaseApp";
import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
import { ListGost } from "~/apps/gost/ListGost";
import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { RouteManager } from "../RouteManager";
import { routeGostFachwahlen } from "./gost/RouteGostFachwahlen";
import { routeGostFaecher } from "./gost/RouteGostFaecher";
import { routeGostJahrgangsdaten } from "./gost/RouteGostJahrgangsdaten";
import { routeGostKlausurplanung } from "./gost/RouteGostKlausurplanung";
import { routeGostKursplanung } from "./gost/RouteGostKursplanung";

export class RouteDataGost {
	item: ShallowRef<GostJahrgang | undefined> = shallowRef(undefined);
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	jahrgangsdaten: DataGostJahrgang = new DataGostJahrgang();
	dataFaecher: DataGostFaecher = new DataGostFaecher();
	listJahrgaenge: ListJahrgaenge = new ListJahrgaenge();

	patchJahrgangsdaten = async (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => {
		if (this.jahrgangsdaten.daten === undefined)
			return false;
		await App.api.patchGostAbiturjahrgang(data, App.schema, abiturjahr);
		return true;
	}

	addAbiturjahrgang = async (idJahrgang: number) => {
		const abiturjahr = await App.api.createGostAbiturjahrgang(App.schema, idJahrgang);
		await routeGost.liste.update_list();
		await RouteManager.doRoute(routeGost.getRoute(abiturjahr));
	}

}

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue")
const SGostApp = () => import("~/components/gost/SGostApp.vue")


export class RouteGost extends RouteNodeListView<ListGost, GostJahrgang, RouteDataGost, RouteApp> {

	public constructor() {
		super("gost", "/gost/:abiturjahr(-?\\d+)?", SGostAuswahl, SGostApp, new ListGost(), 'abiturjahr', new RouteDataGost());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Oberstufe";
		super.setView("liste", SGostAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeGostJahrgangsdaten,
			routeGostFaecher,
			routeGostFachwahlen,
			routeGostKursplanung,
			routeGostKlausurplanung
		];
		super.defaultChild = routeGostJahrgangsdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		const redirect: RouteNode<unknown, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (to_params.abiturjahr === undefined)
			await this.liste.update_list();
		const abiturjahr = (to_params.abiturjahr !== undefined) ? parseInt(to_params.abiturjahr as string)
			: ((this.data.item.value !== undefined) ? this.data.item.value.abiturjahr : -1);
		if (redirect.hidden({ abiturjahr: "" + abiturjahr }))
			return { name: this.defaultChild!.name, params: { abiturjahr: abiturjahr }};
		return { name: redirect.name, params: { abiturjahr: abiturjahr }};
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.data.listJahrgaenge.update_list();
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr === undefined) {
			await this.onSelect(undefined);
		} else {
			const abiturjahr = parseInt(to_params.abiturjahr as string);
			await this.onSelect(this.liste.liste.find(s => s.abiturjahr === abiturjahr));
		}
	}

	protected async onSelect(item?: GostJahrgang) {
		if (item === this.data.item.value)
			return;
		this.liste.ausgewaehlt = item;
		if (item === undefined) {
			this.data.item.value = undefined;
			await this.data.jahrgangsdaten.unselect();
			await this.data.dataFaecher.unselect();
		} else {
			this.data.item.value = item;
			await this.data.jahrgangsdaten.select(this.data.item.value);
			await this.data.dataFaecher.select(this.data.item.value);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<GostJahrgang | undefined> {
		return this.getSelectorByAbiturjahr(this.liste);
	}

	public getRoute(abiturjahr? : number | null) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { abiturjahr: abiturjahr ?? -1 }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			addAbiturjahrgang: this.data.addAbiturjahrgang,
			item: this.data.item.value,
			schule: this.data.schule,
			listJahrgaenge: this.data.listJahrgaenge
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this.data.item,
			schule: this.data.schule,
			jahrgangsdaten: this.data.jahrgangsdaten,
			dataFaecher: this.data.dataFaecher,
			listJahrgaenge: this.data.listJahrgaenge
		};
	}


	/**
     * TODO
     *
     * @returns
     */
	public getChildRouteSelector() {
		const router = useRouter();
		const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
			get:() => this.selectedChildRecord || this.defaultChild!.record,
			set: (value) => {
				this.selectedChildRecord = value;
				const abiturjahr = (this.data.item.value === undefined) ? undefined : "" + this.data.item.value.abiturjahr;
				void router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
			}
		});
		return selectedRoute;
	}

	/**
     * Eine Hilfs-Methode zum Erzeugen der beschreibaren Computed-Property für die Auswahl einer
     * Route eines Routen-Eintrags in der zugehörigen vue-Komponente.
     *
     * @param auswahl   die Liste der Auswahl
     *
     * @returns die Computed-Property
     */
	public getSelectorByAbiturjahr(auswahl: ListGost) : WritableComputedRef<GostJahrgang | undefined> {
		const router = useRouter();
		const route = useRoute();
		const name: string = this.name;
		const redirect_name: string = (this.selectedChild === undefined) ? name : this.selectedChild.name;

		const selected = computed({
			get(): GostJahrgang | undefined {
				if (route.params.abiturjahr === undefined)
					return undefined;
				let tmp = auswahl.ausgewaehlt;
				if ((tmp === undefined) || (tmp.abiturjahr.toString() !== route.params.abiturjahr))
					tmp = auswahl.liste.find(s => s.abiturjahr.toString() === route.params.abiturjahr);
				return tmp;
			},
			set(value: GostJahrgang | undefined) {
				auswahl.ausgewaehlt = value;
				const from_name = route.name?.toString() || "";
				if ((from_name !== name) && from_name?.startsWith(name)) {  // TODO Ergänze Methode bei RouteNode isNested und nutze diese
					const params = {...route.params};
					params.abiturjahr = "" + value?.abiturjahr;
					void router.push({ name: from_name, params: params });
				} else {
					void router.push({ name: redirect_name, params: { abiturjahr: value?.abiturjahr } });
				}
			}
		});
		return selected;
	}

}

export const routeGost = new RouteGost();
