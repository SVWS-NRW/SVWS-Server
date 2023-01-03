<template>
	<svws-ui-content-card title="Basisdaten">
		<div class="content-wrapper">
			<div class="input-wrapper">
				<svws-ui-text-input placeholder="Kürzel" v-model="kuerzel" type="text" />
				<svws-ui-text-input placeholder="Parallelität" v-model="parallelitaet" type="text" />
				<svws-ui-text-input placeholder="Sortierung" v-model="inputSortierung" type="text" />
				<svws-ui-multi-select title="Jahrgang" v-model="jahrgang" :items="listJahrgaenge.liste"
					:item-text="(item: JahrgangsListeEintrag) => item.kuerzel?.toString() || ''" />
				<svws-ui-checkbox v-model="inputIstSichtbar"> Ist sichtbar </svws-ui-checkbox>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed, WritableComputedRef } from "vue";
	import { Jahrgaenge, JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";

	const { data, listJahrgaenge, mapJahrgaenge } = defineProps<{ 
		data: DataKlasse, 
		listJahrgaenge: ListJahrgaenge,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>
	}>();

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => data.daten?.kuerzel?.toString(),
		set: (value) => data.patch({ kuerzel: value })
	});

	const parallelitaet: WritableComputedRef<string | undefined> = computed({
		get: () => data.daten?.parallelitaet?.toString(),
		set: (value) => data.patch({ parallelitaet: value })
	});

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => ((data.daten === undefined) || (data.daten.idJahrgang === null)) ? undefined : mapJahrgaenge.get(data.daten.idJahrgang),
		set: (value) => data.patch({ idJahrgang: value?.id })
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
