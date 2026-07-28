<template>
	<div class="page page-flex-row max-w-480">
		<Teleport v-if="hatUpdateKompetenz" defer to=".svws-sub-nav-target">
			<svws-ui-sub-nav>
				<svws-ui-button type="transparent" @click="export_laufbahnplanung"><span class="icon-sm i-ri-upload-2-line" />Exportieren</svws-ui-button>
				<svws-ui-button type="transparent" @click="show = true"><span class="icon-sm i-ri-download-2-line" /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal v-model:show="show" :import-laufbahnplanung="(data) => gostLaufbahnplanungState.importLaufbahnplanung(data)" />
				<svws-ui-button :type="gostLaufbahnplanungState.hatZwischenspeicher ? 'error' : 'transparent'" @click="gostLaufbahnplanungState.saveLaufbahnplanung()">Planung merken</svws-ui-button>
				<svws-ui-button type="danger" @click="gostLaufbahnplanungState.restoreLaufbahnplanung()" v-if="gostLaufbahnplanungState.hatZwischenspeicher">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button :type="manager.modus === 'normal' ? 'transparent' : 'danger'" @click="manager.switchModus()" title="Modus wechseln">
					<span class="icon-sm i-ri-loop-right-line" /> Modus: <span>{{ manager.modus }}</span>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht />
				<svws-ui-button type="transparent" @click="manager.switchFaecherAnzeigen()"> {{ "Fächer anzeigen: " + manager.getTextFaecherAnzeigen() }} </svws-ui-button>
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport defer to=".svws-ui-header--actions">
			<svws-ui-button-select type="secondary" :dropdown-actions="dropdownList">
				<template #icon> <span class="icon i-ri-printer-line" /> </template>
			</svws-ui-button-select>
			<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="grow overflow-y-auto overflow-x-hidden min-w-fit grid content-start auto-cols-min gap-2">
			<div v-if="manager.istAbiturAb2030" class="p-2 rounded-md bg-ui-caution text-ui-oncaution min-w-fit font-bold flex flex-row items-center gap-2">
				<span class="icon-lg i-ri-error-warning-fill icon-ui-oncaution" />
				Sie verwenden den experimentellen Belegprüfungsalgorithmus für das Abitur 2030. Dieser kann noch Fehler enthalten, so dass zur Zeit noch alle Ergebnisse manuell geprüft werden müssen.
			</div>
			<s-laufbahnplanung-card-planung v-if="visible" :manager />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-xl overflow-y-auto overflow-x-hidden pr-4">
			<div class="flex flex-col gap-y-12 lg:gap-y-16">
				<s-laufbahnplanung-card-beratung v-if="visible && hatUpdateKompetenz" :patch-beratungsdaten="doPatchBeratungsdaten" :updated />
				<s-laufbahnplanung-card-gkl v-if="visible && manager.zeigeGKLWahlen()" />
				<s-laufbahnplanung-card-status v-if="visible" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref, watch } from "vue";
	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";
	import { BenutzerKompetenz } from "../../../../../core/src/core/types/benutzer/BenutzerKompetenz";
	import type { GostLaufbahnplanungBeratungsdaten } from "../../../../../core/src/core/data/gost/GostLaufbahnplanungBeratungsdaten";
	import { LaufbahnplanungUiManager } from "./LaufbahnplanungUiManager";
	import { useServerState } from "../../../states/ServerState";
	import { useReportingState } from "../../../states/ReportingState";
	import { ReportingReportvorlage } from "../../../../../core/src/core/types/reporting/ReportingReportvorlage";
	import { useAbschnittState } from "../../../states/AbschnittState";
	import { useGostLaufbahnplanungState } from "../../../states/GostLaufbahnplanungState";
	import { useBenutzerState } from "../../../states/BenutzerState";
	import { useConfigState } from "../../../states/ConfigState";

	const props = defineProps<SchuelerLaufbahnplanungProps>();
	const benutzerState = useBenutzerState();
	const serverState = useServerState();
	const abschnittState = useAbschnittState();
	const reportingState = useReportingState();
	const configState = useConfigState();

	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const manager = computed<LaufbahnplanungUiManager>(() => new LaufbahnplanungUiManager(
		serverState.mode,
		() => configState.config,
		{ faecherZeigen: "app.schueler.laufbahnplanung.faecher.anzeigen", modus: "app.schueler.laufbahnplanung.modus" }
	));

	const hatUpdateKompetenz = computed<boolean>(() => {
		if ((gostLaufbahnplanungState.schuelerOrNull === null) || (gostLaufbahnplanungState.schuelerOrNull.abiturjahrgang === null)) {
			return false;
		}
		return benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_ALLGEMEIN)
			|| (benutzerState.benutzerHatKompetenz(BenutzerKompetenz.OBERSTUFE_LAUFBAHNPLANUNG_FUNKTIONSBEZOGEN)
				&& benutzerState.kompetenzenAbiturjahrgaenge.has(gostLaufbahnplanungState.schuelerOrNull.abiturjahrgang));
	});

	const visible = computed<boolean>(() => (gostLaufbahnplanungState.schuelerOrNull !== null)
		&& (gostLaufbahnplanungState.schuelerOrNull.abiturjahrgang !== null));

	const show = ref<boolean>(false);

	const updated = ref<boolean>(false);
	const curId = ref<number | undefined>();

	watch(() => [gostLaufbahnplanungState.schuelerOrNull, gostLaufbahnplanungState.gostBelegpruefungErgebnis],
		([neu, neu2], [alt, alt2]) => {
			if (gostLaufbahnplanungState.schuelerOrNull === null) {
				updated.value = false;
				curId.value = undefined;
				return;
			}
			if (alt !== neu) {
				updated.value = false;
				curId.value = undefined;
			}
			if ((neu2 !== alt2) && (updated.value === false) && (curId.value === gostLaufbahnplanungState.schuelerOrNull.id)) {
				updated.value = true;
			} else {
				curId.value = gostLaufbahnplanungState.schuelerOrNull.id;
			}
		});

	async function doPatchBeratungsdaten(data: Partial<GostLaufbahnplanungBeratungsdaten>) {
		await gostLaufbahnplanungState.patchBeratungsdaten(data);
		updated.value = false;
	}

	const dropdownList = [
		{ text: "Laufbahnwahlbogen", action: () => downloadPDF("Laufbahnwahlbogen"), default: true },
		{ text: "Laufbahnwahlbogen (nur Belegung)", action: () => downloadPDF("Laufbahnwahlbogen (nur Belegung)") },
	];

	async function downloadPDF(title: string) {
		const reportingParameter = ReportingReportvorlage.SCHUELER_V_GOST_LAUFBAHNPLANUNG_WAHLBOGEN.getReportingParameter();
		reportingParameter.idSchuljahresabschnitt = abschnittState.auswahl.id;
		reportingParameter.idsHauptdaten.add(gostLaufbahnplanungState.schueler.id);
		schleifen: for (const gruppe of reportingParameter.reportvorlageParameterGruppen) {
			if (gruppe.name === "Inhaltsoptionen") {
				for (const vp of gruppe.reportvorlageParameter) {
					if (vp.name === "nurBelegteFaecher") {
						vp.wert = (title === "Laufbahnwahlbogen (nur Belegung)").toString();
						break schleifen;
					}
				}
			}
		}
		await reportingState.createPDFReport(reportingParameter);
	}

	async function export_laufbahnplanung() {
		const { data, name } = await gostLaufbahnplanungState.exportLaufbahnplanung();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

</script>
