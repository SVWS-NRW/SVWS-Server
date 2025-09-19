import { describe, expect, test } from "vitest";
import { privilegedApiServer } from "../../utils/APIUtils";
import { BetriebListeEintrag } from "../../../svws-webclient/core/src/core/data/betrieb/BetriebListeEintrag";

describe("Betrieb Tests2", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {

		const api = privilegedApiServer;

		test("getBetriebe", async () => {
			const result = await api.getBetriebe(schema);

			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(BetriebListeEintrag);
		});

		test("getBetriebStammdaten", async () => {
			const result = await api.getBetriebStammdaten(schema, 1);
			expect(result).toMatchSnapshot();
		});

		// TODO: FIX ME
		test.skip("patchBetriebStammdaten", async () => {
			// const result = await api.patchBetriebStammdaten(
			// 	{ hausnr: "73", branche: "Lebensmittel" },
			// 	schema,
			// 	1
			// );
			// expect(result).toBeTruthy();
		});

		// TODO: FIX ME
		test.skip("patchBeschaeftigungsart", async () => {
			// const result = await api.patchBeschaeftigungsart(schema, 1);
			// expect(result).toBeTruthy();
		});

		// TODO: FIX ME
		test.skip("patchBetriebsart", async () => {
			// const result = await api.patchBetriebsart(schema, 1);
			// expect(result).toBeTruthy();
		});
	})
})
