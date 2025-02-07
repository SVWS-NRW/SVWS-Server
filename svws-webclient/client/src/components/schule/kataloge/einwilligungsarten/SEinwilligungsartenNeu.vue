<template>
	<div class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<div>
						<svws-ui-text-input	:valid="validatorEinwilligungsartBezeichnung" v-model="einwilligungsart.bezeichnung" type="text" placeholder="Bezeichnung" />
						<div v-if="!validatorEinwilligungsartBezeichnung(einwilligungsart.bezeichnung) && (einwilligungsart.bezeichnung.length > 0)" class="flex my-auto">
							<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
							<p> Diese Bezeichnung wird bereits verwendet </p>
						</div>
					</div>
					<div>
						<svws-ui-text-input :valid="validatorEinwilligungsartSchluessel" v-model="einwilligungsart.schluessel" type="text" placeholder="Schlüssel" />
						<div v-if="!validatorEinwilligungsartSchluessel(einwilligungsart.schluessel) && (einwilligungsart.schluessel.length > 0)" class="flex my-auto">
							<span class="icon i-ri-alert-line mx-0.5 mr-1 inline-flex" />
							<p> Dieser Schlüssel wird bereits verwendet </p>
						</div>
					</div>
					<svws-ui-textarea-input v-model="einwilligungsart.beschreibung" type="text" placeholder="Beschreibung" class="col-span-full" />
					<svws-ui-select v-model="auswahlPersonTyp" :items="personTypen" :item-text="item => item.bezeichnung" placeholder="Personenart" label="Personenart" class="col-span-full" />
				</svws-ui-input-wrapper>
				<div class="mt-7 flex flex-row gap-4 justify-end">
					<svws-ui-button type="secondary" @click="cancel" :disabled="isLoading">Abbrechen</svws-ui-button><svws-ui-button @click="addEinwilligungsart"
						:disabled="((!validatorEinwilligungsartBezeichnung(einwilligungsart.bezeichnung) || !validatorEinwilligungsartSchluessel(einwilligungsart.schluessel)) ||
							isLoading || (einwilligungsart.bezeichnung === ''))">
						Speichern <svws-ui-spinner :spinning="isLoading" />
					</svws-ui-button>
				</div>
			</svws-ui-content-card>
		</div>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import { Einwilligungsart, PersonTyp } from "@core";
	import type { SchuleEinwilligungsartenNeuProps } from "~/components/schule/kataloge/einwilligungsarten/SEinwilligungsartenNeuProps";

	const props = defineProps<SchuleEinwilligungsartenNeuProps>();
	const einwilligungsart = ref(new Einwilligungsart());
	const isLoading = ref<boolean>(false);

	function validatorEinwilligungsartBezeichnung(value: string | null): boolean {
		if (value === null)
			return true;
		for (const eintrag of props.manager().liste.list())
			if (eintrag.bezeichnung === value.trim())
				return false;
		return true;
	}

	function validatorEinwilligungsartSchluessel(value: string | null): boolean {
		if ((value === null) || (value.trim() === ""))
			return true;
		for (const eintrag of props.manager().liste.list())
			if (eintrag.schluessel === value.trim())
				return false;
		return true;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	const personTypen = computed<PersonTyp[]>(() => [PersonTyp.LEHRER, PersonTyp.SCHUELER]);

	const auswahlPersonTyp = computed<PersonTyp>({
		get: () => PersonTyp.getByID(einwilligungsart.value.personTyp) ?? PersonTyp.SCHUELER,
		set: (value) => {
			const tmp = new Einwilligungsart();
			tmp.bezeichnung = einwilligungsart.value.bezeichnung;
			tmp.schluessel = einwilligungsart.value.schluessel;
			tmp.beschreibung = einwilligungsart.value.beschreibung;
			tmp.personTyp = value.id;
			einwilligungsart.value = tmp;
		},
	});

	async function addEinwilligungsart() {
		if (isLoading.value === true)
			return;
		isLoading.value = true;
		props.checkpoint.active = false;
		await props.add({
			bezeichnung: einwilligungsart.value.bezeichnung.trim(),
			personTyp: einwilligungsart.value.personTyp,
			schluessel: einwilligungsart.value.schluessel.trim(),
			beschreibung: einwilligungsart.value.beschreibung?.trim(),
		});
		einwilligungsart.value = new Einwilligungsart();
		isLoading.value = false;
	}
</script>
