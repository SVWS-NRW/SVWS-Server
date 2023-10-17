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
					{{ value }}
				</template>
				<template #cell(isTainted)="{ value }">
					{{ value }}
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
		{ key: "isTainted", label: "Tainted ?", sortable: true, span: 1 },
	]

	const selectedItems = computed<Array<SchemaListeEintrag>>({
		get: () => props.auswahlGruppe,
		set: (items) => props.setAuswahlGruppe(items)
	});


</script>
