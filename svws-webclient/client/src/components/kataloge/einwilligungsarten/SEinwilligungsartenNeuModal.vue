<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Einwilligungsart hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="eintragBezeichnung" :valid="isValidBezeichnung" type="text" placeholder="Bezeichnung" />
				<div>
					<div v-if="!eintragBezeichnung" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Feld darf nicht leer sein </p>
					</div>
					<div v-else-if="existsBezeichnungFuerPersonTyp()" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Diese Bezeichnung existiert bereits für {{ getBezeichnungTyp(einwilligung.personTyp) }} </p>
					</div>
				</div>
				<svws-ui-text-input v-model="eintragSchluessel" :valid="isValidSchluessel" type="text" placeholder="Schlüssel" />
				<div>
					<div v-if="!eintragSchluessel" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Feld darf nicht leer sein </p>
					</div>
					<div v-else-if="existsSchluesselFuerPersonTyp()" class="flex mt-2">
						<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
						<p> Dieser Schlüssel existiert bereits für {{ getBezeichnungTyp(einwilligung.personTyp) }} </p>
					</div>
				</div>
				<svws-ui-textarea-input v-model="eintragBeschreibung" type="text" placeholder="Beschreibung" class="col-span-full" />
				<svws-ui-select v-model="auswahlPersonTyp" :items="personTypen" :item-text="item => item.bezeichnung" placeholder="Personentyp" label="Personentyp" class="col-span-full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="saveEntries()" :disabled="!isValidBezeichnung() || !isValidSchluessel()">
				Speichern
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Einwilligungsart, PersonTyp } from '@core';
	import { computed, ref, shallowRef } from 'vue';

	const props = defineProps<{
		addEintrag: (einwilligungsart: Partial<Einwilligungsart>) => Promise<void>;
		mapKatalogeintraege: Map<number, Einwilligungsart>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const einwilligung = shallowRef(new Einwilligungsart());

	const personTypen = computed<PersonTyp[]>(() => PersonTyp.values());

	const eintragBezeichnung = computed<string>({
		get: () => einwilligung.value.bezeichnung ?? "",
		set: (value) => {
			const tmp = new Einwilligungsart();
			tmp.bezeichnung = value;
			tmp.schluessel = einwilligung.value.schluessel;
			tmp.beschreibung = einwilligung.value.beschreibung;
			tmp.personTyp = einwilligung.value.personTyp;
			einwilligung.value = tmp;
		}
	});

	const eintragSchluessel = computed<string>({
		get: () => einwilligung.value.schluessel ?? "",
		set: (value) => {
			const tmp = new Einwilligungsart();
			tmp.bezeichnung = einwilligung.value.bezeichnung;
			tmp.schluessel = value;
			tmp.beschreibung = einwilligung.value.beschreibung;
			tmp.personTyp = einwilligung.value.personTyp;
			einwilligung.value = tmp;
		}
	});

	const eintragBeschreibung = computed<string>({
		get: () => einwilligung.value.beschreibung ?? "",
		set: (value) => {
			const tmp = new Einwilligungsart();
			tmp.bezeichnung = einwilligung.value.bezeichnung;
			tmp.schluessel = einwilligung.value.schluessel;
			tmp.beschreibung = value;
			tmp.personTyp = einwilligung.value.personTyp;
			einwilligung.value = tmp;
		}
	});

	const auswahlPersonTyp = computed<PersonTyp>({
		get: () => PersonTyp.getByID(einwilligung.value.personTyp) ?? PersonTyp.SCHUELER,
		set: (value) => {
			const tmp = new Einwilligungsart();
			tmp.bezeichnung = einwilligung.value.bezeichnung;
			tmp.schluessel = einwilligung.value.schluessel;
			tmp.beschreibung = einwilligung.value.beschreibung;
			tmp.personTyp = value.id;
			einwilligung.value = tmp;
		}
	});

	function isValidBezeichnung(): boolean {
		return !!einwilligung.value.bezeichnung && !existsBezeichnungFuerPersonTyp();
	}

	function existsBezeichnungFuerPersonTyp(): boolean {
		for (const eintrag of filterMapByPersonTyp(einwilligung.value.personTyp))
			if (eintrag.bezeichnung === einwilligung.value.bezeichnung.trim())
				return true;
		return false;
	}

	function isValidSchluessel(): boolean {
		return !!einwilligung.value.schluessel && !existsSchluesselFuerPersonTyp();
	}

	function existsSchluesselFuerPersonTyp(): boolean {
		for (const eintrag of filterMapByPersonTyp(einwilligung.value.personTyp))
			if (eintrag.schluessel === einwilligung.value.schluessel.trim())
				return true;
		return false;
	}

	function filterMapByPersonTyp(personTyp: number) {
		return Array.from(props.mapKatalogeintraege.values()).filter(ele => ele.personTyp === personTyp);
	}

	function getBezeichnungTyp(typId: number): string | undefined {
		return PersonTyp.getByID(typId)?.bezeichnung;
	}

	async function saveEntries() {
		await props.addEintrag({
			bezeichnung: einwilligung.value.bezeichnung.trim(),
			personTyp: einwilligung.value.personTyp,
			schluessel: einwilligung.value.schluessel.trim(),
			beschreibung: einwilligung.value.beschreibung?.trim()
		});
		showModal().value = false;
	}

	const openModal = () => {
		einwilligung.value = new Einwilligungsart();
		einwilligung.value.personTyp = PersonTyp.SCHUELER.id;
		showModal().value = true;
	}

</script>
