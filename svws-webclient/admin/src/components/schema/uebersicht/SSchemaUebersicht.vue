<template>
	<div class="page--content">
		<svws-ui-content-card v-if="eintrag !== undefined" title="Schema verwalten">
			<svws-ui-input-wrapper>
				<template v-if="schuleInfo() === undefined">
					<div class="flex gap-3">
						<svws-ui-select title="Schulen nach Schulnummer und Ort suchen" v-model="schule" :items="schulen()" :item-text="i=> `${i.SchulNr}: ${i.ABez1 ?? ''} ${i.ABez2 ?? ''} ${i.ABez3 ?? ''}`" autocomplete :item-filter="schulen_filter" />
						<svws-ui-button :disabled="schule == undefined" type="secondary" @click="init"><svws-ui-spinner :spinning="loading" /> Mit Schulnummer initialisieren</svws-ui-button>
					</div>
				</template>
				<template v-if="eintrag.isInConfig === false">
					<svws-ui-button type="secondary" @click="inConfig">In Config setzen</svws-ui-button>
				</template>
				<s-schema-migrate-modal v-slot="{ openModal }" :migrate-schema="migrateSchema" :target-schema="eintrag.name" :migration-quellinformationen="migrationQuellinformationen">
					<svws-ui-button type="secondary" @click="openModal" title="Schild2-Schema migrieren"> <i-ri-share-forward-2-line /> Schild2-Schema hierher migrieren </svws-ui-button>
				</s-schema-migrate-modal>
				<svws-ui-button type="secondary" @click="getBackupSchema" title="SQLite-Schema als Backup erstellen"> <i-ri-upload-2-line /> Backup erstellen </svws-ui-button>
				<s-schema-import-modal v-slot="{ openModal }" :restore-schema="restoreSchema">
					<svws-ui-button type="secondary" @click="openModal" title="Schema aus Backup wiederherstellen"> <i-ri-download-2-line /> Schema aus Backup hier wiederherstellen </svws-ui-button>
				</s-schema-import-modal>
				<svws-ui-button type="secondary" v-if="revisionUpToDate" @click="upgradeSchema" title="Schema auf aktuelle Revision hochsetzen"> <i-ri-speed-line /> Revision auf {{ revision }} setzen </svws-ui-button>
				<div v-if="eintrag.isTainted">Achtung, auch nach dem Hochsetzen auf die aktuelle Revision bleibt das Schema „Tainted“.</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Admin-Benutzer">
			<svws-ui-table :columns="cols" :items="admins()" />
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import type { SchemaUebersichtProps } from "./SSchemaUebersichtProps";
	import type { DataTableColumn } from "@ui";
	import { computed, ref } from "vue";
	import type { SchulenKatalogEintrag } from "@core";

	const props = defineProps<SchemaUebersichtProps>();

	const eintrag = computed(()=> props.data());
	const schule = ref<SchulenKatalogEintrag>()
	const loading = ref<boolean>(false);

	const revisionUpToDate = computed<boolean>(()=> {
		const revServer = props.revision;
		if (eintrag.value === undefined || revServer === null || eintrag.value.revision < 0)
			return false;
		return revServer !== eintrag.value.revision;
	})

	const cols: DataTableColumn[] = [
		{ key: "anzeigename", label: "Name", span: 3 },
		{ key: "name", label: "Benutzername", span: 1, sortable: true },
	];

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

	async function init() {
		if (schule.value === undefined)
			return;
		loading.value = true;
		await props.initSchema(Number(schule.value.SchulNr));
		loading.value = false;
	}

	/** Der Filter für Schulen */
	const schulen_filter = (items: SchulenKatalogEintrag[], search: string): SchulenKatalogEintrag[] => {
		// Teilmatch Schulnummer
		const nrmatch = search.match(/\d+/);
		const nr = nrmatch ? nrmatch[0] : undefined;
		//Teilmatch Name
		const ort = search.replace(/\d+\s*/, "").trim();
		if (!nr && !ort) {
			return items;
		}
		if (!nr) {
			return items.filter((item: SchulenKatalogEintrag) => {
				if (item && item.Ort !== null) {
					return item.Ort
						.toLocaleLowerCase("de-DE")
						.startsWith(ort.toLocaleLowerCase("de-DE"));
				}
				return false;
			});
		} else if (!ort) {
			return items.filter(item => {
				if (item.SchulNr) {
					return item.SchulNr.includes(nr);
				}
				return false;
			});
		} else {
			return items.filter(item => {
				if (item.SchulNr && item.Ort) {
					return (
						item.SchulNr.includes(nr) &&
						item.Ort
							.toLocaleLowerCase("de-DE")
							.startsWith(ort.toLocaleLowerCase("de-DE"))
					);
				}
				return false;
			});
		}
	};
</script>
