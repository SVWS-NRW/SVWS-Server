<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Anmeldedaten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Status"
					:model-value="statusNeuaufnahme?.text"
					readonly />
				<ui-select label="Schuljahresabschnitt"
					v-model="model.schuljahresabschnitt.value"
					:manager="schuljahresabschnittManager"
					:validation="() => model.getFehler('idSchuljahresabschnitt')"
					required />
				<ui-select label="Jahrgang"
					v-model="model.jahrgang.value"
					:manager="jahrgangManager"
					:validation="() => model.getFehler('idJahrgang')"
					:disabled="(model.proxy.idSchuljahresabschnitt <= 0)"
					required />
				<ui-select label="Klasse"
					v-model="model.klasse.value"
					:manager="klassenManager"
					:disabled="(model.proxy.idJahrgang === null)" />
				<svws-ui-text-input placeholder="Anmeldedatum" type="date"
					v-model="model.anmeldedatum.value" />
				<svws-ui-text-input placeholder="Aufnahmedatum" type="date"
					v-model="model.aufnahmedatum.value" />
				<ui-select label="Einschulungsart" v-if="schulenMitPrimaerstufe"
					v-model="model.einschulungsart.value"
					:manager="einschulungsartManager"
					:removable="true" />
				<svws-ui-text-input placeholder="Beginn Bildungsgang" type="date" v-if="schulenMitBKoderSK"
					v-model="model.proxy.beginnBildungsgang" />
				<svws-ui-input-number placeholder="Dauer Bildungsgang" v-if="schulenMitBKoderSK"
					v-model="model.proxy.dauerBildungsgang" />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Persönliche Daten" class="col-span-full">
			<svws-ui-input-wrapper :grid="4">
				<svws-ui-text-input placeholder="Nachname"
					v-model="model.proxy.nachname"
					:validation="() => model.getFehler('nachname')"
					:min-len="1" :max-len="120" required />
				<svws-ui-text-input placeholder="Rufname"
					v-model="model.proxy.vorname"
					:validation="() => model.getFehler('vorname')"
					:min-len="1" :max-len="80" required />
				<svws-ui-text-input placeholder="Alle Vornamen"
					v-model="model.proxy.alleVornamen"
					:validation="() => model.getFehler('alleVornamen')"
					:max-len="255" />
				<div />
				<svws-ui-text-input placeholder="Geburtsdatum" type="date"
					v-model="model.proxy.geburtsdatum"
					:validation="() => model.getFehler('geburtsdatum')"
					required />
				<ui-select label="Geschlecht"
					v-model="model.geschlecht.value"
					:manager="geschlechtManager"
					:validation="() => model.getFehler('geschlecht')"
					:removable="false" required />
				<ui-select label="Religion"
					v-model="model.religion.value"
					:manager="religionManager"
					:validation="() => model.getFehler('idReligion')"
					:removable="false" required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addSchueler" :disabled="(!formIsValid) || (!hatKompetenzUpdate)">
					Anlegen
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
	</div>
	<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
</template>

<script setup lang="ts">

	import type { SchuelerNeuProps } from "~/components/schueler/neuanlage/SchuelerNeuProps";
	import type { Schuljahresabschnitt } from "@core";
	import { BenutzerKompetenz, Geschlecht, SchuelerNeu, SchuelerStatus, Schulform } from "@core";
	import { computed, ref, watch } from "vue";
	import { SelectManager, useBenutzerState, useSchuleState } from "@ui";
	import { SchuelerNeuModelProxy } from "~/components/schueler/neuanlage/modelproxy/SchuelerNeuModelProxy";

	const props = defineProps<SchuelerNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();

	const manager = () => props.manager();
	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHUELER_INDIVIDUALDATEN_AENDERN));
	const isLoading = ref<boolean>(false);
	const schulenMitBKoderSK = computed(() => (schuleState.schulform === Schulform.BK) || (schuleState.schulform === Schulform.SK));
	const schulenMitPrimaerstufe = computed(() => {
		const erlaubteSchulformen = [Schulform.G, Schulform.FW, Schulform.WF, Schulform.GM, Schulform.KS, Schulform.S, Schulform.GE, Schulform.V];
		return erlaubteSchulformen.includes(schuleState.schulform);
	});
	const schuljahr = manager().aktuellerAbschnitt.schuljahr;
	const statusNeuaufnahme = SchuelerStatus.NEUAUFNAHME.daten(schuljahr);

	const abschnitteFiltered = computed(() => manager().schuljahresabschnitteFilteredById.values());
	const jahrgaenge = computed(() => Array.from(manager().jahrgaengeById.values()));
	const einschulungsarten = computed(() => manager().einschulungsartenById.values());
	const religionen = computed(() => manager().religionenById.values());

	const initialData = ref<SchuelerNeu>(Object.assign(new SchuelerNeu(), { status: statusNeuaufnahme?.id ?? -1 }));
	const model = new SchuelerNeuModelProxy(() => initialData.value, () => manager());

	const formIsValid = computed<boolean>(() => model.getAlleFehler().isEmpty());

	// --- manager ---

	const schuljahresabschnittManager = new SelectManager({
		options: abschnitteFiltered,
		optionDisplayText: schuljahresabschnittText,
		selectionDisplayText: schuljahresabschnittText,
	});

	const jahrgangManager = new SelectManager({
		options: jahrgaenge,
		optionDisplayText: j => j.bezeichnung ?? '',
		selectionDisplayText: j => j.bezeichnung ?? '',
	});

	const klassenManager = new SelectManager({
		options: model.klassen,
		optionDisplayText: k => k.kuerzel ?? "",
		selectionDisplayText: k => k.kuerzel ?? "",
	});

	const einschulungsartManager = new SelectManager({
		options: einschulungsarten,
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: i => i.text,
		selectionDisplayText: i => i.text,
	});

	const religionManager = new SelectManager({
		options: religionen,
		optionDisplayText: r => r.bezeichnung,
		selectionDisplayText: r => r.bezeichnung,
	});

	// --- util ---

	function schuljahresabschnittText(value: Schuljahresabschnitt) {
		return value.schuljahr > 0 ? `${value.schuljahr}/${(value.schuljahr + 1) % 100}.${value.abschnitt}` : "Abschnitt";
	}

	async function addSchueler() {
		if (isLoading.value) {
			return;
		}
		isLoading.value = true;
		props.checkpoint.active = false;
		const result = await props.add(model.proxy);
		isLoading.value = false;
		await props.gotToSchnelleingabe(result.id);
	}

	function cancel() {
		props.checkpoint.active = false;
		void props.gotoDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}
		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
