import { test as baseTest } from "../Schueler.pages"
import { SSchuelerLaufbahnplanungPage } from "./PageSchuelerLaufbahnplanung"
import { PageSLaufbahnplanungCardStatus } from "./PageSLaufbahnplanungCardStatus"
import { SchuelerLaufbahntabellePage } from "./PageSchuelerLaufbahntabelle"
import { PageSvwsUiSubnav } from "./PageSvwsUiSubnav"
import { Page } from "@playwright/test"
import { PageSLaufbahnplanungCardBeratung } from "./PageSLaufbahnplanungCardBeratung"

/** Erweitert die Test-Definition mit den Pages aus dem übergeordneten Verzeichnis */
export const test = baseTest.extend<{
    schuelerlaufbahnplanungpage: SSchuelerLaufbahnplanungPage,
	pageSLaufbahnplanungCardStatus: PageSLaufbahnplanungCardStatus,
	schuelerLaufbahntabellePage: SchuelerLaufbahntabellePage,
	pageSvwsUiSubnav: PageSvwsUiSubnav,
	pageSLaufbahnplanungCardBeratung: PageSLaufbahnplanungCardBeratung,

}>({
	pageSvwsUiSubnav: async ({ page }, use) => await use(new PageSvwsUiSubnav(page)),
	schuelerlaufbahnplanungpage: async ({ page }, use) => await use(new SSchuelerLaufbahnplanungPage(page)),
	pageSLaufbahnplanungCardStatus: async ({ page }, use) => await use(new PageSLaufbahnplanungCardStatus(page)),
	schuelerLaufbahntabellePage: async ({ page }, use) => await use(new SchuelerLaufbahntabellePage(page)),
	pageSLaufbahnplanungCardBeratung: async ({ page }, use) => await use(new PageSLaufbahnplanungCardBeratung(page)),
})

