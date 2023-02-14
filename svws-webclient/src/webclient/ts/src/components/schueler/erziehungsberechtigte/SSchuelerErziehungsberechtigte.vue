<template>
	<div class="app-container relative">
		<div v-for="(e, i) in data" :key="i" class="col-span-full">
			<s-card-schueler-erziehungsberechtigte :erzieher="e" @patch="patch" :map-erzieherarten="mapErzieherarten" :map-orte="mapOrte" :map-ortsteile="mapOrtsteile" />
		</div>
		<div class="flex justify-start">
			<svws-ui-button>Erziehungsberechtigten hinzufügen</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { Erzieherart, ErzieherStammdaten, List, OrtKatalogEintrag, OrtsteilKatalogEintrag } from "@svws-nrw/svws-core-ts";
	// TODO import { useDebouncedPatch } from '~/utils/composables/debouncedPatch';

	const props = defineProps<{
		data: List<ErzieherStammdaten>;
		patch: (data : Partial<ErzieherStammdaten>, id: number) => Promise<void>;
		mapErzieherarten: Map<number, Erzieherart>;
		mapOrte: Map<number, OrtKatalogEintrag>;
		mapOrtsteile: Map<number, OrtsteilKatalogEintrag>;
	}>();

	// TODO debouncing... const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)

</script>
