import type { Locator, Page} from "@playwright/test";
import { expect } from "@playwright/test";
import { api } from "../../Api";
import type { GostFach, List } from "@core";
import type { Schueler } from "../DataSchueler";


interface fach_tabelle_zelle{
	text? : string,
	node? : Locator
}

interface fach_tabelle {
	kuerzel: fach_tabelle_zelle,
	bezeichnung: fach_tabelle_zelle,
	ws? : fach_tabelle_zelle,
	folge? : fach_tabelle_zelle,
	abJg? : fach_tabelle_zelle,
	ef_1? : fach_tabelle_zelle,
	ef_2? : fach_tabelle_zelle,
	q1_1? : fach_tabelle_zelle,
	q1_2? : fach_tabelle_zelle,
	q2_1? : fach_tabelle_zelle,
	q2_2? : fach_tabelle_zelle,
	abi_fach? : fach_tabelle_zelle,
}

const abiturjahr = 2022

export class SchuelerLaufbahntabellePage {
	rg_tabellenkopf : Locator;
	rg_tabelleninhalt : Locator;
	rg_tabellenfoot : Locator;
	fachbelegung = new Map<string|undefined, fach_tabelle>();
	schueler : Schueler;

	constructor(public page: Page) {
		this.rg_tabellenkopf = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(0);
		this.rg_tabelleninhalt = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(1);
		this.rg_tabellenfoot = this.page.getByRole('table').nth(1).getByRole('rowgroup').nth(2);
		this.schueler = { id:1, name:"", name2click:"", export_lp_name:"", import_lp_name:"", abiturjahr:2022, jahrgang:5}
	}

	ladeConfig(schueler: Schueler) {
		this.schueler = schueler;
	}

	async pruefeSichtbarkeit(){
		await this.rg_tabellenkopf.waitFor({state:"visible"});
		await this.rg_tabelleninhalt.waitFor({state:"visible"});
		await this.rg_tabellenfoot.waitFor({state:"visible"});
		await this.pruefeSichtbarkeit_tabellenkopf();
		await this.pruefeSichtbarkeit_tabellenfoot();
	}

	async pruefeSichtbarkeit_tabellenkopf() {
		await expect(this.page.getByRole('row', { name: 'Fachwahlen Sprachen EF Qualifikationsphase Abitur' })).toBeVisible();
		await expect(this.page.getByRole('row', { name: 'Kürzel Fach WS Folge ab JG EF.1 EF.2 Q1.1 Q1.2 Q2.1 Q2.2 Fach' })).toBeVisible();
	}

	async pruefeSichtbarkeit_tabellenfoot() {
		await expect(this.rg_tabellenfoot.getByRole('rowheader', { name: 'Anrechenbare Kurse' })).toBeVisible();
  		await expect(this.rg_tabellenfoot.getByRole('rowheader', { name: 'Wochenstunden' })).toBeVisible();
  		await expect(this.rg_tabellenfoot.getByRole('rowheader', { name: 'Durchschnitt' })).toBeVisible();
	}

	/**
	 * Erstellt aus den Zeilen der Laufbahntabelle Objekte des Typs "fach_tabelle" und speichert sie in "fachbelegung".
	 */
	async ladeFachbelegungeng_aus_Tabelle() {
		const rows = this.rg_tabelleninhalt.getByRole('row');
		await expect(rows).toHaveCount(35);
		let zeile = 0;
		for (const row of await rows.all()) {
			await row.waitFor( {state: "visible"});
			const row_texte = await row.getByRole('cell').allInnerTexts();
			const row_zelle = row.getByRole('cell');
			const fachBelegung : fach_tabelle = {
				kuerzel: { text: await row_zelle.nth(0).textContent() ?? undefined, node:  row_zelle.nth(0) },
				bezeichnung: { text:row_texte[1], node: row_zelle.nth(1) },
				ws: { text:row_texte[2], node: row.nth(2) },
				folge: { text: row_texte[3], node: row_zelle.nth(3) },
				abJg: { text:row_texte[4], node: row_zelle.nth(4) },
				ef_1: { text:row_texte[5], node: row_zelle.nth(5) },
				ef_2: { text:row_texte[6], node: row_zelle.nth(6) },
				q1_1: { text:row_texte[7], node: row_zelle.nth(7) },
			    q1_2: { text:row_texte[8], node: row_zelle.nth(8) },
			    q2_1: { text:row_texte[9], node: row_zelle.nth(9) },
				q2_2: { text:row_texte[10], node: row_zelle.nth(10) },
				abi_fach: { text:row_texte[11], node: row_zelle.nth(11) },
			};
			this.fachbelegung.set(fachBelegung.kuerzel.text,fachBelegung);
			zeile++;
		}
	}

