// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Lehrer } from "~/apps/lehrer/Lehrer";
import { ListLehrer } from "~/apps/lehrer/ListLehrer";
import { App } from "~/apps/BaseApp";

describe("Lehrer", () => {
	let instance: Lehrer;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Lehrer();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListLehrer);
	});
	test("stammdaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.stammdaten.daten?.nachname).toBeDefined();
	});
	test("personaldaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.personaldaten.daten?.einsatzstatus).toBeDefined();
	});
});
