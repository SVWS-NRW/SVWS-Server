import type { Page } from "@playwright/test";


const login =async (page:Page) : Promise<void> => {
	await page.goto('/');
	await page.getByRole('button', { name: 'Anmelden' }).click();
}

export{ login }