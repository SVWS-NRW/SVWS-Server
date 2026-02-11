<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Stundenplan drucken</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<div>
					<svws-ui-select title="Stundenplan" v-model="stundenplanAuswahl" :items="mapStundenplaene.values()"
						:item-text="s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')'" />
				</div>
				<div>
					<svws-ui-radio-group>
						<svws-ui-radio-option :value="0" v-model="gruppe1" name="Unterrichte" label="Unterrichte" />
						<svws-ui-radio-option :value="1" v-model="gruppe1" name="Unterrichte" label="Unterrichte und Pausenaufsichten" />
						<svws-ui-radio-option :value="2" v-model="gruppe1" name="Unterrichte" label="Unterrichte und Pausenzeiten" />
					</svws-ui-radio-group>
				</div>
				<div class="text-left">
					<svws-ui-checkbox v-model="option8">Fachkürzel statt Fachbezeichnung verwenden</svws-ui-checkbox>
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
	import type { StundenplanListeEintrag, ApiFile } from '@core';
	import { DateUtils, ReportingParameter, ReportingReportvorlage, ArrayList } from '@core';

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
	const gruppe1 = ref<0 | 1 | 2>(0);
	const option8 = ref(false);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined) {
			return;
		}
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getBezeichnung();
		reportingParameter.einzelausgabeDetaildaten = false;
		reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_V_LEHRER_STUNDENPLAN.getVorlageParameterList());
		for (const vp of reportingParameter.vorlageParameter) {
			switch (vp.name) {
				case "mitPausenaufsichten":
					vp.wert = (gruppe1.value === 1).toString();
					break;
				case "mitPausenzeiten":
					vp.wert = (gruppe1.value === 2).toString();
					break;
				case "mitFachkuerzelStattFachbezeichnung":
					vp.wert = option8.value.toString();
					break;
			}
		}
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

	const wochentag = ['So.', 'Mo.', 'Di.', 'Mi.', 'Do.', 'Fr.', 'Sa.', 'So.'];

	function toDateStr(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return wochentag[date[3] % 7] + " " + date[2] + "." + date[1] + "." + date[0];
	}

	function toKW(iso: string): string {
		const date = DateUtils.extractFromDateISO8601(iso);
		return "" + date[5];
	}

</script>
