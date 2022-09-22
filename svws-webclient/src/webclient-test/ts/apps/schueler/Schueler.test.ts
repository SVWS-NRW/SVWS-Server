// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Schueler } from "~/apps/schueler/Schueler";
import { ListSchueler } from "~/apps/schueler/ListSchueler";
import { App } from "~/apps/BaseApp";
import { Schule } from "~/apps/schule/Schule";

describe("Schueler", () => {
	let instance: Schueler;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Schueler();
		// eslint-disable-next-line @typescript-eslint/ban-ts-comment
		//@ts-ignore
		App.apps = { schule: new Schule() };
		await App.apps.schule.init();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListSchueler);
	});
	test("stammdaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.stammdaten.daten?.nachname).toBeDefined();
	});
	test.todo("schulbesuchsdaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.schulbesuchsdaten.daten?.id).toBeDefined();
	});
	test("dataGostLaufbahndaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.dataGostLaufbahndaten?.daten?.abiturjahr).toBeDefined();
	});
	test("erzieher: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.erzieher?.daten?.[0].id).toBeDefined();
	});
});
