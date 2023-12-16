<template>
	<template v-if="true">
		<svws-ui-header>
			<div class="flex items-center">
				<span class="inline-block mr-3">Zeitraster</span>
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

	import type { ZeitrasterAppProps } from "./SZeitrasterAppProps";

	const props = defineProps<ZeitrasterAppProps>();

</script>
