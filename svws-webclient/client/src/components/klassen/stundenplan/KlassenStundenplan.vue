<template>
	<Teleport defer to=".svws-ui-header--actions">
		<stundenplan-drucken-modal :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_KLASSEN_STUNDENPLAN" v-slot="{ openModal }"
			:ids-hauptdaten="[id]" :map-stundenplaene :api-status>
			<svws-ui-button @click="openModal" type="secondary"><span class="icon i-ri-printer-line" /> Stundenplan drucken</svws-ui-button>
		</stundenplan-drucken-modal>
		<svws-ui-modal-hilfe> <hilfe-klassen-stundenplan /> </svws-ui-modal-hilfe>
	</Teleport>
	<div class="page page-flex-col overflow-x-auto">
		<template v-if="stundenplan() === undefined">
			<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-50 text-center">
				<span class="icon-xxl i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</template>
		<template v-else>
			<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp
				:kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan autofocus />
			<stundenplan-klassen :id :ignore-empty :manager :wochentyp :kalenderwoche />
		</template>
	</div>
</template>

<script setup lang="ts">

	import { ReportingReportvorlage } from '@core/core/types/reporting/ReportingReportvorlage';
	import type { KlassenStundenplanProps } from './KlassenStundenplanProps';

	defineProps<KlassenStundenplanProps>();

</script>
