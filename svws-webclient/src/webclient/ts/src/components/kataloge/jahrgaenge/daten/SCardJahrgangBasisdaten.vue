<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="inputKuerzel" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung" v-model="inputBezeichnung" type="text" />
				<svws-ui-text-input placeholder="Bezeichnung in Statistik" v-model="inputKuerzelStatistik" type="text" />
				<svws-ui-multi-select title="Folgejahrgang" v-model="inputIdFolgejahrgang"
					:items="listJahrgaenge?.filter((e: JahrgangsListeEintrag) => e.id !== id)"
					:item-text="(e: JahrgangsListeEintrag) => e.bezeichnung?.toString() || ''" />
				<svws-ui-text-input placeholder="Kürzel Schulgliederung" v-model="inputKuerzelSchulgliederung" type="text" />
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { DataJahrgang } from "~/apps/kataloge/jahrgaenge/DataJahrgang";

	const props = defineProps<{
		data: DataJahrgang,
		listJahrgaenge: JahrgangsListeEintrag[];
	}>();

	const id: ComputedRef<number | undefined> = computed(() => {
		return props.data.daten?.id.valueOf();
	});

	const inputKuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel?.toString(),
		set: (value) => void props.data.patch({ kuerzel: value })
	});

	const inputBezeichnung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.bezeichnung?.toString(),
		set: (value) => void props.data.patch({ bezeichnung: value })
	});

	const inputKuerzelStatistik: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzelStatistik?.toString(),
		set: (value: string | undefined) => void props.data.patch({ kuerzelStatistik: value })
	});

	const inputKuerzelSchulgliederung: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzelSchulgliederung?.toString(),
		set: (value) => void props.data.patch({ kuerzelSchulgliederung: value })
	});

	const inputIdFolgejahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => props.listJahrgaenge.find((e: JahrgangsListeEintrag) => props.data.daten?.idFolgejahrgang === e.id),
		set: (value) => void props.data.patch({ idFolgejahrgang: value?.id })
	});

</script>
