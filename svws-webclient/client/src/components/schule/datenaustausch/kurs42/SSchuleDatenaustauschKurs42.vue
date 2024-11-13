<template>
	<div class="page--flex">
		<svws-ui-header>
			<span class="inline-block mr-3">Import aus Kurs42</span>
			<br>
			<span class="opacity-50 flex">
				<span class="icon-xl i-ri-download-2-line" />
			</span>
		</svws-ui-header>
		<svws-ui-tab-bar :tab-manager enable-focus-switching>
			<router-view />
		</svws-ui-tab-bar>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenaustauschKurs42Props } from './SSchuleDatenaustauschKurs42Props';

	const props = defineProps<SchuleDatenaustauschKurs42Props>();

</script>
