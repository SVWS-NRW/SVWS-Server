<template>
	<div class="page--content overflow-hidden flex flex-col">
		<div class="flex flex-col items-start gap-2">
			<svws-ui-input-wrapper :grid="2">
				<abschnitt-auswahl :disable-headless="true" :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="a => setAbschnitt(a)" :akt-schulabschnitt="aktSchulabschnitt" />
				<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" />
				<svws-ui-text-input placeholder="Gültig ab" v-model="gueltigAb" type="date" />
				<div class="col-span-full flex flex-row gap-2">
					<svws-ui-tooltip>
						<span class="font-bold flex flex-row gap-1">GPU001.txt: <span class="icon i-ri-information-line mt-0.5" /></span>
						<template #content>
							Die CSV-Datei muss als Textkodierung UTF-8 ohne BOM verwenden. Als Trennzeichen wird das Semikolon verwendet und für die textbegrenzung doppelte Anführungszeichen (")
						</template>
					</svws-ui-tooltip>
					<input type="file" accept=".txt" @change="onFileChanged" :disabled="loading">
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

	import { ref } from 'vue';
	import type { SchuleDatenaustauschUntisStundenplanProps } from './SSchuleDatenaustauschUntisStundenplanProps';
	import { type List, type Schuljahresabschnitt, StundenplanListeEintragMinimal } from "@core";

	const props = defineProps<SchuleDatenaustauschUntisStundenplanProps>();

	const bezeichnung = ref<string>("Import Untis");
	// eslint-disable-next-line vue/no-setup-props-destructure
	const gueltigAb = ref<string>(getGueltigAb(props.aktAbschnitt));
	// eslint-disable-next-line vue/no-setup-props-destructure
	const abschnitt = ref<Schuljahresabschnitt>(props.aktAbschnitt);
	const file = ref<File | null>(null);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string | null> | undefined>();

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files)
			file.value = target.files[0];
	}

	function getGueltigAb(ab: Schuljahresabschnitt) {
		const a = ab.abschnitt;
		const s = ab.schuljahr;
		return a === 1 ? `${s}-08-01` : `${s+1}-02-01`
	}

	function setAbschnitt(a: Schuljahresabschnitt) {
		gueltigAb.value = getGueltigAb(a);
		abschnitt.value = a;
	}

	async function import_file() {
		if (!file.value || abschnitt.value === undefined || bezeichnung.value.length < 1)
			return;
		status.value = undefined;
		loading.value = true;
		const entry = new StundenplanListeEintragMinimal();
		entry.bezeichnung = bezeichnung.value;
		entry.idSchuljahresabschnitt = abschnitt.value.id;
		entry.gueltigAb = gueltigAb.value;
		const formData = new FormData();
		formData.append('entry', StundenplanListeEintragMinimal.transpilerToJSON(entry));
		formData.append("data", file.value);
		const result = await props.importUntisStundenplanGPU001(formData);
		logs.value = result.log;
		status.value = result.success;
		loading.value = false;
		gueltigAb.value = '';
		bezeichnung.value = '';
		abschnitt.value = props.aktAbschnitt;
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
./SSchuleDatenaustauschUntisStundenplanProps