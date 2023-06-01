<template>
	<div v-if="visible" class="page--flex">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">{{ bezeichnung }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID:
					{{ id }}
				</svws-ui-badge>
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
	import type { BenutzergruppeAppProps } from "./SBenutzergruppeAppProps";

	const props = defineProps<BenutzergruppeAppProps>();

	const id: ComputedRef<string> = computed(() => "" + props.auswahl()?.id ?? "?");
	const bezeichnung: ComputedRef<string> = computed(() => props.auswahl()?.bezeichnung ?? "–");

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
