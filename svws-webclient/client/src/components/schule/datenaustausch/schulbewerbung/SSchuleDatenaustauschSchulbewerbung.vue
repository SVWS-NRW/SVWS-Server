<template>
	<div class="page--content page--content--full">
		<svws-ui-content-card title="Import von Schulbewerbung.de">
			<svws-ui-button type="primary" @click="doImport">
				<i-ri-download2-line />
				<span>Import</span>
			</svws-ui-button>
			<div class="flex items-start gap-3">
				Hier wird zukünftig der Import von Schulbewerbung.de gehandhabt...
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Export zu Schulbewerbung.de">
			<div class="flex items-start gap-3">
				Hier wird zukünftig der Export zu Schulbewerbung.de gehandhabt...
			</div>
			<svws-ui-button type="primary" @click="doExport">
				<i-ri-upload2-line />
				<span>Export</span>
			</svws-ui-button>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchuleDatenaustauschSchulbewerbungProps } from './SSchuleDatenaustauschSchulbewerbungProps';
	const props = defineProps<SchuleDatenaustauschSchulbewerbungProps>();

	function doImport() {
		console.log("Der Import von Schulbewerbung.de ist noch nicht implementiert.");
	}

	function doExport() {
		console.log("Der Export zu Schulbewerbung.de ist noch nicht implementiert.");
	}

</script>
