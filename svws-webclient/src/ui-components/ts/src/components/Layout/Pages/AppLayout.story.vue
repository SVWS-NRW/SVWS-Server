<script setup lang="ts">
import {ref} from "vue";
import {hstEvent} from 'histoire/client';

import SvwsUiAppLayout from "./AppLayout.vue";
import SvwsUiSecondaryMenu from "../Sidebar/SecondaryMenu.vue";
import SvwsUiSidebarMenu from "../Sidebar/Menu.vue";
import SvwsUiSidebarMenuHeader from "../Sidebar/MenuHeader.vue";
import SvwsUiSidebarMenuItem from "../Sidebar/MenuItem.vue";
import SvwsUiHeader from "../Header.vue";
import SvwsUiOverlay from "../Modal/Overlay.vue";
import SvwsUiBadge from "../../Typography/Badge.vue";
import SvwsUiTabBar from "../../Controls/TabBar.vue";
import SvwsUiTabPanel from "../../Controls/TabPanel.vue";

</script>

<template>
	<Story title="SVWS UI/Layout/Pages/App">
		<svws-ui-app-layout>
			<template #sidebar>
				<svws-ui-sidebar-menu
					class="print:hidden"
				>
					<template #header>
						<svws-ui-sidebar-menu-header>
							A
						</svws-ui-sidebar-menu-header>
					</template>
					<template #default>
						<svws-ui-sidebar-menu-item active>
							<template #label>Menu Item Label</template>
							<template #icon>
								<i-ri-team-line/>
							</template>
						</svws-ui-sidebar-menu-item>
						<svws-ui-sidebar-menu-item>
							<template #label>Menu Item Label</template>
							<template #icon>
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
									<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
									   stroke-width="2">
										<path
											d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z"/>
										<path
											d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5"/>
									</g>
								</svg>
							</template>
						</svws-ui-sidebar-menu-item>
					</template>
					<template #footer>
						<svws-ui-sidebar-menu-item
							class="print:hidden"
							subline=""
						>
							<template #label>Abmelden
							</template
							>
							<template #icon>
								<i-ri-logout-circle-line/>
							</template
							>
						</svws-ui-sidebar-menu-item>
					</template>
					<template #version>
						v0.0.1
					</template>
					<template #metaNavigation>
						<a href="#">Hilfe</a>
						<a href="https://www.svws.nrw.de/faq/impressum">Impressum</a>
						<a href="#">Datenschutz</a>
					</template>
				</svws-ui-sidebar-menu>
			</template>
			<template #secondaryMenu>
				<svws-ui-secondary-menu>
					<template #headline>Sidebar Headline</template>
					<template #header>
						Sidebar Header
					</template>
					<template #content>
						<div class="px-6">
							Sidebar Content
							<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
								earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
								quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
							</div>
						</div>
					</template>
				</svws-ui-secondary-menu>
			</template>
			<template #main>
				<!--<div class="app-layout--main--placeholder">
					<i-ri-archive-line/>
				</div>-->
				<svws-ui-overlay class="hidden"/>
				<svws-ui-header>
					<span>Title</span><br/>
					<span class="opacity-50">Subline</span>
				</svws-ui-header>
				<svws-ui-tab-bar>
					<template #tabs>
						<svws-ui-tab-button>Individualdaten</svws-ui-tab-button>
						<svws-ui-tab-button>Erziehungsberechtigte</svws-ui-tab-button>
						<svws-ui-tab-button>Adressen / Betriebe</svws-ui-tab-button>
						<svws-ui-tab-button>Schulbesuch</svws-ui-tab-button>
						<svws-ui-tab-button>Aktuelles Halbjahr</svws-ui-tab-button>
						<svws-ui-tab-button>Leistungsdaten</svws-ui-tab-button>
						<svws-ui-tab-button hidden>Laufbahnplanung</svws-ui-tab-button>
						<svws-ui-tab-button>Stundenplan</svws-ui-tab-button>
					</template>
					<template #panels>
						<svws-ui-tab-panel>
							<s-schueler-individualdaten />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-erziehungsberechtigte />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-adressen />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-schulbesuch />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-halbjahr />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-leistungsdaten />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel hidden>
							<s-schueler-laufbahnplanung />
						</svws-ui-tab-panel>
						<svws-ui-tab-panel>
							<s-schueler-stundenplan />
						</svws-ui-tab-panel>
					</template>
				</svws-ui-tab-bar>
				<div class="px-6">
					<div>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
						earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
						quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
					</div>
				</div>
			</template>
		</svws-ui-app-layout>
	</Story>
</template>
