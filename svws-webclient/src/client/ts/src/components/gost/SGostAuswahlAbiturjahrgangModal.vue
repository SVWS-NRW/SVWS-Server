<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Abiturjahr hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-button type="transparent" v-for="jahrgang in mapJahrgaengeOhneAbiJahrgang.values()" :key="jahrgang.id" @click="props.addAbiturjahrgang(jahrgang.id)" :title="`Stufe ${jahrgang.kuerzel} hinzufügen`">
					{{ jahrgang.kuerzel }}
				</svws-ui-button>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { JahrgangsListeEintrag } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		mapJahrgaengeOhneAbiJahrgang: Map<number, JahrgangsListeEintrag>;
		addAbiturjahrgang: (idJahrgang: number) => Promise<void>;
	}>();

	const modal = ref();

	const openModal = () => {
		modal.value.openModal();
	}
</script>
