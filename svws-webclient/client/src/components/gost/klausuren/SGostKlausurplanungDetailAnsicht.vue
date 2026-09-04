<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<div class="page page-flex-col">
		<svws-ui-content-card class="col-span-full" :title="`Klausurplan ${state.jahrgangsdaten.jahrgang}, ${state.halbjahr.halbjahr}. Halbjahr${state.quartal === 0 ? '' : ', ' + state.quartal + '. Quartal'}`">
			<div v-if="termine.size() > 0" class="flex flex-col gap-20 mt-8">
				<s-gost-klausurplanung-detail-ansicht-termin v-for="termin in termine"
					:key="termin.id"
					:termin />
			</div>
			<div v-else>
				<span>Es wurden noch keine Klausurtermine geplant.</span>
			</div>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';
	import { computed, onMounted, ref } from 'vue';

	const state = useGostKlausurplanungState();
	const termine = computed(() => state.manager.terminHtMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

</script>
