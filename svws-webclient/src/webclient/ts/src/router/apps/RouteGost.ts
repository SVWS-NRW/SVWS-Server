import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { computed, shallowRef, ShallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteParams, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { DataGostFaecher } from "~/apps/gost/DataGostFaecher";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
import { ListGost } from "~/apps/gost/ListGost";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
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
}

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue")
const SGostApp = () => import("~/components/gost/SGostApp.vue")


export class RouteGost extends RouteNodeListView<ListGost, GostJahrgang, RouteDataGost, RouteApp> {

	public constructor() {
		super("gost", "/gost/:abiturjahr(-?\\d+)?", SGostAuswahl, SGostApp, new ListGost(), 'abiturjahr', new RouteDataGost());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Oberstufe";
		super.setView("liste", SGostAuswahl, (route) => this.getProps(route));
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
		if ((to.name === this.name) && (to_params.abiturjahr === undefined)) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			return { name: redirect_name, params: { abiturjahr: this.liste.liste.at(0)?.abiturjahr }};
		}
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		await this.liste.update_list();  // Die Auswahlliste wird als letztes geladen
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr === undefined) {
			this.onSelect(undefined);
		} else {
			const abiturjahr = parseInt(to_params.abiturjahr as string);
			this.onSelect(this.liste.liste.find(s => s.abiturjahr === abiturjahr));
		}
	}

	protected onSelect(item?: GostJahrgang) {
		if (item === this.data.item.value)
			return;
		if (item === undefined) {
			this.data.item.value = undefined;
			this.data.jahrgangsdaten.unselect();
			this.data.dataFaecher.unselect();
		} else {
			this.data.item.value = item;
			this.data.jahrgangsdaten.select(this.data.item.value);
			this.data.dataFaecher.select(this.data.item.value);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<GostJahrgang | undefined> {
		return this.getSelectorByAbiturjahr(this.liste);
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		const prop: Record<string, any> = {};
		prop.item = this.data.item;
		prop.schule = this.data.schule;
		prop.jahrgangsdaten = this.data.jahrgangsdaten;
		prop.dataFaecher = this.data.dataFaecher;
		return prop;
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
				router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
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
					router.push({ name: from_name, params: params });
				} else {
					router.push({ name: redirect_name, params: { abiturjahr: value?.abiturjahr } });
				}
			}
		});
		return selected;
	}

}

export const routeGost = new RouteGost();
