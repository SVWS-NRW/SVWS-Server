<script setup lang="ts">
	import {h, ref} from "vue";
	import {hstEvent} from 'histoire/client';

	import SvwsUiAppLayout from "./AppLayout.vue";
	import SvwsUiSecondaryMenu from "../Sidebar/SecondaryMenu.vue";
	import SvwsUiSidebarMenu from "../Sidebar/Menu.vue";
	import SvwsUiSidebarMenuHeader from "../Sidebar/MenuHeader.vue";
	import SvwsUiSidebarMenuItem from "../Sidebar/MenuItem.vue";
	import SvwsUiHeader from "../Header.vue";

	const routes = [
		{ path: "/", name: "home", component: { render: () => h("h1", "Home") }, meta: { text: "Home" } },
		{ path: "/about", name: "about", component: { render: () => h("h1", "About") }, meta: { text: "About" } },
		{
			path: "/settings",
			name: "settings",
			component: { render: () => h("h1", "Settings") },
			meta: { text: "Settings" }
		},
		{ path: "/hidden", name: "hidden", component: { render: () => h("h1", "Hidden") }, meta: { text: "Hidden" } },
		{ path: "/link1", name: "link1", component: { render: () => h("h1", "link1") }, meta: { text: "Link mit einem sehr langen Titel" } },
		{ path: "/link2", name: "link2", component: { render: () => h("h1", "link2") }, meta: { text: "Link mit einem sehr langen Titel 2" } },
		{ path: "/link3", name: "link3", component: { render: () => h("h1", "link3") }, meta: { text: "Link mit einem sehr langen Titel 3" } }
	];

	const hidden = ref([false, false, false, true]);
	const selectedRoute = ref(routes[0]);

</script>

<template>
	<Story title="SVWS UI/Layout/Pages/App">
		<svws-ui-app-layout>
			<template #sidebar>
				<svws-ui-sidebar-menu
					class="print:hidden">
					<template #header>
						<svws-ui-sidebar-menu-header>
							A
						</svws-ui-sidebar-menu-header>
					</template>
					<template #default>
						<svws-ui-sidebar-menu-item active>
							<template #label>Menu Item Label</template>
							<template #icon>
								<i-ri-team-line />
							</template>
						</svws-ui-sidebar-menu-item>
						<svws-ui-sidebar-menu-item>
							<template #label>Menu Item Label</template>
							<template #icon>
								<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="1.2em" height="1.2em">
									<g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"
										stroke-width="2">
										<path
											d="m2.573 8.463l8.659-4.329a.6.6 0 0 1 .536 0l8.659 4.33a.6.6 0 0 1 0 1.073l-8.659 4.329a.6.6 0 0 1-.536 0l-8.659-4.33a.6.6 0 0 1 0-1.073Z" />
										<path
											d="M22.5 13V9.5l-2-1m-16 2v5.412a2 2 0 0 0 1.142 1.807l5 2.374a2 2 0 0 0 1.716 0l5-2.374a2 2 0 0 0 1.142-1.807V10.5" />
									</g>
								</svg>
							</template>
						</svws-ui-sidebar-menu-item>
					</template>
					<template #footer>
						<svws-ui-sidebar-menu-item class="print:hidden"
							subline="">
							<template #label>
								Abmelden
							</template>
							<template #icon>
								<i-ri-logout-circle-line />
							</template>
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
							<div>
								Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
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
				<svws-ui-header>
					<span>Title</span><br>
					<span class="opacity-50">Subline</span>
				</svws-ui-header>
				<RouterTabBar :routes="routes" :hidden="hidden" v-model="selectedRoute">
					<component :is="selectedRoute.component" />
				</RouterTabBar>
				<!--TODO bzw. WIP, das muss noch aufgeräumt werden-->
				<Notification>Test</Notification>
				<!--<Notification type="success">Success!</Notification>
				<Notification type="highlight">Hinweis</Notification>
				<Notification type="error">
					<div class="py-1 mb-1">Error Notification</div>
					<div class="font-normal">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab at commodi cumque, esse eum fugit hic inventore magnam, minus nihil possimus praesentium ratione repellendus soluta temporibus vitae voluptatibus. Aspernatur, itaque!</div>
					<svws-ui-button type="transparent" class="inline-flex items-center -ml-2 my-2"><i-ri-bug-line/>Fehler melden</svws-ui-button>
				</Notification>-->
			</template>
		</svws-ui-app-layout>
	</Story>
</template>
