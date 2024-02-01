<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div class="flex gap-2"><span>Schema</span><svws-ui-button @click="refresh" type="icon" title="Liste aktualisieren"> <i-ri-loop-right-line /> </svws-ui-button></div>
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoSchema" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="mapSchema().values()"
				:columns="cols" clickable :selectable="hasRootPrivileges" count scroll-into-view scroll>
				<template #cell(name)="{ value }">
					{{ value }}
				</template>
				<template #cell(revision)="{ value }">
					{{ value > 0 ? value : '–' }}
				</template>
				<template #cell(isTainted)="{ value }">
					<i-ri-file-damage-line v-if="value === true" />
				</template>
				<template #cell(isInConfig)="{ value, rowData }">
					<i-ri-settings-2-line v-if="value === true" />
					<i-ri-alert-fill v-if="rowData.isDeactivated === true" class="text-error" />
				</template>
				<template v-if="hasRootPrivileges" #actions>
					<svws-ui-button v-if="selectedItems.length > 0" type="trash" @click="removeSchemata" />
					<s-schema-migrate-modal v-slot="{ openModal }" :migrate-schema="migrateSchema" :migration-quellinformationen="migrationQuellinformationen">
						<svws-ui-button type="icon" @click="openModal" title="Schild2-Schema migrieren"> <i-ri-share-forward-2-line />  </svws-ui-button>
					</s-schema-migrate-modal>
					<s-schema-auswahl-import-modal v-slot="{ openModal }" :import-schema="importSchema">
						<svws-ui-button @click="openModal" type="icon" title="SQLite-Schema importieren"> <i-ri-download-2-line /> </svws-ui-button>
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
		{ key: "name", label: "Schemaname", sortable: true, span: 2 },
		{ key: "revision", label: "Revision", sortable: true, span: 1 },
		{ key: "isTainted", label: "Tainted", tooltip: 'Dieses Schema ist nicht in der angegebenen Revision und wird als Entwickler-Schema betrachtet', sortable: true, span: 1 },
		{ key: "isInConfig", label: "Config", tooltip: 'Dieses Schema ist in der Config-Datei eingetragen', sortable: true, span: 1 },
	]

	const selectedItems = computed<Array<SchemaListeEintrag>>({
		get: () => props.auswahlGruppe,
		set: (items) => props.setAuswahlGruppe(items)
	});

</script>
