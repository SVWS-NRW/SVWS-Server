<template>
	<div class="page--content">
		<svws-ui-content-card title="Schema verwalten">
			<template v-if="data()?.isInConfig === false">
				<svws-ui-button type="secondary" @click="inConfig">In Config setzen</svws-ui-button>
			</template>
			<svws-ui-button type="secondary" @click="openMigrate">Anderes Schema hierher migrieren</svws-ui-button>
			<svws-ui-button type="secondary" @click="getBackupSchema" title="SQLite-Schema als Backup erstellen"> <i-ri-download-2-line /> Backup erstellen </svws-ui-button>
		</svws-ui-content-card>
		<svws-ui-content-card title="weiteres" class="opacity-50">
			Weitere Funktionen
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";

	const props = defineProps<SchemaUebersichtProps>();

	async function inConfig() {}
	async function openMigrate() {}

	async function getBackupSchema() {
		const { data, name } = await props.backupSchema();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}
</script>
