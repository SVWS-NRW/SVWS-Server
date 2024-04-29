<template>
	<div class="page--flex">
		<svws-ui-header>
			<span class="inline-block mr-3">Datenaustausch mit Untis</span>
			<br>
			<span class="opacity-50 flex">
				<span class="icon-xl i-ri-download-2-line" />
				<span class="i-ri-upload-2-line icon-xl" />
			</span>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="tabs" :hidden="tabsHidden" :model-value="tab" @update:model-value="setTab">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenaustauschUntisProps } from './SSchuleDatenaustauschUntisProps';

	const props = defineProps<SchuleDatenaustauschUntisProps>();

</script>
