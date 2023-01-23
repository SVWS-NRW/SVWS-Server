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
	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { DataKlasse } from "~/apps/klassen/DataKlasse";
	import { ListJahrgaenge } from "~/apps/jahrgaenge/ListJahrgaenge";

	const props = defineProps<{
		data: DataKlasse,
		listJahrgaenge: ListJahrgaenge,
		mapJahrgaenge: Map<Number, JahrgangsListeEintrag>
	}>();

	const kuerzel: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.kuerzel?.toString(),
		set: (value) => void props.data.patch({ kuerzel: value })
	});

	const parallelitaet: WritableComputedRef<string | undefined> = computed({
		get: () => props.data.daten?.parallelitaet?.toString(),
		set: (value) => void props.data.patch({ parallelitaet: value })
	});

	const jahrgang: WritableComputedRef<JahrgangsListeEintrag | undefined> = computed({
		get: () => ((props.data.daten === undefined) || (props.data.daten.idJahrgang === null)) ? undefined : props.mapJahrgaenge.get(props.data.daten.idJahrgang),
		set: (value) => void props.data.patch({ idJahrgang: value?.id })
	});

	const inputIstSichtbar: WritableComputedRef<boolean | undefined> = computed({
		get: () => props.data.daten?.istSichtbar,
		set: (value) => void props.data.patch({ istSichtbar: value })
	});

	const inputSortierung: WritableComputedRef<number | undefined> = computed({
		get: () => props.data.daten?.sortierung,
		set: (value) => void props.data.patch({ sortierung: value })
	});

</script>