	/**
	 * überprüft, ob alle Gostfächer von der API in der Laufbahntabelle angezeigt werden.
	 * Dazu werden die Inhalte der Objekte listGostFaecher(api) und fachbelegung(tabelle) miteinander verglichen.
	 */
	async vergleicheFaecher_API_mit_Tabelle() : Promise<void> {
		await this.ladeFachbelegungeng_aus_Tabelle();
		const listGostFaecher = await this.getGostFaechervonAPI();
		await expect(this.rg_tabelleninhalt.getByRole('row')).toHaveCount(listGostFaecher.size());

		for (const [key, value] of this.fachbelegung) {
			const gostfach = this.getGostFachmitKuerzelvonAPI(key,listGostFaecher);
			expect(gostfach?.kuerzelAnzeige).toBe(value.kuerzel.text);
			expect(gostfach?.bezeichnung).toBe(value.bezeichnung.text);
			expect(gostfach?.wochenstundenQualifikationsphase).toBe(Number(value.ws?.text));
			if ((key != undefined) && [ "E", "F", "L", "S", "S0" ].includes(key)) {
				await this.pruefeSchuelerZeile_Sprache_WebToApi(value);
			}
		}
	}

	async getGostFaechervonAPI() : Promise<List<GostFach>> {
		await api.connectTo((process.env.PLAYWRIGHT_svws_testing_api_host ?? 'https://localhost') + (process.env.PLAYWRIGHT_svws_testing_api_port != null ? (':' + process.env.PLAYWRIGHT_svws_testing_api_port) : ''));
		await api.login('gymabi', 'Admin', '');
		const listGostFaecher = await api.server.getGostAbiturjahrgangFaecher(api.schema,this.schueler.abiturjahr);
		await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
		return listGostFaecher;
	}

	getGostFachmitKuerzelvonAPI(kuerzel:string | undefined, listGostFaecher:List<GostFach>) : GostFach | undefined {
		if (listGostFaecher)
			for (const fach of listGostFaecher)
				if (fach.kuerzelAnzeige === kuerzel)
					return fach;
		return undefined;
	}

	async pruefeSchuelerZeile_Sprache_WebToApi(value : fach_tabelle) : Promise<void> {
		const daten = undefined;
		expect(this.schueler.id).toBeDefined();
		if (this.schueler.id) {
			const daten = await api.server.getGostSchuelerLaufbahnplanung(api.schema, this.schueler.id);
			const sprachen = daten.sprachendaten;
			for (const belegung of sprachen.belegungen) {
				if (belegung.sprache === value.kuerzel) {
					expect(belegung.belegungVonJahrgang).toEqual(value.abJg?.text);
					expect(belegung.reihenfolge).toEqual(Number(value.folge?.text));
				}
			}
		}
	}

	async clickZelle_EF_bis_Q2_alle_Faecher() {
		await this.ladeFachbelegungeng_aus_Tabelle();
		for (const [key, value] of this.fachbelegung) {
			if (value.kuerzel.text) {
				await this.clickZelle(value.ef_1?.node);
				await this.clickZelle(value.ef_2?.node);
				await this.clickZelle(value.q1_1?.node);
				await this.clickZelle(value.q1_2?.node);
				await this.clickZelle(value.q2_1?.node);
				await this.clickZelle(value.q2_2?.node);
			}
		}
	}

	async clickZelle(value : Locator | undefined) {
		await value?.waitFor({ state: "visible" });
		await value?.click();
	}

}