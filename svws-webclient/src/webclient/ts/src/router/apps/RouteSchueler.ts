import { JahrgangsListeEintrag, KlassenListeEintrag, KursListeEintrag, List, SchuelerListeEintrag, Schulform, Vector } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import { DataSchuelerStammdaten } from "~/apps/schueler/DataSchuelerStammdaten";
import { RouteNodeListView } from "../RouteNodeListView";
import { routeSchuelerAbschnitt } from "~/router/apps/schueler/RouteSchuelerAbschnitt";
import { routeSchuelerAdressen } from "~/router/apps/schueler/RouteSchuelerAdressen";
import { routeSchuelerErziehungsberechtigte } from "~/router/apps/schueler/RouteSchuelerErziehungsberechtigte";
import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/RouteSchuelerIndividualdaten";
import { routeSchuelerLaufbahnplanung } from "~/router/apps/schueler/RouteSchuelerLaufbahnplanung";
import { routeSchuelerLeistungen } from "~/router/apps/schueler/RouteSchuelerLeistungen";
import { routeSchuelerSchulbesuch } from "~/router/apps/schueler/RouteSchuelerSchulbesuch";
import { routeSchuelerStundenplan } from "~/router/apps/schueler/RouteSchuelerStundenplan";
import { ListSchueler } from "~/apps/schueler/ListSchueler";
import { RouteNode } from "~/router/RouteNode";
import { ListJahrgaenge } from "~/apps/kataloge/jahrgaenge/ListJahrgaenge";
import { ListKurse } from "~/apps/kurse/ListKurse";
import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
import { routeApp, RouteApp } from "~/router/RouteApp";
import { ListGost } from "~/apps/gost/ListGost";
import { routeLogin } from "../RouteLogin";

export class RouteDataSchueler {
	stammdaten: DataSchuelerStammdaten = new DataSchuelerStammdaten();
	schule: DataSchuleStammdaten = new DataSchuleStammdaten();
	mapKlassen: Map<Number, KlassenListeEintrag> = new Map();
	mapJahrgaenge: Map<Number, JahrgangsListeEintrag> = new Map();
	mapKurse: Map<Number, KursListeEintrag> = new Map();
	listeAbiturjahrgaenge: ListGost = new ListGost();
}

const SSchuelerAuswahl = () => import("~/components/schueler/SSchuelerAuswahl.vue")
const SSchuelerApp = () => import("~/components/schueler/SSchuelerApp.vue")

export class RouteSchueler extends RouteNodeListView<ListSchueler, SchuelerListeEintrag, RouteDataSchueler, RouteApp> {

	public constructor() {
		super("schueler", "/schueler/:id(\\d+)?", SSchuelerAuswahl, SSchuelerApp, new ListSchueler(), 'id', new RouteDataSchueler());
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

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<any> {
		if (to_params.id === undefined) {
			const redirect_name: string = (this.selectedChild === undefined) ? this.defaultChild!.name : this.selectedChild.name;
			await this.liste.update_list();
			let id = this.liste.gefiltert[0]?.id;
			// TODO Handhabung bei neuer Schule -> Schülerliste leer
			if (id === undefined)
				id = -1;
			return this.getRoute(id);
		}
		await this.data.schule.select(true);  // undefined würde das laden verhindern, daher true
		// TODO Code für Schulform mit Fehlerhandling in die zukünftige API-Klasse auslagern
		const schulform : Schulform | undefined = this.data.schule.schulform.value;
		if (schulform === undefined)
			throw new Error("Schulform unbekannt.");
		if (schulform?.daten.hatGymOb)
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
		await this.liste.update_list();
	}

	protected async update(to: RouteNode<unknown, any>, to_params: RouteParams) {
		if (to_params.id === undefined) {
			await this.onSelect(undefined);
		} else {
			const id = parseInt(to_params.id as string);
			await this.onSelect(this.liste.liste.find(s => s.id === id));
		}
	}

	protected async onSelect(item?: SchuelerListeEintrag) {
		if (item === this.item)
			return;
		if (item === undefined) {
			this.item = undefined;
			await this.data.stammdaten.unselect();
		} else {
			this.item = item;
			await this.data.stammdaten.select(this.item);
		}
	}

	protected getAuswahlComputedProperty(): WritableComputedRef<SchuelerListeEintrag | undefined> {
		return this.getSelector();
	}

	public getRoute(id?: number) : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { id: id }};
	}

	public getAuswahlProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			mapKlassen: this.data.mapKlassen,
			mapJahrgaenge: this.data.mapJahrgaenge,
			mapKurse: this.data.mapKurse,
			schulgliederungen: this.data.schule.schulgliederungen.value,
			abschnitte: this.data.schule.daten?.abschnitte || new Vector(),
			aktAbschnitt: routeApp.data.aktAbschnitt,
			setAbschnitt: routeApp.data.setAbschnitt
		};
	}

	public getProps(to: RouteLocationNormalized): Record<string, any> {
		return {
			...super.getProps(to),
			stammdaten: this.data.stammdaten,
			mapKlassen: this.data.mapKlassen,
		};
	}

}

export const routeSchueler = new RouteSchueler();
