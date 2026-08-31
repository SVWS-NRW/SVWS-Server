<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card>
			<svws-ui-content-card title="Allgemein">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Name" class="contentFocusField"
						v-model="model.proxy.name"
						:validation="() => model.getFehler('name')"
						:max-len="50" required />
					<svws-ui-text-input placeholder="Namensergänzung"
						v-model="model.proxy.nameZusatz"
						:validation="() => model.getFehler('nameZusatz')"
						:max-len="50" />
					<ui-select label="Betriebsart"
						v-model="model.betriebsart.value"
						:manager="betriebsartenManager"
						searchable />
					<svws-ui-text-input placeholder="Branche"
						v-model="model.proxy.branche"
						:validation="() => model.getFehler('branche')"
						:max-len="50" />
					<svws-ui-textarea-input placeholder="Bemerkungen"
						v-model="model.proxy.bemerkungen"
						:validation="() => model.getFehler('bemerkungen')"
						resizeable="none" :max-len="255" />
					<div>
						<svws-ui-input-wrapper :grid="1">
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="model.proxy.istAusbildungsbetrieb">
								Ausbildungsbetrieb
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.istMassnahmentraeger">
								Maßnahmenträger
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.belehrungNachISGErforderlich">
								Belehrung nach ISG notwendig
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.bietetPraktikumsplaetzeAn">
								Bietet Praktikumsplätze
							</svws-ui-checkbox>
							<svws-ui-checkbox v-model="model.proxy.erweitertesFuehrungszeugnisErforderlich">
								Erweitertes Führungszeugnis notwendig
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-spacing :size="2" />
			<svws-ui-content-card title="Adresse">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Straße"
						v-model="model.adresse.value"
						:validation="() => model.getFehler('strasse')" />
					<ui-select label="Wohnort"
						v-model="model.wohnort.value"
						:manager="wohnortManager"
						searchable />
					<svws-ui-text-input placeholder="Telefon" type="tel"
						v-model="model.proxy.telefon1"
						:validation="() => model.getFehler('telefon1')"
						:max-len="20" />
					<svws-ui-text-input placeholder="2. Telefon" type="tel"
						v-model="model.proxy.telefon2"
						:validation="() => model.getFehler('telefon2')"
						:max-len="20" />
					<svws-ui-text-input placeholder="E-Mail-Adresse" type="email"
						v-model="model.proxy.eMail"
						:validation="() => model.getFehler('eMail')"
						:max-len="100" />
					<svws-ui-text-input placeholder="Fax" type="tel"
						v-model="model.proxy.fax"
						:validation="() => model.getFehler('fax')"
						:max-len="20" />
					<svws-ui-spacing :size="2" />
					<svws-ui-content-card title="Ansicht & Sortierung">
						<svws-ui-input-wrapper :grid="2">
							<svws-ui-input-number placeholder="Sortierung"
								v-model="model.proxy.sortierung"
								:validation="() => model.getFehler('sortierung')"
								:min="0"
								:disabled
								:removeable="false" required />
							<svws-ui-spacing />
							<svws-ui-checkbox v-model="model.proxy.istSichtbar">
								Sichtbar
							</svws-ui-checkbox>
						</svws-ui-input-wrapper>
					</svws-ui-content-card>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">
					Abbrechen
				</svws-ui-button>
				<svws-ui-button @click="addBetriebe" :disabled="!formIsValid">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import { BenutzerKompetenz, Betrieb, type Betriebsart, type OrtKatalogEintrag } from "@core";
	import type { BetriebeNeuProps } from "~/components/schule/kataloge/betriebe/BetriebeNeuProps";
	import { SelectManager, useBenutzerState } from "@ui";
	import { BetriebModelProxy } from "~/components/schule/kataloge/betriebe/modelproxy/BetriebModelProxy";

	const props = defineProps<BetriebeNeuProps>();
	const benutzerState = useBenutzerState();

	const initialData = ref<Betrieb>(Object.assign(new Betrieb(), { istSichtbar: true, sortierung: 32000, anzahlRestabschnitte: 0 }));
	const model = new BetriebModelProxy(() => initialData.value, () => props.manager());
	const isLoading = ref<boolean>(false);
	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.SCHULBEZOGENE_DATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);
	const formIsValid = computed(() => model.getAlleFehler().isEmpty());
	const betriebsartenById = computed<Map<number, Betriebsart>>(() => props.manager().betriebsartenById);
	const orteById = computed<Map<number, OrtKatalogEintrag>>(() => props.manager().orteById);
	const betriebsarten = computed(() => betriebsartenById.value.values());
	const orte = computed(() => orteById.value.values());

	const betriebsartenManager = new SelectManager({
		options: betriebsarten,
		optionDisplayText: v => v.bezeichnung,
		selectionDisplayText: v => v.bezeichnung,
	});

	const wohnortManager = new SelectManager({
		options: orte,
		optionDisplayText: v => v.plz + ' ' + v.ortsname,
		selectionDisplayText: v => v.plz + ' ' + v.ortsname,
	});

	async function addBetriebe() {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = false;
		isLoading.value = true;
		const { id, referenziertInAnderenTabellen, ansprechpartner, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.goToDefaultView(null);
	}

	watch(() => model.proxy, async () => {
		if (isLoading.value) {
			return;
		}

		props.checkpoint.active = true;
	}, { immediate: false, deep: true });

</script>
