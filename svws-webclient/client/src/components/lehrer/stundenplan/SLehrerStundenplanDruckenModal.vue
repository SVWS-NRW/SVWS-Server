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
				<div class="flex flex-col gap-4">
					<svws-ui-radio-group>
						<svws-ui-radio-option :value="0" v-model="gruppe1" name="Unterrichte" label="Unterrichte" />
						<svws-ui-radio-option :value="1" v-model="gruppe1" name="Unterrichte" label="Unterrichte mit Pausenaufsichten" />
						<svws-ui-radio-option :value="2" v-model="gruppe1" name="Unterrichte" label="Unterrichte mit Pausenzeiten" />
					</svws-ui-radio-group>
					<svws-ui-radio-group>
						<svws-ui-radio-option :value="0" v-model="gruppe2" name="Ausgabe" label="Gesamtausdruck" />
						<svws-ui-radio-option :value="1" v-model="gruppe2" name="Ausgabe" label="Einzelausdruck" />
						<svws-ui-radio-option :value="2" v-model="gruppe2" name="Ausgabe" label="Kombinierter Ausdruck" />
					</svws-ui-radio-group>
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
	import { ref, watchEffect } from 'vue';
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
	const gruppe1 = ref<0|1|2>(0);
	const gruppe2 = ref<0|1|2>(0);

	async function downloadPDF() {
		if (stundenplanAuswahl.value === undefined)
			return;
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		if (gruppe2.value === 2)
			reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN_KOMBINIERT.getBezeichnung();
		else
			reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_LEHRER_STUNDENPLAN.getBezeichnung();
		if (gruppe2.value === 1)
			reportingParameter.einzelausgabeDetaildaten = true;
		reportingParameter.detailLevel = gruppe1.value;
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
