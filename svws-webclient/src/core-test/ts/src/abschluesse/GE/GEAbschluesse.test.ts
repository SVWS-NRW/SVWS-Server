import { readFileSync, readdirSync } from "fs";
import { resolve } from "path";

import { describe, test, expect } from "vitest";

import { AbschlussManager, ServicePrognose } from "~/index";
import { GEAbschlussTestfall } from "./GEAbschlussTestfall";

const test_dir = resolve(
	__dirname,
	"../../../../../../../svws-core/src/test/resources/de/svws_nrw/abschluesse/ge/test"
);
const files = readdirSync(test_dir);

describe.each(files)("Testfall %s", (file) => {
	const data_raw = readFileSync(resolve(test_dir, file), "utf8");
	const data = JSON.parse(data_raw);
	console.log("" + file + "...");
	const testfall = new GEAbschlussTestfall(data);
	test(`Testfall ${file}`, () => {
		const abschlussBerechnung = new ServicePrognose();
		const output = abschlussBerechnung.handle(testfall.input);
		const testAbschluss = AbschlussManager.equalsAbschluesse(
			output.abschluss,
			data.Prognose.abschluss
		);
		if (testAbschluss === false) {
			console.error("" + file + " -> " + JSON.stringify(output) + "\n" + JSON.stringify(testfall.input));
		}
		expect(testAbschluss).toBe(true);
	});
});
