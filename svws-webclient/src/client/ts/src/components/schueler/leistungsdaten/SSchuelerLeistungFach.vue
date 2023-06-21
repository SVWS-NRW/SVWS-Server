<template>
	<!--<span class="cell&#45;&#45;background-color" :style="{ 'background-color': bgColor }" />-->
	<!--<span class="w-4 h-4 rounded-full inline-block mr-1 border border-black/25" :style="{ 'background-color': bgColor }" />-->
	<span class="relative z-10 py-0.5 px-1 rounded font-medium" :style="{ 'background-color': bgColor }">
		{{ fach_bezeichnung }}
	</span>
</template>

<script setup lang="ts">

	import type { FaecherListeEintrag } from "@core";
	import { ZulaessigesFach } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

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

<style lang="postcss">
.cell--background-color {
	@apply absolute inset-0 opacity-50 pointer-events-none;

	@supports (mix-blend-mode: multiply) {
		@apply opacity-100 mix-blend-multiply;
	}
}
</style>
