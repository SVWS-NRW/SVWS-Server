<template>
	<div class="content-card overflow-auto h-full w-full">
		<div class="content-card--content">
			<div class="flex flex-col items-start gap-3 p-2">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Bezeichnung" v-model="bezeichnung" type="text" />
					<svws-ui-text-input placeholder="Gültig ab" v-model="gueltigAb" type="date" />
					<abschnitt-auswahl :headless="false" :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="(a) => abschnitt = a" :akt-schulabschnitt="aktSchulabschnitt" />
					<div class="col-span-full">
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
			<log-box :logs="logs" :status="status" />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { List, Schuljahresabschnitt, SimpleOperationResponse } from "@core";
	import { StundenplanListeEintragMinimal, DateUtils } from "@core";

	const props = defineProps<{
		setUntisImportGPU: (formData: FormData) => Promise<SimpleOperationResponse>;
		abschnitte: Map<number, Schuljahresabschnitt>;
		aktAbschnitt: Schuljahresabschnitt;
		aktSchulabschnitt: number;
	}>();

	const bezeichnung = ref<string>("");
	const gueltigAb = ref<string>("");
	// eslint-disable-next-line vue/no-setup-props-destructure
	const abschnitt = ref<Schuljahresabschnitt>(props.aktAbschnitt);
	const file = ref<File | null>(null);

	const status = ref<boolean | undefined>(undefined);
	const loading = ref<boolean>(false);
	const logs = ref<List<string|null>>();

	function onFileChanged(event: Event) {
		const target = event.target as HTMLInputElement;
		if (target && target.files)
			file.value = target.files[0];
	}

	async function import_file(event: Event) {
		if (!file.value || abschnitt.value === undefined || bezeichnung.value.length < 1)
			return;
		status.value = undefined;
		loading.value = true;
		const entry = new StundenplanListeEintragMinimal();
		entry.bezeichnung = bezeichnung.value;
		entry.idSchuljahresabschnitt = abschnitt.value.id;
		entry.gueltigAb = gueltigAb.value;
		try {
			DateUtils.extractFromDateISO8601(entry.gueltigAb);
		} catch (e) {
			const a = abschnitt.value.abschnitt;
			const s = abschnitt.value.schuljahr;
			entry.gueltigAb = a === 1 ? `${s}-08-01` : `${s+1}-02-01`
		}
		const formData = new FormData();
		formData.append('entry', StundenplanListeEintragMinimal.transpilerToJSON(entry));
		formData.append("data", file.value);
		const result = await props.setUntisImportGPU(formData);
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
