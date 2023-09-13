import { expect, type Page } from "@playwright/test";

export default class SchuelerIndividualdatenPage {

	constructor(public page: Page) {}

	async fillContentCardAllgemein() {
		await this.fillNachname("Mustermann");
		await this.fillRufname("Max");
		await this.fillZusatz("Dr.");
		await this.fillAlleVornamen("Max Moritz");
	}

	async fillNachname(value: string) {
		await this.page.getByLabel('Nachname').fill(value);
	}

	async fillRufname(value: string) {
		await this.page.getByLabel('Rufname').fill(value);
	}

	async fillZusatz(value: string) {
		await this.page.getByLabel('Zusatz').fill(value);
	}

	async fillAlleVornamen(value: string) {
		await this.page.getByLabel('Alle Vornamen').fill(value);
	}

}