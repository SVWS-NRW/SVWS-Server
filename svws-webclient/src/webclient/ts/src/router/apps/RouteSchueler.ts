import { JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, List, SchuelerListeEintrag, SchuelerStammdaten, SchuelerStatus, Schulgliederung, Vector } from "@svws-nrw/svws-core-ts";
import { computed, reactive, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { RouteNode } from "~/router/RouteNode";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { ListGost } from "~/apps/gost/ListGost";
import { routeLogin } from "../RouteLogin";
import { RouteManager } from "../RouteManager";
import { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import { Filter, SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";

export class RouteDataSchueler {
	auswahl: ShallowRef<SchuelerListeEintrag | undefined> = shallowRef(undefined);
	auswahlGruppe: ShallowRef<SchuelerListeEintrag[]> = shallowRef([]);
	_stammdaten: SchuelerStammdaten | undefined = undefined;
	listSchueler: List<SchuelerListeEintrag> = new Vector();
	mapSchueler: Map<number, SchuelerListeEintrag> = new Map();
	mapKlassen: Map<Number, KlassenListeEintrag> = new Map();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	mapKurse: Map<Number, KursListeEintrag> = new Map();
	listeAbiturjahrgaenge: ListGost = new ListGost();

	public filter = reactive({
		jahrgang: undefined,
		kurs: undefined,
		klasse: undefined,
		schulgliederung: undefined,
		status: [ SchuelerStatus.AKTIV, SchuelerStatus.EXTERN ]
	}) as Filter;

	public async ladeListe() {
		this.listSchueler = await routeLogin.data.api.getSchuelerFuerAbschnitt(routeLogin.data.schema, routeApp.data.aktAbschnitt.value.id);
		const mapSchueler = new Map<number, SchuelerListeEintrag>();
		for (const l of this.listSchueler)
			mapSchueler.set(l.id, l);
		this.mapSchueler = mapSchueler;
	}

	public async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.auswahl.value)
			return;
		if (item === undefined) {
			this.auswahl.value = undefined;
			this._stammdaten = undefined;
		} else {
			this.auswahl.value = item;
			this._stammdaten = await routeLogin.data.api.getSchuelerStammdaten(routeLogin.data.schema, item.id);
		}
	}

	get stammdaten(): SchuelerStammdaten {
		if (this._stammdaten === undefined)
			throw new Error("Unerwarteter Fehler: Schülerstammdaten nicht initialisiert");
		return this._stammdaten;
	}

	gotoSchueler = async (value: SchuelerListeEintrag | undefined) => {
		if (value === undefined) {
			await RouteManager.doRoute({ name: routeSchueler.name, params: { } });
			return;
		}
		const redirect_name: string = (routeSchueler.selectedChild === undefined) ? routeSchuelerIndividualdaten.name : routeSchueler.selectedChild.name;
		await RouteManager.doRoute({ name: redirect_name, params: { id: value.id } });
	}

	setFilter = (value: Filter) => Object.assign(this.filter, value);
	setAuswahlGruppe = (value: SchuelerListeEintrag[]) => this.auswahlGruppe.value = value;
}

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")

export class RouteSchueler extends RouteNode<RouteDataSchueler, RouteApp> {

	public constructor() {
		super("schueler", "/schueler/:id(\\d+)?", SSchuelerApp, new RouteDataSchueler());
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schüler";
		super.setView("liste", SSchuelerAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuelerIndividualdaten,
			routeSchuelerErziehungsberechtigte,
			routeSchuelerAdressen,
			routeSchuelerSchulbesuch,
			routeSchuelerAbschnitt,
			routeSchuelerLeistungen,
			routeSchuelerLaufbahnplanung,
			routeSchuelerStundenplan
		];
		super.defaultChild = routeSchuelerIndividualdaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams): Promise<any> {
		return true;
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined) {
			await this.data.ladeListe();
			if (this.data.mapSchueler.size === 0)
				// TODO Handhabung bei neuer Schule -> Schülerliste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapSchueler.values().next().value.id);
		}
		// Prüfe, ob die Schulform eine gymnasiale Oberstufe hat und lade ggf. die Abiturjahrgänge
		if (routeApp.data.schulform.value.daten.hatGymOb)
			await this.data.listeAbiturjahrgaenge.update_list();
		// aktualisiere die Klassen und erstelle Map
		const listKlassen = await routeLogin.data.api.getKlassenFuerAbschnitt(routeLogin.data.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKlassen: Map<number, KlassenListeEintrag> = new Map()
		for (const k of listKlassen)
			mapKlassen.set(k.id, k)
		this.data.mapKlassen.clear();
		this.data.mapKlassen = mapKlassen;
		// aktualisiere die Kurse und erstelle Map
		const listKurse = await routeLogin.data.api.getKurseFuerAbschnitt(routeLogin.data.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKurse: Map<number, KursListeEintrag> = new Map();
		for (const k of listKurse)
			mapKurse.set(k.id, k)
		this.data.mapKurse.clear();
		this.data.mapKurse = mapKurse;
		// aktualisiere die Jahrgänge und erstelle Map
		const listJahrgaenge = await routeLogin.data.api.getJahrgaenge(routeLogin.data.schema);
		const mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map()
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j)
		this.data.mapJahrgaenge.clear();
		this.data.mapJahrgaenge = mapJahrgaenge;
		// Die Auswahlliste wird als letztes geladen
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		if (to_params.id === undefined) {
			await this.data.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id);
			await this.data.onSelect(this.data.mapSchueler.get(id));
		}
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuelerAuswahlProps {
		return {
			auswahl: this.data.auswahl.value,
			auswahlGruppe: this.data.auswahlGruppe.value,
			listSchueler: this.data.listSchueler,
			filter: this.data.filter,
			mapKlassen: this.data.mapKlassen,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKurse: this.data.mapKurse,
			schulgliederungen: routeApp.data.schulgliederungen.value,
			abschnitte: routeApp.data.schuleStammdaten.abschnitte,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			setAbschnitt: routeApp.data.setAbschnitt,
			gotoSchueler: this.data.gotoSchueler,
			setFilter: this.data.setFilter,
			setAuswahlGruppe: this.data.setAuswahlGruppe
		};
	}

	public getProps(to: RouteLocationNormalized): SchuelerAppProps {
		return {
			auswahl: this.data.auswahl.value,
			stammdaten: this.data.stammdaten,
			mapKlassen: this.data.mapKlassen,
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

export const routeSchueler = new RouteSchueler();
