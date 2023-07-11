import { test, expect } from '@playwright/test';


test.describe("navigation", () => {

	test("main navigation", async ({ page }) => {
		await page.goto('/');

		await page.getByText('Anmelden').click();
		await page.getByRole('row', { name: '09a Berg Eric' }).getByRole('cell', { name: '09a' }).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();
		await page.getByRole('row', { name: '09a Berg Eric' }).click(); //.getByRole('cell', { name: '09a' }).click();
		await page.getByRole('button', { name: 'Laufbahnplanung' }).click();

		const anzahl_faecher = await page.getByRole('table').nth(1).getByRole('row').count().then(function(result){ console.log(result); return result;});
		console.log(anzahl_faecher);
		await expect(page.getByRole('table').nth(1).getByRole('row')).toHaveCount(40);
		const table = page.getByRole('table').nth(1);



		const vorher = await page.getByRole('table').nth(1).getByRole('rowgroup').nth(1).getByRole('row').nth(0).getByRole('cell').nth(6).innerText().then(function(result){
			console.log(result);
			return result;
		});
		await page.getByRole('table').nth(1).getByRole('rowgroup').nth(1).getByRole('row').nth(0).getByRole('cell').nth(6).click();
		const nachher = await page.getByRole('table').nth(1).getByRole('rowgroup').nth(1).getByRole('row').nth(0).getByRole('cell').nth(6).innerText().then(function(result){
			return result;
		});

		console.log( vorher +"--"+nachher);

		// const deutsch = zeile_d.find

		// for (const row of await table_body.getByRole('row').all()){
		//       console.log(await row.textContent());
		//       const zellen = await.getR
		//       for(const zelle of await row.getByRole('cell').all()){
		//         console.log(await zelle.textContent());
		//         console.log(await)
		//       }
		// }


		// await Promise.all([
		//   page.getByText('Anmelden').click(),
		//   page.getByRole('row', { name: '09a Beyer Benjamin' }).getByRole('cell', { name: '09a' }).click(),
		//   page.getByRole('button', { name: 'Laufbahnplanung' }).click(),
		//   page.getByRole('row', { name: 'S0 Spanisch ab EF 4 — S S S S S M' }).getByText('S', { exact: true }).nth(4).click()


		// ]);





	});

});