<template>
	<template v-if="stundenplan() !== undefined">
		<Teleport to=".svws-ui-header--actions" defer>
			<svws-ui-button @click="show = true" type="secondary"><span class="icon i-ri-printer-line" /> Stundenplan drucken</svws-ui-button>
			<svws-ui-modal-hilfe> <hilfe-faecher-stundenplan /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="page page-flex-col overflow-x-auto">
			<template v-if="stundenplan() === undefined">
				<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-50 text-center">
					<span class="icon-xxl i-ri-calendar-event-line" />
					<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
				</div>
			</template>
			<template v-else>
				<stundenplan-auswahl :stundenplan="stundenplan()" :map-stundenplaene :goto-stundenplan :goto-wochentyp :goto-kalenderwoche :manager :wochentyp :kalenderwoche :ganzer-stundenplan :set-ganzer-stundenplan />
				<stundenplan-fach :id :ignore-empty :manager :wochentyp :kalenderwoche />
				<svws-ui-modal v-model:show="show" size="medium">
					<template #modalTitle>Stundenplan drucken</template>
					<template #modalContent>
						<report-parameters :reportvorlage="ReportingReportvorlage.STUNDENPLANUNG_V_FACH_STUNDENPLAN"
							:id-hauptdaten-objekt="stundenplan()?.id" :ids-hauptdaten="[id]" :ids-detaildaten="[]" />
					</template>
				</svws-ui-modal>
			</template>
		</div>
	</template>
	<template v-else>
		<div class="page">
			<div class="flex flex-col gap-2 justify-center items-center min-h-full w-full grow text-headline-md text-ui-50 text-center">
				<span class="icon-xxl i-ri-calendar-event-line" />
				<span>Derzeit liegt kein Stundenplan<br>für diesen Lernabschnitt vor.</span>
			</div>
		</div>
	</template>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { FaecherStundenplanProps } from "./FaecherStundenplanProps";
	import { ReportingReportvorlage } from "@core/core/types/reporting/ReportingReportvorlage";

	defineProps<FaecherStundenplanProps>();
	const show = ref(false);

</script>
