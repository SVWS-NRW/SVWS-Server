import { BenutzerKompetenz, GostFach, GostFaecherManager, GostJahrgang, GostJahrgangsdaten, JahrgangsListeEintrag, List, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { computed, Ref, ref, shallowRef, ShallowRef, triggerRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw, useRouter } from "vue-router";
import { GostAppProps } from "~/components/gost/SGostAppProps";
import { GostAuswahlProps } from "~/components/gost/SGostAuswahlProps";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";
import { routeGostFachwahlen } from "./gost/RouteGostFachwahlen";
import { routeGostFaecher } from "./gost/RouteGostFaecher";
import { routeGostJahrgangsdaten } from "./gost/RouteGostJahrgangsdaten";
import { routeGostKlausurplanung } from "./gost/RouteGostKlausurplanung";
import { routeGostKursplanung } from "./gost/RouteGostKursplanung";

export class RouteDataGost {
	auswahl: ShallowRef<GostJahrgang | undefined> = shallowRef(undefined);
	_jahrgangsdaten: Ref<GostJahrgangsdaten | undefined> = ref(undefined);
	faecherManager: ShallowRef<GostFaecherManager> = shallowRef(new GostFaecherManager(new Vector()));
	listAbiturjahrgaenge: List<GostJahrgang> = new Vector();
	mapAbiturjahrgaenge: Map<number, GostJahrgang> = new Map();
	mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map();

	get jahrgangsdaten(): GostJahrgangsdaten {
		if (this._jahrgangsdaten.value === undefined)
			throw new Error("Unerwarteter Fehler: Jahrgangsdaten nicht initialisiert");
		return this._jahrgangsdaten.value;
	}

	public async ladeListe() {
		this.listAbiturjahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema);
		const mapAbiturjahrgaenge = new Map<number, GostJahrgang>();
		for (const l of this.listAbiturjahrgaenge)
			mapAbiturjahrgaenge.set(l.abiturjahr, l);
		this.mapAbiturjahrgaenge = mapAbiturjahrgaenge;
	}

	public get mapJahrgaengeOhneAbiJahrgang() : Map<number, JahrgangsListeEintrag> {
		const jahrgaengeMitAbiturjahrgang = new Set();
		for (const j of this.listAbiturjahrgaenge)
			jahrgaengeMitAbiturjahrgang.add(j.jahrgang);
		const map = new Map<number, JahrgangsListeEintrag>();
		for (const j of this.mapJahrgaenge.values())
			if (!jahrgaengeMitAbiturjahrgang.has(j.kuerzel))
				map.set(j.id, j);
		return map;
	}

	public async onSelect(item?: GostJahrgang) {
		if (item === this.auswahl.value)
			return;
		this.auswahl.value = item;
		if (item === undefined) {
			this.auswahl.value = undefined;
			this._jahrgangsdaten.value = undefined;
			this.faecherManager.value = new GostFaecherManager();
		} else {
			this.auswahl.value = item;
			this._jahrgangsdaten.value = await api.server.getGostAbiturjahrgang(api.schema, item.abiturjahr);
			const listFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema, item.abiturjahr);
			this.faecherManager.value = new GostFaecherManager(listFaecher);
		}
	}

	getFaecherManager = () => {
		return this.faecherManager.value;
	}

	patchJahrgangsdaten = async (data: Partial<GostJahrgangsdaten>, abiturjahr : number) => {
		if (this._jahrgangsdaten.value === undefined)
			return false;
		await api.server.patchGostAbiturjahrgang(data, api.schema, abiturjahr);
		Object.assign(this._jahrgangsdaten.value, data);
		return true;
	}

	addAbiturjahrgang = async (idJahrgang: number) => {
		const abiturjahr = await api.server.createGostAbiturjahrgang(api.schema, idJahrgang);
		await this.ladeListe();
		await RouteManager.doRoute(routeGost.getRoute(abiturjahr));
	}

	patchFach = async (data: Partial<GostFach>, fach_id: number) => {
		if (this._jahrgangsdaten.value === undefined)
			return false;
		await api.server.patchGostAbiturjahrgangFach(data, api.schema, this._jahrgangsdaten.value.abiturjahr, fach_id);
		const fach = this.faecherManager.value.get(fach_id);
		if (fach !== null)
			Object.assign(fach, data);
		triggerRef(this.faecherManager);
		return true;
	}

	setAbiturjahrgang = async (value: GostJahrgang | undefined) => {
		if (value === undefined || value === null) {
			await RouteManager.doRoute({ name: routeGost.name, params: { } });
			return;
		}
		const redirect_name: string = (routeGost.selectedChild === undefined) ? routeGostJahrgangsdaten.name : routeGost.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { abiturjahr: value.abiturjahr } });
	}
}

const SGostAuswahl = () => import("~/components/gost/SGostAuswahl.vue")
const SGostApp = () => import("~/components/gost/SGostApp.vue")


export class RouteGost extends RouteNode<RouteDataGost, RouteApp> {

	public constructor() {
		super(Schulform.getMitGymOb(), [ BenutzerKompetenz.KEINE ], "gost", "/gost/:abiturjahr(-?\\d+)?", SGostApp, new RouteDataGost());
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
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const redirect: RouteNode<unknown, any> = (this.selectedChild === undefined) ? this.defaultChild! : this.selectedChild;
		if (to_params.abiturjahr === undefined)
			await this.data.ladeListe();
		const abiturjahr = (to_params.abiturjahr !== undefined) ? parseInt(to_params.abiturjahr as string)
			: ((this.data.auswahl.value !== undefined) ? this.data.auswahl.value.abiturjahr : -1);
		if (redirect.hidden({ abiturjahr: "" + abiturjahr }))
			return { name: this.defaultChild!.name, params: { abiturjahr: abiturjahr }};
		return { name: redirect.name, params: { abiturjahr: abiturjahr }};
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) {
		// Lade die Liste der Jahrgänge, für welche Abiturjahrgänge ggf. angelegt werden können.
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge = new Map<number, JahrgangsListeEintrag>();
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j);
		this.data.mapJahrgaenge = mapJahrgaenge;		// Die Auswahlliste wird als letztes geladen
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.abiturjahr instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.abiturjahr === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const abiturjahr = parseInt(to_params.abiturjahr);
			await this.data.onSelect(this.data.mapAbiturjahrgaenge.get(abiturjahr));
		}
	}


	public getRoute(abiturjahr? : number | null) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { abiturjahr: abiturjahr ?? -1 }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): GostAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			mapAbiturjahrgaenge: this.data.mapAbiturjahrgaenge,
			mapJahrgaengeOhneAbiJahrgang: this.data.mapJahrgaengeOhneAbiJahrgang,
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			apiStatus: api.status,
			setAbschnitt: routeApp.data.setAbschnitt,
			addAbiturjahrgang: this.data.addAbiturjahrgang,
			setAbiturjahrgang: this.data.setAbiturjahrgang
		};
	}

	public getProps(to: RouteLocationNormalized): GostAppProps {
		return {
			auswahl: this.data.auswahl.value,
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
				const abiturjahr = (this.data.auswahl.value === undefined) ? undefined : "" + this.data.auswahl.value.abiturjahr;
				void router.push({ name: value.name, params: { abiturjahr: abiturjahr } });
			}
		});
		return selectedRoute;
	}
}

export const routeGost = new RouteGost();
