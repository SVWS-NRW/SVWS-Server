<template>
	<svws-ui-content-card title="Basisdaten">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
			<svws-ui-text-input placeholder="Bezeichnung" v-model="inputBezeichnung" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<template #title>
			<div class="content-card--header content-card--headline text-headline-sm">
				<i-ri-bar-chart-fill class="mr-1 opacity-50" />
				<span>Statistik</span>
			</div>
		</template>
		<svws-ui-input-wrapper>
			<svws-ui-text-input placeholder="Bezeichnung in Statistik" v-model="inputFachStatistik" type="text" />
		</svws-ui-input-wrapper>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { FachDaten } from "@svws-nrw/svws-core";
	import type { WritableComputedRef } from "vue";
	import { computed } from "vue";

	const props = defineProps<{
		data: FachDaten;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<FachDaten>): void;
	}>()

	function doPatch(data: Partial<FachDaten>) {
		emit('patch', data);
	}

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzel ?? undefined,
		set: (value) => doPatch({ kuerzel: value })
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.bezeichnung ?? undefined,
		set: (value) => doPatch({ bezeichnung: value })
	});

	const inputFachStatistik: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.kuerzelStatistik ?? undefined,
		set: (value) => doPatch({ kuerzelStatistik: value })
	});

</script>
