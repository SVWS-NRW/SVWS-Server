<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
			</div>
			<svws-ui-text-input placeholder="Bezeichnung" v-model="inputText" type="text" />
			<svws-ui-text-input placeholder="Zeugnisbezeichnung" v-model="inputTextzeugnis" type="text" />
		</div>
	</svws-ui-content-card>
	<svws-ui-content-card>
		<template #title>
			<div class="content-card--header content-card--headline text-headline-sm">
				<i-ri-bar-chart-fill class="mr-1 opacity-50" />
				<span>Statistik</span>
			</div>
		</template>
		<div class="input-wrapper">
			<div class="col-span-2">
				<svws-ui-multi-select title="Statistikkürzel" v-model="inputStatistikKuerzel" :items="inputKatalogReligionenStatistik"
									  :item-text="(i: Religion) => i.daten.kuerzel" required />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { Religion, ReligionEintrag } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, WritableComputedRef } from "vue";

	const props = defineProps<{
		auswahl: ReligionEintrag;
	}>();

	const emit = defineEmits<{
		(e: 'patch', data: Partial<ReligionEintrag>): void;
	}>()

	function doPatch(data: Partial<ReligionEintrag>) {
		emit('patch', data);
	}

	const inputKatalogReligionenStatistik: ComputedRef<Religion[] | undefined> = computed(() => Religion.values());

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.auswahl.kuerzel ?? undefined,
		set: (value) => doPatch({ kuerzel: value })
	});

	const inputText: WritableComputedRef<string | undefined> = computed({
		get: () => props.auswahl.text ?? undefined,
		set: (value) => doPatch({ text: value })
	});

	const inputTextzeugnis: WritableComputedRef<string | undefined> = computed({
		get: () => props.auswahl.textZeugnis ?? undefined,
		set: (value) => doPatch({ textZeugnis: value })
	});

	const inputStatistikKuerzel: WritableComputedRef<Religion | undefined> = computed({
		get: () => Religion.getByKuerzel(props.auswahl.kuerzel || null) || undefined,
		set: (value) => doPatch({ kuerzel: value?.daten.kuerzel || null })
	});

</script>
