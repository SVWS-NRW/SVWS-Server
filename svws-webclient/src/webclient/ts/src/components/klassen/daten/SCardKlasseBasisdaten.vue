<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
				<svws-ui-text-input placeholder="Parallelität" v-model="parallelitaet" type="text" />
				<svws-ui-text-input placeholder="Sortierung" v-model="inputSortierung" type="text" />
				<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="jahrgaengeList"
					:item-text="(item: Jahrgaenge) => item.daten.kuerzel.toString() ||''" />
				<svws-ui-checkbox v-model="inputIstSichtbar"> Ist sichtbar </svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, WritableComputedRef } from "vue";
	import { Jahrgaenge } from "@svws-nrw/svws-core-ts";
	import { injectMainApp, Main } from "~/apps/Main";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";

	const { data } = defineProps<{ 
		data: DataKlasse, 
	}>();

	const main: Main = injectMainApp();

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => data.daten?.kuerzel?.toString(),
		set: (value) => data.patch({ kuerzel: value })
	});

	const parallelitaet: WritableComputedRef<string | undefined> = computed({
		get: () => data.daten?.parallelitaet?.toString(),
		set: (value) => data.patch({ parallelitaet: value })
	});

	// TODO Lade die Liste der Jahrgänge, Statistik-Katalog ist hier falsch
	const jahrgaengeList: ComputedRef<Jahrgaenge[]> = computed(() => Jahrgaenge.values());
	const jahrgang: WritableComputedRef<Jahrgaenge | undefined> = computed({
		get: () => jahrgaengeList.value.find((e: Jahrgaenge) => data.daten === undefined ? undefined : Jahrgaenge.getByID(data.daten.idJahrgang)),
		set: (value) => data.patch({idJahrgang: value?.daten.id})
	});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get: () => data.daten?.istSichtbar,
		set: (value) => data.patch({ istSichtbar: value })
	});

	const inputSortierung: WritableComputedRef<number | undefined> = computed({
		get: () => data.daten?.sortierung,
		set: (value) => data.patch({ sortierung: value })
	});

</script>
