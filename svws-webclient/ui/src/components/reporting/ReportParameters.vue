<template>
	<div class="w-full flex flex-col gap-4">
		<div v-if="reportvorlage === undefined">
			<ui-select :manager="reportvorlageSelectManager" v-model="localReportvorlage" />
		</div>
		<div v-if="idAbschnitt === undefined">
			<svws-ui-input-number v-model="localIdAbschnit" placeholder="ID Schuljahresabschnitt" />
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
						<div v-if="gruppe.beschreibung" class="text-sm opacity-75 mt-1">{{ gruppe.beschreibung }}</div>
					</div>
					<div :class="['grid gap-2', gruppe.uiAnzahlSpalten === 0 ? 'grid-cols-1' : gruppe.uiAnzahlSpalten > 6 ? 'grid-cols-6' : `grid-cols-${gruppe.uiAnzahlSpalten}`]">
						<template v-for="vp in gruppe.reportvorlageParameter" :key="vp.name">
							<div v-if="vp.uiIstSichtbar === true" :class="vp.uiAnzahlSpalten === 0 ? 'col-span-1' : vp.uiAnzahlSpalten > 6 ? 'col-span-6' : `col-span-${vp.uiAnzahlSpalten}`">
								<component :is="inputComponent(vp, gruppe)" v-model="parameterWert(vp).value" :name="vp.name" />
								<label :for="vp.name"> {{ vp.bezeichnung }} </label>
							</div>
						</template>
					</div>
					<br>
				</div>
			</template>
			<div class="text-left" />
			<template v-for="gruppe of parameter.sortierungDefinitionenGruppen" :key="gruppe.bezeichnung">
				<div v-if="gruppe.uiIstSichtbar && !gruppe.sortierungDefinitionenOptionen.isEmpty()" class="col-span-full">
					<span class="font-bold">{{ gruppe.bezeichnung }}</span>
					<ui-select :manager="mapSelectManagerSortierung.get(gruppe.bezeichnung)" :model-value="gruppe.sortierungDefinitionen.isEmpty() ? undefined : gruppe.sortierungDefinitionen.get(0)"
						@update:model-value="v => v instanceof ReportingSortierungDefinition && gruppe.sortierungDefinitionen.addFirst(v)" />
				</div>
			</template>
			<template v-for="gruppe of parameter.filterDefinitionenGruppen" :key="gruppe.bezeichnung">
				<div v-if="gruppe.uiIstSichtbar && !gruppe.filterDefinitionenOptionen.isEmpty()" class="col-span-full">
					<span class="font-bold">{{ gruppe.bezeichnung }}</span>
					<ui-select-multi v-if="gruppe.uiIstMultiselect" :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" v-model="gruppe.filterDefinitionen" />
					<ui-select v-else :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" :model-value="gruppe.filterDefinitionen.isEmpty() ? undefined : gruppe.filterDefinitionen.get(0)"
						@update:model-value="v => v instanceof ReportingFilterDefinition && gruppe.filterDefinitionen.addFirst(v)" />
				</div>
			</template>

			<template v-if="ausgabeformat === 0 || ((parameter.ausgabeformatOptionen.size() > 1) && (ausgabeformat === undefined) && (parameter.ausgabeformat === 0))">
				<ui-select :manager="ausgabeSelectManager" v-model="parameter.ausgabeformat" />
			</template>

			<!-- Ausgabeformat -->
			<template v-if="parameter.ausgabeformatOptionen.contains(ReportingAusgabeformat.PDF.getId())">
				<div class="text-left col-span-4">
					<svws-ui-button @click="downloadPDF" :is-loading class="mt-4">
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-printer-line" />
						Drucken
					</svws-ui-button>
				</div>
			</template>
			<!-- E-Mail-Eingabefelder -->
			<template v-if="(parameter.ausgabeformatOptionen.contains(ReportingAusgabeformat.EMAIL.getId())) && ServerMode.DEV.equals(servermode ?? null)">
				<div class="border-2 border-ui-25 rounded-md p-2 my-2">
					<div class="flex flex-col mb-2">
						<div class="font-bold">E-Mail-Versand</div>
						<div class="text-sm opacity-75 mt-1">Die Dateien werden gemäß der obiger Einstellungen erzeugt und dann als E-Mail versendet.</div>
					</div>
					<div class="flex flex-col gap-4">
						<div class="flex flex-col gap-1">
							<svws-ui-text-input v-model="eMailParameter.betreff" placeholder="Betreff eingeben" />
							<svws-ui-textarea-input autoresize v-model="eMailParameter.text" placeholder="E-Mail-Text eingeben" />
						</div>
						<svws-ui-checkbox v-model="eMailParameter.istPrivateEmailAlternative">
							Private E-Mail-Adresse verwenden, wenn keine schulische E-Mail-Adresse vorhanden ist.
						</svws-ui-checkbox>
					</div>
				</div>
				<svws-ui-button @click="sendPdfByEmail" :is-loading>
					<svws-ui-spinner v-if="isLoading" spinning />
					<span v-else class="icon i-ri-mail-send-line" />
					E-Mails senden
				</svws-ui-button>
			<!-- Ende: E-Mail-Eingabefelder -->
			</template>
		</div>
	</div>
</template>

