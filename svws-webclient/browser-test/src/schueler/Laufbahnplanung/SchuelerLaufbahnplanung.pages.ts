import { test as baseTest } from "../Schueler.pages"
import { SSchuelerLaufbahnplanungPage } from "./PageSchuelerLaufbahnplanung"
import { SchuelerLaufbahnPage } from "./PageSchuelerLaufbahn"
import { SchuelerLaufbahntabellePage } from "./PageSchuelerLaufbahntabelle"
import { PageSvwsUiSubnav } from "./PageSvwsUiSubnav"
import { Page } from "@playwright/test"

/** Erweitert die Test-Definition mit den Pages aus dem übergeordneten Verzeichnis */
export const test = baseTest.extend<{
    schuelerlaufbahnplanungpage: SSchuelerLaufbahnplanungPage,
	schuelerLaufbahnPage: SchuelerLaufbahnPage,
	schuelerLaufbahntabellePage: SchuelerLaufbahntabellePage,
	pageSvwsUiSubnav: PageSvwsUiSubnav,

}>({
	pageSvwsUiSubnav: async ({ page }, use) => await use(new PageSvwsUiSubnav(page)),
	schuelerlaufbahnplanungpage: async ({ page }, use) => await use(new SSchuelerLaufbahnplanungPage(page)),
	schuelerLaufbahnPage: async ({ page }, use) => await use(new SchuelerLaufbahnPage(page)),
	schuelerLaufbahntabellePage: async ({ page }, use) => await use(new SchuelerLaufbahntabellePage(page)),
})

