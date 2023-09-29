<template>
	<span> {{ kurs_kuerzel }} </span>
</template>

<script setup lang="ts">

	import type { KursListeEintrag } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		kurs: number,
		mapKurse: Map<number, KursListeEintrag>
	}>();

	const kurs: ComputedRef<KursListeEintrag | undefined> = computed(() => props.mapKurse.get(props.kurs));
	const kurs_kuerzel: ComputedRef<string | undefined> = computed(() => kurs.value?.kuerzel ?? undefined);

</script>
