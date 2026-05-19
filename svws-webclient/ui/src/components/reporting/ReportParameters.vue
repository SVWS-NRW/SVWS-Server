<template>
	<div class="w-full flex flex-row gap-8 items-start">
		<div class="flex flex-col gap-4 shrink-0" :class="(serverState.hasDev && (createHtmlPreview !== undefined)) ? 'w-2/5' : 'w-full'">
			<div v-if="reportvorlage === undefined">
				<ui-select :manager="reportvorlageSelectManager" v-model="localReportvorlage" />
			</div>
			<div v-if="idAbschnitt === undefined">
				<svws-ui-input-number v-model="localIdAbschnitt" placeholder="ID Schuljahresabschnitt" />
			</div>
			<div v-if="(idHauptdatenObjekt !== undefined) && (idHauptdatenObjekt < 0)">
				<svws-ui-input-number v-model="localIdHauptdatenObjekt" placeholder="ID des Hauptdaten-Objekts" />
			</div>
			<div v-if="idsHauptdaten === undefined">
				<svws-ui-text-input v-model="localIdsHauptdaten" placeholder="IDs Hauptdaten" />
			</div>
			<div v-if="idsDetaildaten === undefined">
				<svws-ui-text-input v-model="localIdsDetaildaten" placeholder="IDs Detaildaten" />
			</div>
			<div>
				<template v-for="gruppe in parameter.reportvorlageParameterGruppen" :key="gruppe.name">
					<div v-if="gruppe.uiIstSichtbar === true && gruppe.reportvorlageParameter.size() > 0" class="border-2 border-ui-25 rounded-md p-2 my-2">
						<div class="flex flex-col mb-2">
							<div class="flex justify-between items-center">
								<div class="font-bold">{{ gruppe.name }}</div>
								<div class="flex flex-row">
									<svws-ui-button @click="() => checkboxes.get(gruppe.name)?.forEach(vp => parameterWert(vp).value = true)" size="small" type="transparent">
										<span class="icon i-ri-checkbox-line" />
										Alle auswählen
									</svws-ui-button>
									<svws-ui-button @click="() => checkboxes.get(gruppe.name)?.forEach(vp => parameterWert(vp).value = false)" size="small" type="transparent">
										<span class="icon i-ri-checkbox-blank-line" />
										Alle abwählen
									</svws-ui-button>
								</div>
							</div>
							<div v-if="gruppe.beschreibung" class="text-sm mt-1">{{ gruppe.beschreibung }}</div>
						</div>
						<div :class="['grid gap-2', gruppe.uiAnzahlSpalten === 0 ? 'grid-cols-1' : gruppe.uiAnzahlSpalten > 6 ? 'grid-cols-6' : `grid-cols-${gruppe.uiAnzahlSpalten}`]">
							<template v-for="vp in gruppe.reportvorlageParameter" :key="vp.name">
								<div v-if="vp.uiIstSichtbar === true" :class="vp.uiAnzahlSpalten === 0 ? 'col-span-1' : vp.uiAnzahlSpalten > 6 ? 'col-span-6' : `col-span-${vp.uiAnzahlSpalten}`">
									<component :is="inputComponent(vp)" v-model="parameterWert(vp).value" :name="vp.name" />
									<label :for="vp.name"> {{ vp.bezeichnung }} </label>
								</div>
							</template>
						</div>
					</div>
				</template>
				<div class="text-left" />

				<div v-if="parameter.sortierungDefinitionenGruppen.size() > 0" class="border-2 border-ui-25 rounded-md p-2 my-2 col-span-full">
					<div class="font-bold mb-2">Sortierung</div>
					<div class="flex items-center gap-x-4 gap-y-2">
						<template v-for="gruppe of parameter.sortierungDefinitionenGruppen" :key="gruppe.bezeichnung">
							<template v-if="gruppe.uiIstSichtbar && !gruppe.sortierungDefinitionenOptionen.isEmpty()">
								<span>{{ gruppe.bezeichnung }}</span>
								<ui-select :manager="mapSelectManagerSortierung.get(gruppe.bezeichnung)" :model-value="gruppe.sortierungDefinitionen.isEmpty() ? undefined : gruppe.sortierungDefinitionen.get(0)"
									@update:model-value="v => v instanceof ReportingSortierungDefinition ? gruppe.sortierungDefinitionen = ListUtils.create1(v) : gruppe.sortierungDefinitionen.clear()" />
							</template>
						</template>
					</div>
				</div>

				<div v-if="parameter.filterDefinitionenGruppen.size() > 0" class="border-2 border-ui-25 rounded-md p-2 my-2 col-span-full">
					<div class="font-bold mb-2">Filterung</div>
					<div class="flex items-center gap-x-4 gap-y-2">
						<template v-for="gruppe of parameter.filterDefinitionenGruppen" :key="gruppe.bezeichnung">
							<template v-if="gruppe.uiIstSichtbar && !gruppe.filterDefinitionenOptionen.isEmpty()">
								<span>{{ gruppe.bezeichnung }}</span>
								<ui-select-multi v-if="gruppe.uiIstMultiselect" :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" :model-value="gruppe.filterDefinitionen"
									@update:model-value="v => filterUpdate(v, gruppe.filterDefinitionen)" />
								<ui-select v-else :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" :model-value="gruppe.filterDefinitionen.isEmpty() ? undefined : gruppe.filterDefinitionen.get(0)"
									@update:model-value="v => v instanceof ReportingFilterDefinition ? gruppe.filterDefinitionen = ListUtils.create1(v) : gruppe.filterDefinitionen.clear()" />
							</template>
						</template>
					</div>
				</div>

				<template v-if="ausgabeformat === 0 || ((parameter.ausgabeformatOptionen.size() > 1) && (ausgabeformat === undefined) && (parameter.ausgabeformat === 0))">
					<ui-select :manager="ausgabeSelectManager" v-model="parameter.ausgabeformat" />
				</template>

				<!-- PDF-Ausgabe -->
				<template v-if="parameter.ausgabeformatOptionen.contains(ReportingAusgabeformat.PDF.getId())">
					<div class="text-left col-span-4 flex gap-2">
						<svws-ui-button @click="downloadPDF" :is-loading class="mt-4">
							<svws-ui-spinner v-if="isLoading" spinning />
							<span v-else class="icon i-ri-printer-line" />
							Drucken
						</svws-ui-button>
						<svws-ui-button v-if="serverState.hasDev && (createHtmlPreview !== undefined)"
							@click="openHtmlPreview" :is-loading class="mt-4">
							<svws-ui-spinner v-if="isLoading" spinning />
							<span v-else class="icon i-ri-eye-line" />
							Vorschau
						</svws-ui-button>
					</div>
				</template>

				<!-- E-Mail-Eingabefelder -->
				<template v-if="(parameter.ausgabeformatOptionen.contains(ReportingAusgabeformat.EMAIL.getId())) && serverState.hasDev && (parameter.eMailDaten !== null)">
					<div class="border-2 border-ui-25 rounded-md p-2 my-2">
						<div class="flex flex-col mb-2">
							<div class="font-bold">E-Mail-Versand</div>
							<div class="text-sm mt-1">Pro Datensatz werden die Dateien gemäß der obiger Einstellungen erzeugt und dann als E-Mail an die zugeordnete Person versendet.</div>
						</div>
						<div class="flex flex-col gap-4">
							<div class="flex flex-col gap-1">
								<svws-ui-text-input v-model="parameter.eMailDaten.betreff" placeholder="Betreff eingeben" />
								<svws-ui-textarea-input autoresize v-model="parameter.eMailDaten.text" placeholder="E-Mail-Text eingeben" />
							</div>
							<svws-ui-checkbox v-model="parameter.eMailDaten.istPrivateEmailAlternative">
								Private E-Mail-Adresse verwenden, wenn keine schulische E-Mail-Adresse vorhanden ist.
							</svws-ui-checkbox>
						</div>
					</div>
					<svws-ui-button @click="sendPdfByEmail" :is-loading>
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-mail-send-line" />
						E-Mails senden
					</svws-ui-button>
				</template>
			<!-- Ende: E-Mail-Eingabefelder -->
			</div>
		</div>
		<!-- HTML-Vorschau (rechte Spalte, nur in DEV mit verfügbarer Vorschau-API) -->
		<div v-if="serverState.hasDev && (createHtmlPreview !== undefined)" class="w-3/5 sticky top-2 h-[calc(100vh-16rem)] flex flex-col">
			<div class="relative flex-1 min-h-0 overflow-hidden rounded-md border-2 border-ui-25 bg-white shadow-sm flex items-center justify-center">
				<iframe v-if="previewHtml !== ''" :srcdoc="previewHtml" class="absolute inset-0 w-full h-full border-0 bg-white" title="Reporting-Vorschau" />
				<div v-else class="text-black italic text-center p-8">
					Erstellen Sie eine Vorschau, um diese hier anzeigen zu lassen.
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import Checkbox from "../../ui/controls/SvwsUiCheckbox.vue";
	import Input from "../../ui/controls/SvwsUiTextInput.vue";
	import InputNumber from "../../ui/controls/SvwsUiInputNumber.vue";
	import Select from "../../ui/controls/select/UiSelect.vue";
	import TextArea from "../../ui/controls/SvwsUiTextareaInput.vue";

	import type { Component, ComputedRef } from "vue";
	import { computed, ref, watch, watchEffect } from "vue";
	import { ReportingReportvorlage } from "../../../../core/src/core/types/reporting/ReportingReportvorlage";
	import { ReportingParameter } from "../../../../core/src/core/data/reporting/ReportingParameter";
	import { ReportingUIKomponentenTyp } from "../../../../core/src/core/types/reporting/ReportingUIKomponentenTyp";
	import type { ReportingReportvorlageParameter } from "../../../../core/src/core/data/reporting/ReportingReportvorlageParameter";
	import type { ApiFile } from "../../../../core/src/api/BaseApi";
	import type { SimpleOperationResponse } from "../../../../core/src/core/data/SimpleOperationResponse";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ReportingAusgabeformat } from "../../../../core/src/core/types/reporting/ReportingAusgabeformat";
	import { ReportingReportvorlageParameterTyp } from "../../../../core/src/core/types/reporting/ReportingReportvorlageParameterTyp";
	import { ServerMode } from "../../../../core/src/core/types/ServerMode";
	import { SelectManager } from "../../../../ui/src/ui/controls/select/manager/SelectManager";
	import { ReportingSortierungDefinition } from "../../../../core/src/core/data/reporting/ReportingSortierungDefinition";
	import { ReportingFilterDefinition } from "../../../../core/src/core/data/reporting/ReportingFilterDefinition";
	import { ListUtils } from "../../../../core/src";
	import { useServerState } from "../../states/ServerState";

	const props = defineProps<{
		reportvorlage?: ReportingReportvorlage;
		idHauptdatenObjekt?: number;
		idsHauptdaten?: Iterable<number>;
		idsDetaildaten?: Iterable<number>;
		idAbschnitt?: number;
		createReport: (parameter: ReportingParameter) => Promise<ApiFile>;
		createHtmlPreview?: (parameter: ReportingParameter) => Promise<string>;
		sendEMail?: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
		ausgabeformat?: number;
	}>();
	const serverState = useServerState();

	const isLoading = ref<boolean>(false);
	const previewHtml = ref<string>('');
	const logs = ref<List<string | null>>();
	const status = ref<boolean>();
	const localReportvorlage = ref<ReportingReportvorlage>();
	const localIdHauptdatenObjekt = ref<number>(-1);
	const localIdsHauptdaten = ref<string>("");
	const localIdsDetaildaten = ref<string>("");
	const localIdAbschnitt = ref<number>(0);
	const parameter = ref(new ReportingParameter());

	watch(() => props.reportvorlage, () => {
			localReportvorlage.value = props.reportvorlage;
			if (localReportvorlage.value === undefined) {
				localReportvorlage.value = ReportingReportvorlage.values().at(0);
			}
		},
		{ immediate: true });
	watchEffect(() => parameter.value = localReportvorlage.value?.getReportingParameter() ?? new ReportingParameter());

	const mapSelectManagerSortierung = computed<Map<string, SelectManager<ReportingSortierungDefinition>>>(() => {
		const map = new Map<string, SelectManager<ReportingSortierungDefinition>>();
		for (const gruppe of parameter.value.sortierungDefinitionenGruppen) {
			if (gruppe.sortierungDefinitionen.isEmpty() && !gruppe.sortierungDefinitionenOptionen.isEmpty()) {
				gruppe.sortierungDefinitionen.add(gruppe.sortierungDefinitionenOptionen.get(0));
			}
			map.set(gruppe.bezeichnung, new SelectManager({
				options: gruppe.sortierungDefinitionenOptionen,
				selectionDisplayText: (option): string => option.bezeichnung,
				optionDisplayText: (option): string => option.bezeichnung,
			}));
		}
		return map;
	});

	const mapSelectManagerFilter = computed<Map<string, SelectManager<ReportingFilterDefinition>>>(() => {
		const map = new Map<string, SelectManager<ReportingFilterDefinition>>();
		for (const gruppe of parameter.value.filterDefinitionenGruppen) {
			map.set(gruppe.bezeichnung, new SelectManager({
				options: gruppe.filterDefinitionenOptionen,
				selectionDisplayText: (option): string => option.bezeichnung,
				optionDisplayText: (option): string => option.bezeichnung,
			}));
		}
		return map;
	});

	const ausgabeSelectManager = computed(() => {
		return new SelectManager({
			options: parameter.value.ausgabeformatOptionen,
			selectionDisplayText: (option) => ReportingAusgabeformat.getByID(option)?.name() ?? '—',
			optionDisplayText: (option) => ReportingAusgabeformat.getByID(option)?.name() ?? '—',
		});
	});


	function filterUpdate(list: Iterable<ReportingFilterDefinition> | null | undefined, selected: List<ReportingFilterDefinition>) {
		selected.clear();
		if ((list === undefined) || (list === null)) {
			return;
		}
		for (const item of list) {
			selected.add(item);
		}
	}

	const checkboxes: ComputedRef<Map<string, ReportingReportvorlageParameter[]>> = computed(() => {
		const map = new Map<string, ReportingReportvorlageParameter[]>();
		for (const gruppe of parameter.value.reportvorlageParameterGruppen) {
			const arr = map.get(gruppe.name) ?? [];
			for (const parameter of gruppe.reportvorlageParameter) {
				if (parameter.typ === ReportingReportvorlageParameterTyp.BOOLEAN.getId()) {
					arr.push(parameter);
				}
			}
			map.set(gruppe.name, arr);
		}
		return map;
	});

	const parameterWert = (vp: ReportingReportvorlageParameter) => computed({
		get: () => {
			const type = ReportingUIKomponentenTyp.getByID(vp.uiKomponentenTyp);
			switch (type.name()) {
				case "INPUT":
				case "SELECT":
				case "TEXTAREA":
					return vp.wert;
				case "NUMBERPICKER":
					return Number.parseInt(vp.wert);
				case "DATEPICKER":
					return Date.parse(vp.wert);
				case "CHECKBOX":
				default:
					return vp.wert === "true";
			}
		},
		set: (value) => vp.wert = value.toString(),
	});

	const listHauptdaten = computed(() => {
		const list = new ArrayList<number>();
		const iterable = props.idsHauptdaten === undefined ? localIdsHauptdaten.value.split(',') : props.idsHauptdaten;
		for (const id of iterable) {
			list.add(Number(id));
		}
		return list;
	});

	const listDetaildaten = computed(() => {
		const list = new ArrayList<number>();
		const iterable = props.idsDetaildaten === undefined ? localIdsDetaildaten.value.split(',') : props.idsDetaildaten;
		for (const id of iterable) {
			list.add(Number(id));
		}
		return list;
	});

	function inputComponent(vp: ReportingReportvorlageParameter): Component {
		const type = ReportingUIKomponentenTyp.getByID(vp.uiKomponentenTyp);
		switch (type.name()) {
			case "INPUT":
				return Input;
			case "NUMBERPICKER":
				return InputNumber;
			case "SELECT":
				return Select;
			case "DATEPICKER":
				return Input;
			case "TEXTAREA":
				return TextArea;
			case "CHECKBOX":
			default:
				return Checkbox;
		}
	}

	const reportvorlageSelectManager = new SelectManager({
		options: ReportingReportvorlage.values(),
		selectionDisplayText: option => option.getBezeichnung(),
		optionDisplayText: option => option.getBezeichnung(),
	});

	async function downloadPDF() {
		isLoading.value = true;
		parameter.value.idSchuljahresabschnitt = props.idAbschnitt ?? localIdAbschnitt.value;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		parameter.value.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		try {
			const { data, name } = await props.createReport(parameter.value);
			const link = document.createElement("a");
			link.href = URL.createObjectURL(data);
			link.download = name;
			link.target = "_blank";
			link.click();
			URL.revokeObjectURL(link.href);
		} finally {
			isLoading.value = false;
		}
	}

	async function openHtmlPreview() {
		if (props.createHtmlPreview === undefined) {
			return;
		}
		isLoading.value = true;
		parameter.value.idSchuljahresabschnitt = props.idAbschnitt ?? localIdAbschnitt.value;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		parameter.value.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		try {
			previewHtml.value = await props.createHtmlPreview(parameter.value);
		} finally {
			isLoading.value = false;
		}
	}

	async function sendPdfByEmail() {
		if (props.sendEMail === undefined) {
			return;
		}
		isLoading.value = true;
		parameter.value.idSchuljahresabschnitt = props.idAbschnitt ?? localIdAbschnitt.value;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		parameter.value.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		try {
			const result = await props.sendEMail(parameter.value);
			status.value = result.success;
			logs.value = result.log;
		} finally {
			isLoading.value = false;
		}
	}

</script>
