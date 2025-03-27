<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Stundenplan drucken</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<div>
					<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
						:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
				</div>
				<div class="text-left">
					<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
					<svws-ui-checkbox v-model="option4">Fach- statt Kursbezeichnung verwenden (nicht Sek-II)</svws-ui-checkbox><br>
					<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox><br>
					<svws-ui-checkbox v-model="option16">Individuelle Kursart anzeigen</svws-ui-checkbox>
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button :disabled="stundenplanAuswahl === undefined" @click="downloadPDF" :is-loading="loading">
				<svws-ui-spinner v-if="loading" spinning />
				Drucken
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';
	import type { StundenplanListeEintrag, ApiFile} from '@core';
	import { DateUtils, ReportingParameter, ReportingReportvorlage } from '@core';

	const props = defineProps<{
		mapStundenplaene: Map<number, StundenplanListeEintrag>;
		getPDF: (parameter: ReportingParameter, idStundenplan: number) => Promise<ApiFile>;
		apiStatus: ApiStatus;
	}>();

	const show = ref<boolean>(false);

	function openModal() {
		show.value = true;
	}

	const loading = ref<boolean>(false);
	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const option2 = ref(false);
	const option4 = ref(false);
	const option8 = ref(false);
	const option16 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined)
			return;
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_SCHUELER_STUNDENPLAN.getBezeichnung();
		reportingParameter.einzelausgabeDetaildaten = false;
		reportingParameter.detailLevel = (option2.value ? 2 : 0) + (option4.value ? 4 : 0) + (option8.value ? 8 : 0) + (option16.value ? 16 : 0);
		const { data, name } = await props.getPDF(reportingParameter, stundenplanAuswahl.value.id);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
		loading.value = false;
		show.value = false;
	}

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.' ];

	function toDateStr(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string) : string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

</script>
