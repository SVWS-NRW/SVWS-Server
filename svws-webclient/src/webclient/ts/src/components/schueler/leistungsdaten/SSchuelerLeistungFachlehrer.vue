<template>
	<span>
		{{ lehrer_kuerzel }}
	</span>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		leistungsdaten: SchuelerLeistungsdaten,
		mapLehrer: Map<number, LehrerListeEintrag>
	}>();

	const id: ComputedRef<number | undefined> = computed(() => props.leistungsdaten.lehrerID ?? undefined);
	const lehrer: ComputedRef<LehrerListeEintrag | undefined> = computed(() => id.value === undefined ? undefined : props.mapLehrer.get(id.value));
	const lehrer_kuerzel: ComputedRef<string | undefined> = computed(() => lehrer.value?.kuerzel ?? undefined);

</script>
