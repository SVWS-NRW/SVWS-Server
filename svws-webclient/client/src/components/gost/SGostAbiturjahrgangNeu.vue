<template>
	<div class="page">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper>
					<template v-for="jahrgang in mapJahrgaengeOhneAbiJahrgang().values()" :key="jahrgang.id">
						<svws-ui-checkbox :model-value="jahrgaenge.has(jahrgang.id)" @update:model-value="ok => updateMap(jahrgang.id, ok)" :value="jahrgang.id">
							Abitur {{ props.getAbiturjahrFuerJahrgang(jahrgang.id) }} (Jahrgang {{ jahrgang.kuerzel }})
						</svws-ui-checkbox>
					</template>
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4">
					<svws-ui-button type="secondary" @click="close"> Abbrechen </svws-ui-button>
					<svws-ui-button type="secondary" @click="clickAddAbiturjahrgang" :disabled="jahrgaenge.size === 0">{{ jahrgaenge.size === 1 ? 'Abiturjahrgang' : 'Abiturjahrgänge' }} anlegen <svws-ui-spinner :spinning /></svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { GostAbiturjahrgangNeuProps } from "./SGostAbiturjahrgangNeuProps";

	const props = defineProps<GostAbiturjahrgangNeuProps>();

	const spinning = ref<boolean>(false);
	const jahrgaenge = ref(new Set<number>());

	function updateMap(id: number, ok: boolean) {
		if (ok)
			jahrgaenge.value.add(id);
		else
			jahrgaenge.value.delete(id);
	}

	async function clickAddAbiturjahrgang() {
		spinning.value = true;
		for (const id of jahrgaenge.value.values())
			await props.addAbiturjahrgang(id);
		jahrgaenge.value.clear();
		spinning.value = false;
	}

	async function close() {
		jahrgaenge.value.clear();
		await props.cancelCreationMode()
	}

</script>
