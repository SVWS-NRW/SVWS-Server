<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium">
		<template #modalTitle>Stundenplan drucken</template>
		<template #modalContent>
			<ui-select v-model="stundenplanModel" :manager="stundenplanSelectManager" label="Stundenplan" />
			<report-parameters :reportvorlage :id-hauptdaten-objekt="stundenplanModel?.id ?? -1" :ids-hauptdaten :ids-detaildaten="[]" />
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { StundenplanListeEintrag } from '@core/core/data/stundenplan/StundenplanListeEintrag';
	import type { ReportingReportvorlage } from '@core/core/types/reporting/ReportingReportvorlage';
	import { DateUtils } from '@core/core/utils/DateUtils';
	import { SelectManager } from '@ui/ui/controls/select/manager/SelectManager';
	import { computed, ref } from 'vue';
	import type { ApiStatus } from '~/components/ApiStatus';

	const props = defineProps<{
		mapStundenplaene: Map<number, StundenplanListeEintrag>;
		apiStatus: ApiStatus;
		reportvorlage: ReportingReportvorlage;
		idsHauptdaten: number[];
	}>();

	const show = ref<boolean>(false);

	function openModal() {
		show.value = true;
	}
	const stundenplanAuswahl = ref<StundenplanListeEintrag>();
	const stundenplanModel = computed({
		get: () => {
			if (stundenplanAuswahl.value === undefined) {
				if (props.mapStundenplaene.size > 0) {
					const [first] = props.mapStundenplaene.values();
					return first;
				}
			}
			return stundenplanAuswahl.value;
		},
		set: value => stundenplanAuswahl.value = value,
	});

	const stundenplanOptions = computed(() => props.mapStundenplaene.values());
	const stundenplanSelectManager = new SelectManager({
		options: stundenplanOptions.value,
		optionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
		selectionDisplayText: s => s.bezeichnung.replace('Stundenplan ', '') + ': ' + toDateStr(s.gueltigAb) + '—' + toDateStr(s.gueltigBis) + ' (KW ' + toKW(s.gueltigAb) + '—' + toKW(s.gueltigBis) + ')',
	});

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

<style>
	.modal--content {
		text-align: left !important;
	}
</style>
