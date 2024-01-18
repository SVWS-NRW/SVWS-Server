import type { LehrerListeEintrag } from "@core";
import { api } from "~/Api";
import { type Page } from "@playwright/test";
import { expect } from "@playwright/test";
import type ErrorPage from "../../PageError";
import type { Schueler } from "../DataSchueler";
import { dataLehrerAuswahl } from "~/lehrer/DataLehrerAuswahl";


export type beratungseintrag = {
	lehrer2click : string,
	datum : string,
	kommentar : string,
}
export class PageSLaufbahnplanungCardBeratung {

	public schueler : Schueler | undefined;
	public lehrer : LehrerListeEintrag | undefined;
	public eintrag : beratungseintrag ;

	constructor (public page: Page) {
		this.eintrag ={
			lehrer2click : '',
			datum : '',
			kommentar : ''
		}
	}

	async laden(){
		this.lehrer = await dataLehrerAuswahl.getZufallslehrer();

		this.eintrag ={
			lehrer2click : this.lehrer?.kuerzel+' ('+this.lehrer?.vorname+' '+this.lehrer?.nachname+')',
			//lehrer2click : 'FONC (Katharina Fonck)',
			datum : '2023-08-10',
			kommentar : 'Testkommentr asdf asdf asdf'
		}
	}

	async testeEingabeBeratung() {
		await this.laden();
		await this.page.waitForTimeout(1000);
		await this.pruefeLabelBeratung();
		await this.clickBerater();
		await this.fillBeratungsdatum();
		await this.fillKommter();
		await this.clickBeratugspeichen();

	}

	private async pruefeLabelBeratung() {
		await this.page.locator("//h3[@title='Beratung']").waitFor({state:"visible"});
		expect((await this.page.locator("//h3[@title='Beratung']").innerText()).valueOf()).toBe("Beratung");
	}

	private async clickBerater() {
		await this.page.getByLabel('Zuletzt beraten von').click();
		// TODO Tests mit den Lehren von API
		await this.page.getByText(this.eintrag?.lehrer2click).click();
	}

	private async fillBeratungsdatum() {
		await this.page.getByLabel('Datum').waitFor({state:"visible"});
		await this.page.getByLabel('Datum').fill(this.eintrag.datum);
	}

	private async fillKommter() {
		await this.page.getByLabel('Kommentar').waitFor({state:"visible"});
		await this.page.getByLabel('Kommentar').fill(this.eintrag.kommentar);
	}

	private async clickBeratugspeichen() {
		await this.page.getByRole('button', { name: 'Beratungsdaten speichern' }).click();
	}

	public async testeAPI_SpeicherungBeratung(id : number)
	{
		await this.page.waitForTimeout(1000);
		const gostLaufbahnBeratungsdaten = await api.server.getGostSchuelerLaufbahnplanungBeratungsdaten(api.schema, id);
		expect(this.lehrer?.id).toBe(gostLaufbahnBeratungsdaten.beratungslehrerID);
		expect(this.eintrag.datum).toBe(gostLaufbahnBeratungsdaten.beratungsdatum);
		expect(this.eintrag.kommentar).toBe(gostLaufbahnBeratungsdaten.kommentar);
	}
}