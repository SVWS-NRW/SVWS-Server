<template>
	<Story title="Layout" id="svws-ui-app-layout" group="app" icon="ri:computer-line" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Default" id="default">
			<svws-ui-app-layout>
				<template #sidebar>
					<svws-ui-menu v-if="false">
						<template #default>
							<svws-ui-menu-item active>
								<template #label>Menu Item Label</template>
								<template #icon>
									<span class="icon i-ri-team-line" />
								</template>
							</svws-ui-menu-item>
							<svws-ui-menu-item>
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
							</svws-ui-menu-item>
						</template>
						<template #footer>
							<svws-ui-menu-item class="print:!hidden"
								subline="">
								<template #label>
									Abmelden
								</template>
								<template #icon>
									<span class="icon i-ri-logout-circle-line" />
								</template>
							</svws-ui-menu-item>
						</template>
						<template #version>
							0.0.1
						</template>
						<template #metaNavigation>
							<a href="https://www.svws.nrw.de/faq/impressum">
								<svws-ui-button type="transparent">
									Impressum
								</svws-ui-button>
							</a>
							<a href="#">
								<svws-ui-button type="transparent">
									Datenschutz
								</svws-ui-button>
							</a>
						</template>
					</svws-ui-menu>
				</template>
				<template #secondaryMenu>
					<div class="h-full flex flex-col">
						<div class="secondary-menu--headline">
							<h1>Sidebar Headline</h1>
						</div>
						<div class="secondary-menu--header">
							Sidebar Header
						</div>
						<div class="secondary-menu--content">
							<div class="px-6">
								Sidebar Content
								<div>
									Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusamus aliquam, atque
									earum est, explicabo in iste laudantium maiores molestiae necessitatibus quaerat
									quod ratione reprehenderit sapiente sit sunt temporibus tenetur voluptatum!
								</div>
							</div>
						</div>
					</div>
				</template>
				<template #main>
					<svws-ui-header>
						<span>Title</span><br>
						<span class="opacity-50">Subline</span>
					</svws-ui-header>
					<svws-ui-tab-bar :tab-manager>
						Tab: {{ selectedTab.text }}
					</svws-ui-tab-bar>
				</template>
				<template #aside>
					Aside content!
				</template>
			</svws-ui-app-layout>
		</Variant>
	</Story>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { TabData } from "../nav/TabData";
	import { TabManager } from "../nav/TabManager";

	async function setTab(tab : TabData) {
		selectedTab.value = tab;
	}

	const tabManager = () => new TabManager([
		{ name: "home", text: "Home" },
		{ name: "about", text: "About" },
		{ name: "settings", text: "Settings" },
		{ name: "hidden", text: "Hidden" },
		{ name: "link1", text: "Link mit einem sehr langen Titel" },
		{ name: "link2", text: "Link mit einem sehr langen Titel 2" },
		{ name: "link3", text: "Link mit einem sehr langen Titel 3" },
	], { name: "home", text: "Home" }, setTab);

	const selectedTab = ref(tabManager().tab);

</script>
