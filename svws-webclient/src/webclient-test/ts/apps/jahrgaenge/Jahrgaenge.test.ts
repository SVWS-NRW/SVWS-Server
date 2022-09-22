// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Jahrgaenge } from "~/apps/jahrgaenge/Jahrgaenge";
import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";
import { App } from "~/apps/BaseApp";

describe("Jahrgaenge", () => {
	let instance: Jahrgaenge;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		instance = new Jahrgaenge();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListJahrgaenge);
	});
	test("jahrgangsdaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.jahrgangsdaten.daten?.kuerzel).toBeDefined();
	});
});
