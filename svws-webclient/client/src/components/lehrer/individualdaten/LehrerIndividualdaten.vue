<template>
	<Teleport v-if="zeigeAlles" to=".svws-ui-header--actions" defer>
		<wiedervorlage-modal type="lehrkraft" mode="create"
			:data="{
				idPerson: lehrerListeManager().daten().id,
				namePerson: `${lehrerListeManager().daten().vorname} ${lehrerListeManager().daten().nachname}`
			}">
			<template #default="{openModal}">
				<svws-ui-button @click="openModal" type="secondary">
					<span class="icon i-ri-alarm-line" aria-hidden="true" /> Wiedervorlage anlegen
				</svws-ui-button>
			</template>
		</wiedervorlage-modal>
		<svws-ui-modal-hilfe> <hilfe-lehrer-individualdaten /> </svws-ui-modal-hilfe>
	</Teleport>

	<div class="page page-grid-cards">
		<svws-ui-content-card title="Allgemein">
			<template v-if="zeigeAlles" #actions>
				<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istSichtbar" focus-class-content>
					Ist sichtbar
				</svws-ui-checkbox>
				<svws-ui-checkbox :readonly v-model="modelProxy.proxy.istRelevantFuerStatistik" statistics>
					Ist relevant für Statistik
				</svws-ui-checkbox>
			</template>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Kürzel"
					v-model="modelProxy.proxy.kuerzel"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('kuerzel')"
					:max-len="10"
					:readonly statistics required focus />
				<ui-select label="Personal-Typ"
					v-model="modelProxy.personalTyp.value"
					:manager="personaltypManger"
					:validation="() => modelProxy.getFehler('personalTyp')"
					:readonly required :removable="false" />
				<svws-ui-text-input placeholder="Nachname"
					v-model="modelProxy.proxy.nachname"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('nachname')"
					:max-len="120"
					:readonly required statistics />
				<svws-ui-text-input placeholder="Rufname"
					v-model="modelProxy.proxy.vorname"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('vorname')"
					:max-len="80"
					:readonly required statistics />
				<svws-ui-spacing />
				<ui-select label="Geschlecht"
					v-model="modelProxy.geschlecht.value"
					:manager="geschlechtManager"
					:validation="() => modelProxy.getFehler('geschlecht')"
					:readonly required :removable="false" />
				<svws-ui-text-input placeholder="Geburtsdatum"
					v-model="modelProxy.proxy.geburtsdatum"
					type="date"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('geburtsdatum')"
					:readonly required statistics />
				<ui-select label="Staatsangehörigkeit"
					v-model="modelProxy.staatsangehoerigkeit.value"
					:manager="staatsangehoerigkeitManager"
					:validation="() => modelProxy.getFehler('idStaatsangehoerigkeit')"
					:readonly required statistics :removable="false" />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Akademischer Grad"
					v-model="modelProxy.proxy.titel"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('titel')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Amtsbezeichnung"
					v-model="modelProxy.proxy.amtsbezeichnung"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('amtsbezeichnung')"
					:max-len="15"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Wohnort und Kontaktdaten">
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input placeholder="Straße"
					v-model="modelProxy.adresse.value"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('strassenname')"
					:max-len="55"
					span="full"
					class="contentFocusField"
					:readonly />
				<ui-select label="Wohnort"
					v-model="modelProxy.wohnort.value"
					:manager="wohnortManager"
					:validation="() => modelProxy.getFehler('wohnortID')"
					:readonly />
				<ui-select label="Ortsteil"
					v-model="modelProxy.ortsteil.value"
					:manager="ortsteilManager"
					:validation="() => modelProxy.getFehler('ortsteilID')"
					:readonly />
				<svws-ui-spacing />
				<svws-ui-text-input placeholder="Telefon"
					type="tel"
					v-model="modelProxy.proxy.telefon"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('telefon')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Mobil oder Fax"
					type="tel"
					v-model="modelProxy.proxy.telefonMobil"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('telefonMobil')"
					:max-len="20"
					:readonly />
				<svws-ui-text-input placeholder="Private E-Mail-Adresse"
					type="email"
					v-model="modelProxy.proxy.emailPrivat"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('emailPrivat')"
					:max-len="100"
					:readonly />
				<svws-ui-text-input placeholder="Schulische E-Mail-Adresse"
					type="email"
					v-model="modelProxy.proxy.emailDienstlich"
					@change="modelProxy.patch"
					:validation="() => modelProxy.getFehler('emailDienstlich')"
					:max-len="100"
					:readonly />
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Leitungsfunktionen" v-if="serverState.mode === ServerMode.DEV">
			<svws-ui-table class="max-h-72! w-full"
				v-model="selectedLeitungsfunktionen"
				:items="getListLeitungsfunktionen()"
				:clicked="clickedLeitungsfunktion"
				@update:clicked="lf => patchLeitungsfunktionModal(lf)"
				:columns="leitungsfunktionenTableColumns"
				:clickable="!readonly"
				:selectable="!readonly"
				scroll scroll-into-view count>
				<template #cell(idLeitungsfunktion)="{ value }">
					{{ getBezeichnungLeitungsfunktion(value) }}
				</template>
				<template #cell(datumBeginnLeitungsfunktion)="{ value }">
					{{ formatDatum(value) }}
				</template>
				<template #cell(datumEndeLeitungsfunktion)="{ value }">
					{{ formatDatum(value) }}
				</template>
				<template #actions v-if="!readonly">
					<div class="inline-flex gap-4">
						<svws-ui-button type="trash"
							@click="deleteSelectedLeitungsfunktionen"
							:disabled="selectedLeitungsfunktionen.length === 0" />
						<svws-ui-button title="Leitungsfunktion hinzufügen"
							type="icon"
							@click="addLeitungsfunktionModal">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-table>
			<svws-ui-modal :show="showModalLeitungsfunktion" @update:show="closeModalLeitungsfunktion">
				<template #modalTitle>
					{{ currentLeitungsfunktionMode === LeitungsfunktionMode.PATCH ? 'Leitungsfunktion bearbeiten' : 'Leitungsfunktion hinzufügen' }}
				</template>
				<template #modalContent>
					<svws-ui-input-wrapper :grid="2" class="text-left">
						<ui-select label="Funktion"
							class="col-span-full"
							v-model="selectedFunktion"
							:manager="leitungsfunktionManager"
							required :removable="false" />
						<svws-ui-text-input placeholder="Bezeichnung" type="text" class="col-span-full"
							v-model="leitungsfunktionEntry.bezeichnung"
							:max-len="255" required />
						<svws-ui-text-input placeholder="Von" type="date"
							v-model="leitungsfunktionEntry.datumBeginnLeitungsfunktion" />
						<svws-ui-text-input placeholder="Bis" type="date"
							v-model="leitungsfunktionEntry.datumEndeLeitungsfunktion" />
					</svws-ui-input-wrapper>
					<div class="mt-7 flex flex-row gap-4 justify-end">
						<svws-ui-button type="secondary" @click="closeModalLeitungsfunktion">Abbrechen</svws-ui-button>
						<svws-ui-button @click="saveLeitungsfunktion" :disabled="saveLeitungsfunktionDisabled">
							Speichern
						</svws-ui-button>
					</div>
				</template>
			</svws-ui-modal>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { LehrerIndividualdatenProps } from "./LehrerIndividualdatenProps";
	import { LehrerIndividualdatenModelProxy } from "./modelproxy/LehrerIndividualdatenModelProxy";
	import WiedervorlageModal from "~/components/wiedervorlage/WiedervorlageModal.vue";
	import type { NationalitaetenKatalogEintrag } from "@core/asd/data/schule/NationalitaetenKatalogEintrag";
	import { Schulleitung } from "@core/asd/data/schule/Schulleitung";
	import { Geschlecht } from "@core/asd/types/Geschlecht";
	import { Nationalitaeten } from "@core/asd/types/schule/Nationalitaeten";
	import type { OrtsteilKatalogEintrag } from "@core/core/data/kataloge/OrtsteilKatalogEintrag";
	import type { Leitungsfunktion } from "@core/core/data/schule/Leitungsfunktion";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { PersonalTyp } from "@core/core/types/PersonalTyp";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import { JavaString } from "@core/java/lang/JavaString";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useOrteState } from "@ui/states/kataloge/OrteState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import { useServerState } from "@ui/states/ServerState";
	import type { DataTableColumn } from "@ui/types";
	import { CoreTypeSelectManager } from "@ui/ui/controls/select/manager/CoreTypeSelectManager";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { ServerMode } from "@core/core/types/ServerMode";

	const props = defineProps<LehrerIndividualdatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const serverState = useServerState();
	const orteState = useOrteState();

	const manager = () => props.lehrerListeManager();
	const dataNotPatched = () => props.lehrerListeManager().daten();
	const modelProxy = new LehrerIndividualdatenModelProxy(dataNotPatched, () => schuleState.validatorKontext, manager, props.patch);

	const readonly = computed<boolean>(() => !benutzerState.benutzerHatKompetenz(BenutzerKompetenz.LEHRERDATEN_AENDERN));
	const selectedLeitungsfunktionen = ref<Schulleitung[]>([]);
	const clickedLeitungsfunktion = ref<Schulleitung | null>(null);
	const leitungsfunktionEntry = ref<Schulleitung>(new Schulleitung());

	enum LeitungsfunktionMode { ADD, PATCH, DEFAULT }

	const currentLeitungsfunktionMode = ref<LeitungsfunktionMode>(LeitungsfunktionMode.DEFAULT);
	const showModalLeitungsfunktion = ref<boolean>(false);

	function getBezeichnungLeitungsfunktion(idLeitungsfunktion: number): string {
		return props.mapLeitungsfunktionen.get(idLeitungsfunktion)?.bezeichnung ?? '-';
	}

	function formatDatum(value: string | null): string {
		return (value === null) || JavaString.isBlank(value) ? '-' : DateUtils.gibDatumGermanFormat(value);
	}

	const leitungsfunktionenTableColumns: DataTableColumn[] = [
		{ key: "idLeitungsfunktion", label: "Funktion", span: 2 },
		{ key: "bezeichnung", label: "Bezeichnung", span: 3 },
		{ key: "datumBeginnLeitungsfunktion", label: "Von", span: 1 },
		{ key: "datumEndeLeitungsfunktion", label: "Bis", span: 1 },
	];

	const leitungsfunktionManager = new SelectManager({
		options: computed(() => [...props.mapLeitungsfunktionen.values()]),
		optionDisplayText: i => i.bezeichnung,
		selectionDisplayText: i => i.bezeichnung,
	});

	const selectedFunktion = computed<Leitungsfunktion | null>({
		get: () => props.mapLeitungsfunktionen.get(leitungsfunktionEntry.value.idLeitungsfunktion) ?? null,
		set: (selected) => leitungsfunktionEntry.value.idLeitungsfunktion = selected?.id ?? -1,
	});

	const datumLeitungsfunktionValid = computed<boolean>(() => {
		const von = leitungsfunktionEntry.value.datumBeginnLeitungsfunktion;
		const bis = leitungsfunktionEntry.value.datumEndeLeitungsfunktion;
		if ((von === null) || JavaString.isBlank(von) || (bis === null) || JavaString.isBlank(bis)) {
			return true;
		}
		return bis >= von;
	});

	const saveLeitungsfunktionDisabled = computed<boolean>(() =>
		(selectedFunktion.value === null) ||
		JavaString.isBlank(leitungsfunktionEntry.value.bezeichnung) ||
		!datumLeitungsfunktionValid.value
	);

	function resetLeitungsfunktion() {
		leitungsfunktionEntry.value = new Schulleitung();
		leitungsfunktionEntry.value.idLeitungsfunktion = -1;
	}

	function openModalLeitungsfunktion() {
		showModalLeitungsfunktion.value = true;
	}

	function closeModalLeitungsfunktion() {
		resetLeitungsfunktion();
		currentLeitungsfunktionMode.value = LeitungsfunktionMode.DEFAULT;
		showModalLeitungsfunktion.value = false;
	}

	function addLeitungsfunktionModal() {
		resetLeitungsfunktion();
		currentLeitungsfunktionMode.value = LeitungsfunktionMode.ADD;
		openModalLeitungsfunktion();
	}

	function patchLeitungsfunktionModal(lf: Schulleitung) {
		resetLeitungsfunktion();
		currentLeitungsfunktionMode.value = LeitungsfunktionMode.PATCH;
		leitungsfunktionEntry.value.id = lf.id;
		leitungsfunktionEntry.value.idLehrer = lf.idLehrer;
		leitungsfunktionEntry.value.idLeitungsfunktion = lf.idLeitungsfunktion;
		leitungsfunktionEntry.value.bezeichnung = lf.bezeichnung;
		leitungsfunktionEntry.value.datumBeginnLeitungsfunktion = lf.datumBeginnLeitungsfunktion;
		leitungsfunktionEntry.value.datumEndeLeitungsfunktion = lf.datumEndeLeitungsfunktion;
		clickedLeitungsfunktion.value = lf;
		openModalLeitungsfunktion();
	}

	async function saveLeitungsfunktion() {
		const { id, ...partialDataWithoutId } = leitungsfunktionEntry.value;

		if (currentLeitungsfunktionMode.value === LeitungsfunktionMode.ADD) {
			if (!props.getListLeitungsfunktionen().isEmpty()) {
				clickedLeitungsfunktion.value = props.getListLeitungsfunktionen().getFirst();
			}

			await props.addLeitungsfunktion(partialDataWithoutId, dataNotPatched().id);

			if (!props.getListLeitungsfunktionen().isEmpty()) {
				clickedLeitungsfunktion.value = props.getListLeitungsfunktionen().getLast();
			}

			closeModalLeitungsfunktion();
			return;
		}

		if (currentLeitungsfunktionMode.value === LeitungsfunktionMode.PATCH) {
			if (leitungsfunktionEntry.value.id <= 0) {
				return;
			}

			await props.patchLeitungsfunktion(partialDataWithoutId, leitungsfunktionEntry.value.id);
			closeModalLeitungsfunktion();
		}
	}

	async function deleteSelectedLeitungsfunktionen() {
		if (selectedLeitungsfunktionen.value.length === 0) {
			return;
		}

		const ids = new ArrayList<number>();
		for (const s of selectedLeitungsfunktionen.value) {
			ids.add(s.id);
		}

		await props.deleteLeitungsfunktionen(ids);
		selectedLeitungsfunktionen.value = [];
	}

	/**
	 * Selects
	 */
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
		options: computed(() => orteState.ortsteile.listByOrtId(modelProxy.proxy.wohnortID)),
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

</script>
