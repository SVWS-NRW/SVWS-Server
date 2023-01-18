
<template>
	<div v-if="visible" class="space-y-3">
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_1 :blockung="blockung" v-model="regel" :regeln="regeln[1].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_6 :blockung="blockung" v-model="regel" :regeln="regeln[6].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_2 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :regeln="regeln[2].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_3 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :regeln="regeln[3].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_7 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :regeln="regeln[7].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_8 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :regeln="regeln[8].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_4 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :list-schueler="listSchueler" :regeln="regeln[4].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_5 :blockung="blockung" v-model="regel" :data-faecher="dataFaecher" :list-schueler="listSchueler" :regeln="regeln[5].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_9 :blockung="blockung" v-model="regel" :regeln="regeln[9].value" @regel-speichern="regelSpeichern" @regel-entfernen="regelEntfernen" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { GostBlockungRegel } from '@svws-nrw/svws-core-ts';
	import { computed, ComputedRef, ShallowRef, shallowRef } from 'vue';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { ListAbiturjahrgangSchueler } from '~/apps/gost/ListAbiturjahrgangSchueler';

	const props = defineProps<{
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
	}>();

	const regel: ShallowRef<GostBlockungRegel | undefined> = shallowRef(undefined);

	const alle_regeln: ComputedRef<GostBlockungRegel[]> = computed(() => props.blockung.datenmanager?.getMengeOfRegeln().toArray() as GostBlockungRegel[] || []);
	const regeln: ComputedRef<GostBlockungRegel[]>[] = [];
	for (let i = 1; i < 10; i++)
		regeln[i] = computed(() => alle_regeln.value.filter(r => r.typ === i));

	async function regelEntfernen(r: GostBlockungRegel) {
		await props.blockung.del_blockung_regel(r.id);
		if (r.id === regel.value?.id)
			regel.value = undefined;
	}

	async function regelSpeichern() {
		if (regel.value === undefined)
			return;
		await props.blockung.add_blockung_regel(regel.value);
		regel.value = undefined;
	}

	const visible: ComputedRef<boolean> = computed(() => {
		return true;
	});

</script>
