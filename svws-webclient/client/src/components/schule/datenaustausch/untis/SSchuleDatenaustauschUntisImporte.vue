<template>
	<div class="page--content page--content--flex-row gap-2 h-full w-full overflow-hidden">
		<!-- Auswahl des Untis-Importes (linke Seite) -->
		<div class="h-full min-w-48 w-48 flex flex-col gap-2">
			<svws-ui-button id="contentFocusField" :type="(aktuell === 'stundenplanGPU001') ? 'primary' : 'secondary'" @click="onSelect('stundenplanGPU001')">
				<div class="flex flex-col gap-1">
					<p class="text-left font-bold ">Stundenplan</p>
					<p class="text-left font-normal">GPU001.txt</p>
				</div>
			</svws-ui-button>
			<svws-ui-button id="contentFocusField" :type="(aktuell === 'stundenplanGPU001002') ? 'primary' : 'secondary'" @click="onSelect('stundenplanGPU001002')">
				<div class="flex flex-col gap-1">
					<p class="text-left font-bold ">Stundenplan</p>
					<p class="text-left font-normal">GPU001.txt, GPU002.txt</p>
				</div>
			</svws-ui-button>
			<svws-ui-button :type="(aktuell === 'rauemeGPU005') ? 'primary' : 'secondary'" @click="onSelect('rauemeGPU005')">
				<div class="flex flex-col gap-1">
					<p class="text-left font-bold ">Raumliste</p>
					<p class="text-left font-normal">GPU005.txt</p>
				</div>
			</svws-ui-button>
		</div>

		<!-- Weitere Eingabemöglichkeiten für den zuvor gewählten Untis-Import (rechte Seite - spezielle Ansicht nach Auswahl) -->
		<div class="flex flex-col gap-8">
			<div v-if="aktuell === 'rauemeGPU005'" class="max-w-196">
				<div class="text-headline-md mb-4">Raumliste aus Untis importieren</div>
				<div class="flex flex-col gap-2 mb-4">
					<p>
						Laden Sie die Datei
						<svws-ui-tooltip>
							<span class="font-bold flex flex-row gap-1">GPU005.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
							<template #content>
								Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die Textbegrenzung doppelte Anführungszeichen (")
							</template>
						</svws-ui-tooltip>
						von Untis hier hoch, um den Raum-Katalog mit den Räumen aus dieser Datei zu ergänzen.
					</p>
				</div>
				<svws-ui-input-wrapper>
					<input id="contentFocusField" type="file" accept=".txt,.csv" @change="importRauemeGPU005" :disabled="loading">
				</svws-ui-input-wrapper>
			</div>
			<div v-if="[ 'stundenplanGPU001', 'stundenplanGPU001002' ].includes(aktuell)" class="max-w-196">
				<svws-ui-input-wrapper>
					<div class="text-headline-md">Stunden aus Untis importieren</div>
					<div v-if="aktuell === 'stundenplanGPU001'" class="mb-4"><span class="font-bold">Hinweis:</span> Diese Importvariante unterstützt keine Mehrwochenpläne</div>
					<div v-if="aktuell === 'stundenplanGPU001002'" class="mb-4"><span class="font-bold">Hinweis:</span> Diese Importvariante unterstützt Mehrwochenpläne, aber nur, wenn die Wochentypen vor dem Rechnen des Stundenplanes ferstgelegt wurden</div>
					<svws-ui-text-input placeholder="Gültig ab" v-model="gueltigAb" type="date" />
					<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" />
					<svws-ui-tooltip>
						<svws-ui-checkbox v-model="ignoreMissing">Ignoriere Unterrichts-Einträge mit fehlerhaften Daten</svws-ui-checkbox>
						<template #content>
							<p>Ist die Einstellung aktiviert, so werden keine Fehler bei den folgenden fehlerhaften Daten erzeugt, sondern nur Meldungen im Log.</p>
							<p>Sind die Kürzel für Klassen, Fächer oder Kurse nicht in der SVWS-Datenbank vorhanden, so werden die zugehörigen Unterrichte ignoriert.</p>
							<p>Ist ein Kürzel für einen Lehrer nicht in der SVWS-Datenbank vorhanden, so wird der Unterricht ohne zugeordneten Lehrer angelegt.</p>
						</template>
					</svws-ui-tooltip>
				</svws-ui-input-wrapper>
			</div>
			<div v-if="aktuell === 'stundenplanGPU001'" class="max-w-196">
				<div class="flex flex-col gap-2 mb-4">
					<p>
						Laden Sie die Datei
						<svws-ui-tooltip>
							<span class="font-bold flex flex-row gap-1">GPU001.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
							<template #content>
								Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die Textbegrenzung doppelte Anführungszeichen (")
							</template>
						</svws-ui-tooltip>
						von Untis hier hoch, um den Stundenplan zu importieren.
					</p>
				</div>
				<input id="contentFocusField" type="file" accept=".txt,.csv" @change="importStundenplanGPU001" :disabled="loading">
			</div>
			<div v-if="aktuell === 'stundenplanGPU001002'" class="max-w-196">
				<div class="flex flex-col gap-2 mb-4">
					<p>
						<span v-if="tmpGPU001?.valid ?? false" class="icon i-ri-checkbox-circle-fill icon-ui-success inline-block" />
						<span v-if="!(tmpGPU001?.valid ?? true)" class="icon i-ri-alert-fill icon-ui-danger inline-block" />
						Laden Sie die Datei
						<svws-ui-tooltip>
							<span class="font-bold flex flex-row gap-1">GPU001.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
							<template #content>
								Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die Textbegrenzung doppelte Anführungszeichen (")
							</template>
						</svws-ui-tooltip>
						von Untis hier hoch, um den Stundenplan zu importieren.
					</p>
					<input id="contentFocusField" type="file" accept=".txt,.csv" @change="(event) => importStundenplanGPU001002(1, event)" :disabled="loading">
				</div>
				<div class="flex flex-col gap-2 mt-8 mb-4">
					<p>
						<span v-if="tmpGPU002?.valid ?? false" class="icon i-ri-checkbox-circle-fill icon-ui-success inline-block" />
						<span v-if="!(tmpGPU002?.valid ?? true)" class="icon i-ri-alert-fill icon-ui-danger inline-block" />
						Laden Sie die Datei
						<svws-ui-tooltip>
							<span class="font-bold flex flex-row gap-1">GPU002.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
							<template #content>
								Die CSV-Datei muss als Textkodierung UTF-8 verwenden. Als Trennzeichen wird das Semikolon verwendet und für die Textbegrenzung doppelte Anführungszeichen (")
							</template>
						</svws-ui-tooltip>
						von Untis hier hoch, um den Stundenplan zu importieren.
					</p>
					<input id="contentFocusField" type="file" accept=".txt,.csv" @change="(event) => importStundenplanGPU001002(2, event)" :disabled="loading">
				</div>
				<div class="mt-8 w-full flex flex-row gap-2">
					<svws-ui-text-input placeholder="Bezeichner WochenTyp 1" :model-value="wochentypen[0]" @update:model-value="(value) => setWochentypBezeichner(1, value)" required />
					<svws-ui-text-input placeholder="Bezeichner WochenTyp 2" :model-value="wochentypen[1]" @update:model-value="(value) => setWochentypBezeichner(2, value)" required />
					<svws-ui-text-input placeholder="Bezeichner WochenTyp 3" :model-value="wochentypen[2]" @update:model-value="(value) => setWochentypBezeichner(3, value)" required />
					<svws-ui-text-input placeholder="Bezeichner WochenTyp 4" :model-value="wochentypen[3]" @update:model-value="(value) => setWochentypBezeichner(4, value)" required />
				</div>
				<div class="mt-2 mb-4 h-64 w-full overflow-scroll grow bg-ui border border-ui-secondary text-ui rounded-md text-base pt-2 pl-2 pr-6 pb-6">
					<pre>{{ resultGPU001?.asString ?? null }}</pre>
				</div>
				<svws-ui-button v-if="resultGPU001 !== null" @click="importStundenplanGPU001002Starten">Import starten</svws-ui-button>
			</div>
			<div>
				<svws-ui-spinner :spinning="loading" />
			</div>
		</div>

		<!-- Die Ausgabe des Logs -->
		<div v-if="status !== null" class="w-full h-full overflow-hidden flex flex-col gap-4">
			<log-box :logs="status.log" :status="status.success" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref, shallowRef } from 'vue';
	import type { SchuleDatenaustauschUntisImporteProps } from './SSchuleDatenaustauschUntisImporteProps';
	import { StundenplanListeEintragMinimal, type Schuljahresabschnitt, type SimpleOperationResponse } from '@core';
	import { UntisGPU001Csv, UntisGPU002Csv } from './UntisGPU';

	const props = defineProps<SchuleDatenaustauschUntisImporteProps>();

	type GPU = 'rauemeGPU005' | 'stundenplanGPU001' | 'stundenplanGPU001002';
	const aktuell = shallowRef<GPU>('stundenplanGPU001');

	const status = shallowRef<SimpleOperationResponse | null>(null);
	const loading = ref<boolean>(false);

	const bezeichnung = ref<string>('');
	const gueltigAb = ref<string | null>(null);
	const ignoreMissing = ref<boolean>(true);

	const tmpGPU001 = shallowRef<UntisGPU001Csv | null>(null);
	const tmpGPU002 = shallowRef<UntisGPU002Csv | null>(null);
	const wochentypen = ref<string[]>([ '', '', '', '' ]);
	const resultGPU001 = shallowRef<UntisGPU001Csv | null>(null);

	onMounted(() => onSelect(aktuell.value));

	function getGueltigAb(ab: Schuljahresabschnitt) {
		const a = ab.abschnitt;
		const s = ab.schuljahr;
		return (a === 1) ? `${s}-08-01` : `${s+1}-02-01`
	}

	function initStundenplanParameter() {
		gueltigAb.value = getGueltigAb(props.schuljahresabschnitt());
		bezeichnung.value = `Import ${new Date().toLocaleDateString('de', {day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Europe/Berlin', hour: 'numeric', minute: 'numeric'})}`;
		tmpGPU001.value = null;
		tmpGPU002.value = null;
		wochentypen.value[0] = 'A';
		wochentypen.value[1] = 'B';
		wochentypen.value[2] = 'C';
		wochentypen.value[3] = 'D';
		resultGPU001.value = null;
	}

	function onSelect(value : GPU): void {
		if (aktuell.value === value)
			return;
		aktuell.value = value;
		if ([ 'stundenplanGPU001', 'stundenplanGPU001002' ].includes(aktuell.value))
			initStundenplanParameter();
		status.value = null;
	}

	async function updateStundenplanGPU001002() {
		if ((tmpGPU001.value === null) || (tmpGPU002.value === null)) {
			resultGPU001.value = null;
			return;
		}
		const mapWochentypenByUnterrichtID = tmpGPU002.value.getMapWochentypByUnterrichtsID(wochentypen.value);
		if (mapWochentypenByUnterrichtID.size === 0) {
			resultGPU001.value = tmpGPU001.value;
			return;
		}
		resultGPU001.value = new UntisGPU001Csv(tmpGPU001.value.asString, mapWochentypenByUnterrichtID);
	}

	async function setWochentypBezeichner(wt: number, value: string | null) {
		if (value === null)
			return;
		wochentypen.value[wt-1] = value;
		await updateStundenplanGPU001002();
	}

	async function importStundenplanGPU001002(gpu: number, event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (file === null)
			return;
		if (gpu === 1)
			tmpGPU001.value = new UntisGPU001Csv(await file.text());
		else if (gpu === 2)
			tmpGPU002.value = new UntisGPU002Csv(await file.text());
		await updateStundenplanGPU001002();
	}

	async function importStundenplanGPU001002Starten() {
		if (resultGPU001.value === null)
			return;
		status.value = null;
		loading.value = true;
		const entry = new StundenplanListeEintragMinimal();
		entry.bezeichnung = bezeichnung.value;
		entry.idSchuljahresabschnitt = props.schuljahresabschnitt().id;
		entry.gueltigAb = gueltigAb.value ?? getGueltigAb(props.schuljahresabschnitt());
		const formData = new FormData();
		formData.append('entry', StundenplanListeEintragMinimal.transpilerToJSON(entry));
		formData.append("data", resultGPU001.value.asString);
		const result = await props.importUntisStundenplanGPU001(formData, ignoreMissing.value);
		initStundenplanParameter();
		status.value = result;
		loading.value = false;
	}

	async function importStundenplanGPU001(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (file === null)
			return;
		if (bezeichnung.value.length < 1)
			return;
		status.value = null;
		loading.value = true;
		const entry = new StundenplanListeEintragMinimal();
		entry.bezeichnung = bezeichnung.value;
		entry.idSchuljahresabschnitt = props.schuljahresabschnitt().id;
		entry.gueltigAb = gueltigAb.value ?? getGueltigAb(props.schuljahresabschnitt());
		const formData = new FormData();
		formData.append('entry', StundenplanListeEintragMinimal.transpilerToJSON(entry));
		formData.append("data", file);
		const result = await props.importUntisStundenplanGPU001(formData, ignoreMissing.value);
		initStundenplanParameter();
		status.value = result;
		loading.value = false;
	}

	async function importRauemeGPU005(event: Event) {
		const target = event.target as HTMLInputElement;
		if ((target.files === null) || (target.files.length === 0))
			return;
		const file = target.files.item(0);
		if (file === null)
			return;
		status.value = null;
		loading.value = true;
		const formData = new FormData();
		formData.append("data", file);
		const result = await props.importUntisRaeumeGPU005(formData);
		status.value = result;
		loading.value = false;
	}

</script>
