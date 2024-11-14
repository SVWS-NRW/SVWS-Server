<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium" class="hidden">
		<template #modalTitle>Vermerkart Hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input	:valid="validatorVermerkBezeichnung" v-model="vermerkart.bezeichnung" type="text" placeholder="Bezeichnung" />
				<div v-if="!validatorVermerkBezeichnung(vermerkart.bezeichnung) && (vermerkart.bezeichnung.length > 0)" class="flex my-auto">
					<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
					<p> Diese Bezeichnung wird bereits verwendet </p>
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!validatorVermerkBezeichnung(vermerkart.bezeichnung) || (vermerkart.bezeichnung.length === 0)"> Speichern </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { VermerkartenManager } from '@core';
	import { VermerkartEintrag } from '@core';

	const props = defineProps<{
		addEintrag: (vermerkart: Partial<VermerkartEintrag>) => Promise<void>;
		vermerkartenManager : () => VermerkartenManager;
	}>();

	const show = ref<boolean>(false);

	const vermerkart = ref(new VermerkartEintrag());

	function validatorVermerkBezeichnung(value: string |null) : boolean {
		if (value === null)
			return true;
		for (const eintrag of props.vermerkartenManager().liste.list())
			if (eintrag.bezeichnung === value.toString().trim())
				return false;
		return true;
	}

	async function saveEntries() : Promise<void> {
		const postCandidate: Partial<VermerkartEintrag> = {bezeichnung: vermerkart.value.bezeichnung};
		show.value = false;
		await props.addEintrag(postCandidate);
	}

	function openModal() : void {
		vermerkart.value = new VermerkartEintrag();
		show.value = true;
	}

</script>
