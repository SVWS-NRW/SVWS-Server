<template>
	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-input-wrapper>
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :disabled>
						Ist Sichtbar
					</svws-ui-checkbox>
					<svws-ui-checkbox v-model="model.proxy.istRelevantFuerStatistik" :disabled>
						Ist relevant für Statistik
					</svws-ui-checkbox>
				</svws-ui-input-wrapper>
				<svws-ui-text-input placeholder="Kürzel"
					v-model="model.proxy.kuerzel"
					:validation="() => model.getFehler('kuerzel')"
					:max-len="10" required
					statistics focus :disabled />
				<ui-select label="Personal-Typ"
					v-model="model.personalTyp.value"
					:manager="personaltypManger"
					:validation="() => model.getFehler('personalTyp')"
					required :removable="false" :disabled />
				<svws-ui-text-input placeholder="Nachname"
					v-model="model.proxy.nachname"
					:validation="() => model.getFehler('nachname')"
					:max-len="120" required
					statistics :disabled />
				<svws-ui-text-input placeholder="Rufname"
					v-model="model.proxy.vorname"
					:validation="() => model.getFehler('vorname')"
					:max-len="80" required
					statistics :disabled />
				<svws-ui-spacing />
				<ui-select label="Geschlecht"
					v-model="model.geschlecht.value"
					:manager="geschlechtManager"
					:validation="() => model.getFehler('geschlecht')"
					required
					:removable="false" :disabled />
				<svws-ui-text-input placeholder="Geburtsdatum"
					type="date"
					v-model="model.proxy.geburtsdatum"
					:validation="() => model.getFehler('geburtsdatum')"
					statistics :disabled />
				<ui-select label="Staatsangehörigkeit"
					v-model="model.staatsangehoerigkeit.value"
					:manager="staatsangehoerigkeitManager"
					:validation="() => model.getFehler('idStaatsangehoerigkeit')"
					required
					statistics :removable="false" :disabled />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akademischer Grad"
					v-model="model.proxy.titel"
					:validation="() => model.getFehler('titel')"
					:max-len="20"
					:disabled />
				<svws-ui-text-input placeholder="Amtsbezeichnung"
					v-model="model.proxy.amtsbezeichnung"
					:validation="() => model.getFehler('amtsbezeichnung')"
					:max-len="15"
					:disabled />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="cancel">Abbrechen</svws-ui-button>
				<svws-ui-button @click="addLehrer"
					:disabled="!model.getAlleBlockierendenFehler().isEmpty() || !hatKompetenzAdd">
					Speichern
				</svws-ui-button>
			</div>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße"
					span="full"
					v-model="model.adresse.value"
					:validation="() => model.getFehler('strassenname')"
					:max-len="55"
					:disabled />
				<ui-select label="Wohnort"
					v-model="model.wohnort.value"
					:manager="wohnortManager"
					:validation="() => model.getFehler('wohnortID')"
					:disabled />
				<ui-select label="Ortsteil"
					v-model="model.ortsteil.value"
					:manager="ortsteilManager"
					:validation="() => model.getFehler('ortsteilID')"
					:disabled />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon"
					type="tel"
					v-model="model.proxy.telefon"
					:validation="() => model.getFehler('telefon')"
					:max-len="20"
					:disabled />
				<svws-ui-text-input placeholder="Mobil oder Fax"
					type="tel"
					v-model="model.proxy.telefonMobil"
					:validation="() => model.getFehler('telefonMobil')"
					:max-len="20"
					:disabled />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse"
					type="email"
					v-model="model.proxy.emailPrivat"
					:validation="() => model.getFehler('emailPrivat')"
					:max-len="100"
					:disabled />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse"
					type="email"
					v-model="model.proxy.emailDienstlich"
					:validation="() => model.getFehler('emailDienstlich')"
					:max-len="100"
					:disabled />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-checkpoint-modal :checkpoint :continue-routing="props.continueRoutingAfterCheckpoint" />
	</div>
</template>

