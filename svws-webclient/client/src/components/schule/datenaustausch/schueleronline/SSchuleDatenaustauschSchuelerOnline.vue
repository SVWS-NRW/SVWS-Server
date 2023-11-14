<template>
	<div class="page--content page--content--full">
		<svws-ui-content-card title="Import von Schüler-Online">
			<svws-ui-button type="primary" @click="doImport">
				<i-ri-download2-line />
				<span>Import</span>
			</svws-ui-button>
			<div class="flex items-start gap-3">
				Hier wird zukünftig der Import von Schüler-Online gehandhabt...
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Export zu Schüler-Online">
			<div class="flex items-start gap-3">
				Hier wird zukünftig der Export zu Schüler-Online gehandhabt...
			</div>
			<svws-ui-button type="primary" @click="doExport">
				<i-ri-upload2-line />
				<span>Export</span>
			</svws-ui-button>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenaustauschSchuelerOnlineProps } from './SSchuleDatenaustauschSchuelerOnlineProps';
	const props = defineProps<SchuleDatenaustauschSchuelerOnlineProps>();

	function doImport() {
		console.log("Der Import von Schüler-Online ist noch nicht implementiert.");
	}

	function doExport() {
		console.log("Der Export zu Schüler-Online ist noch nicht implementiert.");
	}

</script>
