<template>
	<div :class="[createHtmlPreview ? 'w-2/5' : 'w-full']">
		<svws-ui-tab-bar :tab-manager="() => tabManager">
			<div v-if="selectedRoute.name === 'eingabe'" class="overflow-auto pr-6">
				<div v-if="reportvorlage === undefined">
					<ui-select :manager="reportvorlageSelectManager" v-model="localReportvorlage" />
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
						<div v-if="istSichtbar(gruppe) && gruppe.reportvorlageParameter.size() > 0" class="border-2 border-ui-25 rounded-md p-2 my-2">
							<div class="flex flex-col mb-2">
								<div class="flex justify-between items-center">
									<div class="font-bold">{{ gruppe.name }}</div>
									<div class="flex flex-row">
										<svws-ui-button @click="() => setzeAlleCheckboxen(gruppe, true)" size="small" type="transparent">
											<span class="icon i-ri-checkbox-line" />
											Alle auswählen
										</svws-ui-button>
										<svws-ui-button @click="() => setzeAlleCheckboxen(gruppe, false)" size="small" type="transparent">
											<span class="icon i-ri-checkbox-blank-line" />
											Alle abwählen
										</svws-ui-button>
									</div>
								</div>
								<div v-if="gruppe.beschreibung" class="text-sm mt-1">{{ gruppe.beschreibung }}</div>
							</div>
							<div :class="['grid gap-2', gruppe.uiAnzahlSpalten === 0 ? 'grid-cols-1' : gruppe.uiAnzahlSpalten > 6 ? 'grid-cols-6' : `grid-cols-${gruppe.uiAnzahlSpalten}`]">
								<template v-for="vp in gruppe.reportvorlageParameter" :key="vp.name">
									<div v-if="istParameterSichtbar(gruppe, vp)"
										:class="[vp.uiAnzahlSpalten === 0 ? 'col-span-1' : vp.uiAnzahlSpalten > 6 ? 'col-span-6' : `col-span-${vp.uiAnzahlSpalten}`, { 'opacity-50': !istParameterAktiv(gruppe, vp) }]">
										<component :is="inputComponent(vp)" v-model="parameterWert(vp).value" :name="vp.name" :disabled="!istParameterAktiv(gruppe, vp)" />
										<label :for="vp.name"> {{ vp.bezeichnung }} </label>
										<svws-ui-tooltip v-if="!istParameterAktiv(gruppe, vp)">
											<span class="icon i-ri-information-line" />
											<template #content>
												{{ parameterBerechtigungText(gruppe, vp) }}
											</template>
										</svws-ui-tooltip>
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
								<template v-if="istSichtbar(gruppe) && !gruppe.sortierungDefinitionenOptionen.isEmpty()">
									<span>{{ gruppe.bezeichnung }}</span>
									<ui-select :manager="mapSelectManagerSortierung.get(gruppe.bezeichnung)" :disabled="!istAktiv(gruppe)"
										:model-value="gruppe.sortierungDefinitionen.isEmpty() ? undefined : gruppe.sortierungDefinitionen.get(0)"
										@update:model-value="v => v instanceof ReportingSortierungDefinition ? gruppe.sortierungDefinitionen = ListUtils.create1(v) : gruppe.sortierungDefinitionen.clear()" />
									<svws-ui-tooltip v-if="!istAktiv(gruppe)">
										<span class="icon i-ri-information-line" />
										<template #content>
											{{ gruppeBerechtigungText(gruppe) }}
										</template>
									</svws-ui-tooltip>
								</template>
							</template>
						</div>
					</div>
					<div v-if="parameter.filterDefinitionenGruppen.size() > 0" class="border-2 border-ui-25 rounded-md p-2 my-2 col-span-full">
						<div class="font-bold mb-2">Filterung</div>
						<div class="flex items-center gap-x-4 gap-y-2">
							<template v-for="gruppe of parameter.filterDefinitionenGruppen" :key="gruppe.bezeichnung">
								<template v-if="istSichtbar(gruppe) && !gruppe.filterDefinitionenOptionen.isEmpty()">
									<span>{{ gruppe.bezeichnung }}</span>
									<ui-select-multi v-if="gruppe.uiIstFilterMultiselect" :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" :disabled="!istAktiv(gruppe)" :model-value="gruppe.filterDefinitionen"
										@update:model-value="v => filterUpdate(v, gruppe.filterDefinitionen)" />
									<ui-select v-else :manager="mapSelectManagerFilter.get(gruppe.bezeichnung)" :disabled="!istAktiv(gruppe)"
										:model-value="gruppe.filterDefinitionen.isEmpty() ? undefined : gruppe.filterDefinitionen.get(0)"
										@update:model-value="v => v instanceof ReportingFilterDefinition ? gruppe.filterDefinitionen = ListUtils.create1(v) : gruppe.filterDefinitionen.clear()" />
									<svws-ui-tooltip v-if="!istAktiv(gruppe)">
										<span class="icon i-ri-information-line" />
										<template #content>
											{{ gruppeBerechtigungText(gruppe) }}
										</template>
									</svws-ui-tooltip>
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
							<svws-ui-button v-if="serverState.hasDev && createHtmlPreview"
								@click="openHtmlPreview" :is-loading class="mt-4">
								<svws-ui-spinner v-if="isLoading" spinning />
								<span v-else class="icon i-ri-eye-line" />
								Vorschau
							</svws-ui-button>
							<svws-ui-button v-if="serverState.hasDev && showJson" @click="downloadJSON('html')" :is-loading class="mt-4">
								JSON für HTML
							</svws-ui-button>
							<svws-ui-button v-if="serverState.hasDev && showJson" @click="downloadJSON('pdf')" :is-loading class="mt-4">
								JSON für PDF
							</svws-ui-button>
						</div>
					</template>
					<!-- E-Mail-Eingabefelder -->
					<template v-if="(parameter.ausgabeformatOptionen.contains(ReportingAusgabeformat.EMAIL.getId())) && serverState.hasAlpha && (parameter.eMailDaten !== null)">
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
						<svws-ui-button v-if="serverState.hasDev && showJson" @click="downloadJSON('email')" :is-loading class="mt-4">
							JSON
						</svws-ui-button>
					</template>
				<!-- Ende: E-Mail-Eingabefelder -->
				</div>
			</div>
			<div v-else>
				<svws-ui-textarea-input v-model="altParameter" />
				Achtung, die Buttons nur klicken, wenn die passende ausgabeformatOptionen gesetzt ist, sonst gibt es einen Fehler!
				<div class="text-left col-span-4 flex gap-2">
					<svws-ui-button @click="downloadPDF" :is-loading class="mt-4">
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-printer-line" />
						Drucken
					</svws-ui-button>
					<svws-ui-button v-if="serverState.hasDev && createHtmlPreview"
						@click="openHtmlPreview" :is-loading class="mt-4">
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-eye-line" />
						Vorschau
					</svws-ui-button>
					<svws-ui-button @click="sendPdfByEmail" :is-loading>
						<svws-ui-spinner v-if="isLoading" spinning />
						<span v-else class="icon i-ri-mail-send-line" />
						E-Mails senden
					</svws-ui-button>
					<svws-ui-button @click="downloadJSONAusTextfeld" :is-loading>
						<span class="icon i-ri-save-3-line" />
						JSON speichern
					</svws-ui-button>
				</div>
			</div>
		</svws-ui-tab-bar>
	</div>
	<!-- HTML-Vorschau (rechte Spalte, nur in DEV mit verfügbarer Vorschau-API) -->
	<!-- <html-preview :html="previewHtml" v-if="serverState.hasDev && (createHtmlPreview !== undefined)" /> -->
	<div v-if="serverState.hasDev && createHtmlPreview" class="w-full sticky top-2 h-[calc(100vh-16rem)] flex flex-col">
		<div class="relative flex-1 min-h-0 overflow-hidden rounded-md border-2 border-ui-25 bg-white shadow-sm flex items-center justify-center">
			<iframe v-if="previewHtml !== ''" :srcdoc="previewHtml" class="absolute inset-0 w-full h-full border-0 bg-white" title="Reporting-Vorschau" />
			<div v-else class="text-black italic text-center p-8">
				Erstellen Sie eine Vorschau, um diese hier anzeigen zu lassen.
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
	import type { ReportingReportvorlageParameterGruppe } from "../../../../core/src/core/data/reporting/ReportingReportvorlageParameterGruppe";
	import { ServerMode } from "../../../../core/src/core/types/ServerMode";
	import { BenutzerKompetenz } from "../../../../core/src/core/types/benutzer/BenutzerKompetenz";
	import type { List } from "../../../../core/src/java/util/List";
	import { ArrayList } from "../../../../core/src/java/util/ArrayList";
	import { ReportingAusgabeformat } from "../../../../core/src/core/types/reporting/ReportingAusgabeformat";
	import { ReportingReportvorlageParameterTyp } from "../../../../core/src/core/types/reporting/ReportingReportvorlageParameterTyp";
	import { SelectManager } from "../../../../ui/src/ui/controls/select/manager/SelectManager";
	import { ReportingSortierungDefinition } from "../../../../core/src/core/data/reporting/ReportingSortierungDefinition";
	import { ReportingFilterDefinition } from "../../../../core/src/core/data/reporting/ReportingFilterDefinition";
	import { ListUtils } from "../../../../core/src";
	import type { TabData } from "../../ui/nav/TabData";
	import { TabManager } from "../../ui/nav/TabManager";

	import { useServerState } from "../../states/ServerState";
	import { useReportingState } from "../../states/ReportingState.js";

	const props = defineProps<{
		showJson?: boolean;
		reportvorlage?: ReportingReportvorlage;
		idHauptdatenObjekt?: number;
		idsHauptdaten?: Iterable<number>;
		idsDetaildaten?: Iterable<number>;
		ausgabeformat?: number;
		createHtmlPreview?: boolean;
	}>();

	const serverState = useServerState();
	const reportingState = useReportingState();

	/** Ein Element (Parameter oder Gruppe) mit Anforderungen an ServerMode und Benutzerkompetenzen. */
	type ElementMitAnforderung = { uiIstSichtbar: boolean; uiErforderlicherServerMode: string; uiErforderlicheKompetenzen: List<number> };

	/** Prüft, ob der erforderliche ServerMode vom aktuellen Servermodus erfüllt wird (leerer Text ⇒ STABLE ⇒ alle Modi). */
	function istServerModeErfuellt(erforderlicherServerMode: string): boolean {
		return ServerMode.getByText(erforderlicherServerMode).checkServerMode(serverState.mode);
	}

	/** Prüft, ob der Benutzer mindestens eine der erforderlichen Kompetenzen besitzt (leere übergebene Liste liefert true).
	 *  AKTUELL: Die Funktion liefert immer true für jede korrekt übergebene Kompetenz, da BenutzerState für Kompetenz-Prüfung noch nicht implementiert ist. */
	function hatKompetenz(erforderlicheKompetenzen: List<number>): boolean {
		if (erforderlicheKompetenzen.isEmpty()) {
			return true;
		}
		for (const id of erforderlicheKompetenzen) {
			const kompetenz = BenutzerKompetenz.getByID(id);
			// TODO: Wenn BenutzerState implementiert wurde, dann mittels '(kompetenz !== null) && benutzerState.benutzerKompetenzen.has(kompetenz)' prüfen.
			if (kompetenz !== null) {
				return true;
			}
		}
		return false;
	}

	/** Liefert die Bezeichnungen der Kompetenzen, die dem Benutzer für diese Anforderung fehlen (leer, wenn die Anforderung erfüllt ist). */
	function fehlendeKompetenzNamen(erforderlicheKompetenzen: List<number>): string[] {
		if (hatKompetenz(erforderlicheKompetenzen)) {
			return [];
		}
		const namen: string[] = [];
		for (const id of erforderlicheKompetenzen) {
			const kompetenz = BenutzerKompetenz.getByID(id);
			if (kompetenz !== null) {
				namen.push(kompetenz.daten.bezeichnung);
			}
		}
		return namen;
	}

	/** Ein Element ist sichtbar, wenn es als sichtbar markiert ist und der ServerMode passt (sonst wird es ausgeblendet). */
	function istSichtbar(element: ElementMitAnforderung): boolean {
		return (element.uiIstSichtbar === true) && istServerModeErfuellt(element.uiErforderlicherServerMode);
	}

	/** Ein Element ist aktiv (bedienbar), wenn der Benutzer die erforderlichen Kompetenzen besitzt (sonst wird es deaktiviert dargestellt). */
	function istAktiv(element: ElementMitAnforderung): boolean {
		return hatKompetenz(element.uiErforderlicheKompetenzen);
	}

	/** Ein Parameter ist sichtbar, wenn seine Gruppe und er selbst sichtbar sind. */
	function istParameterSichtbar(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean {
		return istSichtbar(gruppe) && istSichtbar(vp);
	}

	/** Ein Parameter ist aktiv, wenn seine Gruppe und er selbst die Kompetenzanforderungen erfüllen. */
	function istParameterAktiv(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): boolean {
		return istAktiv(gruppe) && istAktiv(vp);
	}

	/** Der Berechtigungs-Hinweis für einen wegen fehlender Kompetenz deaktivierten Parameter. */
	function parameterBerechtigungText(gruppe: ReportingReportvorlageParameterGruppe, vp: ReportingReportvorlageParameter): string {
		const namen = [...fehlendeKompetenzNamen(gruppe.uiErforderlicheKompetenzen), ...fehlendeKompetenzNamen(vp.uiErforderlicheKompetenzen)];
		return "Es fehlt die Berechtigung: " + namen.join(" oder ");
	}

	/** Der Berechtigungs-Hinweis für eine wegen fehlender Kompetenz deaktivierte Sortier- oder Filtergruppe. */
	function gruppeBerechtigungText(gruppe: ElementMitAnforderung): string {
		return "Es fehlt die Berechtigung: " + fehlendeKompetenzNamen(gruppe.uiErforderlicheKompetenzen).join(" oder ");
	}

	/** Setzt alle aktiven und sichtbaren Checkboxen einer Gruppe auf den angegebenen Wert (deaktivierte/ausgeblendete werden übersprungen). */
	function setzeAlleCheckboxen(gruppe: ReportingReportvorlageParameterGruppe, wert: boolean): void {
		const params = checkboxes.value.get(gruppe.name);
		if (params === undefined) {
			return;
		}
		for (const vp of params) {
			if (istParameterSichtbar(gruppe, vp) && istParameterAktiv(gruppe, vp)) {
				parameterWert(vp).value = wert;
			}
		}
	}

	const tabs = computed<TabData[]>(() => [
		{ name: "eingabe", text: "Eingabe", hide: !props.showJson },
		{ name: "json", text: "JSON", hide: !props.showJson },
	]);

	async function setTab(tab: TabData) {
		selectedRoute.value = tab;
	}

	const tabManager = new TabManager(tabs.value, tabs.value[0], setTab);
	const selectedRoute = ref(tabs.value[0]);

	const isLoading = ref<boolean>(false);
	const previewHtml = ref<string>('');
	const logs = ref<List<string | null>>();
	const status = ref<boolean>();
	const localReportvorlage = ref<ReportingReportvorlage>();
	const localIdHauptdatenObjekt = ref<number>(-1);
	const localIdsHauptdaten = ref<string>("");
	const localIdsDetaildaten = ref<string>("");
	const parameter = ref(new ReportingParameter());
	const altParameter = ref();

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

	async function downloadJSON(type: 'html' | 'pdf' | 'email') {
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		if (type === 'html') {
			parameter.value.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		} else if (type === 'pdf') {
			parameter.value.ausgabeformat = ReportingAusgabeformat.PDF.getId();
		} else {
			parameter.value.ausgabeformat = ReportingAusgabeformat.EMAIL.getId();
		}
		await reportingState.createJSONReportingParameter(parameter.value);
	}

	async function downloadJSONAusTextfeld() {
		await reportingState.createJSONReportingParameter(ReportingParameter.transpilerFromJSON(altParameter.value));
	}

	async function downloadPDF() {
		isLoading.value = true;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		if (selectedRoute.value.name === 'json') {
			parameter.value = ReportingParameter.transpilerFromJSON(altParameter.value);
		}
		try {
			await reportingState.createPDFReport(parameter.value);
		} finally {
			isLoading.value = false;
		}
	}

	async function openHtmlPreview() {
		if (!props.createHtmlPreview) {
			return;
		}
		isLoading.value = true;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		if (selectedRoute.value.name === 'json') {
			parameter.value = ReportingParameter.transpilerFromJSON(altParameter.value);
		}
		parameter.value.ausgabeformat = ReportingAusgabeformat.HTML.getId();
		try {
			previewHtml.value = await reportingState.createHTMLReport(parameter.value);
		} finally {
			isLoading.value = false;
		}
	}

	async function sendPdfByEmail() {
		isLoading.value = true;
		parameter.value.idHauptdatenObjekt = (props.idHauptdatenObjekt !== undefined && props.idHauptdatenObjekt >= 0) ? props.idHauptdatenObjekt : localIdHauptdatenObjekt.value;
		parameter.value.idsHauptdaten = listHauptdaten.value;
		parameter.value.idsDetaildaten = listDetaildaten.value;
		try {
			const result = await reportingState.createEMailReport(parameter.value);
			status.value = result.success;
			logs.value = result.log;
		} finally {
			isLoading.value = false;
		}
	}

</script>
