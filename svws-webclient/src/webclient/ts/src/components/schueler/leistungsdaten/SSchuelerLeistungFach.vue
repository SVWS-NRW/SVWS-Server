<template>
	<span :style="{ 'background-color': bgColor }">
		{{ fach_bezeichnung }}
	</span>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten, ZulaessigesFach } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		data: SchuelerLernabschnittsdaten;
		leistungsdaten: SchuelerLeistungsdaten,
		mapFaecher: Map<number, FaecherListeEintrag>
	}>();

	const id: ComputedRef<number> = computed(() => props.leistungsdaten.fachID);
	const fach: ComputedRef<FaecherListeEintrag | undefined> = computed(() => props.mapFaecher.get(id.value));
	const fach_bezeichnung: ComputedRef<string | undefined> = computed(() => fach.value?.bezeichnung ?? undefined);

	const zul_fach: ComputedRef<ZulaessigesFach | undefined> = computed(() => {
		if (fach.value === undefined)
			return;
		return ZulaessigesFach.getByKuerzelASD(fach.value.kuerzel);
	});

	const bgColor: ComputedRef<string> = computed<string>(() => {
		if (zul_fach.value === undefined)
			return "#ffffff";
		return zul_fach.value.getHMTLFarbeRGB();
	});

</script>
