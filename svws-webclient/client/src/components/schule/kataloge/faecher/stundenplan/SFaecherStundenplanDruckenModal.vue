<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small">
		<template #modalTitle>Stundenplan drucken</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<div class="text-left">
					<svws-ui-checkbox v-model="option2">Pausenzeiten anzeigen</svws-ui-checkbox><br>
				</div>
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="downloadPDF" :is-loading="loading">
				<svws-ui-spinner v-if="loading" spinning />
				Drucken
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';
	import type { ApiFile, StundenplanManager } from '@core';
	import { ArrayList } from '@core';
	import { ReportingParameter, ReportingReportvorlage } from '@core';

	const props = defineProps<{
		getPDF: (parameter: ReportingParameter) => Promise<ApiFile>;
		apiStatus: ApiStatus;
		manager: StundenplanManager;
		id: number;
	}>();

	const show = ref<boolean>(false);

	function openModal() {
		show.value = true;
	}

	const loading = ref<boolean>(false);
	const option2 = ref(false);

	async function downloadPDF() {
		loading.value = true;
		const reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlage = ReportingReportvorlage.STUNDENPLANUNG_v_FACH_STUNDENPLAN.getBezeichnung();
		reportingParameter.idsHauptdaten.add(props.manager.stundenplanGetID());
		reportingParameter.idsDetaildaten.add(props.id);
		reportingParameter.einzelausgabeDetaildaten = false;
		reportingParameter.vorlageParameter = new ArrayList(ReportingReportvorlage.STUNDENPLANUNG_v_FACH_STUNDENPLAN.getVorlageParameterList());
		for (const vp of reportingParameter.vorlageParameter) {
			if (vp.name === "mitPausenzeiten") {
				vp.wert = option2.value.toString();
			}
		}
		const { data, name } = await props.getPDF(reportingParameter);
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
		loading.value = false;
		show.value = false;
	}

</script>
