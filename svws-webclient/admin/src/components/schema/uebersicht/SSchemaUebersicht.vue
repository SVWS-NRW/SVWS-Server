<template>
	<div class="page--content">
		<svws-ui-content-card v-if="eintrag !== undefined" title="Schema verwalten">
			<template v-if="eintrag.isInConfig === false">
				<svws-ui-button type="secondary" @click="inConfig">In Config setzen</svws-ui-button>
			</template>
			<s-schema-migrate-modal v-slot="{ openModal }" :migrate-schema="migrateSchema" :target-schema="eintrag.name">
				<svws-ui-button type="secondary" @click="openModal" title="Schild2-Schema migrieren"> <i-ri-share-forward-2-line /> Schild2-Schema hierher migrieren </svws-ui-button>
			</s-schema-migrate-modal>
			<svws-ui-button type="secondary" @click="getBackupSchema" title="SQLite-Schema als Backup erstellen"> <i-ri-download-2-line /> Backup erstellen </svws-ui-button>
			<s-schema-import-modal v-slot="{ openModal }" :restore-schema="restoreSchema">
				<svws-ui-button type="secondary" @click="openModal" title="Schema aus Backup wiederherstellen"> <i-ri-upload-2-line /> Schema aus Backup hier wiederherstellen </svws-ui-button>
			</s-schema-import-modal>
			<svws-ui-button type="secondary" v-if="revisionUpToDate" @click="upgradeSchema" title="Schema auf aktuelle Revision hochsetzen"> <i-ri-speed-line /> Revision auf {{ revision }} setzen </svws-ui-button>
			<div v-if="eintrag.isTainted">Achtung, auch nach dem Hochsetzen auf die aktuelle Revision bleibt das Schema „Tainted“.</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="weiteres" class="opacity-50">
			Weitere Funktionen
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";
	import { computed } from "vue";

	const props = defineProps<SchemaUebersichtProps>();

	const eintrag = computed(()=> props.data());

	const revisionUpToDate = computed<boolean>(()=> {
		const revServer = props.revision;
		if (eintrag.value === undefined || revServer === null || eintrag.value.revision < 0)
			return false;
		return revServer !== eintrag.value.revision;
	})

	async function inConfig() {}

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
