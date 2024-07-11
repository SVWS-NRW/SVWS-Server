<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Einwilligungsart hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="einwilligung.bezeichnung" :valid="isValidBezeichnung" type="text" placeholder="Bezeichnung" />
				<div>
					<div v-if="!einwilligung.bezeichnung" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Feld darf nicht leer sein </p>
					</div>
					<div v-else-if="existsBezeichnung()" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Diese Bezeichnung wird bereits verwendet </p>
					</div>
				</div>
				<svws-ui-text-input v-model="einwilligung.schluessel" :valid="() => !!einwilligung.schluessel" type="text" placeholder="Schlüssel" />
				<div>
					<div v-if="!einwilligung.schluessel" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Feld darf nicht leer sein </p>
					</div>
				</div>
				<svws-ui-select v-model="auswahlPersonTyp" :items="personTypen" :item-text="item => item.bezeichnung" placeholder="Personentyp" label="Personentyp" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!einwilligung.schluessel || !isValidBezeichnung()">
				Speichern
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Einwilligungsart, PersonTyp } from '@core';
	import { computed, ref } from 'vue';

	const props = defineProps<{
		addEintrag: (einwilligungsart: Partial<Einwilligungsart>) => Promise<void>;
		mapKatalogeintraege: Map<number, Einwilligungsart>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const einwilligung = ref(new Einwilligungsart());

	const personTypen = computed<PersonTyp[]>(() => PersonTyp.values());

	const auswahlPersonTyp = computed<PersonTyp>({
		get: () => PersonTyp.getByID(einwilligung.value.personTyp) ?? PersonTyp.SCHUELER,
		set: (value) => einwilligung.value.personTyp = value.id
	});

	function isValidBezeichnung(): boolean {
		return !!einwilligung.value.bezeichnung && !existsBezeichnung();
	}

	function existsBezeichnung(): boolean {
		for (const eintrag of props.mapKatalogeintraege.values())
			if (eintrag.bezeichnung === einwilligung.value.bezeichnung.trim())
				return true;
		return false;
	}

	async function saveEntries() {
		await props.addEintrag({bezeichnung: einwilligung.value.bezeichnung, personTyp: einwilligung.value.personTyp, schluessel: einwilligung.value.schluessel});
		showModal().value = false;
	}

	const openModal = () => {
		einwilligung.value = new Einwilligungsart();
		einwilligung.value.personTyp = PersonTyp.SCHUELER.id;
		showModal().value = true;
	}

</script>
