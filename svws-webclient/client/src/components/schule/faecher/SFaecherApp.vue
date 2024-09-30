<template>
	<template v-if="fach !== undefined">
		<header class="svws-ui-header">
			<div class="svws-ui-header--title">
				<div class="svws-headline-wrapper">
					<h2 class="svws-headline">
						<span>{{ fach.bezeichnung }}</span>
						<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
							ID: {{ fach.id }}
						</svws-ui-badge>
					</h2>
					<span class="svws-subline">{{ fach.kuerzel }}</span>
				</div>
			</div>
			<div class="svws-ui-header--actions" />
		</header>
		<svws-ui-router-tab-bar :tabs="tabs" :hidden="tabsHidden" :tab="tab" :set-tab="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAppProps } from "./SFaecherAppProps";
	import type { FachDaten } from "@core";

	const props = defineProps<FaecherAppProps>();

	const fach = computed<FachDaten | undefined>(() => props.fachListeManager().auswahl());

</script>
