import { test as baseTest } from "../App.pages"
import { SchuelerAuswahlPage } from "./PageSchuelerAuswahl";


/** Erweitert die Test-Definition mit den Pages aus dem übergeordneten Verzeichnis */
export const test = baseTest.extend<{
	schuelerauswahlPage: SchuelerAuswahlPage,
}>({
	schuelerauswahlPage: async ({ page }, use) => await use(new SchuelerAuswahlPage(page)),
});


