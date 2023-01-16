
<template>
	<div v-if="visible" class="space-y-3">
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_1 :blockung="blockung" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_6 :blockung="blockung" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_2 :blockung="blockung" :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_3 :blockung="blockung" :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_7 :blockung="blockung" :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_8 :blockung="blockung" :data-faecher="dataFaecher" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_4 :blockung="blockung" :data-faecher="dataFaecher" :list-schueler="listSchueler" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_5 :blockung="blockung" :data-faecher="dataFaecher" :list-schueler="listSchueler" />
		</div>
		<div class="rounded bg-white px-4 py-3 shadow-dark-20 shadow">
			<Blockungsregel_9 :blockung="blockung" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from 'vue';
	import { DataGostFaecher } from '~/apps/gost/DataGostFaecher';
	import { DataGostKursblockung } from '~/apps/gost/DataGostKursblockung';
	import { ListAbiturjahrgangSchueler } from '~/apps/gost/ListAbiturjahrgangSchueler';

	const props = defineProps<{
		dataFaecher: DataGostFaecher;
		blockung: DataGostKursblockung;
		listSchueler: ListAbiturjahrgangSchueler;
	}>();

	const visible: ComputedRef<boolean> = computed(() => {
		return true;
	});

</script>
