import { readFileSync } from "fs";
import { resolve } from "path";

import { describe, expect, it } from "vitest";

import { AdressenUtils } from "~/index";
import { TestdatenSplitStrasse } from "./TestdatenSplitStrasse";

const test_dir = resolve(__dirname, "../../../../../../../svws-core/src/test/resources/de/svws_nrw/core/utils/");

describe('AdressenUtils', () => {
	it('splitStrasse', () => {
		const data_raw = readFileSync(resolve(test_dir, 'TestdatenSplitStrasse.csv'), 'utf8');
		const data : Array<TestdatenSplitStrasse> = [];
		data_raw.split(/\r?\n/).forEach(line => {
			if ((line.trim() !== '') && (line.trim() !== '"strasse";"name";"hausNr";"zusatz"')) {
				const parts = line.trim().split(";");
				const daten : TestdatenSplitStrasse = new TestdatenSplitStrasse();
				for (let i = 0; i < parts.length; i++) {
					if (parts[i].slice(0, 1) === '"')
						parts[i] = parts[i].slice(1);
					if (parts[i].slice(-1) === '"')
						parts[i] = parts[i].slice(0, parts[i].length - 1);
				}
				daten.strasse = parts[0];
				daten.name = parts[1];
				daten.hausNr = parts[2];
				daten.zusatz = parts[3];
				data.push(daten);
			}
		});
		data.forEach((daten) => {
			const aufgeteilt = AdressenUtils.splitStrasse(daten.strasse);
			expect(aufgeteilt[0]).toBe(daten.name);
			expect(aufgeteilt[1]).toBe(daten.hausNr);
			expect(aufgeteilt[2]).toBe(daten.zusatz);
		});
	})
})
