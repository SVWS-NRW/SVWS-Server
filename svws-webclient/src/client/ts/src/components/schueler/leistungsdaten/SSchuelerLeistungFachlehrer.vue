<template>
	<span>
		{{ lehrer_kuerzel }}
	</span>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		fachlehrer: number | null,
		mapLehrer: Map<number, LehrerListeEintrag>
	}>();
	const id: ComputedRef<number | undefined> = computed(() => props.fachlehrer ?? undefined);
	const lehrer: ComputedRef<LehrerListeEintrag | undefined> = computed(() => id.value === undefined ? undefined : props.mapLehrer.get(id.value));
	const lehrer_kuerzel: ComputedRef<string | undefined> = computed(() => lehrer.value?.kuerzel ?? undefined);

</script>