<script setup lang="ts">

	import Checkbox from "../../ui/controls/SvwsUiCheckbox.vue";
	import Input from "../../ui/controls/SvwsUiTextInput.vue";
	import InputNumber from "../../ui/controls/SvwsUiInputNumber.vue";
	import Select from "../../ui/controls/select/UiSelect.vue";
	import TextArea from "../../ui/controls/SvwsUiTextareaInput.vue";

	import type { Component, Ref, ComputedRef } from "vue";
	import { computed, ref, watch, watchEffect } from "vue";
	import { ReportingReportvorlage } from "../../../../core/src/core/types/reporting/ReportingReportvorlage";
	import { ReportingParameter } from "../../../../core/src/core/data/reporting/ReportingParameter";
	import { ReportingUIKomponentenTyp } from "../../../../core/src/core/types/reporting/ReportingUIKomponentenTyp";
	import type { ReportingReportvorlageParameter } from "../../../../core/src/core/data/reporting/ReportingReportvorlageParameter";
	import type { ApiFile } from "../../../../core/src/api/BaseApi";
	import type { SimpleOperationResponse } from "../../../../core/src/core/data/SimpleOperationResponse";
	import type { List } from "../../../../core/src/java/util/List";
	import { ReportingEMailDaten } from "../../../../core/src/core/data/reporting/ReportingEMailDaten";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ReportingAusgabeformat } from "../../../../core/src/core/types/reporting/ReportingAusgabeformat";
	import { ReportingReportvorlageParameterTyp } from "../../../../core/src/core/types/reporting/ReportingReportvorlageParameterTyp";
	import type { ReportingReportvorlageParameterGruppe } from "../../../../core/src/core/data/reporting/ReportingReportvorlageParameterGruppe";
	import { ServerMode } from "../../../../core/src/core/types/ServerMode";
	import { SelectManager } from "../../../../ui/src/ui/controls/select/manager/SelectManager";
	import { ReportingSortierungDefinition } from "../../../../core/src/core/data/reporting/ReportingSortierungDefinition";
	import { ReportingFilterDefinition } from "../../../../core/src/core/data/reporting/ReportingFilterDefinition";

	const props = defineProps<{
		reportvorlage?: ReportingReportvorlage;
		idHauptdatenObjekt?: number;
		idsHauptdaten?: Iterable<number>;
		idsDetaildaten?: Iterable<number>;
		idAbschnitt?: number;
		createReport: (parameter: ReportingParameter) => Promise<ApiFile>;
		sendEMail?: (parameter: ReportingParameter) => Promise<SimpleOperationResponse>;
		servermode?: ServerMode;
		ausgabeformat?: number;
	}>();

	const isLoading = ref<boolean>(false);
	const logs = ref<List<string | null>>();
	const status = ref<boolean>();
	const eMailParameter: Ref<ReportingEMailDaten> = ref(new ReportingEMailDaten());
	const parameter: Ref<ReportingParameter> = ref(new ReportingParameter());
	const localReportvorlage = ref<ReportingReportvorlage>();
	const localIdHauptdatenObjekt = ref<number>(-1);
	const localIdsHauptdaten = ref<string>("");
	const localIdsDetaildaten = ref<string>("");
	const localIdAbschnit = ref<number>(0);

	watch(() => props.reportvorlage, () => {
		localReportvorlage.value = props.reportvorlage;
	}, { immediate: true });

	watchEffect(() => parameter.value = localReportvorlage.value?.getReportingParameter() ?? new ReportingParameter());

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

	function inputComponent(vp: ReportingReportvorlageParameter, gruppe: ReportingReportvorlageParameterGruppe): Component {
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

	const mapSelectManagerSortierung = new Map<string, SelectManager<ReportingSortierungDefinition>>();
	const mapSelectManagerFilter = new Map<string, SelectManager<ReportingFilterDefinition>>();

	for (const option of parameter.value.sortierungDefinitionenGruppen) {
		const objectSelectManager = new SelectManager({
			options: option.sortierungDefinitionenOptionen,
			selectionDisplayText: (option): string => option.bezeichnung,
			optionDisplayText: (option): string => option.bezeichnung,
		});
		mapSelectManagerSortierung.set(option.bezeichnung, objectSelectManager);
	}

	for (const option of parameter.value.filterDefinitionenGruppen) {
		const objectSelectManager = new SelectManager({
			options: option.filterDefinitionenOptionen,
			selectionDisplayText: (option): string => option.bezeichnung,
			optionDisplayText: (option): string => option.bezeichnung,
		});
		mapSelectManagerFilter.set(option.bezeichnung, objectSelectManager);
	}

	const ausgabeSelectManager = new SelectManager({
		options: parameter.value.ausgabeformatOptionen,
		selectionDisplayText: (option) => ReportingAusgabeformat.getByID(option)?.name() ?? '—',
		optionDisplayText: (option) => ReportingAusgabeformat.getByID(option)?.name() ?? '—',
	});

	const reportvorlageSelectManager = new SelectManager({
		options: ReportingReportvorlage.values(),
		selectionDisplayText: option => option.getBezeichnung(),
		optionDisplayText: option => option.getBezeichnung(),
	});

	async function downloadPDF() {
		isLoading.value = true;
		parameter.value.idSchuljahresabschnitt = props.idAbschnitt ?? localIdAbschnit.value;
		parameter.value.idHauptdatenObjekt = props.idHauptdatenObjekt ?? localIdHauptdatenObjekt.value;
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

	async function sendPdfByEmail() {
		if (props.sendEMail === undefined) {
			return;
		}
		isLoading.value = true;
		parameter.value.idSchuljahresabschnitt = props.idAbschnitt ?? localIdAbschnit.value;
		parameter.value.idHauptdatenObjekt = props.idHauptdatenObjekt ?? localIdHauptdatenObjekt.value;
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
