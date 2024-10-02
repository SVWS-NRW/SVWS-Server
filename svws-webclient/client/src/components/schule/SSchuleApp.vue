<template>
	<div class="page--flex">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ schule().bezeichnung1 }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							Schulnummer: {{ schule().schulNr }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ schule().bezeichnung2 ?? "" + " " + schule().bezeichnung3 ?? "" }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :tabs="[ tab ]" :hidden="[ false ]" :tab="tab" :set-tab="async (a) => {}">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleAppProps } from "./SSchuleAppProps";
	import type { TabData } from "@ui";

	const props = defineProps<SchuleAppProps>();

	const tab: TabData = { name: 'stammdaten', text: 'Stammdaten' };

</script>
