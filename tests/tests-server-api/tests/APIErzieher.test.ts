import { describe, test, expect } from 'vitest'
import { privilegedApiServer } from "../../utils/APIUtils";
import { ErzieherListeEintrag } from '../../../svws-webclient/core/src/core/data/erzieher/ErzieherListeEintrag';
import { Erzieherart } from '../../../svws-webclient/core/src/core/data/erzieher/Erzieherart';

describe("Erzieher Tests", () => {
	describe.each([{schema: "GymAbi01"}])('gegen %s', ({schema}) => {
		const api = privilegedApiServer;

		test('GET: Ein ErzieherStammdaten Objekt', async () => {
			const result = await api.getErzieherStammdaten(schema, 19941);
			expect(result).toMatchSnapshot();
		})

		test('GET: Ein ErzieherStammdaten Objekt, das nicht existiert', async () => {
			await expect(api.getErzieherStammdaten(schema, 99999)).rejects.toThrowError(`Fetch failed for GET: /db/${schema}/erzieher/99999/stammdaten`);
		})

		// TODO: Fix me
		test.skip("getErzieher", async () => {
			const result = await api.getErzieher(schema);
			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(ErzieherListeEintrag);
		});

		test("getErzieherArten", async () => {
			const result = await api.getErzieherArten(schema);
			expect(result).toMatchSnapshot();
			expect(result.toArray()[0]).toBeInstanceOf(Erzieherart);
		});
	})
});
