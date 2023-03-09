// Wir verwenden xhr2, damit wir die openapi-ts auch in Node verwenden können.
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
//@ts-ignore
global.XMLHttpRequest = require("xhr2");
import { describe, test, expect, beforeAll } from "vitest";
import { Klassen } from "~/apps/klassen/Klassen";
import { ListKlassen } from "~/apps/klassen/ListKlassen";
import { App } from "~/apps/BaseApp";
import { Schule } from "~/apps/schule/Schule";

describe("Klassen", () => {
	let instance: Klassen;
	let schule: Schule;

	beforeAll(async () => {
		App.setup({
			url: "localhost",
			username: "Admin",
			password: "",
			schema: "svwsschema"
		});
		schule = new Schule();
		await schule.init();
		instance = new Klassen();
		await instance.init();
		await instance.auswahl.update_list();
	});

	test("auswahl: liefert ein Objekt ListSchule", async () => {
		expect(instance.auswahl).toBeInstanceOf(ListKlassen);
	});
	test("klassendaten: ist definiert", async () => {
		instance.auswahl.ausgewaehlt = instance.auswahl.liste[0];
		await Promise.allSettled(instance.auswahl["_pending"]);
		expect(instance.klassendaten.daten?.kuerzel).toBeDefined();
	});
});
