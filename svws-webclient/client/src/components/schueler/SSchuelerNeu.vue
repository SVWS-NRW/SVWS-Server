<template>
	<div class="page page-grid-cards" v-if="data.id === 0">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<ui-select label="Status" v-model="selectedStatus" :manager="statusManager" :removable="false" :readonly="true" />
				<svws-ui-text-input placeholder="Schuljahr" required type="text" :readonly="true" />
				<svws-ui-text-input placeholder="Halbjahr" type="text" required :readonly="true" />
				<svws-ui-text-input placeholder="Jahrgang" type="text" required :readonly="true" />
				<svws-ui-text-input placeholder="Klasse" type="text" required :readonly="true" />
				<svws-ui-spacing />
				<ui-select label="Einschulungsart" v-model="einschulungsart" :manager="einschulungsartManager" :removable="true" v-if="schulenMitPrimaerstufe" />
				<!--TODO Anmeldedatum darf nicht in der Zukunft liegen-->
				<svws-ui-text-input placeholder="Anmeldedatum" type="date" v-model="data.anmeldedatum" />
				<!--TODO Aufnahmedatum darf nicht vor dem Anmeldedatum liegen-->
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date" v-model="data.aufnahmedatum" />
				<!--TODO Beginn Bildungsgang darf nicht vor dem Aufnahmedatum liegen-->
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-model="data.beginnBildungsgang" />
				<svws-ui-text-input placeholder="Dauer Bildungsgang" type="date" readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Name" required v-model="data.nachname" :valid="fieldIsValid('nachname')" />
				<svws-ui-text-input placeholder="Vorname" required v-model="data.vorname" :valid="fieldIsValid('vorname')" />
				<svws-ui-text-input placeholder="Weitere Vornamen" v-model="data.alleVornamen" :valid="fieldIsValid('alleVornamen')" />
				<ui-select label="Geschlecht" v-model="geschlecht" :manager="geschlechtManager" :removable="false" required />
				<svws-ui-text-input placeholder="Geburtsdatum" required type="date" v-model="data.geburtsdatum" :valid="fieldIsValid('geburtsdatum')" />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addSchuelerStammdaten" :disabled="(!formIsValid) || (!hatKompetenzUpdate)">Anlegen</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/SSchuelerNeuProps";
	import { Geschlecht, JavaString, SchuelerStammdaten, SchuelerStatus, Schulform, SchuelerSchulbesuchsdaten, BenutzerKompetenz, type SchuelerStatusKatalogEintrag } from "@core";
	import { computed, ref, watch } from "vue";
	import { CoreTypeSelectManager, SelectManager } from "@ui";

	const props = defineProps<SchuelerNeuProps>();

	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));

	const schuljahr = computed<number>(() => props.aktAbschnitt.schuljahr);

	const data = ref(new SchuelerStammdaten());
	const dataSchulbesuchsdaten = ref(new SchuelerSchulbesuchsdaten());
	const isLoading = ref<boolean>(false);

	watch(() => data.value, async() => {
		if (isLoading.value)
			return;
		props.checkpoint.active = true;
	}, {immediate: false, deep: true});

	//TODO Schulform.GY aus dem Array entfernen
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [ Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V, Schulform.GY];
		return erlaubteSchulformen.includes(props.schulform);
	});

	//validation logic
	function fieldIsValid(field: keyof SchuelerStammdaten | null):(v: string | null) => boolean {
		return (v: string | null) => {
			switch (field) {
				case 'nachname':
					return stringIsValid(data.value.nachname, true, 120);
				case 'vorname':
					return stringIsValid(data.value.vorname, true, 120);
				case 'alleVornamen':
					return stringIsValid(data.value.alleVornamen, false, 120);
				default:
					return true;
			}
		}
	}

	const formIsValid = computed(() => {
		return Object.keys(data.value).every(field => {
			const validateField = fieldIsValid(field as keyof SchuelerStammdaten);
			const fieldValue = data.value[field as keyof SchuelerStammdaten] as string | null;
			return validateField(fieldValue);
		});
	});

	function stringIsValid(input: string | null, mandatory: boolean, maxLength: number) {
		if (mandatory)
			return (input !== null) && (!JavaString.isBlank(input)) && (input.length <= maxLength);
		return (input === null) || (input.length <= maxLength);
	}

	const statusManager = new CoreTypeSelectManager({ clazz: SchuelerStatus.class, schuljahr: schuljahr, schulformen: props.schulform, optionDisplayText: "text", selectionDisplayText: "text" });

	const selectedStatus = computed<SchuelerStatusKatalogEintrag | null>({
		get: () :SchuelerStatusKatalogEintrag | null => SchuelerStatus.data().getWertByKuerzel('' + data.value.status)?.daten(schuljahr.value) ?? null,
		set: (value) => data.value.status = value?.id ?? -1,
	});

	const einschulungsarten = computed(() => props.mapEinschulungsarten.values());
	const einschulungsartManager = new SelectManager({ options: einschulungsarten.value, optionDisplayText: i => i.text, selectionDisplayText: i => i.text });

	const einschulungsart = computed({
		get: () => props.mapEinschulungsarten.get(dataSchulbesuchsdaten.value.grundschuleEinschulungsartID ?? -1) ?? null,
		set: (value) => dataSchulbesuchsdaten.value.grundschuleEinschulungsartID = value?.id ?? -1,
	});

	const geschlechtManager = new SelectManager({ options: Geschlecht.values(), optionDisplayText: (i) => i.text, selectionDisplayText: (i) => i.text });

	const geschlecht = computed({
		get: () => Geschlecht.fromValue(data.value.geschlecht) ?? null,
		set: (value: any) => data.value.geschlecht = value?.id ?? -1 });

	async function addSchuelerStammdaten() {
		if (isLoading.value)
			return;

		isLoading.value = true;
		props.checkpoint.active = false;

		const { id, ...partialData } = data.value;
		const result = await props.addSchueler(partialData);

		const { grundschuleEinschulungsartID } = dataSchulbesuchsdaten.value;
		const schulbesuchsdaten: Partial<SchuelerSchulbesuchsdaten> = {
			grundschuleEinschulungsartID,
		};
		await props.patchSchuelerSchulbesuchdaten(schulbesuchsdaten, result.id);
		isLoading.value = false;
		// await props.gotoSchnelleingabeView(true, result.id);
	}

	function cancel() {
		props.checkpoint.active = false;
		props.schuelerListeManager().schuelerstatus.auswahlClear();
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.AKTIV);
		props.schuelerListeManager().schuelerstatus.auswahlAdd(SchuelerStatus.EXTERN);
		void props.gotoDefaultView(null);
	}

</script>
