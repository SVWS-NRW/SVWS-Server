import { readFileSync } from "node:fs";
import { resolve } from "node:path";

import { describe, expect, it } from "vitest";
import { AdressenUtils } from "../../../src/core/utils/AdressenUtils";


const test_dir = resolve(__dirname, "../../../../../svws-core/src/test/resources/de/svws_nrw/core/utils/");

describe('AdressenUtils', () => {
	it('splitStrasse', () => {
		const data_raw = readFileSync(resolve(test_dir, 'TestdatenSplitStrasse.csv'), 'utf8');
		const data: Array<TestdatenSplitStrasse> = [];
		data_raw.split(/\r?\n/).forEach(line => {
			if ((line.trim() !== '') && (line.trim() !== '"strasse";"nameTrimmed";"hausNrTrimmed";"zusatzTrimmed";"name";"hausNr";"zusatz"')) {
				const parts = line.trim().split(";");
				const daten: TestdatenSplitStrasse = new TestdatenSplitStrasse();
				for (let i = 0; i < parts.length; i++) {
					if (parts[i].startsWith('"')) {
						parts[i] = parts[i].slice(1);
					}
					if (parts[i].endsWith('"')) {
						parts[i] = parts[i].slice(0, -1);
					}
				}
				daten.strasse = parts[0];
				daten.nameTrimmed = parts[1];
				daten.hausNrTrimmed = parts[2];
				daten.zusatzTrimmed = parts[3];
				daten.name = parts[4];
				daten.hausNr = parts[5];
				daten.zusatz = parts[6];
				data.push(daten);
			}
		});
		data.forEach((daten) => {
			const aufgeteilt = AdressenUtils.splitStrasse(daten.strasse);
			expect(aufgeteilt[0]).toBe(daten.name);
			expect(aufgeteilt[1]).toBe(daten.hausNr);
			expect(aufgeteilt[2]).toBe(daten.zusatz);
		});

		data.forEach((daten) => {
			const aufgeteilt = AdressenUtils.splitAndTrimStrasse(daten.strasse);
			expect(aufgeteilt[0]).toBe(daten.nameTrimmed);
			expect(aufgeteilt[1]).toBe(daten.hausNrTrimmed);
			expect(aufgeteilt[2]).toBe(daten.zusatzTrimmed);
		});
	});
});

/**
 * DTO-Klasse für das einlesen von Testdaten aus
 * einer CSV-Resource für das Testen des Aufteilens
 * von Strassennamen.
 */
class TestdatenSplitStrasse {

	/** Die Strasseninformation als ein kombinierter String */
	public strasse: string = "";

	/** Der Namensteil der Strasseninformation */
	public name: string = "";

	/** Der Namensteil der Strasseninformation getrimmt */
	public nameTrimmed: string = "";

	/** Der Teil mit der Hausnummer der Strasseninformation */
	public hausNr: string = "";

	/** Der Teil mit der Hausnummer der Strasseninformation getrimmt */
	public hausNrTrimmed: string = "";

	/** Der Teil mit dem Hausnummerzusatz der Strasseninformation */
	public zusatz: string = "";

	/** Der Teil mit dem Hausnummerzusatz der Strasseninformation getrimmt */
	public zusatzTrimmed: string = "";

}
