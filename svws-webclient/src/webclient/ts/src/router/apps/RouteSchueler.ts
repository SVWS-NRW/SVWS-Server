import { BenutzerKompetenz, GostJahrgang, JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, List, SchuelerListeEintrag, SchuelerStammdaten, SchuelerStatus, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { computed, reactive, ShallowRef, shallowRef, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams, RouteRecordRaw } from "vue-router";
import { SchuelerAppProps } from "~/components/schueler/SSchuelerAppProps";
import { Filter, SchuelerAuswahlProps } from "~/components/schueler/SSchuelerAuswahlProps";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { RouteNode } from "~/router/RouteNode";
import { api } from "../Api";
import { RouteManager } from "../RouteManager";

export class RouteDataSchueler {
	auswahl: ShallowRef<SchuelerListeEintrag | undefined> = shallowRef(undefined);
	auswahlGruppe: ShallowRef<SchuelerListeEintrag[]> = shallowRef([]);
	_stammdaten: SchuelerStammdaten | undefined = undefined;
	listSchueler: List<SchuelerListeEintrag> = new Vector();
	mapSchueler: Map<number, SchuelerListeEintrag> = new Map();
	mapKlassen: Map<Number, KlassenListeEintrag> = new Map();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	mapKurse: Map<Number, KursListeEintrag> = new Map();
	mapAbiturjahrgaenge: Map<number, GostJahrgang> = new Map();

	public filter = reactive({
		jahrgang: undefined,
		kurs: undefined,
		klasse: undefined,
		schulgliederung: undefined,
		status: [ SchuelerStatus.AKTIV, SchuelerStatus.EXTERN ]
	}) as Filter;

	public async ladeListe() {
		this.listSchueler = await api.server.getSchuelerFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
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
			this._stammdaten = await api.server.getSchuelerStammdaten(api.schema, item.id);
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
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schueler", "/schueler/:id(\\d+)?", SSchuelerApp, new RouteDataSchueler());
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
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		if (id === undefined) {
			await this.data.ladeListe();
			if (this.data.mapSchueler.size === 0)
				// TODO Handhabung bei neuer Schule -> Schülerliste leer
				return this.getRoute(-1);
			return this.getRoute(this.data.mapSchueler.values().next().value.id);
		}
		// Prüfe, ob die Schulform eine gymnasiale Oberstufe hat und lade ggf. die Abiturjahrgänge
		if (api.schulform.daten.hatGymOb) {
			const listAbiturjahrgaenge = await api.server.getGostAbiturjahrgaenge(api.schema)
			const mapAbiturjahrgaenge = new Map<number, GostJahrgang>();
			for (const j of listAbiturjahrgaenge)
				mapAbiturjahrgaenge.set(j.abiturjahr, j);
			this.data.mapAbiturjahrgaenge = mapAbiturjahrgaenge;
		}
		// aktualisiere die Klassen und erstelle Map
		const listKlassen = await api.server.getKlassenFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKlassen: Map<number, KlassenListeEintrag> = new Map()
		for (const k of listKlassen)
			mapKlassen.set(k.id, k)
		this.data.mapKlassen = mapKlassen;
		// aktualisiere die Kurse und erstelle Map
		const listKurse = await api.server.getKurseFuerAbschnitt(api.schema, routeApp.data.aktAbschnitt.value.id);
		const mapKurse: Map<number, KursListeEintrag> = new Map();
		for (const k of listKurse)
			mapKurse.set(k.id, k)
		this.data.mapKurse = mapKurse;
		// aktualisiere die Jahrgänge und erstelle Map
		const listJahrgaenge = await api.server.getJahrgaenge(api.schema);
		const mapJahrgaenge: Map<number, JahrgangsListeEintrag> = new Map()
		for (const j of listJahrgaenge)
			mapJahrgaenge.set(j.id, j)
		this.data.mapJahrgaenge = mapJahrgaenge;
		// Die Auswahlliste wird als letztes geladen
		await this.data.ladeListe();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id instanceof Array)
			throw new Error("Fehler: Die Parameter der Route dürfen keine Arrays sein");
		const id = !to_params.id ? undefined : parseInt(to_params.id);
		await this.data.onSelect((id === undefined) ? undefined : this.data.mapSchueler.get(id));
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
			schulgliederungen: api.schulgliederungen,
			abschnitte: api.mapAbschnitte.value,
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
			stammdaten: this.data.auswahl.value === undefined ? undefined : this.data.stammdaten,
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
