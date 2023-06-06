<template>
	<svws-ui-multi-select title="—" v-model="lehrer" :items="mapLehrer.values()" :item-text="getLehrerText" headless class="w-full" />
</template>

<script setup lang="ts">

	import type { LehrerListeEintrag } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		fachlehrer: number | null,
		mapLehrer: Map<number, LehrerListeEintrag>
	}>();
	const id: ComputedRef<number | undefined> = computed(() => props.fachlehrer ?? undefined);
	const lehrer: ComputedRef<LehrerListeEintrag | undefined> = computed(() => id.value === undefined ? undefined : props.mapLehrer.get(id.value));
	const lehrer_kuerzel: ComputedRef<string | undefined> = computed(() => lehrer.value?.kuerzel ?? undefined);

	function getLehrerText(lehrer: LehrerListeEintrag | undefined): string {
		if (lehrer === undefined)
			return "---";
		return lehrer.nachname + ", " + lehrer.vorname + " (" + lehrer.kuerzel + ")";
	}

</script>
