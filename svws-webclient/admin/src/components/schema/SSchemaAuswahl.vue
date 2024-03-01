<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div class="flex flex-col gap-0.5">
				<span>Schema</span>
				<svws-ui-button @click="refresh" type="transparent" title="Liste aktualisieren" class="-ml-3"> <i-ri-loop-right-line /> Liste aktualisieren</svws-ui-button>
			</div>
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoSchema" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="mapSchema().values()"
				:columns="cols" clickable :selectable="hasRootPrivileges" count scroll-into-view scroll>
				<template #header(isTainted)>
					<svws-ui-tooltip>
						<i-ri-file-damage-line class="w-[1.4em] h-[1.4em] -my-1" />
						<template #content>Tainted: Schema ist kann von der angegebenen Revision abweichen und wird als Entwickler-Schema betrachtet</template>
					</svws-ui-tooltip>
				</template>
				<template #header(isInConfig)>
					<svws-ui-tooltip>
						<i-ri-settings-2-line class="w-[1.4em] h-[1.4em] -my-1" />
						<template #content>Schema ist in der Config-Datei eingetragen</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(name)="{ value }">
					<span class="line-clamp-1 break-all -my-[0.1em] py-[0.1em]">{{ value }}</span>
				</template>
				<template #cell(revision)="{ value }">
					<span v-if="value > 0">{{ value }}</span>
					<span v-else class="opacity-25">–</span>
				</template>
				<template #cell(isTainted)="{ value }">
					<i-ri-error-warning-line v-if="value === true" />
				</template>
				<template #cell(isInConfig)="{ value, rowData }">
					<i-ri-check-line v-if="value === true" />
					<i-ri-alert-fill v-if="rowData.isDeactivated === true" class="text-error" />
				</template>
				<template v-if="hasRootPrivileges" #actions>
					<svws-ui-button v-if="selectedItems.length > 0" type="trash" @click="removeSchemata" title="Entfernt die ausgewählten SVWS-Schemata. Die jeweiligen Datenbank-Benutzer verlieren ihre Rechte auf das Schema, bleiben allerdings in der Datenbank angelegt." :disabled="apiStatus.pending" />
					<s-schema-migrate-modal v-slot="{ openModal }" :migrate-schema="migrateSchema" :migration-quellinformationen="migrationQuellinformationen">
						<svws-ui-button type="icon" @click="openModal" title="Schild2-Schema migrieren"> <i-ri-database-2-line class="!w-[1.5em] !h-[1.5em]" />  </svws-ui-button>
					</s-schema-migrate-modal>
					<s-schema-auswahl-import-modal v-slot="{ openModal }" :import-schema="importSchema">
						<svws-ui-button @click="openModal" type="icon" title="Backup wiederherstellen: SQLite-Schema importieren"> <i-ri-device-recover-line class="!w-[1.5em] !h-[1.5em]" /> </svws-ui-button>
					</s-schema-auswahl-import-modal>
					<s-schema-duplicate-modal v-slot="{ openModal }" :duplicate-schema="duplicateSchema">
						<svws-ui-button @click="openModal" type="icon" title="Schema duplizieren"> <i-ri-file-copy-line /> </svws-ui-button>
					</s-schema-duplicate-modal>
					<s-schema-auswahl-neu-modal v-slot="{ openModal }" :add-schema="addSchema">
						<svws-ui-button @click="openModal" type="icon" title="Schema hinzufügen"> <i-ri-add-line /> </svws-ui-button>
					</s-schema-auswahl-neu-modal>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { SchemaListeEintrag } from "@core";
	import type { SchemaAuswahlProps } from "./SSchemaAuswahlProps";

	const props = defineProps<SchemaAuswahlProps>();

	const cols = [
		{ key: "name", label: "Name", sortable: true, span: 2 },
		{ key: "revision", label: "Revision", sortable: true, span: 1 },
		{ key: "isTainted", label: "Tainted", tooltip: 'Tainted: Schema kann von der angegebenen Revision abweichen und wird als Entwickler-Schema betrachtet', sortable: true, span: 0.5 },
		{ key: "isInConfig", label: "Config", tooltip: 'Schema ist in der Config-Datei eingetragen', sortable: true, span: 0.5 },
	]

	const selectedItems = computed<Array<SchemaListeEintrag>>({
		get: () => props.auswahlGruppe,
		set: (items) => props.setAuswahlGruppe(items)
	});

</script>
