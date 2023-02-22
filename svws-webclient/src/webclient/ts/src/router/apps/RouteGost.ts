import { GostFach, GostFaecherManager, GostJahrgang, GostJahrgangsdaten, JahrgangsListeEintrag, Vector } from "@svws-nrw/svws-core-ts";
import { computed, Ref, ref, shallowRef, ShallowRef, triggerRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { ListGost } from "~/apps/gost/ListGost";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { RouteNodeListView } from "~/router/RouteNodeListView";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeGostFachwahlen } from "./gost/RouteGostFachwahlen";
import { routeGostFaecher } from "./gost/RouteGostFaecher";
import { routeGostJahrgangsdaten } from "./gost/RouteGostJahrgangsdaten";
import { routeGostKlausurplanung } from "./gost/RouteGostKlausurplanung";
import { routeGostKursplanung } from "./gost/RouteGostKursplanung";

export class RouteDataGost {
	item: ShallowRef<GostJahrgang | undefined> = shallowRef(undefined);
	jahrgangsdaten: Ref<GostJahrgangsdaten | undefined> = ref(undefined);
	faecherManager: ShallowRef<GostFaecherManager> = shallowRef(new GostFaecherManager(new Vector()));
	mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map();

	public async ladeJahrgaenge() {
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		this.mapJahrgaenge = mapJahrgaenge;
	}

	public get mapJahrgaengeOhneAbiJahrgang() : Map<number, JahrgangsListeEintrag> {
		const jahrgaengeMitAbiturjahrgang = new Set(routeGost.liste.liste.map(r => r.jahrgang));
		const map = new Map<number, JahrgangsListeEintrag>();
		for (const j of this.mapJahrgaenge.values())
			if (!jahrgaengeMitAbiturjahrgang.has(j.kuerzel))
				map.set(j.id, j);
		return map;
	}

	getFaecherManager = () => {
		return this.faecherManager.value;
	}

	patchJahrgangsdaten = async (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => {
		if (this.jahrgangsdaten.value === undefined)
			return false;
		await api.server.patchGostAbiturjahrgang(data, api.schema, abiturjahr);
		Object.assign(this.jahrgangsdaten.value, data);
		return true;
	}

	addAbiturjahrgang = async (idJahrgang: number) => {
		const abiturjahr = await api.server.createGostAbiturjahrgang(api.schema, idJahrgang);
		await routeGost.liste.update_list();
		await RouteManager.doRoute(routeGost.getRoute(abiturjahr));
	}

	patchFach = async (data: Partial<GostFach>, fach_id: number) => {
		if (this.jahrgangsdaten.value === undefined)
			return false;
		await api.server.patchGostAbiturjahrgangFach(data, api.schema, this.jahrgangsdaten.value.abiturjahr, fach_id);
		const fach = this.faecherManager.value.get(fach_id);
		if (fach !== null)
			Object.assign(fach, data);
		triggerRef(this.faecherManager);
		return true;
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
		// Lade die Liste der Jahrgänge, für welche Abiturjahrgänge ggf. angelegt werden können.
		await this.data.ladeJahrgaenge();
		// Die Auswahlliste wird als letztes geladen
		await this.liste.update_list();
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
			this.data.jahrgangsdaten.value = undefined;
			this.data.faecherManager.value = new GostFaecherManager();
		} else {
			this.data.item.value = item;
			this.data.jahrgangsdaten.value = await api.server.getGostAbiturjahrgang(api.schema, item.abiturjahr);
			const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, item.abiturjahr);
			this.data.faecherManager.value = new GostFaecherManager(listFaecher);
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
			mapJahrgaengeOhneAbiJahrgang: this.data.mapJahrgaengeOhneAbiJahrgang,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			apiStatus: api.status,
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			item: this.data.item.value
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
