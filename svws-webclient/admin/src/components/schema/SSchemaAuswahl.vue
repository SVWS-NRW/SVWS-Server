<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div class="flex flex-col gap-0.5">
				<span>Schema</span>
				<svws-ui-button @click="refresh" type="transparent" title="Liste aktualisieren" class="-ml-3"> <span class="icon i-ri-loop-right-line" /> Liste aktualisieren</svws-ui-button>
			</div>
		</template>
		<template #content>
			<svws-ui-table :items="mapSchema().values()" :columns="cols" :model-value="selectedItems" @update:model-value="setAuswahlGruppe"
				:clickable="!auswahlGruppe.length" :clicked="auswahlGruppe.length ? undefined : auswahl" @update:clicked="gotoSchema" :selectable="hasRootPrivileges" count scroll-into-view scroll>
				<template #header(isTainted)>
					<svws-ui-tooltip>
						<span class="icon i-ri-file-damage-line w-[1.4em] h-[1.4em] -my-1" />
						<template #content>Tainted: Schema ist kann von der angegebenen Revision abweichen und wird als Entwickler-Schema betrachtet</template>
					</svws-ui-tooltip>
				</template>
				<template #header(isInConfig)>
					<svws-ui-tooltip>
						<span class="icon i-ri-settings-2-line w-[1.4em] h-[1.4em] -my-1" />
						<template #content>Schema ist in der Config-Datei eingetragen</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(name)="{ value }">
					<span class="line-clamp-1 break-all -my-[0.1em] py-[0.1em]">{{ value }}</span>
				</template>
				<template #cell(revision)="{ value }">
					<template v-if="(value > 0) && (revision !== null)">
						<svws-ui-tooltip v-if="value > revision">
							<span>{{ value }}</span>
							<span class="icon i-ri-alert-fill icon-error -my-0.5" />
							<template #content>Dieser SVWS-Server unterstützt nur Schemata bis zur Version {{ revision }}. Um dieses Schema verwenden zu können, ist ein Upgrade des SVWS-Server notwendig.</template>
						</svws-ui-tooltip>
						<span v-else>{{ value }}</span>
					</template>
					<span v-else class="opacity-25">–</span>
				</template>
				<template #cell(isTainted)="{ value }">
					<span class="icon i-ri-error-warning-line -my-0.5" v-if="value === true" />
				</template>
				<template #cell(isInConfig)="{ value, rowData }">
					<span class="icon i-ri-check-line -my-0.5" v-if="value === true" />
					<span class="icon i-ri-alert-fill icon-error -my-0.5" v-if="rowData.isDeactivated === true" />
					<s-schema-auswahl-neu-modal v-if="value === false" v-slot="{ openModal }" :add-existing="addExistingSchemaToConfig" :schema="rowData.name">
						<span class="icon i-ri-close-line -my-0.5 opacity-25" />
						<svws-ui-button @click="openModal" type="icon" class="ml-auto" title="Schema in die Konfiguration übernehmen"> <span class="icon i-ri-share-forward-2-line" /> </svws-ui-button>
					</s-schema-auswahl-neu-modal>
				</template>
				<template v-if="hasRootPrivileges && auswahlGruppe.length === 0" #actions>
					<s-schema-migrate-modal v-slot="{ openModal }" :migrate-schema="migrateSchema" :migration-quellinformationen="migrationQuellinformationen">
						<svws-ui-button type="icon" @click="openModal" title="Schild2-Schema migrieren"> <span class="icon i-ri-database-2-line !w-[1.5em] !h-[1.5em]" />  </svws-ui-button>
					</s-schema-migrate-modal>
					<s-schema-auswahl-import-modal v-slot="{ openModal }" :import-schema="importSchema">
						<svws-ui-button @click="openModal" type="icon" title="Backup wiederherstellen: SQLite-Schema importieren"> <span class="icon i-ri-device-recover-line !w-[1.5em] !h-[1.5em]" /> </svws-ui-button>
					</s-schema-auswahl-import-modal>
					<s-schema-duplicate-modal v-slot="{ openModal }" :duplicate-schema="duplicateSchema">
						<svws-ui-button @click="openModal" type="icon" title="Schema duplizieren"> <span class="icon i-ri-file-copy-line" /> </svws-ui-button>
					</s-schema-duplicate-modal>
					<s-schema-auswahl-neu-modal v-slot="{ openModal }" :add-schema="addSchema">
						<svws-ui-button @click="openModal" type="icon" title="Schema hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
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
