// main.test.ts
import { beforeAll, describe } from "vitest";
import { ensureLogin } from "./Login";
import { registerEndpunkteTests } from "./Endpunkte";
import { registerSQLInjectionTests } from "./SQLInjection";
import { registerAuthTests } from "./Auth";
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
