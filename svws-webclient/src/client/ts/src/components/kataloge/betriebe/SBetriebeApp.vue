<template>
	<template v-if="true">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ auswahl?.name1 }}</span>
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
	<div v-else class="app-content-placeholder">
		<i-ri-archive-line />
	</div>
</template>
<script setup lang="ts">
	import type { ComputedRef } from 'vue';
	import { computed } from 'vue';
	import type { BetriebeAppProps } from './SBetriebeAppProps';


	const props = defineProps<BetriebeAppProps>();

	const visible: ComputedRef<boolean> = computed(() => {
		return props.auswahl !== undefined;
	});

</script>

