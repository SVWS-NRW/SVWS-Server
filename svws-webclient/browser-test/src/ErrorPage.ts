import type { Page } from "@playwright/test";

export default class ErrorPage{

	constructor(public page: Page){}

	async isVisible(){
		return await this.page.getByText('Error').isVisible()
	}

	async getMessage():Promise<string[]>{
		return await this.page.locator("//div[text()='Error ']/following-sibling::div").allInnerTexts();
	}

}
