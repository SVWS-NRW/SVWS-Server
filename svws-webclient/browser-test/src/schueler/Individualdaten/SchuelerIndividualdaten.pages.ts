import { test as baseTest } from "../Schueler.pages"
import { SchuelerIndividualdatenPage } from "./PageSchuelerIndividualdaten"

/** Erweitert die Test-Definition mit den Pages aus dem übergeordneten Verzeichnis */
export const test = baseTest.extend<{
	schuelerIndividualdatenPage: SchuelerIndividualdatenPage,
}>({
	schuelerIndividualdatenPage: async ({ page }, use) => await use(new SchuelerIndividualdatenPage(page)),
})

