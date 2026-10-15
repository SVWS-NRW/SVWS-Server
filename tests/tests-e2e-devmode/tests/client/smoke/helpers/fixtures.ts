import { test as base } from '@playwright/test';

// Docker-Container liefert ein Self-Signed-Zertifikat aus. Gleiches Muster
// wie im übrigen Repo (siehe z.B. tests/client/smoke/root/basic_smoketest.spec.ts,
// tests/client/smoke/bisz/alle_tabs.spec.ts, tests/client/gruppenprozesse/admin/*.spec.ts):
// dort steht jeweils ein eigenes `test.use({ ignoreHTTPSErrors: true })` pro
// Datei. Da alle Admin-Smoke-Tests dieser Suite `test`/`expect` gemeinsam aus
// dieser Datei importieren, wird der Options-Fixture hier einmalig für alle
// überschrieben statt in jeder Datei einzeln.
export const test = base.extend({
	ignoreHTTPSErrors: true,
});
export { expect } from '@playwright/test';
