<template>
	<div class="page--content overflow-hidden flex flex-col">
		<div class="flex flex-col items-start gap-2">
			<svws-ui-input-wrapper>
				<abschnitt-auswahl :disable-headless="true" :daten="schuljahresabschnittsauswahl" />
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
				<svws-ui-spacing />
				<div class="col-span-full flex flex-row gap-2">
					<svws-ui-tooltip>
						<span class="font-bold flex flex-row gap-1">GPU001.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
						<template #content>
							Die CSV-Datei muss als Textkodierung UTF-8 ohne BOM verwenden. Als Trennzeichen wird das Semikolon verwendet und für die textbegrenzung doppelte Anführungszeichen (")
						</template>
					</svws-ui-tooltip>
					<input id="contentFocusField" type="file" accept=".txt" @change="onFileChanged" :disabled="loading">
				</div>
				<div class="col-span-full">
					<svws-ui-button type="secondary" @click="import_file" :disabled="bezeichnung.length < 1 || !file || loading"> Stundenplan importieren </svws-ui-button>
				</div>
			</svws-ui-input-wrapper>
			<div>
				<svws-ui-spinner :spinning="loading" />
			</div>
		</div>
		<log-box :logs="logs" :status="status" hfull />
	</div>
</template>

<script setup lang="ts">

	import { onMounted, ref } from 'vue';
	import type { SchuleDatenaustauschUntisStundenplanProps } from './SSchuleDatenaustauschUntisStundenplanProps';
	import type { List, Schuljahresabschnitt} from "@core";
	import { StundenplanListeEintragMinimal } from "@core";

	const props = defineProps<SchuleDatenaustauschUntisStundenplanProps>();

	const bezeichnung = ref<string>(`Import ${new Date().toLocaleDateString('de', {day: '2-digit', month: '2-digit', year: 'numeric', timeZone: 'Europe/Berlin', hour: 'numeric', minute: 'numeric'})}`);
	const gueltigAb = ref<string | null>(null);

	onMounted(() => {
		gueltigAb.value = getGueltigAb(props.schuljahresabschnittsauswahl().aktuell);
	});

	const file = ref<File | null>(null);
	const ignoreMissing = ref<boolean>(true);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();

	function onFileChanged(event: Event) {
		const target = <HTMLInputElement>event.currentTarget;
		if (target.files !== null)
			file.value = target.files[0];
	}

	function getGueltigAb(ab: Schuljahresabschnitt) {
		const a = ab.abschnitt;
		const s = ab.schuljahr;
		return a === 1 ? `${s}-08-01` : `${s+1}-02-01`
	}

	async function import_file() {
		if ((file.value === null) || (bezeichnung.value.length < 1))
			return;
		status.value = undefined;
		loading.value = true;
		const entry = new StundenplanListeEintragMinimal();
		entry.bezeichnung = bezeichnung.value;
		entry.idSchuljahresabschnitt = props.schuljahresabschnittsauswahl().aktuell.id;
		entry.gueltigAb = gueltigAb.value ?? getGueltigAb(props.schuljahresabschnittsauswahl().aktuell);
		const formData = new FormData();
		formData.append('entry', StundenplanListeEintragMinimal.transpilerToJSON(entry));
		formData.append("data", file.value);
		const result = await props.importUntisStundenplanGPU001(formData, ignoreMissing.value);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
		gueltigAb.value = null;
		bezeichnung.value = '';
		file.value = null;
	}

	defineExpose({
		status
	});

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply flex flex-col overflow-y-hidden overflow-x-auto h-full;
	}

</style>
