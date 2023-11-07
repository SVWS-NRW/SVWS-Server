<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Schema</span>
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" @update:clicked="gotoSchema" :model-value="selectedItems" @update:model-value="setAuswahlGruppe" :items="mapSchema.values()"
				:columns="cols" clickable selectable count scroll-into-view scroll>
				<template #cell(name)="{ value }">
					{{ value }}
				</template>
				<template #cell(revision)="{ value }">
					{{ value > 0 ? value : '–' }}
				</template>
				<template #cell(isTainted)="{ value }">
					<i-ri-file-damage-line v-if="value === true" />
				</template>
				<template #cell(isInConfig)="{ value }">
					<i-ri-settings-2-line v-if="value === true" />
				</template>
				<template #actions>
					<svws-ui-button type="trash" @click="removeSchemata" v-if="selectedItems.length > 0" />
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
