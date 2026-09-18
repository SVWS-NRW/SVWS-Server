<template>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl />
	</Teleport>
	<div class="page page-flex-col">
		<svws-ui-content-card class="col-span-full">
			<template #title>
				<h3 class="content-card--headline">Klausurplan {{ state.jahrgangsdaten.jahrgang }}, {{ state.halbjahr.halbjahr }}. Halbjahr{{ state.quartal === 0 ? '' : ', ' + state.quartal + '. Quartal' }}</h3>
			</template>
			<div class="flex items-center gap-2 whitespace-nowrap">
				<svws-ui-checkbox type="toggle" v-model="zeigeBetroffeneUnterrichte">Zeige betroffene Unterrichte</svws-ui-checkbox>
				<template v-if="zeigeBetroffeneUnterrichte">
					<span>und markiere Anwesenheit unter</span>
					<div class="w-28 shrink-0 mb-2">
						<svws-ui-input-number class="w-full" v-model="betroffeneUnterrichteAnwesenheitsschwelle" :min="0" :max="100" />
					</div>
					<span>%</span>
				</template>
			</div>
			<div v-if="termine.size() > 0" class="flex flex-col gap-20 mt-8">
				<s-gost-klausurplanung-detail-ansicht-termin v-for="termin in termine"
					:key="termin.id"
					:termin
					:zeige-betroffene-unterrichte
					:betroffene-unterrichte-anwesenheitsschwelle />
			</div>
			<div v-else>
				<span>Es wurden noch keine Klausurtermine geplant.</span>
			</div>
		</svws-ui-content-card>
	</div>
	<Teleport to="body">
		<div class="klausurplanung-detail-druckansicht">
			<h1>Klausurplan {{ state.jahrgangsdaten.jahrgang }}, {{ state.halbjahr.halbjahr }}. Halbjahr{{ state.quartal === 0 ? '' : ', ' + state.quartal + '. Quartal' }}</h1>
			<div v-if="termine.size() > 0">
				<div v-for="termin in termine" :key="termin.id" class="klausurplanung-detail-drucktermin">
					<s-gost-klausurplanung-detail-ansicht-termin :termin
						:zeige-betroffene-unterrichte
						:betroffene-unterrichte-anwesenheitsschwelle />
				</div>
			</div>
			<div v-else>Es wurden noch keine Klausurtermine geplant.</div>
		</div>
	</Teleport>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref } from 'vue';

	import { useGostKlausurplanungState } from '@ui/states/GostKlausurplanungState';

	const state = useGostKlausurplanungState();
	const termine = computed(() => state.manager.terminHtMitDatumGetMengeByAbijahrAndHalbjahrAndQuartal(state.jahrgangsdaten.abiturjahr, state.halbjahr, state.quartal));
	const zeigeBetroffeneUnterrichte = computed({
		get: () => state.zeigeBetroffeneUnterrichte,
		set: (value: boolean) => state.setZeigeBetroffeneUnterrichte(value),
	});
	const betroffeneUnterrichteAnwesenheitsschwelle = computed({
		get: () => state.betroffeneUnterrichteAnwesenheitsschwelle,
		set: (value: number | null) => state.setBetroffeneUnterrichteAnwesenheitsschwelle(value),
	});

	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
	});

</script>

<style>
	.klausurplanung-detail-druckansicht {
		display: none;
	}

	@page klausurplanung-detail {
		size: A4 landscape;
		margin: 12.1mm 12.1mm 5.6mm 12.1mm;
	}

	@media print {
		body:has(.klausurplanung-detail-druckansicht) #app {
			display: none !important;
		}

		.klausurplanung-detail-druckansicht {
			display: block !important;
			page: klausurplanung-detail;
		}

		.klausurplanung-detail-druckansicht h1 {
			font-size: 1.5rem;
			margin: 0 0 1rem;
		}

		.klausurplanung-detail-drucktermin {
			break-inside: avoid;
			break-inside: avoid-page;
			page-break-inside: avoid;
		}

		.klausurplanung-detail-drucktermin + .klausurplanung-detail-drucktermin {
			margin-top: 2rem;
		}

		.klausurplanung-detail-drucktermin table {
			break-inside: avoid;
			break-inside: avoid-page;
			page-break-inside: avoid;
		}

		.klausurplanung-detail-drucktermin > div > table {
			margin-left: 0 !important;
			margin-right: 0 !important;
		}
	}

</style>
