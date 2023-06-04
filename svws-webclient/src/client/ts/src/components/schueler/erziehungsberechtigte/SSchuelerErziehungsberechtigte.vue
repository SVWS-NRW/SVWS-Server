<template>
	<div class="page--content relative">
		<template v-for="(e, i) in data" :key="i">
			<s-card-schueler-erziehungsberechtigte :erzieher="e" @patch="patch" :map-erzieherarten="mapErzieherarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		</template>
		<svws-ui-content-card v-if="data.size() < 1" class="col-span-full" title="Erziehungsberechtigte">
			<div>Noch keine Einträge vorhanden.</div>
		</svws-ui-content-card>
		<div :class="data.size() ? 'mt-9' : ''">
			<svws-ui-button>Person hinzufügen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	// TODO import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	// TODO debouncing... const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
