<template>
	<span :style="{ 'background-color': bgColor }">
		{{ fach_bezeichnung }}
	</span>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag, SchuelerLeistungsdaten, SchuelerLernabschnittsdaten, ZulaessigesFach } from "@svws-nrw/svws-core";
	import { computed, ComputedRef } from "vue";

	const props = defineProps<{
		fach: number,
		mapFaecher: Map<number, FaecherListeEintrag>
	}>();

	const fach: ComputedRef<FaecherListeEintrag | undefined> = computed(() => props.mapFaecher.get(props.fach));
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
