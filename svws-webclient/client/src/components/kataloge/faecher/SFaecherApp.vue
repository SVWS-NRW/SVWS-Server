<template>
	<template v-if="fach !== undefined">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ fach.bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ fach.id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-40">{{ fach.kuerzel }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<i-ri-archive-line />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { FaecherAppProps } from "./SFaecherAppProps";
	import { type FaecherListeEintrag } from "@core";

	const props = defineProps<FaecherAppProps>();

	const fach = computed<FaecherListeEintrag | undefined>(() => props.auswahl());

</script>
