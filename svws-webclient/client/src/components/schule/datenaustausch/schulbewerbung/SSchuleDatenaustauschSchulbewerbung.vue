<template>
	<svws-ui-header>
		<span class="inline-block mr-3">Datenaustausch mit Schulbewerbung.de</span>
		<br>
		<span class="opacity-50 flex">
			<span class="i-ri-download-2-line icon-xl" />
			<span class="i-ri-upload-2-line icon-xl" />
		</span>
	</svws-ui-header>
	<div class="svws-ui-page w-full">
		<div class="svws-ui-tab-content">
			<div class="page page-flex-row">
				<svws-ui-content-card title="Import von Schulbewerbung.de">
					<svws-ui-button class="contentFocusField" type="primary" @click="doImport">
						<span class="icon i-ri-download-2-line" />
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
					<svws-ui-button class="contentFocusField" type="primary" @click="doExport">
						<span class="icon i-ri-upload-2-line" />
						<span>Export</span>
					</svws-ui-button>
				</svws-ui-content-card>
			</div>
		</div>
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
