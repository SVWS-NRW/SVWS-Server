// main.test.ts
import { beforeAll, describe } from "vitest";

import { registerAuthTests } from "./Auth";
import { registerEndpunkteTests } from "./Endpunkte";
import { ensureLogin } from "./Login";
import { registerSQLInjectionTests } from "./SQLInjection";
import { registerTimeBasedTests } from "./TimeBased";

describe("Teste die WeNoM-Client-API", () => {

	beforeAll(async () => {
		await ensureLogin();
	});

	describe.concurrent("Führe die Testsuites parallel aus", () => {
		registerEndpunkteTests();
		registerSQLInjectionTests();
		registerAuthTests();
		registerTimeBasedTests();
	});

});