<script setup lang="ts">

	import type { LehrerNeuProps } from './LehrerNeuProps';
	import { computed, ref, watch } from "vue";
	import { CoreTypeSelectManager, SelectManager, useBenutzerState, useOrteState, useSchuleState } from "@ui";
	import type { NationalitaetenKatalogEintrag, OrtsteilKatalogEintrag } from "@core";
	import { BenutzerKompetenz, Geschlecht, LehrerStammdaten, Nationalitaeten, PersonalTyp } from "@core";
	import { LehrerIndividualdatenModelProxy } from "~/components/lehrer/individualdaten/modelproxy/LehrerIndividualdatenModelProxy";

	const props = defineProps<LehrerNeuProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const orteState = useOrteState();

	const isLoading = ref<boolean>(false);

	const hatKompetenzAdd = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));
	const disabled = computed<boolean>(() => !hatKompetenzAdd.value);

	const model = new LehrerIndividualdatenModelProxy(
		() => Object.assign(new LehrerStammdaten(),
			{
				personalTyp: PersonalTyp.LEHRKRAFT.kuerzel,
				istSichtbar: true,
				istRelevantFuerStatistik: true,
			} as Partial<LehrerStammdaten>
		),
		() => schuleState.validatorKontext,
		props.lehrerListeManager
	);

	const personaltypManger = new SelectManager({
		options: PersonalTyp.values(),
		optionDisplayText: typ => typ.bezeichnung,
		selectionDisplayText: typ => typ.bezeichnung,
	});

	const geschlechtManager = new SelectManager({
		options: Geschlecht.values(),
		optionDisplayText: geschlecht => geschlecht.text,
		selectionDisplayText: geschlecht => geschlecht.text,
	});

	const staatsangehoerigkeitManager = new CoreTypeSelectManager({
		clazz: Nationalitaeten.class,
		optionDisplayText: nationalitaet => nationalitaet.staatsangehoerigkeit,
		selectionDisplayText: nationalitaet => nationalitaet.staatsangehoerigkeit,
		sort: staatsangehoerigkeitSort,
	});

	function staatsangehoerigkeitSort(a: NationalitaetenKatalogEintrag, b: NationalitaetenKatalogEintrag): number {
		const va = a.staatsangehoerigkeit;
		const vb = b.staatsangehoerigkeit;
		if ((va.length > 0) && (vb.length > 0)) {
			return va.localeCompare(vb);
		} else if ((va.length > 0) && (vb.length === 0)) {
			return -1;
		} else if ((va.length === 0) && (vb.length > 0)) {
			return 1;
		}
		return 0;
	}

	const wohnortManager = new SelectManager({
		options: computed(() => orteState.orte.list),
		optionDisplayText: ort => `${ort.plz ?? '—'} ${ort.ortsname ?? '—'}`,
		selectionDisplayText: ort => `${ort.plz ?? '—'} ${ort.ortsname ?? '—'}`,
	});

	const ortsteilManager = new SelectManager({
		options: computed(() => orteState.ortsteile.listByOrtId(model.proxy.wohnortID)),
		optionDisplayText: ortsteil => ortsteil.ortsteil ?? '—',
		selectionDisplayText: ortsteil => ortsteil.ortsteil ?? '—',
		sort: ortsteilSort,
	});

	function ortsteilSort(a: OrtsteilKatalogEintrag, b: OrtsteilKatalogEintrag): number {
		if ((a.ortsteil !== null) && (b.ortsteil !== null)) {
			return a.ortsteil.localeCompare(b.ortsteil);
		} else if ((a.ortsteil !== null) && (b.ortsteil === null)) {
			return -1;
		} else if ((a.ortsteil === null) && (b.ortsteil !== null)) {
			return 1;
		}
		return 0;
	}

	async function addLehrer() {
		if (isLoading.value) {
			return;
		}

		isLoading.value = true;
		props.checkpoint.active = false;
		const { id, ...partialData } = model.proxy;
		await props.add(partialData);
		isLoading.value = false;
	}

	async function cancel() {
		props.checkpoint.active = false;
		await props.gotoDefaultView(null);
	}

	watch(() => model.pending, () => {
		if (isLoading.value) {
			return;
		}

		if (Object.keys(model.pending).length > 0) {
			props.checkpoint.active = true;
		}

	}, { immediate: false, deep: true });

</script>
