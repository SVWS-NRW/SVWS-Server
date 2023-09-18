<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Abiturjahr hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-checkbox v-for="jahrgang in mapJahrgaengeOhneAbiJahrgang.values()" :key="jahrgang.id" :model-value="jahrgaenge.get(jahrgang.id)" @update:model-value="updateMap(jahrgang.id, $event)" :value="jahrgang.id">
					Abitur {{ props.getAbiturjahrFuerJahrgang(jahrgang.id) }} (Jahrgang {{ jahrgang.kuerzel }})
				</svws-ui-checkbox>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="clickAddAbiturjahrgang()" :disabled="![...jahrgaenge.values()].includes(true)"> Abiturjahrgänge anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { JahrgangsListeEintrag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		mapJahrgaengeOhneAbiJahrgang: Map<number, JahrgangsListeEintrag>;
		addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
		getAbiturjahrFuerJahrgang: (idJahrgang: number) => number;
	}>();

	const modal = ref();
	const jahrgaenge = ref(new Map<number, boolean>());

	function updateMap(id: number, ok: any) {
		if (typeof ok === 'boolean')
			jahrgaenge.value.set(id, ok);
	}

	const clickAddAbiturjahrgang = async () => {
		for (const [id, ok] of jahrgaenge.value.entries()) {
			if (ok === true) {
				await props.addAbiturjahrgang(id);
				console.log(id, ok)
			}
		}
		jahrgaenge.value.clear();
		modal.value.closeModal();
	}

	const openModal = () => {
		modal.value.openModal();
	}

</script>
