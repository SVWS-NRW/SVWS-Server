<template>
	<template v-if="visible">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ 'Pausenzeit' }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID:
					{{ auswahl?.id }}
				</svws-ui-badge>
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

	import type { PausenzeitenAppProps } from "./SPausenzeitenAppProps";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<PausenzeitenAppProps>();

	const visible: ComputedRef<boolean> = computed(() => props.auswahl !== undefined);

</script>
