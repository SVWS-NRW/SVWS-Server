import type { Locator, Page} from "@playwright/test";
import { expect } from "@playwright/test";
import { api } from "../../api/Api";
import type { SchuelerListeEintrag } from "@core";

process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0';


export class SchuelerAuswahlPage {

	// Element
	rg_tabellenkopf : any;
	rg_tabelleninhalt : any;
	rg_tabellenfoot : any;
	tabelle : any;
	listeSchueler : any;
	listeKlassen : any;

	constructor(public page: Page) { }

	async init () {
		this.tabelle = this.page.getByRole('table').nth(0);
		this.rg_tabellenkopf = this.tabelle.getByRole('rowgroup').nth(0);
		this.rg_tabelleninhalt = this.tabelle.getByRole('rowgroup').nth(1);
		this.rg_tabellenfoot = this.tabelle.getByRole('rowgroup').nth(2);
	}

	async laden () {
		const connect = await api.connectTo('localhost');
		const login = await api.login('gymabi', 'Admin', '');
		this.listeSchueler = await api.server.getSchuelerAktuell(api.schema);
		this.listeKlassen = await api.getKlassenListe(1);
	}

	async getrowgroupName(rg : Locator) : Promise<string | null> {
		return await rg.getAttribute('aria-label');
	}

	async pruefeAttributAriaLabel() {
		await expect(this.tabelle).toHaveAttribute('aria-label','Tabelle');
		await expect(this.rg_tabellenkopf).toHaveAttribute('aria-label','Tabellenkopf');
		await expect(this.rg_tabelleninhalt).toHaveAttribute('aria-label','Tabelleninhalt');
		await expect(this.rg_tabellenfoot).toHaveAttribute('aria-label','Fußzeile');
	}

	async clickSchueler(eintrag : SchuelerListeEintrag) {
		const klassen_kuerzel = this.listeKlassen.get(eintrag.idKlasse)?.kuerzel;
		const idjahrgang = this.listeKlassen.get(eintrag.idKlasse)?.idJahrgang;
		const role_name = klassen_kuerzel+' '+eintrag.nachname+' '+eintrag.vorname;
		//expect(role_name).toBe("EF Addens Sophie");
		//expect(this.page.getByRole('row', { name : role_name.toString()})).toBeDefined();
		await this.page.getByRole('row', { name: role_name }).click();
	}

	public getSchuelermitJahrgang(id : number) : SchuelerListeEintrag[] {
		const liste : SchuelerListeEintrag[] = [];
		for (const eintrag of this.listeSchueler) {
			if ((id === -1) && (eintrag.status === 2))
				liste.push(eintrag);
			if ((this.listeKlassen.get(eintrag.idKlasse)?.idJahrgang === id) && (eintrag.status === 2))
				liste.push(eintrag);
		}
		return liste;
	}

}