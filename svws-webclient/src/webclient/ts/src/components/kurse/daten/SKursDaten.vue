<template>
	<div class="app-container">
		<s-card-kurs-basisdaten :data="data" :map-jahrgaenge="mapJahrgaenge" :map-lehrer="mapLehrer" @patch="doPatch" />
	</div>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag, KursDaten, LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed } from "vue";
	import { useDebouncedPatch } from "~/utils/composables/debouncedPatch";

	const props = defineProps<{
		patch: (data : Partial<KursDaten>) => Promise<void>;
		data: KursDaten;
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>;
		mapLehrer: Map<number, LehrerListeEintrag>;
	}>();

	const { doPatch } = useDebouncedPatch(computed(() => props.data), props.patch)
</script>
