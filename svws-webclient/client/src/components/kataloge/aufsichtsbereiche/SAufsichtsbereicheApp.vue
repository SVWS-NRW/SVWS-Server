<template>
	<template v-if="auswahl">
		<svws-ui-header>
			<div>
				<span class="inline-block mr-3">{{ auswahl.kuerzel }}</span>
				<svws-ui-badge type="light" title="ID" class="font-mono" size="small">
					ID: {{ auswahl.id }}
				</svws-ui-badge>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</template>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-archive-line" />
	</div>
</template>

<script setup lang="ts">

	import type { AufsichtsbereicheAppProps } from "./SAufsichtsbereicheAppProps";

	const props = defineProps<AufsichtsbereicheAppProps>();

</script>
