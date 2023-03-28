<template>
	<div class="app-container relative">
		<div v-for="(e, i) in data" :key="i" class="col-span-full">
			<s-card-schueler-erziehungsberechtigte :erzieher="e" @patch="patch" :map-erzieherarten="mapErzieherarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		</div>
		<div>
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { SchuelerErziehungsberechtigteProps } from "./SSchuelerErziehungsberechtigteProps";
	// TODO import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';

	const props = defineProps<SchuelerErziehungsberechtigteProps>();

	// TODO debouncing... const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
