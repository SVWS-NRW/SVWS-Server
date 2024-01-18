import { test as baseTest } from "../Schueler.pages"
import { PageSchuelerIndividualdaten } from "./PageSchuelerIndividualdaten"

/** Erweitert die Test-Definition mit den Pages aus dem übergeordneten Verzeichnis */
export const test = baseTest.extend<{
	schuelerIndividualdatenPage: PageSchuelerIndividualdaten,
}>({
	schuelerIndividualdatenPage: async ({ page }, use) => await use(new PageSchuelerIndividualdaten(page)),
})

