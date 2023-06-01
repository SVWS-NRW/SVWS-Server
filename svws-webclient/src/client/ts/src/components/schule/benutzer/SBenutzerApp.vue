<template>
	<div v-if="visible" class="page--flex">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ anzeigename }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID:
					{{ id }}
				</svws-ui-badge>
			</div>
			<div>
				<span class="opacity-50">{{ name }}</span>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" @update:model-value="setTab" :model-value="tab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-school-line />
	</div>
</template>

<script setup lang="ts">

	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	import type { BenutzerAppProps } from "./SBenutzerAppProps";

	const props = defineProps<BenutzerAppProps>();

	const id: ComputedRef<number | string> = computed(() => props.auswahl()?.id ?? "?");
	const anzeigename: ComputedRef<string> = computed(() => props.auswahl()?.anzeigename ?? "---");
	const name: ComputedRef<string> = computed(() => props.auswahl()?.name ?? "---");

	const visible: ComputedRef<boolean> = computed(() => props.auswahl() !== undefined);

</script>
